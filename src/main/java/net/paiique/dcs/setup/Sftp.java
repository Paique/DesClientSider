package net.paiique.dcs.setup;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import net.paiique.dcs.util.TextFileUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sftp {

    private static final Logger LOGGER = Logger.getLogger(Sftp.class.getName());

    private ChannelSftp getFilesFromSftp() {

        Console console = System.console();

        if (console == null) throw new RuntimeException("Couldn't get system Console instance.");

        try {
            JSch jSch = new JSch();
            Path knownHostsFile = Path.of(System.getProperty("user.home") + "/.ssh/known_hosts");

            if (!knownHostsFile.toFile().exists())
                throw new FileNotFoundException("known_hosts does not exist in the .ssh folder!");

            jSch.setKnownHosts(knownHostsFile.toString());
            Scanner reader = new Scanner(System.in);

            System.out.print("Host: ");
            String hostAndPort = reader.next();

            System.out.print("Username: ");
            String username = reader.next();

            System.out.println("Using " + hostAndPort + " to connect");

            int port = 22;
            String host = hostAndPort;
            if (hostAndPort.contains(":")) {
                String[] parsedHost = hostAndPort.split(":");
                host = parsedHost[0];
                port = Integer.parseInt(parsedHost[1]);
            }

            List<String> hosts = new TextFileUtils().read(knownHostsFile);

            System.out.println("Scanning host key, and adding to known_hosts if necessary.");
            for (String knowHost : hosts) {
                if (host.equals(knowHost)) {
                    ProcessBuilder processBuilder = new ProcessBuilder();

                    List<String> cmds = new ArrayList<>();
                    String os = System.getProperty("os.name").toLowerCase();

                    cmds.add(os.equals("windows") ? "/bin/bash" : "cmd");
                    cmds.add(os.equals("windows") ? "-c" : "/C");
                    cmds.add("ssh-keyscan");
                    cmds.add("-H");
                    cmds.add("-p");
                    cmds.add(String.valueOf(port));
                    cmds.add("-t");
                    cmds.add("ed25519"); //Todo: This is not right, needs revision.
                    cmds.add(host);
                    processBuilder.command(cmds);
                    processBuilder.inheritIO();
                    Process process = processBuilder.start();
                    process.waitFor();

                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuilder ssh = new StringBuilder();
                    while (stdInput.ready()) {
                        ssh.append(stdInput.readLine());
                    }
                    jSch.getIdentityRepository().add(ssh.toString().getBytes());
                    break;
                }
            }
            System.out.println("Done!");

            Session jschSession = jSch.getSession(username, host);
            java.util.Properties config = new java.util.Properties();

            config.put("StrictHostKeyChecking", "no");
            jschSession.setConfig(config);

            String keyPath = System.getenv("PRIV_KEY");

            if (keyPath != null) {
                System.out.println("Private key is set!");
                jSch.addIdentity(keyPath);

            } else {
                System.out.println("---------------WARN------------------");
                System.out.println("If you have a Private Key you can set the path using the ENV \"PRIV_KEY\"!");
                System.out.println("Windows powershell: $env:PRIV_KEY=" + Path.of("C:/users/" + System.getProperty("user.name") + "/.ssh/id_rsa"));
                System.out.println("Linux Bash: PRIV_KEY=" + Path.of("/home/" + System.getProperty("user.name") + "/.ssh/id_rsa"));
                System.out.println("-------------------------------------");
                System.out.println();
            }

            String password = System.getenv("PASSWORD");

            if (password == null || password.isBlank()) {
                System.out.println("---------------WARN------------------");
                System.out.println("If you are running this software multiple times consider setting a env \"PASSWORD\" with your password.");
                System.out.println("In Windows powershell: $env:PASSWORD='Your Password'");
                System.out.println("In Linux Bash: PASSWORD='Your Password'");
                System.out.println("Remember! Setting passwords as ENV it's not safe!");
                System.out.println("-------------------------------------");
                System.out.println();
                char[] passwordArray = console.readPassword("Password (Not showing characters): ");
                password = new String(passwordArray);
            } else System.out.println("Using password from ENV!");

            jschSession.setPassword(password);
            jschSession.setPort(port);
            System.out.println("Connecting to the session...");
            jschSession.connect();

            return (ChannelSftp) jschSession.openChannel("sftp");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while executing Sftp class: " + e);
        }
        return null;
    }


    public boolean execute(boolean skipVerification, List<String> keywords, List<String> contraKeywords) {
        ChannelSftp channelSftp = new Sftp().getFilesFromSftp();
        Scanner reader = new Scanner(System.in);

        try {
            if (channelSftp == null)
                throw new JSchException("Failed while connecting to SFTP! \n Check the username, password, host, and port!");
            System.out.println("Connecting to SFTP...");
            channelSftp.connect();
            System.out.println("Connected to SFTP!");

            System.out.println("Getting mods from the server...");
            List<ChannelSftp.LsEntry> mods = channelSftp.ls("mods").stream().toList();

            System.out.println(keywords.size() + " keywords loaded.");
            System.out.println(mods.size() + " mods detected.");

            List<String> clientSideMods = new ArrayList<>();
            keywords.forEach(keyword -> mods.forEach(modFile -> {
                String mod = modFile.getFilename().toLowerCase();
                contraKeywords.forEach(contraKeyword -> {
                    if (mod.contains(keyword) && !mod.contains(contraKeyword)) {
                        clientSideMods.add(mod);
                    }
                });
            }));

            if (!clientSideMods.isEmpty()) {
                System.out.println(clientSideMods);
                System.out.println(clientSideMods.size() + " client-side mods detected!");

                String remove = "";
                if (!skipVerification) {
                    System.out.print("\nDisable mods? (y/n): ");
                    remove = reader.next();
                }

                if (remove.equals("y") || skipVerification) {
                    remove = "";

                    channelSftp.mkdir("client");
                    for (String mod : clientSideMods) {
                        try {

                            SftpATTRS attrs;
                            try {
                                attrs = channelSftp.stat("mods/client/" + mod);
                            } catch (Exception e) {
                                attrs = null;
                            }

                            if (attrs != null) {
                                if (!skipVerification) {
                                    System.out.print("File" + mod + " already exists, remove? (y/n/a) (a = all)");
                                    remove = reader.next().toLowerCase();
                                }

                                if (remove.equals("a")) skipVerification = true;
                                if (remove.equals("y") || remove.equals("yes") || skipVerification) {
                                    channelSftp.rm("mods/" + mod);
                                    System.out.println("File removed: " + mod);
                                }
                                continue;
                            }

                            System.out.println("Moving to client folder: " + mod);
                            channelSftp.rename("mods/" + mod, "mods/client/" + mod);

                        } catch (SftpException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("All detected client-side mods are moved to a new folder named client, or removed if prompted.");
                } else {
                    System.out.println("Not removing the mods and exiting...");
                    System.exit(0);
                }
            } else {
                System.out.println("Client-side mods not detected!");
                System.out.println("Exiting...");
                System.exit(0);
            }
            channelSftp.exit();
            channelSftp.disconnect();
            channelSftp.getSession().disconnect();
            return true;

        } catch (JSchException | SftpException e) {
            LOGGER.log(Level.SEVERE, "Falied while executing Main class: " + e);
            return false;
        }
    }
}
