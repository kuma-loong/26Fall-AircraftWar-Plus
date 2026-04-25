# 软件构造实验指导书 - 实验五：Swing 和多线程

## 0. 本仓库本轮实际完成范围

### 已完成（非 UI）
- 音效多线程：
  - 普通 BGM 循环播放；
  - Boss 出现切换 Boss BGM；
  - Boss 阵亡恢复普通 BGM；
  - 子弹击中、拾取道具、炸弹触发、游戏结束音效均已接入。
- 火力道具限时恢复：
  - `FireItem` / `SuperFireItem` 使用 `Runnable + Thread.sleep()` 实现限时火力；
  - 默认持续时间 `10000ms`；
  - 连续拾取时，以最后一次火力效果为准，旧线程不会提前恢复默认策略。
- 难度非 UI 保留：
  - `Game(GameDifficulty difficulty)` 继续可用；
  - 背景图已支持按难度切换；
  - 默认 `new Game()` 仍为 `NORMAL`。
- 排行榜数据逻辑保持实验四行为：
  - 继续按难度分文件；
  - 游戏结束默认用 `Player` 记分并控制台输出。

### 本轮明确跳过
- 难度选择界面
- `CardLayout` 页面切换
- 排行榜 `JTable`
- 姓名输入弹窗
- 排行榜删除按钮 UI

### 验收边界说明
- 本轮仅覆盖实验五的多线程与音效非 UI 目标。
- 不提前实现实验六的难度参数细化（敌机血量、速度、生成频率、射击频率等）。

## 1. 实验目的
1. **熟悉 Java 图形界面程序设计**：掌握 Java Swing 中的容器、常用组件和布局管理器（特别是 `CardLayout` 和 `JTable`）的使用方法。
2. **理解 Java 事件处理机制**：掌握界面事件响应（如 `ActionListener`）及前端交互与后端逻辑分离的基本方法。
3. **理解 Java 多线程编程**：理解多线程的概念和生命周期，掌握多线程的实现方法（实现 `Runnable` 接口与继承 `Thread` 类）。

## 2. 实验环境
* **操作系统**：macOS
* **开发工具**：Visual Studio Code
* **运行环境**：OpenJDK 25

---

## 3. 实验内容与核心任务（迭代目标 Checklist）

本次实验基于“飞机大战”游戏项目进行功能迭代，主要包含以下四个核心任务：

### 任务一：难度选择界面
* **功能描述**：实现游戏启动前的难度选择功能（简单、普通、困难）。
* **交互逻辑**：玩家点击对应难度按钮后，界面发生切换，并显示该难度对应的游戏地图。

### 任务二：音效控制机制（多线程）
* **功能描述**：为游戏添加完整的音频支持。
* **触发机制**：
  * **背景音乐**：游戏运行期间循环播放，游戏结束后停止。
  * **Boss 音乐**：Boss 敌机出场时循环播放其专属背景音乐；Boss 坠毁或游戏结束时，停止该专属音乐并恢复普通背景音乐（或直接结束）。
  * **动作音效**：实现子弹击中敌机、炸弹爆炸、道具生效、游戏结束等事件触发时的对应短促音效播放。

### 任务三：火力道具生效时长控制（多线程）
* **功能描述**：完善游戏中的两种火力道具功能。
* **逻辑要求**：当道具生效时，英雄机切换发射弹道（例如变为多发散弹）。该状态需**持续指定的时长**，时长结束后，英雄机必须**自动恢复**为默认的直射状态。

### 任务四：得分排行榜界面与数据交互
* **功能描述**：游戏结束后的数据统计与展示。
* **交互逻辑**：
  * 游戏结束后，**自动弹出**得分排行榜界面。
  * 同步弹出输入窗口（Dialog），供玩家填写姓名。
  * 支持玩家记录本局得分到排行榜中。
  * 支持在排行榜界面中选中并**删除任意一条历史得分记录**。
  * 附加要求：排行榜需按游戏难度分类展示对应玩家的得分排名。

## 3.1 当前代码状态对照
- 任务一：未实现，本轮跳过 UI。
- 任务二：已完成非 UI 音频线程与事件接线。
- 任务三：已完成限时火力恢复线程。
- 任务四：仅保留 DAO 数据层与控制台输出，未实现 Swing 展示层。

