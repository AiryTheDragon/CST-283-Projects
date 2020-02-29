/************************************************************************************************
 *  This class contains stores the zombie positions and visited locations for the zombie application
 *
 *  Note: when thinking of a position position (i, j) - i represents the column and j represents the row
 *  similar to cartesian coordinates.
 *  e.g. (4,0) is the 5th element in the 1st row
 *
 *  Features:
 *  This class implements the creation, resetting and updating of the zombie location and visitation grids
 *
 *  It implements the zombie movement - for each step a zombie moves exactly one space in a random location
 *  and does not move off the grid
 *
 *  Based on user options, a grid may be displayed to the console displaying a combined grid of zombie
 *  positions and visited locations
 *
 *  CST 283 Programming Assignment 4
 *  @author Michael Clinesmith
 ***********************************************************************************************/

import java.util.Random;

public class ZombieZone
{
    private int MAX_DIMENSION = 20, MIN_DIMENSION=3;
    private int widthDimension, heightDimension;
    private int[][] zombieGridCount = new int[MAX_DIMENSION][MAX_DIMENSION];
    private int[][] zombieGridTemp = new int[MAX_DIMENSION][MAX_DIMENSION];
    private boolean[][] zombieGridVisit = new boolean[MAX_DIMENSION][MAX_DIMENSION];
    private int steps=0;

    /**
     * No-argument constructor creates a default zombie zone of 10X10 dimension
     */
    public ZombieZone()
    {
        widthDimension=10;
        heightDimension=10;
        resetZombieGrid();
        resetZombieVisitGrid();
    }

    /**
     * Constructor that creates a zombie zone of size width X height
     * @param width int: the width of the created zone
     * @param height int: the height of the created zone
     */
    public ZombieZone(int width, int height)
    {
        widthDimension=width;
        heightDimension=height;
        // Keep array dimensions in bounds
        if (width<MIN_DIMENSION)
        {
            widthDimension=MIN_DIMENSION;
        }
        if (height<MIN_DIMENSION)
        {
            heightDimension=MIN_DIMENSION;
        }
        if (width>MAX_DIMENSION)
        {
            widthDimension=MAX_DIMENSION;
        }
        if (height>MAX_DIMENSION)
        {
            heightDimension=MAX_DIMENSION;
        }
        resetZombieGrid();
        resetZombieVisitGrid();
    }

    /**
     * Constructor that creates a zombie zone of size width X height with zombies in the center
     * @param width int: the width of the created zone
     * @param height int: the height of the created zone
     * @param zombieNumber int: The number of zombies to place in the center of the zone
     */
    public ZombieZone(int width, int height, int zombieNumber)
    {
        widthDimension=width;
        heightDimension=height;
        // Keep array dimensions in bounds
        if (width<MIN_DIMENSION)
        {
            widthDimension=MIN_DIMENSION;
        }
        if (height<MIN_DIMENSION)
        {
            heightDimension=MIN_DIMENSION;
        }
        if (width>MAX_DIMENSION)
        {
            widthDimension=MAX_DIMENSION;
        }
        if (height>MAX_DIMENSION)
        {
            heightDimension=MAX_DIMENSION;
        }
        resetZombieGrid(zombieNumber);
        resetZombieVisitGrid();
    }

    /**
     * Method to get the number of zombies currently located at position (x,y)
     * @param x int: x coordinate position
     * @param y int: y coordinate position
     * @return int: The number of zombies located at position (x,y)
     */
    public int getZombiesAt(int x, int y)
    {
        return zombieGridCount[x][y];
    }

    /**
     * Method to get if the position (x,y) has been visited by zombies
     * @param x int: x coordinate position
     * @param y int: y coordinate position
     * @return int: true if the position (x,y) has been visited by zombies, false otherwise
     */
    public boolean getVisitedAt(int x, int y)
    {
        return zombieGridVisit[x][y];
    }

    /**
     * Accessor method to get the width of the current grid
     * @return int: The width of the grid (number of columns)
     */
    public int getWidth()
    {
        return widthDimension;
    }

    /**
     * Accessor method to get the height of the current grid
     * @return int: The height of the grid (number of rows)
     */
    public int getHeight()
    {
        return heightDimension;
    }

    /**
     * Accessor method to get the number of steps taken in the grid
     * @return
     */
    public int getSteps()
    {
        return steps;
    }

    /**
     * Mutator method to set the width of the current grid
     * @param widthDimension int: The width of the grid (number of columns)
     */
    public void setWidth( int widthDimension )
    {
        this.widthDimension = widthDimension;

        if (widthDimension<MIN_DIMENSION)       // make sure widthDimension stays within bounds
        {
            this.widthDimension=MIN_DIMENSION;
        }
        if (widthDimension>MAX_DIMENSION)
        {
            this.widthDimension=MAX_DIMENSION;
        }
    }

