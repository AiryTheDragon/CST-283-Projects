/************************************************************************************************
 *  This class contains the main driver and interface for viewing character data
 *
 *  The user can load and save the data into files
 *  The user can add, modify and delete records
 *  The user can sort any of the record fields ascending or descending and randomize them
 *
 *  CST 283 Programming Assignment 3
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class CharacterListInterface extends Application
{
    // main node
    private BorderPane mainLayout;

    // holds data
    private CharacterData characterData;
    private String currentFileName="";

    // titlebox objects
    private BorderPane TitleBar;
    private Image DnDimage;
    private ImageView DnDimageView;
    private Button quitButton;
    private Label appLabel;
    private HBox quitButtonHBox;

    // Load/Save objects
    private VBox loadFileButtonVBox;
    private Button loadFileButton, saveFileButton;
    private File selectedFile;

    // Data sorting objects
    private Button addElementButton, sortButton, updateElementButton;

    private HBox sortHBox;

    // Data retreive record object
    private ComboBox<String> retreiveKeyListComboBox;
    private Label retreiveKeyLabel;
    private VBox retreiveKeyVBox;
    private Button retreiveBackButton;

    // Sorting objects
    private ComboBox<String> sortingFieldList;
    private RadioButton sortingAscending, sortingDescending, sortingRandom;
    private ToggleGroup sortingRadioToggle;
    private Label sortingLabel;
    private Button sortingButton;
    private VBox sortingContainer;

    // Data single record editing objects
    private GridPane oneRecordGridPane;
    private Label[] recordLabels=new Label[CharacterRecord.getNumOfFields()];
    private TextField[] recordValues=new TextField[CharacterRecord.getNumOfFields()];
    private Button saveRecordButton, exitRecordButton, deleteRecordButton;

    // Data Display objects
    private TextArea characterDataDisplay;
    private HBox centerHBox;

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
     * @param primaryStage Stage object used to create the stage
     */
    @Override
    public void start( Stage primaryStage )
    {
        initializeScene(primaryStage);

        // Set up overall scene
        Scene scene = new Scene( mainLayout, 1100, 900 );
        scene.getStylesheets().add( "DaD.css" );
        primaryStage.setScene( scene );
        primaryStage.setTitle( "Dungeons & Dragons Character Data Viewer" );
        primaryStage.show();
    }

    /**
     * Method that calls other methods to create the interface, then combines the parts in the
     *      mainLayout object
     * @param primaryStage Stage object used to create the stage
     */
    public void initializeScene(Stage primaryStage)
    {
        characterData = new CharacterData(  );

        initializeTitleBar();
        initializeLoadButton(primaryStage);
        initializeDataButtons();
        initializeDataTextArea();
        initializeSingleRecordArea();
        initializeRetrieveRecordObjects();
        initializeSortingObjects();

        mainLayout = new BorderPane();
        mainLayout.setTop( TitleBar );
        mainLayout.setLeft(loadFileButtonVBox);
        mainLayout.setCenter( centerHBox );
        mainLayout.setBottom( sortHBox );
    }

    /**
     * Method that creates the title bar that contains an image, title, and quit button
     */
    public void initializeTitleBar()
    {
        DnDimage=new Image("file:dndlogo.jpg");
        DnDimageView= new ImageView( DnDimage );

        quitButton = new Button( "QUIT" );
        quitButton.setOnAction( new AppButtonHandler() );
        quitButton.setAlignment( Pos.BASELINE_RIGHT );
        quitButton.setPadding( new Insets( 20 ) );

        quitButtonHBox = new HBox(quitButton);
        quitButtonHBox.setPadding(new Insets( 20 ));

        appLabel = new Label("Dungeons and Dragons Character Data Sorter");
        appLabel.setAlignment( Pos.CENTER );
        appLabel.setStyle( "-fx-font-size: 28; -fx-text-fill:  orange" );

        TitleBar = new BorderPane();
        TitleBar.setLeft(DnDimageView);
        TitleBar.setRight(quitButtonHBox);
        TitleBar.setCenter(appLabel);
    }

    /**
     * Method that creates the Load and Save buttons
     * The actions of the load and save buttons are implemented in this method
     *
     * @param primaryStage
     */
    public void initializeLoadButton(Stage primaryStage)
    {
        FileChooser fileChooser = new FileChooser();

        loadFileButton = new Button("Load File");
        // code to load file
        loadFileButton.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile!=null)
            {
                String message = "Do you want to load this file?\n" +
                        "This will erase any data already in the application?";
                Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
                alert.setTitle( "Load File?" );
                alert.setContentText( message );
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                {
                    characterData = new CharacterData( selectedFile.getName() );
                    currentFileName = selectedFile.getName();
                    characterDataDisplay.setText( characterData.toStringFormatted() );
                }
            }
        });
        loadFileButton.setPadding( new Insets( 20 ) );

        saveFileButton = new Button("Save File");
        // code to save file
        saveFileButton.setOnAction(e -> {
            selectedFile = fileChooser.showSaveDialog(primaryStage);
            if(selectedFile!=null)
            {
                String message = "Do you want to save this file?\n" +
                        "This will overwrite the data in the file.";
                Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
                alert.setTitle( "Save File?" );
                alert.setContentText( message );
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                {
                    try
                    {
                        PrintWriter outputFile = new PrintWriter(selectedFile.getName());
                        System.out.println( characterData.toString() );
                        System.out.println( characterData.toStringFormatted() );
                        // data gotten from saveDataString method and saves the filename in CharacterData
                        outputFile.println( characterData.saveDataString( selectedFile.getName() ) );
                        outputFile.close();
                        message = "File " + selectedFile.getName() +" saved.";
                        alert = new Alert( Alert.AlertType.INFORMATION );
                        alert.setTitle( "File Saved" );
                        alert.setContentText( message );
                        alert.showAndWait();

                    }
                    catch (IOException ex)
                    {
                        message = "There were problems with saving the data.\n" +
                                "The data was not saved.";
                        alert = new Alert( Alert.AlertType.ERROR );
                        alert.setTitle( "IOException Error" );
                        alert.setContentText( message );
                        alert.showAndWait();

                    }
                }
            }
        });
        saveFileButton.setPadding( new Insets( 20 ) );

        loadFileButtonVBox = new VBox(20, loadFileButton, saveFileButton);
        loadFileButtonVBox.setPadding( new Insets( 20 ) );
    }

    /**
     * Method that creates the text area data display
     */
    public void initializeDataTextArea()
    {
        characterDataDisplay = new TextArea(  );
        characterDataDisplay.setPrefColumnCount( 100 );
        centerHBox = new HBox( characterDataDisplay );
        centerHBox.setPadding( new Insets( 20 ) );
    }

    /**
     * Method that creates the buttons at the bottom of the interface to regulate data operations
     */
    public void initializeDataButtons()
    {
        //private RadioButton ascending, descending;
        //private ToggleGroup radioToggle;

        addElementButton = new Button( "Add Record" );
        addElementButton.setOnAction( new AppButtonHandler() );
        addElementButton.setAlignment( Pos.CENTER );
        addElementButton.setPadding( new Insets( 20 ) );

        sortButton = new Button( "Sort Records" );
        sortButton.setOnAction( new AppButtonHandler() );
        sortButton.setAlignment( Pos.CENTER );
        sortButton.setPadding( new Insets( 20 ) );

        updateElementButton = new Button( "Update/Delete Record" );
        updateElementButton.setOnAction( new AppButtonHandler() );
        updateElementButton.setAlignment( Pos.CENTER );
        updateElementButton.setPadding( new Insets( 20 ) );

        sortHBox = new HBox( 20, addElementButton, updateElementButton, sortButton);
        sortHBox.setPadding( new Insets( 20 ) );
        sortHBox.setAlignment( Pos.CENTER );
    }

    /**
     * Method that creates an interface for interacting with a single data object
     */
    public void initializeSingleRecordArea()
    {
        int halfRecords= CharacterRecord.getNumOfFields()/2;

        oneRecordGridPane = new GridPane();

        // create two columns of fields for records
        for (int i=0; i<halfRecords; i++)
        {
            recordLabels[i] = new Label(CharacterRecord.getFieldLabel( i ));
            recordValues[i] = new TextField();
            recordValues[i].setPadding( new Insets( 10 ) );
            oneRecordGridPane.add(recordLabels[i], 0, i+1);
            oneRecordGridPane.add(recordValues[i], 1, i+1);

        }
        for (int i=halfRecords; i<CharacterRecord.getNumOfFields(); i++)
        {
            recordLabels[i] = new Label(CharacterRecord.getFieldLabel( i ));
            recordValues[i] = new TextField();
            recordValues[i].setPadding( new Insets( 10 ) );
            oneRecordGridPane.add(recordLabels[i], 3, i-halfRecords+1);
            oneRecordGridPane.add(recordValues[i], 4, i-halfRecords+1);

        }

        recordValues[0].setDisable( true ); //disable key value from being directly edited

        //create single record buttons
        saveRecordButton = new Button( "Save Record" );
        saveRecordButton.setOnAction( new RecordButtonHandler() );
        saveRecordButton.setAlignment( Pos.CENTER );
        saveRecordButton.setPadding( new Insets( 10 ) );

        deleteRecordButton = new Button( "Delete Record" );
        deleteRecordButton.setOnAction( new RecordButtonHandler() );
        deleteRecordButton.setAlignment( Pos.CENTER );
        deleteRecordButton.setPadding( new Insets( 10 ) );

        exitRecordButton = new Button( "Exit Record" );
        exitRecordButton.setOnAction( new RecordButtonHandler() );
        exitRecordButton.setAlignment( Pos.CENTER );
        exitRecordButton.setPadding( new Insets( 10 ) );

        // add buttons to GridPane
        oneRecordGridPane.add(saveRecordButton, 1, halfRecords+2  );
        oneRecordGridPane.add (deleteRecordButton, 2, halfRecords+2);
        oneRecordGridPane.add(exitRecordButton, 4, halfRecords+2);

    }

    /**
     * Method that creates the interface to selecting a key for a record to view
     */
    public void initializeRetrieveRecordObjects()
    {
        retreiveKeyLabel = new Label("Select record key");
        retreiveKeyListComboBox = new ComboBox<>();
        retreiveKeyListComboBox.setOnAction( new RecordButtonHandler() );

        retreiveBackButton = new Button("Back");
        retreiveBackButton.setOnAction( new RecordButtonHandler() );
        retreiveBackButton.setAlignment( Pos.CENTER );
        retreiveBackButton.setPadding( new Insets( 10 ) );

        retreiveKeyVBox = new VBox(20, retreiveKeyLabel, retreiveKeyListComboBox, retreiveBackButton);
    }

    /**
     * Method that creates the interface to selecting a type of sort for the records
     */
    public void initializeSortingObjects()
    {
        sortingLabel = new Label("Select the type and field to sort");

        sortingFieldList = new ComboBox<String>( );
        sortingFieldList.setPadding( new Insets( 20 ) );
        for (int i=0; i<CharacterRecord.getNumOfFields(); i++)
        {
            sortingFieldList.getItems().add( CharacterRecord.getFieldLabel( i ) );
        }

        sortingRadioToggle = new ToggleGroup();

        sortingAscending = new RadioButton( "Ascending" );
        sortingAscending.setToggleGroup( sortingRadioToggle );
        sortingDescending = new RadioButton( "Descending" );
        sortingDescending.setToggleGroup( sortingRadioToggle );
        sortingRandom = new RadioButton( "Random" );
        sortingRandom.setToggleGroup( sortingRadioToggle );
        sortingRandom.setSelected( true );

        sortingButton = new Button( "Sort!" );
        sortingButton.setOnAction( new RecordButtonHandler() );
        sortingButton.setAlignment( Pos.CENTER );
        sortingButton.setPadding( new Insets( 20 ) );

        sortingContainer = new VBox( 20 , sortingLabel, sortingFieldList, sortingAscending, sortingDescending,
                sortingRandom, sortingButton);

    }

    /**
     * Method that resets the interface so it shows the CharacterData in the center display
     */
    public void restoreCharacterDisplay()
    {
        centerHBox.getChildren().clear();
        centerHBox.getChildren().add( characterDataDisplay );
        characterDataDisplay.setText( characterData.toStringFormatted() );

        // enable buttons when leaving one record menu
        sortHBox.setDisable( false );
        loadFileButtonVBox.setDisable( false );

    }

    /**
     * Method that locks the action buttons besides those in the center display
     */
    public void lockButtons()
    {
        sortHBox.setDisable( true );
        loadFileButtonVBox.setDisable( true );
    }

    /**
     * Class that handles ActionEvents for setting a new game or quitting
     */
    class AppButtonHandler implements EventHandler<ActionEvent>
    {
        /**
         * Method that handles ActionEvents for the new game and quit buttons
         * @param event ActionEvent: Event caused by clicking the new game and quit buttons
         */
        @Override
        public void handle( ActionEvent event )
        {
            String message;

            if (event.getSource() == quitButton)   // user chooses to quit
            {
                message = "Do you want to quit the application?";

                Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
                alert.setTitle( "Quit?" );
                alert.setContentText( message );
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                {
                    System.exit( 0 );
                }
            }
            if (event.getSource()==addElementButton)
            {
                CharacterRecord newRecord = new CharacterRecord( );

                // confirm new key is not in characterData, otherwise try again
                while(characterData.isUsedKey(newRecord.getKey()))
                {
                    newRecord = new CharacterRecord(  );
                }

                // set up interface to show one record panel
                centerHBox.getChildren().clear();
                centerHBox.getChildren().add( oneRecordGridPane );
                // disable buttons when in other menu
                lockButtons();

                recordValues[0].setText( newRecord.getFieldValue( 0 ) ); // place key value in field
            }
            if (event.getSource()==updateElementButton)
            {
                centerHBox.getChildren().clear();
                centerHBox.getChildren().add( retreiveKeyVBox );

                // disable buttons when in other menu
                lockButtons();

                retreiveKeyListComboBox.getItems().clear();
                characterData.updateComboBox( retreiveKeyListComboBox);
            }
            if (event.getSource()==sortButton)
            {
                centerHBox.getChildren().clear();
                centerHBox.getChildren().add( sortingContainer );

                // disable buttons when in other menu
                lockButtons();
            }
        }
    }

    /**
     * Class that handles ActionEvents for setting a new game or quitting
     */
    class RecordButtonHandler implements EventHandler<ActionEvent>
    {
        /**
         * Method that handles ActionEvents for the new game and quit buttons
         *
         * @param event ActionEvent: Event caused by clicking the new game and quit buttons
         */
        @Override
        public void handle( ActionEvent event )
        {
            String message;

            if (event.getSource() == exitRecordButton)   // user chooses to exit the record screen
            {
                message = "Do you want to go back to the main screen without editing the record?";

                Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
                alert.setTitle( "Exit Record" );
                alert.setContentText( message );
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                {
                    restoreCharacterDisplay();
                }
            }
            else if (event.getSource() == retreiveBackButton)   // user chooses to exit the record screen
            {
                    restoreCharacterDisplay();
            }
            else if(event.getSource()==saveRecordButton)
            {
                message = "Do you want to save this record?";

                Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
                alert.setTitle( "Save Record" );
                alert.setContentText( message );
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                {
                    CharacterRecord rec = new CharacterRecord( recordValues[0].getText(), true );

                    // put textfield data in record then clear them
                    for(int i=1; i<CharacterRecord.getNumOfFields(); i++)
                    {
                        rec.setFieldValue( i,recordValues[i].getText());
                        recordValues[i].setText("");
                    }
                    if (characterData.isUsedKey( rec.getKey() ))    // key already in characterData
                    {
                        characterData.updateRecord( rec );
                    }
                    else                                            // key not in characterData
                    {
                        characterData.addRecord( rec );
                    }

                    restoreCharacterDisplay();
                }
            }
            else if(event.getSource()==deleteRecordButton)
            {
                message = "Are you sure you want to delete the record with key " + recordValues[0].getText() +"?";

                Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
                alert.setTitle( "Delete Record" );
                alert.setContentText( message );
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK)
                {
                    characterData.deleteRecord( recordValues[0].getText() );

                    restoreCharacterDisplay();
                }

            }
            else if(event.getSource()==retreiveKeyListComboBox)
            {
                String key = retreiveKeyListComboBox.getValue();
                CharacterRecord rec = characterData.getRecordFromKey( key );

                // set up interface to show one record panel
                centerHBox.getChildren().clear();
                centerHBox.getChildren().add( oneRecordGridPane );
                // disable buttons when in other menu
                sortHBox.setDisable( true );
                loadFileButtonVBox.setDisable( true );

                // put record values into textfields
                for(int i=0; i<CharacterRecord.getNumOfFields(); i++)
                {
                    recordValues[i].setText( rec.getFieldValue( i ) );
                }
            }
            if (event.getSource()==sortingButton)
            {
                String key=sortingFieldList.getValue();

                if(sortingAscending.isSelected())
                {
                    characterData.sortRecords( key, true );
                }
                else if(sortingDescending.isSelected())
                {
                    characterData.sortRecords( key, false );
                }
                else if(sortingRandom.isSelected())
                {
                    characterData.sortRandom();
                }

                restoreCharacterDisplay();

            }
        }
    }
}
