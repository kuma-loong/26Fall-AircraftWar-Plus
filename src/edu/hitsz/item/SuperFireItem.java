package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 超级火力道具（实验一模拟生效）。
 */
public class SuperFireItem extends AbstractItem {
    private static final long DEFAULT_DURATION_MILLIS = 3_000L;

    private final long durationMillis;

    public SuperFireItem(int locationX, int locationY, int speedX, int speedY) {
        this(locationX, locationY, speedX, speedY, DEFAULT_DURATION_MILLIS);
    }

    public SuperFireItem(int locationX, int locationY, int speedX, int speedY, long durationMillis) {
        super(locationX, locationY, speedX, speedY);
        this.durationMillis = durationMillis;
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        heroAircraft.activateTemporarySuperFireShootStrategy(durationMillis);
    }
}
