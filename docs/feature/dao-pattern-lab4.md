# Feature：实验四 DAO 排行榜与单元测试

## 1. DAO 模式落地
- 值对象：`ScoreRecord`，封装玩家名、分数、记录时间。
- DAO 接口：`ScoreRecordDao`，定义查询、新增、删除记录方法。
- DAO 实现：`FileScoreRecordDao`，负责 TSV 文件读写。
- 服务层：`ScoreBoardService`，负责排序、名次计算和控制台打印。

## 2. 难度分文件存储
- `GameDifficulty` 提供 `EASY`、`NORMAL`、`HARD` 三个难度。
- 对应文件：
  - `data/ranking-easy.tsv`
  - `data/ranking-normal.tsv`
  - `data/ranking-hard.tsv`
- 当前 `Game` 默认使用 `NORMAL`，但已预留构造器参数支持后续难度接入。

## 3. 游戏结束接入
- `Game.checkResultAction()` 在英雄机死亡时仅触发一次结算。
- 默认玩家名固定为 `Player`。
- 结算时自动写入当前得分，并在控制台打印当前难度排行榜。

## 4. 单元测试覆盖
- `HeroAircraftTest`
  - 验证 `getInstance()` 单例性；
  - 验证父类 `setLocation()` / 位置读取；
  - 验证默认、火力、超级火力三种射击策略的子弹数量。
- `FileScoreRecordDaoTest`
  - 验证新增与读取；
  - 验证空文件读取；
  - 验证按名次删除；
  - 验证不同难度文件路径分离。
