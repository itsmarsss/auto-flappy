package autoflappy;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class AutoFlappy {

    static final String version = "1.0.0 Alpha";

    public void run() {
        System.out.println("    _         _        _____ _                         ");
        System.out.println("   / \\  _   _| |_ ___ |  ___| | __ _ _ __  _ __  _   _ ");
        System.out.println("  / _ \\| | | | __/ _ \\| |_  | |/ _` | '_ \\| '_ \\| | | |");
        System.out.println(" / ___ \\ |_| | || (_) |  _| | | (_| | |_) | |_) | |_| |");
        System.out.println("/_/   \\_\\__,_|\\__\\___/|_|   |_|\\__,_| .__/| .__/ \\__, |");
        System.out.println("                                    |_|   |_|    |___/");
        System.out.println("--------------------------------------------------");
        System.out.println("   =========== PROGRAM SOURCE CODE =========");
        System.out.println("   = https://github.com/itsmarsss/AutoFlappy =");
        System.out.println("   =========================================");
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

            if(note.toString().equals("Author's Note: "))
                note = new StringBuilder();

        }catch(Exception e) {
            return "Unable to check for version and creator's note";
        }
        if(!newest.equals(version)) {
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
