package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.factory.ItemPoolType;
import edu.hitsz.factory.ItemSimpleFactory;
import edu.hitsz.item.AbstractItem;
import edu.hitsz.item.BombEffect;
import edu.hitsz.item.FreezeEffect;
import edu.hitsz.item.ItemEffectObserver;
import edu.hitsz.shootstrategy.NoShootStrategy;
import edu.hitsz.shootstrategy.ShootStrategy;

import java.util.List;

/**
 * 所有种类飞机的抽象父类
 * @author kuma
 */
public abstract class AbstractAircraft extends AbstractFlyingObject implements ItemEffectObserver {

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
    private boolean dropDisabled = false;
    private boolean bombDestroyedPendingSettlement = false;
    /**
     * 当前飞机持有的射击策略（Strategy Pattern Context）。
     */
    private ShootStrategy shootStrategy = new NoShootStrategy();

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
     * 重置飞机血量与最大血量。
     *
     * @param hp 新生命值
     */
    public void resetHp(int hp) {
        int safeHp = Math.max(1, hp);
        this.maxHp = safeHp;
        this.hp = safeHp;
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
     * 设置当前射击策略。
     * @param shootStrategy 射击策略；传入 null 时退化为不发射策略
     */
    public void setShootStrategy(ShootStrategy shootStrategy) {
        if (shootStrategy == null) {
            this.shootStrategy = new NoShootStrategy();
            return;
        }
        this.shootStrategy = shootStrategy;
    }

    /**
     * 获取当前射击策略。
     * @return 当前策略对象
     */
    public ShootStrategy getShootStrategy() {
        return shootStrategy;
    }

    /**
     * 获取敌机掉落概率，默认不掉落。
     * @return [0,1] 区间内概率
     */
    public double getDropProbability() {
        return 0.0;
    }

    /**
     * 获取敌机掉落池类型，默认无掉落池。
     * @return 掉落池类型；null 表示不掉落
     */
    public ItemPoolType getDropItemPoolType() {
        return null;
    }

    /**
     * 尝试生成掉落道具，具体掉落参数由子类定义。
     * @return 掉落道具；未触发时返回 null
     */
    public AbstractItem tryGenerateDropItem() {
        if (dropDisabled) {
            return null;
        }
        ItemPoolType poolType = getDropItemPoolType();
        if (poolType == null) {
            return null;
        }
        double probability = getDropProbability();
        if (probability <= 0) {
            return null;
        }
        if (Math.random() < Math.min(1.0, probability)) {
            return ItemSimpleFactory.createItem(poolType, getLocationX(), getLocationY());
        }
        return null;
    }

    /**
     * 飞机射击方法，由 Context 委托给当前 Strategy。
     * @return 当前策略生成的子弹列表
     */
    public List<BaseBullet> shoot() {
        return shootStrategy.shoot(this);
    }

    public boolean isDropDisabled() {
        return dropDisabled;
    }

    public void markBombDestroyedForSettlement() {
        dropDisabled = true;
        bombDestroyedPendingSettlement = true;
        vanish();
    }

    public boolean consumeBombDestroyedFlag() {
        if (!bombDestroyedPendingSettlement) {
            return false;
        }
        bombDestroyedPendingSettlement = false;
        return true;
    }

    @Override
    public void onBombActivated(BombEffect bombEffect) {
        if (this instanceof BossEnemy) {
            return;
        }
        if (this instanceof AceEnemy) {
            decreaseHp(bombEffect.getAceDamage());
            if (notValid()) {
                markBombDestroyedForSettlement();
            }
            return;
        }
        markBombDestroyedForSettlement();
    }

    @Override
    public void onFreezeActivated(FreezeEffect freezeEffect) {
        if (this instanceof BossEnemy) {
            return;
        }
        if (this instanceof MobEnemy) {
            freezePermanently();
            return;
        }
        if (this instanceof EliteEnemy) {
            freezeTemporarily(freezeEffect.getEliteFreezeMillis());
            return;
        }
        if (this instanceof ElitePlusEnemy) {
            freezeTemporarily(freezeEffect.getElitePlusFreezeMillis());
            return;
        }
        if (this instanceof AceEnemy) {
            slowTemporarily(freezeEffect.getAceSlowFactor(), freezeEffect.getAceSlowMillis());
        }
    }

}
