package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.AbstractFlyingObject;

/**
 * 道具抽象父类。
 */
public abstract class AbstractItem extends AbstractFlyingObject {

    public AbstractItem(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    /**
     * 道具生效逻辑。
     * @param heroAircraft 英雄机
     */
    public abstract void activate(HeroAircraft heroAircraft);
}
