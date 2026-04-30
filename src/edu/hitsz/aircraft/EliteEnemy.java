package edu.hitsz.aircraft;

import edu.hitsz.application.GameViewport;
import edu.hitsz.shootstrategy.StraightShootStrategy;

/**
 * 精英敌机。
 * 实验一中具备向下直射单排子弹能力。
 */
public class EliteEnemy extends AbstractAircraft {

    private static final int BULLET_POWER = 20;
    private static final int SHOOT_DIRECTION = 1;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        // 默认精英敌机射击频率：每 2 个全局射击节拍发射一次
        setBaseShootCycle(2);
        setShootStrategy(new StraightShootStrategy(BULLET_POWER, SHOOT_DIRECTION, false));
    }

    @Override
    public void forward() {
        super.forward();
        if (locationY >= GameViewport.getHeight()) {
            vanish();
        }
    }

}
