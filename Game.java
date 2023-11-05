import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Game is where setup occurs. The idea of creating a game class is that for different screens or setups, we will have
 * different games which use different things. This class ensures that not just one monitor can run this program.
 */
public class Game
{
    private final Robot ROBOT = new Robot();
    private int topOfDino;
    private int rightOfDino;
    private int bottomOfDino;
    private int topOfRetry;

    /**
     * Our constructor takes in one boolean to see if we need to set up or not. If it does need to set up, it will run the
     * setup method. If it doesn't, it will look to the PresetValues class and get values from there.
     * @param calibrate If we need to set up or not
     * @throws AWTException Because we use setup()
     * @throws InterruptedException Because we use setup()
     * // @throws IOException Because we use setup()
     */
    public Game(boolean calibrate) throws AWTException, InterruptedException //, IOException
    {
        if (calibrate)
        {
            setup();
        }

        else
        {
            topOfDino = PresetValues.TOP_OF_DINO;
            rightOfDino = PresetValues.RIGHT_OF_DINO;
            bottomOfDino = PresetValues.BOTTOM_OF_DINO;
            topOfRetry = PresetValues.TOP_OF_RETRY;
        }
    }

    public int getBottomOfDino()
    {
        return bottomOfDino;
    }
    public int getRightOfDino()
    {
        return rightOfDino;
    }
    public int getTopOfDino()
    {
        return topOfDino;
    }
    public int getTopOfRetry()
    {
        return topOfRetry;
    }
    public int getGameHeight()
    {
        return bottomOfDino - topOfDino;
    }

    /**
     * This takes a screenshot using the ROBOT.createScreenCapture method. We only need the width since x, y, and height
     * are already defined in this class.
     * @param width How wide do we want the capture to be
     * @return A buffered image of the screen
     * // @throws IOException Because of using saveImage sometimes
     */
    public BufferedImage screenshot(int width) throws IOException
    {
        Rectangle screen = new Rectangle(rightOfDino, topOfDino, width, getGameHeight());

        BufferedImage image = ROBOT.createScreenCapture(screen);

        saveImage(image, "src/image.png");

        return image;
    }


    /**
     * Saves an image to a file.
     * @param image A buffered image
     * @param fileName A file somewhere accessible from the current location
     * @throws IOException Since we use write
     */
    private void saveImage(BufferedImage image, String fileName) throws IOException
    {
        File file = new File(fileName);
        ImageIO.write(image, "png", file);
    }


    /**
     * This checks to see if there is an obstacle in the picture. It sets the background color to be some color in at the
     * top of the image and checks if there is any other colors in the image.
     *
     * @param image The image we want to process
     * @return if there is an obstacle or not
     */
    public boolean isObstacle(BufferedImage image)
    {
        // An obstacle will almost never be in this position
        int background = image.getRGB(0, 0);


        // We do 2 at a time to speed this process up. This is one of the most costly things we have to do, so anything
        // that will cut corners is good.

        // Go through the height (2 at a time)
        for (int i = 0; i < image.getHeight(); i += 2)
        {
            // Go through the width (2 at a time)
            for (int j = 0; j < image.getWidth(); j += 2)
            {
                if (image.getRGB(j, i) != background)
                {
                    return true;
                }
            }
        }

        return false;
    }




    /**
     * Setup writes to PresetValues.java and finds those values with help from the user.
     * @throws InterruptedException from Thread.sleep()
     */
    public void setup() throws InterruptedException
    {
        // Stores the string which we'll write
        String presetValuesString = "public class PresetValues\n{\n";

        Scanner scnr = new Scanner(System.in); // Need this to wait until the user is ready

        // Give instructions
        System.out.println("Setup will require the chrome dino game to be open. Once you have it open (chrome://dino),");
        System.out.println("run til you die. Once you are ready hit enter.");
        scnr.nextLine();



        System.out.println("For every location it asks for, move your cursor there, alt + tab back to this page, and");
        System.out.println("then hit enter. It will then record the mouse location. Do this for all locations. Hit enter");
        System.out.println("to continue.");
        scnr.nextLine();



        // First we need the y position of the mouse at the top of the dino's head
        System.out.println("Location: Top of the dino's head. Waiting for input to capture...");

        scnr.nextLine();
        topOfDino = getMousePosition().y;

        presetValuesString += "\tpublic static final int TOP_OF_DINO = " + topOfDino + ";\n";


        // Now we need to know the x position of the mouse at the rightmost part of the dino
        System.out.println("Location: Rightmost part of the dino's head. Waiting for input to capture...");

        scnr.nextLine();
        rightOfDino = getMousePosition().x + 10; // The dino has an outline that can mess up our search for an obstacle so move it a little bit more to the right to be sure

        presetValuesString +="\tpublic static final int RIGHT_OF_DINO = " + rightOfDino + ";\n";


        // Now we need to know the y position of the moues at the bottom of the dino's head
        System.out.println("Location: Bottom of the dino's head. Waiting for input to capture...");

        scnr.nextLine();
        bottomOfDino = getMousePosition().y;

        presetValuesString += "\tpublic static final int BOTTOM_OF_DINO = " + bottomOfDino + ";\n";



        // Now we need the y coordinate of the top of the retry button
        System.out.println("Location: Top of the retry button. Waiting for input...");

        scnr.nextLine();
        topOfRetry = getMousePosition().y + 10;

        presetValuesString += "\tpublic static final int TOP_OF_RETRY = " + topOfRetry + ";\n";


        System.out.println("Hit enter to continue to the game...");
        scnr.nextLine();

        presetValuesString += "}";

        createFile(presetValuesString, "src/PresetValues.java");
    }


    /**
     * Writes a string s to a file with fileName in the project
     * @param s The string we'll write
     * @param fileName The file in the project we want to write to
     */
    private void createFile(String s, String fileName)
    {
        try
        {
            FileWriter fileWriter = new FileWriter(fileName);

            fileWriter.write(s);
            fileWriter.close();

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Waits for input, then returns the mouse position
     * @return Current mouse position
     */
    private Point getMousePosition()
    {
        return MouseInfo.getPointerInfo().getLocation();
    }
}
