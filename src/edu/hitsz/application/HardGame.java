package edu.hitsz.application;

import edu.hitsz.dao.GameDifficulty;

/**
 * 困难难度游戏，支持更强的递进与 Boss 血量增长。
 */
public class HardGame extends Game {
    private static final int UPGRADE_INTERVAL = 250;

    public HardGame() {
        super(GameDifficulty.HARD);
        initializeDifficulty();
        finalizeDifficultyInitialization();
    }

    @Override
    protected void initializeDifficulty() {
        enemyMaxNumber = 6;
        mobWeight = 0.30;
        eliteWeight = 0.25;
        elitePlusWeight = 0.25;
        aceWeight = 0.20;
        setHeroShootCycle(14);
        setEnemyShootCycle(19);
        setEnemySpawnCycle(16);
        enemyHpScale = 1.20;
        enemySpeedScale = 1.05;
        bossEnabled = true;
        bossThreshold = 250;
    }

    @Override
    protected void progressDifficulty() {
        if (!reachedFrameInterval(UPGRADE_INTERVAL)) {
            return;
        }
        setEnemySpawnCycle(Math.max(8, getEnemySpawnCycle() - 1));
        setEnemyShootCycle(Math.max(10, getEnemyShootCycle() - 1));
        setHeroShootCycle(Math.min(22, getHeroShootCycle() + 1));
        enemyHpScale += 0.10;
        enemySpeedScale += 0.05;
        printDifficultyUpgrade(
                "战场压力升级：敌机刷新更频繁，生成周期缩短到 "
                        + (int) getEnemySpawnCycle()
                        + " 帧；敌机射击间隔缩短到 "
                        + (int) getEnemyShootCycle()
                        + " 帧。为保留前期手感，普通敌机仍保持一发击毁；精英及以上敌机生命值"
                        + describePercentIncrease(enemyHpScale)
                        + "，飞行速度"
                        + describePercentIncrease(enemySpeedScale)
                        + "；英雄机射击周期调整为 "
                        + (int) getHeroShootCycle()
                        + " 帧。"
        );
    }

    @Override
    protected int getBossHpBonusPerSpawn(int spawnIndex) {
        return 50 * Math.max(0, spawnIndex - 1);
    }
}
