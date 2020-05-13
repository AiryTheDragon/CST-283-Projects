/************************************************************************************************
 *  This class handles the processing of a date object
 *
 *  It stores the fields of a month, day and year and also will format the the date is several ways
 *  It allows comparisons between dates
 *
 *  CST 283 Programming Assignment 1
 *  @author Michael Clinesmith
 ***********************************************************************************************/

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class Dates
{
    // date fields
    private int month, day, year;

    // String to save the month codes
    private final String[] MONTH_CODE = {"NA ", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL",
            "AUG", "SEP", "OCT", "NOV", "DEC" };
    private final String[] MONTH = {"Not Valid", "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"};
    /**
     * No-argument Constructor for a date object
     * Sets the date to be January 1, 1900
     */
    public Dates()
    {
        month = 1;
        day = 1;
        year = 1900;
    }

    /**
     * Constructor to set the date of a date object
     * @param m int: value representing the month
     * @param d int: value representing the day
     * @param y int: value representing the year
     */
    public Dates(int m, int d, int y)
    {
        month = m;
        day = d;
        year = y;
    }

    /**
     * Constructor that stores a date of the format YYYY-MM-DD
     * @param stringDate
     */
    public Dates(String stringDate)
    {
        stringDate = stringDate.trim();             // eliminates any extra whitespace
        StringTokenizer dateTokens;
        try
        {
            dateTokens = new StringTokenizer( stringDate, "-" );
            year = Integer.parseInt( dateTokens.nextToken() );
            month = Integer.parseInt( dateTokens.nextToken() );
            day = Integer.parseInt( dateTokens.nextToken() );
        }
        catch (NumberFormatException | NoSuchElementException e)
        {
            year = 1900;
            month = 1;
            day = 1;
        }
        // System.out.println( toModString() );

    }

    /**
     * Constructor that takes a Date object and makes a deep copy of it
     * @param dates
     */
    public Dates ( Dates dates)
    {
        year = dates.getYear();
        month = dates.getMonth();
        day = dates.getDay();
    }

    /**
     * Accessor method to get the date of a date object
     * @return int: value representing the day
     */
    public int getDay()
    {
        return day;
    }

    /**
     * Accessor method to get the month of a date object
     * @return int: value representing the month
     */
    public int getMonth()
    {
        return month;
    }

    /**
     * Accessor method to get the year of a date object
     * @return int: value representing the year
     */
    public int getYear()
    {
        return year;
    }

    /**
     * Method to get the 3-letter string for a month
     * @return String: a three letter code representing the month stored in date object
     */
    public String getMonthCode()
    {
        String code = MONTH_CODE[0];            // default invalid month code
        if ( month>0 && month < 13 )            // if month valid get code
        {
            code = MONTH_CODE[month];
        }
        return code;
    }


    /**
     * Method to get the 3-letter string for a month
     * @return String: a three letter code representing the month stored in date object
     */
    public String getMonthName()
    {
        String code = MONTH[0];            // default invalid month code
        if ( month>0 && month < 13 )            // if month valid get code
        {
            code = MONTH[month];
        }
        return code;
    }

    /**
     * Mutator method to set the date of a date object
     * @param day int: a value to set the day of the object
     */
    public void setDay( int day )
    {
        this.day = day;
    }
    /**
     * Mutator method to set the month of a date object
     * @param month int: a value to set the month of the object
     */

    public void setMonth( int month )
    {
        this.month = month;
    }
    /**
     * Mutator method to set the year of a date object
     * @param year int: a value to set the year of the object
     */

    public void setYear( int year )
    {
        this.year = year;
    }

    /**
     * Method checks to see if a saved day is a valid day
     *
     * @return boolean: true if the day is valid, false if it is not
     */
    public boolean isValid()
    {
        boolean valid = true;
        if ( year < 1)                      // must be a valid AD year
        {
            valid = false;
        }
        else if( month<1 || month>12 )      // must be a valid month
        {
            valid = false;
        }
        else if( day<1 || day>31 )          // days must be from 1 to 31
        {
            valid = false;
        }
        else if ( day == 31 && (month == 2 || month == 4 || month == 6 || month == 9 || month == 11)) // day cannot be 31 for these months
        {
            valid = false;
        }
        else if (day == 30 && month == 2)   // day cannot be 30 in February
        {
            valid = false;
        }
        else if (day == 29 && month ==2 && !isLeapYear() )  // day cannot be 29 in February if it is not a leap year
        {
            valid = false;
        }

        return valid;
    }

    /**
     * Method checks if a year is a leap year (for years greater than 0)
     * if the year is greater than 0
     * is divisible by 4
     * and is not divisible by 100 unless it is also divisible by 400
     * then they year is a leap year
     *
     * @return boolean: true if the year is a leap year, false otherwise
     */
    public boolean isLeapYear()
    {
        boolean leapYear = false;

        if ( year > 0 && year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))
        {
            leapYear = true;
        }
        return leapYear;
    }

    /**
     * Method to convert a date object to a string
     * @return String: a string representing the date object
     */
    @Override
    public String toString()
    {
        String string = getMonthName() + " " + getDay() + ", " + getYear();

        return string;
    }

    /**
     * Method to convert a date object to a string in the format requested by the instructor
     * @return String: a string representing the date object in the form DD MMM YY
     */
    public String toModString()
    {
        String string = dayToString() + " " + getMonthCode() + " " + yearToString();

        return string;
    }

    /**
     * Method to convert a day to a two character String
     * @return String: a two character String for the day or "NA" if the date was not volid
     */
    public String dayToString()
    {
        String string="NA";
        if (isValid())                                  // if date object is valid
        {
            if ( day > 9 )                              // if 2 digits in day convert to string
            {
                string = Integer.toString( day );

            }
            else                                        // add 0 in front of day
            {
                string = "0" + Integer.toString( day );
            }
        }
        return string;
    }

    /**
     * Method to convert a year to a two character String
     * @return String: a two character String for the year or "NA" if the date was not valid
     */
    public String yearToString()
    {
        String string="NA";
        int shortYear = year % 100;             // shortYear is from 0 to 99

        if (isValid())
        {
            if (shortYear<10)                   // shortYear has only 1 digit
            {
                string = "0" + Integer.toString( shortYear );
            }
            else                                // shortYear has 2 digits
            {
                string = Integer.toString( shortYear );
            }
        }

        return string;
    }

    /**
     * Method to check if two Dates objects are equal
     * @param date Dates: A Dates object that is being compared to this object
     * @return boolean: true if the dates are the same, false if not
     */
    public boolean isEqual(Dates date)
    {
        boolean equal = false;
        if (year == date.getYear() && month == date.getMonth() && day == date.getDay())
        {
            equal = true;
        }
        return equal;
    }

    /**
     * Method to compare two Dates objects
     * @param date Dates: A Dates object that is being compared to this object
     * @return int: returns 1 if this object comes after the Dates object parameter, -1 if it comes before, and 0 if they are equal
     */
    public int compareTo(Dates date)
    {
        int compare = 0;                        // if none of if statements apply, then equal

        // set to 1 if this object is later, set to -1 if parameter object is later
        if (year > date.getYear())              // compare year first
        {
            compare = 1;
        }
        else if ( year < date.getYear())
        {
            compare = -1;
        }
        else if( month > date.getMonth())       // if year the same check month
        {
            compare = 1;
        }
        else if ( month < date.getMonth())
        {
            compare = -1;
        }
        else if ( day > date.getDay())          // if year and month the same, check day
        {
            compare = 1;
        }
        else if (day < date.getDay())
        {
            compare = -1;
        }
        return compare;
    }

}


