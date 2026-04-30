## 实验六：观察者模式与模板模式（实际完成情况）

### 1. 本轮目标
- 使用观察者模式实现炸弹与冰冻道具的真实效果，去掉模拟输出。
- 使用模板模式重构 `Game`，支持 `Easy/Normal/Hard` 三档难度与递进。
- 保持 Lab5 已有功能不回退：菜单、排行榜、音频、火力限时恢复、HeroAircraft DCL 单例。

### 2. 实际完成清单
- 模板模式重构：
  - `Game` 改为抽象模板基类，`start()` 负责启动 `Timer` 与 BGM。
  - `action()` 固化单帧骨架：生成 -> 射击 -> 移动 -> 碰撞 -> 后处理 -> 递进 -> 重绘 -> 结算。
  - 新增 `EasyGame`、`NormalGame`、`HardGame` 三个子类，仅覆盖差异步骤。
  - `GameMainFrame.startGame()` 改为按 `GameDifficulty` 分派具体子类。
- 观察者模式重构：
  - 新增 `ItemEffectObserver`、`BombEffect`、`FreezeEffect`。
  - `AbstractItem` 新增观察者注册/移除/清空及通知能力。
  - `BombItem`、`FreezeItem` 在 `activate(...)` 中广播效果，不再直接依赖具体敌机类。
  - `AbstractAircraft` 与 `EnemyBullet` 作为观察者接收通知并执行效果。
- 飞行状态扩展：
  - `AbstractFlyingObject` 新增永久静止、临时冻结、临时减速与自动恢复。
  - `forward()` 每帧先刷新状态再计算有效速度。
- 炸弹与冰冻效果落地：
  - 炸弹：`Mob/Elite/ElitePlus` 直接坠毁计分且禁止掉落；`Ace` 固定掉血 30；`Boss` 无效；`EnemyBullet` 消失。
  - 冰冻：`Mob` 永久静止；`Elite` 静止 4000ms；`ElitePlus` 静止 3000ms；`Ace` 0.5 倍减速 5000ms；`Boss` 无效；`EnemyBullet` 静止 5000ms。
- 难度参数与递进：
  - `Easy`：`enemyMax=4`，权重 `0.65/0.25/0.10/0.00`，`heroShoot=12`，`enemyShoot=24`，`spawn=24`，`hpScale=0.90`，`speedScale=0.90`，无 Boss。
  - `Normal`：`enemyMax=5`，权重 `0.45/0.25/0.20/0.10`，`heroShoot=15`，`enemyShoot=20`，`spawn=20`，`hpScale=1.00`，`speedScale=1.00`，`bossThreshold=300`，Boss 血量固定。
  - `Hard`：`enemyMax=6`，权重 `0.30/0.25/0.25/0.20`，`heroShoot=14`，`enemyShoot=19`，`spawn=16`，`hpScale=1.20`，`speedScale=1.05`，`bossThreshold=250`，Boss 每次 +50*(n-1)。
  - `Normal` 每 300 帧递进：`spawn-1(min 12)`、`hpScale+0.10`、`speedScale+0.10`。
  - `Hard` 每 250 帧递进：`spawn-1(min 8)`、`enemyShoot-1(min 10)`、`heroShoot+1(max 22)`、`hpScale+0.10`、`speedScale+0.05`。
  - 困难模式中普通敌机保持基础生命值，继续维持一发击毁；强化主要集中在精英及以上敌机与整体战场节奏。
  - 两种递进均输出描述性控制台日志。

### 3. 测试与验证
- 保留并通过原有 13 个测试。
- 新增测试：
  - `ItemObserverEffectTest`：覆盖炸弹差异效果与冰冻定时恢复。
  - `GameTemplateModeTest`：覆盖三档初始参数、难度递进、Easy 无 Boss、Hard Boss 血量增长、炸弹结分与禁掉落。
- 执行结果：
  - 源码编译通过。
  - 测试编译通过。
  - JUnit5 全量通过（20/20）。

### 4. 本轮未做项
- 创新功能（加分项）未纳入本轮。
- 独立实验报告与演示视频未纳入本轮。
