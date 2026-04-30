package edu.hitsz.aircraft;

import edu.hitsz.application.GameViewport;
import edu.hitsz.factory.ItemPoolType;
import edu.hitsz.shootstrategy.ScatterShootStrategy;

/**
 * 王牌敌机：
 * 向下并可横向移动，发射三发扇形散射子弹。
 */
public class AceEnemy extends AbstractAircraft {

    private static final double DROP_PROBABILITY = 0.55;
    private static final int BULLET_POWER = 30;
    private static final int SHOOT_DIRECTION = 1;
    private static final int BULLET_COUNT = 3;

    public AceEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        // 默认王牌敌机射击频率：每 2 个全局射击节拍发射一次
        setBaseShootCycle(2);
        setShootStrategy(new ScatterShootStrategy(BULLET_POWER, SHOOT_DIRECTION, false, BULLET_COUNT));
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
        return ItemPoolType.ACE_POOL;
    }
}
