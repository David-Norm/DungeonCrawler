package drippyspelunk.model.dungeon;

import java.util.Random;

/**
 * Generation logic for the dungeon's room layout.
 *
 * @author Devin Arroyo
 * @author Mark Malyshev
 * @author David Norman
 * @version 1.5
 */
public class DungeonCrawlerLabyrinthGenerator {

    /**
     * Represents an empty cell in the grid.
     */
    private static final int EMPTY_CELL = 0;
    /**
     * Represents a blank room in the grid.
     */
    private static final int BLANK_ROOM = 2;
    /**
     * Represents a blank room that leads to a pillar room.
     */
    private static final int BLANK_ROOM_TO_PILLAR = 3; //4,5,6
    /**
     * Represents a pillar room.
     */
    private static final int PILLAR_ROOM = 13; // 14,15,16
    /**
     * Represents the starting room.
     */
    private static final int STARTING_ROOM = 9;
    /**
     * Represents the ending room.
     */
    private static final int ENDING_ROOM = 10;

    /**
     * A random number generator.
     */
    private final Random myRandom = new Random();

    /**
     * Generates a dungeon grid with pillars.
     *
     * @param theGrid         The initial grid.
     * @param theStartingPosX The starting x-position.
     * @param theStartingPosY The starting y-position.
     * @param thePillarAmount The number of pillars to generate.
     * @param theBigSize      The size of the final grid.
     * @param theDifficulty   The difficulty level.
     * @return The generated dungeon grid.
     */
    public int[][] generateGridPillars(final int[][] theGrid, final int theStartingPosX, final int theStartingPosY, final int thePillarAmount, final int theBigSize, final int theDifficulty) {
        int startI = theStartingPosX;
        int startJ = theStartingPosY;
        final int[] pillar_starts_I = {theStartingPosX, theStartingPosX, theBigSize - theStartingPosX, theBigSize - theStartingPosX};
        final int[] pillar_starts_J = {theStartingPosY, theBigSize - theStartingPosY, theStartingPosY, theBigSize - theStartingPosY};

        generateGrid(theGrid, startI, startJ, -2, 0);
        final int[][] newGrid = insertArray(theGrid, theBigSize);
        asciiGrid(newGrid);
        for (int p = 0; p < thePillarAmount; p++) {

            startI = pillar_starts_I[p];
            startJ = pillar_starts_J[p];

            newGrid[startI][startJ] = PILLAR_ROOM + p;
            generateGrid(newGrid, startI, startJ, 2, p + 3);
            newGrid[startI][startJ] = PILLAR_ROOM + p;
        }

        return newGrid;
    }

    /**
     * Recursively generates a path in the dungeon grid using a random walk algorithm.
     *
     * @param theGrid         The dungeon grid.
     * @param theStartingPosX The starting x-position.
     * @param theStartingPosY The starting y-position.
     * @param theGoalValue    The value representing the goal.
     * @param theBiome        The biome value for new rooms.
     */
    public void generateGrid(final int[][] theGrid, final int theStartingPosX, final int theStartingPosY, final int theGoalValue, final int theBiome) {

        int i = theStartingPosX;
        int j = theStartingPosY;
        int ii = theStartingPosX;
        int jj = theStartingPosY;
        int prevI = i;
        int prevJ = j;

        while (true) {

            final int direction = myRandom.nextInt(4) + 1; // 1-4

            switch (direction) {
                case 1:
                    ii = i + 1;
                    i = ii + 1;
                    break;
                case 2:
                    jj = j + 1;
                    j = jj + 1;
                    break;
                case 3:
                    ii = i - 1;
                    i = ii - 1;
                    break;
                case 4:
                    jj = j - 1;
                    j = jj - 1;
                    break;
            }

            // Check boundaries
            if (i < 0 || i >= theGrid.length || j < 0 || j >= theGrid[0].length) { //Returns the iterator somewhere back to the path
                while (true) {
                    if (theGoalValue == -2) {
                        final int backTrackI = myRandom.nextInt(theGrid.length / 2) * 2;
                        final int backTrackJ = myRandom.nextInt(theGrid.length / 2) * 2;

                        i = backTrackI;
                        ii = backTrackI;
                        j = backTrackJ;
                        jj = backTrackJ;
                        if (theGrid[i][j] == 2) {
                            break;
                        }
                    } else {
                        i = prevI;
                        ii = prevI;
                        j = prevJ;
                        jj = prevJ;
                        break;
                    }
                }
                continue; // Try another direction
            }

            if (theGoalValue == -2) {
                if (theGrid[i][j] == theGoalValue) {
                    theGrid[ii][jj] = 1;
                    break;
                } else {
                    theGrid[ii][jj] = 1;
                    theGrid[i][j] = 2;
                }
            } else {
                if (theGrid[i][j] >= theGoalValue && theGrid[i][j] != theBiome && theGrid[i][j] != theBiome + 10) {
                    theGrid[ii][jj] = 1;
                    break;
                } else {
                    theGrid[ii][jj] = 1;
                    theGrid[i][j] = theBiome;
                }
            }

            ii = i;
            jj = j;

            prevI = i;
            prevJ = j;
        }
    }

