/************************************************************************************************
 *  This class stores an array of county FIPS code, state and population data
 *
 *  It allows storage of state and national jurisdiction codes and lists of subjurisdictions
 *  of those entities
 *
 *  CST 283 Programming Assignment 5
 *  Modified from CountyData.java - CST 183 Programming Assignment 7
 *  @author Michael Clinesmith
 ***********************************************************************************************/

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.Scanner;
import java.io.*;
import java.util.StringTokenizer;

public class CountyList
{
    // constants usable by entire class
    private final static int NUM_OF_COUNTIES = 5000;

    private int numOfElements=0;
    private County[] countyList = new County[NUM_OF_COUNTIES];

    /**
     * No argument constructor
     */
    public CountyList()
    {
        numOfElements=0;
    }

    /**
     * Constructor loads County FIPS code and populations data from codeFilename and popFilename
     * @param codeFilename String: The file that has County FIPS code and name information
     * @param popFilename String: The file that has County FIPS code and population information
     */
    public CountyList(String codeFilename, String popFilename)
    {
        String message;
        File codeData;                // file that holds the population data
        Scanner inputFile;                  // used to get data from file
        int i = 0, j = 0, numElems = 0;
        String inputLine;                   // String used to get a line of file input

        setupListWithCodeData(codeFilename);

        setupListWithPopData(popFilename);

        calculateSuperPopData();

    }

    /**
     * Accessor method that gets the number of elements in the CountyArray
     * @return int: The number of elements in the County Array
     */
    public int getNumOfElements()
    {
        return numOfElements;
    }

    /**
     * Method that gets the index position of the country record with the given code
     * @param code String: A String code representing a county jursidiction
     * @return int: The index position of the county, or -1 if not found
     */
    public int getIndex(String code)
    {
        boolean isFound = false;
        int index = -1;
        for (int i=0; i<numOfElements && !isFound; i++)    // search array and exit if element found
        {
            if (countyList[i].getFIPScode().equals( code ))
            {
                index = i;
                isFound = true;
            }
        }
        return index;
    }

    /**
     * Method that get the name of a county with a given FIPS code
     * @param code String: A FIPS code for a county
     * @return String: The name of the county, or Not Found if not found
     */
    public String getName(String code)
    {
        String name = "Not Found";
        int index = getIndex( code );
        if (index>=0)
        {
            name = countyList[index].getCountyName();
        }
        return name;
    }

    /**
     * Method that gets the state abbreviation for a county
     * @param code String: The FIPS code of a county
     * @return String: The State abbreviation for a county or "Not Found" if not found
     */
    public String getState(String code)
    {
        String state = "Not Found";
        int index = getIndex( code );
        if (index>=0)
        {
            state = countyList[index].getStateCode();
        }
        return state;
    }

    /**
     * Method that gets the population for a county given its FIPS code
     * @param code String: The FIPS code of a county
     * @return int: The county's population, or -1 if it was not found
     */
    public int getPopulation(String code)
    {
        int pop = -1;
        int index = getIndex( code );
        if (index>=0)
        {
            pop = countyList[index].getPopulation();
        }
        return pop;
    }

    /**
     * Method that begins the creation of the county list, loading FIPS code and name information
     *
     * THE USER MAY END THE PROGRAM IN THE METHOD IF THERE IS PROBLEMS LOADING THE DATA
     *
     * @param filename String: A file that contains the FIPS code and name information for counties
     */
    private void setupListWithCodeData(String filename)
    {
        File codeData;
        String message;
        Scanner inputFile;
        int i=0;
        int commaLoc = 0;
        int USPos = 0;
        int currentStatePos = 0;

        String inputLine;

        try
        {
            String fips, name, state;                     // Work variables

            // Build list of county objects
            codeData = new File(filename);

            if(!codeData.exists())  // file not found
            {
                message = "The file " + filename + " containing fips data was not found.\n" +
                        "Do you want to end the program?";

                quitOption( message );  // give user option to exit program
                return;                 // end method since file not found
            }

            inputFile = new Scanner(codeData);

            // Read input file while more data exist
            // Read one line at a time (assuming each line contains one username)
            i = 0;          // used to work through array elements
            while (inputFile.hasNext())
            {
                inputLine = inputFile.nextLine();

                // Read all data on one line
                fips      = inputLine.substring( 0, 5 );
                commaLoc = inputLine.indexOf( "," );
                if (commaLoc>6)
                {
                    name = inputLine.substring( 6, commaLoc);
                    state = inputLine.substring( commaLoc+2 );
                }
                else
                {
                    name = inputLine.substring( 6 );
                    state = "NA";
                }

                countyList[i] = new County(fips,name,state, 0);

                // add county or state to appropriate superjursidiction
                if (commaLoc<0)     // new State
                {
                    countyList[i].setStateEntity( true );
                    if (!name.equals( "00000" )) // a State, but not UNITED STATES
                    {
                        countyList[USPos].addToSubEntityList( fips ); // sub to US
                        currentStatePos = i;                            // new State, set position marker
                    }
                }
                else            // County within a state
                {
                    countyList[currentStatePos].addToSubEntityList( fips );
                }

                i++;
            }
            numOfElements = i;    // Capture number of elements

            inputFile.close();
        }
        catch (IOException e)  // if error loading data, give error message and end program
        {
            message = "There was an error processing the file " + filename + ".\n" +
                    "Do you wish to end the program?";

            quitOption( message );      // give user option to quit
            return;                     // error processing, so return without displaying confirmation message
        }

        message = "The jurisdiction data from the file " + filename +
                "\nis now uploaded into memory.";

        Alert alert = new Alert( Alert.AlertType.INFORMATION );
        alert.setTitle( "DATA LOADED" );
        alert.setContentText( message );
        alert.showAndWait();

        return;
    }

