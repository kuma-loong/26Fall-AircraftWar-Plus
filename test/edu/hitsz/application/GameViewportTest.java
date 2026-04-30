package edu.hitsz.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.Dimension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 运行时视口缩放规则测试。
 */
class GameViewportTest {

    @Test
    @DisplayName("Viewport keeps original size when already within bounds")
    void shouldKeepViewportSizeWhenWithinBounds() {
        Dimension fitted = GameViewport.fitToBounds(512, 768, 1600, 900);

        assertEquals(512, fitted.width);
        assertEquals(768, fitted.height);
    }

    @Test
    @DisplayName("Viewport scales down oversized image proportionally")
    void shouldScaleViewportDownProportionally() {
        Dimension fitted = GameViewport.fitToBounds(4000, 3000, 1440, 810);

        assertEquals(1080, fitted.width);
        assertEquals(810, fitted.height);
    }

    @Test
    @DisplayName("Viewport scales down by width limit when width is the bottleneck")
    void shouldScaleViewportByWidthLimit() {
        Dimension fitted = GameViewport.fitToBounds(3000, 1200, 1500, 1200);

        assertEquals(1500, fitted.width);
        assertEquals(600, fitted.height);
    }
}
