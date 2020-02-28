/************************************************************************************************
 *  This class handles the processing of a quake records
 *
 *  It contains an array to manage all the data records uploaded from a file
 *  It has method to allow searching for data records by region, date, and magnitude
 *  The methods may display to the console, or to a file based on the user's request
 *
 *  CST 283 Programming Assignment 1
 *  @author Michael Clinesmith
 ***********************************************************************************************/
import javax.swing.JOptionPane;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.*;
import java.util.StringTokenizer;

public class QuakeData
{
    private final int MAX_RECORDS = 130000;
    private QuakeRecord[] records = new QuakeRecord[MAX_RECORDS];
    private int numOfRecords, numOfRecordsMissed;

    /**
     * No-argument constructor
     */
    public QuakeData()
    {
        numOfRecords = 0;
        numOfRecordsMissed = 0;
    }

    /**
     * Constructor with filename
     * The constructor processes the data in the file name given, and if valid, creates an array of records
     * containing the quake data
     * @param fileName String: the name of the file to open containing quake data records
     */
    public QuakeData(String fileName)
    {
        String message;
        File quakeData;                     // file that holds the quake data
        Scanner inputFile;                  // used to get data from file
        String inputLine;                   // String used to get a line of file input

        numOfRecords = 0;
        numOfRecordsMissed = 0;

        try                                         // catch if problems loading data
        {
            quakeData = new File(fileName);

            if(!quakeData.exists())                 // end program if file data does not exist
            {
                message = "The file " + fileName + " does not exist for processing data.\n" +
                        "The program will now end.";

                JOptionPane.showMessageDialog(null, message);
                System.exit(0);
            }

            inputFile = new Scanner( quakeData );

            // Read input file while more data exist
            // Read one line at a time (assuming each line contains one quake record)
            while (inputFile.hasNext() && numOfRecords<MAX_RECORDS)
            {
                try                                 // catch if problem with an individual data record
                {
                    inputLine = inputFile.nextLine();
                    records[numOfRecords] = new QuakeRecord( inputLine );    // record processed in QuakeRecord class
                                                                                // will throw IOException if problem with data

                    numOfRecords++;
                }
                catch (IOException e)
                {
                    message = "There was an error processing a record in the file " + fileName + ".\n" +
                            "The record will be skipped.";

                    JOptionPane.showMessageDialog( null, message, "Corrupted Data", JOptionPane.ERROR_MESSAGE );
                    numOfRecordsMissed++;
                }

            }

            // if extra records not saved because array storage is full, count records so it can be noted
            if (inputFile.hasNext())
            {
                message = "Quake data array is full.\n" +
                        "The remaining records will be skipped.";

                JOptionPane.showMessageDialog( null, message, "Storage Full", JOptionPane.ERROR_MESSAGE );
            }
            while (inputFile.hasNext())
            {
                inputLine = inputFile.nextLine();
                numOfRecordsMissed++;                           // record not saved since no room
            }

            inputFile.close();
        }
        catch (IOException e)  // if error loading data, give error message and end program
        {
            message = "There was an error processing the file " + fileName + ".\n" +
                    "The program will now end.";

            JOptionPane.showMessageDialog(null, message);
            System.exit(0);
        }
    }

    /**
     * Accessor method for getting the number of records in the QuakeRecord array
     * @return int: The number of records in the array
     */
    public int getNumOfRecords()
    {
        return numOfRecords;
    }

    /**
     * Accessor method for getting the number of records that had problems and were not added to the QuakeRecord array
     * @return int: The number of records not added to the array
     */
    public int getNumOfRecordsMissed()
    {
        return numOfRecordsMissed;
    }

    /**
     * Accessor method of getting the max size of the QuakeRecord array
     * @return int: The maximum number of records that can be held in the array
     */
    public int getMAX_RECORDS()
    {
        return MAX_RECORDS;
    }

    /**
     * Accessor method for getting an array record
     * @param i int: The index number for the record in the QuakeRecord array
     * @return QuakeRecord: The deep copy of the record stored in the QuakeRecord array
     */
    public QuakeRecord getRecord(int i)
    {
        QuakeRecord rec;

        if ( i>=0 && i<numOfRecords)
        {
            rec = new QuakeRecord( records[i] );
        }
        else
        {
            rec = new QuakeRecord();
            rec.setLocation( "Invalid Record" );
        }
        return rec;
    }

