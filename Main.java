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
        Scanner scnr = new Scanner(System.in);
        System.out.println("Do you need to setup the game? (y or n)");
        boolean setup = scnr.next().toLowerCase().charAt(0) == 'y';

        Game game = new Game(setup);

        System.out.println("Switch to dino game and place your mouse in the top left corner when ready!");

        while (mouseNotInCorner()); // Wait until mouse is in corner


        System.out.println("Looking for best start...");

        int bestStart = 0;
        long bestTime = 0;
        for (int i = 10; i < 500; i += 10)
        {
            System.out.println();
            System.out.println("i = " + i);
            System.out.println("bestTime = " + bestTime);
            System.out.println("bestStart = " + bestStart);
            start();
            long start = System.currentTimeMillis();
            Dino dino = new Dino(i, i, 10);
            dino.run(game);
            long end = System.currentTimeMillis();

            if (end - start > bestTime)
            {
                bestTime = end - start;
                bestStart = i;
            }

            if (end - start < bestTime)
            {
                break;
            }

            if (!mouseNotInCorner())
            {
                return;
            }
        }


        System.out.println("Looking for best increment period...");

        int bestI = 0;
        bestTime = 0;
        for (int i = 0; i < 100; i += 5)
        {
            System.out.println();
            System.out.println("i = " + i);
            System.out.println("bestTime = " + bestTime);
            System.out.println("bestI = " + bestI);
            start();
            long start = System.currentTimeMillis();
            Dino dino = new Dino(bestStart, Toolkit.getDefaultToolkit().getScreenSize().width, i);
            dino.run(game);
            long end = System.currentTimeMillis();

            if (end - start > bestTime)
            {
                bestTime = end - start;
                bestI = i;
            }

            if (end - start < bestTime)
            {
                break;
            }

            if (!mouseNotInCorner())
            {
                return;
            }
        }


        System.out.println("Looking for best end...");


        int bestEnd = 0;
        bestTime = 0;
        for (int i = 500; i < Toolkit.getDefaultToolkit().getScreenSize().width; i += 10)
        {
            System.out.println();
            System.out.println("i = " + i);
            System.out.println("bestTime = " + bestTime);
            System.out.println("bestEnd = " + bestEnd);
            start();
            long start = System.currentTimeMillis();
            Dino dino = new Dino(bestStart, i,  bestI);
            dino.run(game);
            long end = System.currentTimeMillis();

            if (end - start > bestTime)
            {
                bestTime = end - start;
                bestI = i;
            }

            if (end - start < bestTime)
            {
                break;
            }

            if (!mouseNotInCorner())
            {
                return;
            }
        }

        System.out.println(bestStart + " " + bestEnd + " " + bestI);

    }
}