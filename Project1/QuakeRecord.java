/************************************************************************************************
 *  This class handles the processing of a quake Record
 *
 *  It stores the date, time, latitude, longitude, richter and location is data types and numerous
 *  operations of the data records
 *
 *  CST 283 Programming Assignment 1
 *  @author Michael Clinesmith
 ***********************************************************************************************/
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class QuakeRecord
{
    private Dates date;
    private Times time;
    private double latitude, longitude, richter;
    private Locations location;

    /**
     * No-argument constructor creates a default location (at the north pole)
     */
    public QuakeRecord()
    {
        date = new Dates();
        time = new Times();
        latitude = 90.0;
        longitude = 0.0;
        richter = 0.0;
        location = new Locations();
    }

    /**
     * Constructor that takes a formatted string and uses it to create a QuakeRecord object
     * The string must be of the form:
     * yyyy-mm-ddThh:mm:ss.sssZ|(decimal from -90 to 90)|(decimal from -180 to 180)|(decimal from 4.0 to 10.0)|(string)
     *
     * if the string is not in the proper form, the constructor will throw an IOexception
     * @param stringRecord String: a String in the proper form
     */
    public QuakeRecord(String stringRecord) throws IOException
    {
        // work variables to parse data
        String dateAndTime, strLat, strLong, strRic;
        StringTokenizer stringData;

        try
        {
            stringData = new StringTokenizer( stringRecord, "|" );

            // Read all data from string
            dateAndTime = stringData.nextToken();
            strLat = stringData.nextToken();
            strLong = stringData.nextToken();
            strRic = stringData.nextToken();
            location = new Locations(stringData.nextToken());

            //process raw data as needed
            date = new Dates(dateAndTime.substring( 0, 10 ));
            time = new Times(dateAndTime.substring( 11, 23 ));
            latitude = Double.parseDouble( strLat );
            longitude = Double.parseDouble( strLong );
            richter = Double.parseDouble( strRic );
        }
        catch (NumberFormatException | NoSuchElementException e)
        {
            // give record default data then throw Exception
            date = new Dates();
            time = new Times();
            latitude = 90.0;
            longitude = 0.0;
            richter = 0.0;
            location = new Locations();

            throw new IOException( "Bad Data" );
        }

    }

    /**
     * Constructor that takes a QuakeRecord object and makes a deep copy of it
     * @param record
     */
    public QuakeRecord( QuakeRecord record)
    {
        date = new Dates(record.getDate());
        time = new Times(record.getTime());
        latitude = record.getLatitude();
        longitude = record.getLongitude();
        richter = record.getRichter();
        location = new Locations( record.getLocation() );
    }

    /**
     * Accessor method to get the Date object with the date
     * @return Dates: A new Dates object that contains the date of the quake
     */
    public Dates getDate()
    {
        return new Dates(date);
    }

    /**
     * Accessor method to get the Time object with the time
     * @return Times: A new Times object the contains the time of the quake
     */
    public Times getTime()
    {
        return new Times(time);
    }

    /**
     * Accessor method to get the latitude of the quake
     * @return double: The latitude of the quake
     */
    public double getLatitude()
    {
        return latitude;
    }

    /**
     * Accessor method to get the longitude of the quake
     * @return double: The longitude of the quake
     */
    public double getLongitude()
    {
        return longitude;
    }

    /**
     * Accessor method to get the magnitude of the quake
     * @return double: The magnitude of the quake
     */
    public double getRichter()
    {
        return richter;
    }

    /**
     * Accessor method to get the string data about the location of the quake
     * @return String: The location of the quake
     */
    public String getLocation()
    {
        return location.getLocation();
    }

    /**
     * Accessor method to get the year of the quake
     * @return int: The year of the quake
     */
    public int getYear()
    {
        return date.getYear();
    }

    /**
     * Accessor method to get the month of the quake
     * @return int: The month of the quake
     */
    public int getMonth()
    {
        return date.getMonth();
    }

    /**
     * Accessor method to get the day of the quake
     * @return int: The day of the quake
     */
    public int getDay()
    {
        return date.getDay();
    }

    /**
     * Accessor method to get the hour of the quake
     * @return int: The hour of the quake
     */
    public int getHour()
    {
        return time.getHour();
    }

    /**
     * Accessor method to get the minute of the quake
     * @return int: The minute of the quake
     */
    public int getMinute()
    {
        return time.getMinute();
    }

    /**
     * Accessor method to get the second (including decimal) of the quake
     * @return double: The second (including decimal) of the quake
     */
    public double getSecond()
    {
        return time.getSecond();
    }

    /**
     * Mutator method to set the date of the quake
     * @param date Dates: the date of the quake
     */
    public void setDate( Dates date )
    {
        this.date = new Dates(date);
    }

    /**
     * Mutator method to set the time of the quake
     * @param time Times: the time of the quake
     */
    public void setTime( Times time )
    {
        this.time = new Times(time);
    }

    /**
     * Mutator method to set the latitude of the quake
     * @param latitude double: the latitude of the quake
     */
    public void setLatitude( double latitude )
    {
        this.latitude = latitude;
    }

    /**
     * Mutator method to set the longitude of the quake
     * @param longitude double: the longitude of the quake
     */
    public void setLongitude( double longitude )
    {
        this.longitude = longitude;
    }

    /**
     * Mutator method to set the magnitude of the quake
     * @param richter double: The magnitude of the quake
     */
    public void setRichter( double richter )
    {
        this.richter = richter;
    }

    /**
     * Mutator method to set a String description of the location of the quake
     * @param loc String: A string representing the location of the quake
     */
    public void setLocation( String loc )
    {
        location.setLocation(loc);
    }

    /**
     * Mutator method to set the year of the quake
     * @param year int: The year of the quake
     */
    public void setYear( int year)
    {
        date.setYear( year );
    }

    /**
     * Mutator method to set the month of the quake
     * @param month int: the month of the quake
     */

    public void setMonth(int month)
    {
        date.setMonth( month );
    }

    /**
     * Mutator method to set the day of the quake
     * @param day int: the day of the quake
     */
    public void setDay(int day)
    {
        date.setDay( day );
    }

    /**
     * Mutator method to set the hour of the quake
     * @param hour int: the hour of the quake
     */
    public void setHour(int hour)
    {
        time.setHour( hour );
    }

    /**
     * Mutator method to set the minute of the quake
     * @param minute int: the minute of the quake
     */
    public void setMinute(int minute)
    {
        time.setMinute( minute );
    }

    /**
     * Mutator method to set the second of the quake
     * @param second double: the second of the quake (can contain a decimal part)
     */
    public void setSecond(double second)
    {
        time.setSecond( second );
    }

    /**
     * Method to check if the data stored in a QuakeRecord is valid
     * The method does not check the data stored in Location for validity
     *
     * @return boolean: true if the data is valid, false otherwise
     */
    public boolean isValid()
    {
        boolean valid = true;

        if (!date.isValid() || !time.isValid())             // checks if date or time is invalid
        {
            valid = false;
        }
        else if( latitude>90 || latitude<-90 )              // checks in latitude is in range
        {
            valid = false;
        }
        else if( longitude>180 || longitude<-180)           // checks in longitude is in range
        {
            valid = false;
        }
        else if (richter>10.0 || richter<4.0)               // checks if richter value is in range
        {
            valid = false;
        }

        return valid;
    }

    /**
     * Method to check if the record passed as a parameter contains the same data as this record
     * @param record QuakeRecord: A record saved in a QuakeRecord object
     * @return boolean: True if the records contain the same data, false if not
     */
    public boolean isEqual( QuakeRecord record)
    {
        boolean equal=false;
        //  check if equivalent parts of the records are equal
        if (date.isEqual( record.getDate() ) && time.isEqual( record.getTime() ) && latitude == record.getLatitude()
                && longitude == record.getLongitude() && richter == record.getRichter() &&
                location.getLocation().equals( record.getLocation() ))
        {
            equal = true;
        }

        return equal;
    }

    /**
     * Method to convert the data in the QuakeRecord object to a String
     * @return String: A string representing the QuakeRecord object information
     */
    @Override
    public String toString()
    {
        String str;

        str = "Date: " + date.toString() +
                "\tTime: " + time.toString() +
                "\tLatitude: " + latitude +
                "\tLongitude: " + longitude +
                "\tMagnitude: " + richter +
                "\tLocation: " + location.toString();
        return str;
    }

    /**
     * Method to convert the data in the QuakeRecord object to a String in the way the instructor prefers
     * @return String: A string representing the QuakeRecord object information
     */
    public String toModString()
    {
        String str;

        str = date.toModString() + " " +
                time.toModString() + ", " +
                latLongString() + ", \tMag: " +
                String.format( "%.1f" , richter) +
                "\t" + location.toString();

        return str;
    }

    /**
     * Method to create a displayable string representing the latitude and longitude in the QuakeRecord object
     * @return
     */
    public String latLongString()
    {
        String str;

        str = String.format( "(%.2f,%.2f)", latitude, longitude );
        return str;
    }
}
