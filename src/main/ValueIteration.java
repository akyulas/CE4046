package main;

import java.util.HashMap;
import util.Utilities;

/**
 * Class that represents value iteration of the application.
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

    /**
     * The ValueIteration class is initialised.
     * @param maze The maze created by the application.
     * @param directoryAdder The string that will point towards the correct location for storage.
     */
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

    /**
     * Start of the policy iteration.
     */
    public void start() {
        System.out.println("Value iteration is starting.\n");
        initializeUtilities();
        Double[][] temp = new Double[numberOfRows][numberOfColumns];
        copyUtilites(utilities, temp);
        recordOfUtilities.put(numberOfIterations, temp);
        calculateUtilitiesUntilConvergence();
        getOptimalPolicy();
        Utilities.printOutPolicy(maze, policy, numberOfRows, numberOfColumns);
        Utilities.printOutResults(maze, utilities, policy, numberOfIterations);
        Utilities.exportUtilites(maze, recordOfUtilities, directoryAdder);
        Utilities.exportAllUtilitesToOneFile(maze, recordOfUtilities, directoryAdder);
    }

    /**
     * The initialisation of the utilities.
     */
    private void initializeUtilities() {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                utilities[i][j] = 0.0;
                temp_utilities[i][j] = 0.0;
            }
        }
    }

    /**
     * Value iteration is carried out until convergence.
     */
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

    /**
     * Copy the utilities from the temp_utilities to the utilities
     */
    private void copyNewUtilities() {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                utilities[i][j] = temp_utilities[i][j];
            }
        }
    }

    /**
     * Copy the utilities from the original to the copy
     * @param original The original utilities
     * @param copy The copy
     */
    private void copyUtilites(Double[][] original, Double[][] copy) {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                copy[i][j] = original[i][j];
            }
        }
    }

    /**
     * Check for convergence is carried out.
     * @param first_matrix The first matrix that stores the utilities.
     * @param second_matrix The second matrix that stores the temp_utilities.
     * @return
     */
    private boolean checkForConvergence(Double[][] first_matrix, Double[][] second_matrix) {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (Math.abs(first_matrix[i][j] - second_matrix[i][j]) >= convergenceFactor)
                    return false;
            }
        }
        return true;
    }

    /**
     * Get the maximum possible expected utility from a particular location.
     * @param row The row of the location.
     * @param column The column of the location.
     * @return The maximum possible expected utility from a particular location.
     */
    private double getMaximumSubsequentState(int row, int column) {
        double max = 0.8 * goUp(row, column) + 0.1 * goRight(row, column) + 0.1 * goLeft(row, column);
        double valueToGoRight = 0.8 * goRight(row, column) + 0.1 * goUp(row, column) + 0.1 * goDown(row, column);
        if (Double.compare(max, valueToGoRight) < 0) {
            max = valueToGoRight;
        }
        double valueToGoDown = 0.8 * goDown(row, column) + 0.1 * goLeft(row, column) + 0.1 * goRight(row, column);
        if (Double.compare(max, valueToGoDown) < 0) {
            max = valueToGoDown;
        }
        double valueToGoLeft = 0.8 * goLeft(row, column) + 0.1 * goUp(row, column) + 0.1 * goDown(row, column);
        if (Double.compare(max, valueToGoLeft) < 0) {
            max = valueToGoLeft;
        }
        return max;
    }

    /**
     * Get the policy for a state.
     * @param row The row of the state in the maze.
     * @param column The column of the state in the maze.
     * @return The policy for the state.
     */
    private Direction getDirectionValueForPolicy(int row, int column) {
        Direction direction = Direction.Up;
        double max = 0.8 * goUp(row, column) + 0.1 * goRight(row, column) + 0.1 * goLeft(row, column);
        double valueToGoRight = 0.8 * goRight(row, column) + 0.1 * goUp(row, column) + 0.1 * goDown(row, column);
        if (Double.compare(max, valueToGoRight) < 0) {
            max = valueToGoRight;
            direction = Direction.Right;
        }
        double valueToGoDown = 0.8 * goDown(row, column) + 0.1 * goLeft(row, column) + 0.1 * goRight(row, column);
        if (Double.compare(max, valueToGoDown) < 0) {
            max = valueToGoDown;
            direction = Direction.Down;
        }
        double valueToGoLeft = 0.8 * goLeft(row, column) + 0.1 * goUp(row, column) + 0.1 * goDown(row, column);
        if (Double.compare(max, valueToGoLeft) < 0) {
            max = valueToGoLeft;
            direction = Direction.Left;
        }
        return direction;
    }

    /**
     * Get the utilities of the tile above the specified location of the maze.
     * @param row The row of the location of the maze.
     * @param column The column of the location of the maze.
     * @return The utility at the top of the  specified location of the maze.
     */
    private double goUp(int row, int column) {
        int temp = row - 1;
        if (temp < 0 || maze.isWall(temp, column)) {
            return utilities[row][column];
        }
        return utilities[temp][column];
    }

    /**
     * Get the utilities of the tile to the right the specified location of the maze.
     * @param row The row of the location of the maze.
     * @param column The column of the location of the maze.
     * @return The utility at the right of the specified location of the maze.
     */
    private double goRight(int row, int column) {
        int temp = column + 1;
        if (temp >= numberOfColumns || maze.isWall(row, temp)) {
            return utilities[row][column];
        }
        return utilities[row][temp];
    }

    /**
     * Get the utilities of the tile to the bottom of the specified location of the maze.
     * @param row The row of the location of the maze.
     * @param column The column of the location of the maze.
     * @return The utility at the bottom of the specified location of the maze.
     */
    private double goDown(int row, int column) {
        int temp = row + 1;
        if (temp >= numberOfRows || maze.isWall(temp, column)) {
            return utilities[row][column];
        }
        return utilities[temp][column];
    }

    /**
     * Get the utilities of the tile to the left of the specified location of the maze.
     * @param row The row of the location of the maze.
     * @param column The column of the location of the maze.
     * @return The utility at the left of the specified location of the maze.
     */
    private double goLeft(int row, int column) {
        int temp = column - 1;
        if (temp < 0 || maze.isWall(row, temp)) {
            return utilities[row][column];
        }
        return utilities[row][temp];
    }

    /**
     * Get the optimal policy for each states.
     */
    private void getOptimalPolicy() {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                policy[i][j] = getDirectionValueForPolicy(i, j);
            }
        }
    }


}
