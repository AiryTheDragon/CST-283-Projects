/************************************************************************************************
 *  This class implements the functionality of a die
 *
 *  Images are loaded for the die images, including blank and questions, and the die can be rolled,
 *  set to certain values and resized
 *
 *  CST 283 Programming Assignment2
 *  @author Michael Clinesmith
 ***********************************************************************************************/

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Random;

public class Die
{
    // static fields for loading images - so images don't have to be loaded multiple times
    private static String diceImageName[] = {"Die0.png", "Die1.png", "Die2.png", "Die3.png", "Die4.png",
                                            "Die5.png", "Die6.png", "DieQ.png"};
    private static Image dieImage[] = new Image[8];
    private static ImageView dieImageView[] = new ImageView[8];
    private static boolean imagesLoaded=false;

    private static final int SIDES=6;
    private int value;
    private int size;

    /**
     * No-argument constructor for a die
     * It calls a method to load dice images if they are not yet loaded
     * It will roll the dice to set a random value
     */
    public Die()
    {
        value = 0;
        size = 100;
        if (!imagesLoaded)
        {
            loadImages();
        }
        roll();
    }

    /**
     * Constructor that sets the die to a specific value
     * It calls a method to load dice images if they are not yet loaded
     * @param val int: The value to set the die
     */
    public Die(int val)
    {
        value = val;
        size = 100;
        if (!imagesLoaded)
        {
            loadImages();
        }
    }

    /**
     * Method to load images for the die if they are not yeat loaded
     * @return boolean: true if the images were successfully loaded, false if not
     */
    public boolean loadImages()
    {
        int i=0;

        try
        {
            for (i = 0; i < 8; i++)
            {
                dieImage[i] = new Image( "file:" + diceImageName[i] );
                dieImageView[i] = new ImageView( dieImage[i] );
            }
            imagesLoaded = true;
        }
        catch (Exception e)      // problem with loading images
        {
            System.out.println( "Images could not be loaded.\ni="+i );
            System.out.println( e.getClass() );
            imagesLoaded = false;
        }

        return imagesLoaded;
    }

    /**
     * Method to get an image of the die
     * @return ImageView: The image of the die for the given value
     */
    public ImageView getImageView()
    {
        ImageView aImageView;
        int val=value;
        if (val<0 || val>6)
        {
            val=7;
        }
        aImageView = new ImageView(dieImage[val]);
        aImageView.setFitWidth( size );
        aImageView.setPreserveRatio( true );
        return aImageView;
    }

    /**
     * Method to get the value of the die
     * @return int: The value of the die
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Method to set the value of the die
     * @param value int: The value the die will be set to
     */
    public void setValue( int value )
    {
        this.value = value;
    }

    /**
     * Method to get the current setting of the die size
     * @return int: The current set size
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Method to set the size of the displayed die when returning an ImageView
     * @param size int: The size to set an ImageView object to
     */
    public void setSize(int size)
    {
        this.size = size;
    }

    /**
     * Method that rolls the die to a random number from 1 to SIDES
     */
    public void roll()
    {
        Random randomNumbers = new Random();
        value=randomNumbers.nextInt(SIDES)+1;
    }

}

