package edu.hitsz.shootstrategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 扇形散射策略。
 */
public class ScatterShootStrategy implements ShootStrategy {

    private static final int Y_OFFSET = 2;
    private static final int SPEED_Y_OFFSET = 5;
    private static final int HORIZONTAL_SPACING = 10;
    private static final int HORIZONTAL_SPEED_STEP = 2;

    private final int power;
    private final int direction;
    private final boolean heroBullet;
    private final int bulletCount;

    public ScatterShootStrategy(int power, int direction, boolean heroBullet, int bulletCount) {
        this.power = power;
        this.direction = direction;
        this.heroBullet = heroBullet;
        this.bulletCount = Math.max(1, bulletCount);
    }

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> bullets = new LinkedList<>();
        int centerX = aircraft.getLocationX();
        int bulletY = aircraft.getLocationY() + direction * Y_OFFSET;
        int baseSpeedY = aircraft.getSpeedY() + direction * SPEED_Y_OFFSET;
        int centerIndex = bulletCount / 2;
        for (int i = 0; i < bulletCount; i++) {
            int indexOffset = i - centerIndex;
            int bulletX = centerX + indexOffset * HORIZONTAL_SPACING;
            int bulletSpeedX = indexOffset * HORIZONTAL_SPEED_STEP;
            bullets.add(createBullet(bulletX, bulletY, bulletSpeedX, baseSpeedY));
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
