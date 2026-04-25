package edu.hitsz.application;

import edu.hitsz.dao.FileScoreRecordDao;
import edu.hitsz.dao.GameDifficulty;
import edu.hitsz.dao.ScoreBoardService;
import edu.hitsz.dao.ScoreRecord;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 游戏结束后展示排行榜数据的面板
 */
public class LeaderboardPanel extends JPanel {
    private final GameMainFrame mainFrame;
    private final JLabel difficultyLabel;

    private GameDifficulty difficulty;
    private ScoreBoardService scoreBoardService;
    private final JTable scoreTable;
    private final DefaultTableModel tableModel;

    public LeaderboardPanel(GameMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // 顶部标题
        JPanel topPanel = new JPanel();
        difficultyLabel = new JLabel();
        difficultyLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        topPanel.add(difficultyLabel);

        // 初始化表格模型
        String[] columnNames = {"名次", "玩家姓名", "得分", "记录时间"};
        tableModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // 禁止双击编辑单元格
            }
        };

        scoreTable = new JTable(tableModel);
        scoreTable.setRowHeight(25);
        // 使列宽合适展示数据
        scoreTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        scoreTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        scoreTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        scoreTable.getColumnModel().getColumn(3).setPreferredWidth(160);

        JScrollPane scrollPane = new JScrollPane(scoreTable);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 底部操作区
        JPanel bottomPanel = new JPanel();
        JButton deleteBtn = new JButton("删除选中记录");
        JButton startMenuBtn = new JButton("返回主菜单");

        // 删除按钮点击事件
        deleteBtn.addActionListener(e -> {
            int selectedRow = scoreTable.getSelectedRow();
            if (selectedRow != -1) {
                int option = JOptionPane.showConfirmDialog(this, 
                        "是否确定删除该记录？", "确认删除", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    // 第 0 列是排名，即 1-based 的名次
                    String rankStr = (String) tableModel.getValueAt(selectedRow, 0);
                    int rank = Integer.parseInt(rankStr);
                    
                    if (scoreBoardService != null && scoreBoardService.deleteRecordByRank(rank)) {
                        // 后端数据删除成功后刷新界面
                        updateTableData();
                    } else {
                        JOptionPane.showMessageDialog(this, "删除失败，记录不存在！", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "请选择一条要删除的记录！", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 导航按钮回到初始界面
        startMenuBtn.addActionListener(e -> mainFrame.showStartMenu());

        bottomPanel.add(deleteBtn);
        bottomPanel.add(startMenuBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * 切换并展示指定难度排行榜。
     * @param difficulty 当前难度
     * @param scoreBoardService 外部注入的排行榜服务
     */
    public void showLeaderboard(GameDifficulty difficulty, ScoreBoardService scoreBoardService) {
        this.difficulty = difficulty == null ? GameDifficulty.NORMAL : difficulty;
        this.scoreBoardService = scoreBoardService == null
                ? new ScoreBoardService(this.difficulty, new FileScoreRecordDao(this.difficulty))
                : scoreBoardService;
        difficultyLabel.setText("难度: " + this.difficulty.name());
        updateTableData();
    }

    /**
     * 重新拉取排行榜服务层数据并渲染到表格。
     */
    public void updateTableData() {
        // 清空模型
        tableModel.setRowCount(0);
        if (scoreBoardService == null) {
            return;
        }

        List<ScoreRecord> records = scoreBoardService.getSortedRecords();
        int rank = 1;
        for (ScoreRecord record : records) {
            tableModel.addRow(new Object[]{
                String.valueOf(rank),
                record.getPlayerName(),
                record.getScore(),
                record.formatRecordTime()
            });
            rank++;
        }
    }
}
