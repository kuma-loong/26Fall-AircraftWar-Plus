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

## 2026-04-25 实验四实现（DAO 排行榜 + JUnit5 + VS Code 配置）

### 需求来源
- 用户要求：按实验四执行计划完成 DAO 排行榜、HeroAircraft 单元测试、VS Code + JDK25 + JUnit5 配置，并同步 UML 与文档。

### 本次修改
1. DAO 模式接入
- 新增 `edu.hitsz.dao` 包：
  - `ScoreRecord`
  - `ScoreRecordDao`
  - `FileScoreRecordDao`
  - `GameDifficulty`
  - `ScoreBoardService`
- 排行榜采用 `data/ranking-*.tsv` 文件存储，业务排序与控制台展示由服务层负责。

2. 游戏逻辑接入
- `Game` 新增 `difficulty` 与 `scoreBoardService` 字段。
- 默认构造器固定使用 `GameDifficulty.NORMAL`，并预留 `Game(GameDifficulty difficulty)` 供后续实验扩展。
- `checkResultAction()` 调整为结算只执行一次：
  - 打印 `Game Over!`
  - 自动保存默认玩家 `Player` 的本局分数
  - 打印当前难度排行榜

3. 测试补齐
- 新增 `HeroAircraftTest`：
  - 单例性测试
  - 父类位置方法测试
  - 默认 / 火力 / 超级火力策略子弹数量测试
- 新增 `FileScoreRecordDaoTest`：
  - 新增与读取
  - 空文件读取
  - 按名次删除
  - 难度文件路径隔离

4. 开发环境配置
- 新增 `.vscode/settings.json`，固定 JDK 25 路径并声明 `src` / `test`。
- 新增 `.vscode/tasks.json`，提供编译源码、运行游戏、编译测试、运行 JUnit5 测试任务。

5. UML 与文档同步
- 新增 `uml/Lab4Dao.puml`。
- 新增 `docs/feature/dao-pattern-lab4.md`。
- 更新 `docs/lab/lab4.md`、`docs/note.md`。

### 基础代码检查与 Code Review
- 已执行静态自检，重点检查：
  - HeroAircraft 仍保持 DCL 单例；
  - `Game` 结算流程不会重复写榜；
  - DAO 文件操作使用 `Iterator`/整体回写，未引入并发修改风险；
  - 难度与数据文件映射关系固定、可扩展。

## 2026-04-25 实验五非 UI 实现（音效线程 + 火力计时）

### 需求来源
- 用户要求：仅完成实验五非 UI 任务，明确跳过难度选择、排行榜 Swing 界面和输入弹窗等 UI 工作。

### 本次修改
1. 音频模块
- 新增 `application/audio/MusicThread`，基于 `Thread` 实现单次播放、循环播放和中途停止。
- 新增 `application/audio/AudioManager`，统一封装普通 BGM、Boss BGM 与短音效播放。

## 2026-04-30 application 包分层整理

### 需求来源
- 用户要求：整理 `application` 包结构，在不破坏现有实验约束的前提下改为更规范的项目组织方式，并完成测试与提交。

### 本次修改
1. 子包拆分
- 保留 `application` 顶层中的入口与主流程类：
  - `Main`
  - `Game`
  - `GameMainFrame`
  - `HeroController`
  - `ImageManager`
- 新增 `application/mode`：
  - `EasyGame`
  - `NormalGame`
  - `HardGame`
  - `GameOverInfo`
- 新增 `application/ui`：
  - `StartMenuPanel`
  - `LeaderboardPanel`
- 新增 `application/audio`：
  - `AudioManager`
  - `MusicThread`

2. 引用与测试同步
- 更新 `Game`、`GameMainFrame` 的 import。
- 更新 `GameTemplateModeTest` 对难度子类的引用。
- 同步修正文档中的旧包路径描述。

### 验证与 Code Review
- 编译通过：
  - `javac -encoding UTF-8 -d out/production $(find src/edu/hitsz -name "*.java")`
