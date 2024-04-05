package net.paiique.dcs;

import com.jcraft.jsch.JSchException;
import net.paiique.dcs.setup.Local;
import net.paiique.dcs.setup.Sftp;
import net.paiique.dcs.util.TextFileUtils;

import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static TextFileUtils textFileUtils;
    public static void main(String[] args) throws JSchException {

        textFileUtils = new TextFileUtils();

        System.out.println("DCS v1");

        Scanner reader = new Scanner(System.in);
        System.out.println("(1) - Local");
        System.out.println("(2) - SFTP (Password)");
        System.out.print("Select a option: ");

        switch (reader.nextInt()) {
            case 1:
                if (new Local().execute()) {
                    System.exit(0);
                }
                break;

            case 2:
               if (new Sftp().execute()) {
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