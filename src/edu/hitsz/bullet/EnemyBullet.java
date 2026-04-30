package edu.hitsz.bullet;

import edu.hitsz.item.BombEffect;
import edu.hitsz.item.FreezeEffect;
import edu.hitsz.item.ItemEffectObserver;

/**
 * 敌机子弹
 * @Author hitsz
 */
public class EnemyBullet extends BaseBullet implements ItemEffectObserver {

    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    @Override
    public void onBombActivated(BombEffect bombEffect) {
        vanish();
    }

    @Override
    public void onFreezeActivated(FreezeEffect freezeEffect) {
        freezeTemporarily(freezeEffect.getEnemyBulletFreezeMillis());
    }
}
