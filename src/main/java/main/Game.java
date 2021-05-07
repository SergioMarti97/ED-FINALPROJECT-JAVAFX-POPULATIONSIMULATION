package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import points2d.Vec2df;
import points2d.Vec2di;
import population.animals.Animal;
import population.animals.AnimalPopulation;
import population.tiles.TileField;

public class Game {

    private final int NUM_ANIMALS = 50;

    private Vec2df screenSize;

    private TileField field;

    private AnimalPopulation sheep;

    private AnimalPopulation foxes;

    //private AnimalPopulation megalodones;

    public Game(Vec2di tileFieldSize, double width, double height, Image imgGrass, Image imgSheep, Image imgFox) {
        screenSize = new Vec2df((float)width, (float)height);
        field = new TileField(tileFieldSize, width, height, imgGrass);
        sheep = new AnimalPopulation(width, height, new Animal(new Vec2df(-100.5f, 100.5f), 15), imgSheep);
        foxes = new AnimalPopulation(width, height, new Animal(new Vec2df(20.0f, 20.0f), 10), imgFox);
        //megalodones = new AnimalPopulation(width, height, new Animal(new Vec2df(5.0f, 5.0f), 20), new Image("/img/shark.png"));
    }

    public void initialize() {
        field.populate();
        sheep.populate(2 * NUM_ANIMALS);
        foxes.populate(NUM_ANIMALS / 4);
        //megalodones.populate(NUM_ANIMALS);
    }

    public void update(float elapsedTime) {
        field.update(elapsedTime);
        sheep.update(elapsedTime, field);
        foxes.update(elapsedTime, sheep);
        //megalodones.update(elapsedTime, sheep, foxes, field);
    }

    public void draw(GraphicsContext gc, Vec2df offset, float scale) {
        clearBackground(gc);
        field.draw(gc, offset, scale);
        sheep.draw(gc, offset, scale);
        foxes.draw(gc, offset, scale);
        //megalodones.draw(gc, offset, scale);
    }

    private void clearBackground(GraphicsContext gc) {
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, screenSize.getX(), screenSize.getY());
    }

    public TileField getField() {
        return field;
    }

    public AnimalPopulation getSheep() {
        return sheep;
    }

    public AnimalPopulation getFoxes() {
        return foxes;
    }

}