- 测试通过：
  - `find test -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -cp lib/junit-platform-console-standalone-1.12.2.jar:out/production -d out/test`
  - `java -jar lib/junit-platform-console-standalone-1.12.2.jar --class-path out/test:out/production --scan-class-path`
- 结论：
  - 包职责边界更清晰，未改动游戏业务逻辑；
  - `Game` 模板方法结构保持不变；
  - UI、音频、难度实现已从顶层 `application` 分离，降低继续堆叠类的风险。

2. 游戏流程接线
- `Game.action()` 启动普通 BGM。
- Boss 生成时切换 Boss BGM，Boss 阵亡后恢复普通 BGM。
- 英雄子弹命中敌机、拾取道具、炸弹触发、游戏结束时接入对应音效。
- 游戏结束时停止循环音乐，继续保留默认玩家 `Player` 的排行榜写入与控制台输出。

3. 火力道具时长控制
- `FireItem` 默认持续 `5000ms`，`SuperFireItem` 默认持续 `3000ms` 的限时火力。
- `HeroAircraft` 新增版本号校验，连续拾取时仅最后一次效果有权恢复默认策略。

4. 难度与背景
- `ImageManager` 新增按 `GameDifficulty` 切换背景图的方法。
- `new Game()` 仍默认 `NORMAL`，不引入新的启动 UI。

5. 测试与文档
- 扩展 `HeroAircraftTest`，覆盖限时恢复与连续拾取覆盖场景。
- 扩展 `FileScoreRecordDaoTest`，补充排序行为验证。
- 更新 `docs/lab/lab5.md`、`docs/note.md`，新增 `docs/feature/audio-and-firepower-lab5.md`。

### 基础代码检查与 Code Review
- 编译检查：
  - `find src/edu/hitsz -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -d out/production`
  - `find test -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -cp lib/junit-platform-console-standalone-1.12.2.jar:out/production -d out/test`
- Code review 关注点：
  - 音频线程通过 `volatile` 状态位与 `stopMusic()` 协同退出；
  - 火力恢复线程不直接操作游戏集合，避免引入 EDT 并发修改问题；
  - 旧火力线程不会覆盖新火力效果；
  - 未提前实现实验六难度参数细化。

## 2026-04-30 实验六实现（观察者模式 + 模板模式）

### 需求来源
- 用户要求：按既定 Lab6 实施计划完成两项核心重构，并补齐测试、UML、文档与 code review。

### 本次修改
1. 模板模式重构
- `Game` 改为抽象模板基类，新增 `start()` 启动 `Timer` 与 BGM。
- `action()` 固化为单帧模板方法：生成 -> 射击 -> 移动 -> 碰撞 -> 后处理 -> 难度递进 -> 重绘 -> 结算。
- 新增 `EasyGame`、`NormalGame`、`HardGame` 三个难度子类。
- `GameMainFrame.startGame()` 改为按 `GameDifficulty` 分派具体子类。

2. 观察者模式重构
- 新增 `item/ItemEffectObserver`、`BombEffect`、`FreezeEffect`。
- `AbstractItem` 新增观察者注册、移除、清空与广播能力。
- `BombItem` / `FreezeItem` 改为真实广播效果，不再输出模拟日志。
- `AbstractAircraft` 与 `EnemyBullet` 作为观察者接收炸弹/冰冻通知。

3. 飞行状态与结算补齐
- `AbstractFlyingObject` 新增永久静止、临时冻结、临时减速和自动恢复逻辑。
- `AbstractAircraft` 新增：
  - `resetHp(int)`
  - 炸弹击毁待结算标记
  - 禁掉落标记
- `Game` 统一处理炸弹击毁后的计分与禁掉落，避免重复结算。

4. 难度参数与递进
- `Easy`：无 Boss、无递进。
- `Normal`：每 300 帧提升敌机生成速度、血量倍率、速度倍率。
- `Hard`：每 250 帧额外提升敌机射击频率并拉长英雄射击周期；Boss 血量按召唤次数增长。
- 两种递进均输出控制台升级日志。

