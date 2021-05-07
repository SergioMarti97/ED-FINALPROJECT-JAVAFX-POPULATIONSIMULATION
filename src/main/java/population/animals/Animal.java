package population.animals;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import points2d.Vec2df;
import population.family.FamilyColor;

import java.util.Random;

public class Animal extends MovingPopulationObject {

    private final float SIZE_LIVING_CONSTANT = 3.0f;

    private FamilyColor color;

    public Animal(Vec2df vel, float size) { // <- Animal modelo
        super(0, new Vec2df(), 10, 0, vel, size);
        color = new FamilyColor();
    }

    public Animal(int id, Vec2df pos, Vec2df vel, float size, float timeLiving, FamilyColor color) {
        super(id, pos, 10, timeLiving, vel, size);
        this.color = color;
    }

    private float generateRandomFloat(Random rand, float max, float min) {
        return min + rand.nextFloat() * (max - min);
    }

    public Animal makeBaby(Random rand) {
        float size = this.size + generateRandomFloat(rand, 1, -1);
        size = (size <= 0) ? 1 : size;

        /*float VelX = vel.getX() + generateRandomFloat(rand, 1, -1);
        float VelY = vel.getY() + generateRandomFloat(rand, 1, -1);
        VelX = (VelX >= MAX_VELOCITY) ? MAX_VELOCITY : VelX;
        VelY = (VelY >= MAX_VELOCITY) ? MAX_VELOCITY : VelY;*/

        return new Animal(
                0,
                new Vec2df(pos.getX() + size, pos.getY() + size),
                //new Vec2df(VelX, VelY),
                new Vec2df(vel),
                size,
                0,
                color.buildSimilarColor(rand)
        );
    }

    public float getMaxTimeLiving() {
        return size * SIZE_LIVING_CONSTANT;
    }

    @Override
    public void drawYourSelf(GraphicsContext gc, Vec2df offset, float scale, Image img) {
        worldToScreen(offset, scale);
        float r = size * scale;

        gc.setFill(color.getColor());
        gc.fillOval(screenPos.getX() - r, screenPos.getY() - r, 2 * r, 2 * r);

        gc.drawImage(img, screenPos.getX() - r, screenPos.getY() - r, 2 * r, 2 * r);

        drawEnergyBar(gc, size, offset, scale);
        drawLiveBar(getMaxTimeLiving(), gc, size, offset, scale);

        drawVelocity(gc, offset, scale);
    }

}
