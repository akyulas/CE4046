package main;

/**
 * Created by jodiakyulas on 6/3/18.
 */
public enum TileType {

    Green(1), White(-0.04), Brown(-1) ,Wall(0);

    private final double reward;

    TileType(double reward) {
        this.reward = reward;
    }

    public double getReward() {
        return this.reward;
    }

}
