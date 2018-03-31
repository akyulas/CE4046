package main;

import java.util.HashMap;
import util.Utilities;
/**
 * Created by jodiakyulas on 6/3/18.
 */
public class PolicyIteration {

    private Double[][] utilities;
    private Double[][] temp_utilities;
    private Direction[][] policy;
    private Maze maze;
    private HashMap<Integer, Double[][]> recordOfUtilities;
    private int numberOfRows;
    private int numberOfColumns;
    private double discountFactor = 0.99;
    private int numberOfIterations = 0;
    private double epsilon = 0.1;
    private double convergenceFactor = epsilon * (1 - discountFactor) / discountFactor;
    String directoryAdder;

    public PolicyIteration (Maze maze, String directoryAdder) {
        this.maze = maze;
        numberOfRows = maze.getNumberOfRows();
        numberOfColumns = maze.getNumberOfColumns();
        utilities = new Double[numberOfRows][numberOfColumns];
        temp_utilities = new Double[numberOfRows][numberOfColumns];
        policy = new Direction[numberOfRows][numberOfColumns];
        recordOfUtilities = new HashMap<Integer, Double[][]>();
        this.directoryAdder = directoryAdder + "policy/";
    }

    public void start() {
        System.out.println("Policy iteration is starting.\n");
        initialize();
        Double[][] temp = new Double[numberOfRows][numberOfColumns];
        copyUtilites(utilities, temp);
        recordOfUtilities.put(numberOfIterations, temp);
        startPolicyIteration();
        Utilities.printOutResults(maze, utilities, policy, numberOfIterations);
        Utilities.exportUtilites(maze, recordOfUtilities, directoryAdder);
    }

    private void initialize() {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                utilities[i][j] = 0.0;
                policy[i][j] = Direction.Left;
                temp_utilities = new Double[numberOfRows][numberOfColumns];
            }
        }
    }

    private void startPolicyIteration() {
        boolean unchanged;
        do {
            numberOfIterations++;
            doPolicyEvaluation();
            Double[][] temp = new Double[numberOfRows][numberOfColumns];
            copyUtilites(utilities, temp);
            recordOfUtilities.put(numberOfIterations, temp);
            unchanged  = true;
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    if (getBestUtility(i, j) > getExpectedUtility(i, j)) {
                        policy[i][j] = getBestDirection(i, j);
                        unchanged = false;
                    }
                }
            }
        } while (!unchanged);
    }

    private void doPolicyEvaluation() {
        boolean converge = false;
        while(!converge) {
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    temp_utilities[i][j] = maze.getTileType(i, j).getReward() + discountFactor * getExpectedUtility(i, j);
                }
            }
            converge = checkForConvergence(utilities, temp_utilities);
            copyUtilities();
        }
    }

    private boolean checkForConvergence(Double[][] first_matrix, Double[][] second_matrix) {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (Math.abs(first_matrix[i][j] - second_matrix[i][j]) >= convergenceFactor)
                    return false;
            }
        }
        return true;
    }

    public void copyUtilities() {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                utilities[i][j] = temp_utilities[i][j];
            }
        }
    }

    private void copyUtilites(Double[][] original, Double[][] copy) {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                copy[i][j] = original[i][j];
            }
        }
    }

    public double getExpectedUtility(int row, int column) {
        double utility = 0.0; Direction direction = policy[row][column];
        switch(direction) {
            case Up:
                utility = 0.8 * goUp(row, column) + 0.1 * goLeft(row, column) + 0.1 * goRight(row, column);
                break;
            case Right:
                utility = 0.8 * goRight(row, column) + 0.1 * goUp(row, column) + 0.1 * goDown(row, column);
                break;
            case Down:
                utility = 0.8 * goDown(row, column) + 0.1 * goLeft(row, column) + 0.1 * goRight(row, column);
                break;
            case Left:
                utility = 0.8 * goLeft(row, column) + 0.1 * goUp(row, column) + 0.1 * goDown(row, column);
        }
        return utility;
    }

    private double goUp(int row, int column) {
        int temp = row - 1;
        if (temp < 0 || maze.isWall(temp, column)) {
            return utilities[row][column];
        }
        return utilities[temp][column];
    }

    private double goRight(int row, int column) {
        int temp = column + 1;
        if (temp >= numberOfColumns || maze.isWall(row, temp)) {
            return utilities[row][column];
        }
        return utilities[row][temp];
    }

    private double goDown(int row, int column) {
        int temp = row + 1;
        if (temp >= numberOfRows || maze.isWall(temp, column)) {
            return utilities[row][column];
        }
        return utilities[temp][column];
    }

    private double goLeft(int row, int column) {
        int temp = column - 1;
        if (temp < 0 || maze.isWall(row, temp)) {
            return utilities[row][column];
        }
        return utilities[row][temp];
    }

    public double getBestUtility(int row, int column) {
        double max = 0.8 * goUp(row, column) + 0.1 * goRight(row, column) + 0.1 * goLeft(row, column);
        double valueToGoRight = 0.8 * goRight(row, column) + 0.1 * goUp(row, column) + 0.1 * goDown(row, column);
        if (max < valueToGoRight) {
            max = valueToGoRight;
        }
        double valueToGoDown = 0.8 * goDown(row, column) + 0.1 * goLeft(row, column) + 0.1 * goRight(row, column);
        if (max < valueToGoDown) {
            max = valueToGoDown;
        }
        double valueToGoLeft = 0.8 * goLeft(row, column) + 0.1 * goUp(row, column) + 0.1 * goDown(row, column);
        if (max < valueToGoLeft) {
            max = valueToGoLeft;
        }
        return max;
    }

    private Direction getBestDirection(int row, int column) { Direction direction = Direction.Up;
        double max = 0.8 * goUp(row, column) + 0.1 * goRight(row, column) + 0.1 * goLeft(row, column);
        double valueToGoRight = 0.8 * goRight(row, column) + 0.1 * goUp(row, column) + 0.1 * goDown(row, column);
        if (max < valueToGoRight) {
            max = valueToGoRight;
            direction = Direction.Right;
        }
        double valueToGoDown = 0.8 * goDown(row, column) + 0.1 * goLeft(row, column) + 0.1 * goRight(row, column);
        if (max < valueToGoDown) {
            max = valueToGoDown;
            direction = Direction.Down;
        }
        double valueToGoLeft = 0.8 * goLeft(row, column) + 0.1 * goUp(row, column) + 0.1 * goDown(row, column);
        if (max < valueToGoLeft) {
            direction = Direction.Left;
        }
        return direction;
    }


}
