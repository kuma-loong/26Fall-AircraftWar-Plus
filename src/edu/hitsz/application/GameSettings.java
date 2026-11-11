package edu.hitsz.application;

import edu.hitsz.dao.GameDifficulty;

/** 单局游戏配置，仅包含难度；背景由难度决定。 */
public class GameSettings {
    private final GameDifficulty difficulty;

    public GameSettings(GameDifficulty difficulty) {
        this.difficulty = difficulty == null ? GameDifficulty.NORMAL : difficulty;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }
}
