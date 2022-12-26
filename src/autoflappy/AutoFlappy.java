package autoflappy;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class AutoFlappy {

    private static final String version = "1.0.0 Alpha";
    private int flappyX = 487;
    private int pipeX = 743;
    private int topY = 460;
    private int bottomY = 1157;
    private int flappyCR = 65;
    private int flappyCG = 41;
    private int flappyCB = 54;

    private int pipeOutCR = 255;
    private int pipeOutCG = 255;
    private int pipeOutCB = 255;


    private static final Scanner sc = new Scanner(System.in);

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
            System.exit(1);
        }


    }

    private void commandPrompt() throws AWTException {
        help();
        String input;
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
                    System.out.println("ByeBye");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Unknown option; [help] for list of options.");
            }
        }
    }

    private int top = -1;
    private int bottom = -1;

    private Rectangle flappy, pipe;

    private Robot rb;

    private double target;

    private void startAutoFlappy() throws AWTException {
        rb = new Robot();
        flappy = new Rectangle(flappyX, topY, 1, bottomY - topY);
        pipe = new Rectangle(pipeX-200, topY, 400, bottomY - topY);

        while (true) {
            BufferedImage checkPipe = rb.createScreenCapture(new Rectangle(400, topY, 1, bottomY - topY));
            int rgb = checkPipe.getRGB(0, 2);

            int a = (rgb >> 24) & 0xFF;
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb) & 0xFF;

            if (r != pipeOutCR && g != pipeOutCG && b != pipeOutCB) {
                updateTopBottom();
            }

            BufferedImage findFlappy = rb.createScreenCapture(flappy);
            int flappyY = findFlappy(findFlappy);

            if ((flappyY > target) && (top > 0 && bottom > 0)) {
                // if(System.currentTimeMillis()-lastClick >= 0) {
                clickFlappy();
                // }
            }
        }
    }

    private void clickFlappy() {
        try {
            rb.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(100);
            rb.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            System.out.println("Clicked");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int findFlappy(BufferedImage findFlappy) {
        for (int i = 0; i < bottomY - topY; i++) {
            int rgb = findFlappy.getRGB(0, i);
            int a = (rgb >> 24) & 0xFF;
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb) & 0xFF;
            if (r == flappyCR && g == flappyCG && b == flappyCB) {
                System.out.println("Flappy at y = " + i + "\t Target at y = " + target);
                return i;
            }
        }
        return 0;
    }

    private void updateTopBottom() {
        BufferedImage findPipe = rb.createScreenCapture(pipe);
        boolean foundTop = false;
        int x = 0;
        for(int i = 0; i < 400; i++){
            int rgb = findPipe.getRGB(i, 5);
            int a = (rgb >> 24) & 0xFF;
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb) & 0xFF;
            if(r != pipeOutCR || g != pipeOutCG || b != pipeOutCB) {
                x = i;
            }
        }
        for (int i = 0; i < bottomY - topY; i++) {
            int rgb = findPipe.getRGB(x, i);
            int a = (rgb >> 24) & 0xFF;
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb) & 0xFF;

            if (!foundTop) {
                if (r == pipeOutCR && g == pipeOutCG && b == pipeOutCB) {
                    top = i;
                    i += 50;
                    foundTop = true;
                }
            } else {
                if (r != pipeOutCR || g != pipeOutCG || b != pipeOutCB) {
                    bottom = i;
                    target = (bottom + (top - bottom) * (0.55));
                    System.out.println("Top:Low bounds - " + top + ":" + bottom);
                    System.out.println("Target - " + target);
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
        flappyCR = sc.nextInt();
        flappyCG = sc.nextInt();
        flappyCB = sc.nextInt();

        System.out.println("Background color? (r, g, b):");
        pipeOutCR = sc.nextInt();
        pipeOutCG = sc.nextInt();
        pipeOutCB = sc.nextInt();

        System.out.println("--------------------------------------------------");
        System.out.println("\t~ Look for Flappy with color (" + flappyCR + ", " + flappyCG + ", " + flappyCB + ")");
        System.out.println("\t~ Look for Background with color (" + pipeOutCR + ", " + pipeOutCG + ", " + pipeOutCB + ")");
        System.out.println("--------------------------------------------------");
    }

    private void help() {
        String help = "help\t- this menu" +
                "\n" +
                "setup\t- setup coordinates" +
                "\n" +
                "start\t- start flappy (read warning)" +
                "\n" +
                "quit\t- quit playing AutoFlappy :(";

        System.out.println(help);
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
