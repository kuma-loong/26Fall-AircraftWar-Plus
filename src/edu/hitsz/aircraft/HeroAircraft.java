package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.shootstrategy.CircleShootStrategy;
import edu.hitsz.shootstrategy.ScatterShootStrategy;
import edu.hitsz.shootstrategy.StraightShootStrategy;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {
    /**
     * 英雄机单例实例，使用 DCL 保证线程安全。
     */
    private static volatile HeroAircraft instance;

    private static final int DEFAULT_POWER = 30;
    private static final int SHOOT_DIRECTION = -1;
    private static final int FIRE_SCATTER_BULLET_COUNT = 3;
    private static final int SUPER_FIRE_CIRCLE_BULLET_COUNT = 20;
    private static final int SUPER_FIRE_CIRCLE_BULLET_SPEED = 7;

    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        setDefaultShootStrategy();
    }

    /**
     * 双重检查锁定（DCL）获取英雄机单例。
     * @return 全局唯一 HeroAircraft
     */
    public static HeroAircraft getInstance() {
        if (instance == null) {
            synchronized (HeroAircraft.class) {
                if (instance == null) {
                    instance = new HeroAircraft(
                            Main.WINDOW_WIDTH / 2,
                            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                            0,
                            -10,
                            100
                    );
                }
            }
        }
        return instance;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    /**
     * 切回英雄机默认直射策略。
     */
    public void setDefaultShootStrategy() {
        setShootStrategy(new StraightShootStrategy(DEFAULT_POWER, SHOOT_DIRECTION, true));
    }

    /**
     * 火力道具：切换为散射策略。
     */
    public void setFireShootStrategy() {
        setShootStrategy(new ScatterShootStrategy(DEFAULT_POWER, SHOOT_DIRECTION, true, FIRE_SCATTER_BULLET_COUNT));
    }

    /**
     * 超级火力道具：切换为环射策略。
     */
    public void setSuperFireShootStrategy() {
        setShootStrategy(new CircleShootStrategy(
                DEFAULT_POWER,
                true,
                SUPER_FIRE_CIRCLE_BULLET_COUNT,
                SUPER_FIRE_CIRCLE_BULLET_SPEED
        ));
    }

}
