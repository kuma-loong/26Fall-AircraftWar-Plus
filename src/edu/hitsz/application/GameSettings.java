package edu.hitsz.application;

import edu.hitsz.dao.GameDifficulty;

/**
 * 单局游戏启动配置。
 */
public class GameSettings {
    private final GameDifficulty difficulty;
    private final BackgroundProfile backgroundProfile;
    private final boolean useImageSizeAsViewport;

    public GameSettings(
            GameDifficulty difficulty,
            BackgroundProfile backgroundProfile,
            boolean useImageSizeAsViewport
    ) {
        this.difficulty = difficulty == null ? GameDifficulty.NORMAL : difficulty;
        this.backgroundProfile = backgroundProfile == null ? BackgroundProfile.defaultProfile() : backgroundProfile;
        this.useImageSizeAsViewport = useImageSizeAsViewport;
    }

    public GameDifficulty getDifficulty() {
        return difficulty;
    }

    public BackgroundProfile getBackgroundProfile() {
        return backgroundProfile;
    }

    public boolean isUseImageSizeAsViewport() {
        return useImageSizeAsViewport;
    }
}
