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
        setHeroShootCycle(18);
        setEnemyShootCycle(18);
        setEnemySpawnCycle(16);
        enemyHpScale = 1.20;
        enemySpeedScale = 1.10;
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
        setHeroShootCycle(Math.min(24, getHeroShootCycle() + 1));
        enemyHpScale += 0.15;
        enemySpeedScale += 0.10;
        printDifficultyUpgrade(String.format(
                "difficulty up: enemySpawnCycle=%.0f enemyShootCycle=%.0f heroShootCycle=%.0f enemyHpScale=%.2f enemySpeedScale=%.2f",
                getEnemySpawnCycle(),
                getEnemyShootCycle(),
                getHeroShootCycle(),
                enemyHpScale,
                enemySpeedScale
        ));
    }

    @Override
    protected int getBossHpBonusPerSpawn(int spawnIndex) {
        return 50 * Math.max(0, spawnIndex - 1);
    }
}
