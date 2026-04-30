package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.GameViewport;
import edu.hitsz.basic.AbstractFlyingObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 道具抽象父类。
 */
public abstract class AbstractItem extends AbstractFlyingObject {
    private final List<ItemEffectObserver> observers = new ArrayList<>();

    public AbstractItem(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void forward() {
        super.forward();
        if (locationY >= GameViewport.getHeight()) {
            vanish();
        }
    }

    /**
     * 道具生效逻辑。
     * @param heroAircraft 英雄机
     */
    public abstract void activate(HeroAircraft heroAircraft);

    public void addObserver(ItemEffectObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    public void removeObserver(ItemEffectObserver observer) {
        observers.remove(observer);
    }

    public void clearObservers() {
        observers.clear();
    }

    protected void notifyBombActivated(BombEffect bombEffect) {
        for (ItemEffectObserver observer : observers) {
            observer.onBombActivated(bombEffect);
        }
    }

    protected void notifyFreezeActivated(FreezeEffect freezeEffect) {
        for (ItemEffectObserver observer : observers) {
            observer.onFreezeActivated(freezeEffect);
        }
    }
}
