package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.ElitePlusEnemy;

/**
 * 精锐敌机工厂。
 */
public class ElitePlusEnemyFactory implements EnemyFactory {

    private static final int SPEED_X = 3;
    private static final int SPEED_Y = 7;
    private static final int HP = 90;

    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY) {
        return new ElitePlusEnemy(locationX, locationY, SPEED_X, SPEED_Y, HP);
    }
}
