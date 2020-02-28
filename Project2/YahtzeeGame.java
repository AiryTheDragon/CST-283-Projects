/************************************************************************************************
 *  This class implements the backend of the Yahtzee Game
 *
 *  It keeps track of the scores and calculates the scoring of the game
 *
 *  CST 283 Programming Assignment2
 *  @author Michael Clinesmith
 ***********************************************************************************************/

public class YahtzeeGame
{
    private String[] scoreString = new String[21];
    private int[] scoreLine = new int[21];
    private final boolean[] SCOREABLE_LINES = {false, true, true, true, true, true, true, false, false, false,
            false, true, true, true, true, true, true, true, false, false, false};
    private boolean[] availableScores= new boolean[21];      // sets up lines that are still available for scoring
    private final int MAX_ROLLS=3, MAX_SCORES=13;
    private int rolls, scoresLeft;
    private int[] diceArray = new int[6];

    /**
     * No-argument constructor, creates the Yahtzee Game
     */
    public YahtzeeGame()
    {
        for(int i=0; i<21; i++)
        {
            scoreString[i]="--";
            scoreLine[i]=0;
            availableScores[i]=SCOREABLE_LINES[i];
        }
        rolls=MAX_ROLLS;
        scoresLeft=MAX_SCORES;
    }

    /**
     * Getter method to get the number of available rolls
     * @return int: The number of rolls remaining in the turn
     */
    public int getRolls()
    {
        return rolls;
    }

    /**
     * Getter method to get the maximum number of rolls in a turn
     * @return int: The maximum number of rolls in a turn
     */
    public int getMAX_ROLLS()
    {
        return MAX_ROLLS;
    }

    /**
     * Setter method for the number of rolls in a turn
     * @param rolls int: The number of rolls to set as remaining
     */
    public void setRolls( int rolls )
    {
        this.rolls = rolls;
    }

    /** Method that returns the number of scoring opportunities left in the game
     * @return int: The number of scoresLeft in the game
     */
    public int getScoresLeft()
    {
        return scoresLeft;
    }

    /**
     * Method that returns an integer value of a score for the given index
     * @param index int: integer indicating a line on the scorecard
     * @return int: The score of that line
     */
    public int getScoreOf(int index)
    {
        return scoreLine[index];
    }

    /**
     * Method that returns the String value of a score for the given index
     * @param index int: integer indicating a line on the scorecard
     * @return String: The String represention of the score
     */
    public String getScoreStringOf(int index)
    {
        return scoreString[index];
    }

    /**
     * Method that checks the values for scoring on a certain line
     * @param index int: integer indicating the line to be scored
     * @param dice Die[]: array of dice to be scored
     * @return int: the score for that line given the dice
     */
    public int checkScore( int index, Die[] dice)
    {
        int score;

        makeDiceArray( dice );      // prepare dice for scoring
        if (index>0 && index<7)
        {
            score = checkScoreUpper(index);
        }
        else if(index>=11 && index<=17)
        {
            score = checkScoreLower(index);
        }
        else
        {
            score = -1;
        }
        return score;
    }

    /**
     * Method that checks to see what the score in the upper section will be based on the type of dice being scored.
     * @param index int: the index position to check the score on, ranges from 1-6
     * @return int: the score for that index position
     */
    public int checkScoreUpper(int index)
    {
        return diceArray[index-1] * index;
    }

    /**
     * Method that checks to see what the score in the lower section will be based on the type of score being made.
     * It uses the dice that were counted in the DiceArray to make the determination
     * @param index int: the index position to check the score on, ranges from 11-17
     * @return int: the score the would be for that index position
     */
    public int checkScoreLower(int index)
    {
        int score = 0;

        if (index==11)   // 3 of a kind
        {
            boolean isValid=false;
            for(int i=0; i<6; i++)  // check if 3 of a kind exists
            {
                if (diceArray[i]>=3)
                {
                    isValid=true;
                }
            }
            if (isValid)
            {
                score = sumOfDice();
            }
        }
        if (index==12)   // 4 of a kind
        {
            boolean isValid=false;
            for(int i=0; i<6; i++)  // check if 4 of a kind exists
            {
                if (diceArray[i]>=4)
                {
                    isValid=true;
                }
            }
            if (isValid)
            {
                score = sumOfDice();
            }
        }
        if (index==13)   // full house - 3 of one number and 2 of another (or possibly 5 of one number)
        {
            boolean has2=false, has3=false, has5=false;

            for(int i=0; i<6; i++)  // check if 4 of a kind exists
            {
                if (diceArray[i]==2)
                {
                    has2=true;
                }
                else if (diceArray[i]==3)
                {
                    has3=true;
                }
                else if (diceArray[i]==5)
                {
                    has5=true;
                }
            }
            if ((has2 && has3) || has5)
            {
                score = 25;
            }
        }
        if (index==14)   // Small Straight - must have 1,2,3,4 or 2,3,4,5 or 3,4,5,6
        {
            boolean isValid=false;
            if( diceArray[0]>=1 && diceArray[1]>=1 && diceArray[2]>=1 && diceArray[3]>=1)
            {
                isValid=true;
            }
            else if (diceArray[1]>=1 && diceArray[2]>=1 && diceArray[3]>=1 && diceArray[4]>=1)
            {
                isValid=true;
            }
            else if (diceArray[2]>=1 && diceArray[3]>=1 && diceArray[4]>=1 && diceArray[5]>=1)
            {
                isValid=true;
            }

            if (isValid)
            {
                score = 30;
            }
        }
        if (index==15)  // Large Straight = must have 1,2,3,4,5 or 2,3,4,5,6
        {
            boolean isValid=false;
            if (diceArray[1]==1 && diceArray[2]==1 && diceArray[3]==1 && diceArray[4]==1 && (diceArray[0]==1 || diceArray[5]==1))
            {
                isValid = true;
            }
            if (isValid)
            {
                score = 40;
            }
        }
        if (index==16)   // yahtzee - 5 of one number
        {
            boolean has5=false;

            for(int i=0; i<6; i++)  // check if 5 of a kind exists
            {
                if (diceArray[i]==5)
                {
                    has5=true;
                }
            }
            if ( has5)
            {
                score = 50;
            }
        }
        if (index==17)  // chance - just sum 5 dice
        {
            score = sumOfDice();
        }
        return score;
    }

