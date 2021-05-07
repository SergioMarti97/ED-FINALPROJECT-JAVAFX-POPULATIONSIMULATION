package population.tiles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import points2d.Vec2df;
import population.LivePopulationObject;

public class Tile extends LivePopulationObject {

    private static final float MAX_TIME_LIVING = 9.0f;

    private static final float REPRODUCE_TIME = 0.75f;

    private Vec2df size;

    private boolean isLive;

    private float accumulatedTime = 0;

    public Tile(int id, Vec2df pos, Vec2df size, boolean isLive) {
        super(id, pos, 0, 0);
        this.size = size;
        this.isLive = isLive;
    }

    public boolean pointInside(Vec2df p) {
        float minX = pos.getX();
        float maxX = pos.getX() + size.getX();
        float minY = pos.getY();
        float maxY = pos.getY() + size.getY();
        return p.getX() >= minX && p.getX() <= maxX && p.getY() >= minY && p.getY() <= maxY;
    }

    @Override
    public void drawYourSelf(GraphicsContext gc, Vec2df offset, float scale, Image img) {
        gc.setFill(isLive ? Color.GREEN : Color.CHOCOLATE);
        worldToScreen(offset, scale);
        gc.fillRect(screenPos.getX(), screenPos.getY(), size.getX() * scale, size.getY() * scale);
        /*if ( isLive ) {
            gc.drawImage(img,
                    screenPos.getX() - size.getX() * scale,
                    screenPos.getY() - size.getY() * scale,
                    size.getX() * scale,
                    size.getY() * scale
            );
        }*/
    }

    public Vec2df getSize() {
        return size;
    }

    public void setSize(Vec2df size) {
        this.size = size;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public float getAccumulatedTime() {
        return accumulatedTime;
    }

    public void setAccumulatedTime(float accumulatedTime) {
        this.accumulatedTime = accumulatedTime;
    }

    public void addAccumulatedTime(float time) {
        this.accumulatedTime += time;
    }

    public static float getMaxTimeLiving() {
        return MAX_TIME_LIVING;
    }

    public static float getReproduceTime() {
        return REPRODUCE_TIME;
    }

}
