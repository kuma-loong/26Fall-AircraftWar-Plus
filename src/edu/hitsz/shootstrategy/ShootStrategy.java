package edu.hitsz.shootstrategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

/**
 * 射击策略接口（Strategy）。
 */
public interface ShootStrategy {

    /**
     * 根据上下文飞机生成本次发射的子弹。
     * @param aircraft 上下文飞机（Context）
     * @return 子弹列表
     */
    List<BaseBullet> shoot(AbstractAircraft aircraft);
}
