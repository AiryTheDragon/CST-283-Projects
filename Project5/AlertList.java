/************************************************************************************************
 *  This class stores an array of Alerts with county FIPS code, start time, end time and warning code
 *
 *  The method allows the the alerts to be sorted based on priority and populations
 *  The method allows the display of all the alerts by saving them as a String
 *
 *  CST 283 Programming Assignment 5
 *  @author Michael Clinesmith
 ***********************************************************************************************/
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.StringTokenizer;

public class AlertList
{
    private static final int ARRAY_LIMIT = 1000;
    private Alerts[] alertsArray;
    private int numOfElements;
    private static CountyList countyList = null; // allows access to countyList for all elements of array

    /**
     * No-argument constructor
     */
    public AlertList()
    {
        alertsArray = new Alerts[ARRAY_LIMIT];
        numOfElements = 0;
    }

    /**
     * Constructor that creates Alerts from data located in the file filename
     *
     * THE USER MAY CHOOSE TO END THE PROGRAM IN THIS METHOD IF THERE ARE PROBLEMS LOADING THE DATA
     *
     * @param filename String: The file that holds Alerts data
     */
    public AlertList(String filename)
    {
        alertsArray = new Alerts[ARRAY_LIMIT];

        File alertData;
        String message;
        Scanner inputFile;
        StringTokenizer alerttokens;
        int i=0;

        String inputLine;


        String fips, startDateTime, endDateTime, code;                     // Work variables

        // Build list of county objects
        alertData = new File( filename );

        if (!alertData.exists())  // file not found
        {
            message = "The file " + filename + " containing alert data was not found.\n" +
                "Do you wish to end the program?";

            quitOption( message );  // give user option to end program
            numOfElements = 0;
        }
        else
        {

            try
            {
                inputFile = new Scanner( alertData );

                // Read input file while more data exist
                // Read one line at a time (assuming each line contains one username)
                i = 0;          // used to work through array elements
                while (inputFile.hasNext() && i<ARRAY_LIMIT)
                {

                    inputLine = inputFile.nextLine();
                    alerttokens = new StringTokenizer( inputLine, "," );

                    try
                    {
                        fips = alerttokens.nextToken();
                        startDateTime = alerttokens.nextToken();
                        endDateTime = alerttokens.nextToken();
                        code = alerttokens.nextToken();

                        alertsArray[i] = new Alerts( fips, startDateTime, endDateTime, code );
                        i++;
                    } catch (NoSuchElementException ex)
                    {
                        message = "There was an error processing an alert record in " + filename + ".\n" +
                                "Do you wish to end the program?  If not, the record will be skipped.";

                        quitOption( message );
                    }

                }
                numOfElements = i;    // Capture number of elements
                inputFile.close();
                message = "The alert data from the file " + filename +
                        "\nis now uploaded into memory.";

                Alert alert = new Alert( Alert.AlertType.INFORMATION );
                alert.setTitle( "DATA LOADED" );
                alert.setContentText( message );
                alert.showAndWait();

                if (i==ARRAY_LIMIT)
                {
                    message = "The maximum number of alerts is stored into memory. " +
                            "\nSome records may not have been uploaded." +
                            "\nDo you wish to end the program?";
                    quitOption( message );  // give user option to end the program
                }

            }
            catch(IOException e)  // if error loading data, give error message and end program
            {
                message = "There was an error processing the file " + filename + ".\n" +
                        "No alerts were loaded." +
                        "\nDo you wish to end the program?";

                quitOption( message );      // give user option to end the program
            }
        }
        return;
    }

    /**
     * Method that allows an Alerts to be added to the alertsArray list
     * @param newAlert Alerts: Alerts object to be added to the alertsArray list
     * @return boolean: true if the object was added, false if not
     */
    public boolean addAlert(Alerts newAlert)
    {
        boolean isAdded = false;
        if (numOfElements<ARRAY_LIMIT)
        {
            isAdded = true;
            alertsArray[numOfElements]= new Alerts(newAlert);
            numOfElements++;
        }
        return isAdded;
    }


    /**
     * Method that returns all the alerts that have been stored into memory to a String
     * @return String: A readable String containing the contents of all the stored warnings in memory
     */
    public static String getPossibleAlertsString()
    {
        return Alerts.possibleAlertsString();
    }

    /**
     * Method that returns the countyList reference stored in this object
     * NOTE - THIS METHOD ONLY RETURNS THE MEMORY REFERENCE, IT DOES NOT MAKE A DEEP COPY OF THE OBJECT
     * @return
     */
    public static CountyList getCountyList()
    {
        return countyList;
    }

    /**
     * Method saves the address of the CountyList inside the Class and also sets it for the Alerts class
     * This method only saves the memory address, it DOES NOT make a deep copy of the list
     * @param list CountyList: the list of County data to allow access from Alerts Class
     */
    public static void setCountyList(CountyList list)
    {
        countyList = list;
        Alerts.setCountyList( list );
    }

    /**
     * Method that uses a bubble sort to sort the contents in the Alert list
     * The Alerts are sorted primarily by Alert Type, secondarily by population
     */
    public void sortAlerts()
    {
        boolean isDone = false;     // used to end unnecessary loops - if no changes made during loop, exit
        Alerts temp;

        // bubble sort
        for (int i=numOfElements-2; i>=0; i--) // loop sorts one less element each time
        {
            isDone = true;
            for (int j=0; j<=i; j++)
            {
                // if element at j has lower priority than element at j+1 - swap elements
                if (alertsArray[j].compareTo( alertsArray[j+1] )<0)
                {
                    isDone = false;
                    temp = alertsArray[j];
                    alertsArray[j] = alertsArray[j+1];
                    alertsArray[j+1] = temp;
                }
            }
        }

        return;
    }

    /**
     * Method that puts the all the alerts together as a String and displays them
     * @return  String: A concatenation of all the alert strings stored in the AlertList object
     */
    public String displayAllAlerts()
    {
        String str = "No alerts on record.";

        if (numOfElements>0)
        {
            str = "";
            for (int i=0; i<numOfElements; i++)
            {
                str += alertsArray[i].toString() + "\n\n";
            }
        }

        return str;
    }

    /**
     * Method that asks if the user wants to quit because of an error in processing
     * @param message String: The question to ask the user
     *
     * THIS METHOD WILL END THE PROGRAM IF THE USER SELECTS OK
     */
    private void quitOption(String message)
    {
        Alert alert = new Alert( Alert.AlertType.CONFIRMATION );
        alert.setTitle( "Quit?" );
        alert.setContentText( message );
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            System.exit( 0 );
        }
        return;
    }



}
