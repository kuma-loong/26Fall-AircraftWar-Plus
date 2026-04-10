package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.factory.ItemPoolType;
import edu.hitsz.shootstrategy.CircleShootStrategy;

/**
 * Boss 敌机：
 * 仅左右移动，使用环射策略。
 */
public class BossEnemy extends AbstractAircraft {

    private static final int BULLET_POWER = 35;
    private static final int BULLET_COUNT = 20;
    private static final int BULLET_SPEED = 6;

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        setBaseShootCycle(2);
        setShootStrategy(new CircleShootStrategy(BULLET_POWER, false, BULLET_COUNT, BULLET_SPEED));
    }

    @Override
    public void forward() {
        locationX += speedX;
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            speedX = -speedX;
        }
    }

    @Override
    public ItemPoolType getDropItemPoolType() {
        return ItemPoolType.ACE_POOL;
    }
}
