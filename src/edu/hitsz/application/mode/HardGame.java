package edu.hitsz.application.mode;

import edu.hitsz.application.Game;
import edu.hitsz.application.GameSettings;
import edu.hitsz.dao.GameDifficulty;

/**
 * 困难难度游戏，支持更强的递进与 Boss 血量增长。
 */
public class HardGame extends Game {
    private static final int UPGRADE_INTERVAL = 250;

    public HardGame(GameSettings settings) {
        super(settings == null ? new GameSettings(GameDifficulty.HARD, null, false) : settings);
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
                "敌机增多，速度加快，射击更密集。"
        );
    }

    @Override
    protected int getBossHpBonusPerSpawn(int spawnIndex) {
        return 50 * Math.max(0, spawnIndex - 1);
    }
}
