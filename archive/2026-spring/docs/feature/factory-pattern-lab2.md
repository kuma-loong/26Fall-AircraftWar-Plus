# Feature：实验二工厂模式重构

## 1. 工厂方法模式（敌机创建）
- 抽象工厂：`EnemyFactory#createEnemy(int locationX, int locationY)`。
- 具体工厂：
  - `MobEnemyFactory`
  - `EliteEnemyFactory`
  - `ElitePlusEnemyFactory`
  - `AceEnemyFactory`
  - `BossEnemyFactory`（实验二不出场，仅保留扩展能力）
- `Game` 通过概率权重选择具体工厂，统一创建敌机对象。

## 2. 简单工厂模式（道具创建）
- 工厂类：`ItemSimpleFactory`。
- 掉落池枚举：`ItemPoolType`。
- 两类掉落池规则：
  - `ELITE_PLUS_POOL`：血量/火力/超级火力/炸弹（4 选 1）。
  - `ACE_POOL`：血量/火力/超级火力/炸弹/冰冻（5 选 1）。
- `Game` 在敌机击毁后只负责“是否触发掉落 + 选择池类型”，道具实例由简单工厂统一创建。

## 3. 设计收益
- `Game` 逻辑从“对象构造细节”解耦，主循环更聚焦游戏流程控制。
- 新增敌机或道具时只需增加具体工厂/分支，减少跨模块改动范围。
- 通过抽象工厂接口与枚举池规则，后续可平滑扩展难度和关卡策略。