    /**
     * Mutator method to set the height of the current grid
     * @param heightDimension int: The height of the grid (number of rows)
     */
    public void setHeight( int heightDimension )
    {
        this.heightDimension = heightDimension; // make sure heightDimension stays within bounds

        if (heightDimension<MIN_DIMENSION)
        {
            this.heightDimension=MIN_DIMENSION;
        }
        if (heightDimension>MAX_DIMENSION)
        {
            this.heightDimension=MAX_DIMENSION;
        }
    }

    /**
     * Mutator to set the number of steps taken
     * @param steps int: The number of steps taken
     */
    public void setSteps( int steps )
    {
        this.steps = steps;
    }

    /**
     * Method to set the number of zombies located at position (x,y)
     * @param x int: x coordinate position
     * @param y int: y coordinate position
     * @param zombieCount int: the number of zombies to set at position (x,y)
     */
    public void setZombiesAt(int x, int y, int zombieCount)
    {
        if (zombieCount>=0)          // only set value if zombieCount is at least 0
        {
            zombieGridCount[x][y] = zombieCount;
        }
    }

    /**
     * Method to set if a zombie has visited the position (x,y) or not
     * @param x int: x coordinate position
     * @param y int: y coordinate position
     * @param isVisited boolean: set if a zombie has visited a position or not
     */
    public void setVisitedAt(int x, int y, boolean isVisited)
    {
        zombieGridVisit[x][y]=isVisited;
    }

    /**
     *  Method that creates a new zombie grid based on currently set values for widthDimension and heightDimension
     *  Also resets the number of steps
     */
    public void resetZombieGrid()
    {
        for (int i=0; i<MAX_DIMENSION; i++)
        {
            for (int j=0; j<MAX_DIMENSION; j++ )
            {
                zombieGridCount[i][j]=0;
            }
        }
        steps=0;
    }

    /**
     * Method that creates a new zombie grid based on currently set values for widthDimension and heightDimension
     * and places zombies in the center of the grid
     * @param zombieNumber int: the number of zombies to place in the grid
     */
    public void resetZombieGrid( int zombieNumber)
    {
        resetZombieGrid();

        // put zombies in middle if the value is greater than 0
        if (zombieNumber>0)
        {
            zombieGridCount[widthDimension / 2][heightDimension / 2] = zombieNumber;
            zombieGridVisit[widthDimension / 2][heightDimension / 2] = true;
        }
    }

    /**
     * Method that creates a new zombie visiting field based on currently set values for the ZombieZone object
     */
    public void resetZombieVisitGrid()
    {
        for (int i=0; i<MAX_DIMENSION; i++)
        {
            for (int j=0; j<MAX_DIMENSION; j++ )
            {
                zombieGridVisit[i][j]=false;
            }
        }
        if(zombieGridCount[widthDimension/2][heightDimension/2]>0) // check if zombies in middle of grid
        {
            zombieGridVisit[widthDimension / 2][heightDimension / 2] = true;
        }
    }

    /**
     * Method to update the ZombieGridVisit array to account for moved zombies
     */
    public void updateZombieVisitGrid()
    {
        for (int i=0; i<widthDimension; i++)
        {
            for (int j=0; j<heightDimension; j++ )
            {
                if(zombieGridCount[i][j]>0)
                {
                    zombieGridVisit[i][j] = true;
                }
            }
        }
    }

    /**
     * Metheod to add a zombie at the given position
     * @param x int: x coordinate position
     * @param y int: y coordinate position
     */
    public void addZombieAt(int x, int y)
    {
        zombieGridCount[x][y]++;
        zombieGridVisit[x][y]=true;
    }

    /**
     * Method to remove a zombie at the given position
     * The method does not allow the number of zombies to become less than 0
     * @param x int: the x coordinate position
     * @param y int: the y coordinate position
     */
    public void removeZombieAt(int x, int y)
    {
        zombieGridCount[x][y]--;
        if (zombieGridCount[x][y]<0)
        {
            zombieGridCount[x][y]=0;
        }
    }

    /**
     * Method that counts the number of zombies in the grid
     * @return int: The total number of zombies in the grid
     */
    public int zombieTotal()
    {
        int total=0;
        for (int i=0; i<widthDimension; i++)
        {
            for (int j=0; j<heightDimension; j++ )
            {
                total += zombieGridCount[i][j];
            }
        }
        return total;
    }

