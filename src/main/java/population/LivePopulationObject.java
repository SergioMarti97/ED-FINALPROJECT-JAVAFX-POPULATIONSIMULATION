package population;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import points2d.Vec2df;

public abstract class LivePopulationObject extends AbstractPopulationObject {

    protected static final float MAX_ENERGY = 100.0f;

    protected final float GAP_BETWEEN_OBJ_BAR = 10.0f;

    protected float energy;

    protected float timeLiving;

    private final Vec2df BAR_SIZE = new Vec2df(50.0f, 10.0f);

    private final Paint FILL_COLOR = Color.YELLOW;

    private final Paint STROKE_COLOR = Color.BLACK;

    private Vec2df barPos;

    private Vec2df barScreenPos;

    private Vec2df barScreenSize;

    public LivePopulationObject(int id, Vec2df pos, float energy, float timeLiving) {
        super(id, pos);

        this.energy = energy;
        this.timeLiving = timeLiving;

        barPos = new Vec2df();
        barScreenPos = new Vec2df();
        barScreenSize = new Vec2df();
    }

    private void updateBarPos(Vec2df barPosition, float barSizeWidth, float gapBetweenObjBar) {
        barPosition.setX(pos.getX() - (barSizeWidth / 2.0f));
        barPosition.setY(pos.getY() + gapBetweenObjBar);
    }

    protected void drawBar(
            Vec2df barPosition,
            Vec2df barSize,
            Vec2df barScreenPos,
            Vec2df barScreenSize,
            float percentage,
            float max,
            float gapBetweenObjBar,
            GraphicsContext gc,
            Vec2df offset,
            float scale,
            Paint fillColor,
            Paint strokeColor) {
        updateBarPos(barPosition, barSize.getX(), gapBetweenObjBar);

        float energyPosW = percentage * (barSize.getX() / max);
        float energyScreenPosW = worldToScreen(energyPosW, offset.getX(), scale);
        worldToScreen(barPosition, barScreenPos, offset, scale);
        worldToScreen(barSize, barScreenSize, offset, scale);

        drawFillRect(barScreenPos.getX(), barScreenPos.getY(), energyScreenPosW, barScreenSize.getY(), gc, fillColor);
        drawStrokeRect(barScreenPos, barScreenSize, gc, strokeColor);
    }

    protected void drawBar(
            float percentage,
            float max,
            float gapBetweenObjBar,
            GraphicsContext gc,
            Vec2df offset,
            float scale,
            Paint fillColor,
            Paint strokeColor
    ) {
        drawBar(barPos,
                BAR_SIZE,
                barScreenPos,
                barScreenSize,
                percentage,
                max,
                gapBetweenObjBar,
                gc,
                offset,
                scale,
                fillColor,
                strokeColor
        );
    }

    protected void drawEnergyBar(GraphicsContext gc, float height, Vec2df offset, float scale) {
        drawBar(energy,
                MAX_ENERGY,
                height + GAP_BETWEEN_OBJ_BAR,
                gc,
                offset,
                scale,
                FILL_COLOR,
                STROKE_COLOR
        );
    }

    protected void drawLiveBar(float maxTimeLiving, GraphicsContext gc, float height, Vec2df offset, float scale) {
        drawBar(maxTimeLiving - timeLiving,
                maxTimeLiving,
                height + 3 * GAP_BETWEEN_OBJ_BAR,
                gc,
                offset,
                scale,
                Color.RED,
                STROKE_COLOR);
        gc.setFill(Color.BLACK);
        gc.fillText(String.format("%.1f/%.1f", timeLiving, maxTimeLiving), barScreenPos.getX(), barScreenPos.getY());
    }

    /**
     * Draw methods
     */

    private void drawFillRect(float x, float y, float w, float h, GraphicsContext gc, Paint color) {
        gc.setFill(color);
        gc.fillRect(x, y, w, h);
    }

    private void drawStrokeRect(float x, float y, float w, float h, GraphicsContext gc, Paint color) {
        gc.setStroke(color);
        gc.strokeRect(x, y, w, h);
    }

    private void drawStrokeRect(Vec2df pos, Vec2df size, GraphicsContext gc, Paint color) {
        drawStrokeRect(pos.getX(), pos.getY(), size.getX(), size.getY(), gc, color);
    }

    /**
     * Getters and Setters
     */

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public void addEnergy(float energy) {
        this.energy += energy;
    }

    public float getTimeLiving() {
        return timeLiving;
    }

    public void setTimeLiving(float timeLiving) {
        this.timeLiving = timeLiving;
    }

    public void addTimeLiving(float time) {
        this.timeLiving += time;
    }

    public static float getMaxEnergy() {
        return MAX_ENERGY;
    }

    /**
     * ToString and another methods
     */

    @Override
    public String toString() {
        return super.toString() +
                ", energy=" + energy +
                ", timeLiving=" + timeLiving + "s";
    }

}
