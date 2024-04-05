package net.paiique.dcs.setup;

import net.paiique.dcs.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Local {
    private static final Logger LOGGER = Logger.getLogger(Local.class.getName());

    private List<String> listFilesUsingFilesList(Path modsPath) {

        File[] fileList = modsPath.toFile().listFiles();
        if (fileList == null) return null;

        List<String> modList = new ArrayList<>();

        for (File file : fileList) {
            modList.add(file.getName());
        }
        return modList;
    }

    public boolean execute() {
        Path path;
        Scanner reader = new Scanner(System.in);
        boolean alwaysRemove = false;

        System.out.print("Path to mods folder (Example: /home/paique/server/mods or C:\\Users\\paique\\server\\mods): ");
        path = Path.of(reader.next());

        if (Files.exists(path) && Files.isDirectory(path)) {
            List<String> mods = listFilesUsingFilesList(path);
            if (mods == null) {
                LOGGER.log(Level.SEVERE, "Error! The mod array is null!");
                System.out.println("Restarting...");
                return false;
            }

            List<String> keywords = Main.textFileUtils.read(Main.keywords.getKeywordFile());

            List<String> clientSideMods = new ArrayList<>();

            keywords.forEach(keyword -> mods.forEach(mod -> {
                if (mod.toLowerCase().contains(keyword) && mod.toLowerCase().endsWith(".jar")) {
                    clientSideMods.add(mod);
                }
            }));

            if (!clientSideMods.isEmpty()) {
                System.out.println(keywords.size() + " keywords loaded.");
                System.out.println(mods.size() + " mods detected.");


                System.out.println(clientSideMods.size() + " client-side mods detected!");
                System.out.println(clientSideMods);
                System.out.print("\nDisable mods? (y/n): ");
                String remove = reader.next();

                List<String> notRemoved = new ArrayList<>();
                if (remove.equalsIgnoreCase("y")) {
                    try {
                        Path clientFolder = Path.of(path + "/client");
                        if (!Files.exists(clientFolder)) Files.createDirectory(clientFolder);
                        for (String mod : clientSideMods) {
                            System.out.println("Moving to client folder: " + mod);
                            Path target = Path.of(clientFolder + "/" + mod);
                            Path source = Path.of(path + "/" + mod);
                            if (Files.exists(target)) {
                                if (!alwaysRemove) {
                                    System.out.print("File " + mod + " already exists in the client folder, delete it? (y/n/a) (a for all): ");
                                    remove = reader.next().toLowerCase();
                                }
                                if (remove.equals("a") || remove.equals("all")) alwaysRemove = true;
                                if (remove.equals("y") || remove.equals("yes") || alwaysRemove) {
                                    Files.delete(source);
                                    System.out.println("File " + mod + " deleted.");
                                } else {
                                    System.out.println("File not removed!!!");
                                    notRemoved.add(mod);
                                }
                                continue;
                            }
                            Files.move(source, target);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    if (notRemoved.isEmpty()) {
                        System.out.println("All detected client-side mods are moved to a new folder named client, or deleted if prompted.");
                    } else {
                        System.out.println(notRemoved.size() + " file(s) not removed:");
                        notRemoved.forEach(System.out::println);
                    }

                }
            } else {
                LOGGER.log(Level.SEVERE, "The directory is empty.");
            }
            return true;

        } else {
            LOGGER.log(Level.SEVERE, "The path " + path + " does not exist, or it's not a directory.");
            return false;
        }
    }

}