5. 测试、UML 与文档
- 新增 `GameTemplateModeTest`。
- 新增 `ItemObserverEffectTest`。
- 新增 `uml/Lab6ObserverPattern.puml`。
- 新增 `uml/Lab6TemplateMethod.puml`。
- 更新 `docs/lab/lab6.md`、新增 `docs/feature/observer-template-lab6.md`、同步 `docs/note.md`。

### 执行验证
- 源码编译：
  - `javac -encoding UTF-8 -d out/production $(find src/edu/hitsz -name "*.java")`
- 测试编译：
  - `find test -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -cp lib/junit-platform-console-standalone-1.12.2.jar:out/production -d out/test`
- 全量测试：
  - `java -jar lib/junit-platform-console-standalone-1.12.2.jar --class-path out/test:out/production --scan-class-path`
- 结果：20/20 测试通过。

### 本轮 Code Review 结论
- `HeroAircraft` 的 DCL 单例未被破坏。
- `Game` 仍使用 Swing EDT 上的 `Timer`，集合读写未回退到跨线程并发。
- 道具触发后调用 `clearObservers()`，未保留脏引用。
- 炸弹计分通过待结算标记统一回收，未出现重复计分。
- 冻结/减速状态只影响敌机与敌机子弹，不污染英雄机火力计时线程。

## 2026-04-30 创新功能实现（一）：自定义背景与连击奖励

### 需求来源
- 用户要求：在全部实验基础内容完成后，新增一组简单可展示的创新功能，优先实现背景快速自定义接口，并补一个轻量玩法创新。

### 本次修改
1. 自定义背景与启动配置
- 新增 `GameSettings`，统一承载难度、背景配置与“按图片尺寸启动窗口”选项。
- 新增 `BackgroundProfile`，用于菜单展示背景名称、路径和原始尺寸。
- `GameMainFrame.startGame(...)` 改为接收完整启动配置，不再只接收 `GameDifficulty`。

2. 菜单与背景资源管理
- `StartMenuPanel` 新增：
  - 背景下拉框
  - “按图片尺寸启动窗口”复选框
  - “刷新背景列表”按钮
- `ImageManager` 新增 `src/images/custom/` 扫描逻辑，支持 `png/jpg/jpeg` 自定义背景导入。
- 新增 `src/images/custom/.gitkeep`，为用户提供明确导入目录。
- 自定义图片加载失败时自动回退到当前难度默认背景。

3. 运行时窗口尺寸重构
- 新增 `GameViewport` 统一管理运行时宽高。
- 将英雄机初始位置、鼠标控制边界、敌机生成区间、Boss 出生点、道具掉落边界、子弹与飞行物越界判定从固定常量切换为运行时视口读取。
- 未启用自由尺寸时，窗口仍保持默认 `512x768`；启用后，窗口尺寸跟随选中背景图原始宽高。

4. 连击奖励系统
- `Game` 新增连击计数、超时重置、分段倍率和 HUD 显示。
- 击毁敌机时连续累加连击：
  - `5+` 连击：`1.2x`
  - `10+` 连击：`1.5x`
  - `20+` 连击：`2.0x`
- 每达到一次 `10` 的倍数连击，为英雄机恢复 `10` 点生命值。
- 英雄机被敌方子弹命中或与敌机相撞时立即清空连击。

5. 文档与 Git 管理
- 新增 `docs/feature/custom-background-and-combo.md`。
- 更新 `docs/note.md`，补充创新功能阶段约束。
- 代码提交拆分为两次功能提交：
  - `51ca3e6 feat: add custom background selection and runtime viewport`
  - `05eed39 feat: add combo scoring and sustain reward`

### 基础代码检查
- 执行：
  - `javac -d /tmp/aircraftwar-build $(find src/edu/hitsz -name "*.java")`
- 结果：编译通过。

### 本轮 Code Review 结论
- 自由尺寸模式未破坏 `HeroAircraft` 的 DCL 单例实现。
- 游戏运行边界统一收口到 `GameViewport`，减少新增功能继续扩散固定窗口常量的风险。
- 自定义背景加载失败存在回退路径，不会因单张资源异常导致程序退出。
- 连击系统只接入 `Game` 的计分与 HUD，不侵入敌机、道具和难度类的职责边界。
