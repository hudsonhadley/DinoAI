import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * Runner is the thing that actually plays the game. It sets the game up by creating a Game object, then runs and jumps
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

        this.END_JUMP_WIDTH = endJumpWidth;
        this.START_JUMP_WIDTH = startJumpWidth;
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
     * Returns whether the game is over or not. It does this by counting how many black pixels are around the dino's
     * head. If there are a lot (i.e. over 150) then it is alive. If there are not a lot (i.e. under 150) then it is
     * dead.
     *
     * @return true if the game is over and false if it is not over
     */
    public boolean isOver(Game game)
    {
//        System.out.println(ROBOT.getPixelColor(game.getGameOverX(), game.getGameOverY()).getRGB() + " " + game.getGameOverColor());
        return ROBOT.getPixelColor(game.getGameOverX(), game.getGameOverY()).getRGB() == game.getGameOverColor();
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
