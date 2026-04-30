# Lab6 特性说明：观察者模式 + 模板模式

## 1. 观察者模式（道具效果）

### 角色划分
- Subject：`AbstractItem`
- Concrete Subject：`BombItem`、`FreezeItem`
- Observer：`ItemEffectObserver`
- Concrete Observer：`AbstractAircraft`、`EnemyBullet`
- Effect 参数对象：`BombEffect`、`FreezeEffect`

### 事件流
1. 英雄机拾取道具。
2. `Game` 在触发前将“当前有效敌机 + 敌机子弹”注册为观察者。
3. 道具 `activate(...)` 广播效果对象。
4. 观察者按自身类型执行差异行为。
5. `Game` 统一清空观察者引用，并对炸弹击毁敌机执行一次性结分与禁掉落。

### 关键取舍
- 不在道具类中直接写敌机分支，避免道具对机型耦合。
- 炸弹结分在 `Game` 统一处理，避免道具直接修改分数。
- 使用效果对象承载参数，测试可注入短时效果，避免长时间等待。

## 2. 模板模式（难度系统）

### 角色划分
- Abstract Class：`Game`
- Concrete Class：`EasyGame`、`NormalGame`、`HardGame`
- Template Method：`action()`
- Hook/Variation：`initializeDifficulty()`、`progressDifficulty()`、`getBossHpBonusPerSpawn(...)`

### 固定骨架
- `spawnEnemyAction()`
- `shootAction()`
- `bulletsMoveAction()` / `aircraftsMoveAction()` / `itemsMoveAction()`
- `crashCheckAction()`
- `postProcessAction()`
- `progressDifficultyAction()`
- `repaint()`
- `checkResultAction()`

### 可变点
- 初始参数：敌机上限、权重、射击周期、生成周期、HP/速度倍率、Boss 开关与阈值。
- 递进规则：Normal 与 Hard 的时间间隔、递进幅度和上下限不同。
- Boss 血量增长：仅 Hard 模式按召唤次数增长。

## 3. 回归风险与控制
- 风险：单例英雄机在多局切换时状态遗留。
- 控制：每局创建 `Game` 时调用 `HeroAircraft.prepareForNewGame()`。

- 风险：道具观察者脏引用导致跨帧污染。
- 控制：每次道具生效后 `clearObservers()`。

- 风险：冻结/减速状态不恢复导致永久异常。
- 控制：`AbstractFlyingObject.forward()` 每帧自动刷新状态并恢复。
