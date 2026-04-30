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
        setSize(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        setResizable(false);

        // 设置窗口的大小和位置,居中放置
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(((int) screenSize.getWidth() - Main.WINDOW_WIDTH) / 2, 0,
                Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
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
     * @param difficulty 选择的游戏难度
     */
    public void startGame(GameDifficulty difficulty) {
        replaceCurrentGame(createGame(difficulty));
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

    private Game createGame(GameDifficulty difficulty) {
        GameDifficulty safeDifficulty = difficulty == null ? GameDifficulty.NORMAL : difficulty;
        return switch (safeDifficulty) {
            case EASY -> new EasyGame();
            case NORMAL -> new NormalGame();
            case HARD -> new HardGame();
        };
    }
}
