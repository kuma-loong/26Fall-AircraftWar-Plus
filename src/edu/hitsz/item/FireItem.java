package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 火力道具（实验一模拟生效）。
 */
public class FireItem extends AbstractItem {

    public FireItem(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        heroAircraft.setFireShootStrategy();
        System.out.println("FireSupply active!");
    }
}
