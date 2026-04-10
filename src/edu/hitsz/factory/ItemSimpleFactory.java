package edu.hitsz.factory;

import edu.hitsz.item.AbstractItem;
import edu.hitsz.item.BloodItem;
import edu.hitsz.item.BombItem;
import edu.hitsz.item.FireItem;
import edu.hitsz.item.FreezeItem;
import edu.hitsz.item.SuperFireItem;

/**
 * 道具简单工厂，按掉落池规则创建道具。
 */
public class ItemSimpleFactory {

    private static final int ITEM_SPEED_X = 0;
    private static final int ITEM_SPEED_Y = 6;

    private ItemSimpleFactory() {
    }

    /**
     * 从指定掉落池中随机创建 1 个道具。
     * @param itemPoolType 掉落池类型
     * @param locationX 初始 x
     * @param locationY 初始 y
     * @return 具体道具
     */
    public static AbstractItem createItem(ItemPoolType itemPoolType, int locationX, int locationY) {
        if (itemPoolType == null) {
            return new BloodItem(locationX, locationY, ITEM_SPEED_X, ITEM_SPEED_Y);
        }

        double random = Math.random();
        if (itemPoolType == ItemPoolType.ELITE_PLUS_POOL) {
            if (random < 0.25) {
                return new BloodItem(locationX, locationY, ITEM_SPEED_X, ITEM_SPEED_Y);
            } else if (random < 0.5) {
                return new FireItem(locationX, locationY, ITEM_SPEED_X, ITEM_SPEED_Y);
            } else if (random < 0.75) {
                return new SuperFireItem(locationX, locationY, ITEM_SPEED_X, ITEM_SPEED_Y);
            } else {
                return new BombItem(locationX, locationY, ITEM_SPEED_X, ITEM_SPEED_Y);
            }
        }

        if (random < 0.2) {
            return new BloodItem(locationX, locationY, ITEM_SPEED_X, ITEM_SPEED_Y);
        } else if (random < 0.4) {
            return new FireItem(locationX, locationY, ITEM_SPEED_X, ITEM_SPEED_Y);
        } else if (random < 0.6) {
            return new SuperFireItem(locationX, locationY, ITEM_SPEED_X, ITEM_SPEED_Y);
        } else if (random < 0.8) {
            return new BombItem(locationX, locationY, ITEM_SPEED_X, ITEM_SPEED_Y);
        } else {
            return new FreezeItem(locationX, locationY, ITEM_SPEED_X, ITEM_SPEED_Y);
        }
    }
}
