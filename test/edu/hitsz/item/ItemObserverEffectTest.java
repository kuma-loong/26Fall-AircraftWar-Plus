package edu.hitsz.item;

import edu.hitsz.aircraft.AceEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.EnemyBullet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 观察者模式：炸弹/冰冻道具效果测试。
 */
class ItemObserverEffectTest {

    @Test
    @DisplayName("Bomb applies different effects to aircraft and enemy bullet")
    void shouldApplyBombEffectByTargetType() {
        BombItem bombItem = new BombItem(0, 0, 0, 0, new BombEffect(30));
        HeroAircraft heroAircraft = HeroAircraft.getInstance();
        heroAircraft.prepareForNewGame();

        MobEnemy mobEnemy = new MobEnemy(100, 100, 0, 10, 30);
        EliteEnemy eliteEnemy = new EliteEnemy(120, 100, 0, 8, 60);
        ElitePlusEnemy elitePlusEnemy = new ElitePlusEnemy(140, 100, 2, 7, 60);
        AceEnemy aceEnemy = new AceEnemy(160, 100, 3, 6, 90);
        BossEnemy bossEnemy = new BossEnemy(180, 100, 2, 0, 300);
        EnemyBullet enemyBullet = new EnemyBullet(200, 100, 0, 8, 20);

        bombItem.addObserver(mobEnemy);
        bombItem.addObserver(eliteEnemy);
        bombItem.addObserver(elitePlusEnemy);
        bombItem.addObserver(aceEnemy);
        bombItem.addObserver(bossEnemy);
        bombItem.addObserver(enemyBullet);
        bombItem.activate(heroAircraft);

        assertTrue(mobEnemy.notValid());
        assertTrue(eliteEnemy.notValid());
        assertTrue(elitePlusEnemy.notValid());
        assertTrue(mobEnemy.consumeBombDestroyedFlag());
        assertTrue(eliteEnemy.consumeBombDestroyedFlag());
        assertTrue(elitePlusEnemy.consumeBombDestroyedFlag());
        assertTrue(enemyBullet.notValid());

        assertFalse(aceEnemy.notValid());
        assertEquals(60, aceEnemy.getHp());
        assertEquals(300, bossEnemy.getHp());
        assertFalse(bossEnemy.notValid());
    }

    @Test
    @DisplayName("Freeze applies timed stop/slow and restores movement")
    void shouldApplyFreezeEffectAndRestoreAfterDuration() throws InterruptedException {
        FreezeEffect shortEffect = new FreezeEffect(80L, 70L, 0.5, 90L, 60L);
        FreezeItem freezeItem = new FreezeItem(0, 0, 0, 0, shortEffect);
        HeroAircraft heroAircraft = HeroAircraft.getInstance();
        heroAircraft.prepareForNewGame();

        MobEnemy mobEnemy = new MobEnemy(100, 100, 0, 10, 30);
        EliteEnemy eliteEnemy = new EliteEnemy(120, 100, 0, 8, 60);
        ElitePlusEnemy elitePlusEnemy = new ElitePlusEnemy(140, 100, 2, 7, 60);
        AceEnemy aceEnemy = new AceEnemy(160, 100, 4, 6, 90);
        BossEnemy bossEnemy = new BossEnemy(180, 100, 2, 0, 300);
        EnemyBullet enemyBullet = new EnemyBullet(200, 100, 0, 8, 20);

        freezeItem.addObserver(mobEnemy);
        freezeItem.addObserver(eliteEnemy);
        freezeItem.addObserver(elitePlusEnemy);
        freezeItem.addObserver(aceEnemy);
        freezeItem.addObserver(bossEnemy);
        freezeItem.addObserver(enemyBullet);
        freezeItem.activate(heroAircraft);

        int mobY = mobEnemy.getLocationY();
        int eliteY = eliteEnemy.getLocationY();
        int elitePlusY = elitePlusEnemy.getLocationY();
        int aceY = aceEnemy.getLocationY();
        int bossX = bossEnemy.getLocationX();
        int bulletY = enemyBullet.getLocationY();

        mobEnemy.forward();
        eliteEnemy.forward();
        elitePlusEnemy.forward();
        aceEnemy.forward();
        bossEnemy.forward();
        enemyBullet.forward();

        assertEquals(mobY, mobEnemy.getLocationY());
        assertEquals(eliteY, eliteEnemy.getLocationY());
        assertEquals(elitePlusY, elitePlusEnemy.getLocationY());
        assertEquals(aceY + 3, aceEnemy.getLocationY());
        assertEquals(bossX + 2, bossEnemy.getLocationX());
        assertEquals(bulletY, enemyBullet.getLocationY());

        Thread.sleep(120L);

        eliteEnemy.forward();
        elitePlusEnemy.forward();
        aceEnemy.forward();
        enemyBullet.forward();
        mobEnemy.forward();

        assertTrue(eliteEnemy.getLocationY() > eliteY);
        assertTrue(elitePlusEnemy.getLocationY() > elitePlusY);
        assertTrue(aceEnemy.getLocationY() > aceY + 3);
        assertTrue(enemyBullet.getLocationY() > bulletY);
        assertEquals(mobY, mobEnemy.getLocationY());
    }
}
