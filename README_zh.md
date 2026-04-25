# Aircraft War
[English](README.md) | [简体中文](README_zh.md)

这是哈尔滨工业大学（深圳）《软件构造》课程的经典综合实验项目。本项目是一个基于 Java 与 Swing 框架开发的纵向卷轴射击类游戏。

项目的主要目标是通过从零到一地迭代开发一款完整的游戏，在实践中深入理解和应用**面向对象编程**原则、**经典设计模式**、**文件 I/O 与 DAO 架构**、**Java 多线程**，以及 **图形用户界面（GUI）设计**。

## 🛠 技术栈
- **开发语言**: Java (JDK 25)
- **GUI 框架**: Java Swing
- **测试框架**: JUnit 5
- **环境要求**: macOS / Windows / Linux

## 🚀 迭代里程碑 (实验特性)

本项目按实验阶段采用敏捷迭代方式进行开发与重构，以下是每个实验阶段对应实现的核心设计模式与特性：

### Lab 1: 面向对象基础与单例模式
- 搭建基础的游戏主循环、碰撞检测机制。
- 引入 **单例模式 (Singleton Pattern)** 实现英雄机（`HeroAircraft`）的唯一性，使用双重检查锁定 (DCL) 保证多线程安全。
- 实现普通敌机、精英敌机以及加血、火力道具的基本交互。

### Lab 2: 工厂模式
- 运用 **工厂方法模式 (Factory Method Pattern)** 解耦各类型敌机的实例化过程，支持扩展出不同类型敌机（Elite、ElitePlus、Ace、Boss）。
- 运用 **简单工厂模式 (Simple Factory Pattern)** 配合策略掉落池随机生成各种增益道具。

### Lab 3: 策略模式
- 利用 **策略模式 (Strategy Pattern)** 重构英雄机与敌机的射击弹道系统。
- 实现了直射、多线散射以及 Boss 的环形发射等多种弹道策略，通过持有 `ShootStrategy` 接口实现运行时动态切换火力。
- 引入 Boss 机制，根据得分触发强力 Boss 及多补给掉落逻辑。

### Lab 4: DAO 模式与单元测试
- 构建了基于 TSV 文本文件的本地排行榜系统。
- 采用 **数据访问对象模式 (DAO Pattern)** 将业务逻辑（排行、展示）与底层读写分离，实现按难度（Easy/Normal/Hard）分文件存储机制。
- 引入 JUnit 5，完成了对核心实体方法（如重定位、子弹生成）及本地 DAO 读写链路的单元测试覆盖。

### Lab 5: Swing 图形界面与多线程
- 使用 `CardLayout` 完成了游戏全链路界面的打通：**难度选择主菜单 -> 游戏主循环 -> 结算姓名输入弹窗 -> 得分排行榜展示**。
- 基于 `JTable` 实现了排行榜的 MVC 渲染，支持选中记录的安全删除同步。
- 引入 **多线程机制**：
  - 音效线程 (`Thread`)：实现 BGM 的循环、Boss 专属 BGM 无缝切流及对应动作音效的触发异步支持。
  - 定时线程 (`Runnable`)：实现火力强化道具的限时生效机制，触发一段时间后英雄机弹道能自动安全恢复。

## 🎮 运行说明

本项目原生适配了 VS Code 开发环境，如果您在 VS Code 下打开，可以直接使用 `.vscode/tasks.json` 定义的指令：

1. **编译代码**: 
   在命令调色板或任务列表中直接运行 `Compile Source` 任务。
   *(或者终端手动执行：`javac -encoding UTF-8 -d out/production $(find src/edu/hitsz -name "*.java")` )*

2. **启动游戏**: 
   直接运行 `Run Game` 任务，或者终端运行 `java -cp out/production edu.hitsz.application.Main`。

3. **运行单元测试**:
   先运行 `Compile Tests`，再执行 `Run JUnit5 Tests`。

## 📝 目录结构简述

- `src/edu/hitsz/application`: 系统的启动入口、主调度面板及 UI (Frame/Panels) 的定义。
- `src/edu/hitsz/aircraft`: 存放英雄机及所有敌方飞行器的定义。
- `src/edu/hitsz/bullet` & `src/edu/hitsz/item`: 弹药与道具的抽象及具体实现。
- `src/edu/hitsz/shootstrategy`: 策略模式的弹道算法簇。
- `src/edu/hitsz/factory`: 负责各种实体创建的工厂集合。
- `src/edu/hitsz/dao`: 负责数据读写及排行榜存储链路的设计。
- `test/`: 单元测试源码。
- `docs/`: 各实验详尽的工作流记录、设计阐述与踩坑避坑笔记。