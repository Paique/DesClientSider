package net.paiique.dcs;

import com.jcraft.jsch.JSchException;
import net.paiique.dcs.setup.Keywords;
import net.paiique.dcs.setup.Local;

import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.util.List;

public class Agent {
    public static void premain(String args, Instrumentation instrumentation) throws JSchException, URISyntaxException {
        Keywords keywords = new Keywords();
        List<String> positiveKeywords = keywords.getKeys();
        List<String> contraKeywords = keywords.getContraKeys();

        new Local().execute(true, positiveKeywords, contraKeywords);
    }
}
