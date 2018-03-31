package util;

import main.Direction;
import main.Maze;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import java.io.File;

/**
 * Created by jodiakyulas on 28/3/18.
 */
public class Utilities {


    public static void printOutResults(Maze maze, Double[][] utilities, Direction[][] policy, int numberOfIterations) {
        for (int j = 0; j < maze.getNumberOfColumns(); j++) {
            for (int i = 0; i < maze.getNumberOfRows(); i++) {
                if (maze.isWall(i , j))
                    continue;
                System.out.println("(" + j + "," + i + ")" + ": " + utilities[i][j]+ " (" + policy[i][j] + ")");
            }
        }
        System.out.println("Number of iterations: " + numberOfIterations);
        System.out.println("===========================================\n");
    }

    public static void exportUtilites(Maze maze, HashMap<Integer, Double[][]> recordOfUtilities, String directoryAdder) {
        String directory = "utilities/" + directoryAdder;
        try {
            File dir = new File(directory);
            dir.mkdirs();
            for(File file: dir.listFiles())
                if (!file.isDirectory())
                    file.delete();
        } catch(Exception e) {
            System.out.println("An exception occurs while exporting the file.");
        }
        Set<Integer> iterations = recordOfUtilities.keySet();
        for (Integer iteration: iterations) {
            Double[][] utilities = recordOfUtilities.get(iteration);
            for (int j = 0; j < maze.getNumberOfColumns(); j++) {
                for (int i = 0; i < maze.getNumberOfRows(); i++) {
                    if (maze.isWall(i , j))
                        continue;
                    export(iteration, utilities[i][j], i, j, directoryAdder);
                }
            }
        }
    }

    private static void export(Integer iteration, Double utility, int row, int column, String directoryAdder) {
        String filePathString = "utilities/" + directoryAdder + row + "_" + column + ".csv";
        try {
            File file = new File(filePathString);
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileWriter fileWriter;
            if (iteration == 0) {
                fileWriter = new FileWriter(file, false);
            } else {
                fileWriter = new FileWriter(file, true);
            }
            fileWriter.append(String.valueOf(iteration));
            fileWriter.append(",");
            fileWriter.append(String.valueOf(utility));
            fileWriter.append("\n");
            fileWriter.flush();
            fileWriter.close();
        } catch(IOException e) {
            System.out.println("An IO exception occurs while exporting the file.");
        } catch(Exception e) {
            System.out.println("An exception occurs while exporting the file.");
        }
    }



}