---

## 4. 实验要求与关键约束

1. **难度逻辑的约束**：**无需细化三种难度的具体差异**（如敌机血量、生成频率等），仅需根据选择的难度替换对应的游戏背景地图即可。三种难度的详细逻辑将在实验六中利用“模板模式”完善。
2. **音效模块的解耦**：原项目提供的 `MusicThread` 仅支持单次播放且无法中途停止。**强烈建议设计独立的「音频管理类 (AudioManager)」**，将播放、循环、停止等多线程底层细节进行统一封装，仅向游戏主类暴露简洁的调用接口（如 `AudioManager.playBGM()`, `AudioManager.stopBGM()`），降低模块耦合，提升代码可维护性。
3. **MVC 模式的应用**：在使用 Swing 构建界面时（特别是排行榜 `JTable`），必须采用 MVC 模式设计，实现 GUI 组件的显示逻辑和数据逻辑的分离。
4. **多线程方式的运用**：需同时展现两种多线程实现方式：
   * 使用 `Runnable` 接口（推荐使用 Lambda 表达式简化）来实现道具时长的控制。
   * 继承 `Thread` 类来完善 `MusicThread` 并控制音效。

---

## 5. 技术指南与步骤适配（VS Code 环境专用）

由于不使用 IDEA 的 UI 拖拽工具，您需要在 VS Code 中通过纯代码实现布局。以下是核心技术点的代码级示范：

### 5.1 CardLayout 卡片布局（用于界面切换）
`CardLayout` 以时间而非空间来管理组件，多个面板（如“主菜单”、“难度选择”、“游戏主界面”）共享同一个窗口，非常适合做界面路由。

```java
import javax.swing.*;
import java.awt.*;

public class GameMainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainCardPanel;

    public GameMainFrame() {
        setTitle("飞机大战 - macOS VSCode版");
        setSize(480, 700); // 假设的游戏窗口大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 1. 初始化 CardLayout 和 承载容器
        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);

        // 2. 创建不同的页面 Panel
        JPanel startMenuPanel = createStartMenuPanel();
        JPanel gamePanel = new JPanel(); // 假定的游戏运行主界面
        gamePanel.setBackground(Color.BLACK); 

        // 3. 将页面加入容器，并为其命名
        mainCardPanel.add(startMenuPanel, "START_MENU");
        mainCardPanel.add(gamePanel, "GAME_PANEL");

        add(mainCardPanel);
        setVisible(true);
    }

    private JPanel createStartMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10)); // 简单网格布局排布按钮
        JButton easyBtn = new JButton("简单模式");
        JButton normalBtn = new JButton("普通模式");
        JButton hardBtn = new JButton("困难模式");

        // 4. 为按钮添加事件监听器，触发 CardLayout 页面切换
        easyBtn.addActionListener(e -> {
            // TODO: 设置游戏难度标志、切换对应地图
            cardLayout.show(mainCardPanel, "GAME_PANEL"); // 切换到游戏界面
        });

        panel.add(easyBtn);
        panel.add(normalBtn);
        panel.add(hardBtn);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameMainFrame());
    }
}
```

### 5.2 JTable 排行榜组件（MVC 模式）
`JTable` 并不存储自己的数据，而是从 `TableModel` 那里获取数据。在添加删除功能时，必须操作 Model 而非 Table。

```java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LeaderboardPanel extends JPanel {
    private JTable scoreTable;
    private DefaultTableModel tableModel;

    public LeaderboardPanel() {
        setLayout(new BorderLayout());

        // 1. 定义表头和初始数据
        String[] columnNames = {"名次", "玩家姓名", "得分", "记录时间"};
        Object[][] data = {
            {"1", "Lily", "800", "04-11 18:15"},
            {"2", "Jane", "500", "01-25 16:33"}
        };

        // 2. 创建 TableModel (重写 isCellEditable 禁止直接双击编辑内容)
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; 
            }
        };

        // 3. 创建 JTable 并装载 Model
        scoreTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(scoreTable);
        add(scrollPane, BorderLayout.CENTER);

        // 4. 创建底部删除按钮区
        JPanel bottomPanel = new JPanel();
        JButton deleteBtn = new JButton("删除选中记录");
        
        deleteBtn.addActionListener(e -> {
            int selectedRow = scoreTable.getSelectedRow();
            if (selectedRow != -1) {
                int result = JOptionPane.showConfirmDialog(this, "是否确定删除该记录？", "确认", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(selectedRow); // MVC模式：操作Model，View会自动更新
                }
            } else {
                JOptionPane.showMessageDialog(this, "请先选择一行记录！");
            }
        });
        
        bottomPanel.add(deleteBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    // 提供给外部调用的添加记录方法
    public void addScoreRecord(String name, String score, String time) {
        int rank = tableModel.getRowCount() + 1; // 简单的名次计算逻辑
        tableModel.addRow(new Object[]{String.valueOf(rank), name, score, time});
    }
}
```

