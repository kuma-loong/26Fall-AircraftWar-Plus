package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 炸弹道具（实验二模拟生效）。
 */
public class BombItem extends AbstractItem {

    public BombItem(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        System.out.println("BombSupply active!");
    }
}
