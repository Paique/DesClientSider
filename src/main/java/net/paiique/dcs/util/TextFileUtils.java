package net.paiique.dcs.util;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TextFileUtils {
    public List<String> read(Path path) {

        try (InputStream inputStream = new FileInputStream(path.toFile())) {
            return getFileStrings(inputStream);
        } catch (SecurityException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<String> read(InputStream inputStream) {
        return getFileStrings(inputStream);
    }

    private List<String> getFileStrings(InputStream inputStream) {
        List<String> strings = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                strings.add(line);
            }
            return strings;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
