package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.AceEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.dao.GameDifficulty;
import edu.hitsz.factory.AceEnemyFactory;
import edu.hitsz.factory.BossEnemyFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.ElitePlusEnemyFactory;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.factory.ItemPoolType;
import edu.hitsz.factory.ItemSimpleFactory;
import edu.hitsz.factory.MobEnemyFactory;
import edu.hitsz.item.AbstractItem;
import edu.hitsz.item.BombItem;
import edu.hitsz.item.FreezeItem;
import edu.hitsz.item.ItemEffectObserver;
import edu.hitsz.application.audio.AudioManager;
import edu.hitsz.application.mode.GameOverInfo;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * 游戏主面板模板基类。
 * 使用模板方法模式固化单帧执行骨架，不同难度仅覆盖参数初始化与难度递进逻辑。
 *
 * @author kuma
 */
public abstract class Game extends JPanel {
    private static final int TIME_INTERVAL = 40;
    private static final int ENEMY_SPAWN_X_MARGIN = 50;
    private static final double ENEMY_SPAWN_TOP_RATIO = 0.05;
    private static final int BOSS_SPAWN_Y = 80;
    private static final int BOSS_DROP_COUNT = 3;
    private static final int ITEM_SPAWN_MARGIN = 20;
    private static final int BOSS_DROP_X_SPREAD = 120;
    private static final int BOSS_DROP_MIN_X_GAP = 24;
    private static final int BOSS_DROP_Y_JITTER = 6;
    private static final int MOB_SCORE = 10;
    private static final int ELITE_SCORE = 20;
    private static final int ELITE_PLUS_SCORE = 30;
    private static final int ACE_SCORE = 50;
    private static final int BOSS_SCORE = 100;
    private static final int COMBO_HIT_EXPIRE_FRAMES = 45;
    private static final int COMBO_HEAL_STEP = 10;
    private static final int COMBO_HEAL_AMOUNT = 10;
    private static final int COMBO_OVERLAY_EXPIRE_FRAMES = 30;
    private static final int COMBO_REWARD_EXPIRE_FRAMES = 45;
    private int backGroundTop = 0;
    private final Timer timer;
    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractItem> items;
    private final EnemyFactory mobEnemyFactory = new MobEnemyFactory();
    private final EnemyFactory eliteEnemyFactory = new EliteEnemyFactory();
    private final EnemyFactory elitePlusEnemyFactory = new ElitePlusEnemyFactory();
    private final EnemyFactory aceEnemyFactory = new AceEnemyFactory();
    private final EnemyFactory bossEnemyFactory = new BossEnemyFactory();
    private final GameDifficulty difficulty;

    private Consumer<GameOverInfo> onGameOver;

    private int enemySpawnCounter = 0;
    private int heroShootCounter = 0;
    private int enemyShootCounter = 0;
    private int frameCount = 0;
    private int score = 0;
    private int comboCount = 0;
    private int comboExpireFrame = -1;
    private int comboScoreRewardExpireFrame = -1;
    private int comboHpRewardExpireFrame = -1;
    private int bossSpawnCount = 0;
    private boolean gameOverFlag = false;
    private boolean gameOverHandled = false;
    private boolean bossActive = false;
    private int nextBossTriggerScore = Integer.MAX_VALUE;
    private String comboScoreRewardText = "";
    private String comboHpRewardText = "";

    protected int enemyMaxNumber = 5;
    protected double heroShootCycle = 15;
    protected double enemyShootCycle = 20;
    protected double enemySpawnCycle = 20;
    protected double enemyHpScale = 1.0;
    protected double enemySpeedScale = 1.0;
    protected double mobWeight = 0.45;
    protected double eliteWeight = 0.25;
    protected double elitePlusWeight = 0.20;
    protected double aceWeight = 0.10;
    protected boolean bossEnabled = true;
    protected int bossThreshold = 300;

    protected Game(GameSettings settings) {
        GameSettings safeSettings = settings == null
                ? new GameSettings(GameDifficulty.NORMAL)
                : settings;
        this.difficulty = safeSettings.getDifficulty();
        ImageManager.setBackgroundByDifficulty(safeSettings.getDifficulty());
        this.heroAircraft = HeroAircraft.getInstance();
        this.heroAircraft.prepareForNewGame();
        this.enemyAircrafts = new LinkedList<>();
        this.heroBullets = new LinkedList<>();
        this.enemyBullets = new LinkedList<>();
        this.items = new LinkedList<>();
        setPreferredSize(new java.awt.Dimension(GameViewport.getWidth(), GameViewport.getHeight()));
        setSize(GameViewport.getWidth(), GameViewport.getHeight());
        new HeroController(this, heroAircraft);
        this.timer = new Timer(TIME_INTERVAL, e -> action());
    }

