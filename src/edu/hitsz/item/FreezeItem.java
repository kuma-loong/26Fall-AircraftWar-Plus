package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 冰冻道具，触发时广播冰冻效果。
 */
public class FreezeItem extends AbstractItem {
    private static final FreezeEffect DEFAULT_FREEZE_EFFECT =
            new FreezeEffect(4000L, 3000L, 0.5, 5000L, 5000L);
    private final FreezeEffect freezeEffect;

    public FreezeItem(int locationX, int locationY, int speedX, int speedY) {
        this(locationX, locationY, speedX, speedY, DEFAULT_FREEZE_EFFECT);
    }

    public FreezeItem(int locationX, int locationY, int speedX, int speedY, FreezeEffect freezeEffect) {
        super(locationX, locationY, speedX, speedY);
        this.freezeEffect = freezeEffect == null ? DEFAULT_FREEZE_EFFECT : freezeEffect;
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        notifyFreezeActivated(freezeEffect);
    }
}
