/************************************************************************************************
 *  This class handles the processing of a time object
 *
 *  It stores the fields of hours, minutes and seconds and also will format the the date in several ways
 *  It allows comparisons between times
 *  --Corrected bug causing hour to display 0 in 12 hour time setting
 *
 *  CST 283 Programming Assignment 1
 *  @author Michael Clinesmith
 ***********************************************************************************************/

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class Times
{
    private int hour, minute;
    private double second;

    /**
     * No-argument constructor to create a time object
     * Sets time to 00:00:00.0
     */

    public Times()
    {
        hour = 0;
        minute = 0;
        second = 0.0;
    }

    /**
     * Constructor to create a time object given hours, minutes and seconds
     * @param h int: the hour for the time
     * @param m int: the minutes for the time
     * @param s double: the seconds for the time (including fractional part)
     */
    public Times(int h, int m, double s)
    {
        hour = h;
        minute = m;
        second = s;
    }

    /**
     * Constructor to create a time object given hours, minutes and seconds
     * @param h int: the hour for the time
     * @param m int: the minutes for the time
     * @param s int: the seconds for the time
     */
    public Times(int h, int m, int s)
    {
        hour = h;
        minute = m;
        second = s;
    }

    /**
     * Constructor to create a time object from a string in the format of HH:MM:SS.SSS
     * @param stringTime String: the time in the format of HH:MM:SS.SSS
     */
    public Times(String stringTime)
    {
        StringTokenizer timeTokens;
        try
        {
            timeTokens = new StringTokenizer( stringTime, ":" );
            hour = Integer.parseInt( timeTokens.nextToken() );
            minute = Integer.parseInt( timeTokens.nextToken() );
            second = Double.parseDouble( timeTokens.nextToken() );
        }
        catch (NumberFormatException | NoSuchElementException e)    // if exceptions in running out of tokens or bad format
        {
            // set to default time
            hour = 0;
            minute = 0;
            second = 0.0;
        }
    }

    /**
     * Constructor that will take a Times object and make a deep copy of it
     * @param times
     */
    public Times(Times times)
    {
        hour = times.getHour();
        minute = times.getMinute();
        second = times.getSecond();
    }

    /**
     * Accessor method to get the hours
     * @return int: hour in the time object
     */
    public int getHour()
    {
        return hour;
    }

    /**
     * Accessor method to get the minutes
     * @return int: minutes in the time object
     */
    public int getMinute()
    {
        return minute;
    }

    /**
     * Accessor method to get the seconds
     * @return double: seconds in the time object, including decimal part
     */
    public double getSecond()
    {
        return second;
    }

    /**
     * Method to get the seconds but only the integer part
     * @return int: the seconds in the time object (truncated)
     */
    public int getSecondInt()
    {
        return (int) (second);
    }

    /**
     * Method to get the hour using standard 12 hour format 12:00 AM - 11:59 PM
     * @return int: the hour going by standard 12 hour format (1 to 12), returns -1 if invalid
     */
    public int getHour12()
    {
        int hr = -1;
        if(isValid())
        {
            hr = (hour+11) % 12 + 1;        // corrected bug
        }
        return hr;
    }

    /**
     * Method to get if the time is AM or PM using standard 12 hour format
     * @return String: "AM" or "PM", or "NA" if invalid
     */
    public String getAMPM()
    {
        String AMPM="NA";

        if (isValid())
        {
            if (hour<12)
            {
                AMPM = "AM";
            }
            else
            {
                AMPM = "PM";
            }
        }

        return AMPM;
    }

    /**
     * Mutator method to set the hours in the time object
     * @param hour int: the hour to set in the time object
     */
    public void setHour( int hour )
    {
        this.hour = hour;
    }

    /**
     * Mutator method to set the minutes in the time object
     * @param minute int: the minutes to set in the time object
     */
    public void setMinute( int minute )
    {
        this.minute = minute;
    }

    /**
     * Mutator method to set the seconds in the time object
     * @param second double: the seconds to set in the time object (can include decimal part)
     */
    public void setSecond( double second )
    {
        this.second = second;
    }

    /**
     * Method to determine if the values stored in the time object are valid
     * @return boolean: true if the values in the time object are all valid, false otherwise
     */
    public boolean isValid()
    {
        boolean valid = true;

        if ( hour<0 || hour>23 || minute<0 || minute>59 || second<0.0 || second>=60.0)
        {
            valid = false;
        }
        return valid;
    }

    /**
     * Method to return the values in the time object as a string object in
     * @return String: the time in the format hH:MM:SS XM with the h optional, "Invalid time" if the values were not valid
     */
    @Override
    public String toString()
    {
        String str = "Invalid time";
        if (isValid())
        {
            str = "" + getHour12() + ":" + MinutetoStringNR() + ":" + SecondtoStringNR() + " " + getAMPM();
        }
        return str;
    }

    /**
     * Method to return the values in the time object as a string object in the form requested by the instructor
     * @return String: the time in the format HHMMz
     */
    public String toModString()
    {
        String str = HourtoString() + MinutetoString() + "z";
        return str;
    }

    /**
     * Method to return true if when rounding to the nearest second, the minutes should be rounded up
     * @return boolean: true if seconds are being rounded from 59 to 00, false if not
     */
    public boolean roundMinuteUp()
    {
        boolean roundUp = false;
        if (second >= 59.5)
        {
            roundUp = true;
        }
        return roundUp;
    }

    /**
     * Method to return true if when rounding to the nearest second, the hours should be rounded up
     * @return boolean: true if rounding minutes and seconds from 59:59 to 00:00, false if not
     */
    public boolean roundHourUp()
    {
        boolean roundUp = false;
        if (second >= 59.5 && minute == 59)
        {
            roundUp = true;
        }
        return roundUp;
    }

    /**
     * Method to return true if when rounding to the nearest second, the day should be rounded up
     * @return boolean: true if rounding hours, minutes and seconds from 23:59:59 to 00:00:00, false if not
     */
    public boolean roundDayUp()
    {
        boolean roundUp = false;
        if (second >= 59.5 && minute == 59 && hour == 23)
        {
            roundUp = true;
        }
        return roundUp;
    }

    /**
     * Method to convert minutes to a String to assist in printing the time in the format requested by the instructor
     * It rounds to the nearest minute, with the exception that it will not round 23:59 to 00:00, causing the
     * date to switch
     * @return String: A string representing the minutes in the time, rounded if necessary; NA is returned if time invalid
     */
    public String MinutetoString()
    {
        String str = "NA";                  // code for invalid time
        int tempMin = minute;
        if (isValid())
        {
            if( second>=30.0 )             // check if closer to next minute
            {
                tempMin++;
            }

            if ( tempMin==60 )              // check if rounding caused overflow to next hour
            {
                tempMin = 0;
                if (hour==23)
                {
                    tempMin = 59;           // do not roll over minute if it would cause day to roll over
                }

            }

            if ( tempMin>9 )                // minutes is two digits
            {
                str = Integer.toString( tempMin );
            }
            else                            // add "0" to beginning of minutes
            {
                str = "0" + Integer.toString( tempMin );
            }
        }
        return str;
    }

    /**
     * Method to convert minutes to a String
     * This method does No Rounding - NR
     * @return String: A string representing the minutes in the time, NA is returned if time invalid
     */
    public String MinutetoStringNR()
    {
        String str = "NA";                  // code for invalid time
        if (isValid())
        {
            if ( minute>9 )                // minutes is two digits
            {
                str = Integer.toString( minute );
            }
            else                            // add "0" to beginning of minutes
            {
                str = "0" + Integer.toString( minute );
            }
        }
        return str;
    }
    /**
     * Method to convert seconds to a String
     * This method does No Rounding - NR
     * @return String: A string representing the seconds in the time, NA is returned if time invalid
     */
    public String SecondtoStringNR()
    {
        String str = "NA";                  // code for invalid time
        int sec = getSecondInt();           // get integer value for second
        if (isValid())
        {
            if ( sec>9 )                    // sec is two digits
            {
                str = Integer.toString( sec );
            }
            else                            // sec is one digit so add "0" to beginning of sec
            {
                str = "0" + Integer.toString( sec );
            }
        }
        return str;
    }


    /**
     * Method to convert hours to a String to assist in printing the time in the format requested by the instructor
     * It rounds to the nearest minute, with the exception that it will not round 23:59 to 00:00, causing the
     * date to switch
     * @return String: A string representing the hours in the time, rounded if necessary; NA is returned if time invalid
     */
    public String HourtoString()
    {
        String str = "NA";                  // code for invalid time
        int tempHours = hour;
        if (isValid())
        {
            // check if needs to round to next hour, but do not round if it will round to next day
            if( minute == 59 && second>=30.0 && hour != 23 )
            {
                tempHours++;
            }

            if ( tempHours>9 )                  // minutes is two digits
            {
                str = Integer.toString( tempHours );
            }
            else                            // add "0" to minutes
            {
                str = "0" + Integer.toString( tempHours );
            }
        }
        return str;
    }

    /**
     * Method to check if two Times objects are equal
     * @param time Times: A Times object that is being compared to this object
     * @return boolean: true if the times are the same, false if not
     */
    public boolean isEqual(Times time)
    {
        boolean equal = false;
        if (hour == time.getHour() && minute == time.getMinute() && second == time.getSecond())
        {
            equal = true;
        }
        return equal;
    }

    /**
     * Method to compare two Times objects
     * @param time Times: A Times object that is being compared to this object
     * @return int: returns 1 if this object comes after the Times object parameter, -1 if it comes before, and 0 if they are equal
     */
    public int compareTo(Times time)
    {
        int compare = 0;                            // if none of if statements apply, then equal

        // set to 1 if this object is later, set to -1 if parameter object is later
        if (hour > time.getHour())                // compare hour first
        {
            compare = 1;
        }
        else if ( hour < time.getHour())
        {
            compare = -1;
        }
        else if( minute > time.getMinute())       // if hours the same check minutes
        {
            compare = 1;
        }
        else if ( minute < time.getMinute())
        {
            compare = -1;
        }
        else if ( second > time.getSecond())      // if hours and minutes the same, check seconds
        {
            compare = 1;
        }
        else if (second < time.getSecond())
        {
            compare = -1;
        }
        return compare;
    }

}