    /**
     * 启动游戏调度器与背景音乐。
     */
    public final void start() {
        if (!timer.isRunning()) {
            AudioManager.playBgm();
            timer.start();
        }
    }

    /**
     * 单帧模板方法。
     */
    public final void action() {
        if (gameOverFlag) {
            return;
        }
        refreshComboState();
        spawnEnemyAction();
        shootAction();
        bulletsMoveAction();
        aircraftsMoveAction();
        itemsMoveAction();
        crashCheckAction();
        postProcessAction();
        progressDifficultyAction();
        repaint();
        checkResultAction();
    }

    public void setOnGameOver(Consumer<GameOverInfo> onGameOver) {
        this.onGameOver = onGameOver;
    }

    /**
     * 子类初始化固定参数。
     */
    protected abstract void initializeDifficulty();

    /**
     * 子类控制难度递进。
     */
    protected abstract void progressDifficulty();

    /**
     * Boss 血量额外奖励，默认不增加。
     *
     * @param spawnIndex 第几次生成 Boss，从 1 开始
     * @return 额外生命值
     */
    protected int getBossHpBonusPerSpawn(int spawnIndex) {
        return 0;
    }

    protected final void finalizeDifficultyInitialization() {
        nextBossTriggerScore = bossEnabled ? bossThreshold : Integer.MAX_VALUE;
    }

    private void spawnEnemyAction() {
        enemySpawnCounter++;
        if (enemySpawnCounter < enemySpawnCycle) {
            return;
        }
        enemySpawnCounter = 0;
        if (shouldSpawnBoss()) {
            spawnBossAction();
            return;
        }
        if (enemyAircrafts.size() < enemyMaxNumber) {
            spawnCommonEnemyAction();
        }
    }

