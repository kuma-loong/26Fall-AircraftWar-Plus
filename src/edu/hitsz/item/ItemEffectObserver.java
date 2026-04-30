package edu.hitsz.item;

/**
 * 道具效果观察者。
 * 炸弹和冰冻触发后由道具统一通知观察者执行具体行为。
 */
public interface ItemEffectObserver {

    /**
     * 炸弹触发通知。
     *
     * @param bombEffect 炸弹效果参数
     */
    void onBombActivated(BombEffect bombEffect);

    /**
     * 冰冻触发通知。
     *
     * @param freezeEffect 冰冻效果参数
     */
    void onFreezeActivated(FreezeEffect freezeEffect);
}
