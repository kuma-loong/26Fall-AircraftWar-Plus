package edu.hitsz.aircraft;

import edu.hitsz.application.GameViewport;
import edu.hitsz.factory.ItemPoolType;
import edu.hitsz.shootstrategy.DoubleStraightShootStrategy;

/**
 * 精锐敌机：
 * 向下并可横向移动，发射双排直射子弹。
 */
public class ElitePlusEnemy extends AbstractAircraft {

    private static final double DROP_PROBABILITY = 0.40;
    private static final int BULLET_POWER = 25;
    private static final int SHOOT_DIRECTION = 1;

    public ElitePlusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        // 默认精锐敌机射击频率：每 2 个全局射击节拍发射一次
        setBaseShootCycle(2);
        setShootStrategy(new DoubleStraightShootStrategy(BULLET_POWER, SHOOT_DIRECTION, false));
    }

    @Override
    public void forward() {
        super.forward();
        if (locationY >= GameViewport.getHeight()) {
            vanish();
        }
    }

    @Override
    public double getDropProbability() {
        return DROP_PROBABILITY;
    }

    @Override
    public ItemPoolType getDropItemPoolType() {
        return ItemPoolType.ELITE_PLUS_POOL;
    }
}
