package net.paiique.dcs.setup;

import com.google.gson.*;
import net.paiique.dcs.Main;
import net.paiique.dcs.util.TextFileUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Keywords {

    public List<String> getKeys() {
        System.out.println("Loading keywords from DCS API");
        return requestKeywords("http://vps.paiique.net:25576/keywords");
    }

    public List<String> getContraKeys() {
        System.out.println("Loading Contra-keywords from DCS API");
        return requestKeywords("http://vps.paiique.net:25576/contra");
    }


    private List<String> requestKeywords(String uri) {
        List<String> keys = new ArrayList<>();

        try {
            URLConnection request = new URL(uri).openConnection();
            request.connect();
            InputStreamReader inputStream = new InputStreamReader((InputStream) request.getContent());

            JsonElement parser = JsonParser.parseReader(inputStream);

            JsonArray jsonArray = parser.getAsJsonArray();

            jsonArray.forEach(jsonElement -> {
                String key = jsonElement.getAsJsonObject().get("keyword").getAsString();
                keys.add(key);
            });

            return keys;
        } catch (IOException e) {
            System.out.println("Error while loading keywords from DCS API");
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
