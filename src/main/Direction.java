package main;

/**
 * Created by jodiakyulas on 6/3/18.
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
