package main;

import java.util.HashMap;
import util.Utilities;
/**
 * Class that represents policy iteration of the application.
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

    /**
     * The PolicyIteration class is initialised.
     * @param maze The maze created by the application.
     * @param directoryAdder The string that will point towards the correct location for storage.
     */
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

    /**
     * Start of the policy iteration.
     */
    public void start() {
        System.out.println("Policy iteration is starting.\n");
        initialize();
        Double[][] temp = new Double[numberOfRows][numberOfColumns];
        copyUtilities(utilities, temp);
        recordOfUtilities.put(numberOfIterations, temp);
        startPolicyIteration();
        Utilities.printOutPolicy(maze, policy, numberOfRows, numberOfColumns);
        Utilities.printOutResults(maze, utilities, policy, numberOfIterations);
        Utilities.exportUtilites(maze, recordOfUtilities, directoryAdder);
        Utilities.exportAllUtilitesToOneFile(maze, recordOfUtilities, directoryAdder);
    }

    /**
     * The initialisation of the utilities and the policies.
     */
    private void initialize() {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                utilities[i][j] = 0.0;
                temp_utilities[i][j] = utilities[i][j];
                policy[i][j] = Direction.Up;
            }
        }
    }

    /**
     * The policy iteration is started.
     */
    private void startPolicyIteration() {
        boolean unchanged;
        do {
            numberOfIterations++;
            doPolicyEvaluation();
            Double[][] temp = new Double[numberOfRows][numberOfColumns];
            copyUtilities(utilities, temp);
            recordOfUtilities.put(numberOfIterations, temp);
            unchanged  = true;
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    if (maze.isWall(i, j))
                        continue;
                    if (getBestUtility(i, j) > getExpectedUtility(i, j)) {
                        policy[i][j] = getBestDirection(i, j);
                        unchanged = false;
                    }
                }
            }
        } while (!unchanged);
    }

    /**
     * Policy Evaluation is carried out.
     */
    private void doPolicyEvaluation() {
        boolean converge = false;
        while(!converge) {
            for (int i = 0; i < numberOfRows; i++) {
                for (int j = 0; j < numberOfColumns; j++) {
                    if (maze.isWall(i, j))
                        continue;
                    temp_utilities[i][j] = maze.getTileType(i, j).getReward() + discountFactor * getExpectedUtility(i, j);
                }
            }
            converge = checkForConvergence(utilities, temp_utilities);
            copyUtilities();
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
     * Copy the utilities from the temp_utilities to the utilities
     */
    public void copyUtilities() {
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
    private void copyUtilities(Double[][] original, Double[][] copy) {
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                copy[i][j] = original[i][j];
            }
        }
    }

    /**
     * Return the expected utility if the policy is followed at a particular location of the maze.
     * @param row The row of the location of the maze.
     * @param column The column of the location of the maze.
     * @return The expected utility.
     */
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
     * Get the best possible utility of a state.
     * @param row The row of the location of the state.
     * @param column The column of the location of the state.
     * @return The best possible utility of the state.
     */
    public double getBestUtility(int row, int column) {
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
     * Get the best policy at a state.
     * @param row The row of the location of the state.
     * @param column The column of the location of the state.
     * @return The best policy at the state.
     */
    private Direction getBestDirection(int row, int column) { Direction direction = Direction.Up;
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
            direction = Direction.Left;
        }
        return direction;
    }


}
