package autoflappy;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AutoFlappy {

    private static final String version = "1.0.0 Alpha";
    private int flappyX = 640;
    private int pipeX = 1360;
    private int topY = 100;
    private int bottomY = 2020;
    private Color flappyC = new Color(207, 194, 44);
    private Color pipeC = new Color(118, 194, 44);
    private Color pipeOutC = new Color(123, 197, 205);

    private static Scanner sc = new Scanner(System.in);

    public void run() {
        System.out.println("    _         _        _____ _                         ");
        System.out.println("   / \\  _   _| |_ ___ |  ___| | __ _ _ __  _ __  _   _ ");
        System.out.println("  / _ \\| | | | __/ _ \\| |_  | |/ _` | '_ \\| '_ \\| | | |");
        System.out.println(" / ___ \\ |_| | || (_) |  _| | | (_| | |_) | |_) | |_| |");
        System.out.println("/_/   \\_\\__,_|\\__\\___/|_|   |_|\\__,_| .__/| .__/ \\__, |");
        System.out.println("                                    |_|   |_|    |___/");
        System.out.println("--------------------------------------------------");
        System.out.println("   ============ PROGRAM SOURCE CODE ==========");
        System.out.println("   = https://github.com/itsmarsss/AutoFlappy =");
        System.out.println("   ===========================================");
        System.out.println("      Welcome to AutoFlappy's Control Prompt");
        System.out.println();
        System.out.println("Purpose: This program was make to automatically play Flappy bird for you since you're bad at it.");
        System.out.println();
        System.out.println("Note: This program does not have a kill switch so good luck.");
        System.out.println();
        System.out.println("Warning[1]: Use this program at your own risk, I (the creator of this program) will not be liable for any issues that this program causes to your Discord Server or computer (or sanity?)");
        System.out.println();
        System.out.println("Version:" + versionCheck());
        System.out.println();

        try {
            commandPrompt();
        } catch (AWTException e) {
            System.out.println("Error with robot class...");
            e.printStackTrace();
            System.exit(0);
        }


    }

    private void commandPrompt() throws AWTException {
        help();
        String input = "";
        while (true) {
            System.out.println();
            System.out.print("Option:");
            input = sc.next();
            sc.nextLine();
            switch (input) {
                case "help":
                    help();
                    break;
                case "setup":
                    setupAutoFlappy();
                    break;
                case "start":
                    startAutoFlappy();
                    break;
                case "quit":
                    quit();
                    break;
                default:
                    System.out.println("Unknown option; [help] for list of options.");
            }
        }
    }

    private void quit() {
    }

    private int top = -1;
    private int bottom = -1;

    private Rectangle flappy, pipe;

    private Robot rb;

    private int lastFlappy;

    private void startAutoFlappy() throws AWTException {
        rb = new Robot();
        flappy = new Rectangle(flappyX, topY, 1, bottomY - topY);
        pipe = new Rectangle(pipeX, topY, 1, bottomY - topY);
        lastFlappy = (bottomY-topY)/2;

        while (true) {
            BufferedImage checkPipe = rb.createScreenCapture(new Rectangle(540, topY, 1, bottomY - topY));
            int rgb = checkPipe.getRGB(0, 2);

            int a = (rgb >> 24) & 0xFF;
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb) & 0xFF;

            if (r == pipeC.getRed() && g == pipeC.getGreen() && b == pipeC.getBlue()) {
                updateTopBottom();
            }

            BufferedImage findFlappy = rb.createScreenCapture(flappy);
            int flappyY = findFlappy(findFlappy);

            if ((flappyY > (bottom + (top-bottom) * (3/10))) && (top > 0 && bottom > 0)) {
               // if(System.currentTimeMillis()-lastClick >= 0) {
                    clickFlappy();
               // }
            }
        }
    }

    private void clickFlappy() {
        try {
            rb.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(20);
            rb.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        } catch (Exception e) {
        }
    }

    private int findFlappy(BufferedImage findFlappy) {
        for (int i = lastFlappy-50; i < lastFlappy+50; i++) {
            int rgb = findFlappy.getRGB(0, i);
            int a = (rgb >> 24) & 0xFF;
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb) & 0xFF;
            if (r == flappyC.getRed() && g == flappyC.getGreen() && b == flappyC.getBlue()) {
                System.out.println("Flappy at y = "+ i);
                return i;
            }
        }
        return 0;
    }

    private void updateTopBottom() {
        BufferedImage findPipe = rb.createScreenCapture(pipe);
        boolean foundTop = false;
        for (int i = 0; i < bottomY - topY; i++) {
            int rgb = findPipe.getRGB(0, i);
            int a = (rgb >> 24) & 0xFF;
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb) & 0xFF;

            if (!foundTop) {
                if (r == pipeOutC.getRed() && g == pipeOutC.getGreen() && b == pipeOutC.getBlue()) {
                    top = i;
                    i += 50;
                    foundTop = true;
                }
            } else {
                if (r != pipeOutC.getRed() || g != pipeOutC.getGreen() || b != pipeOutC.getBlue()) {
                    bottom = i;
                    System.out.println("Top:Low bounds - " + top + ":" + bottom);
                    return;
                }
            }

        }
    }

    private void setupAutoFlappy() {
        flappyX = Integer.MIN_VALUE;
        pipeX = Integer.MIN_VALUE;
        topY = Integer.MIN_VALUE;
        bottomY = Integer.MIN_VALUE;

        while (flappyX < 0) {
            System.out.println("Where to find flappy? (x coordinates):");
            flappyX = sc.nextInt();

        }

        while (pipeX < 0) {
            System.out.println("Where to find pipes? (x coordinates):");
            pipeX = sc.nextInt();
        }

        while (topY < 0) {
            System.out.println("Game window highest? (y coordinates):");
            topY = sc.nextInt();
        }

        while (bottomY < 0) {
            System.out.println("Game window lowest? (y coordinates):");
            bottomY = sc.nextInt();
        }

        System.out.println("--------------------------------------------------");
        System.out.println("\t~ Look for Flappy at x = " + flappyX + " from y = " + topY + " to " + bottomY);
        System.out.println("\t~ Look for Pipes at x = " + pipeX + " from y = " + topY + " to " + bottomY);
        System.out.println("--------------------------------------------------");

        System.out.println("Flappy color? (r, g, b):");
        flappyC = new Color(sc.nextInt(), sc.nextInt(), sc.nextInt());

        System.out.println("Pipe color? (r, g, b):");
        pipeC = new Color(sc.nextInt(), sc.nextInt(), sc.nextInt());

        System.out.println("Pipe outline color? (r, g, b):");
        pipeOutC = new Color(sc.nextInt(), sc.nextInt(), sc.nextInt());

        System.out.println("--------------------------------------------------");
        System.out.println("\t~ Look for Flappy with color " + flappyC.toString());
        System.out.println("\t~ Look for Pipe with color " + pipeC.toString());
        System.out.println("\t~ Look for Pipe Outline with color " + pipeOutC.toString());
        System.out.println("--------------------------------------------------");
    }

    private void help() {
        StringBuilder help = new StringBuilder();
        help.append("help\t- this menu")
                .append("\n")
                .append("setup\t- setup coordinates")
                .append("\n")
                .append("start\t- start flappy (read warning)")
                .append("\n")
                .append("quit\t- quit playing AutoFlappy :(");

        System.out.println(help.toString());
    }

    static String versionCheck() {
        URL url;
        String newest;
        StringBuilder note = new StringBuilder("Author's Note: ");
        try {
            url = new URL("https://raw.githubusercontent.com/itsmarsss/AutoFlappy/main/newestversion");
            URLConnection uc;
            uc = url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            newest = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null)
                note.append(line).append("\n");

            if (note.toString().equals("Author's Note: "))
                note = new StringBuilder();

        } catch (Exception e) {
            return "Unable to check for version and creator's note";
        }
        if (!newest.equals(version)) {
            return "   [There is a newer version of AutoFlappy]" +
                    "\n\t##############################################" +
                    "\n\t   " + version + "(current) >> " + newest + "(newer)" +
                    "\nNew version: https://github.com/itsmarsss/AutoFlappy/releases" +
                    "\n\t##############################################" +
                    "\n" + note;
        }
        return " This program is up to date!" +
                "\n" + note;
    }

}