    /**
     * Method to add a record to the QuakeRecords array
     * @param record QuakeRecord: Quake data stored in a QuakeRecord object
     * @return boolean: true if the record was saved in the array, false if not
     */
    public boolean addRecord(QuakeRecord record)
    {
        boolean recordAdded=true;

        if (numOfRecords==MAX_RECORDS)          // if array full, cannot add record
        {
            numOfRecordsMissed++;
            recordAdded = false;
        }
        else
        {
            records[numOfRecords] = new QuakeRecord( record );          // makes a deep copy of the input record
            numOfRecords++;
        }

        return recordAdded;
    }

    /**
     * Method to remove a record from the QuakeRecords array
     * @param record QuakeRecord: The QuakeRecord object to remove from the QuakeRecords array
     * @return boolean: true if the record was removed from the array, false if not
     */
    public boolean removeRecord(QuakeRecord record)
    {
        boolean recordRemoved = false;

        for(int i=0; i<numOfRecords && !recordRemoved; i++)
        {
            if (record.isEqual( records[i] ))
            {
                records[i] = records[numOfRecords-1];       // move the last record in the array to replace this record
                numOfRecords--;
                recordRemoved = true;
            }
        }

        return recordRemoved;
    }

    /**
     * Method to search the quake records by latitude and longitude and display them to the console
     * @param minLat double: the minimum latitude in the area to retreive records
     * @param maxLat double: the maximum latitude in the area to retreive records
     * @param minLon double: the minimum longitude in the area to retreive records
     * @param maxLon double: the maximum longitude in the area to retreive records
     * @return boolean: true if the search was done and displayed, false if there were problems with the parameters and
     *                  the records were not displayed
     */
    public boolean regionSearch(double minLat, double maxLat, double minLon, double maxLon)
    {
        boolean searchDone = true;
        int count = 0;
        String message = "";            // used for printing messages to console
        double recLat, recLong;         // temp variables for checking latitude and longitude in records
        // check if parameters are valid
        if( minLat >  maxLat || minLon > maxLon  || minLat < -90 || maxLat > 90 || minLon < -180 || maxLon > 180)
        {
            searchDone = false;
            message = "Latitude or longitude values not valid." +
                    "\nSearch not processed.";
            System.out.println( message );
        }
        else
        {
            for(int i=0; i<numOfRecords; i++)
            {
                // if record in range, display it

                recLat = records[i].getLatitude();
                recLong = records[i].getLongitude();
                if (minLat <= recLat && recLat <= maxLat && minLon <= recLong && recLong <= maxLon)
                {
                    System.out.println( records[i].toModString() );
                    count++;
                }
            }
            System.out.println( count + " records found." );
        }

        return searchDone;
    }

    /**
     * Method to search the quake records by latitude and longitude and store them in the given filename
     * @param minLat double: the minimum latitude in the area to retreive records
     * @param maxLat double: the maximum latitude in the area to retreive records
     * @param minLon double: the minimum longitude in the area to retreive records
     * @param maxLon double: the maximum longitude in the area to retreive records
     * @param filename String: the name of the file to store the data
     * @return boolean: true if the search was done and saved, false if there were problems with the parameters or
     *                  the records were not saved
     */
    public boolean regionSearch(double minLat, double maxLat, double minLon, double maxLon, String filename)
    {
        File quakeOutputFile;
        PrintWriter outputFile;
        String message;
        String input;                   // used to get input from the user
        double recLat, recLong;         // temp variables for checking latitude and longitude in records
        boolean createFile = true;
        int count = 0;
        Scanner keyboard = new Scanner( System.in );

        quakeOutputFile = new File(filename);

        if(quakeOutputFile.exists())
        {
            message = "File " + filename + " already exists.\n" +
                    "Do you wish to overwrite this file? Y/N";

            System.out.println( message );

            input = keyboard.nextLine();
            if (input==null || input.length()<1 || (input.charAt( 0 ) != 'Y' && input.charAt( 0 ) != 'y'))
            {
                createFile = false;
                message = "File not created.";
                System.out.println( message );
            }
        }
        //  check if parameter values for latitude and longitude are in range and valid
        if( minLat >  maxLat || minLon > maxLon  || minLat < -90 || maxLat > 90 || minLon < -180 || maxLon > 180)
        {
            createFile = false;
            message = "Latitude or longitude values not valid." +
                        "\nFile not created.";
            System.out.println( message );
        }

        if (createFile)
        {
            try                     // catch possible exception
            {
                outputFile = new PrintWriter( quakeOutputFile );

                for(int i=0; i<numOfRecords; i++)
                {
                    // if record in range, display it
                    recLat = records[i].getLatitude();
                    recLong = records[i].getLongitude();
                    if (minLat <= recLat && recLat <= maxLat && minLon <= recLong && recLong <= maxLon)
                    {
                        outputFile.println( records[i].toModString() );
                        count++;
                    }
                }
                outputFile.println( count + " records found." );
                outputFile.close();

                message = "Records saved in file " + filename + ".\n";
                System.out.println( message );

            }
            catch(FileNotFoundException e)
            {
                message = "File " + filename + " could not be opened.\n" +
                        "File not created.";
                System.out.println( message );
            }
        }

        return createFile;
    }

