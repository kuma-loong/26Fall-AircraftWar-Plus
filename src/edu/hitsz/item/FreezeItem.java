package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 冰冻道具（实验二模拟生效）。
 */
public class FreezeItem extends AbstractItem {

    public FreezeItem(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        System.out.println("FreezeSupply active!");
    }
}
