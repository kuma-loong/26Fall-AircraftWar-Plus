package edu.hitsz.shootstrategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 双排直射策略。
 */
public class DoubleStraightShootStrategy implements ShootStrategy {

    private static final int Y_OFFSET = 2;
    private static final int SPEED_Y_OFFSET = 5;
    private static final int BULLET_SPACING = 12;

    private final int power;
    private final int direction;
    private final boolean heroBullet;

    public DoubleStraightShootStrategy(int power, int direction, boolean heroBullet) {
        this.power = power;
        this.direction = direction;
        this.heroBullet = heroBullet;
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        int centerX = aircraft.getLocationX();
        int bulletY = aircraft.getLocationY() + direction * Y_OFFSET;
        int bulletSpeedY = aircraft.getSpeedY() + direction * SPEED_Y_OFFSET;
        bullets.add(createBullet(centerX - BULLET_SPACING, bulletY, 0, bulletSpeedY));
        bullets.add(createBullet(centerX + BULLET_SPACING, bulletY, 0, bulletSpeedY));
        return bullets;
    }

    private BaseBullet createBullet(int x, int y, int speedX, int speedY) {
        if (heroBullet) {
            return new HeroBullet(x, y, speedX, speedY, power);
        }
        return new EnemyBullet(x, y, speedX, speedY, power);
    }
}
