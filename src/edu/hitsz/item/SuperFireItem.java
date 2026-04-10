package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 超级火力道具（实验一模拟生效）。
 */
public class SuperFireItem extends AbstractItem {

    public SuperFireItem(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        heroAircraft.setSuperFireShootStrategy();
        System.out.println("FirePlusSupply active!");
    }
}
