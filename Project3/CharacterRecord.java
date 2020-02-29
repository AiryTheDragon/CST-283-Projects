/************************************************************************************************
 *  This class handles the processing of a character Record
 *
 *  It stores the 20 individual items for each record.  The fields are stored as strings, but
 *  certain fields are flagged to be sorted as integers
 *
 *  CST 283 Programming Assignment 3
 *  @author Michael Clinesmith
 ***********************************************************************************************/
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class CharacterRecord
{
    static final private int NUM_OF_FIELDS = 20;

    static final private String[] FIELD_LABELS = {"Key", "Character Name", "Class", "Level", "2nd Class",
            "2nd Level", "Experience Points", "Race", "Background", "Alignment",
            "Player Name", "Strength", "Dexterity", "Constitution", "Intelligence",
            "Wisdom", "Charisma", "Perception", "Armor Class", "Max Hit Points"};
    static final private String[] FIELD_SHORT = {"Key", "Name", "Class", "Level", "Clas2",
            "Lev2", "Exp", "Race", "Back", "Align",
            "Owner", "Str", "Dex", "Con", "Int",
            "Wis", "Cha", "Per", "AC", "MaxHP"};
    static final private Boolean[] FIELD_INT = {true, false, false, true, false,
            true, true, false, false, false,
            false, true, true, true, true,
            true, true, true, true, true};

    static private int nextKey=1; // used to generate new keys for new records
    static private int[] fieldLongest= new int[NUM_OF_FIELDS];  // keeps track of longest record in each field for formatting
    static private boolean fieldLongestInitialized=false; // used to initialize field lengths when first created
    private String[] record= new String[NUM_OF_FIELDS];

    /**
     * No-argument constructor creates an empty record with key value of nextKey, which will be unique when no
     * created records have a larger value for a key
     */
    public CharacterRecord()
    {
        record[0]=Integer.toString( nextKey );  // gives the record a new key
        nextKey++;
        for(int i=1; i<NUM_OF_FIELDS; i++)
        {
            if(FIELD_INT[i])    // empty record has different values based on an integer or String field
            {
                record[i]="0";
            }
            else
            {
                record[i]="None";
            }
        }
        if(!fieldLongestInitialized)
        {
            initializeFieldLongest();
        }
    }

    /**
     * Constructor that takes a formatted string where fields are separated by commas and uses it to create
     *      a CharacterRecord object
     * The String should have NUM_OF_FIELDS=20 fields
     * The key value will be checked and nextKey will be set to a larger value than the key value
     *
     * if the string is short of data, the constructor will throw an IOexception
     * @param stringRecord String: a String in the proper form
     */
    public CharacterRecord(String stringRecord) throws IOException
    {
        StringTokenizer stringData;
        int i=0, keyValue;

        if(!fieldLongestInitialized)
        {
            initializeFieldLongest();
        }

        try
        {
            stringData = new StringTokenizer( stringRecord, "," );

            for(i=0; i<NUM_OF_FIELDS; i++)
            {
                record[i]=stringData.nextToken();
                if(record[i].length()>fieldLongest[i])
                {
                    fieldLongest[i]=record[i].length();
                }
            }
        }
        catch (NoSuchElementException e)    // if string does not have enough data
        {
            if (i==0) // blank record - throw exceptions and do not create a record
            {
                String message = "There was a blank record in the file.\n" +
                        "This record will be ignored";

                Alert alert = new Alert( Alert.AlertType.ERROR );
                alert.setTitle( "Blank Record" );
                alert.setContentText( message );
                alert.showAndWait();

                throw new IOException("Blank Record");
            }

            for(; i<NUM_OF_FIELDS; i++)     // start at the current value of i
            {
                if(FIELD_INT[i])    // empty record has different values based on an integer or String field
                {
                    record[i]="0";
                }
                else
                {
                    record[i]="None";
                }
            }
            String message = "There was an error processing a record in the file.\n" +
                    "The record is incomplete.";

            Alert alert = new Alert( Alert.AlertType.ERROR );
            alert.setTitle( "Incomplete Data" );
            alert.setContentText( message );
            alert.showAndWait();
        }

        // check key and set next key to be bigger
        try
        {
            keyValue = Integer.parseInt( record[0] );
            if (keyValue>=nextKey)
            {
                nextKey = keyValue + 1;
            }
        }
        catch (NumberFormatException e)
        {
            // do nothing, just catch exception to continue
        }
    }

    /**
     * Constructor record that makes a deep copy of a CharacterRecord object
     * @param charRecord CharacterRecord: The object to make a copy of
     */
    public CharacterRecord( CharacterRecord charRecord)
    {
        for (int i=0; i<getNumOfFields(); i++)
        {
            record[i]=charRecord.getFieldValue(i);
        }
        updateFieldLongest( charRecord );
    }

    /**
     * Constructor record that creates a CharacterRecord with the given key
     * with defaults=true, the other fields are set to "0" or "None"
     * with defaults=false, the other fields are set to ""
     *
     * @param key String: Value used to set the CharacterRecord key
     * @return CharacterRecord: A CharacterRecord with a key and otherwise default or blank fields
     */
    public CharacterRecord( String key, boolean defaults)
    {
        record[0]=key;
        if (defaults)
        {
            for(int i=1; i<NUM_OF_FIELDS; i++)
            {
                if(FIELD_INT[i])    // empty record has different values based on an integer or String field
                {
                    record[i]="0";
                }
                else
                {
                    record[i]="None";
                }
            }
        }
        else
        {
            for(int i=1; i<NUM_OF_FIELDS; i++)
            {

                    record[i]="";
            }
        }
    }

    /**
     * Accessor method to get the number of fields in a record
     * @return int: The number of fields in a record (20)
     */
    public static int getNumOfFields()
    {
        return NUM_OF_FIELDS;
    }

    /**
     * Atatic ccessor method to get the field label for a position
     * @param index int: The position in the FIELD_LABELS array
     * @return String: The label for the index position
     */
    public static String getFieldLabel(int index)
    {
        String label="Out of Bounds";
        if(index>=0 && index<NUM_OF_FIELDS)
        {
            label = FIELD_LABELS[index];
        }
        return label;
    }

    /**
     * Static accessor method to get the shortened field label for a position
     * @param index int: The position in the FIELD_LABELS array
     * @return String: The shorter label for the index position
     */
    public static String getFieldShort(int index)
    {
        String label="Out of Bounds";
        if(index>=0 && index<NUM_OF_FIELDS)
        {
            label = FIELD_SHORT[index];
        }
        return label;
    }

    /**
     * Static accessor method to get the next available valid key
     * The key should be bigger than that in all the records currently created
     *
     * @return String: The next available key
     */
    public static String getNextKey()
    {
        return Integer.toString( nextKey );
    }

    /**
     * Accessor method to get the value of a position in the record
     * @param index int: Index position in the record to get the value of
     * @return String: The value at that position
     */
    public String getFieldValue(int index)
    {
        String label="Out of Bounds";
        if(index>=0 && index<NUM_OF_FIELDS)
        {
            label = record[index];
        }
        return label;
    }

    public static boolean isFieldTypeInt(int index)
    {
        boolean isInt=false;
        if(index>=0 && index<=getNumOfFields())
        {
            isInt = FIELD_INT[index];
        }
        return isInt;
    }

    /**
     * Accessor method to get the key of a record - in position 0
     * @return String: The key of a record
     */
    public String getKey()
    {
        return record[0];
    }

    /**
     * Method that will search to find if the given label is one of the fields in a record and returns that index
     * @param label String: A label to search for in the field label constant arrays
     * @return int: The index of that label if found, otherwise -1
     */
    public static int getLabelIndex(String label)
    {
        int index=-1;
        boolean found = false;

        for(int i=0; i<NUM_OF_FIELDS && !found; i++)
        {
            // check if label in label lists
            if(label.equals( FIELD_LABELS[i] ) || label.equals( FIELD_SHORT[i] ))
            {
                index=i;
                found=true;
            }
        }
        return index;
    }

    /**
     * Mutator method to set the value of a position in the record
     * @param index int: The index position in the record to set
     * @param str String: The value to set at that position
     * @return boolean: Returns true if a value was set, false otherwise
     */
    public boolean setFieldValue(int index, String str)
    {
        boolean setValue=false;

        if(index>=0 && index<NUM_OF_FIELDS)
        {
            record[index]= str;
            setValue = true;
        }
        return setValue;
    }

    /**
     * Mutator method to set the value of a position in the record
     * @param fieldLabel String: A String representing the field label of the position to set
     * @param str String: The value to set at that position
     * @return boolean: Returns true if a value was set, false otherwise
     */
    public boolean setFieldValue(String fieldLabel, String str)
    {
        boolean setValue=false;
        int index=getLabelIndex( fieldLabel );

        if(index>=0 && index<NUM_OF_FIELDS)
        {
            record[index]= str;
            setValue = true;
        }
        return setValue;
    }

    /**
     * method to adjust the longest field lengths when a record is encountered.
     * @param rec The record check to update longest field lengths
     */
    public static void updateFieldLongest(CharacterRecord rec)
    {
        String str="";                      // used to get field values to check for length
        for (int i=0; i<NUM_OF_FIELDS; i++)
        {
            str = rec.getFieldValue( i );
            if(str.length()>fieldLongest[i])
            {
                fieldLongest[i]=str.length();
            }
        }
    }

    /**
     * Method that initializes the fieldLongest array - which keeps track of the longest
     */
    public static void initializeFieldLongest()
    {
        for (int i=0; i<NUM_OF_FIELDS; i++)
        {
            fieldLongest[i]=FIELD_SHORT[i].length();
        }
        fieldLongestInitialized=true;
    }

    /**
     * Method that resets the length of longest field records and nextKey field
     * when dealing with new CharacterData
     */
    public static void resetRecordFields()
    {
        initializeFieldLongest();
        nextKey=1;
    }

    /**
     * Method to convert the data in the CharacterRecord object to a String
     * @return String: A string representing the CharacterRecord object information
     */
    @Override
    public String toString()
    {
        String str=record[0];

        for (int i=0; i<NUM_OF_FIELDS-1; i++)
        {
            str += "," + record[i + 1];
        }

        return str;
    }

    /**
     * Method to convert the data in the CharacterRecord object to a String, formatted so that each field is
     * the same length as the longest item in the field for easier reading
     * @return String: A formatted, easier to read string representing the CharacterRecord object information
     */
    public String toStringFormatted()
    {
        String str= String.format( "%"+fieldLongest[0]+"s", record[0] );
        for (int i=0; i<NUM_OF_FIELDS-1; i++)
        {
            str += "," + String.format( "%"+fieldLongest[i+1]+"s",  record[i + 1]);
        }
        return str;
    }

    /**
     * Method to convert the data in the CharacterRecord object to a String, formatted so that each field is
     * the same length as the longest item in the field for easier reading
     * @return String: A formatted, easier to read string representing the CharacterRecord object information
     */
    public static String toStringLabelsFormatted()
    {
        if(!fieldLongestInitialized)
        {
            initializeFieldLongest();
        }

        String str= String.format( "%"+fieldLongest[0]+"s", FIELD_SHORT[0] );
        for (int i=0; i<NUM_OF_FIELDS-1; i++)
        {
            str += "," + String.format( "%"+fieldLongest[i+1]+"s",  FIELD_SHORT[i + 1]);
        }
        return str;
    }

}





