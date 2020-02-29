/************************************************************************************************
 *  This class contains the character data
 *
 *  Note: Key values should be unique and the program will work to keep them that way, but there is
 *  nothing stopping someone creating a data file where the keys are not unique
 *
 *  The class implements sorting and randomizing the order of records
 *  The records can be viewed through the toString methods
 *
 *
 *
 *  CST 283 Programming Assignment 3
 *  @author Michael Clinesmith
 ***********************************************************************************************/

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class CharacterData
{
    private final int MAX_RECORDS = 10000;
    private CharacterRecord[] records = new CharacterRecord[MAX_RECORDS];
    private int numOfRecords, numOfRecordsMissed;
    private String currentFile="";

    /**
     * No-argument constructor
     */
    public CharacterData()
    {
        numOfRecords = 0;
        numOfRecordsMissed = 0;
    }

    /**
     * Constructor with filename
     * The constructor processes the data in the file name given, and if valid, creates an array of records
     * containing the character data
     * @param fileName String: the name of the file to open containing character data records
     */
    public CharacterData(String fileName)
    {
        String message;
        File characterData;                     // file that holds the character data
        Scanner inputFile;                  // used to get data from file
        String inputLine;                   // String used to get a line of file input

        numOfRecords = 0;
        numOfRecordsMissed = 0;

        try                                         // catch if problems loading data
        {
            characterData = new File(fileName);

            if(!characterData.exists())                 // end program if file data does not exist
            {

                message = "The file " + fileName + " does not exist for processing data.\n" +
                        "No data was uploaded.";
                Alert alert = new Alert( Alert.AlertType.ERROR );
                alert.setTitle( "File not found" );
                alert.setContentText( message );
                alert.showAndWait();

            }
            else
            {
                inputFile = new Scanner( characterData );

                // Read input file while more data exist
                // Read one line at a time (assuming each line contains one character record)
                while (inputFile.hasNext() && numOfRecords < MAX_RECORDS)
                {

                    inputLine = inputFile.nextLine();
                    try{
                        records[numOfRecords] = new CharacterRecord( inputLine );    // record processed in CharacterRecord class
                        numOfRecords++;
                    }
                    catch (IOException ex)
                    {
                        // blank line sent to create a record, so ignore
                    }

                }

                // if extra records not saved because array storage is full, count records so it can be noted
                if (inputFile.hasNext())
                {
                    message = "Character data array is full.\n" +
                            "The remaining records will be skipped.";

                    Alert alert = new Alert( Alert.AlertType.ERROR );
                    alert.setTitle( "Too much data - Array Overflow" );
                    alert.setContentText( message );
                    alert.showAndWait();

                }
                while (inputFile.hasNext())
                {
                    inputLine = inputFile.nextLine();
                    numOfRecordsMissed++;                           // record not saved since no room
                }

                inputFile.close();
                currentFile=fileName;
            }
        }
        catch (IOException e)  // if error loading data, give error message
        {
            message = "There was an error encountered.  No data was loaded.";
            Alert alert = new Alert( Alert.AlertType.ERROR );
            alert.setTitle( "IOException Error" );
            alert.setContentText( message );
            alert.showAndWait();
        }
    }

    /**
     * Accessor method for getting the number of records in the CharacterRecord array
     * @return int: The number of records in the array
     */
    public int getNumOfRecords()
    {
        return numOfRecords;
    }

    /**
     * Accessor method for getting the number of records that had problems and were not added to the CharacterRecord array
     * @return int: The number of records not added to the array
     */
    public int getNumOfRecordsMissed()
    {
        return numOfRecordsMissed;
    }

    /**
     * Accessor method of getting the max size of the CharacterRecord array
     * @return int: The maximum number of records that can be held in the array
     */
    public int getMAX_RECORDS()
    {
        return MAX_RECORDS;
    }

    /**
     * Accessor method of getting the most recent filename used to load or save the file
     * @return String: The name of the file used to load or save the file
     */
    public String getCurrentFile()
    {
        return currentFile;
    }

    /**
     * Method that gets the record given a particular key
     * @param key String: The key to identify the record to be retrieved
     * @return CharacterRecord: A deep copy of the record searched for.  If not found, null is returned.
     */
    public CharacterRecord getRecordFromKey(String key)
    {
        CharacterRecord rec = null; // if no record found, return null value
        int index = getIndexFromKey( key );
        if(index>=0 && index<numOfRecords)
        {
            rec = new CharacterRecord( records[index]); // creates a deep copy of record
        }

        return rec;
    }

    /**
     * Method that gets the index of the record given a particular key
     * @param key String: The key to identify the record
     * @return int: the index of the record
     */
    public int getIndexFromKey(String key)
    {
        boolean found=false;
        int index=-1;
        for(int i=0; i<numOfRecords & !found; i++)
        {
            if( key.equals( records[i].getKey()) )
            {
                found=true;
                index=i;
            }
        }
        return index;
    }

    /**
     * Method that gets the record given a particular index
     * @param index int: The index of the record in the data array
     * @return CharacterRecord: A deep copy of the record located at the index position in the data
     */
    public CharacterRecord getRecordFromIndex(int index)
    {
        CharacterRecord rec = null; // if no record found, return null value
        if(index>=0 && index<numOfRecords)
        {
            rec = new CharacterRecord( records[index]); // creates a deep copy of record
        }
        return rec;
    }

    /**
     * Method that adds a record to CharacterData
     * @param rec CharacterRecord: record to be added to CharacterData
     * @return boolean: true if the record was added, false if not (because of too many records)
     */
    public boolean addRecord( CharacterRecord rec)
    {
        boolean recordAdded=true;
        if(numOfRecords>=MAX_RECORDS)
        {
            recordAdded=false;
        }
        else
        {
            records[numOfRecords] = new CharacterRecord( rec ); // makes a deep copy
            numOfRecords++;
        }

        return recordAdded;
    }

    /**
     * Method that deletes a record from CharacterData
     * @param rec CharacterRecord: record to be deleted from CharacterData (only key is accessed)
     * @return boolean: true if the record was deleted, false if not
     */
    public boolean deleteRecord( CharacterRecord rec)
    {
        boolean recordDeleted=false;
        int index=getIndexFromKey( rec.getKey() );  // finds the index of the record from the rec's key
        if(index>=0 && index<numOfRecords)
        {
            //delete the record but keep other records in the same order
            for(int i=index; i<numOfRecords; i++)
            {
                records[i]=records[i+1]; // shallow copy moving of records - no risk of changing records from outside here
            }
            numOfRecords--;
            recordDeleted=true;
        }
        return recordDeleted;
    }

    /**
     * Method that deletes a record from CharacterData
     * @param key String: Key of the record to be deleted from CharacterData
     * @return boolean: true if the record was deleted, false if not
     */
    public boolean deleteRecord( String key)
    {
        boolean recordDeleted=false;
        int index=getIndexFromKey( key );  // finds the index of the record from the key
        if(index>=0 && index<numOfRecords)
        {
            //delete the record but keep other records in the same order
            for(int i=index; i<numOfRecords; i++)
            {
                records[i]=records[i+1]; // shallow copy moving of records - no risk of changing records from outside here
            }
            numOfRecords--;
            recordDeleted=true;
        }
        return recordDeleted;
    }

    /**
     * Method that updates a record from CharacterData
     * Note the method does not add the record if the key is not already in CharacterData
     * @param rec CharacterRecord: record to be updated in CharacterData
     * @return boolean: true if the record was updated, false if not
     */
    public boolean updateRecord( CharacterRecord rec)
    {
        boolean recordUpdated = false;
        int index=getIndexFromKey( rec.getKey() );  // finds the index of the record from the rec's key
        if(index>=0 && index<numOfRecords)
        {
            records[index]=new CharacterRecord( rec ); // makes a deep copy
            recordUpdated=true;
        }

        return recordUpdated;
    }

    /**
     * Method that resets the CharacterData object
     */
    public void resetCharacterData()
    {
        numOfRecords = 0;
        numOfRecordsMissed = 0;
        currentFile="";
        CharacterRecord.resetRecordFields();
    }

    /**
     * Method to check if a key has been used (Helps with giving unique keys)
     * @param key String: The key to check if it has been used already
     * @return boolean: True if it has been used, false if not
     */
    public boolean isUsedKey(String key)
    {
        boolean used=false;
        {
            for(int i=0; i<numOfRecords && !used; i++)
            {

                if(key.equals( records[i].getKey() ))
                {
                    used=true;
                }
            }
        }
        return used;
    }

    /**
     * Method to update a comboBox with all the keys in CharacterData
     * @param comboBox ComboBox: A ComboBox that needs the keys entered into it so they can be selected
     */
    public void updateComboBox( ComboBox<String> comboBox )
    {
        for (int i = 0; i<numOfRecords; i++)
        {
            comboBox.getItems().add(records[i].getKey());
        }
    }

    /**
     * Method that gets the data stored in the CharacterRecords and returns it as a String
     * it also updates the filename value
     * @param filename String: The name of the file to be used to store the CharacterRecords data
     * @return String: A string of CharacterRecords
     */
    public String saveDataString(String filename)
    {
        currentFile=filename;
        return toString();
    }

    /**
     * Method that sorts the records according to the field sent in fieldLabel
     * @param fieldLabel String: the field to sort
     * @param ascOrDes boolean: indicates if the sort is to be ascending (true) or descending(false)
     * @return boolean: true if the records were sorted, false if not
     */
    public boolean sortRecords(String fieldLabel, boolean ascOrDes)
    {
        int fieldIndex=CharacterRecord.getLabelIndex( fieldLabel );
        boolean sortedRecords=false;

        if (fieldIndex>=0 && fieldIndex<=CharacterRecord.getNumOfFields())  // index must be valid
        {
            sortedRecords=true;
            if (ascOrDes)                   // ascending sort
            {
                if (CharacterRecord.isFieldTypeInt( fieldIndex ))           // sort with ints
                {
                    sortRecordsAscInt(fieldIndex);
                }
                else                                                        // sort with strings
                {
                    sortRecordsAscString(fieldIndex);
                }
            }
            else                            // descending sort
            {
                if (CharacterRecord.isFieldTypeInt( fieldIndex ))           // sort with ints
                {
                    sortRecordsDesInt(fieldIndex);
                }
                else                                                        // sort with strings
                {
                    sortRecordsDesString(fieldIndex);

                }
            }
        }
        return sortedRecords;
    }

    /**
     * Method that sorts the records ascending in String order based in the field represented by fieldIndex
     * @param fieldIndex int: the field to sort
     */
    public void sortRecordsAscString(int fieldIndex)
    {
        // bubblesort modified from program shown in class
        int lastPos;
        int index;
        CharacterRecord temp;

        for (lastPos = numOfRecords - 1; lastPos >= 0; lastPos--)
        {
            for (index = 0; index <= lastPos - 1; index++)
            {
                if (records[index].getFieldValue( fieldIndex ).compareTo( records[index+1].getFieldValue( fieldIndex ) ) > 0)
                {
                    temp = records[index];
                    records[index] = records[index + 1];
                    records[index + 1] = temp;
                }
            }
        }
    }
    /**
     * Method that sorts the records descending in String order based in the field represented by fieldIndex
     * @param fieldIndex int: the field to sort
     */
    public void sortRecordsDesString(int fieldIndex)
    {
        // bubblesort modified from program shown in class
        int lastPos;
        int index;
        CharacterRecord temp;

        for (lastPos = numOfRecords - 1; lastPos >= 0; lastPos--)
        {
            for (index = 0; index <= lastPos - 1; index++)
            {
                if (records[index].getFieldValue( fieldIndex ).compareTo( records[index+1].getFieldValue( fieldIndex ) ) < 0)
                {
                    temp = records[index];
                    records[index] = records[index + 1];
                    records[index + 1] = temp;
                }
            }
        }
    }

    /**
     * Method that sorts the records ascending in integer order based in the field represented by fieldIndex
     * @param fieldIndex int: the field to sort
     */
    public void sortRecordsAscInt(int fieldIndex)
    {
        // bubblesort modified from program shown in class
        int lastPos;
        int index;
        CharacterRecord temp;
        int thisValue, nextValue;

        try                 // watch for parsing errors
        {
            for (lastPos = numOfRecords - 1; lastPos >= 0; lastPos--)
            {
                for (index = 0; index <= lastPos - 1; index++)
                {
                    thisValue = Integer.parseInt( records[index].getFieldValue( fieldIndex ) );
                    nextValue = Integer.parseInt( records[index+1].getFieldValue( fieldIndex ) );

                    if (thisValue>nextValue)
                    {
                        temp = records[index];
                        records[index] = records[index + 1];
                        records[index + 1] = temp;
                    }
                }
            }
        }
        catch(NumberFormatException e)  // not all value are integers, so format for strings
        {
            sortRecordsAscString( fieldIndex );
        }
    }

    /**
     * Method that sorts the records descending in integer order based in the field represented by fieldIndex
     * @param fieldIndex int: the field to sort
     */
    public void sortRecordsDesInt(int fieldIndex)
    {
        // bubblesort modified from program shown in class
        int lastPos;
        int index;
        CharacterRecord temp;
        int thisValue, nextValue;

        try                 // watch for parsing errors
        {
            for (lastPos = numOfRecords - 1; lastPos >= 0; lastPos--)
            {
                for (index = 0; index <= lastPos - 1; index++)
                {
                    thisValue = Integer.parseInt( records[index].getFieldValue( fieldIndex ) );
                    nextValue = Integer.parseInt( records[index+1].getFieldValue( fieldIndex ) );

                    if (thisValue<nextValue)
                    {
                        temp = records[index];
                        records[index] = records[index + 1];
                        records[index + 1] = temp;
                    }
                }
            }
        }
        catch(NumberFormatException e)  // not all value are integers, so format for strings
        {
            sortRecordsAscString( fieldIndex );
        }
    }

    public void sortRandom()
    {
        if (numOfRecords>0)
        {
            // create an array of random numbers then sort them along with records
            int[] randomValue= new int[numOfRecords];
            Random randomNumbers = new Random( );

            for(int i=0; i<numOfRecords; i++)
            {
                randomValue[i]=randomNumbers.nextInt();
            }

            // bubblesort modified from program shown in class
            int lastPos;
            int index;
            int temp;
            CharacterRecord tempRecord;

            for (lastPos = numOfRecords - 1; lastPos >= 0; lastPos--)
            {
                for (index = 0; index <= lastPos - 1; index++)
                {
                    if ( randomValue[index]<randomValue[index+1])
                    {
                        temp = randomValue[index];
                        randomValue[index] = randomValue[index+1];
                        randomValue[index+1]=temp;
                        tempRecord = records[index];
                        records[index] = records[index + 1];
                        records[index + 1] = tempRecord;
                    }
                }
            }

        }
    }

    /**
     * Method that gets the data stored in the CharacterRecords and returns it as a String
     * @return String: A string of CharacterRecords
     */
    @Override
    public String toString()
    {
        String str="";

        for(int i=0; i<numOfRecords-1; i++)
        {
            str += records[i].toString() + "\n";
        }
        if(numOfRecords>0)                      // if at least one record
        {
            str += records[numOfRecords-1];     // add last record without newline
        }

        return str;
    }

    /**
     * Method that gets the data stored in the CharacterRecords and saves it as a String
     * It formats the string so that there are labels and the fields line up evenly
     * @return String: A formatted string of CharacterRecords
     */
    public String toStringFormatted()
    {
        String str=CharacterRecord.toStringLabelsFormatted();

        for(int i=0; i<numOfRecords; i++)
        {
            str += "\n" + records[i].toStringFormatted();
        }

        return str;
    }


}
