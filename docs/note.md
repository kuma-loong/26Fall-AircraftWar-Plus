# 项目注意事项

1. 当前阶段是实验一，严格控制范围，不提前实现后续迭代复杂机制。
2. `HeroAircraft` 必须保持 DCL 单例，不允许外部 `new HeroAircraft(...)`。
3. 集合遍历中通过 `vanish()` + `removeIf()` 清理，避免并发修改异常。
4. 控制台输出仅用于实验一要求的模拟效果：
   - `FireSupply active!`
   - `FirePlusSupply active!`
5. 本次未执行 UML 同步，需在后续专门任务中补齐。
6. 实验二阶段要求：
   - 敌机创建走工厂方法模式，不在 `Game` 里直接 `new` 敌机。
   - 道具创建走简单工厂模式，按敌机类型选择掉落池。
7. 实验二模拟输出（非加血道具）：
   - `FireSupply active!`
   - `FirePlusSupply active!`
   - `BombSupply active!`
   - `FreezeSupply active!`
8. 精锐/王牌需具备横向移动能力，依赖 `AbstractFlyingObject.forward()` 的触边反向逻辑。
9. 实验三开始，弹道逻辑统一走策略模式：
   - 禁止在具体飞机类内继续硬编码弹道 `shoot()` 分支。
   - 通过 `AbstractAircraft#setShootStrategy(...)` 注入算法对象。
10. 实验三火力道具切换后的弹道状态不自动恢复，恢复机制留到实验五多线程阶段。
11. 实验三 Boss 机制要求：
   - 分数阈值触发 Boss；
   - 同屏最多 1 台 Boss；
   - Boss 仅左右移动；
   - 阵亡固定掉落 3 个随机道具。
12. 实验四排行榜要求：
   - 必须采用 DAO 模式拆分数据访问与业务展示逻辑；
   - 每个难度使用独立文件保存记录；
   - 当前默认难度为 `NORMAL`，默认玩家名为 `Player`；
   - 排行榜仅输出到控制台，不新增 Swing 界面。
13. 实验四测试要求：
   - 使用 JUnit5；
   - 至少覆盖 `HeroAircraft` 的 3 个方法（可含父类方法）；
   - 文件读写测试优先使用临时目录，避免污染正式数据文件。
14. 实验五非 UI 约束：
   - 音效播放使用独立 `MusicThread`，循环音乐必须支持中途停止；
   - 音频异常不能导致主循环崩溃；
   - `FireItem` / `SuperFireItem` 使用 `Runnable + Thread.sleep()` 实现限时恢复。
15. 实验五连续火力覆盖规则：
   - 只允许最后一次火力效果在线程结束后恢复默认直射；
   - 旧计时线程不得提前覆盖新的火力策略。
16. 实验五未实现 UI：
   - 难度选择界面、`CardLayout`、`JTable`、姓名输入、删除按钮继续留待后续任务。
17. 实验六开始，`BombItem` / `FreezeItem` 已改为真实效果，不再保留模拟输出。
18. `Game` 已改为模板基类：
   - 禁止重新把三档难度逻辑塞回单个 `Game` 分支；
   - 难度差异必须优先放到 `EasyGame`、`NormalGame`、`HardGame`。
19. 实验六观察者模式约束：
   - 道具只负责广播效果，不直接处理得分与掉落；
   - 炸弹结分与禁掉落由 `Game` 统一结算；
   - 每次道具触发后必须清空观察者，避免脏引用。
20. 实验六运动状态约束：
   - 冻结/减速统一走 `AbstractFlyingObject`；
   - 不要在具体敌机/子弹类里重复实现独立计时恢复。
