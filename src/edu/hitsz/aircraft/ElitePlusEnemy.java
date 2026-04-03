package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 精锐敌机：
 * 向下并可横向移动，发射双排直射子弹。
 */
public class ElitePlusEnemy extends AbstractAircraft {

    private final int shootNum = 2;
    private final int power = 25;
    private final int direction = 1;

    public ElitePlusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        // 默认精锐敌机射击频率：每 2 个全局射击节拍发射一次
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
        for (int i = 0; i < shootNum; i++) {
            int bulletX = x + (i * 2 - shootNum + 1) * 12;
            bullets.add(new EnemyBullet(bulletX, y, 0, speedY, power));
        }
        return bullets;
    }
}
