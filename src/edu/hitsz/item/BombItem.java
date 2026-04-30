package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 炸弹道具，触发时广播炸弹效果。
 */
public class BombItem extends AbstractItem {
    private static final int DEFAULT_ACE_DAMAGE = 30;
    private final BombEffect bombEffect;

    public BombItem(int locationX, int locationY, int speedX, int speedY) {
        this(locationX, locationY, speedX, speedY, new BombEffect(DEFAULT_ACE_DAMAGE));
    }

    public BombItem(int locationX, int locationY, int speedX, int speedY, BombEffect bombEffect) {
        super(locationX, locationY, speedX, speedY);
        this.bombEffect = bombEffect == null ? new BombEffect(DEFAULT_ACE_DAMAGE) : bombEffect;
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        notifyBombActivated(bombEffect);
    }
}
