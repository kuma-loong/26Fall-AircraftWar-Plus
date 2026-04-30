package edu.hitsz.item;

/**
 * 冰冻效果参数对象。
 */
public final class FreezeEffect {
    private final long eliteFreezeMillis;
    private final long elitePlusFreezeMillis;
    private final double aceSlowFactor;
    private final long aceSlowMillis;
    private final long enemyBulletFreezeMillis;

    public FreezeEffect(
            long eliteFreezeMillis,
            long elitePlusFreezeMillis,
            double aceSlowFactor,
            long aceSlowMillis,
            long enemyBulletFreezeMillis
    ) {
        this.eliteFreezeMillis = Math.max(0L, eliteFreezeMillis);
        this.elitePlusFreezeMillis = Math.max(0L, elitePlusFreezeMillis);
        this.aceSlowFactor = aceSlowFactor > 0 ? aceSlowFactor : 1.0;
        this.aceSlowMillis = Math.max(0L, aceSlowMillis);
        this.enemyBulletFreezeMillis = Math.max(0L, enemyBulletFreezeMillis);
    }

    public long getEliteFreezeMillis() {
        return eliteFreezeMillis;
    }

    public long getElitePlusFreezeMillis() {
        return elitePlusFreezeMillis;
    }

    public double getAceSlowFactor() {
        return aceSlowFactor;
    }

    public long getAceSlowMillis() {
        return aceSlowMillis;
    }

    public long getEnemyBulletFreezeMillis() {
        return enemyBulletFreezeMillis;
    }
}
