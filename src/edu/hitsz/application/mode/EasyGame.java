package edu.hitsz.application.mode;

import edu.hitsz.application.Game;
import edu.hitsz.dao.GameDifficulty;

/**
 * 简单难度游戏，不生成 Boss，也不执行难度递进。
 */
public class EasyGame extends Game {

    public EasyGame() {
        super(GameDifficulty.EASY);
        initializeDifficulty();
        finalizeDifficultyInitialization();
    }

    @Override
    protected void initializeDifficulty() {
        enemyMaxNumber = 4;
        mobWeight = 0.65;
        eliteWeight = 0.25;
        elitePlusWeight = 0.10;
        aceWeight = 0.00;
        setHeroShootCycle(12);
        setEnemyShootCycle(24);
        setEnemySpawnCycle(24);
        enemyHpScale = 0.90;
        enemySpeedScale = 0.90;
        bossEnabled = false;
    }

    @Override
    protected void progressDifficulty() {
        // 简单模式不递进
    }
}