    /**
     * Method to reset the zombie grid with new dimensions
     * @param width int: The number of columns in the grid
     * @param height int: the number of rows in the grid
     * @param zom int: The number of zombies to put in the middle of the grid
     */
    public void newZombieGrid(int width, int height, int zom)
    {
        // Keep array dimensions in bounds
        if (width<MIN_DIMENSION)
        {
            widthDimension=MIN_DIMENSION;
        }
        if (height<MIN_DIMENSION)
        {
            heightDimension=MIN_DIMENSION;
        }
        if (width>MAX_DIMENSION)
        {
            widthDimension=MAX_DIMENSION;
        }
        if (height>MAX_DIMENSION)
        {
            heightDimension=MAX_DIMENSION;
        }
        resetZombieGrid(zom);
        resetZombieVisitGrid();
        steps=0;
    }

    /**
     * Method ot reset the temporary grid used to move zombies
     */
    public void resetTempGrid()
    {
        for (int i=0; i<MAX_DIMENSION; i++)
        {
            for (int j=0; j<MAX_DIMENSION; j++ )
            {
                zombieGridTemp[i][j]=0;
            }
        }
    }

    /**
     * Method to move all the zombies in the grid
     * The zombies will move exactly one space and may not move off the grid
     * It will increase the number of steps by one.
     * @param isToConsole boolean: true is the grid is to be displayed to the console, false if not.
     */
    public void oneStep(boolean isToConsole)
    {
        resetTempGrid();
        boolean isValidMove;
        int moveX=0, moveY=0;
        int randomValue;
        Random randomNumbers = new Random(  );
        steps++;

        // loops to move zombies to temp grid
        for (int i=0; i<widthDimension; i++)
        {
            for (int j=0; j<heightDimension; j++ )
            {
                for (int k=0; k<zombieGridCount[i][j]; k++) // loop once for each zombie
                {
                    isValidMove=false;
                    while (!isValidMove) // loop until zombie moves to a valid location
                    {
                        moveX = i;
                        moveY = j;
                        randomValue = randomNumbers.nextInt( 4 );
                        switch (randomValue)
                        {
                            case 0:
                                moveX++;
                                break;
                            case 1:
                                moveY++;
                                break;
                            case 2:
                                moveX--;
                                break;
                            case 3:
                                moveY--;
                                break;
                        }
                        if (moveX >= 0 && moveX < widthDimension && moveY >= 0 && moveY < heightDimension) // check if valid move
                        {
                            isValidMove = true;
                        }
                    }
                    zombieGridTemp[moveX][moveY]++;
                }
            }
        }
        // loops to copy temp grid to real grid
        for (int i=0; i<widthDimension; i++)
        {
            for (int j=0; j<heightDimension; j++ )
            {
                zombieGridCount[i][j]=zombieGridTemp[i][j];
            }
        }
        // update visit grid
        updateZombieVisitGrid();

        // display to console if true
        if(isToConsole)
        {
            displayGrid();
        }

    }

    /**
     * Method to check is all locations in the grid have been visited by zombies
     * @return
     */
    public boolean isAllVisited()
    {
        boolean isAllVisited=true;
        for (int i=0; i<widthDimension && isAllVisited; i++)
        {
            for (int j=0; j<heightDimension && isAllVisited; j++ )
            {
                if (!zombieGridVisit[i][j]) // if a position has not been visited
                {
                    isAllVisited=false;
                }
            }
        }
        return isAllVisited;
    }

    /**
     * Method to display a combination of the grids to the console
     * The grid has a border and prints the number of zombies in each location
     * If there is no zombie in a location, it will display an "X" for visited locations
     * or a blank for unvisited locations
     */
    public void displayGrid()
    {
        // first line
        System.out.print("|");
        for(int i=0; i<widthDimension; i++)
        {
            System.out.print("-");
        }
        System.out.print("|\n");
        for(int j=0; j<heightDimension; j++) // each iteration of j loop prints a row of the grid
        {
            System.out.print("|");
            for (int i=0; i<widthDimension; i++)
            {
                if (zombieGridCount[i][j]>9)        // if many zombies, display Z
                {
                    System.out.print( "Z" );
                }
                else if(zombieGridCount[i][j]>0)    // display the number of zombies
                {
                    System.out.print( zombieGridCount[i][j] );
                }
                else if(zombieGridVisit[i][j])      // if no zombies but visited, display "x"
                {
                    System.out.print("x");
                }
                else                                // otherwise display blank space
                {
                    System.out.print(" ");
                }
            }
            System.out.print("|\n");
        }
        // last line of grid
        System.out.print("|");
        for(int i=0; i<widthDimension; i++)
        {
            System.out.print("-");
        }
        System.out.print("|\n");
        // list step number then 2 new lines
        System.out.println( "Step: "+steps +"\n");
    }
}
