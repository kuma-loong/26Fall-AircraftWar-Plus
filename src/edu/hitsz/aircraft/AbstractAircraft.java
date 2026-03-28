package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import java.util.List;

/**
 * 所有种类飞机的抽象父类
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {

    //最大生命值
    protected int maxHp;
    protected int hp;
    /**
     * 敌机射击基准周期（按 Game.shootAction 调用次数计）。
     */
    private int baseShootCycle = 1;
    /**
     * 敌机射击周期难度倍率，>1 更慢，<1 更快。
     */
    private double shootCycleFactor = 1.0;
    private int shootCounter = 0;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void increaseHp(int increase) {
        hp += increase;
        if (hp > maxHp) {
            hp = maxHp;
        }
    }

    /**
     * 设置敌机射击基准周期。
     * @param baseShootCycle 基准周期，最小为 1
     */
    public void setBaseShootCycle(int baseShootCycle) {
        this.baseShootCycle = Math.max(1, baseShootCycle);
        this.shootCounter = 0;
    }

    /**
     * 设置敌机射击周期难度倍率。
     * @param shootCycleFactor >0；值越大射击越慢
     */
    public void setShootCycleFactor(double shootCycleFactor) {
        if (shootCycleFactor <= 0) {
            this.shootCycleFactor = 1.0;
        } else {
            this.shootCycleFactor = shootCycleFactor;
        }
        this.shootCounter = 0;
    }

    /**
     * 通用射击节流判断，每帧调用一次。
     * @return true 表示当前允许执行 shoot()
     */
    public boolean canShootNow() {
        shootCounter++;
        int effectiveShootCycle = Math.max(1, (int) Math.round(baseShootCycle * shootCycleFactor));
        if (shootCounter >= effectiveShootCycle) {
            shootCounter = 0;
            return true;
        }
        return false;
    }


    /**
     * 飞机射击方法
     * @return
     *  可射击对象需实现，返回子弹列表
     *  非可射击对象空实现，返回空列表
     */
    public abstract List<BaseBullet> shoot();

}
