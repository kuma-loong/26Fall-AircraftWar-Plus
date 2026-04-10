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

## 2026-03-28 敌机射击频率通用接口重构

### 目标
- 提供可复用的敌机射击频率控制机制，支持未来按敌机类型与难度统一调节。

### 修改内容
- `AbstractAircraft` 新增通用射击频率能力：
  - `setBaseShootCycle(int)`
  - `setShootCycleFactor(double)`
  - `canShootNow()`
- `EliteEnemy` 改为使用基类频率机制，默认 `baseShootCycle=2`。
- `Game` 敌机射击逻辑改为：`canShootNow()` 为真时才调用 `shoot()`。
- `Game` 新增难度接口：`setEnemyShootCycleFactor(double)`，可统一影响“当前场上 + 后续生成”敌机。

### 结果
- 射击频率控制从具体敌机类抽离到通用基类，后续接入难度系统时可直接复用。
- 编译检查通过：`javac -d /tmp/aircraftwar-build $(find src/edu/hitsz -name "*.java")`。

## 2026-03-28 英雄机与敌机射击参数解耦

### 目的
- 为后续难度系统提供独立控制维度，分别调整“英雄输出节奏”和“敌机压制节奏”。

### 修改内容
- `Game` 将原单一 `shootCycle` 拆分为：
  - `heroShootCycle` / `heroShootCounter`
  - `enemyShootCycle` / `enemyShootCounter`
- `shootAction()` 改为分别判断英雄和敌机射击时机。
- 新增接口：
  - `setHeroShootCycle(double)`
  - `setEnemyShootCycle(double)`

### 兼容性
- 仍保留敌机侧通用频率控制：`AbstractAircraft.canShootNow()` + `setShootCycleFactor(double)`。
- 当前形成两层控制：敌机全局节拍（Game） + 敌机个体/难度倍率（Aircraft）。

## 2026-03-28 实验一 UML 同步与教程编写

### 修改内容
- 更新 `uml/Inheritence.puml`：
  - 同步实验一新增敌机与道具体系；
  - 同步 `HeroAircraft` 的 DCL 单例关键成员（私有构造、静态实例、`getInstance()`）；
  - 增加 `Game` 对飞机/子弹/道具的依赖关系；
  - 追加单独页面描述单例结构图。
- 新增 `docs/feature/uml-tutorial.md`：
  - PlantUML 语法速查；
  - 本项目实验一 UML 同步清单；
  - 常见错误与推荐流程。

### 结果
- 实验一 UML 范围已补全。
- 提供可复用的项目内 UML 维护教程。

## 2026-04-03 实验二实现（除 UML）与首版自检

### 需求来源
- 用户要求：完成 `docs/lab/lab2.md` 对应的实验二代码与文档（排除 UML），首版完成后交由用户测试并进行 review。

### 本次修改
1. 工厂方法模式（敌机创建）
- 新增抽象工厂：`EnemyFactory`。
- 新增具体工厂：`MobEnemyFactory`、`EliteEnemyFactory`、`ElitePlusEnemyFactory`、`AceEnemyFactory`、`BossEnemyFactory`。
- `Game` 敌机生成逻辑改为基于工厂与权重创建，不再直接 `new` 敌机。

2. 简单工厂模式（道具创建）
- 新增 `ItemPoolType`（`ELITE_PLUS_POOL`、`ACE_POOL`）。
- 新增 `ItemSimpleFactory`，按掉落池规则随机创建道具。
- `Game` 道具掉落逻辑改为调用简单工厂，不再硬编码道具创建分支。

3. 敌机行为补齐（实验二清单）
- `ElitePlusEnemy`：向下+左右移动，发射双排直射子弹。
- `AceEnemy`：向下+左右移动，发射三发扇形散射子弹。
- `ImageManager` 为 `AceEnemy` 独立映射 `elitePro.png`，便于界面区分敌机类型。

4. 掉落与道具效果
- 精锐击毁掉落池：4 种（不含冰冻），默认掉落概率 `35%`。
- 王牌击毁掉落池：5 种（含冰冻），默认掉落概率 `50%`。
- `BombItem` 与 `FreezeItem` 改为控制台模拟输出激活提示。

