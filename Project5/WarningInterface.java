/************************************************************************************************
 *  This class contains the main driver and interface for viewing warning alerts data
 *
 *  The application initially displays the different definitions of alerts
 *
 *  The user has the option of loading from the default alerts.txt file and displaying those alerts
 *  or choosing a specific file to display those alerts
 *  or displaying the alert definitions
 *
 *  The alerts are sorted by level of security warning, then warnings, watches, and advisories
 *  In those levels, the alerts are sorted by population
 *
 *  CST 283 Programming Assignment 5
 *  - File heavily modified from CharacterListInterface.java from CST 283 PA 3
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.util.Optional;

public class WarningInterface extends Application
{
    // main node
    private BorderPane mainLayout;

    // holds data
    private CountyList countyList;
    private String currentFileName="";
    private AlertList alertsList;

    // data files
    private String COUNTY_FIPS_FILE = "fipsCounty.txt";
    private String COUNTY_POP_FILE = "popCounty.txt";
    private String WARNING_SPEC_FILE = "warningList.txt";
    private String ALERT_DEF_FILE = "alerts.txt";

    // titlebox objects
    private BorderPane TitleBar;
    private Image Airyimage;
    private ImageView AiryimageView;
    private Button quitButton;
    private Label appLabel;
    private HBox quitButtonHBox;

    // Load/Save objects
    private VBox loadFileButtonVBox;
    private Button loadFileButton, loadAlertsTxtButton, warningsButton;
    private File selectedFile;

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
        scene.getStylesheets().add( "Warning.css" );
        primaryStage.setScene( scene );
        primaryStage.setTitle( "Security and Weather Alert Program" );
        primaryStage.show();
    }

    /**
     * Method that calls other methods to create the interface, then combines the parts in the
     *      mainLayout object
     * @param primaryStage Stage object used to create the stage
     */
    public void initializeScene(Stage primaryStage)
    {

        alertsList = new AlertList( ALERT_DEF_FILE );
        countyList = new CountyList( COUNTY_FIPS_FILE, COUNTY_POP_FILE );
        alertsList.setCountyList( countyList );

        initializeTitleBar();
        initializeLoadButton(primaryStage);
        initializeDataTextArea();

        mainLayout = new BorderPane();
        mainLayout.setTop( TitleBar );
        mainLayout.setLeft(loadFileButtonVBox);
        mainLayout.setCenter( centerHBox );
    }

    /**
     * Method that creates the title bar that contains an image, title, and quit button
     */
    public void initializeTitleBar()
    {
        Airyimage=new Image("file:AiryJavaJXDrawing.png");
        AiryimageView= new ImageView( Airyimage );

        quitButton = new Button( "QUIT" );
        quitButton.setOnAction( new AppButtonHandler() );
        quitButton.setAlignment( Pos.BASELINE_RIGHT );
        quitButton.setPadding( new Insets( 20 ) );

        quitButtonHBox = new HBox(quitButton);
        quitButtonHBox.setPadding(new Insets( 20 ));

        appLabel = new Label("Security and Weather Alert System");
        appLabel.setAlignment( Pos.CENTER );
        appLabel.setStyle( "-fx-font-size: 28; -fx-text-fill:  orange" );

        TitleBar = new BorderPane();
        TitleBar.setLeft(AiryimageView);
        TitleBar.setRight(quitButtonHBox);
        TitleBar.setCenter(appLabel);
    }

    /**
     * Method that creates the Load buttons and warning button
     * The actions of the buttons are implemented in this method
     *
     * @param primaryStage Stage: The Stage used to allow access to the file chooser
     */
    public void initializeLoadButton(Stage primaryStage)
    {
        FileChooser fileChooser = new FileChooser();

        loadFileButton = new Button("Load alerts data file");
        // code to load file
        loadFileButton.setOnAction(e -> {
            selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile!=null)
            {
                String message = "Do you want to load the file " + selectedFile.getName() + "?\n" +
                        "This will remove the alerts shown in the application?";
                Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
                alert.setTitle( "Load File?" );
                alert.setContentText( message );
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK)
                {
                    alertsList = new AlertList( selectedFile.getName() );
                    alertsList.sortAlerts();
                    currentFileName = selectedFile.getName();
                    characterDataDisplay.setText( alertsList.displayAllAlerts() );                }

            }
        });
        loadFileButton.setPadding( new Insets( 20 ) );

        loadAlertsTxtButton = new Button("Load alerts.txt");
        loadAlertsTxtButton.setPadding( new Insets( 20 ) );

        // code to load alerts.txt
        loadAlertsTxtButton.setOnAction(e -> {

            String message = "Do you want to load the file " + ALERT_DEF_FILE + "?\n" +
                "This will remove the alerts shown in the application.";

            Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
            alert.setTitle( "Load File?" );
            alert.setContentText( message );
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK)
            {
                alertsList = new AlertList( ALERT_DEF_FILE );
                alertsList.sortAlerts();
                characterDataDisplay.setText( alertsList.displayAllAlerts() );
                currentFileName = ALERT_DEF_FILE;
            }

        });

        characterDataDisplay = new TextArea( alertsList.getPossibleAlertsString()); // initially set to display warnings

        warningsButton = new Button("Display warnings");
        warningsButton.setPadding( new Insets( 20 ) );

        // code to display warnings
        warningsButton.setOnAction(e -> {

            String message = "Do you want to show warning definitions?" +
                    "This will remove the alerts shown in the application.";

            Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
            alert.setTitle( "View Warning Definitions" );
            alert.setContentText( message );
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK)
            {
                characterDataDisplay.setText( alertsList.getPossibleAlertsString() );
            }

        });

        loadFileButtonVBox = new VBox(20, loadAlertsTxtButton, loadFileButton, warningsButton );
        loadFileButtonVBox.setPadding( new Insets( 20 ) );
    }

    /**
     * Method that creates the text area data display
     */
    public void initializeDataTextArea()
    {
        characterDataDisplay = new TextArea( alertsList.getPossibleAlertsString());
        characterDataDisplay.setPrefColumnCount( 100 );
        centerHBox = new HBox( characterDataDisplay );
        centerHBox.setPadding( new Insets( 20 ) );
    }

    /**
     * Class that handles ActionEvents for quit button
     */
    class AppButtonHandler implements EventHandler<ActionEvent>
    {
        /**
         * Method that handles ActionEvents for the quit button
         * @param event ActionEvent: Event caused by clicking the quit button
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
        }
    }
}
