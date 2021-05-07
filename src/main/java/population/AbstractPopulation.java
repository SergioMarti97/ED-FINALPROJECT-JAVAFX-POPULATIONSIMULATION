package population;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import points2d.Vec2df;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractPopulation<T extends AbstractPopulationObject> {

    protected ReadOnlyStringWrapper text = new ReadOnlyStringWrapper(this,
            "text", "N/A");

    protected ArrayList<T> population;

    protected Random rand;

    protected Image image;

    public AbstractPopulation(Image image) {
        this.image = image;
        population = new ArrayList<>();
        rand = new Random();
    }

    public abstract void populate(int numObjects);

    public abstract float getEnergyFromEatenObjects(AbstractPopulationObject object);

    public void draw(GraphicsContext gc, Vec2df offset, float scale) {
        for ( AbstractPopulationObject o : population ) {
            o.drawYourSelf(gc, offset, scale, image);
        }
    }

    public ArrayList<T> getPopulation() {
        return population;
    }

    public ReadOnlyStringProperty getText() {
        return text.getReadOnlyProperty();
    }

}
