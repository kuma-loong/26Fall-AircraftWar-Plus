package edu.hitsz.application.ui;

import edu.hitsz.application.GameMainFrame;
import edu.hitsz.application.GameSettings;
import edu.hitsz.application.BackgroundProfile;
import edu.hitsz.application.ImageManager;
import edu.hitsz.dao.GameDifficulty;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 游戏开始菜单面板
 */
public class StartMenuPanel extends JPanel {
    private final GameMainFrame mainFrame;
    private final JComboBox<BackgroundProfile> backgroundSelector;
    private final JCheckBox autoViewportCheckBox;

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
        JButton refreshBackgroundBtn = createMenuButton("刷新背景列表");
        backgroundSelector = new JComboBox<>();
        autoViewportCheckBox = new JCheckBox("按图片尺寸启动窗口");
        autoViewportCheckBox.setOpaque(false);
        autoViewportCheckBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        autoViewportCheckBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        backgroundSelector.setAlignmentX(Component.CENTER_ALIGNMENT);
        backgroundSelector.setMaximumSize(new Dimension(280, 36));
        refreshBackgroundProfiles();

        // 绑定事件
        btnEasy.addActionListener(e -> mainFrame.startGame(buildSettings(GameDifficulty.EASY)));
        btnNormal.addActionListener(e -> mainFrame.startGame(buildSettings(GameDifficulty.NORMAL)));
        btnHard.addActionListener(e -> mainFrame.startGame(buildSettings(GameDifficulty.HARD)));
        refreshBackgroundBtn.addActionListener(e -> refreshBackgroundProfiles());

        // 居中添加按钮，并设置间距
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(createSectionLabel("背景设置"));
        buttonPanel.add(backgroundSelector);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(autoViewportCheckBox);
        buttonPanel.add(Box.createVerticalStrut(12));
        buttonPanel.add(refreshBackgroundBtn);
        buttonPanel.add(Box.createVerticalStrut(28));
        buttonPanel.add(createSectionLabel("选择难度"));
        buttonPanel.add(Box.createVerticalStrut(12));
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

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        return label;
    }

    private GameSettings buildSettings(GameDifficulty difficulty) {
        BackgroundProfile profile = (BackgroundProfile) backgroundSelector.getSelectedItem();
        return new GameSettings(difficulty, profile, autoViewportCheckBox.isSelected());
    }

    private void refreshBackgroundProfiles() {
        BackgroundProfile previousSelection = (BackgroundProfile) backgroundSelector.getSelectedItem();
        DefaultComboBoxModel<BackgroundProfile> model = new DefaultComboBoxModel<>();
        List<BackgroundProfile> profiles = ImageManager.getAvailableBackgroundProfiles();
        for (BackgroundProfile profile : profiles) {
            model.addElement(profile);
        }
        backgroundSelector.setModel(model);
        if (previousSelection != null) {
            model.setSelectedItem(previousSelection);
        }
    }
}