    /**
     * Displays the dungeon grid as ASCII art.
     *
     * @param theGrid The dungeon grid to display.
     */
    public void asciiGrid(final int[][] theGrid) {
        StringBuilder line1 = new StringBuilder();
        StringBuilder line2 = new StringBuilder();
        StringBuilder line3 = new StringBuilder();


        final String Horizontal_Hallway = "═══";
        final String Vertical_Hallway = "  ║  ";
        final String Empty_Room = "     ";
        final String Empty_Hallway = "   ";

        for (int i = 0; i < theGrid.length; i++) { // Outer loop for rows

            //first row, rooms
            if (i % 2 == 0) {
                for (int j = 0; j < theGrid[i].length; j++) { // Inner loop for columns
                    // even j means rooms
                    if (j % 2 == 0) {
                        if (theGrid[i][j] == EMPTY_CELL) {
                            line1.append(Empty_Room);
                            line2.append(Empty_Room);
                            line3.append(Empty_Room);
                        }
                        if (theGrid[i][j] == BLANK_ROOM) {
                            line1.append("┌───┐");
                            line2.append("│   │");
                            line3.append("└───┘");
                        }
                        if (theGrid[i][j] == BLANK_ROOM_TO_PILLAR) {
                            line1.append("┌───┐");
                            line2.append("│ 3 │");
                            line3.append("└───┘");
                        }
                        if (theGrid[i][j] == 4) {
                            line1.append("┌───┐");
                            line2.append("│ 4 │");
                            line3.append("└───┘");
                        }
                        if (theGrid[i][j] == 5) {
                            line1.append("┌───┐");
                            line2.append("│ 5 │");
                            line3.append("└───┘");
                        }
                        if (theGrid[i][j] == 6) {
                            line1.append("┌───┐");
                            line2.append("│ 6 │");
                            line3.append("└───┘");
                        }
                        if (theGrid[i][j] >= PILLAR_ROOM && theGrid[i][j] < PILLAR_ROOM + 4) {
                            line1.append("┌───┐");
                            line2.append("│ P │");
                            line3.append("└───┘");
                        }
                        if (theGrid[i][j] == STARTING_ROOM) {
                            line1.append("┌───┐");
                            line2.append("│ S │");
                            line3.append("└───┘");
                        }
                        if (theGrid[i][j] == ENDING_ROOM) {
                            line1.append("┌───┐");
                            line2.append("│ E │");
                            line3.append("└───┘");
                        }
                    } else { //odd j means hallways
                        if (theGrid[i][j] == 0) {
                            line1.append(Empty_Hallway);
                            line2.append(Empty_Hallway);
                            line3.append(Empty_Hallway);
                        } else {
                            line1.append(Empty_Hallway);
                            line2.append(Horizontal_Hallway);
                            line3.append(Empty_Hallway);
                        }
                    }
                }
                System.out.println(line1); // 3 lines in each row
                System.out.println(line2);
                System.out.println(line3);
                line1 = new StringBuilder();
                line2 = new StringBuilder();
                line3 = new StringBuilder();

            } else { // odd i - vertical will be empty room or a vertical hallway
                for (int j = 0; j < theGrid[i].length; j++) { // Inner loop for columns
                    if (j % 2 == 1) { //room
                        line1.append(Empty_Hallway);
                        line2.append(Empty_Hallway);
                    } else { //hallway

                        if (theGrid[i][j] == 1) {
                            line1.append(Vertical_Hallway);
                            line2.append(Vertical_Hallway);
                        } else {
                            line1.append(Empty_Room);
                            line2.append(Empty_Room);
                        }
                    }
                }


                System.out.println(line1);
                System.out.println(line2);
                line1 = new StringBuilder();
                line2 = new StringBuilder();
            }
        }


        System.out.println(line1); // 3 lines in each row
        System.out.println(line2);
        System.out.println(line3);
    }


    /**
     * Calculates the sum of all values in the grid.
     *
     * @param theGrid The grid to sum.
     * @return The sum of all values.
     */
    public int sumGrid(final int[][] theGrid) {
        int sum = 1;
        for (final int[] ints : theGrid) { // Outer loop for rows
            for (final int anInt : ints) { // Inner loop for columns
                sum += anInt;
            }
        }
        return sum;
    }

    /**
     * Calculates the sum of only the 'inner' rooms (value 2).
     *
     * @param theGrid The grid to sum.
     * @return The sum of inner room values.
     */
    public int innerSumGrid(final int[][] theGrid) {
        int sum = 1;
        for (final int[] ints : theGrid) { // Outer loop for rows
            for (final int anInt : ints) { // Inner loop for columns
                if (anInt == 2) sum += anInt;
            }
        }
        return sum;
    }

    /**
     * Inserts a small grid into a larger grid.
     *
     * @param theSmallGrid The smaller grid to insert.
     * @param theBigSize   The size of the larger grid.
     * @return The new, larger grid with the smaller one inserted.
     */
    public int[][] insertArray(final int[][] theSmallGrid, final int theBigSize) {
        final int[][] bigGrid = new int[theBigSize][theBigSize];

        // Calculate total padding needed on each axis
        final int rowPadding = theBigSize - theSmallGrid.length;
        final int colPadding = theBigSize - theSmallGrid[0].length;

        // Calculate offsets (14 on north/west, 16 on south/east when difference is 30)
        final int rowOffset = rowPadding / 2 - (rowPadding % 2 == 0 ? 0 : 1); // Round down
        final int colOffset = colPadding / 2 - (colPadding % 2 == 0 ? 0 : 1); // Round down

        // Insert smallGrid into bigGrid
        for (int i = 0; i < theSmallGrid.length; i++) {
            System.arraycopy(theSmallGrid[i], 0, bigGrid[rowOffset + i], colOffset, theSmallGrid[0].length);
        }
        return bigGrid;
    }
}