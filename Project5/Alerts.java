/************************************************************************************************
 *  This class stores information regarding security and weather alerts.
 *
 *  It also loads alert definitions into memory
 *
 *  It will create an alert message using the toString method which also uses data contained in
 *  a CountyList object
 *
 *  CST 283 Programming Assignment 5
 *  Modified from CountyData.java - CST 183 Programming Assignment 7
 *  @author Michael Clinesmith
 ***********************************************************************************************/

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class Alerts
{

    // static fields concerning alert meaning
    private static final int MAX_CODES=50, MAX_SECURITY=10;
    private static final String ALERT_FILE = "warningList.txt";  // This file should have the alert definitions in it
    private static int numCodes, numSeverity, numSecurity;
    private static String[] weatherWarningCodes = new String[MAX_CODES];            // 2 letter weather codes
    private static String[] weatherWarningType = new String[MAX_CODES];             // definition of those codes
    private static String[] weatherWarningSeverity = new String[MAX_SECURITY];      // 1 letter weather severity code
    private static String[] weatherWarningSeverityType = new String[MAX_SECURITY];  // definition of those codes
    private static String[] securityWarningCode = new String[MAX_SECURITY];         // color national security code
    private static String[] securityWarningType = new String[MAX_SECURITY];         // definition of those codes


    private static boolean isLoaded=false;      // used to indicate codes needing to be loaded (so only happens once)
    private static CountyList countyList = null; // used to allow access to the county list (and only save once)

    // single alert fields
    private String FIPSCode, warningCode;
    private Dates startDate, endDate;
    private Times startTime, endTime;

    /**
     * No argument constructor, sets default values and loads the codes if necessary
     */
    public Alerts()
    {
        FIPSCode="99999";
        warningCode="---";
        startDate = new Dates();
        startTime = new Times();
        endDate = new Dates();
        endTime = new Times();
        if(!isLoaded)
        {
            loadCodes();
        }
    }

    /**
     * Constructor with arguments
     * @param fCode String: FIPS codes representing a county (or state or US)
     * @param startDateTime String: A String representing the starting date and time
     * @param endDateTime String: A String representing the ending date and time
     * @param wCode String: The warning code
     */
    public Alerts(String fCode, String startDateTime, String endDateTime, String wCode )
    {

        FIPSCode = fCode;
        warningCode = wCode;
        try
        {
            // parse out parts of string from format YYYYMMDD to MM, DD, YYYY and convert to ints
            startDate = new Dates(Integer.parseInt( startDateTime.substring( 4,6 )),
                            Integer.parseInt( startDateTime.substring( 6, 8 )),
                            Integer.parseInt( startDateTime.substring( 0, 4 )));
            
            // parse out parts of string from format HHMM to HH, MM, SS and convert to ints
            startTime = new Times(Integer.parseInt( startDateTime.substring( 8, 10) ),
                            Integer.parseInt( startDateTime.substring( 10, 12 ) ),
                            0);

            // parse out parts of string from format YYYYMMDD to MM, DD, YYYY and convert to ints
            endDate = new Dates(Integer.parseInt( endDateTime.substring( 4,6 )),
                    Integer.parseInt( endDateTime.substring( 6, 8 )),
                    Integer.parseInt( endDateTime.substring( 0, 4 )));

            // parse out parts of string from format HHMM to HH, MM, SS=0 and convert to ints
            endTime = new Times(Integer.parseInt( endDateTime.substring( 8, 10) ),
                    Integer.parseInt( endDateTime.substring( 10, 12 ) ),
                    0);

        }
        catch(NumberFormatException | StringIndexOutOfBoundsException e ) // give default values if error in processing
        {
            startDate = new Dates();
            startTime = new Times();
            endDate = new Dates();
            endTime = new Times();
        }
        if(!isLoaded)
        {
            loadCodes();
        }
    }

    /**
     * Copy constructor - makes a deep copy of an Alerts
     * @param anAlert Alerts: The Alerts object to make a copy of
     */
    public Alerts(Alerts anAlert)
    {
        FIPSCode = anAlert.getFIPSCode();
        startDate = anAlert.getStartDate();
        startTime = anAlert.getStartTime();
        endDate = anAlert.getEndDate();
        endTime = anAlert.getEndTime();
        warningCode = anAlert.getWarningCode();
    }

    /**
     * Accessor method to get the County FIPS code
     * @return String: A County FIPS code
     */
    public String getFIPSCode()
    {
        return FIPSCode;
    }

    /**
     * Accessor method to get the startDate of an alert
     * @return Dates: A deep copy of the startDate of an alert
     */
    public Dates getStartDate()
    {
        return new Dates(startDate);
    }

    /**
     * Accessor method to get the endDate of an alert
     * @return Dates: A deep copy of the endDate of an alert
     */
    public Dates getEndDate()
    {
        return new Dates(endDate);
    }

    /**
     * Accessor method to get the startTime of an alert
     * @return Times: A deep copy of the startTime of an alert
     */
    public Times getStartTime()
    {
        return new Times(startTime);
    }

    /**
     * Accessor method to get the endTime of an alert
     * @return Times: A deep copy of the endTime of an alert
     */
    public Times getEndTime()
    {
        return new Times(endTime);
    }

    /**
     * Accessor method to get the warningCode of an alert
     * @return String: The warningCode of an alert
     */
    public String getWarningCode()
    {
        return warningCode;
    }

    /**
     * Method that gets the current CountyList being used regarding alrets
     * This method only returns a copy of the current address being used
     * It DOES NOT make a deep copy of the list
     * @return CountyList : The current CountyList stored in memory being used.
     */
    public static CountyList getCountyList()
    {
        return countyList;
    }

    /**
     * Mutator method to set the FIPSCode of an alert
     * @param FIPSCode String: A County (or State or US) FIPS code
     */
    public void setFIPSCode( String FIPSCode )
    {
        this.FIPSCode = FIPSCode;
    }

    /**
     * Mutator method to set the startDate of an alert - makes a deep copy
     * @param startDate Dates: A Dates object representing a starting date for an alert
     */
    public void setStartDate( Dates startDate )
    {
        this.startDate = new Dates(startDate);
    }

    /**
     * Mutator method to set the startTime of an alert - makes a deep copy
     * @param startTime Times: A Times object representing the starting time for an alert
     */
    public void setStartTime( Times startTime )
    {
        this.startTime = new Times(startTime);
    }

    /**
     * Mutator method to set the endDate for an alert - makes a deep copy
     * @param endDate Dates: A Dates object representing the ending date for an alert
     */
    public void setEndDate( Dates endDate )
    {
        this.endDate = new Dates(endDate);
    }

    /**
     * Mutator method to set the endTime for an alert - makes a deep copy
     * @param endTime Times: A Times object representing the ending time for an alert
     */
    public void setEndTime( Times endTime )
    {
        this.endTime = new Times(endTime);
    }

    /**
     * Mutator method to set the warningCode for an alert
     * @param warningCode String: A String representing the warning code
     */
    public void setWarningCode( String warningCode )
    {
        this.warningCode = warningCode;
    }

    /**
     * Static Mutator method to set the countyList object to be able to reference county population data
     * @param list CountyList: a CountyList object containing a list of county populations
     */
    public static void setCountyList( CountyList list )
    {
        countyList = list;
    }

    /**
     * Method that loads the warning labels from the file stored in ALERT_FILE into the class.
     *
     * The warning file is assumed to have the 2 character codes listed first, 1 character codes listed
     *      next, and color security codes last with appropriate headers
     *
     * There are a number of possible error messages that give the user the option to exit the program
     *      if the data is not formatted correctly
     *
     * THIS METHOD ALLOWS THE USER TO EXIT THE PROGRAM IF THERE ARE PROBLEMS LOADING THE DATA FILE
     *
     */
    public void loadCodes()
    {
        File warningData;
        String message;
        Scanner inputFile;
        int i=0;

        boolean fileEndEarly = false;
        boolean fileEnd = false;
        boolean continueLoop = true;

        String inputLine;

        try
        {
            String fips, name, state;                     // Work variables

            // Build list of county objects
            warningData = new File( ALERT_FILE );

            if (!warningData.exists())  // file not found
            {
                message = "The file " + ALERT_FILE + " containing warning definitions was not found.\n" +
                        "Do you wish to exit the program?";

                quitOption( message ); // gives user choice to quit
                return;                // exit method since no file found

            }

            inputFile = new Scanner( warningData );

            // Read one line at a time
            // skip through junk until "WEATHER AND NATURAL DISASTER" reached or at file end
            fileEnd = !(skipUntil( inputFile, "WEATHER AND NATURAL DISASTER" ));

            continueLoop = true;
            i = 0;          // used to work through array elements
            // load warning codes one line at a time
            while (continueLoop && !fileEndEarly && i<MAX_CODES)
            {
                // keep loading codes until try attempt fails, then proceed
                try
                {
                    if (inputFile.hasNext())
                    {
                        inputLine = inputFile.nextLine();
                        weatherWarningCodes[i] = inputLine.substring( 1, 3 );
                        weatherWarningType[i] = inputLine.substring( 4 );
                        i++;
                    } else
                    {
                        fileEndEarly = true;
                    }
                } catch (StringIndexOutOfBoundsException e)
                {
                    continueLoop = false;
                    numCodes = i;
                }
            }

            if (i==MAX_CODES)       // should not happen unless file modified
            {
                message = "The maximum number of weather warning codes has been uploaded.\n" +
                        "Some codes may not have been uploaded.\n" +
                        "Do you wish to exit?";

                quitOption( message );  // give user option to quit

            }

            // skip through junk until "where" reached or at file end
            fileEndEarly = !(skipUntil( inputFile, "where" ));

            continueLoop = true;
            i = 0;          // used to work through array elements
            // load warning severity codes one line at a time
            while (continueLoop && !fileEndEarly && i<MAX_SECURITY)
            {
                // keep loading codes until try attempt fails, then proceed
                try
                {
                    if (inputFile.hasNext())
                    {
                        inputLine = inputFile.nextLine();
                        weatherWarningSeverity[i] = inputLine.substring( 0, 1 );
                        weatherWarningSeverityType[i] = inputLine.substring( 2 );
                        i++;
                    } else
                    {
                        fileEndEarly = true;
                    }
                } catch (StringIndexOutOfBoundsException e)
                {
                    continueLoop = false;
                    numSeverity = i;
                }
            }

            if (i==MAX_SECURITY)        // should not happen unless file modified
            {
                message = "The maximum number of weather warning severities has been uploaded.\n" +
                        "Some codes severities may not have been uploaded.\n" +
                        "Do you wish to exit the program?";

                quitOption( message );  // give user option to quit
            }

            // skip through junk until "NATIONAL SECURITY" reached or at file end
            fileEndEarly = !(skipUntil( inputFile, "NATIONAL SECURITY" ));

            continueLoop = true;
            i = 0;          // used to work through array elements
            // load warning security codes one line at a time
            while (continueLoop && inputFile.hasNext())
            {
                int spacePos;

                // keep loading codes until try attempt fails, then proceed
                try
                {
                    inputLine = inputFile.nextLine();
                    spacePos = inputLine.indexOf( ' ' );

                    securityWarningCode[i] = inputLine.substring( 0, spacePos );
                    securityWarningType[i] = inputLine.substring( spacePos ).trim(); // also removes leading whitespace
                    i++;

                }
                catch (StringIndexOutOfBoundsException e)
                {
                    continueLoop = false;
                    numSecurity = i;
                }
            }

            if (i==MAX_SECURITY)        // should not happen unless file modified
            {
                message = "The maximum number of national security advisories has been uploaded.\n" +
                        "Some security levels may not have been uploaded." +
                        "Do you wish to end the program?";

               quitOption( message );   // give user option to quit
            }


            // end program if data not all loaded properly
            if (fileEndEarly || numSeverity==0)
            {
                message = "Not all the required definitions were found in the file " + ALERT_FILE + ".\n" +
                        "Do you wish to end the program?";

                quitOption( message );  // give user option to quit
            }

            message = "The security and warning message definitions from the file " + ALERT_FILE +
                    "\nare now uploaded into memory.";

            Alert alert = new Alert( Alert.AlertType.INFORMATION );
            alert.setTitle( "DATA LOADED" );
            alert.setContentText( message );
            alert.showAndWait();

            isLoaded = true;


        }
        catch (IOException e)  // if error loading data, give error message and end program
        {
            message = "There was an error processing the file " + ALERT_FILE + ".\n" +
                    "Do you wish to end the program?";

            quitOption( message );  // give user option to quit
        }

        return;
    }

    /**
     * This method takes an open file, and a string flag, and will advance the file stream until
     * the flag is found
     * @param openFile  Scanner: An input file stream to advance until the flag is found
     * @param stringFlag String: An string at the beginning of a line that the file is searching for
     * @return boolean: true if the flag was found, and false if not
     */
    public boolean skipUntil(Scanner openFile, String stringFlag)
    {
        boolean isFound = false;
        String inputLine = "";
        while (openFile.hasNext() && !isFound)
        {
            inputLine = openFile.nextLine();
            if (inputLine.startsWith( stringFlag ))
            {
                isFound = true;
            }

        }
        return isFound;
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

    /**
     * Method that saves all the alerts that have been stored into memory to a String
     * @return String: A readable String containing the contents of all the stored warnings in memory
     */
    public static String possibleAlertsString()
    {

        String alertString="NATIONAL SECURITY WARNINGS:\n";

        // add national security warnings
        for (int i=0; i<numSecurity; i++)
        {
            alertString += securityWarningCode[i] + " " + securityWarningType[i] + "\n";

        }

        // add first letter warning codes
        alertString +="\nFIRST LETTER WEATHER SEVERITY CODES:\n";

        for (int i=0; i<numSeverity; i++)
        {
            alertString += weatherWarningSeverity[i] + " " + weatherWarningSeverityType[i] + "\n";
        }

        // add second and third letter warning codes

        alertString +="\nSECOND AND THIRD LETTER WEATHER SEVERITY CODES:\n";
        for (int i=0; i<numCodes; i++)
        {
            alertString += weatherWarningCodes[i] + " " + weatherWarningType[i] + "\n";

        }


        return alertString;
    }

    /**
     * Method that identifies and returns the security warning or weather alert description based on its code
     * @return String: A String storing the type of security or weather alert
     */
    public String getWarningString()
    {
        boolean isFound = false;
        String str = "Not Found";
        for (int i=0; i<numSecurity && !isFound; i++)           // check if code is in security codes
        {
            if (warningCode.equals( securityWarningCode[i] ))
            {
                str = securityWarningType[i];
                isFound = true;
            }
        }

        if (!isFound && warningCode.length()==3)                // if not found, check if code is in weather codes
        {
            isFound=false;
            for (int i=0; i<numCodes; i++)
            {
                // check if ith code matches the last two letters of warningCode
                if (weatherWarningCodes[i].equals( warningCode.substring( 1 ) ))
                {
                    str = weatherWarningType[i] + " ";
                    for(int j=0; j<numSeverity && !isFound; j++)
                    {
                        // check if jth severity code matches the first letter of warningCode
                        if(weatherWarningSeverity[j].equals( warningCode.substring( 0,1 ) ))
                        {
                            isFound = true;
                            str +=weatherWarningSeverityType[j];
                        }
                    }

                    if (!isFound) // no matches for first letter
                    {
                        // end i loop, because no possible match on first character
                        str = "Not Found";
                        i=numCodes;
                    }
                }
            }
        }
    return str;
    }

    /**
     * Method that creates a priority level based on the type of alert
     * This is used to help sort the alerts
     * 0 - RED
     * 1 - ORANGE
     * 2 - YELLOW
     * 3 - BLUE
     * 4 - GREEN
     * 5 - Warning
     * 6 - Watch
     * 7 - Advisory
     * 8 - Other (unidentified)
     *
     * @return int : A priority level as listed above based on the warningCode
     */
    public int getAlertPriority()
    {
        int alertPriority = numSecurity + numSeverity;
        boolean isFound = false;
        for (int i=0; i<numSecurity && !isFound; i++)           // check if code is in security codes
        {
            if (warningCode.equals( securityWarningCode[i] ))
            {
                alertPriority = i;
                isFound = true;
            }
        }

        if (!isFound && warningCode.length()>0)                // if not found, check if code is in weather codes
        {
            isFound = false;

            for (int j = 0; j < numSeverity && !isFound; j++)
            {
                // check if jth severity code matches the first letter of warningCode
                if (weatherWarningSeverity[j].equals( warningCode.substring( 0, 1 ) ))
                {
                    isFound = true;
                    alertPriority = j + numSecurity;
                }
            }
        }
        return alertPriority;
    }

    /**
     * Method that checks if two alerts are equal
     * @param anAlert Alerts: Alerts object to compare to this one
     * @return boolean: true if the alerts are equal, false if they are not
     */
    public boolean isEqual(Alerts anAlert)
    {
        boolean equal = false;
        if (FIPSCode.equals( anAlert.getFIPSCode()) &&
                warningCode.equals( anAlert.getWarningCode() ) &&
                startDate.isEqual( anAlert.getStartDate() ) &&
                endDate.isEqual( anAlert.getEndDate() ) &&
                startTime.isEqual( anAlert.getStartTime() ) &&
                endTime.isEqual( anAlert.getEndTime() ) )
        {
            equal = true;
        }
        return equal;
    }


    /**
     * Method to compare two Alerts objects
     * The method compares the alert priority and if equal, compares the population (if it exists)
     *
     * The method will return 1 if
     * 1) "this" ALerts has a higher priority (lower value) than the anAlert parameter
     * 2) "this" Alerts has the same priority as the anAlert parameter and "this" Alerts has a greater population
     *
     * The method will return 0 if
     * "this" Alerts has the same priority and population as the anAlert parameter
     *
     * The method will return -1 if
     * 1) "this" Alerts has a lower priority (higher value) than the anAlert parameter
     * 2) "this" Alerts has the same priority as the anAlert parameter and "this" Alerts has a lower population
     *
     * @param anAlert Alerts: An Alert object that is being compared to this object
     * @return int: See Comment
     */
    public int compareTo(Alerts anAlert)
    {
        int value = 0;
        // compare priorities
        if (getAlertPriority()<anAlert.getAlertPriority())
        {
            value = 1;
        }
        else if(getAlertPriority()>anAlert.getAlertPriority())
        {
            value = -1;
        }
        else // alert level same so compare populations if exist
        {
            if(countyList!=null)
            {
                if (countyList.getPopulation( FIPSCode )>countyList.getPopulation( anAlert.getFIPSCode() ))
                {
                    value = 1;
                }
                else if (countyList.getPopulation( FIPSCode )<countyList.getPopulation( anAlert.getFIPSCode() ))
                {
                    value = -1;
                }
            }

        }

        return value;
    }

    /**
     * Method that returns a string based on the alert information stored in the alert
     * It also uses information in the CountyList Class if available
     * @return String: A String message displaying the Alerts data in readable format
     */
    @Override
    public String toString()
    {

        String str="";

        // get the warning type
        str += getWarningString() + " for ";

        // get the county name or FIPS code
        if (countyList!=null)
        {
            str += countyList.getName( FIPSCode ) + ",  " + countyList.getState( FIPSCode);
        }
        else
        {
            str += "county " + FIPSCode;
        }

        str +="\n";

        // get the start date
        str += startDate.toString() + " " + startTime.toString() + " - ";

        // get the end date
        str += endDate.toString() + " " + endTime.toString() + "\n";

        str += "Population Impact: ";

        // get the population if countyList exists
        if (countyList!=null)
        {
            str += String.format( "%,d" , countyList.getPopulation( FIPSCode ));
        }
        else
        {
            str += "???,???";
        }

        return str;
    }
}
