package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 精英敌机。
 * 实验一中具备向下直射单排子弹能力。
 */
public class EliteEnemy extends AbstractAircraft {

    private final int shootNum = 1;
    private final int power = 20;
    private final int direction = 1;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        // 默认精英敌机射击频率：每 2 个全局射击节拍发射一次
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
        int speedX = 0;
        int speedY = this.getSpeedY() + direction * 5;
        for (int i = 0; i < shootNum; i++) {
            bullets.add(new EnemyBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power));
        }
        return bullets;
    }
}
