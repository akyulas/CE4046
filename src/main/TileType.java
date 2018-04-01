package main;

/**
 * Class that represents the possible tile types.
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
