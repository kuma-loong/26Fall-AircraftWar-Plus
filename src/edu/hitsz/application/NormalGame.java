package edu.hitsz.application;

import edu.hitsz.dao.GameDifficulty;

/**
 * 普通难度游戏，仅提升敌机生成速度、血量和速度。
 */
public class NormalGame extends Game {
    private static final int UPGRADE_INTERVAL = 300;

    public NormalGame() {
        super(GameDifficulty.NORMAL);
        initializeDifficulty();
        finalizeDifficultyInitialization();
    }

    @Override
    protected void initializeDifficulty() {
        enemyMaxNumber = 5;
        mobWeight = 0.45;
        eliteWeight = 0.25;
        elitePlusWeight = 0.20;
        aceWeight = 0.10;
        setHeroShootCycle(15);
        setEnemyShootCycle(20);
        setEnemySpawnCycle(20);
        enemyHpScale = 1.00;
        enemySpeedScale = 1.00;
        bossEnabled = true;
        bossThreshold = 300;
    }

    @Override
    protected void progressDifficulty() {
        if (!reachedFrameInterval(UPGRADE_INTERVAL)) {
            return;
        }
        setEnemySpawnCycle(Math.max(12, getEnemySpawnCycle() - 1));
        enemyHpScale += 0.10;
        enemySpeedScale += 0.10;
        printDifficultyUpgrade(String.format(
                "difficulty up: enemySpawnCycle=%.0f enemyHpScale=%.2f enemySpeedScale=%.2f",
                getEnemySpawnCycle(),
                enemyHpScale,
                enemySpeedScale
        ));
    }
}
