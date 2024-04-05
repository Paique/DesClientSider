package net.paiique.dcs.setup;

import net.paiique.dcs.Main;
import net.paiique.dcs.util.TextFileUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Keywords {

    private List<String> getDefaultKeys() {
        InputStream inputStream = Keywords.class
                .getClassLoader()
                .getResourceAsStream("badmods.txt");

        if (inputStream == null) throw new RuntimeException("Falied while getting client mods keywords.");

        return new TextFileUtils().read(inputStream);
    }

    public Path getKeywordFile() {
        try {
            String path = String.valueOf(Paths.get(Keywords.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile().getParentFile());

            Path keywordsPath = Path.of(path + "/keywords.txt");

            if (!Files.exists(keywordsPath)) {
                System.out.println("Generating Keywords file...");
                Files.createFile(keywordsPath);

                FileWriter writer = new FileWriter(keywordsPath.toFile());

                for (String key : getDefaultKeys()) {
                    writer.append(key).append("\n");
                }
                writer.close();
                return keywordsPath;
            }

            System.out.println("Keyword file detected!");
            return keywordsPath;

        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
