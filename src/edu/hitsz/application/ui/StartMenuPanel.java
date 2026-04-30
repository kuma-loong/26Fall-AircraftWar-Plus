package edu.hitsz.application.ui;

import edu.hitsz.application.GameMainFrame;
import edu.hitsz.dao.GameDifficulty;

import javax.swing.*;
import java.awt.*;

/**
 * 游戏开始菜单面板
 */
public class StartMenuPanel extends JPanel {
    private final GameMainFrame mainFrame;

    public StartMenuPanel(GameMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // 创建三个难度按钮
        JButton btnEasy = createMenuButton("简单模式");
        JButton btnNormal = createMenuButton("普通模式");
        JButton btnHard = createMenuButton("困难模式");

        // 绑定事件
        btnEasy.addActionListener(e -> mainFrame.startGame(GameDifficulty.EASY));
        btnNormal.addActionListener(e -> mainFrame.startGame(GameDifficulty.NORMAL));
        btnHard.addActionListener(e -> mainFrame.startGame(GameDifficulty.HARD));

        // 居中添加按钮，并设置间距
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(btnEasy);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(btnNormal);
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(btnHard);
        buttonPanel.add(Box.createVerticalGlue());

        add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("SansSerif", Font.PLAIN, 24));
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        return button;
    }
}
