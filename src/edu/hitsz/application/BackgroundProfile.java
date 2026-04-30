package edu.hitsz.application;

import java.util.Objects;

/**
 * 背景资源元数据。
 * 用于菜单展示、背景加载以及可选的按图片尺寸启动窗口。
 */
public class BackgroundProfile {
    private final String displayName;
    private final String imagePath;
    private final int width;
    private final int height;
    private final boolean defaultProfile;

    private BackgroundProfile(String displayName, String imagePath, int width, int height, boolean defaultProfile) {
        this.displayName = displayName;
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;
        this.defaultProfile = defaultProfile;
    }

    public static BackgroundProfile defaultProfile() {
        return new BackgroundProfile("按难度默认背景", null, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT, true);
    }

    public static BackgroundProfile customProfile(String displayName, String imagePath, int width, int height) {
        return new BackgroundProfile(displayName, imagePath, width, height, false);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isDefaultProfile() {
        return defaultProfile;
    }

    public boolean supportsViewportResize() {
        return !defaultProfile && imagePath != null && !imagePath.isBlank();
    }

    @Override
    public String toString() {
        if (defaultProfile) {
            return displayName;
        }
        return displayName + " (" + width + "x" + height + ")";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BackgroundProfile that)) {
            return false;
        }
        return width == that.width
                && height == that.height
                && defaultProfile == that.defaultProfile
                && Objects.equals(displayName, that.displayName)
                && Objects.equals(imagePath, that.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(displayName, imagePath, width, height, defaultProfile);
    }
}
