package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.factory.AceEnemyFactory;
import edu.hitsz.factory.BossEnemyFactory;
import edu.hitsz.factory.EliteEnemyFactory;
import edu.hitsz.factory.ElitePlusEnemyFactory;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.factory.ItemPoolType;
import edu.hitsz.factory.ItemSimpleFactory;
import edu.hitsz.factory.MobEnemyFactory;
import edu.hitsz.item.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

/**
 * 游戏主面板，游戏启动
 * @author kuma
 */
public class Game extends JPanel {

    private int backGroundTop = 0;

    // Swing 调度器，逻辑与渲染都在 EDT 执行，避免并发访问集合
    private final Timer timer;
    //时间间隔(ms)，控制刷新频率
    private final int timeInterval = 40;

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractItem> items;

    //屏幕中出现的敌机最大数量
    private final int enemyMaxNumber = 5;
    private static final int ENEMY_SPAWN_X_MARGIN = 50;
    private static final double ENEMY_SPAWN_TOP_RATIO = 0.05;

    private static final double MOB_WEIGHT = 0.45;
    private static final double ELITE_WEIGHT = 0.25;
    private static final double ELITE_PLUS_WEIGHT = 0.20;

    private static final int MOB_SCORE = 10;
    private static final int ELITE_SCORE = 20;
    private static final int ELITE_PLUS_SCORE = 30;
    private static final int ACE_SCORE = 50;
    private static final int BOSS_SCORE = 100;
    private static final int BOSS_TRIGGER_SCORE_INTERVAL = 300;
    private static final int BOSS_SPAWN_Y = 80;
    private static final int BOSS_DROP_COUNT = 3;
    private static final int ITEM_SPAWN_MARGIN = 20;
    private static final int BOSS_DROP_X_SPREAD = 120;
    private static final int BOSS_DROP_MIN_X_GAP = 24;
    private static final int BOSS_DROP_Y_JITTER = 6;

    //敌机生成周期
    protected double enemySpawnCycle  =  20;
    private int enemySpawnCounter = 0;

    //英雄机射击周期
    protected double heroShootCycle = 15;
    private int heroShootCounter = 0;
    //敌机射击周期（全局节拍）
    protected double enemyShootCycle = 20;
    private int enemyShootCounter = 0;
    /**
     * 敌机射击周期倍率：>1 更慢，<1 更快。
     */
    private double enemyShootCycleFactor = 1.0;

    //当前玩家分数
    private int score = 0;

    //游戏结束标志
    private boolean gameOverFlag = false;
    // 当前是否存在 Boss（同屏唯一）
    private boolean bossActive = false;
    // 下一次 Boss 触发分数阈值
    private int nextBossTriggerScore = BOSS_TRIGGER_SCORE_INTERVAL;

    private final EnemyFactory mobEnemyFactory = new MobEnemyFactory();
    private final EnemyFactory eliteEnemyFactory = new EliteEnemyFactory();
    private final EnemyFactory elitePlusEnemyFactory = new ElitePlusEnemyFactory();
    private final EnemyFactory aceEnemyFactory = new AceEnemyFactory();
    private final EnemyFactory bossEnemyFactory = new BossEnemyFactory();

    public Game() {
        heroAircraft = HeroAircraft.getInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        items = new LinkedList<>();

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

        this.timer = new Timer(timeInterval, e -> gameCycle());

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        if (!timer.isRunning()) {
            timer.start();
        }

    }

    private void gameCycle() {
        enemySpawnCounter++;
        if (enemySpawnCounter >= enemySpawnCycle) {
            enemySpawnCounter = 0;
            if (shouldSpawnBoss()) {
                spawnBossAction();
            } else if (enemyAircrafts.size() < enemyMaxNumber) {
                spawnCommonEnemyAction();
            }
        }

        // 飞机发射子弹
        shootAction();
        // 子弹移动
        bulletsMoveAction();
        // 飞机移动
        aircraftsMoveAction();
        // 道具移动
        itemsMoveAction();
        // 撞击检测
        crashCheckAction();
        // 后处理
        postProcessAction();
        // 重绘界面
        repaint();
        // 游戏结束检查
        checkResultAction();
    }

