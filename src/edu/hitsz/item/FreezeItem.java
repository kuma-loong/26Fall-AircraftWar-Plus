package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 冰冻道具占位类（实验一不生成、不生效）。
 */
public class FreezeItem extends AbstractItem {

    public FreezeItem(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    @Override
    public void activate(HeroAircraft heroAircraft) {
        // 实验一范围外
    }
}
