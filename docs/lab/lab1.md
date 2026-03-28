# 实验一：系统分析及单例模式

## 实验目标
1. 重构英雄机为线程安全单例（DCL）。
2. 扩展敌机与道具体系，建立继承关系。
3. 实现普通/精英敌机随机生成。
4. 实现精英敌机射击。
5. 实现精英敌机概率掉落道具。
6. 实现英雄机拾取道具与道具效果（部分模拟）。

## 实际完成情况
- 已将 `HeroAircraft` 改造为 DCL 单例，并将 `Game` 中创建方式替换为 `HeroAircraft.getInstance()`。
- 已新增敌机类：`EliteEnemy`、`ElitePlusEnemy`、`AceEnemy`、`BossEnemy`。
- 已新增道具体系：`AbstractItem`、`BloodItem`、`FireItem`、`SuperFireItem`、`BombItem`、`FreezeItem`。
- 已在 `Game.action()` 中实现 8:2 概率随机生成普通敌机和精英敌机。
- 已实现精英敌机向下直射单排子弹。
- 已实现精英敌机被击毁后 30% 概率掉落道具（仅血量/火力/超级火力）。
- 已实现英雄机与道具碰撞：
  - `BloodItem`：真实回血（封顶 maxHp）；
  - `FireItem`：控制台输出 `FireSupply active!`；
  - `SuperFireItem`：控制台输出 `FirePlusSupply active!`。
- 已接入道具绘制、移动与失效清理。

## 参数说明（实验一）
- 敌机生成比例：`Mob : Elite = 8 : 2`
- 精英子弹参数：威力 `20`，向下附加速度 `+5`
- 精英掉落概率：`30%`
- 血量道具恢复：`30HP`

## 未纳入本次范围
- UML 类图同步（按本次任务要求排除）。
- 炸弹与冰冻道具功能逻辑（仅保留类定义）。
- 精锐/王牌/Boss 敌机具体行为（仅保留占位类与继承关系）。
