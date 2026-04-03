package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;

/**
 * Boss 敌机工厂。
 * 实验二中保留工厂实现，但不接入出场逻辑。
 */
public class BossEnemyFactory implements EnemyFactory {

    private static final int SPEED_X = 1;
    private static final int SPEED_Y = 2;
    private static final int HP = 300;

    @Override
    public AbstractAircraft createEnemy(int locationX, int locationY) {
        return new BossEnemy(locationX, locationY, SPEED_X, SPEED_Y, HP);
    }
}
