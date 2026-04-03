package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.AceEnemy;

/**
 * 王牌敌机工厂。
 */
public class AceEnemyFactory implements EnemyFactory {

    private static final int SPEED_X = 4;
    private static final int SPEED_Y = 6;
    private static final int HP = 120;

    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY) {
        return new AceEnemy(locationX, locationY, SPEED_X, SPEED_Y, HP);
    }
}
