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

## 2026-03-28 并发风险重构（Code Review）

### 背景
- Code review 识别到高风险问题：`Game` 使用 `java.util.Timer` 在后台线程修改集合，同时 Swing EDT 在 `paint` 中遍历同一集合，存在并发访问与 `ConcurrentModificationException` 风险。

### 重构内容
- 将 `Game` 主循环调度从 `java.util.Timer + TimerTask` 重构为 `javax.swing.Timer`。
- 新增 `gameCycle()` 方法承载单帧更新逻辑。
- `action()` 改为安全启动 `timer.start()`（避免重复启动）。
- 游戏结束时使用 `timer.stop()` 停止循环。

### 结果
- 更新逻辑与渲染逻辑统一在 EDT 执行，消除跨线程并发修改集合的高风险。
- 编译检查通过：`javac -d /tmp/aircraftwar-build $(find src/edu/hitsz -name "*.java")`。
