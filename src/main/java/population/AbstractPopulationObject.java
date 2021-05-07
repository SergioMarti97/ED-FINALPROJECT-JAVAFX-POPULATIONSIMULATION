package population;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import points2d.Vec2df;

public abstract class AbstractPopulationObject {

    protected int id;

    protected Vec2df pos;

    protected Vec2df screenPos;

    public AbstractPopulationObject(int id, Vec2df pos) {
        this.id = id;
        this.pos = pos;
        screenPos = new Vec2df(pos);
    }
    
    public float distance2(AbstractPopulationObject o) {
        return (this.getPos().getX() - o.getPos().getX()) * (this.getPos().getX() - o.getPos().getX()) +
                (this.getPos().getY() - o.getPos().getY()) * (this.getPos().getY() - o.getPos().getY());
    }
    
    public float distance(AbstractPopulationObject o) {
        return (float)Math.sqrt(distance2(o));
    }

    protected float worldToScreen(float magnitude, float offset, float scale) {
        return (magnitude + (offset / scale)) * scale;
    }

    protected Vec2df worldToScreen(Vec2df in, Vec2df out, Vec2df offset, float scale) {
        out.setX(worldToScreen(in.getX(), offset.getX(), scale)); // (in.getX() + (offset.getX() / scale)) * scale
        out.setY(worldToScreen(in.getY(), offset.getY(), scale)); // (in.getY() + (offset.getY() / scale)) * scale
        return out;
    }

    public Vec2df worldToScreen(Vec2df offset, float scale) {
        return worldToScreen(pos, screenPos, offset, scale);
    }

    public Vec2df getPos() {
        return pos;
    }

    public void setPos(Vec2df pos) {
        this.pos = pos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", pos=" + pos;
    }

    public abstract void drawYourSelf(GraphicsContext gc, Vec2df offset, float scale, Image img);

}
