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
        for (int i = 0; i < bulletCount; i++) {
            double angle = 2 * Math.PI * i / bulletCount;
            int speedX = (int) Math.round(bulletSpeed * Math.cos(angle));
            int speedY = (int) Math.round(bulletSpeed * Math.sin(angle));
            bullets.add(createBullet(originX, originY, speedX, speedY));
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
