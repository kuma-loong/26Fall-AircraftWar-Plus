package edu.hitsz.dao;

/**
 * 游戏难度枚举，不同难度使用独立排行榜文件。
 */
public enum GameDifficulty {
    EASY("easy"),
    NORMAL("normal"),
    HARD("hard");

    private final String fileSuffix;

    GameDifficulty(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }
}
