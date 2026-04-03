package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;

/**
 * 敌机工厂方法模式抽象工厂。
 */
public interface EnemyFactory {

    /**
     * 创建一个敌机实例。
     * @param locationX 初始 x
     * @param locationY 初始 y
     * @return 具体敌机对象
     */
    AbstractAircraft createEnemy(int locationX, int locationY);
}
