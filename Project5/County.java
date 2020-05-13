/************************************************************************************************
 *  This class stores county FIPS code, state and population data.
 *
 *  It allows storage of state and national jurisdiction codes, names, populations and lists of subjurisdictions
 *  of those entities (for states and national codes)
 *
 *  CST 283 Programming Assignment 5
 *  Modified from CountyData.java - CST 183 Programming Assignment 7
 *  @author Michael Clinesmith
 ***********************************************************************************************/

public class County
{
    // class fields
    private String FIPScode;
    private String countyName, stateCode;

    private int population;
    private boolean stateEntity;
    private String[] subEntityList;
    private int subEntityCount;

    /**
     * No parameter constructor
     */
    public County()
    {
        FIPScode = "99999";
        countyName = "None";
        stateCode = "NA";
        population = 0;
        stateEntity = false;
        subEntityList = null;
        subEntityCount = 0;
    }

    /**
     * Constructor with parameters
     * @param code String: County FIPS code
     * @param county String: County name
     * @param state String: State code
     * @param popData int array: contains population data
     */
    public County(String code, String county, String state, int popData)
    {
        FIPScode = code;
        countyName = county;
        stateCode = state;
        population = popData;
        stateEntity = false;
        subEntityList = null;
        subEntityCount = 0;
    }

    /**
     * County copy constructor
     * @param county County: A County object to make a deep copy of
     */
    public County(County county)
    {
        FIPScode = county.getFIPScode();
        countyName = county.getCountyName();
        stateCode = county.getStateCode();
        population = county.getPopulation();
        stateEntity = county.isStateEntity();
        subEntityList = county.getSubEntityList();
        subEntityCount = county.getSubEntityCount();
    }

    /**
     * Mutator method to set the FIPS code
     * @param FIPScode String: FIPS code of a county
     */
    public void setFIPScode(String FIPScode)
    {
        this.FIPScode = FIPScode;
    }

    /**
     * Mutator method to set the county name
     * @param countyName String: A county name
     */
    public void setCountyName(String countyName)
    {
        this.countyName = countyName;
    }

    /**
     * Mutator method to set the state code
     * @param stateCode String: A state code representing the state a county is in
     */
    public void setStateCode(String stateCode)
    {
        this.stateCode = stateCode;
    }

    /**
     * Mutator method to set the county population
     * @param pop int: The population in a particular year
     */
    public void setPopulation(  int pop)
    {
        population = pop;
    }

    /**
     * Mutator method to set the StateEntity status
     * @param status boolean: A true or false value used to set stateEntity
     */
    public void setStateEntity( boolean status)
    {
        stateEntity = status;
    }

    /**
     * Mutator method to set a subEntityList
     * Setting a non null list will set the stateEntity status to true if neither stateEntity or nationEntity are true
     *
     * @param list String[]: A list used to set a state's or nation's subjurisdictions - it may be null
     * @param count int: The number of elements in the list
     */
    public void setSubEntityList( String[] list, int count)
    {
        if( count == 0)     // no elements in list - clue from user to remove it
        {
            subEntityList=null;
        }
        else
        {
            if (subEntityList == null || subEntityList.length < count)  // need to create a suitable sized list
            {
                if (count < 100)
                {
                    subEntityList = new String[100];
                } else
                {
                    subEntityList = new String[count + 50];
                }
            }

            for (int i = 0; i < count; i++)                             // create the list in the object
            {
                subEntityList[i] = list[i];
            }
            subEntityCount = count;
            if (!stateEntity)      // since a list now exists, set state flag
            {
                stateEntity = true;
            }
        }
    }

    /**
     * Method that adds a code to a jurisdiction's subEntityList
     * @param code String: A String representing a jurisdiction code
     */
    public void addToSubEntityList(String code)
    {

        if( subEntityCount==0 || subEntityCount==subEntityList.length)      // need to make a new list
        {
            String[] newList = new String[subEntityCount+20];
            // transfer elements to new list
            for(int i=0; i<subEntityCount; i++)
            {
                newList[i] = subEntityList[i];
            }
            newList[subEntityCount] = code;                                 // add new code
            subEntityCount++;
            subEntityList = newList;                                        // save new list as subEntityList
        }
        else
        {
            subEntityList[subEntityCount] = code;                           // add new code
            subEntityCount++;
        }
    }

    /**
     * Method that subtracts or removes a code from a jurisdiction's SubEntityList
     * @param code String: The code to remove from the list
     * @return boolean: true if the code was removed, false if not (i.e. it was not in the list)
     */
    public boolean subtractFromSubEntityList(String code)
    {
        boolean codeFound=false;
        for(int i=0; i<subEntityCount && !codeFound; i++)
        {
            if (subEntityList[i].equals( code ))
            {
                codeFound=true;
                subEntityCount--;
                subEntityList[i]=subEntityList[subEntityCount];
            }
        }
        return codeFound;
    }

    /**
     * Accessor method to get a county's FIPS code
     * @return String: The FIPS code of a county
     */
    public String getFIPScode()
    {
        return FIPScode;
    }

    /**
     * Accessor method to get a county's name
     * @return String: The name of a county
     */
    public String getCountyName()
    {
        return countyName;
    }

    /**
     * Accessor method to get a county's state code
     * @return String: The state code of a county
     */
    public String getStateCode()
    {
        return stateCode;
    }

    /**
     * Accessor method to get a county's population
     * @return int: the county's population in that year
     */
    public int getPopulation()
    {
        return population;
    }

    /**
     * Accessor method to get it an object has been set as a stateEntity
     * @return boolean: true if it is a stateEntity, false if not
     */
    public boolean isStateEntity()
    {
        return stateEntity;
    }

    /**
     * Accessor method to get the subEntityList
     * @return String[]: The subEntityList array or null if none exists
     */
    public String[] getSubEntityList()
    {
        String[] list = null;
        if(subEntityCount>0)
        {
            list = subEntityList.clone();
        }
        return list;
    }

    /**
     * Accessor method to get the number of elements in a subEntityList
     * @return int: The number of elements in the subEntityList
     */
    public int getSubEntityCount()
    {
        return subEntityCount;
    }

    /**
     * Method to return a string representing the data stored in a CountyData obuect
     * @return String: Contains the FIPS code, county name, state code, and population data of a CountyData object
     */
    @Override
    public String toString()
    {
        String data;

        if(stateEntity)
        {
            data = "FIPS Code: " + FIPScode +
                    "\nState Name: " + countyName +
                    "\nSubjurisdiction IDs:";
            for (int i=0; i<subEntityCount; i++)
            {
                data += "\n" + subEntityList[i];
            }
        }
        else
        {
            data = "FIPS Code: " + FIPScode +
                    "\nCounty Name: " + countyName +
                    "\nState: " + stateCode +
                    "\nPopulation: " + population;
        }
        return data;
    }

}
