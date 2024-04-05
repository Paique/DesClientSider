package net.paiique.dcs;

import com.jcraft.jsch.JSchException;
import net.paiique.dcs.setup.Keywords;
import net.paiique.dcs.setup.Local;
import net.paiique.dcs.util.TextFileUtils;

import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;

public class Agent {
    public static TextFileUtils textFileUtils;
    public static Keywords keywords;
    public static void premain(String args, Instrumentation instrumentation) throws JSchException, URISyntaxException {
        new Local().execute(true);
    }
}
