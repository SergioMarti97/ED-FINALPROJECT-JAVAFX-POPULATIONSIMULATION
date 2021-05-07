package population.family;

import javafx.scene.paint.Color;

import java.util.Random;

public class FamilyColor {

    private int r;

    private int g;

    private int b;

    public FamilyColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public FamilyColor() {
        r = (int)(Math.random() * 255);
        g = (int)(Math.random() * 255);
        b = (int)(Math.random() * 255);
    }

    private int generateRandomInt(Random rand, int max, int min) {
        return rand.nextInt(max - min) + min;
    }

    public FamilyColor buildSimilarColor(Random rand) {
        int r = this.r + generateRandomInt(rand, 5, -5);
        int g = this.g + generateRandomInt(rand, 5, -5);
        int b = this.b + generateRandomInt(rand, 5, -5);
        r = Math.max(r, 0);
        r = Math.min(r, 200);
        g = Math.max(g, 0);
        g = Math.min(200, g);
        b = Math.max(b, 0);
        b = Math.min(200, b);
        return new FamilyColor(r, g, b);
    }

    public Color getColor() {
        return Color.rgb(r, g, b);
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

}
