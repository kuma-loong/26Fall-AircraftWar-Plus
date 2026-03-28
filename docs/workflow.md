# 工作流记录

## 2026-03-28 实验一实现（除 UML）

### 需求来源
- 用户要求：按 `PLAN.md` 完成实验一全部代码内容，排除 UML 类图同步。

### 本次修改
1. 单例重构
- `HeroAircraft` 改为 DCL 单例。
- `Game` 改为通过 `HeroAircraft.getInstance()` 获取英雄机。

2. 核心实体扩展
- 新增敌机类：`EliteEnemy`、`ElitePlusEnemy`、`AceEnemy`、`BossEnemy`。
- 新增道具体系：`AbstractItem` 及五个子类。

3. 游戏逻辑完善
- 随机生成普通/精英敌机。
- 精英敌机射击实现。
- 敌机子弹命中英雄机判定。
- 精英敌机掉落三类道具。
- 英雄机拾取道具并触发效果。
- 道具渲染、移动与失效清理。
- 击毁得分区分：普通+10，精英+20。

4. 资源映射
- `ImageManager` 新增精英敌机/占位敌机/道具图片映射。

### 基础代码检查
- 执行：`javac $(find src/edu/hitsz -name "*.java")`
- 结果：编译通过。

### 备注
- 工作区存在用户已有改动：`PLAN.md`（未修改、未回滚）。
