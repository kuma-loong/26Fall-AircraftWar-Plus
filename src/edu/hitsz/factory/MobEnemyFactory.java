package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.MobEnemy;

/**
 * 普通敌机工厂。
 */
public class MobEnemyFactory implements EnemyFactory {

    private static final int SPEED_X = 0;
    private static final int SPEED_Y = 10;
    private static final int HP = 30;

    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY) {
        return new MobEnemy(locationX, locationY, SPEED_X, SPEED_Y, HP);
    }
}
