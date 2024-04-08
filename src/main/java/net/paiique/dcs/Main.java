package net.paiique.dcs;

import com.jcraft.jsch.JSchException;
import net.paiique.dcs.setup.Keywords;
import net.paiique.dcs.setup.Local;
import net.paiique.dcs.setup.Sftp;
import net.paiique.dcs.util.TextFileUtils;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws URISyntaxException {

        System.out.println("DCS v1");
        Keywords keywords = new Keywords();
        List<String> positiveKeywords = keywords.getKeys();
        List<String> contraKeywords = keywords.getContraKeys();

        Scanner reader = new Scanner(System.in);
        System.out.println("(1) - Local");
        System.out.println("(2) - SFTP (Password)");
        System.out.print("Select a option: ");

        switch (reader.nextInt()) {
            case 1:
                if (new Local().execute(false, positiveKeywords, contraKeywords)) {
                    System.exit(0);
                }
                break;

            case 2:
               if (new Sftp().execute(false, positiveKeywords, contraKeywords)) {
                   System.exit(0);
               }
               break;

            default:
                System.out.println("Invalid selection.");
                main(args);
        }
        main(args);
    }
}