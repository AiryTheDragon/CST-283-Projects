/************************************************************************************************
 *  This class contains the main driver of the program, providing a basic interface for the user
 *  to request earthquake data.
 *
 *  The program loads data from the filename stored in QUAKE_FILENAME, then allows the user to
 *  request data in three different formats.
 *
 *  The user has the option to display the data to the console, or choose a filename to save the data to.
 *
 *  It does basic error checking on the data, requiring the data to be in range and maxes to be greater
 *  than mins.
 *
 *  The program will loop until the user indicates that no more requests are desired.
 *
 *  CST 283 Programming Assignment 1
 *  @author Michael Clinesmith
 ***********************************************************************************************/

import javax.swing.JOptionPane;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.*;
import java.util.StringTokenizer;

public class QuakeMain
{
    private final static String QUAKE_FILENAME = "quakes.txt";
    private final static String QUAKE_REGULAR_OUTPUT_FILENAME = "quakesFormatted.txt";
    private final static String QUAKE_MOD_OUTPUT_FILENAME = "quakesModFormatted.txt";
    private static QuakeData quakeData;

    /**
     * Method is the main method of the program
     * It calls methods to create the quake data objects, display info messages, and run the main program
     * @param args String[]: Not used
     */
    public static void main( String args[] )
    {

        quakeData = importQuakeData( QUAKE_FILENAME );

        displayIntroMessage();
        mainProgram();
        displayEndingMessage();

    }

    /**
     * Method that directs the program to upload quake records to the computer memory
     * @param filename String: File name that contains the quake data
     * @return QuakeData: A QuakeData object that contains the quake data that was saved in filename
     */
    public static QuakeData importQuakeData( String filename )
    {
        String messageStr = "";
        QuakeData data = new QuakeData( filename );

        messageStr = filename + " opened and uploaded.\n" +
                data.getNumOfRecords() + " records uploaded.\n" +
                data.getNumOfRecordsMissed() + " records not uploaded."
        ;

        JOptionPane.showMessageDialog( null, messageStr );      // messages uses that data uploaded
        return data;
    }

    /**
     * Method that prints an introductory message
     */
    public static void displayIntroMessage()
    {
        String message =    "Welcome to the quake data processing program!\n" +
                            "Programmed by Michael Clinesmith\n\n" +
                            "Enter requests in the following forms:\n" +
                            "R, minLat, maxLat, minLon, maxLon\n" +
                            "D, minDate, maxDate\n" +
                            "M, minMag\n" +
                            "Enter HELP for more information on the commands.";

        System.out.println( message );

    }

    /**
     * Method that prints a help message explaining the commands allowed in the program
     */
    public static void displayHelpMessage()
    {
        String message =    "Console commands:\n" +
                            "R, minLat, maxLat, minLon, maxLon\n" +
                            "List all quakes in the dataset by region with north/south boundary between\n" +
                            "minLat and maxLat, and east/west boundary between minLon and maxLon\n" +
                            "Latitudes are between -90.0 amd 90.0 degrees and Longitudes between -180.0 and 180.0 degrees.\n\n" +
                            "D, minDate, maxDate\n" +
                            "List all quakes in the dataset by date with the calendar date of the quake from\n" +
                            "minDate to maxDate.  Dates are to be in the format of YYYY-MM-DD.\n\n" +
                            "M, minMag\n" +
                            "List all quakes in the dataset with magnitude minMag or greater\n" +
                            "Magnitudes need to be between 4.0 and 10.0.\n\n" +
                            "HELP\n" +
                            "Displays this help message";

        System.out.println( message );
    }

    /**
     * Method that prints an ending message to the computer console
     */
    public static void displayEndingMessage()
    {
        String message =    "Thank you for using the quake data processing program!";

        System.out.println( message );

    }

    /**
     * Method that handles the looping of the information requests of the program
     */
    public static void mainProgram()
    {
        String message = "Enter a quake data request or type HELP for more information:";
        boolean again = true;

        while (again)   // handles one complete user request each iteration
        {
            System.out.println( message );
            again = handleUserRequest();
        }
    }

    /**
     * Method that handles one information request from the user
     * It prompts for the user request then sends it to the appropriate method for processing
     * When completed, it prompts the user if another request is desired
     *
     * @return boolean: true if the user can make another info request, false if the user chooses to quit
     */
    public static boolean handleUserRequest()
    {
        boolean anotherRequest = true;
        String message, input;
        char requestType = ' ';
        Scanner keyboard = new Scanner(System.in);
        input = keyboard.nextLine();
        input = input.toUpperCase();                // converts string to upper case to ease processing

        if (input.length()>0)
        {
            requestType = input.charAt( 0 );
        }

        // handle request
        switch (requestType)
        {
            case 'R':
                processRRequest(input);
                break;
            case 'D':
                processDRequest(input);
                break;
            case 'M':
                processMRequest(input);
                break;
            case 'H':
                displayHelpMessage();
                break;
            default:
                System.out.println( "Request not understood or in the proper format.\nType HELP to view the proper format\n" );
        }

        message = "Do you want to process another request?";
        System.out.println( message );

        input = keyboard.nextLine();
        if ( input.length()>0 && (input.charAt( 0 ) == 'N' || input.charAt( 0 ) == 'n'))
        {
            anotherRequest = false;

        }
        return anotherRequest;
    }

