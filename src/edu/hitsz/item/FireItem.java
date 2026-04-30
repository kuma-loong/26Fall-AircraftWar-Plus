package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 火力道具（实验一模拟生效）。
 */
public class FireItem extends AbstractItem {
    private static final long DEFAULT_DURATION_MILLIS = 5_000L;

    private final long durationMillis;

    public FireItem(int locationX, int locationY, int speedX, int speedY) {
        this(locationX, locationY, speedX, speedY, DEFAULT_DURATION_MILLIS);
    }

    public FireItem(int locationX, int locationY, int speedX, int speedY, long durationMillis) {
        super(locationX, locationY, speedX, speedY);
        this.durationMillis = durationMillis;
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        heroAircraft.activateTemporaryFireShootStrategy(durationMillis);
    }
}
