# Feature：实验五非 UI 功能（音频管理与限时火力）

## 1. 音频模块
- 新增 `edu.hitsz.application.MusicThread`：
  - 继承 `Thread`；
  - 支持单次播放、循环播放；
  - 提供 `stopMusic()` 中途停止。
- 新增 `edu.hitsz.application.AudioManager`：
  - 统一管理普通 BGM、Boss BGM、击中音效、炸弹音效、补给音效、结束音效；
  - 音频异常不抛回主循环，避免影响游戏运行。

## 2. 游戏事件接线
- `Game.action()` 启动普通背景音乐。
- Boss 生成时切换为 `bgm_boss.wav`。
- Boss 阵亡且游戏未结束时恢复普通背景音乐。
- 英雄子弹命中敌机时播放 `bullet_hit.wav`。
- 英雄机拾取道具时播放 `get_supply.wav`。
- 炸弹道具触发时追加播放 `bomb_explosion.wav`。
- 游戏结束时停止循环音乐并播放 `game_over.wav`。

## 3. 火力道具限时恢复
- `FireItem` 激活后切换为散射策略。
- `SuperFireItem` 激活后切换为环射策略。
- 使用 `Runnable + Thread.sleep()` 开启限时恢复线程，散射默认持续 `5000ms`，环射默认持续 `3000ms`。
- `HeroAircraft` 使用版本号校验，仅允许最后一次火力效果在线程结束后恢复默认直射。

## 4. 兼容性与边界
- `HeroAircraft` 仍保持 DCL 单例。
- 未实现实验五 UI：难度选择界面、`CardLayout`、`JTable`、姓名输入弹窗、删除按钮。
- 排行榜继续沿用实验四 DAO 数据逻辑，游戏结束默认按 `Player` 写入当前难度文件。
