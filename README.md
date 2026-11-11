# AircraftWar Plus

这是哈尔滨工业大学（深圳）《软件构造实践》课程的综合实验项目，基于 2026 春季学期完成的 [Java/Swing 飞机大战](https://github.com/kuma-loong/AircraftWar/)，开发面向 Android 平台的纵向卷轴射击游戏，由本人独立完成。

项目通过迁移既有游戏功能，实践 **Android 应用开发**、**面向对象设计**、**数据存储**及**网络通信**，并扩展联网竞赛和创新玩法。

## 🛠 技术栈

- **开发语言**：Java（JDK 17），Kotlin 的引入待后续确定。
- **目标平台**：Android，最低支持 Android 8.0（API 26），当前编译及目标 API 为 35。
- **构建工具**：Gradle 8.11.1、Android Gradle Plugin 8.9.2。
- **测试框架**：JUnit 5（桌面代码回归测试）。
- **开发环境**：Linux 工作站、VS Code Remote SSH；Mac 运行 Android 模拟器。

## 🚀 迭代里程碑（实验特性）

当前已完成工作站环境配置、Android 工程初始化与桌面代码清理；Android 应用目前包含启动验证页面，游戏功能正在迁移准备阶段。

### 实验一：Android 开发技术与项目初始化

- 完成项目任务书，明确功能迁移及网络功能设计。
- 配置独立管理的 Java、Android SDK 和命令行开发工具。
- 建立 Android 工程、项目说明与开发计划。

### 实验二：代码迁移

- 迁移飞行器、子弹、碰撞检测、敌机生成及道具效果。
- 保留工厂、策略、观察者等已有设计结构。
- 迁移难度递进、Boss 战及连击计分规则。

### 实验三：界面与音效

- 实现移动端菜单、触摸操控、游戏绘制及结算界面。
- 适配背景音乐、Boss 音乐和动作音效。
- 处理游戏暂停、恢复及资源释放。

### 实验四：排行榜与存储

- 实现按难度区分的本地排行榜。
- 保存游戏成绩及设置，支持离线使用。

### 实验五：网络对战

- 实现创建房间、加入房间及准备流程。
- 双方独立运行游戏，实时展示比分和比赛状态。
- 实现比赛结果展示及退出、断线处理。

### 实验六：扩展功能

- **每日挑战与在线排行榜**：获取统一挑战配置，上传成绩并查询当日排名。
- **连击能量与主动护盾**：通过连击积累能量，释放限时护盾抵挡一次致命伤害。

### 实验七：结题验收

- 完成游戏流程、存储与双客户端网络交互验证。
- 整理源码、APK、运行说明及结题报告。

## 🎮 运行说明

以下命令在项目根目录执行，工作站环境通过脚本启用。

### 1. 启用开发环境

```bash
source scripts/env.sh
```

新 Linux x86_64 工作站可通过 `python3 scripts/setup-environment.py` 安装所需工具。

### 2. 编译桌面代码并运行测试

```bash
bash scripts/check-desktop.sh
```

VS Code 中也可运行 `Check Desktop` 任务。在具有图形显示的环境中，可运行 `Run Game` 任务，或执行：

```bash
java -cp out/production edu.hitsz.application.Main
```

### 3. 构建 Android 应用

```bash
bash scripts/android-build.sh assembleDebug
```

生成的 APK 位于 `android/app/build/outputs/apk/debug/app-debug.apk`。下载至 Mac 并启动 Android 模拟器后，在 Mac 执行：

```bash
adb install -r app-debug.apk
```

## 📝 目录结构简述

- `src/edu/hitsz/application`：桌面版入口、游戏调度、界面及音频管理。
- `src/edu/hitsz/aircraft`：英雄机与各类敌机。
- `src/edu/hitsz/bullet`、`src/edu/hitsz/item`：子弹、道具及效果规则。
- `src/edu/hitsz/shootstrategy`、`src/edu/hitsz/factory`：射击策略与实体工厂。
- `src/edu/hitsz/dao`：排行榜记录与数据访问。
- `test/`：桌面代码回归测试。
- `android/`：Android 应用工程。
- `scripts/`：环境配置、构建及验证脚本。
- `docs/`：本学期工作记录。
- `archive/2026-spring/`：上学期项目资料。
- [PLAN.md](PLAN.md)：本学期开发计划。
