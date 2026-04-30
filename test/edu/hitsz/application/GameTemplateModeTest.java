package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.item.BombItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 模板方法模式：不同难度配置与递进测试。
 */
class GameTemplateModeTest {

    @Test
    @DisplayName("Easy mode does not spawn boss")
    void shouldNotSpawnBossInEasyMode() {
        EasyGame game = new EasyGame();
        HeroAircraft.getInstance().prepareForNewGame();
        HeroAircraft.getInstance().setLocation(256, 1500);
        game.debugSetScore(10_000);

        runFrames(game, 120);

        assertTrue(game.getEnemyAircrafts().stream().noneMatch(BossEnemy.class::isInstance));
        assertFalse(game.isBossEnabled());
    }

    @Test
    @DisplayName("Normal and hard mode use configured initial parameters")
    void shouldUseConfiguredInitialParameters() {
        NormalGame normalGame = new NormalGame();
        HardGame hardGame = new HardGame();

        assertEquals(5, normalGame.getEnemyMaxNumber());
        assertEquals(15.0, normalGame.getHeroShootCycle());
        assertEquals(20.0, normalGame.getEnemyShootCycle());
        assertEquals(20.0, normalGame.getEnemySpawnCycle());
        assertEquals(1.0, normalGame.getEnemyHpScale());
        assertEquals(1.0, normalGame.getEnemySpeedScale());
        assertEquals(300, normalGame.getBossThreshold());

        assertEquals(6, hardGame.getEnemyMaxNumber());
        assertEquals(14.0, hardGame.getHeroShootCycle());
        assertEquals(19.0, hardGame.getEnemyShootCycle());
        assertEquals(16.0, hardGame.getEnemySpawnCycle());
        assertEquals(1.2, hardGame.getEnemyHpScale());
        assertEquals(1.05, hardGame.getEnemySpeedScale());
        assertEquals(250, hardGame.getBossThreshold());
    }

    @Test
    @DisplayName("Difficulty progression changes parameters by mode")
    void shouldProgressDifficultyByMode() {
        NormalGame normalGame = new NormalGame();
        HardGame hardGame = new HardGame();
        HeroAircraft heroAircraft = HeroAircraft.getInstance();
        heroAircraft.prepareForNewGame();
        heroAircraft.setLocation(256, 1500);

        runFrames(normalGame, 300);
        runFrames(hardGame, 250);

        assertEquals(19.0, normalGame.getEnemySpawnCycle());
        assertEquals(20.0, normalGame.getEnemyShootCycle());
        assertEquals(15.0, normalGame.getHeroShootCycle());
        assertEquals(1.10, normalGame.getEnemyHpScale(), 1e-9);
        assertEquals(1.10, normalGame.getEnemySpeedScale(), 1e-9);

        assertEquals(15.0, hardGame.getEnemySpawnCycle());
        assertEquals(18.0, hardGame.getEnemyShootCycle());
        assertEquals(15.0, hardGame.getHeroShootCycle());
        assertEquals(1.30, hardGame.getEnemyHpScale(), 1e-9);
        assertEquals(1.10, hardGame.getEnemySpeedScale(), 1e-9);
    }

    @Test
    @DisplayName("Hard boss hp scales by spawn count while normal boss hp is stable")
    void shouldIncreaseBossHpOnlyInHardMode() {
        NormalGame normalGame = new NormalGame();
        HardGame hardGame = new HardGame();

        normalGame.debugForceSpawnBoss();
        BossEnemy normalBossFirst = findBoss(normalGame);
        int normalBossHpFirst = normalBossFirst.getHp();
        normalBossFirst.vanish();
        normalGame.debugPostProcess();
        normalGame.debugForceSpawnBoss();
        BossEnemy normalBossSecond = findBoss(normalGame);
        int normalBossHpSecond = normalBossSecond.getHp();
        assertEquals(normalBossHpFirst, normalBossHpSecond);

        hardGame.debugForceSpawnBoss();
        BossEnemy hardBossFirst = findBoss(hardGame);
        int hardBossHpFirst = hardBossFirst.getHp();
        hardBossFirst.vanish();
        hardGame.debugPostProcess();
        hardGame.debugForceSpawnBoss();
        BossEnemy hardBossSecond = findBoss(hardGame);
        int hardBossHpSecond = hardBossSecond.getHp();
        assertEquals(hardBossHpFirst + 50, hardBossHpSecond);
    }

    @Test
    @DisplayName("Bomb destroyed enemy gains score and does not drop item")
    void shouldSettleBombScoreWithoutDrop() {
        NormalGame game = new NormalGame();
        HeroAircraft heroAircraft = HeroAircraft.getInstance();
        heroAircraft.prepareForNewGame();
        game.getEnemyAircrafts().add(new edu.hitsz.aircraft.ElitePlusEnemy(120, 120, 2, 7, 60));
        int scoreBefore = game.getScore();

        game.debugActivateItem(new BombItem(100, 100, 0, 0));

        assertEquals(scoreBefore + 30, game.getScore());
        assertTrue(game.getItems().isEmpty());
    }

    @Test
    @DisplayName("Hard mode keeps mob enemy one-shot")
    void shouldKeepMobEnemyOneShotInHardMode() {
        HardGame hardGame = new HardGame();
        MobEnemy mobEnemy = new MobEnemy(100, 100, 0, 10, 30);

        hardGame.debugConfigureEnemyOnSpawn(mobEnemy);

        assertEquals(30, mobEnemy.getHp());
    }

    private void runFrames(Game game, int frameCount) {
        for (int i = 0; i < frameCount; i++) {
            game.action();
        }
    }

    private BossEnemy findBoss(Game game) {
        for (AbstractAircraft enemyAircraft : game.getEnemyAircrafts()) {
            if (enemyAircraft instanceof BossEnemy bossEnemy) {
                return bossEnemy;
            }
        }
        throw new AssertionError("boss not found");
    }
}
