package edu.hitsz.application;

/**
 * 运行时游戏视口配置。
 * 统一管理窗口宽高，支持在不改动核心逻辑坐标系接口的前提下切换窗口尺寸。
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

    public static synchronized int getWidth() {
        return width;
    }

    public static synchronized int getHeight() {
        return height;
    }
}
