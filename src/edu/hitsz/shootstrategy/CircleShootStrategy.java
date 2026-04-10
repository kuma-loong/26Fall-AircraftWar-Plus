package edu.hitsz.shootstrategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 环射策略。
 */
public class CircleShootStrategy implements ShootStrategy {

    private final int power;
    private final boolean heroBullet;
    private final int bulletCount;
    private final int bulletSpeed;
    /**
     * 环射初始半径，确保子弹从飞机周围向外发散而非堆在中心点。
     */
    private static final int EXTRA_SPAWN_RADIUS = 6;

    public CircleShootStrategy(int power, boolean heroBullet, int bulletCount, int bulletSpeed) {
        this.power = power;
        this.heroBullet = heroBullet;
        this.bulletCount = Math.max(1, bulletCount);
        this.bulletSpeed = Math.max(1, bulletSpeed);
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        int originX = aircraft.getLocationX();
        int originY = aircraft.getLocationY();
        int spawnRadius = Math.max(1, Math.max(aircraft.getWidth(), aircraft.getHeight()) / 2 + EXTRA_SPAWN_RADIUS);
        for (int i = 0; i < bulletCount; i++) {
            double angle = 2 * Math.PI * i / bulletCount;
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            int spawnX = originX + (int) Math.round(spawnRadius * cos);
            int spawnY = originY + (int) Math.round(spawnRadius * sin);
            int speedX = (int) Math.round(bulletSpeed * cos);
            int speedY = (int) Math.round(bulletSpeed * sin);
            if (speedX == 0 && speedY == 0) {
                speedX = cos >= 0 ? 1 : -1;
            }
            bullets.add(createBullet(spawnX, spawnY, speedX, speedY));
        }
        return bullets;
    }

    private BaseBullet createBullet(int x, int y, int speedX, int speedY) {
        if (heroBullet) {
            return new HeroBullet(x, y, speedX, speedY, power);
        }
        return new EnemyBullet(x, y, speedX, speedY, power);
    }
}
