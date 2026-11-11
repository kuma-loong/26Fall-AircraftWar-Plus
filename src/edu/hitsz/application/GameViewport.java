package edu.hitsz.application;

import java.awt.Dimension;

/**
 * 运行时游戏视口配置。
 * 统一提供游戏逻辑坐标边界。
 */
public final class GameViewport {
    private static int width = Main.WINDOW_WIDTH;
    private static int height = Main.WINDOW_HEIGHT;

    private GameViewport() {
    }

    /**
     * 重置为默认窗口尺寸。
     */
    public static synchronized void reset() {
        width = Main.WINDOW_WIDTH;
        height = Main.WINDOW_HEIGHT;
    }

    /**
     * 按给定尺寸更新运行时视口。
     *
     * @param width 目标宽度
     * @param height 目标高度
     */
    public static synchronized void configure(int width, int height) {
        GameViewport.width = Math.max(1, width);
        GameViewport.height = Math.max(1, height);
    }

    /**
     * 在给定边界内按比例缩放目标尺寸。
     * 若原尺寸未超过边界，则保持不变；若超过，则等比缩小。
     *
     * @param sourceWidth 原始宽度
     * @param sourceHeight 原始高度
     * @param maxWidth 允许的最大宽度
     * @param maxHeight 允许的最大高度
     * @return 缩放后的尺寸
     */
    public static Dimension fitToBounds(int sourceWidth, int sourceHeight, int maxWidth, int maxHeight) {
        int safeSourceWidth = Math.max(1, sourceWidth);
        int safeSourceHeight = Math.max(1, sourceHeight);
        int safeMaxWidth = Math.max(1, maxWidth);
        int safeMaxHeight = Math.max(1, maxHeight);
        if (safeSourceWidth <= safeMaxWidth && safeSourceHeight <= safeMaxHeight) {
            return new Dimension(safeSourceWidth, safeSourceHeight);
        }

        double widthScale = safeMaxWidth / (double) safeSourceWidth;
        double heightScale = safeMaxHeight / (double) safeSourceHeight;
        double scale = Math.min(widthScale, heightScale);
        int fittedWidth = Math.max(1, (int) Math.floor(safeSourceWidth * scale));
        int fittedHeight = Math.max(1, (int) Math.floor(safeSourceHeight * scale));
        return new Dimension(fittedWidth, fittedHeight);
    }

    public static synchronized int getWidth() {
        return width;
    }

    public static synchronized int getHeight() {
        return height;
    }
}
