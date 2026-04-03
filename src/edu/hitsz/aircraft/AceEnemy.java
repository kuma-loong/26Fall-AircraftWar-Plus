package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.ItemPoolType;

import java.util.LinkedList;
import java.util.List;

/**
 * 王牌敌机：
 * 向下并可横向移动，发射三发扇形散射子弹。
 */
public class AceEnemy extends AbstractAircraft {

    private static final double DROP_PROBABILITY = 0.50;

    private final int power = 30;
    private final int direction = 1;

    public AceEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        // 默认王牌敌机射击频率：每 2 个全局射击节拍发射一次
        setBaseShootCycle(2);
    }

    @Override
    public void forward() {
        super.forward();
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> bullets = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction * 2;
        int speedY = this.getSpeedY() + direction * 5;
        // 三发扇形：左、中、右
        bullets.add(new EnemyBullet(x - 10, y, -2, speedY, power));
        bullets.add(new EnemyBullet(x, y, 0, speedY, power));
        bullets.add(new EnemyBullet(x + 10, y, 2, speedY, power));
        return bullets;
    }

    @Override
    public double getDropProbability() {
        return DROP_PROBABILITY;
    }

    @Override
    public ItemPoolType getDropItemPoolType() {
        return ItemPoolType.ACE_POOL;
    }
}
