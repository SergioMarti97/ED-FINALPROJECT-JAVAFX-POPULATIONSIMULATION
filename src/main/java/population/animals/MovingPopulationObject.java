package population.animals;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import points2d.Vec2dd;
import points2d.Vec2df;
import population.LivePopulationObject;

import java.util.Objects;

public abstract class MovingPopulationObject extends LivePopulationObject {

    protected final int MAX_VELOCITY = 50;

    protected Vec2df vel;

    protected float size;

    protected float mass;

    private Vec2df velocityScreen;

    public MovingPopulationObject(int id, Vec2df pos, float energy, float timeLiving, Vec2df vel, float size) {
        super(id, pos, energy, timeLiving);
        this.vel = vel;
        this.size = size;
        mass = this.size * this.size;
        velocityScreen = new Vec2df();
    }

    public boolean doIOverlap(MovingPopulationObject s) {
        return Math.abs(distance2(s)) <= (this.getSize() + s.getSize()) * (this.getSize() + s.getSize());
    }

    public void update(float elapsedTime, Vec2dd world) {
        float decrease = (vel.mag() / 500) * elapsedTime + (mass / 500) * elapsedTime;
        energy -= decrease;
        timeLiving += elapsedTime;
        updatePhysics(elapsedTime);
        updateCollisionEdges(world);
    }

    private void updatePhysics(float elapsedTime) {
        pos.addToX(vel.getX() * elapsedTime);
        pos.addToY(vel.getY() * elapsedTime);
    }

    private void updateCollisionEdges(Vec2dd world) {
        if ( pos.getX() - size < 0 ) {
            pos.setX(size);
            vel.setX(-vel.getX());
        }
        if ( pos.getX() + size >= world.getX() ) {
            pos.setX((float)world.getX() - size);
            vel.setX(-vel.getX());
        }
        if ( pos.getY() - size < 0 ) {
            pos.setY(size);
            vel.setY(-vel.getY());
        }
        if ( pos.getY() + size >= world.getY() ) {
            pos.setY((float)world.getY() - size);
            vel.setY(-vel.getY());
        }
    }

    public <T extends MovingPopulationObject> Pair<T, T> updateStaticCollision(T a) {
        if ( !this.equals(a) && this.id != a.getId() ) {
            if ( doIOverlap(a) ) {
                float dist = distance(a);
                dist = (dist <= 0) ? 1 : dist;
                float overlap = (dist - size - a.getSize());
                float differenceX = pos.getX() - a.getPos().getX();
                float differenceY = pos.getY() - a.getPos().getY();

                pos.setX(pos.getX() - (overlap * differenceX / dist));
                pos.setY(pos.getY() - (overlap * differenceY / dist));
                a.getPos().setX(a.getPos().getX() + (overlap * differenceX / dist));
                a.getPos().setY(a.getPos().getY() + (overlap * differenceY / dist));

                return new Pair<>((T)this, a);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    protected void drawVelocity(GraphicsContext gc, Vec2df offset, float scale) {
        worldToScreen(vel, velocityScreen, offset, scale);
        gc.setStroke(Color.ROYALBLUE);
        double lw = gc.getLineWidth();
        gc.setLineWidth(3);
        gc.strokeLine(
                screenPos.getX(),
                screenPos.getY(),
                screenPos.getX() + velocityScreen.getX(),
                screenPos.getY() + velocityScreen.getY()
        );
        gc.setLineWidth(lw);
    }

    /**
     * Getters and Setters
     */

    public float getSize() {
        return size;
    }

    public Vec2df getVel() {
        return vel;
    }

    public void setVel(Vec2df vel) {
        this.vel = vel;
    }

    public float getMass() {
        return mass;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", vel=" + vel +
                ", size=" + size +
                ", mass=" + mass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Animal)) return false;
        Animal animal = (Animal) o;
        return Float.compare(animal.getSize(), getSize()) == 0 &&
                Float.compare(animal.getMass(), getMass()) == 0 &&
                Float.compare(animal.getEnergy(), getEnergy()) == 0 &&
                Float.compare(animal.getTimeLiving(), getTimeLiving()) == 0 &&
                getVel().equals(animal.getVel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(MAX_VELOCITY, getVel(), getSize(), getMass(), getEnergy(), getTimeLiving());
    }

}
