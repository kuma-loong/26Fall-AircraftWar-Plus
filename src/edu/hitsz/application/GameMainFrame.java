package edu.hitsz.application;

import edu.hitsz.application.mode.EasyGame;
import edu.hitsz.application.mode.GameOverInfo;
import edu.hitsz.application.mode.HardGame;
import edu.hitsz.application.mode.NormalGame;
import edu.hitsz.application.ui.LeaderboardPanel;
import edu.hitsz.application.ui.StartMenuPanel;
import edu.hitsz.dao.FileScoreRecordDao;
import edu.hitsz.dao.GameDifficulty;
import edu.hitsz.dao.ScoreBoardService;

import javax.swing.*;
import java.awt.*;

/**
 * 游戏主窗口，采用 CardLayout 管理多面板（开始菜单、游戏界面、排行榜）
 */
public class GameMainFrame extends JFrame {
    private static final String START_MENU_CARD = "START_MENU";
    private static final String GAME_CARD = "GAME_PANEL";
    private static final String LEADERBOARD_CARD = "LEADERBOARD";
    private static final String DEFAULT_PLAYER_NAME = "Player";

    private final CardLayout cardLayout;
    private final JPanel mainCardPanel;
    private final StartMenuPanel startMenuPanel;
    private final LeaderboardPanel leaderboardPanel;

    private Game currentGame;

    public GameMainFrame() {
        setTitle("Aircraft War");
        setResizable(false);
        applyWindowBounds();

        // 设置窗口的大小和位置,居中放置
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);

        startMenuPanel = new StartMenuPanel(this);
        leaderboardPanel = new LeaderboardPanel(this);
        mainCardPanel.add(startMenuPanel, START_MENU_CARD);
        mainCardPanel.add(leaderboardPanel, LEADERBOARD_CARD);

        setContentPane(mainCardPanel);
        setVisible(true);
    }

    /**
     * 启动游戏界面
     * @param settings 当前启动配置
     */
    public void startGame(GameSettings settings) {
        GameSettings safeSettings = settings == null
                ? new GameSettings(GameDifficulty.NORMAL, BackgroundProfile.defaultProfile(), false)
                : settings;
        configureViewport(safeSettings);
        replaceCurrentGame(createGame(safeSettings));
        currentGame.setOnGameOver(this::handleGameOver);
        cardLayout.show(mainCardPanel, GAME_CARD);
        currentGame.start();
    }

    /**
     * 返回主菜单
     */
    public void showStartMenu() {
        cardLayout.show(mainCardPanel, START_MENU_CARD);
    }

    private void handleGameOver(GameOverInfo gameOverInfo) {
        GameDifficulty difficulty = gameOverInfo.difficulty();
        ScoreBoardService scoreBoardService =
                new ScoreBoardService(difficulty, new FileScoreRecordDao(difficulty));
        String playerName = JOptionPane.showInputDialog(
                this,
                "游戏结束！您的得分为: " + gameOverInfo.score() + "\n请输入玩家姓名：",
                "游戏结束",
                JOptionPane.QUESTION_MESSAGE
        );
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = DEFAULT_PLAYER_NAME;
        }
        scoreBoardService.recordScore(playerName, gameOverInfo.score());
        leaderboardPanel.showLeaderboard(difficulty, scoreBoardService);
        cardLayout.show(mainCardPanel, LEADERBOARD_CARD);
    }

    private void replaceCurrentGame(Game nextGame) {
        if (currentGame != null) {
            mainCardPanel.remove(currentGame);
        }
        currentGame = nextGame;
        mainCardPanel.add(currentGame, GAME_CARD);
        mainCardPanel.revalidate();
        mainCardPanel.repaint();
    }

    private Game createGame(GameSettings settings) {
        GameDifficulty safeDifficulty = settings.getDifficulty();
        return switch (safeDifficulty) {
            case EASY -> new EasyGame(settings);
            case NORMAL -> new NormalGame(settings);
            case HARD -> new HardGame(settings);
        };
    }

    private void configureViewport(GameSettings settings) {
        BackgroundProfile profile = settings.getBackgroundProfile();
        if (settings.isUseImageSizeAsViewport() && profile.supportsViewportResize()) {
            GameViewport.configure(profile.getWidth(), profile.getHeight());
        } else {
            GameViewport.reset();
        }
        applyWindowBounds();
    }

    private void applyWindowBounds() {
        int width = GameViewport.getWidth();
        int height = GameViewport.getHeight();
        setSize(width, height);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(((int) screenSize.getWidth() - width) / 2, 0, width, height);
    }
}
