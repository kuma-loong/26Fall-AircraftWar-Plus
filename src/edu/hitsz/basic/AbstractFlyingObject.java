package edu.hitsz.basic;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.GameViewport;
import edu.hitsz.application.ImageManager;

import java.awt.image.BufferedImage;

/**
 * 可飞行对象的基类
 * @author hitsz
 */
public abstract class AbstractFlyingObject {

    //locationX,locationY为图片中心位置坐标
    protected int locationX;
    protected int locationY;

    // x,y轴移动速度
    protected int speedX;
    protected int speedY;

    //图片, null 表示未设置
    protected BufferedImage image = null;

    //x 轴长度，根据图片尺寸获得, -1 表示未设置
    protected int width = -1;

    //y 轴长度，根据图片尺寸获得, -1 表示未设置
    protected int height = -1;

    //有效（生存）标记，标记为 false的对象会在下次刷新时清除
    protected boolean isValid = true;
    private boolean permanentlyFrozen = false;
    private long freezeUntilMillis = 0L;
    private long slowUntilMillis = 0L;
    private double slowFactor = 1.0;

    public AbstractFlyingObject() {
    }

    public AbstractFlyingObject(int locationX, int locationY, int speedX, int speedY) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    /**
     * 可飞行对象根据速度移动
     * 若飞行对象触碰到横向边界，横向速度反向
     */
    public void forward() {
        refreshMotionState();
        int effectiveSpeedX = getEffectiveSpeed(speedX);
        int effectiveSpeedY = getEffectiveSpeed(speedY);
        locationX += effectiveSpeedX;
        locationY += effectiveSpeedY;
        if (locationX <= 0 || locationX >= GameViewport.getWidth()) {
            // 横向超出边界后反向
            speedX = -speedX;
        }
    }

    /**
     * 永久静止。
     */
    public void freezePermanently() {
        permanentlyFrozen = true;
        freezeUntilMillis = Long.MAX_VALUE;
        clearTemporarySlow();
    }

    /**
     * 临时冻结，持续一段时间后自动恢复。
     *
     * @param durationMillis 持续毫秒
     */
    public void freezeTemporarily(long durationMillis) {
        long safeDuration = Math.max(0L, durationMillis);
        freezeUntilMillis = Math.max(freezeUntilMillis, System.currentTimeMillis() + safeDuration);
    }

    /**
     * 临时减速，持续一段时间后自动恢复。
     *
     * @param factor 减速倍率，(0,1] 为减速
     * @param durationMillis 持续毫秒
     */
    public void slowTemporarily(double factor, long durationMillis) {
        if (factor <= 0) {
            return;
        }
        slowFactor = factor;
        slowUntilMillis = System.currentTimeMillis() + Math.max(0L, durationMillis);
    }

    /**
     * 取消所有临时运动效果（永久冻结除外）。
     */
    public void clearTemporaryMotionEffect() {
        if (!permanentlyFrozen) {
            freezeUntilMillis = 0L;
        }
        clearTemporarySlow();
    }

    /**
     * 按倍率缩放当前速度分量。
     *
     * @param factor 速度倍率，>0
     */
    public void scaleSpeed(double factor) {
        if (factor <= 0) {
            return;
        }
        speedX = scaleComponent(speedX, factor);
        speedY = scaleComponent(speedY, factor);
    }

    /**
     * 碰撞检测，当对方坐标进入我方范围，判定我方击中<br>
     * 对方与我方覆盖区域有交叉即判定撞击。
     *  <br>
     * 非飞机对象区域：
     *  横向，[x - width/2, x + width/2]
     *  纵向，[y - height/2, y + height/2]
     *  <br>
     * 飞机对象区域：
     *  横向，[x - width/2, x + width/2]
     *  纵向，[y - height/4, y + height/4]
     *
     * @param flyingObject 撞击对方
     * @return true: 我方被击中; false 我方未被击中
     */
    public boolean crash(AbstractFlyingObject flyingObject) {
        // 缩放因子，用于控制 y轴方向区域范围
        int factor = this instanceof AbstractAircraft ? 2 : 1; //我方
        int fFactor = flyingObject instanceof AbstractAircraft ? 2 : 1;//对方

        //对方坐标、宽度、高度
        int x = flyingObject.getLocationX();
        int y = flyingObject.getLocationY();
        int fWidth = flyingObject.getWidth();
        int fHeight = flyingObject.getHeight();

        return x + (fWidth+this.getWidth())/2 > locationX
                && x - (fWidth+this.getWidth())/2 < locationX
                && y + ( fHeight/fFactor+this.getHeight()/factor )/2 > locationY
                && y - ( fHeight/fFactor+this.getHeight()/factor )/2 < locationY;
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocation(double locationX, double locationY){
        this.locationX = (int) locationX;
        this.locationY = (int) locationY;
    }

    public int getSpeedY() {
        return speedY;
    }

    public BufferedImage getImage() {
        if (image == null){
            image = ImageManager.get(this);
        }
        return image;
    }

    public int getWidth() {
        if (width == -1){
            // 若未设置，则查询图片宽度并设置
            width = ImageManager.get(this).getWidth();
        }
        return width;
    }

    public int getHeight() {
        if (height == -1){
            // 若未设置，则查询图片高度并设置
            height = ImageManager.get(this).getHeight();
        }
        return height;
    }


    /**
     * 标记消失
     * isValid = false.
     * notValid() => true.
     */
    public void vanish() {
        isValid = false;
    }

    public boolean notValid() {
        return !this.isValid;
    }

    private void refreshMotionState() {
        long now = System.currentTimeMillis();
        if (!permanentlyFrozen && freezeUntilMillis > 0 && now >= freezeUntilMillis) {
            freezeUntilMillis = 0L;
        }
        if (slowUntilMillis > 0 && now >= slowUntilMillis) {
            clearTemporarySlow();
        }
    }

    private void clearTemporarySlow() {
        slowUntilMillis = 0L;
        slowFactor = 1.0;
    }

    private int getEffectiveSpeed(int baseSpeed) {
        if (permanentlyFrozen || freezeUntilMillis > 0) {
            return 0;
        }
        return scaleComponent(baseSpeed, slowFactor);
    }

    private int scaleComponent(int value, double factor) {
        if (value == 0) {
            return 0;
        }
        int scaled = (int) Math.round(value * factor);
        if (scaled == 0) {
            return value > 0 ? 1 : -1;
        }
        return scaled;
    }

}
