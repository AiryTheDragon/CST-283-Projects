/************************************************************************************************
 *  This class handles the processing of a Location object
 *
 *  It was written to allow extra processing on the location data, but currently only has a string data object
 *
 *  CST 283 Programming Assignment 1
 *  @author Michael Clinesmith
 ***********************************************************************************************/
public class Locations
{

    String location;

    /**
     * No-argument constructor
     */
    public Locations()
    {
        location = "";
    }

    /**
     * Constructor that takes a String object
     * @param loc String: The String containing an object's general location
     */
    public Locations(String loc)
    {
        location = loc;
    }

    /**
     * Accessor method that returns the String containing an object's general location
     * @return String: The general location of a quake record
     */
    public String getLocation()
    {
        return location;
    }

    /**
     * Mutator method that sets a location
     * @param location String: A String containing a general location of a record
     */
    public void setLocation( String location )
    {
        this.location = location;
    }

    /**
     * Method that returns the data stored in the Location object
     * @return String: The String that states the general location
     */
    @Override
    public String toString()
    {
        return location;
    }

    /**
     * Method that checks if this Location object contains the same data as another
     * @param locations Location: A Location object to compare
     * @return boolean: true if the objects have the same description, false otherwise
     */
    public boolean isEqual( Locations locations)
    {
        boolean equal = false;
        if( location.equals( locations.getLocation()))
        {
            equal = true;
        }
        return equal;
    }
}