    /**
     * Method that sets the score for one of the lines in the scorecard
     * It then also updates the other scores on the scorecard as necessary
     * @param index int: The line on the scorecard to update
     * @param dice Die[]: The array of dice objects used in the scoring
     * @return String: A String representation of the score value that has been set
     */
    public String score(int index, Die[] dice)
    {
        int value = checkScore( index, dice );
        scoreLine[index]=value;
        scoreString[index] = Integer.toString( value );
        scoreUpdate();

        availableScores[index]=false;               // since scored, this item is no longer available to score
        rolls=MAX_ROLLS;
        scoresLeft--;

        return scoreString[index];
    }

    /**
     * Method that calculates the sum of the dice counted in the diceArray
     * @return int: The sum of dice in the diceArray
     */
    public int sumOfDice()
    {
        int sum=0;
        for (int i=0; i<6; i++)
        {
            sum += (i+1) * diceArray[i];    // adds to sum the dice value * number of dice of that value
        }
        return sum;
    }

    /**
     * This method updates the values that are indirectly changed
     */
    public void scoreUpdate()
    {
        int upperScore=0, lowerScore=0;

        // check upper section scores
        for (int i=1; i<7; i++) // totals upper score
        {
            upperScore+=scoreLine[i];
        }
        scoreLine[7]= upperScore;
        scoreString[7]=Integer.toString( upperScore );
        if (upperScore>=63)     // check for upper bonus
        {
             scoreLine[8]=35;
             scoreString[8]=Integer.toString( 35 );
             upperScore+=35;
        }
        scoreLine[9]= upperScore;
        scoreString[9]=Integer.toString( upperScore );
        scoreLine[19]= upperScore;
        scoreString[19]=Integer.toString( upperScore );

        // check lower section scores
        for (int i=11; i<18; i++)
        {
            lowerScore+=scoreLine[i];
        }
        scoreLine[18] = lowerScore;
        scoreString[18]=Integer.toString( lowerScore );
        scoreLine[20] = lowerScore+upperScore;
        scoreString[20]=Integer.toString( scoreLine[20] );
    }

    /**
     * Method called when a roll is made
     * @return boolean: True if there are rolls remaining, false if not
     */
    public boolean madeRoll()
    {
        boolean rollsLeft=true;
        rolls--;
        if (rolls<1)
        {
            rollsLeft=false;
        }
        return rollsLeft;
    }

    /**
     * Method that checks if the games is completed, this is, all scores have been made
     * @return boolean: True if the game is completed, false if not
     */
    public boolean isComplete()
    {
        boolean complete=true;
        for (int i=0; i<21 && complete; i++)
        {
            if (availableScores[i])     // if a position can still be scored
            {
                complete=false;
            }
        }
        return complete;
    }

    /**
     * Method to count the number of 1, 2, 3, 4, 5, and 6s on the dice for easier scoring
     * for example:
     * diceArray[0] will store the number of 1s in the Die array
     * ...
     * diceArray[5] will store the number of 6s in the Die array
     *
     * @param dice Die[]: An array of dice with values 1 to 6 to count for each number
     * @return int[]: Not specifically "returned" but calling function can use array to help score the dice
     */
    public void makeDiceArray(Die[] dice)
    {
        int len = dice.length;
        int val;
        //initializes array
        for (int i=0; i<6; i++)
        {
            diceArray[i]=0;
        }
        // counts each number on dice
        for (int i=0; i<len; i++)
        {
            val = dice[i].getValue();   // get value in this Die

            if (val>=1 && val<=6)       // if valid value
            {
                diceArray[val-1]++;
            }
        }
    }

    /**
     * Method to reset the values of the game so a new game can be run
     */
    public void resetGame()
    {
        for(int i=0; i<21; i++)
        {
            scoreString[i]="--";
            scoreLine[i]=0;
            availableScores[i]=SCOREABLE_LINES[i];
        }
        rolls=MAX_ROLLS;
        scoresLeft=MAX_SCORES;
    }

}
