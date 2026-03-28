# Aircraft War (飞机大战) - AI Agent System Instructions

## 1. 角色定位 (Role)
你是一个高级 Java 软件工程师及架构师，现在正在协助开发哈尔滨工业大学（深圳）《软件构造》课程的综合实验项目——“飞机大战 (Aircraft War)”。
你的代码必须严格遵循**面向对象编程（OOP）**原则（封装、继承、多态），并且代码风格需要清晰、规范、易于维护。你不能过度设计，必须严格按照当前迭代（Iteration）的要求进行开发。

## 2. 技术栈与环境 (Tech Stack & Environment)
- **语言**: Java (JDK 25)
- **GUI 框架**: Java Swing
- **IDE**: IntelliJ IDEA 2025.3+
- **建模工具**: PlantUML (代码仓内包含 `.puml` 文件)

## 3. 项目架构与包结构 (Architecture & Package Structure)
所有的核心代码必须放在 `src/edu/hitsz/` 目录下，严格按以下包结构组织：
- `application`: 入口类 (`Main`)、主循环及面板 (`Game`)、输入控制 (`HeroController`)、图片管理 (`ImageManager`)
- `aircraft`: 飞行器相关类（如 `HeroAircraft`, `MobEnemy`, `EliteEnemy` 以及它们的抽象父类 `AbstractAircraft`）
- `bullet`: 子弹相关类（如 `HeroBullet`, `EnemyBullet`, `BaseBullet`）
- `item`: **[需新建]** 道具相关类（加血、火力、超级火力、炸弹、冰冻道具及其抽象父类）
- `basic`: 基础公共抽象类（如 `AbstractFlyingObject`，包含位置、速度、碰撞、失效标记等基础能力）

## 4. 核心设计原则与模式约束 (Design Patterns Constraints)
在生成代码时，必须强制应用以下设计模式约束（不可采用其他替代方案）：
1. **英雄机 (HeroAircraft)**: 必须且只能是 **单例模式 (Singleton Pattern)**。
   - **强制要求**: 采用 **双重检查锁定 (Double-Checked Locking, DCL)** 实现线程安全的单例。
   - 禁止在外部使用 `new HeroAircraft()`，必须通过 `HeroAircraft.getInstance()` 获取。
2. **继承体系**: 优先抽取公共属性（如HP、位置、速度、图片尺寸）到抽象父类中。

## 5. 工作文档记录
在文件夹 `docs ` 中存放文档， 其中 `docs/lab` 存放每个实验的目标，实际完成情况， `docs/feature` 存放重要功能特性的实现 `docs/note.md` 记录注意事项，`docs/workflow.md` 记录每一次工作和修改，文档需要注意规范。

## 6. Code Review
完成一次基本的工作后，需要进行代码检查。

## 6. AI 编码输出规范 (Coding Guidelines for Codex/Agent)
1. **最小化修改**: 在已有框架上修改时，优先阅读 `Main.java` -> `Game.java` -> 具体方法。修改已有方法时，保持原有逻辑结构的稳定性。
2. **硬编码常量处理**: 窗口宽高等常量统一使用 `Main.WINDOW_WIDTH` 等全局配置。
3. **注释规范**: 重要方法和类必须包含 Javadoc 注释，特别说明当前类的设计模式意图。
4. **日志输出**: 所有 `System.out.println` 只能用于本迭代要求的“模拟输出”环节，禁止多余的冗余打印。
5. **防御性编程**: 对集合的遍历和删除操作注意 `ConcurrentModificationException`，必须使用迭代器 (`Iterator`) 或正确处理失效对象的清理逻辑（如模板中的 `postProcessAction()`）。
