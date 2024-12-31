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

    private static final String version = "3.4.7";

    private int flappyX = 487;

    private int pipeX = 743;
    private int checkPipeX = 400;

    private int topY = 460;
    private int bottomY = 1157;

    private int minY = 1057;

    private int flappyCR = 65;
    private int flappyCG = 41;
    private int flappyCB = 54;

    private int pipeOutCR = 255;
    private int pipeOutCG = 255;
    private int pipeOutCB = 255;

    private double colorTolerancePercent = 10;

    private int range = 400;
    private double targetPercent = 0.55;

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

    private Rectangle pipe;

    private Robot rb;

    private double target;

    private void startAutoFlappy() throws AWTException {
        rb = new Robot();
        Rectangle flappy = new Rectangle(flappyX, topY, 1, bottomY - topY);
        pipe = new Rectangle(pipeX - (range / 2), topY, range, bottomY - topY);

        do {
            BufferedImage checkPipe = rb.createScreenCapture(new Rectangle(checkPipeX, topY, 1, bottomY - topY));
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

            if (((flappyY > target) && (top > 0 && bottom > 0)) ||
                    (flappyY > minY)) {
                clickFlappy();
            }
        } while (true);
    }

    private void clickFlappy() {
        try {
            rb.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(100);
            rb.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            System.out.println("Clicked");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private int findFlappy(BufferedImage findFlappy) {
        for (int i = 0; i < bottomY - topY; i++) {
            int rgb = findFlappy.getRGB(0, i);
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb) & 0xFF;

            if (isColorWithinTolerance(r, g, b, flappyCR, flappyCG, flappyCB, colorTolerancePercent)) {
                System.out.println("Flappy at y = " + i + "\t Target at y = " + target);
                return i;
            }
        }
        return 0;
    }

    private boolean isColorWithinTolerance(int r1, int g1, int b1, int r2, int g2, int b2, double tolerancePercent) {
        double tolerance = 255 * (tolerancePercent / 100);

        return Math.abs(r1 - r2) <= tolerance &&
                Math.abs(g1 - g2) <= tolerance &&
                Math.abs(b1 - b2) <= tolerance;
    }

    private void updateTopBottom() {
        BufferedImage findPipe = rb.createScreenCapture(pipe);
        boolean foundTop = false;
        int x = 0;

        for (int i = 0; i < range; i++) {
            int rgb = findPipe.getRGB(i, 5);
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb) & 0xFF;
            if (!isColorWithinTolerance(r, g, b, pipeOutCR, pipeOutCG, pipeOutCB, colorTolerancePercent)) {
                x = i;
            }
        }

        for (int i = 0; i < bottomY - topY; i++) {
            int rgb = findPipe.getRGB(x, i);
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb) & 0xFF;

            if (!foundTop) {
                if (isColorWithinTolerance(r, g, b, pipeOutCR, pipeOutCG, pipeOutCB, colorTolerancePercent)) {
                    top = i;
                    foundTop = true;
                }
            } else {
                if (!isColorWithinTolerance(r, g, b, pipeOutCR, pipeOutCG, pipeOutCB, colorTolerancePercent)) {
                    bottom = i;
                    target = (bottom + (top - bottom) * targetPercent);
                    System.out.println("Top:Low bounds - " + top + ":" + bottom);
                    System.out.println("Target - " + target);
                    return;
                }
            }
        }
    }

    private void setupAutoFlappy() {
        flappyX = -1;
        pipeX = -1;
        checkPipeX = -1;
        topY = -1;
        bottomY = -1;
        minY = -1;
        range = -1;
        targetPercent = -1;
        flappyCR = -1;
        flappyCG = -1;
        flappyCB = -1;
        pipeOutCR = -1;
        pipeOutCG = -1;
        pipeOutCB = -1;
        colorTolerancePercent = -1;

        while (flappyX < 0) {
            System.out.println("Where to find flappy? (x coordinates):");
            flappyX = sc.nextInt();

        }

        while (pipeX < 0) {
            System.out.println("Where to find pipes? (x coordinates):");
            pipeX = sc.nextInt();
        }

        while (range < 0) {
            System.out.println("Range for finding pipes? (range):");
            range = sc.nextInt();
        }

        while (checkPipeX < 0) {
            System.out.println("Where to find passed pipes? (x coordinates):");
            checkPipeX = sc.nextInt();
        }

        while (topY < 0) {
            System.out.println("Game window highest? (y coordinates):");
            topY = sc.nextInt();
        }

        while (bottomY < 0) {
            System.out.println("Game window lowest? (y coordinates):");
            bottomY = sc.nextInt();
        }

        while (minY < 0) {
            System.out.println("Lowest allowed flappy? (y coordinates):");
            minY = sc.nextInt();
        }

        while (targetPercent < 0 || targetPercent > 100) {
            System.out.println("Target value, percentage from bottom to top? (percentage):");
            targetPercent = sc.nextDouble();
        }
        targetPercent /= 100;

        System.out.println("--------------------------------------------------");
        System.out.println("\t~ Look for Flappy at x = " + flappyX + " from y = " + topY + " to " + bottomY);
        System.out.println("\t~ Look for Pipes at x = " + pipeX + " from y = " + topY + " to " + bottomY + " with range " + range);
        System.out.println("\t~ Look for passed Pipes at x = " + checkPipeX + " from y = " + topY + " to " + bottomY);
        System.out.println("\t~ Keep Flappy above " + targetPercent + "% of the distance from bottom to top of pipes.");
        System.out.println("--------------------------------------------------");

        while (flappyCR < 0 || flappyCR > 255 ||
                flappyCG < 0 || flappyCG > 255 ||
                flappyCB < 0 || flappyCB > 255) {
            System.out.println("Flappy color? (r g b):");
            flappyCR = sc.nextInt();
            flappyCG = sc.nextInt();
            flappyCB = sc.nextInt();
        }

        while (pipeOutCR < 0 || pipeOutCR > 255 ||
                pipeOutCG < 0 || pipeOutCG > 255 ||
                pipeOutCB < 0 || pipeOutCB > 255) {
            System.out.println("Background color? (r g b):");
            pipeOutCR = sc.nextInt();
            pipeOutCG = sc.nextInt();
            pipeOutCB = sc.nextInt();
        }

        while (colorTolerancePercent < 0 || colorTolerancePercent > 100) {
            System.out.println("Color check tolerance (percentage):");
            colorTolerancePercent = sc.nextDouble();
        }

        System.out.println("--------------------------------------------------");
        System.out.println("\t~ Look for Flappy with color (" + flappyCR + ", " + flappyCG + ", " + flappyCB + ")");
        System.out.println("\t~ Look for Background with color (" + pipeOutCR + ", " + pipeOutCG + ", " + pipeOutCB + ")");
        System.out.println("\t~ Use color check tolerance of " + colorTolerancePercent + "%");
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