5. 文档同步
- 更新 `docs/lab/lab2.md`（实际完成、参数、范围说明）。
- 新增 `docs/feature/factory-pattern-lab2.md`。
- 更新 `docs/note.md`（实验二注意事项）。

### 基础代码检查
- 执行：`javac -d /tmp/aircraftwar-build $(find src/edu/hitsz -name "*.java")`
- 结果：编译通过。

### 首版 Review 结论
- 已满足实验二代码清单（除 UML）：
  - 两种工厂模式均落地；
  - 4 类敌机出场；
  - 精锐/王牌移动与射击满足要求；
  - 掉落范围与模拟输出满足要求。
- 建议用户进行界面实测，重点验证生成概率体感与弹道观感是否需要调参。

## 2026-04-03 实验二 UML 补齐

### 修改内容
- 新增 `uml/Lab2Factory.puml`，包含两页结构图：
  - 简单工厂模式（道具创建）；
  - 工厂方法模式（敌机创建）。
- 图中显式标注模式名称（`<<Simple Factory Pattern>>`、`<<Factory Method Pattern>>`）。
- 更新 `docs/lab/lab2.md`，补充 UML 文件路径与页面说明。

### 自检结论
- UML 内容与当前代码实现一致：
  - `ItemSimpleFactory + ItemPoolType` 对应道具创建链路；
  - `EnemyFactory + 5 个具体工厂` 对应敌机创建链路；
  - `Game` 作为工厂调用方关系明确。

## 2026-04-10 实验三实现（策略模式 + Boss + 道具动态切换，除 UML）

### 需求来源
- 用户要求：按 `docs/lab/lab3.md` 的目标和约束落地实验三执行计划，并将 UML 放在最后一项。

### 本次修改
1. 策略模式重构（弹道系统）
- 新增 `shootstrategy` 包：
  - `ShootStrategy`
  - `NoShootStrategy`
  - `StraightShootStrategy`
  - `DoubleStraightShootStrategy`
  - `ScatterShootStrategy`
  - `CircleShootStrategy`
- `AbstractAircraft` 改为 Strategy Context：
  - 新增 `shootStrategy` 字段；
  - 新增 `setShootStrategy()` / `getShootStrategy()`；
  - `shoot()` 统一委托策略执行。

2. 机型默认策略注入
- `MobEnemy`：不发射。
- `EliteEnemy`：单排直射。
- `ElitePlusEnemy`：双排直射。
- `AceEnemy`：3 发散射。
- `BossEnemy`：20 发环射。
- `HeroAircraft`：默认直射（DCL 单例保持不变）。

3. Boss 机制接入
- `Game` 接入 `BossEnemyFactory` 和分数触发逻辑。
- 新增 Boss 唯一性控制：同屏最多一个 Boss。
- Boss 触发后悬浮上方，仅左右移动，不向下移动。
- Boss 阵亡后固定掉落 3 个随机道具（全道具池）。
- 增加 Boss 计分逻辑。

4. 道具动态切换
- `FireItem`：激活后切换英雄机为散射策略。
- `SuperFireItem`：激活后切换英雄机为环射策略。
- `BombItem` / `FreezeItem` 继续仅控制台提示。

5. 文档同步
- 更新 `docs/lab/lab3.md`（新增落地执行与验收映射）。
- 新增 `docs/feature/strategy-pattern-lab3.md`。
- 更新 `docs/note.md`（实验三注意事项）。

### 基础代码检查
- 执行：`javac -d /tmp/aircraftwar-build $(find src/edu/hitsz -name "*.java")`
- 结果：编译通过。

### Code Review 结论（本轮）
- 策略模式已替换机型内弹道硬编码，符合实验三重构原则。
- HeroAircraft 单例模式保持 DCL，符合项目强约束。
- Boss 生成、唯一性、移动、环射和 3 道具掉落链路已接入。
- 未引入火力超时恢复逻辑，符合实验三多线程豁免要求。