    /**
     * Method that populates the county list with population data
     *
     * THE USER MAY END THE PROGRAM IN THE METHOD IF THERE IS PROBLEMS LOADING THE DATA
     *
     * @param filename String: A file that contains the FIPS code and name information for counties
     */
    private void setupListWithPopData(String filename)
    {
        File popData;
        String message;
        Scanner inputFile;
        String inputLine;
        int pop=0;
        int index;

        try
        {
            String fips, name, state;                     // Work variables

            StringTokenizer lineTokens;     //  used to get tokens from data input

            // Build list of county objects
            popData = new File(filename);

            if(!popData.exists())  // file not found
            {
                message = "The file " + filename + " containing fips data was not found.\n" +
                        "Do you wish to end the program?";

                quitOption( message );  // give user option to quit
                return;                 // exit method since file was not found
            }

            inputFile = new Scanner(popData);

            // Read input file while more data exist
            // Read one line at a time (assuming each line contains one username)
            index = 0;          // used to work through array elements
            while (inputFile.hasNext())
            {
                inputLine = inputFile.nextLine();

                // Read all data on one line
                fips      = inputLine.substring( 0, 5 );
                try
                {
                    pop = Integer.parseInt( inputLine.substring( 6 ));
                    index = getIndex(fips);

                    countyList[index].setPopulation( pop );
                }
                catch (NumberFormatException ex)
                {
                    message = "There was an error reading a population data record.\n" +
                            "Do you wish to exit this program?  If not, this record will be skipped.";

                    quitOption( message );  // give user option to quit
                }
                catch (ArrayIndexOutOfBoundsException ex)
                {
                    message = "A data record was found but that record was not in the county file.\n" +
                            "Do you wish to exit this program?  If not, this record will be skipped.";

                    quitOption( message );  // give user option to quit
                }

            }

            inputFile.close();
        }
        catch (IOException e)  // if error loading data, give error message and end program
        {
            message = "There was an error processing the file " + filename + ".\n" +
                    "Do you wish to exit this program?";

            quitOption( message );  // give user option to quit
            return;                 // exit method without confirmation message
        }

        message = "The population data from the file " + filename +
                "\nis now uploaded into memory.";

        Alert alert = new Alert( Alert.AlertType.INFORMATION );
        alert.setTitle( "DATA LOADED" );
        alert.setContentText( message );
        alert.showAndWait();

        return;
    }

    /**
     * Method that fills in the population values for all superjurisdictions (above the counties)
     */
    private void calculateSuperPopData()
    {
        String[] list;
        int listLength;
        int USIndex = getIndex( "00000" );
        int pop;

        for(int i = 0; i<numOfElements; i++)
        {
            if(countyList[i].isStateEntity() && i !=USIndex) // skip US to the end
            {
                list = countyList[i].getSubEntityList();
                listLength = countyList[i].getSubEntityCount();
                pop = 0;

                for(int j=0; j<listLength; j++)
                {
                    // gets the population of the county with code list[j] and adds it to the total
                    pop += countyList[getIndex( list[j] )].getPopulation();
                }
                countyList[i].setPopulation( pop );
            }
        }
        if (USIndex>=0)     // US index exists
        {
            // calculate US pop
            list = countyList[USIndex].getSubEntityList();
            listLength = countyList[USIndex].getSubEntityCount();
            pop = 0;
            for (int j = 0; j < listLength; j++)
            {
                // gets the population of the county with code list[j] and adds it to the total
                pop += countyList[getIndex( list[j] )].getPopulation();
            }
            countyList[USIndex].setPopulation( pop );
        }
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
