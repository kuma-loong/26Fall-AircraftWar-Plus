# Feature：实验三策略模式重构（弹道系统）

## 1. 设计目标
- 将“飞机对象”与“弹道算法”解耦，避免在飞机类中继续堆积弹道分支。
- 通过策略对象组合实现运行时可替换弹道。
- 维持现有工厂模式与主循环结构，不引入额外并发复杂度。

## 2. 模式落地
- Strategy：`ShootStrategy`
- ConcreteStrategy：
  - `NoShootStrategy`
  - `StraightShootStrategy`
  - `DoubleStraightShootStrategy`
  - `ScatterShootStrategy`
  - `CircleShootStrategy`
- Context：`AbstractAircraft`（所有机型继承该 Context）

## 3. 关键实现
1. `AbstractAircraft` 新增 `shootStrategy` 字段并提供：
- `setShootStrategy(ShootStrategy shootStrategy)`
- `getShootStrategy()`
- `shoot()` 统一委托给当前策略

2. 各机型默认策略注入：
- `MobEnemy` -> 不发射
- `EliteEnemy` -> 单排直射
- `ElitePlusEnemy` -> 双排直射
- `AceEnemy` -> 3 发散射
- `BossEnemy` -> 20 发环射
- `HeroAircraft` -> 默认直射

3. 道具动态切换：
- `FireItem` 激活后调用 `heroAircraft.setFireShootStrategy()`
- `SuperFireItem` 激活后调用 `heroAircraft.setSuperFireShootStrategy()`

## 4. Boss 机制补齐
- `Game` 接入 Boss 工厂创建链路。
- 达到分数阈值触发 Boss，且同屏仅允许 1 个 Boss。
- Boss 仅左右移动，不执行向下移动。
- Boss 击毁后固定掉落 3 个随机道具（全道具池）。

## 5. 约束遵守说明
- HeroAircraft 仍为 DCL 单例，无外部 `new HeroAircraft()`。
- 未实现火力超时恢复，符合实验三“多线程豁免”约束。
- `BombItem` / `FreezeItem` 仅控制台输出提示信息。
