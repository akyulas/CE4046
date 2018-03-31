package main;

import java.util.HashMap;
import util.Utilities;

/**
 * Created by jodiakyulas on 6/3/18.
 */
public class ValueIteration {

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

    public ValueIteration(Maze maze, String directoryAdder) {
        this.maze = maze;
        numberOfRows = maze.getNumberOfRows();
        numberOfColumns = maze.getNumberOfColumns();
        utilities = new Double[numberOfRows][numberOfColumns];
        temp_utilities = new Double[numberOfRows][numberOfColumns];
        policy = new Direction[numberOfRows][numberOfColumns];
        recordOfUtilities = new HashMap<Integer, Double[][]>();
        this.directoryAdder = directoryAdder + "value/";
    }

    public void start() {
        System.out.println("Value iteration is starting.\n");
        initializeUtilities();
        Double[][] temp = new Double[numberOfRows][numberOfColumns];
        copyUtilites(utilities, temp);
        recordOfUtilities.put(numberOfIterations, temp);
        calculateUtilitiesUntilConvergence();
        getOptimalPolicy();
        Utilities.printOutResults(maze, utilities, policy, numberOfIterations);
        Utilities.exportUtilites(maze, recordOfUtilities, directoryAdder);
    }

    private void initializeUtilities() {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                utilities[i][j] = 0.0;
                temp_utilities[i][j] = 0.0;
            }
        }
    }

    private void calculateUtilitiesUntilConvergence() {
        boolean converge = false;
        while (!converge) {
            numberOfIterations++;
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    if (maze.isWall(i, j))
                        continue;
                    temp_utilities[i][j] = maze.getTileType(i, j).getReward() + discountFactor * getMaximumSubsequentState(i, j);
                }
            }
            converge = checkForConvergence(utilities, temp_utilities);
            copyNewUtilities();
            Double[][] temp = new Double[numberOfRows][numberOfColumns];
            copyUtilites(utilities, temp);
            recordOfUtilities.put(numberOfIterations, temp);
        }
    }

    private void copyNewUtilities() {
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

    private boolean checkForConvergence(Double[][] first_matrix, Double[][] second_matrix) {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (Math.abs(first_matrix[i][j] - second_matrix[i][j]) >= convergenceFactor)
                    return false;
            }
        }
        return true;
    }

    private double getMaximumSubsequentState(int row, int column) {
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

    private Direction getDirectionValueForPolicy(int row, int column) {
        Direction direction = Direction.Up;
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
            max = valueToGoLeft;
            direction = Direction.Left;
        }
        return direction;
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

    private void getOptimalPolicy() {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                policy[i][j] = getDirectionValueForPolicy(i, j);
            }
        }
    }


}