### 5.3 玩家姓名输入与弹窗交互
使用 `JOptionPane` 可以在游戏结束时方便地弹出输入框：
```java
// 游戏结束时的逻辑处理
String playerName = JOptionPane.showInputDialog(gameWindow, "游戏结束！您的得分为: " + currentScore + "\n请输入玩家姓名：");
if (playerName != null && !playerName.trim().isEmpty()) {
    // 将玩家成绩记录到排行榜 Model 中
}
// 随后使用 CardLayout 切换到排行榜界面
```

### 5.4 多线程：控制道具生效时长（Runnable）
由于是控制时长，使用 `Runnable` 配合 `Thread.sleep` 是一种方式（也可以使用 `java.util.Timer`）。
```java
public void activateFirepowerProp(int durationMillis) {
    // 1. 立即改变弹道状态
    heroAircraft.setFireMode(FireMode.SCATTER);

    // 2. 开启新线程计时，避免阻塞主游戏循环
    Runnable propTimer = () -> {
        try {
            // 线程休眠指定的道具生效时间
            Thread.sleep(durationMillis); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 3. 时长结束后，恢复默认弹道
        heroAircraft.setFireMode(FireMode.STRAIGHT);
    };

    new Thread(propTimer, "Firepower-Prop-Timer").start();
}
```

### 5.5 多线程：封装音效管理类（Thread）
基于原 `MusicThread`（继承自 `Thread` 类），在 VS Code 中新建一个 `AudioManager` 单例或静态类：

```java
import java.io.InputStream;
// 根据实际音频库（如 javax.sound.sampled.*）引入包

public class AudioManager {
    private static MusicThread bgmThread;
    private static MusicThread bossBgmThread;

    // 播放背景音乐（需修改 MusicThread 使其支持循环和打断）
    public static void playBGM(String filepath) {
        if (bgmThread != null && bgmThread.isAlive()) {
            bgmThread.stopMusic(); // 自定义方法，内部修改标志位打断循环
        }
        bgmThread = new MusicThread(filepath, true); // true 代表循环播放
        bgmThread.start();
    }

    public static void stopBGM() {
        if (bgmThread != null) bgmThread.stopMusic();
    }

    // 播放短促音效（单次播放，不阻塞）
    public static void playSoundEffect(String filepath) {
        new MusicThread(filepath, false).start();
    }
}

// 提示：需要在 MusicThread 的 run() 方法中加入循环标志位 (volatile boolean isPlaying)
// 并在流读取/播放的 while 循环中检测该标志位以实现随时停止。
```

---

## 6. 实验完成验收标准
开发完成后，请自测以下行为是否符合要求：
1. [ ] 启动游戏进入难度选择 UI，点击不同难度，游戏背景图正确切换。
2. [ ] 游戏全过程包含 BGM（持续循环），击中、爆炸、吃道具时有对应短音效。
3. [ ] Boss 出现时 BGM 无缝切换为 Boss 专属音乐，Boss 死亡后恢复或停止。
4. [ ] 吃到火力道具后，弹道改变，**X 秒后**，无需玩家操作，弹道自动恢复原状。
5. [ ] 英雄机坠毁（游戏结束）后，**自动**弹出输入姓名的弹窗。
6. [ ] 提交姓名后，界面切换到排行榜 UI，刚刚输入的名字和分数出现在列表内。
7. [ ] 在排行榜中点击任意一行，点击“删除”，该行消失且不报错。
