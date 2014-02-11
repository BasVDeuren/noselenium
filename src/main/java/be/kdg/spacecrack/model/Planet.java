package be.kdg.spacecrack.model;

/**
 * Created by Tim on 11/02/14.
 */
public class Planet {
    private int x;
    private int y;

    public Planet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