    private void shootAction() {
        heroShootCounter++;
        enemyShootCounter++;

        if (heroShootCounter >= heroShootCycle) {
            heroShootCounter = 0;
            heroBullets.addAll(heroAircraft.shoot());
        }

        if (enemyShootCounter >= enemyShootCycle) {
            enemyShootCounter = 0;
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.canShootNow()) {
                    enemyBullets.addAll(enemyAircraft.shoot());
                }
            }
        }
    }

    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void itemsMoveAction() {
        for (AbstractItem item : items) {
            item.forward();
        }
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        for (BaseBullet enemyBullet : enemyBullets) {
            if (enemyBullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(enemyBullet)) {
                heroAircraft.decreaseHp(enemyBullet.getPower());
                enemyBullet.vanish();
            }
        }

        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    registerHeroBulletHit();
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    AudioManager.playBulletHit();
                    if (enemyAircraft.notValid()) {
                        handleEnemyDestroyed(enemyAircraft);
                    }
                }
            }
        }

        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft.notValid()) {
                continue;
            }
            if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                enemyAircraft.vanish();
                heroAircraft.decreaseHp(Integer.MAX_VALUE);
                resetCombo();
            }
        }

        for (AbstractItem item : items) {
            if (item.notValid()) {
                continue;
            }
            if (heroAircraft.crash(item)) {
                AudioManager.playGetSupply();
                activateItem(item);
                item.vanish();
            }
        }
    }

    private void activateItem(AbstractItem item) {
        registerObserversForItem(item);
        item.activate(heroAircraft);
        item.clearObservers();
        if (item instanceof BombItem) {
            AudioManager.playBombExplosion();
            settleBombDestroyedEnemies();
        }
    }

    private void registerObserversForItem(AbstractItem item) {
        if (!(item instanceof BombItem) && !(item instanceof FreezeItem)) {
            return;
        }
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (!enemyAircraft.notValid()) {
                item.addObserver(enemyAircraft);
            }
        }
        for (BaseBullet enemyBullet : enemyBullets) {
            if (!enemyBullet.notValid() && enemyBullet instanceof ItemEffectObserver observer) {
                item.addObserver(observer);
            }
        }
    }

    private void settleBombDestroyedEnemies() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft.consumeBombDestroyedFlag()) {
                handleEnemyDestroyed(enemyAircraft);
            }
        }
    }

    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        items.removeIf(AbstractFlyingObject::notValid);
        bossActive = hasAliveBoss();
    }

    private void progressDifficultyAction() {
        frameCount++;
        progressDifficulty();
    }

    private boolean hasAliveBoss() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft instanceof BossEnemy) {
                return true;
            }
        }
        return false;
    }

    private void checkResultAction() {
        if (heroAircraft.getHp() > 0) {
            return;
        }
        timer.stop();
        gameOverFlag = true;
        AudioManager.stopAllLoopingMusic();
        AudioManager.playGameOver();
        System.out.println("Game Over!");
        if (!gameOverHandled && onGameOver != null) {
            gameOverHandled = true;
            onGameOver.accept(new GameOverInfo(difficulty, score));
        }
    }

    private void spawnCommonEnemyAction() {
        int locationX = (int) (Math.random() * (GameViewport.getWidth() - ENEMY_SPAWN_X_MARGIN)) + ENEMY_SPAWN_X_MARGIN / 2;
        int locationY = (int) (Math.random() * GameViewport.getHeight() * ENEMY_SPAWN_TOP_RATIO);
        AbstractAircraft enemyAircraft = createEnemyByWeight(locationX, locationY);
        configureEnemyOnSpawn(enemyAircraft);
        enemyAircrafts.add(enemyAircraft);
    }

    private boolean shouldSpawnBoss() {
        return bossEnabled && !bossActive && score >= nextBossTriggerScore;
    }

    private void spawnBossAction() {
        AbstractAircraft bossEnemy = bossEnemyFactory.createEnemy(GameViewport.getWidth() / 2, BOSS_SPAWN_Y);
        configureEnemyOnSpawn(bossEnemy);
        bossSpawnCount++;
        int scaledBossHp = bossEnemy.getMaxHp() + getBossHpBonusPerSpawn(bossSpawnCount);
        bossEnemy.resetHp(scaledBossHp);
        enemyAircrafts.add(bossEnemy);
        bossActive = true;
        nextBossTriggerScore += bossThreshold;
        AudioManager.playBossBgm();
    }

    private void configureEnemyOnSpawn(AbstractAircraft enemyAircraft) {
        enemyAircraft.resetHp(scaleHp(enemyAircraft, enemyAircraft.getMaxHp()));
        enemyAircraft.scaleSpeed(enemySpeedScale);
    }

    /**
     * 普通敌机始终保持一发击毁，避免难度提升直接损害基础爽感。
     */
    private int scaleHp(AbstractAircraft enemyAircraft, int baseHp) {
        if (enemyAircraft instanceof MobEnemy) {
            return baseHp;
        }
        return Math.max(1, (int) Math.round(baseHp * enemyHpScale));
    }

    private AbstractAircraft createEnemyByWeight(int locationX, int locationY) {
        double random = Math.random();
        if (random < mobWeight) {
            return mobEnemyFactory.createEnemy(locationX, locationY);
        }
        if (random < mobWeight + eliteWeight) {
            return eliteEnemyFactory.createEnemy(locationX, locationY);
        }
        if (random < mobWeight + eliteWeight + elitePlusWeight) {
            return elitePlusEnemyFactory.createEnemy(locationX, locationY);
        }
        return aceEnemyFactory.createEnemy(locationX, locationY);
    }

    private void handleEnemyDestroyed(AbstractAircraft enemyAircraft) {
        if (enemyAircraft instanceof BossEnemy) {
            registerEnemyDestroyed(BOSS_SCORE);
            bossActive = false;
            dropBossItems(enemyAircraft);
            if (!gameOverFlag) {
                AudioManager.resumeNormalBgm();
            }
            return;
        }
        if (enemyAircraft instanceof AceEnemy) {
            registerEnemyDestroyed(ACE_SCORE);
        } else if (enemyAircraft instanceof ElitePlusEnemy) {
            registerEnemyDestroyed(ELITE_PLUS_SCORE);
        } else if (enemyAircraft instanceof EliteEnemy) {
            registerEnemyDestroyed(ELITE_SCORE);
        } else if (enemyAircraft instanceof MobEnemy) {
            registerEnemyDestroyed(MOB_SCORE);
        }

        if (!enemyAircraft.isDropDisabled()) {
            AbstractItem droppedItem = enemyAircraft.tryGenerateDropItem();
            if (droppedItem != null) {
                items.add(droppedItem);
            }
        }
    }

    private void dropBossItems(AbstractAircraft bossEnemy) {
        int centerX = bossEnemy.getLocationX();
        int centerY = bossEnemy.getLocationY();
        int[] dropXs = new int[BOSS_DROP_COUNT];
        for (int i = 0; i < BOSS_DROP_COUNT; i++) {
            int dropX;
            int attempts = 0;
            do {
                int offsetX = (int) Math.round((Math.random() * 2 - 1) * BOSS_DROP_X_SPREAD);
                dropX = clampToWindow(centerX + offsetX, ITEM_SPAWN_MARGIN, GameViewport.getWidth() - ITEM_SPAWN_MARGIN);
                attempts++;
            } while (hasCloseDropX(dropXs, i, dropX) && attempts < 10);
            dropXs[i] = dropX;

            int yJitter = (int) Math.round((Math.random() * 2 - 1) * BOSS_DROP_Y_JITTER);
            int dropY = clampToWindow(centerY + yJitter, ITEM_SPAWN_MARGIN, GameViewport.getHeight() - ITEM_SPAWN_MARGIN);
            items.add(ItemSimpleFactory.createItem(ItemPoolType.ACE_POOL, dropX, dropY));
        }
    }

    private void registerEnemyDestroyed(int baseScore) {
        int finalScore = applyComboMultiplier(baseScore);
        score += finalScore;
        showScoreReward(baseScore, finalScore);
    }

    /**
     * 连击判定以英雄机子弹命中敌机为准。
     * 命中后刷新连击持续时间，并在达到阶段时发放回血奖励。
     */
    private void registerHeroBulletHit() {
        if (comboCount > 0 && frameCount <= comboExpireFrame) {
            comboCount++;
        } else {
            comboCount = 1;
        }
        comboExpireFrame = frameCount + COMBO_HIT_EXPIRE_FRAMES;
        if (comboCount % COMBO_HEAL_STEP == 0) {
            heroAircraft.increaseHp(COMBO_HEAL_AMOUNT);
            showHpReward("HP +" + COMBO_HEAL_AMOUNT);
        }
    }

    private int applyComboMultiplier(int baseScore) {
        return (int) Math.round(baseScore * getComboMultiplier());
    }

    private double getComboMultiplier() {
        if (comboCount >= 20) {
            return 2.0;
        }
        if (comboCount >= 10) {
            return 1.5;
        }
        if (comboCount >= 5) {
            return 1.2;
        }
        return 1.0;
    }

    private void refreshComboState() {
        if (comboCount > 0 && frameCount > comboExpireFrame) {
            resetCombo();
        }
    }

    private void resetCombo() {
        comboCount = 0;
        comboExpireFrame = -1;
    }

    private void showScoreReward(int baseScore, int finalScore) {
        if (finalScore <= baseScore) {
            return;
        }
        comboScoreRewardText = "SCORE +" + (finalScore - baseScore) + "  x" + formatMultiplier(getComboMultiplier());
        comboScoreRewardExpireFrame = frameCount + COMBO_REWARD_EXPIRE_FRAMES;
    }

    private void showHpReward(String rewardText) {
        if (rewardText == null || rewardText.isEmpty()) {
            return;
        }
        comboHpRewardText = rewardText;
        comboHpRewardExpireFrame = frameCount + COMBO_REWARD_EXPIRE_FRAMES;
    }

    private String formatMultiplier(double multiplier) {
        return String.format(Locale.ROOT, "%.1f", multiplier);
    }

    private boolean hasCloseDropX(int[] dropXs, int size, int candidateX) {
        for (int i = 0; i < size; i++) {
            if (Math.abs(dropXs[i] - candidateX) < BOSS_DROP_MIN_X_GAP) {
                return true;
            }
        }
        return false;
    }

    private int clampToWindow(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    protected final boolean reachedFrameInterval(int interval) {
        return interval > 0 && frameCount > 0 && frameCount % interval == 0;
    }

    protected final void printDifficultyUpgrade(String message) {
        System.out.println("[" + difficulty.name() + "] " + message);
    }

    protected final String describePercentIncrease(double currentScale) {
        int percent = (int) Math.round((currentScale - 1.0) * 100);
        if (percent <= 0) {
            return "保持当前倍率";
        }
        return "提升 " + percent + "%";
    }

    protected final void setEnemySpawnCycle(double enemySpawnCycle) {
        if (enemySpawnCycle > 0) {
            this.enemySpawnCycle = enemySpawnCycle;
            this.enemySpawnCounter = 0;
        }
    }

    protected final void setHeroShootCycle(double heroShootCycle) {
        if (heroShootCycle > 0) {
            this.heroShootCycle = heroShootCycle;
            this.heroShootCounter = 0;
        }
    }

    protected final void setEnemyShootCycle(double enemyShootCycle) {
        if (enemyShootCycle > 0) {
            this.enemyShootCycle = enemyShootCycle;
            this.enemyShootCounter = 0;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, backGroundTop - GameViewport.getHeight(), null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, backGroundTop, null);
        backGroundTop += 1;
        if (backGroundTop == GameViewport.getHeight()) {
            backGroundTop = 0;
        }

        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);
        paintImageWithPositionRevised(g, items);
        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);
        paintScoreAndLife(g);
        paintComboOverlay(g);
    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image!";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE: " + score, x, y);
        y += 20;
        g.drawString("LIFE: " + heroAircraft.getHp(), x, y);
        if (comboCount > 1) {
            y += 20;
            g.drawString("COMBO: x" + comboCount, x, y);
        }
    }

    private void paintComboOverlay(Graphics g) {
        if (!(g instanceof Graphics2D graphics2D)) {
            return;
        }
        graphics2D.setFont(new Font("SansSerif", Font.BOLD, 20));

        if (!comboScoreRewardText.isEmpty() && frameCount <= comboScoreRewardExpireFrame) {
            drawRightAlignedHudText(graphics2D, comboScoreRewardText, 25, new Color(255, 140, 0));
        } else {
            comboScoreRewardText = "";
        }

        if (!comboHpRewardText.isEmpty() && frameCount <= comboHpRewardExpireFrame) {
            drawRightAlignedHudText(graphics2D, comboHpRewardText, 47, new Color(255, 99, 71));
        } else {
            comboHpRewardText = "";
        }
    }

    private void drawRightAlignedHudText(Graphics2D graphics2D, String text, int baselineY, Color color) {
        graphics2D.setColor(color);
        int textWidth = graphics2D.getFontMetrics().stringWidth(text);
        int x = Math.max(20, GameViewport.getWidth() - textWidth - 12);
        graphics2D.drawString(text, x, baselineY);
    }

    public final GameDifficulty getDifficulty() {
        return difficulty;
    }

    public final int getEnemyMaxNumber() {
        return enemyMaxNumber;
    }

    public final double getHeroShootCycle() {
        return heroShootCycle;
    }

    public final double getEnemyShootCycle() {
        return enemyShootCycle;
    }

    public final double getEnemySpawnCycle() {
        return enemySpawnCycle;
    }

    public final double getEnemyHpScale() {
        return enemyHpScale;
    }

    public final double getEnemySpeedScale() {
        return enemySpeedScale;
    }

    public final boolean isBossEnabled() {
        return bossEnabled;
    }

    public final int getBossThreshold() {
        return bossThreshold;
    }

    public final int getScore() {
        return score;
    }

    public final int getBossSpawnCount() {
        return bossSpawnCount;
    }

    final int getComboCount() {
        return comboCount;
    }

    public final List<AbstractAircraft> getEnemyAircrafts() {
        return enemyAircrafts;
    }

    public final List<BaseBullet> getEnemyBullets() {
        return enemyBullets;
    }

    public final List<AbstractItem> getItems() {
        return items;
    }

    final void debugSetScore(int score) {
        this.score = Math.max(0, score);
    }

    final void debugForceSpawnBoss() {
        if (bossEnabled && !bossActive) {
            spawnBossAction();
        }
    }

    final void debugPostProcess() {
        postProcessAction();
    }

    final void debugActivateItem(AbstractItem item) {
        activateItem(item);
    }

    final void debugConfigureEnemyOnSpawn(AbstractAircraft enemyAircraft) {
        configureEnemyOnSpawn(enemyAircraft);
    }

    final void debugRegisterHeroBulletHit() {
        registerHeroBulletHit();
    }

    final void debugRegisterEnemyDestroyed(int baseScore) {
        registerEnemyDestroyed(baseScore);
    }
}
