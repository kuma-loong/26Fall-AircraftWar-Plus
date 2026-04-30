package edu.hitsz.item;

/**
 * 炸弹效果参数对象。
 */
public final class BombEffect {
    private final int aceDamage;

    public BombEffect(int aceDamage) {
        this.aceDamage = Math.max(0, aceDamage);
    }

    public int getAceDamage() {
        return aceDamage;
    }
}
