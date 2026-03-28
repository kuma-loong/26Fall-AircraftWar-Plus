# 项目注意事项

1. 当前阶段是实验一，严格控制范围，不提前实现后续迭代复杂机制。
2. `HeroAircraft` 必须保持 DCL 单例，不允许外部 `new HeroAircraft(...)`。
3. 集合遍历中通过 `vanish()` + `removeIf()` 清理，避免并发修改异常。
4. 控制台输出仅用于实验一要求的模拟效果：
   - `FireSupply active!`
   - `FirePlusSupply active!`
5. 本次未执行 UML 同步，需在后续专门任务中补齐。
