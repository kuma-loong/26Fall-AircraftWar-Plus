package edu.hitsz.application;

import edu.hitsz.dao.GameDifficulty;

/**
 * 游戏结束事件数据，只承载结算结果，不处理界面或持久化逻辑。
 * @param difficulty 当前难度
 * @param score 本局得分
 */
public record GameOverInfo(GameDifficulty difficulty, int score) {
}
