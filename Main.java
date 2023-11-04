import java.awt.*;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.Scanner;

public class Main
{
    /**
     * Start assumes the game is over and presses the middle of the screen to start the game again.
     * @throws AWTException from robot
     * @throws InterruptedException from sleep
     */
    public static void start() throws AWTException, InterruptedException
    {
        Robot robot = new Robot();

        robot.mouseMove(Toolkit.getDefaultToolkit().getScreenSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(100);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }


    public static boolean mouseNotInCorner()
    {
        Point mouse = MouseInfo.getPointerInfo().getLocation();
//        System.out.println(mouse.x + " " + mouse.y);

        return (mouse.x >= 10 || mouse.y >= 10);
    }


    public static void main(String[] args) throws IOException, InterruptedException, AWTException
    {
        int bestStart = 225;
        int bestEnd = 600;
        int bestTimePeriod = 30;

        
        Scanner scnr = new Scanner(System.in);
        System.out.println("Do you need to setup the game? (y or n)");
        boolean setup = scnr.next().toLowerCase().charAt(0) == 'y';

        Game game = new Game(setup);
        Dino dino = new Dino(bestStart, bestEnd, bestTimePeriod);

        System.out.println("Switch to dino game and place your mouse in the top left corner when ready!");    


        while (mouseNotInCorner()); // Wait until mouse is in corner
        long start = System.currentTimeMillis();
        start();

        dino.run(game);

        
        long time = System.currentTimeMillis() - start;
        time /= 1000;

        int hours = (int)time / 3600;
        time %= 3600;

        int minutes = (int)time / 60;
        time %= 60;

        int seconds = (int)time;

        System.out.println(hours + ":" + minutes + ":" + seconds);
    }
}
