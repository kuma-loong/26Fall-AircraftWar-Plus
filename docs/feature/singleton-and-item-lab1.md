# Feature：英雄机单例与道具链路（实验一）

## 1. HeroAircraft 单例（DCL）
- `HeroAircraft` 构造器改为 `private`。
- 引入 `private static volatile HeroAircraft instance`。
- 通过 `getInstance()` 双重检查锁定创建与返回唯一实例。
- 调用方（`Game`）仅可通过 `HeroAircraft.getInstance()` 获取对象。

## 2. 精英敌机逻辑
- `EliteEnemy` 继承 `AbstractAircraft`。
- 支持向下发射 `EnemyBullet`（单排直射）。
- 在 `Game.shootAction()` 中统一调用敌机 `shoot()`，普通敌机空实现。

## 3. 道具生成与生效
- 新建 `item` 包与 `AbstractItem` 抽象父类，统一下落移动与出界消失逻辑。
- 精英敌机被击毁后按概率掉落道具。
- 当前仅投放三类道具：`BloodItem`、`FireItem`、`SuperFireItem`。
- 英雄机碰撞道具后道具失效并触发效果。