    /**
     * Method to retrieve and display quake records from the minDate to the maxDate and display them to the console
     * @param minDate Dates: The earliest date, saved in a Dates object
     * @param maxDate Dates: The latest date, saved in a Dates object
     * @return boolean: true if the search was done and displayed, false if there were problems with the parameters and
     *                  the records were not displayed
     */
    public boolean dateSearch(Dates minDate, Dates maxDate)
    {
        boolean searchDone = true;
        int count = 0;
        String message="";

        // check if date conditions valid and minDate not after maxDate
        if( !minDate.isValid() || !maxDate.isValid() || minDate.compareTo( maxDate)>0 )
        {
            searchDone = false;
            message = "Input dates not valid." +
                    "\nSearch not processed.";
            System.out.println( message );
        }
        else
        {
            System.out.println( minDate.toModString() + " " + maxDate.toModString() );

            for(int i=0; i<numOfRecords; i++)
            {
                // if record date is not before minDate or after maxDate
                if (minDate.compareTo(records[i].getDate())<=0 && maxDate.compareTo( records[i].getDate())>=0)
                {
                    System.out.println( records[i].toModString() );
                    count++;
                }
            }
            System.out.println( count + " records found." );
        }

        return searchDone;
    }

    /**
     * Method to retrieve and display quake records from the minDate to the maxDate and to save to the given filename
     * @param minDate Dates: The earliest date, saved in a Dates object
     * @param maxDate Dates: The latest date, saved in a Dates objecct
     * @param filename String: The filename in which to save the record
     * @return boolean: true if the search was done and saved, false if there were problems with the parameters or
     *                  the records were not saved
     */
    public boolean dateSearch(Dates minDate, Dates maxDate, String filename)
    {
        File quakeOutputFile;
        PrintWriter outputFile;
        String message;
        String input;           // used to get input from the user
        boolean createFile = true;
        int count = 0;
        Scanner keyboard = new Scanner( System.in );

        quakeOutputFile = new File(filename);

        if(quakeOutputFile.exists())
        {
            message = "File " + filename + " already exists.\n" +
                    "Do you wish to overwrite this file? Y/N";

            System.out.println( message );

            input = keyboard.nextLine();
            if (input==null || input.length()<1 || (input.charAt( 0 ) != 'Y' && input.charAt( 0 ) != 'y'))
            {
                createFile = false;
                message = "File not created.";
                System.out.println( message );
            }
        }

        // check if date conditions valid and minDate not after maxDate
        if( !minDate.isValid() || !maxDate.isValid() || minDate.compareTo( maxDate)>0 )
        {
            createFile = false;
            message = "Input dates not valid." +
                    "\nSearch not processed.";
            System.out.println( message );
        }

        if (createFile)
        {
            try                     // catch possible exception
            {
                outputFile = new PrintWriter( quakeOutputFile );

                for (int i = 0; i < numOfRecords; i++)
                {
                    // if record date is not before minDate or after maxDate
                    if (minDate.compareTo( records[i].getDate() ) <= 0 && maxDate.compareTo( records[i].getDate() ) >= 0)
                    {
                        outputFile.println( records[i].toModString() );
                        count++;
                    }
                }
                outputFile.println( count + " records found." );
                outputFile.close();

                message = "Records saved in file " + filename + ".\n";
                System.out.println( message );

            }
            catch (FileNotFoundException e)
            {
                message = "File " + filename + " could not be opened.\n" +
                        "File not created.";
                System.out.println( message );
            }

        }

        return createFile;
    }

