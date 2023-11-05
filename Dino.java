import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Dino is the thing that actually plays the game. It sets the game up by creating a Game object, then runs and jumps
 * if it sees obstacles. It has a public static variable robot that we will use in our methods. It also has a jump
 * method and an isObstacle method.
 */
public class Dino
{
    private final Robot ROBOT;
    private final int START_JUMP_WIDTH;
    private final int END_JUMP_WIDTH;
    private final double TIME_PERIOD;

    public Dino() throws AWTException
    {
        Random rand = new Random();
        ROBOT = new Robot();

        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        END_JUMP_WIDTH = rand.nextInt(width / 2, width);
        START_JUMP_WIDTH = rand.nextInt(END_JUMP_WIDTH);

        TIME_PERIOD = rand.nextDouble(60);
    }

    public Dino(int startJumpWidth, int endJumpWidth, int timePeriod) throws AWTException
    {
        ROBOT = new Robot();

        this.START_JUMP_WIDTH = startJumpWidth;
        this.END_JUMP_WIDTH = endJumpWidth;
        this.TIME_PERIOD = timePeriod;
    }


    public int getEND_JUMP_WIDTH()
    {
        return END_JUMP_WIDTH;
    }


    public int getSTART_JUMP_WIDTH()
    {
        return START_JUMP_WIDTH;
    }


    public double getTIME_PERIOD()
    {
        return TIME_PERIOD;
    }


    /**
     * Presses the space key.
     */
    private void jump()
    {
        ROBOT.keyPress(KeyEvent.VK_SPACE);
    }


    /**
     * Returns whether the game is over or not. It does this by looking at if the retry button that pops up when you
     * die is there or not. It compares the background color to something it knows where the retry button is. If these
     * are the same then it is not over. If it is different then it is over.
     *
     * @return true if the game is over and false if it is not over
     */
    public boolean isOver(Game game)
    {
        int retryButton = ROBOT.getPixelColor(Toolkit.getDefaultToolkit().getScreenSize().width / 2, game.getTopOfRetry()).getRGB();
        int background = ROBOT.getPixelColor(Toolkit.getDefaultToolkit().getScreenSize().width / 2, game.getTopOfRetry() - 30).getRGB();


        // Unfortunately clouds exist, so we can't do exactly unequal. Luckily the difference between the retry button and the background
        // is greater than the clouds
        return retryButton - background > 100;
    }


    /**
     * Does the actual running of the dino in the game.
     *
     * @param game What game are we playing
     * @throws IOException From jumping
     */
    public void run(Game game) throws IOException
    {
        int jumpWidth = START_JUMP_WIDTH;

        BufferedImage image = game.screenshot(jumpWidth);


        int count = 0;

        // While the game is not over we will keep running
        while (!isOver(game))
        {
//            System.out.println(jumpWidth);

            image = game.screenshot(jumpWidth);

            // If there is an obstacle in the image then jump!
            if (game.isObstacle(image))
            {
                jump();
            }


            // If we have reached the time period, then increment it
            if (count % TIME_PERIOD == 0)
            {
                jumpWidth++;

                // If this goes over, bring it back to the end jump width
                if (jumpWidth > END_JUMP_WIDTH)
                {
                    jumpWidth = END_JUMP_WIDTH;
                }
            }
            count++;
        }
    }
}
