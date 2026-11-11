# 工作记录

## 2026-11-11 初始化

依据根目录 init_todo.md 执行：

- 工具隔离安装至 ~/.local/share/aircraftwar；不使用 sudo，不修改系统 Java 和 shell 启动配置。
- 安装 Temurin 17.0.21+9、Android command-line tools 19.0、Platform 35、Build Tools 35.0.0、ADB 37.0.1、gh 2.103.3。版本/下载校验固定在 scripts/tools-lock.json；提供可重复安装入口。
- 配置显式环境脚本，修正旧 VS Code 配置中的 Mac/JDK25 绝对路径；Gradle 使用 8.11.1 Wrapper 及分发校验和。
- 旧文档、UML、实验指导材料移动到 archive/2026-spring，保留历史；重写 README、PLAN、AGENTS，补充环境说明。
- origin 指向新仓库，旧仓库改为 upstream；未认证、提交、推送。保留既有 Git 历史，新远端访问权限待用户登录后核实。
- 移除自定义背景菜单、图片导入/扫描、配置类、按图片尺寸启动窗口的逻辑及专属测试；保留内置难度背景、逻辑边界和连击。
- 修复原 GameTemplateModeTest 对跨包子类调用包可见辅助方法的编译问题：通过 Game 类型访问，不扩大生产接口。
- 增加 Android Java 启动工程，AGP 8.9.2、SDK35、minSDK26。仅提供迁移中的启动页面，尚无 Android 游戏功能；语言未切换 Kotlin。

### 验证

- bash scripts/check-desktop.sh：编译通过，28/28 测试通过，包括连击、道具、难度与 DAO。
- bash scripts/check-performance.sh：在 Swing EDT 上，三档难度各预热100帧，执行2000帧更新与512×768离屏绘制。EASY 0.474、NORMAL 0.493、HARD 0.508 ms/帧；受控场景中英雄机移至画面外，无击杀及声音事件。这不是完整压力测试、真实帧率或迁移前后对比。
- 工作站资源检查：31GiB内存、约28GiB可用，24GiB交换空间未使用，工作区所在盘约384GiB可用（安装前采样）。
- bash scripts/android-build.sh assembleDebug lintDebug：43项任务执行成功；APK签名校验通过。Lint 无错误，2项占位工程警告：备份规则未显式提供、未设置应用图标。正式界面阶段处理。
- git diff --check、shell语法检查、安装脚本Python语法检查通过。
- 人工代码检查：自定义背景相关符号在活动源码和测试中无残留；移除专属测试未掩盖其余测试失败。

### 验证边界

本机无GUI、无连接设备，因此未运行APK和人工试玩。APK安装、启动、触摸体验和双模拟器网络交互须在Mac模拟器继续验证。当前占位Android工程尚未接入桌面规则。单次离屏冒烟结果仅说明该场景未见明显阻塞，不宣称整体性能或真机兼容性已验收。