    /**
     * Method that handles the processing quake data by region
     * It prompts the user for a filename if the user wishes to save to a file then calls a method to process the request
     * @param str String: Input string received from the user for the region quake data search
     */
    public static void processRRequest(String str)
    {
        double minLat=0.0, maxLat=0.0, minLon=0.0, maxLon=0.0;
        String message = "";
        boolean validRequest = true;
        String fileName = "";                    // filename to use to store data if requested

        StringTokenizer request = new StringTokenizer( str , ",");

        try                         // used to catch processing errors
        {

            request.nextToken();    // skips the first token - The R
            minLat = Double.parseDouble(  request.nextToken());
            maxLat = Double.parseDouble(  request.nextToken());
            minLon = Double.parseDouble(  request.nextToken());
            maxLon = Double.parseDouble(  request.nextToken());

        }
        catch (NumberFormatException | NoSuchElementException e)
        {
            message =   "The attempt to search for records was not in the proper format, the correct format is:\n" +
                        "R, minLat, maxLat, minLon, maxLon\n";
            System.out.println( message );
            validRequest = false;
        }

        if (validRequest)
        {
            fileName = filenameRequest();                   // check if user wishes to save to a file

            if (fileName.isEmpty())                        // user did not enter filename - display to console
            {
                quakeData.regionSearch( minLat, maxLat, minLon, maxLon );
            }
            else
            {
                quakeData.regionSearch( minLat, maxLat, minLon, maxLon, fileName );
            }
        }

    }

    /**
     * Method that handles the processing quake data by date
     * It prompts the user for a filename if the user wishes to save to a file then calls a method to process the request
     * @param str String: Input string received from the user for the date quake data search
     */
    public static void processDRequest(String str)
    {
        Dates minDate = new Dates();
        Dates maxDate = new Dates();
        String message = "";
        boolean validRequest = true;
        String fileName = "";                    // filename to use to store data if requested

        StringTokenizer request = new StringTokenizer( str , ",");

        try                         // used to catch processing errors
        {
            // extracts the two dates in string format and sends them to the Dates constructor to create
            // Dates objects
            request.nextToken();    // skips the first token - The D
            minDate = new Dates(request.nextToken());
            maxDate = new Dates(request.nextToken());
        }
        catch (NumberFormatException | NoSuchElementException e)
        {
            message =   "The attempt to search for records was not in the proper format, the correct format is:\n" +
                        "D, minDate, maxDate\n";
            System.out.println( message );
            validRequest = false;
        }

        if (validRequest)
        {
            fileName = filenameRequest();                  // check if user wishes to save to a file

            if (fileName.isEmpty())                        // user did not enter filename - display to console
            {
                quakeData.dateSearch( minDate, maxDate );
            }
            else
            {
                quakeData.dateSearch( minDate, maxDate, fileName );
            }
        }

    }

    /**
     * Method that handles the processing quake data by magnitude
     * It prompts the user for a filename if the user wishes to save to a file then calls a method to process the request
     * @param str String: Input string received from the user for the magnitude quake data search
     */
    public static void processMRequest(String str)
    {
        double magnitude = 0.0;
        String message = "";
        boolean validRequest = true;
        String fileName = "";                    // filename to use to store data if requested

        try                         // used to catch processing errors
        {
            magnitude = Double.parseDouble( str.substring( 2 ) );       // gets the double after the "M,"
        }
        catch (NumberFormatException | NoSuchElementException e)
        {
            message =   "The attempt to search for records was not in the proper format, the correct format is:\n" +
                    "M, minMag\n";
            System.out.println( message );
            validRequest = false;
        }

        if (validRequest)
        {
            fileName = filenameRequest();                  // check if user wishes to save to a file

            if (fileName.isEmpty())                        // user did not enter filename - display to console
            {
                quakeData.magnitudeSearch( magnitude );
            }
            else
            {
                quakeData.magnitudeSearch( magnitude, fileName );
            }
        }

    }

    /**
     * Method that asks the user about saving to a quake data to a file and gets the filename to do so
     * @return String: A filename to save the quake data to.  If the user wishes to display data to the console,
     *                  the return String will be empty
     */
    public static String filenameRequest()
    {
        String message, input;
        Scanner keyboard = new Scanner( System.in );

        message =   "Do you wish to save this data to a file?  If yes, enter the filename and press enter.\n" +
                    "Otherwise just press enter and the data will be displayed to the console.";
        System.out.println( message );
        input = keyboard.nextLine();

        return input;
    }

}
