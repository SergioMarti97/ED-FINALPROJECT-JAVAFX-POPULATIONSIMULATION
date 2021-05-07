package population.animals;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Pair;
import points2d.Vec2dd;
import points2d.Vec2df;
import population.AbstractPopulation;
import population.AbstractPopulationObject;
import population.UpdateableOverPopulation;

import java.util.ArrayList;
import java.util.Random;

public class AnimalPopulation extends AbstractPopulation<Animal> implements UpdateableOverPopulation {

    private Animal exampleAnimal;

    private Vec2dd screen;

    private ArrayList<Pair<Animal, Animal>> animalsInCollision;

    private ArrayList<Animal> babies;

    private ArrayList<Animal> deaths;

    public AnimalPopulation(double width, double height, Animal exampleAnimal, Image imgAnimal) {
        super(imgAnimal);
        this.exampleAnimal = exampleAnimal;
        screen = new Vec2dd(width, height);
        babies = new ArrayList<>();
        deaths = new ArrayList<>();
        population = new ArrayList<>();
        animalsInCollision = new ArrayList<>();
        rand = new Random();
    }

    @Override
    public void populate(int numAnimals) {
        population.clear();
        for ( int i = 0; i < numAnimals; i++ ) {
            Animal individual = exampleAnimal.makeBaby(rand);
            individual.getPos().setX((float) (rand.nextFloat() * screen.getX()));
            individual.getPos().setY((float) (rand.nextFloat() * screen.getY()));
            individual.setTimeLiving((float)(Math.random()) * 5);
            individual.setId(i);
            population.add(individual);
        }
    }

    @Override
    public float getEnergyFromEatenObjects(AbstractPopulationObject populationObject) {
        return getEnergyFromEatenAnimals((Animal)populationObject);
    }

    private float getEnergyFromEatenAnimals(Animal predator) {
        float energy = 0;
        for ( Animal animal : population ) {
            if ( predator.doIOverlap(animal) ) {
                energy += animal.getEnergy();
                deaths.add(animal);
            }
        }
        return energy;
    }

    public void checkDynamicCollision(Animal b1, Animal b2) {
        float distance = b1.distance(b2);

        float nx = (b2.getPos().getX() - b1.getPos().getX()) / distance;
        float ny = (b2.getPos().getY() - b1.getPos().getY()) / distance;

        float tx = -ny;
        float ty = nx;

        float dpTan1 = b1.getVel().getX() * tx + b1.getVel().getY() * ty;
        float dpTan2 = b2.getVel().getX() * tx + b2.getVel().getY() * ty;

        float dpNorm1 = b1.getVel().getX() * nx + b1.getVel().getY() * ny;
        float dpNorm2 = b2.getVel().getX() * nx + b2.getVel().getY() * ny;

        float m1 = (dpNorm1 * (b1.getMass() - b2.getMass()) + 2.0f * b2.getMass() * dpNorm2) / (b1.getMass() + b2.getMass());
        float m2 = (dpNorm2 * (b2.getMass() - b1.getMass()) + 2.0f * b1.getMass() * dpNorm1) / (b1.getMass() + b2.getMass());

        b1.getVel().setX(tx * dpTan1 + nx * m1);
        b1.getVel().setY(ty * dpTan1 + ny * m1);
        b2.getVel().setX(tx * dpTan2 + nx * m2);
        b2.getVel().setY(ty * dpTan2 + ny * m2);
    }

    @Override
    public void draw(GraphicsContext gc, Vec2df offset, float scale) {
        for ( Animal a : population) {
            a.drawYourSelf(gc, offset, scale, image);
        }
    }

    @Override
    public void update(float elapsedTime, AbstractPopulation... food) {
        addAllBabies();
        removeAllDeaths();
        for ( Animal a : population) {
            a.update(elapsedTime, screen);
            if ( a.getEnergy() <= 0 || a.getTimeLiving() >= a.getMaxTimeLiving() ) {
                deaths.add(a);
            }
            if ( a.getEnergy() > Animal.getMaxEnergy()) {
                a.addEnergy(- (Animal.getMaxEnergy() - 10) );
                babies.add(a.makeBaby(rand));
            }
            addPointsFromFood(a, food);
            checkStaticCollision(a);
        }
        checkDynamicCollisions();
        text.set("" + population.size());
    }

    private void addPointsFromFood(Animal animal, AbstractPopulation... food) {
        float points = 0;
        for ( AbstractPopulation f : food ) {
            points += f.getEnergyFromEatenObjects(animal);
        }
        animal.addEnergy(points);
    }

    private void addAllBabies() {
        for ( Animal baby : babies ) {
            baby.setId(population.size());
            population.add(baby);
        }
        babies.clear();
    }

    private void removeAllDeaths() {
        population.removeAll(deaths);
        deaths.clear();
    }

    private void checkStaticCollision(Animal a) {
        for ( Animal t : population) {
            animalsInCollision.add(a.updateStaticCollision(t));
        }
    }

    private void checkDynamicCollisions() {
        for ( Pair<Animal, Animal> pair : animalsInCollision ) {
            if ( pair != null ) {
                checkDynamicCollision(pair.getKey(), pair.getValue());
            }
        }
        animalsInCollision.clear();
    }

}
