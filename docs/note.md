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
