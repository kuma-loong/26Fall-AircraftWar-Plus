import edu.hitsz.application.Game;
import edu.hitsz.application.mode.*;
import edu.hitsz.aircraft.HeroAircraft;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.swing.SwingUtilities;

/** 固定大小离屏绘制冒烟检查，不代表真实设备帧率。 */
public class PerformanceSmoke {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            for (Game game : new Game[]{new EasyGame(), new NormalGame(), new HardGame()}) {
                HeroAircraft.getInstance().prepareForNewGame();
                HeroAircraft.getInstance().setLocation(-1000, -1000);
                BufferedImage target = new BufferedImage(512, 768, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = target.createGraphics();
                try {
                    for (int i = 0; i < 100; i++) { game.action(); game.paint(graphics); }
                    long start = System.nanoTime();
                    for (int i = 0; i < 2000; i++) { game.action(); game.paint(graphics); }
                    double ms = (System.nanoTime() - start) / 1_000_000.0 / 2000;
                    System.out.printf("PERF %s: %.3f ms/frame, enemies=%d%n", game.getDifficulty(), ms, game.getEnemyAircrafts().size());
                    if (game.getEnemyAircrafts().size() > 20 || game.getScore() != 0) {
                        throw new IllegalStateException("Unexpected smoke scenario state");
                    }
                } finally { graphics.dispose(); }
            }
        });
    }
}
