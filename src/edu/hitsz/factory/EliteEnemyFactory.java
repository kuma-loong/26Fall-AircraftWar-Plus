package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemy;

/**
 * 精英敌机工厂。
 */
public class EliteEnemyFactory implements EnemyFactory {

    private static final int SPEED_X = 0;
    private static final int SPEED_Y = 8;
    private static final int HP = 60;

    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY) {
        return new EliteEnemy(locationX, locationY, SPEED_X, SPEED_Y, HP);
    }
}
