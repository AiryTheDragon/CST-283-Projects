/************************************************************************************************
 *  This class contains the main driver of the program and provides a the interface for the Yahtzee Game
 *
 *  The interface includes a Yahtzee scorecard, a title box, 5 dice to be rolled, instructions that can
 *  be viewed, buttons, to roll, and set scores, and to restart and quit the game
 *
 *  It creates a YahtzeeGame object, which it uses to score its lines
 *  It uses Die objects, which are the dice that can be rolled
 *
 *  When the game completes, a popup displays the game score and offers the choice of a new game
 *
 *  CST 283 Programming Assignment2
 *  @author Michael Clinesmith
 ***********************************************************************************************/

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class YahtzeeInterface extends Application
{
    private BorderPane mainLayout;
    private YahtzeeGame theGame;        // The game object

    // Scorecard layout objects
    private Label[] scorecardLabels = new Label[21];
    private Label[] scorecardScores = new Label[21];
    private Button[] scorecardButtons = new Button[21];
    private GridPane scorecard;
    private String[] cardLabels = {"Upper Section",
            "Aces = 1",
            "Twos = 2",
            "Threes = 3",
            "Fours = 4",
            "Fives = 5",
            "Sixes = 6",
            "Upper Sum",
            "Bonus for 63+",
            "UPPER TOTAL",
            "Lower Section",
            "3 of a kind",
            "4 of a kind",
            "Full House",
            "Sm. Straight",
            "Lg. Straight",
            "Yahtzee",
            "Chance",
            "LOWER TOTAL",
            "UPPER TOTAL",
            "COMBINED TOTAL"};
    // Denotes lines on scorecard able to directly score
    private final boolean[] SCOREABLE_LINES = {false, true, true, true, true, true, true, false, false, false,
            false, true, true, true, true, true, true, true, false, false, false};
    private boolean[] availableScores = new boolean[21];      // sets up lines that are still available for scoring

    // Dice layout objects
    private HBox DiceHolder;
    private Die dice[] = new Die[5];
    private CheckBox holdDie[] = new CheckBox[5];
    private VBox[] diceBox = new VBox[5];

    // Action Button layout objects
    private Button rollButton;
    private Button scoreButton;
    private VBox rollBox;

    // Top Screen
    private Button newGameButton;
    private Button quitButton;
    private Label yahtzeeTitleLabel;
    private HBox TitleBox;

    // Informational panels
    private RadioButton radioGamePlay, radioScoreExplanation, radioScoreValue;
    private ToggleGroup radioGroup;
    private VBox radioInfoVBox;
    private TextArea gameInstructions, scoringInstructions;
    private VBox diceAndInstBox;
    private String gameInstruct="Game Play: \n" +
            "Click the roll button to roll the dice\n" +
            "Click the hold button below a die to not\n" +
            "roll that die when the other dice are rolled.\n" +
            "You have up to 3 rolls per turn.\n" +
            "Click the UNLOCK SCORES button to open areas of the \n" +
            "game board to score.\n" +
            "Click the score button to be given an option to score in that position\n" +
            "a popup will show up showing you the value and allowing you\n" +
            "to confirm that score\n" +
            "Cancelling that popup will let you roll again if you have remaining rolls.\n" +
            "After confirming yoru score, roll the dice again for you next turn.\n" +
            "Continue until you complete the game!\n" +
            "Have fun!\n\n";
    private String scoringInstruct="Scoring Instructions: \n" +
            "Upper section:\n" +
            "In the upper section, for each line you score only that kind of dice.\n" +
            "For example if you have three 5s and two 3s, \n" +
            "You can score 15 for the 5s, or 6 for the 3s.\n" +
            "Getting at least 63 in the upper section will give\n +" +
            "You a 35 point bonus.\n\n" +
            "Lower section:\n" +
            "For three of a kind-requires at least 3 of one kind of dice to score.\n" +
            "Four of a kind requires at least 4 of one kind of dice,\n" +
            "A full house requires 3 of one kind of dice and 2 of another.\n" +
            "A small straight requires a run of 4 dice, e.g. 2, 3, 4, 5\n" +
            "A large straight requires a run of 5 dice, e.g. 1, 2, 3, 4, 5\n" +
            "A Yahtzee requires 5 of one kind of die , e.g. 4, 4, 4, 4, 4\n" +
            "There is no requirement on the chance.\n\n";
    private String scoringValues= "Scoring values:\n" +
            "Aces: 1 point for each 1\n" +
            "2s: 2 points for each 2\n" +
            "3s: 3 points for each 3\n" +
            "4s: 4 points for each 4\n" +
            "5s: 5 points for each 5\n" +
            "6s: 6 points for each 6\n" +
            "3 of a kind: add all dice\n" +
            "4 of a kind: add all dice\n" +
            "Full House: 25\n" +
            "Small Striaght: 30\n" +
            "Large Straight: 40\n" +
            "Yahtzee: 50\n" +
            "Chance: add all dice\n";

    // Roll count layout objects
    private Label rollCountLabel;
    private Label rollCount;
    private VBox rollCountBox;

    /**
     * Starting method of application - calls launch
     * @param args String[]: Not used
     */
    public static void main( String[] args )
    {
        // Launch the application.
        launch( args );
    }

    /**
     * Method that calls the initializeScene method and creates the scene
     *
     * @param primaryStage Stage object used to create the stage
     */
    @Override
    public void start( Stage primaryStage )
    {
        initializeScene();

        // Set up overall scene
        Scene scene = new Scene( mainLayout, 1100, 900 );
        scene.getStylesheets().add( "Yaht.css" );
        primaryStage.setScene( scene );
        primaryStage.setTitle( "YAHTZEE! - Game design by Michael Clinesmith" );
        primaryStage.show();
    }

    /**
     * Method that calls other methods to create the game, create the interface, then sets up the mainLayout
     */
    public void initializeScene()
    {
        theGame = new YahtzeeGame();
        createTitle();
        createInfoBoxes();
        createScorecard();
        createRollButtons();
        createDiceHolders();

        mainLayout = new BorderPane();
        mainLayout.setTop( TitleBox );
        mainLayout.setLeft( scorecard );
        mainLayout.setCenter( diceAndInstBox );
    }

    /**
     * Method that creates the interface parts at the top of the screen, the name and new game and quit buttons
     */
    public void createTitle()
    {
        newGameButton = new Button( "NEW GAME" );
        newGameButton.setOnAction( new GameButtonHandler() );

        quitButton = new Button( "QUIT" );
        quitButton.setOnAction( new GameButtonHandler() );

        yahtzeeTitleLabel = new Label( "YAHTZEE!" );
        yahtzeeTitleLabel.setStyle( "-fx-font-size: 24; -fx-text-fill:  orange" );

        TitleBox = new HBox( 50, yahtzeeTitleLabel, newGameButton, quitButton );
        TitleBox.setAlignment( Pos.CENTER );
        TitleBox.setPadding( new Insets( 20 ) );
    }

    /**
     * Method that sets up the information box in the center of the interface and the radio
     * buttons that control the information in the box
     */
    public void createInfoBoxes()
    {
        // create radiobuttons that control info in text area
        radioGamePlay = new RadioButton( "Game Play" );
        radioScoreExplanation = new RadioButton( "Score Explanation" );
        radioScoreValue = new RadioButton( "Score Values" );

        radioGroup = new ToggleGroup();
        radioGamePlay.setToggleGroup( radioGroup );
        radioGamePlay.setSelected( true );
        radioGamePlay.setOnAction( new RadioHandler() );
        radioScoreExplanation.setToggleGroup( radioGroup );
        radioScoreExplanation.setOnAction( new RadioHandler() );
        radioScoreValue.setToggleGroup( radioGroup );
        radioScoreValue.setOnAction( new RadioHandler() );

        radioInfoVBox= new VBox( 10, radioGamePlay, radioScoreExplanation, radioScoreValue  );
        radioInfoVBox.setPadding( new Insets( 10 ) );
        radioInfoVBox.setAlignment( Pos.CENTER );

        // create textArea
        gameInstructions = new TextArea( gameInstruct);
        gameInstructions.setEditable( false );
        gameInstructions.setPadding( new Insets( 50 ) );
        gameInstructions.setPrefRowCount( 25 );

    }

    /**
     * Method that creates the buttons next to the dice for rolling and scoring
     */
    public void createRollButtons()
    {
        rollButton = new Button( "ROLL DICE" );
        rollButton.setOnAction( new RollButtonHandler() );

        scoreButton = new Button( "UNLOCK SCORES" );
        scoreButton.setOnAction( new RollButtonHandler() );
        scoreButton.setDisable( true );     // score Button initially disabled

        rollBox = new VBox( 10, rollButton, scoreButton );
        rollBox.setAlignment( Pos.CENTER );
    }

    /**
     * Method that creates the Yahtzee Scorecard
     * It also sets the values in the associated availableScores Array controlling the scoreable fields
     */
    public void createScorecard()
    {
        scorecard = new GridPane();
        for (int i = 0; i < 21; i++)
        {
            scorecardLabels[i] = new Label( cardLabels[i] );
            scorecardLabels[i].setPadding( new Insets( 10 ) );
            scorecardScores[i] = new Label( "--" );
            scorecardScores[i].setPadding( new Insets( 10 ) );
            if (SCOREABLE_LINES[i])         // if this line is directly scoreable
            {
                scorecardButtons[i] = new Button( "score" );
            } else
            {
                scorecardButtons[i] = new Button( "-----" );
            }

            scorecardButtons[i].setOnAction( new ScoringButtonHandler() );
            scorecardButtons[i].setDisable( true );
            scorecard.add( scorecardLabels[i], 0, i );
            scorecard.add( scorecardScores[i], 1, i );
            scorecard.add( scorecardButtons[i], 2, i );
            scorecard.setAlignment( Pos.CENTER );
            scorecard.setGridLinesVisible( true );
            availableScores[i] = SCOREABLE_LINES[i];        // sets the lines on the scorecard that can be scored
        }

        // highlight important sections
        scorecardScores[9].setStyle( "-fx-font-weight: bold" );     // Upper Total
        scorecardLabels[9].setStyle( "-fx-font-weight: bold" );
        scorecardScores[18].setStyle( "-fx-font-weight: bold" );    // Lower Total
        scorecardLabels[18].setStyle( "-fx-font-weight: bold" );
        scorecardScores[20].setStyle( "-fx-font-weight: bold" );    // Combined Total
        scorecardLabels[20].setStyle( "-fx-font-weight: bold" );

        scorecard.setPadding( new Insets( 10 ) );
    }

    /**
     * Method that creates the Dice, the hold checkboxes, the rolls remaining fields
     * and combines them along with the roll buttons, and information controls into the diceAndInstBox
     */
    public void createDiceHolders()
    {
        for (int i = 0; i < 5; i++)
        {
            dice[i] = new Die( 7 );
            dice[i].setSize( 80 );
            holdDie[i] = new CheckBox( "Hold" );
            holdDie[i].setPadding( new Insets( 10 ) );
            holdDie[i].setDisable( true );      // initially disable hold when invalid dice
            diceBox[i] = new VBox( 10, dice[i].getImageView(), holdDie[i] );
            diceBox[i].setAlignment( Pos.CENTER );
        }

        // labels for keeping track of rolls remaining
        rollCountLabel = new Label( "Rolls Remaining:" );
        rollCountLabel.setAlignment( Pos.CENTER );
        rollCount = new Label( Integer.toString( theGame.getRolls() ) );
        rollCount.setAlignment( Pos.CENTER );
        rollCountBox = new VBox( 10, rollCountLabel, rollCount );
        rollCountBox.setAlignment( Pos.CENTER );

        // Put together Dice Holder box
        DiceHolder = new HBox( 10, diceBox );
        DiceHolder.getChildren().add( rollBox );
        DiceHolder.getChildren().add( rollCountBox );
        DiceHolder.setAlignment( Pos.CENTER );
        DiceHolder.setPadding( new Insets(  50) );
        // puts dice, radiobuttons controlling info, and text area together
        diceAndInstBox = new VBox( 10,  DiceHolder , radioInfoVBox, gameInstructions);
    }

    /**
     * Method that unlocks the scoring buttons that have not yet been scored so the user can select them
     */
    public void unlockScoringButtons()
    {
        for (int i = 0; i < 21; i++)
        {
            if (availableScores[i])
            {
                scorecardButtons[i].setDisable( false );
            }
        }
        rollButton.setDisable( true );  // locks rolling button while scoring
    }

    /**
     * Method that locks the scoring buttons preventing scoring when not applicable
     */
    public void lockScoringButtons()
    {
        for (int i = 0; i < 21; i++)
        {
            scorecardButtons[i].setDisable( true );
        }
    }

    /**
     * Method that prevents the hold die locks from being selected when not applicable
     */
    public void lockHolds()
    {
        for (int i = 0; i < 5; i++)
        {
            holdDie[i].setDisable( true );
        }
    }

    /**
     * Methood that prepares for the next round after scoring or a new game by locking the scoring buttons, resetting the dice,
     * locking the dice holds, locking the score button, unlocking the roll button and resetting the rolls
     */
    public void prepareNextRound()
    {

        lockScoringButtons();

        // resets dice
        for (int i = 0; i < 5; i++)
        {
            dice[i].setValue( 7 );
            diceBox[i].getChildren().clear();
            diceBox[i].getChildren().addAll( dice[i].getImageView(), holdDie[i] );
            holdDie[i].setSelected( false );
            diceBox[i].setAlignment( Pos.CENTER );
        }
        lockHolds();

        // locks score button by dice
        scoreButton.setDisable( true );
        // unlocks roll button by dice
        rollButton.setDisable( false );

        rollCount.setText( Integer.toString( theGame.getRolls() ) );    // set roll count label

    }

    /**
     * Method that updates the fields on the scorecard
     */
    public void updateScores()
    {
        for (int i = 0; i < 21; i++)
        {
            scorecardScores[i].setText( theGame.getScoreStringOf( i ) );
        }

    }

    /**
     * Method that runs after a game has been completed, displaying the score and
     * giving the user the option to restart the game
     */
    public void gameCompleted()
    {
        lockScoringButtons();
        String message = "You completed the game!  Your final score was " + scorecardScores[20].getText() + "!" +
                "\nClick OK to reset the game board and play again!";

        Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
        alert.setTitle( "Final Score" );
        alert.setContentText( message );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            resetGame();
        }

    }

    /**
     * Method that resets the game
     * It resets the scores by calling the YahtzeeGame object, updates those scores to the interface,
     * the prepares for the next round
     */
    public void resetGame()
    {
        theGame.resetGame();
        updateScores();
        prepareNextRound();
        resetAvailableScores();
    }

    /**
     * Method that resets the availableScores array for a new game!
     */
    public void resetAvailableScores()
    {
        for (int i=0; i<21; i++)
        {
            availableScores[i] = SCOREABLE_LINES[i];        // sets the lines on the scorecard that can be scored
        }
    }

    /**
     * Class that handles the ActionEvents from the roll and unlock scoring buttons
     */
    class RollButtonHandler implements EventHandler<ActionEvent>
    {
        /**
         * Method that handles the roll and unlock scoring button clicks
         * @param event ActionEvent: event triggered by a button click
         */
        @Override
        public void handle( ActionEvent event )
        {
            boolean stillRolls = true;
            if (event.getSource() == rollButton)      // dice are rolled
            {
                for (int i = 0; i < 5; i++)
                {
                    if (holdDie[i].isSelected() == false)
                    {
                        dice[i].roll();
                        // clear and read dice back to the diceBox with new images
                        diceBox[i].getChildren().clear();
                        diceBox[i].getChildren().addAll( dice[i].getImageView(), holdDie[i] );
                        diceBox[i].setAlignment( Pos.CENTER );
                    }
                    holdDie[i].setDisable( false ); // unlock holds on roll
                }
                scoreButton.setDisable( false );    // possible to score

                // make updates on roll number
                stillRolls = theGame.madeRoll();                                // check if still rolls available
                rollCount.setText( Integer.toString( theGame.getRolls() ) );    // set roll count label
                if (!stillRolls)                                                // disable rolls if no rolls remaining
                {
                    rollButton.setDisable( true );
                }
            }
            if (event.getSource() == scoreButton)     // prepare to score
            {
                unlockScoringButtons();
            }

        }
    }

    /**
     * Class that handles the ActionEvents for the scoring buttons on the Yahtzee scorecard
     */
    class ScoringButtonHandler implements EventHandler<ActionEvent>
    {
        /**
         * Method that handles the ActionEvents for the scoring buttons on the Yahtzee scorecard
         * @param event ActionEvent: event triggered by a button click
         */
        @Override
        public void handle( ActionEvent event )
        {
            String message = "";
            int score = 0;
            String scoreString = "";

            for (int i = 0; i < 21; i++)
            {
                if (event.getSource() == scorecardButtons[i])
                {
                    score = theGame.checkScore( i, dice );
                    message = "The scoring value for " + scorecardLabels[i].getText() + " is " + score + "." +
                            "\n Do you want to make this score?";

                    Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
                    alert.setTitle( "Verify Score" );
                    alert.setContentText( message );
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK)
                    {
                        scoreString = theGame.score( i, dice );
                        updateScores();
                        scorecardScores[i].setText( scoreString );
                        availableScores[i] = false;
                        if (theGame.isComplete())
                        {
                            gameCompleted();
                        } else
                        {
                            prepareNextRound();
                        }
                    } else    // decide not to score, so lock scoring buttons and unlock roll if still rolls
                    {
                        lockScoringButtons();
                        if (theGame.getRolls() > 0)
                        {
                            rollButton.setDisable( false );
                        }
                    }
                }
            }
        }

    }

    /**
     * Class that handles ActionEvents for setting a new game or quitting
     */
    class GameButtonHandler implements EventHandler<ActionEvent>
    {
        /**
         * Method that handles ActionEvents for the new game and quit buttons
         * @param event ActionEvent: Event caused by clicking the new game and quit buttons
         */
        @Override
        public void handle( ActionEvent event )
        {
            String message;

            if (event.getSource() == newGameButton)      // user chooses new game
            {
                message = "Do you want to restart the game?";

                Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
                alert.setTitle( "Restart" );
                alert.setContentText( message );
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                {
                    resetGame();
                }
            }
            else if (event.getSource() == quitButton)   // user chooses to quit
            {
                message = "Do you want to quit the game?";

                Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
                alert.setTitle( "Quit?" );
                alert.setContentText( message );
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                {
                    System.exit( 0 );
                }
            }
        }
    }

    /**
     * Class that handles ActionEvents by clicking on the information radiobuttons
     */
    class RadioHandler implements EventHandler<ActionEvent>
    {
        /**
         * Method that handles the ActionEvents of clicking on the information radiobuttons
         * @param event ActionEvent: event created by clicking on a radiobutton
         */
        @Override
        public void handle( ActionEvent event )
        {
            // set information in textarea based on user choice
            if (event.getSource() == radioGamePlay)
            {
                gameInstructions.setText( gameInstruct );

            } else if (event.getSource() == radioScoreExplanation)
            {
                gameInstructions.setText( scoringInstruct );


            } else if (event.getSource() == radioScoreValue)
            {
                gameInstructions.setText( scoringValues );
            }
        }
    }
}