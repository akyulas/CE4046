package main;

/**
 * The direction that represents Up, Down, Left, And Right
 */
public enum Direction {

    Up("Up"), Down("Down"), Left("Left"), Right("Right");

    private String string;

    Direction(String string) {
        this.string = string;
    }

    public String toString() {
        return string;
    }
    
}