    //***********************
    //      Action 各部分
    //***********************

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
     * 按工厂方法模式创建常规敌机并加入场景。
     */
    private void spawnCommonEnemyAction() {
        int locationX = (int) (Math.random() * (Main.WINDOW_WIDTH - ENEMY_SPAWN_X_MARGIN)) + ENEMY_SPAWN_X_MARGIN / 2;
        int locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * ENEMY_SPAWN_TOP_RATIO);
        AbstractAircraft enemyAircraft = createEnemyByWeight(locationX, locationY);
        applyEnemyShootCycleFactor(enemyAircraft);
        enemyAircrafts.add(enemyAircraft);
    }

    private boolean shouldSpawnBoss() {
        return !bossActive && score >= nextBossTriggerScore;
    }

    private void spawnBossAction() {
        int locationX = Main.WINDOW_WIDTH / 2;
        AbstractAircraft bossEnemy = bossEnemyFactory.createEnemy(locationX, BOSS_SPAWN_Y);
        applyEnemyShootCycleFactor(bossEnemy);
        enemyAircrafts.add(bossEnemy);
        bossActive = true;
        nextBossTriggerScore += BOSS_TRIGGER_SCORE_INTERVAL;
    }

    private AbstractAircraft createEnemyByWeight(int locationX, int locationY) {
        double random = Math.random();
        if (random < MOB_WEIGHT) {
            return mobEnemyFactory.createEnemy(locationX, locationY);
        }
        if (random < MOB_WEIGHT + ELITE_WEIGHT) {
            return eliteEnemyFactory.createEnemy(locationX, locationY);
        }
        if (random < MOB_WEIGHT + ELITE_WEIGHT + ELITE_PLUS_WEIGHT) {
            return elitePlusEnemyFactory.createEnemy(locationX, locationY);
        }
        return aceEnemyFactory.createEnemy(locationX, locationY);
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // 敌机子弹攻击英雄机
        for (BaseBullet enemyBullet : enemyBullets) {
            if (enemyBullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(enemyBullet)) {
                heroAircraft.decreaseHp(enemyBullet.getPower());
                enemyBullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        handleEnemyDestroyed(enemyAircraft);
                    }
                }
            }
        }

        // 英雄机 与 敌机 相撞，均损毁
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft.notValid()) {
                continue;
            }
            if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                enemyAircraft.vanish();
                heroAircraft.decreaseHp(Integer.MAX_VALUE);
            }
        }

        // 我方获得道具，道具生效
        for (AbstractItem item : items) {
            if (item.notValid()) {
                continue;
            }
            if (heroAircraft.crash(item)) {
                item.activate(heroAircraft);
                item.vanish();
            }
        }

    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 删除无效的道具
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        items.removeIf(AbstractFlyingObject::notValid);
        bossActive = hasAliveBoss();
    }

    private boolean hasAliveBoss() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft instanceof BossEnemy) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查游戏是否结束，若结束：关闭线程池
     */
    private void checkResultAction(){
        // 游戏结束检查英雄机是否存活
        if (heroAircraft.getHp() <= 0) {
            timer.stop();
            gameOverFlag = true;
            System.out.println("Game Over!");
        }
    };

    //***********************
    //      Paint 各部分
    //***********************
    /**
     * 重写 paint方法
     * 通过重复调用paint方法，实现游戏动画
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        paintImageWithPositionRevised(g, enemyAircrafts);

        paintImageWithPositionRevised(g, items);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.isEmpty()) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(Color.RED);
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE: " + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE: " + this.heroAircraft.getHp(), x, y);
    }

    private void handleEnemyDestroyed(AbstractAircraft enemyAircraft) {
        if (enemyAircraft instanceof BossEnemy) {
            score += BOSS_SCORE;
            bossActive = false;
            dropBossItems(enemyAircraft);
            return;
        }
        if (enemyAircraft instanceof AceEnemy) {
            score += ACE_SCORE;
        } else if (enemyAircraft instanceof ElitePlusEnemy) {
            score += ELITE_PLUS_SCORE;
        } else if (enemyAircraft instanceof EliteEnemy) {
            score += ELITE_SCORE;
        } else {
            score += MOB_SCORE;
        }

        AbstractItem droppedItem = enemyAircraft.tryGenerateDropItem();
        if (droppedItem != null) {
            items.add(droppedItem);
        }
    }

    /**
     * Boss 阵亡后，沿水平方向随机分散生成道具，避免重叠在同一坐标。
     */
    private void dropBossItems(AbstractAircraft bossEnemy) {
        int centerX = bossEnemy.getLocationX();
        int centerY = bossEnemy.getLocationY();
        int[] dropXs = new int[BOSS_DROP_COUNT];
        for (int i = 0; i < BOSS_DROP_COUNT; i++) {
            int dropX;
            int attempts = 0;
            do {
                int offsetX = (int) Math.round((Math.random() * 2 - 1) * BOSS_DROP_X_SPREAD);
                dropX = clampToWindow(centerX + offsetX, ITEM_SPAWN_MARGIN, Main.WINDOW_WIDTH - ITEM_SPAWN_MARGIN);
                attempts++;
            } while (hasCloseDropX(dropXs, i, dropX) && attempts < 10);
            dropXs[i] = dropX;

            int yJitter = (int) Math.round((Math.random() * 2 - 1) * BOSS_DROP_Y_JITTER);
            int dropY = clampToWindow(centerY + yJitter, ITEM_SPAWN_MARGIN, Main.WINDOW_HEIGHT - ITEM_SPAWN_MARGIN);
            items.add(ItemSimpleFactory.createItem(ItemPoolType.ACE_POOL, dropX, dropY));
        }
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

    /**
     * 统一设置当前场上及后续生成敌机的射击周期倍率。
     * @param enemyShootCycleFactor >0；值越大射击越慢
     */
    public void setEnemyShootCycleFactor(double enemyShootCycleFactor) {
        if (enemyShootCycleFactor <= 0) {
            return;
        }
        this.enemyShootCycleFactor = enemyShootCycleFactor;
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.setShootCycleFactor(enemyShootCycleFactor);
        }
    }

    private void applyEnemyShootCycleFactor(AbstractAircraft enemyAircraft) {
        enemyAircraft.setShootCycleFactor(enemyShootCycleFactor);
    }

    /**
     * 设置英雄机全局射击周期。
     * @param heroShootCycle >0；值越大射击越慢
     */
    public void setHeroShootCycle(double heroShootCycle) {
        if (heroShootCycle <= 0) {
            return;
        }
        this.heroShootCycle = heroShootCycle;
        this.heroShootCounter = 0;
    }

    /**
     * 设置敌机全局射击周期。
     * @param enemyShootCycle >0；值越大射击越慢
     */
    public void setEnemyShootCycle(double enemyShootCycle) {
        if (enemyShootCycle <= 0) {
            return;
        }
        this.enemyShootCycle = enemyShootCycle;
        this.enemyShootCounter = 0;
    }

}
