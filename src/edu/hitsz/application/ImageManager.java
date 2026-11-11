package edu.hitsz.application;


import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.ElitePlusEnemy;
import edu.hitsz.aircraft.AceEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.dao.GameDifficulty;
import edu.hitsz.item.BloodItem;
import edu.hitsz.item.BombItem;
import edu.hitsz.item.FireItem;
import edu.hitsz.item.FreezeItem;
import edu.hitsz.item.SuperFireItem;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 * @author kuma
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, BufferedImage> CLASSNAME_IMAGE_MAP = new HashMap<>();
    private static final String IMAGE_DIRECTORY = "src/images/";

    public static BufferedImage BACKGROUND_IMAGE;
    public static BufferedImage HERO_IMAGE;
    public static BufferedImage HERO_BULLET_IMAGE;
    public static BufferedImage ENEMY_BULLET_IMAGE;
    public static BufferedImage MOB_ENEMY_IMAGE;
    public static BufferedImage ELITE_ENEMY_IMAGE;
    public static BufferedImage ELITE_PLUS_ENEMY_IMAGE;
    public static BufferedImage ACE_ENEMY_IMAGE;
    public static BufferedImage BOSS_ENEMY_IMAGE;
    public static BufferedImage BLOOD_ITEM_IMAGE;
    public static BufferedImage FIRE_ITEM_IMAGE;
    public static BufferedImage SUPER_FIRE_ITEM_IMAGE;
    public static BufferedImage BOMB_ITEM_IMAGE;
    public static BufferedImage FREEZE_ITEM_IMAGE;

    static {
        try {

            BACKGROUND_IMAGE = loadImage("bg.jpg");

            HERO_IMAGE = loadImage("hero.png");
            MOB_ENEMY_IMAGE = loadImage("mob.png");
            ELITE_ENEMY_IMAGE = loadImage("elite.png");
            ELITE_PLUS_ENEMY_IMAGE = loadImage("elitePlus.png");
            ACE_ENEMY_IMAGE = loadImage("elitePro.png");
            BOSS_ENEMY_IMAGE = loadImage("boss.png");
            HERO_BULLET_IMAGE = loadImage("bullet_hero.png");
            ENEMY_BULLET_IMAGE = loadImage("bullet_enemy.png");
            BLOOD_ITEM_IMAGE = loadImage("prop_blood.png");
            FIRE_ITEM_IMAGE = loadImage("prop_bullet.png");
            SUPER_FIRE_ITEM_IMAGE = loadImage("prop_bulletPlus.png");
            BOMB_ITEM_IMAGE = loadImage("prop_bomb.png");
            FREEZE_ITEM_IMAGE = loadImage("prop_freeze.png");

            CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
            CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(ElitePlusEnemy.class.getName(), ELITE_PLUS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(AceEnemy.class.getName(), ACE_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), BOSS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BloodItem.class.getName(), BLOOD_ITEM_IMAGE);
            CLASSNAME_IMAGE_MAP.put(FireItem.class.getName(), FIRE_ITEM_IMAGE);
            CLASSNAME_IMAGE_MAP.put(SuperFireItem.class.getName(), SUPER_FIRE_ITEM_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BombItem.class.getName(), BOMB_ITEM_IMAGE);
            CLASSNAME_IMAGE_MAP.put(FreezeItem.class.getName(), FREEZE_ITEM_IMAGE);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static BufferedImage get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static BufferedImage get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }

    /**
     * 按难度切换背景图，不改变其他资源加载方式。
     * @param difficulty 当前难度
     */
    public static void setBackgroundByDifficulty(GameDifficulty difficulty) {
        GameDifficulty safeDifficulty = difficulty == null ? GameDifficulty.NORMAL : difficulty;
        String backgroundFileName = switch (safeDifficulty) {
            case EASY -> "bg2.jpg";
            case NORMAL -> "bg4.jpg";
            case HARD -> "bg5.jpg";
        };
        try {
            BACKGROUND_IMAGE = scaleToViewport(loadImage(IMAGE_DIRECTORY + backgroundFileName));
        } catch (IOException exception) {
            try {
                BACKGROUND_IMAGE = scaleToViewport(loadImage(IMAGE_DIRECTORY + "bg.jpg"));
            } catch (IOException ignored) {
                // 图片资源在静态初始化阶段已校验，此处保底不再退出程序。
            }
        }
    }

    private static BufferedImage loadImage(String fileName) throws IOException {
        File imageFile = new File(fileName);
        if (!imageFile.exists()) {
            imageFile = new File(IMAGE_DIRECTORY + fileName);
        }
        try (InputStream inputStream = new FileInputStream(imageFile)) {
            return ImageIO.read(inputStream);
        }
    }

    private static BufferedImage scaleToViewport(BufferedImage source) {
        if (source == null) {
            return null;
        }
        int targetWidth = GameViewport.getWidth();
        int targetHeight = GameViewport.getHeight();
        if (source.getWidth() == targetWidth && source.getHeight() == targetHeight) {
            return source;
        }
        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = scaledImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(source, 0, 0, targetWidth, targetHeight, null);
        graphics.dispose();
        return scaledImage;
    }

}
