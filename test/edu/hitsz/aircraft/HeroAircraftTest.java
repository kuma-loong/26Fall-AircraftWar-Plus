package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * HeroAircraft 及其父类行为测试。
 */
class HeroAircraftTest {

    private HeroAircraft heroAircraft;

    @BeforeEach
    void setUp() {
        heroAircraft = HeroAircraft.getInstance();
        heroAircraft.setDefaultShootStrategy();
        heroAircraft.setLocation(256, 700);
    }

    @Test
    @DisplayName("HeroAircraft.getInstance returns singleton instance")
    void shouldReturnSingletonInstance() {
        HeroAircraft anotherHeroAircraft = HeroAircraft.getInstance();
        assertSame(heroAircraft, anotherHeroAircraft);
    }

    @Test
    @DisplayName("AbstractFlyingObject.setLocation updates hero position")
    void shouldUpdateLocationThroughParentMethod() {
        heroAircraft.setLocation(120.8, 560.2);
        assertAll(
                () -> assertEquals(120, heroAircraft.getLocationX()),
                () -> assertEquals(560, heroAircraft.getLocationY())
        );
    }

    @Test
    @DisplayName("Default straight strategy fires one bullet")
    void shouldShootOneBulletWithDefaultStrategy() {
        heroAircraft.setDefaultShootStrategy();
        List<BaseBullet> bullets = heroAircraft.shoot();
        assertEquals(1, bullets.size());
    }

    @Test
    @DisplayName("Fire strategy fires three bullets")
    void shouldShootThreeBulletsWithFireStrategy() {
        heroAircraft.setFireShootStrategy();
        List<BaseBullet> bullets = heroAircraft.shoot();
        assertEquals(3, bullets.size());
    }   

    @Test
    @DisplayName("Super fire strategy fires twenty bullets")
    void shouldShootTwentyBulletsWithSuperFireStrategy() {
        heroAircraft.setSuperFireShootStrategy();
        List<BaseBullet> bullets = heroAircraft.shoot();
        assertEquals(20, bullets.size());
    }

    @Test
    @DisplayName("Temporary fire strategy returns to default after duration")
    void shouldRestoreDefaultStrategyAfterTemporaryFireEnds() throws InterruptedException {
        heroAircraft.activateTemporaryFireShootStrategy(120);

        assertTrue(waitUntilBulletCount(3, 80));
        assertTrue(waitUntilBulletCount(1, 500));
    }

    @Test
    @DisplayName("Latest temporary fire effect wins during overlap")
    void shouldKeepLatestTemporaryFireEffectWhenOverlapping() throws InterruptedException {
        heroAircraft.activateTemporaryFireShootStrategy(120);
        Thread.sleep(40);
        heroAircraft.activateTemporarySuperFireShootStrategy(220);

        assertTrue(waitUntilBulletCount(20, 80));
        Thread.sleep(110);
        assertEquals(20, heroAircraft.shoot().size());
        assertTrue(waitUntilBulletCount(1, 500));
    }

    private boolean waitUntilBulletCount(int expectedCount, long timeoutMillis) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutMillis;
        while (System.currentTimeMillis() <= deadline) {
            if (heroAircraft.shoot().size() == expectedCount) {
                return true;
            }
            Thread.sleep(10);
        }
        return heroAircraft.shoot().size() == expectedCount;
    }
}