    /**
     * Method to search and retrieve records of quakes with magnitudes at least minMag and display to the console
     * @param minMag double: The minimum magnitude for quakes to retrieve data
     * @return boolean: true if the search was done and displayed, false if there were problems with the parameters and
     *                  the records were not displayed
     */
    public boolean magnitudeSearch(double minMag)
    {
        boolean searchDone = true;
        int count = 0;
        String message="";

        // check if minimum magnitude in appropriate range
        if( minMag<4.0 || minMag>10.0 )
        {
            searchDone = false;
            message = "Magnitude must be between 4.0 and 10.0" +
                        "\nSearch not processed.";
            System.out.println( message );
        }
        else
        {
            for(int i=0; i<numOfRecords; i++)
            {
                // if magnitude is at least minumum specified
                if (records[i].getRichter()>=minMag)
                {
                    System.out.println( records[i].toModString() );
                    count++;
                }
            }
            System.out.println( count + " records found." );
        }

        return searchDone;
    }

    /**
     * Method to search and retrieve records of quakes with magnitudes at least minMag and save to filename
     * @param minMag double: The minimum magnitude for quakes to retrieve data
     * @param filename String: The name of the file to use to save the quake records
     * @return boolean: true if the search was done and displayed, false if there were problems with the parameters and
     *                  the records were not displayed
     */
    public boolean magnitudeSearch(double minMag, String filename)
    {
        File quakeOutputFile;
        PrintWriter outputFile;
        String message;
        String input;           // used to get input from the user
        boolean createFile = true;
        int count = 0;
        Scanner keyboard = new Scanner( System.in );

        quakeOutputFile = new File(filename);

        if(quakeOutputFile.exists())
        {
            message = "File " + filename + " already exists.\n" +
                    "Do you wish to overwrite this file? Y/N";

            System.out.println( message );

            input = keyboard.nextLine();
            if (input==null || input.length()<1 || (input.charAt( 0 ) != 'Y' && input.charAt( 0 ) != 'y'))
            {
                createFile = false;
                message = "File not created.";
                System.out.println( message );
            }
        }

        // check if minimum magnitude in appropriate range
        if( minMag<4.0 || minMag>10.0 )
        {
            createFile = false;
            message = "Magnitude must be between 4.0 and 10.0" +
                    "\nSearch not processed.";
            System.out.println( message );
        }

        if (createFile)
        {
            try                     // catch possible exception
            {
                outputFile = new PrintWriter( quakeOutputFile );

                for (int i = 0; i < numOfRecords; i++)
                {
                    // if magnitude is at least minumum specified
                    if (records[i].getRichter()>=minMag)
                    {
                        outputFile.println( records[i].toModString() );
                        count++;
                    }
                }
                outputFile.println( count + " records found." );
                outputFile.close();

                message = "Records saved in file " + filename + ".\n";
                System.out.println( message );

            }
            catch (FileNotFoundException e)
            {
                message = "File " + filename + " could not be opened.\n" +
                        "File not created.";
                System.out.println( message );
            }

        }

        return createFile;
    }

    /**
     * Method to return information about the QuakeData object saved as a String
     * It does not include the data saved in the records as part of the string
     * @return String: A String listing the number of records, the number of records missed, the maximum array size,
     *                  and the first record of the QuakeRecord array
     */
    @Override
    public String toString()
    {
        String str="";

        str += "Number of records: " + numOfRecords;
        str += "\nNumber of records missed: " + numOfRecordsMissed;
        str += "\nMaximum number records that can be loaded: " + MAX_RECORDS;

        if( numOfRecords>0)
        {
            str += "\nFirst record: " + getRecord( 0 ).toString();
        }

        return str;
    }

    /**
     * Method to return information about the QuakeData object saved as a String
     * It does not include the data saved in the records as part of the string
     * @return String: A String listing the number of records, the number of records missed, the maximum array size,
     *                  and the first record of the QuakeRecord array
     */
    public String toModString()
    {
        String str="";

        str += "Number of records: " + numOfRecords;
        str += "\nNumber of records missed: " + numOfRecordsMissed;
        str += "\nMaximum number records that can be loaded: " + MAX_RECORDS;

        if( numOfRecords>0)
        {
            str += "\nFirst record: " + getRecord( 0 ).toModString();
        }

        return str;
    }

}
