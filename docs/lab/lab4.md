# 实验四：DAO模式和单元测试

## 一、实验目的
1. 深入理解数据访问对象（DAO）模式的设计意图及结构。
2. 结合《飞机大战》案例，规范绘制 DAO 模式 UML 结构类图。
3. 掌握通过重构实现 DAO 模式的方法。
4. 理解单元测试的作用，并使用 JUnit5 完成核心类测试。

## 二、实验环境
- IDE：VS Code
- JDK：OpenJDK 25
- 测试框架：JUnit5 Console Standalone

## 三、实验四实际完成情况

### 1. DAO 模式重构
- 新增 `edu.hitsz.dao` 包，包含：
  - `ScoreRecord`
  - `ScoreRecordDao`
  - `FileScoreRecordDao`
  - `GameDifficulty`
  - `ScoreBoardService`
- 排行榜持久化采用 TSV 文件实现，业务排序与控制台展示从文件读写逻辑中分离。

### 2. 文件读写机制
- 难度文件已拆分为：
  - `data/ranking-easy.tsv`
  - `data/ranking-normal.tsv`
  - `data/ranking-hard.tsv`
- 当前游戏固定 `NORMAL` 难度，后续若加入难度选择，可直接向 `Game(GameDifficulty difficulty)` 传参。

### 3. 游戏接入结果
- `Game` 新增当前难度字段与排行榜服务字段。
- 游戏结束后仅在首次结算时：
  - 输出 `Game Over!`
  - 以默认玩家名 `Player` 保存当前分数
  - 在控制台打印当前难度排行榜

### 4. 单元测试完成情况
- 新增 `test/edu/hitsz/aircraft/HeroAircraftTest.java`
- 新增 `test/edu/hitsz/dao/FileScoreRecordDaoTest.java`
- 已覆盖：
  - `HeroAircraft.getInstance()`
  - 父类 `setLocation()` / `getLocationX()` / `getLocationY()`
  - 默认、火力、超级火力策略下 `shoot()` 子弹数量
  - DAO 新增、读取、空文件读取、按名次删除、难度文件路径分离

### 5. VS Code + JDK25 + JUnit5 配置
- 新增 `.vscode/settings.json`：
  - 指定 JDK 25 路径 `/opt/homebrew/Cellar/openjdk/25.0.2/libexec/openjdk.jdk/Contents/Home`
  - 指定 `src` / `test` 源码目录
  - 指定 `lib/**/*.jar` 作为引用库
- 新增 `.vscode/tasks.json`：
  - `Compile Source`
  - `Run Game`
  - `Compile Tests`
  - `Run JUnit5 Tests`

## 四、UML 交付
- 新增 `uml/Lab4Dao.puml`
- 图中显式标注 `<<DAO Pattern>>`，展示了值对象、DAO 接口、DAO 实现、服务层与 `Game` 的关系。

## 五、约束遵守说明
- 未新增 Swing 排行榜界面，排行榜仅控制台输出。
- 未新增玩家输入逻辑，默认玩家名为 `Player`。
- HeroAircraft 仍保持 DCL 单例。
- 排行榜按难度分文件存储，符合实验四核心约束。

## 六、验证计划
1. 编译源码：
   - `find src/edu/hitsz -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -d out/production`
2. 编译测试：
   - `find test -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -cp lib/junit-platform-console-standalone-1.12.2.jar:out/production -d out/test`
3. 运行测试：
   - `java -jar lib/junit-platform-console-standalone-1.12.2.jar --class-path out/test:out/production --scan-class-path`
4. 手动验收：
   - 运行游戏并结束一局，控制台应打印 `Game Over!` 与 `NORMAL` 难度排行榜。
