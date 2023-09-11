package de.finn.CloudSystem.Spigot.main;

import de.finn.CloudSystem.Spigot.Security.Encryptor;
import de.finn.CloudSystem.Spigot.config.ConfigManager;
import de.finn.CloudSystem.Spigot.event.CloudForcemapFinishEvent;
import de.finn.CloudSystem.Spigot.event.CloudSignUpdateMapEvent;
import de.finn.CloudSystem.Spigot.event.CloudSignUpdateMaxPlayerEvent;
import de.finn.CloudSystem.Spigot.event.CloudSignUpdateOnPlayerEvent;
import de.finn.CloudSystem.Spigot.event.CloudSignUpdateStatusEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConnectionManager implements Runnable {

  private ConfigManager config;
  private Socket connection;
  private PrintWriter writer;
  private BufferedReader reader;

  private String masterIP;
  private int masterPort;
  private String key;

  private String servername;
  private String gamemode;
  private String map;

  private static ConnectionManager instance;

  public ConnectionManager() {
    instance = this;
  }

  public static ConnectionManager getInstance() {
    return instance;
  }

  public String getMasterIP() {
    return masterIP;
  }

  public void send(String cmd) {
    if (writer != null) {
      writer.println(Encryptor.encrypt(cmd, key));
    }
  }

  public void disconnect() {
    try {
      connection.close();
      writer.close();
      reader.close();
    } catch (IOException ex) {
    }
  }

  @Override
  public void run() {
    config = new ConfigManager(new File("./cloud.ccfg"));
    config.load();
    if (!config.isSet("Connection.Master.IP")) {
      config.set("Connection.Master.IP", "127.0.0.1");
      config.set("Connection.Master.Key", "<put CloudKey of MasterServer here>");
      config.set("Connection.Server.Port", 31216);
    }
    config.reload();

    masterIP = config.getString("Connection.Master.IP");
    masterPort = config.getInt("Connection.Server.Port");
    key = config.getString("Connection.Master.Key");

    File pr = new File("./server.properties");
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream(pr));
      masterPort = Integer.parseInt(prop.getProperty("cloud-port", "31216"));
      servername = prop.getProperty("cloud-servername", "Server#XY");
      gamemode = prop.getProperty("cloud-gamemode", "NONE");
      map = prop.getProperty("cloud-map", "default");
    } catch (IOException e1) {
    }

    try {
      connection = new Socket(masterIP, masterPort);
      writer =
          new PrintWriter(
              new OutputStreamWriter(connection.getOutputStream(), Charset.forName("UTF-8")), true);
      reader =
          new BufferedReader(
              new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
    } catch (IOException e) {
    }

    if (writer == null || reader == null || !connection.isConnected()) {
      System.out.println("Could not connect to CloudMaster!");
      Bukkit.getServer().shutdown();
    }

    send("auth;" + key);
    send("info;" + servername + ";" + gamemode + ";" + map);

    while (true) {
      try {
        String read = reader.readLine();
        if (read == null) {
          System.out.println("Cloud disconnected! Stopping server.");
          for (Player all : Bukkit.getOnlinePlayers()) {
            all.kickPlayer("§cCloudError » §7Server lost connection to CloudMaster.");
          }
          Bukkit.getServer().shutdown();
          return;
        }

        String a = Encryptor.decrypt(read, key);
        String cmd = a.split(";")[0];
        String[] args = a.replaceFirst(cmd, "").replaceFirst(";", "").split(";");

        System.out.println("Received: " + a);

        if (cmd.equalsIgnoreCase("changeMap")) {
          String servername = args[0];
          String map = args[1];
          CloudSignUpdateMapEvent update = new CloudSignUpdateMapEvent(servername, map);
          Bukkit.getScheduler()
              .runTask(
                  Main.getPlugin(Main.class),
                  new Runnable() {
                    @Override
                    public void run() {
                      Bukkit.getPluginManager().callEvent(update);
                    }
                  });
        } else if (cmd.equalsIgnoreCase("changeMaxPlayer")) {
          String servername = args[0];
          int maxPlayer = Integer.parseInt(args[1]);
          CloudSignUpdateMaxPlayerEvent update =
              new CloudSignUpdateMaxPlayerEvent(servername, maxPlayer);
          Bukkit.getScheduler()
              .runTask(
                  Main.getPlugin(Main.class),
                  new Runnable() {
                    @Override
                    public void run() {
                      Bukkit.getPluginManager().callEvent(update);
                    }
                  });
        } else if (cmd.equalsIgnoreCase("changeOnPlayer")) {
          String servername = args[0];
          int onPlayer = Integer.parseInt(args[1]);
          CloudSignUpdateOnPlayerEvent update =
              new CloudSignUpdateOnPlayerEvent(servername, onPlayer);
          Bukkit.getScheduler()
              .runTask(
                  Main.getPlugin(Main.class),
                  new Runnable() {
                    @Override
                    public void run() {
                      Bukkit.getPluginManager().callEvent(update);
                    }
                  });
        } else if (cmd.equalsIgnoreCase("changeStatus")) {
          String servername = args[0];
          String status = args[1];
          CloudSignUpdateStatusEvent update = new CloudSignUpdateStatusEvent(servername, status);
          Bukkit.getScheduler()
              .runTask(
                  Main.getPlugin(Main.class),
                  new Runnable() {
                    @Override
                    public void run() {
                      Bukkit.getPluginManager().callEvent(update);
                    }
                  });
        } else if (cmd.equalsIgnoreCase("forcemapFinish")) {
          String servername = args[0];
          String mapname = args[1];
          if (!mapname.equalsIgnoreCase("NONE!=!")) {
            CloudForcemapFinishEvent update =
                new CloudForcemapFinishEvent(servername, null, mapname, Main.gamemode, true);
            Bukkit.getScheduler()
                .runTask(
                    Main.getPlugin(Main.class),
                    new Runnable() {

                      @Override
                      public void run() {
                        Bukkit.getPluginManager().callEvent(update);
                      }
                    });
          } else {
            String maps = args[2];
            CloudForcemapFinishEvent update =
                new CloudForcemapFinishEvent(servername, maps, null, Main.gamemode, false);
            Bukkit.getScheduler()
                .runTask(
                    Main.getPlugin(Main.class),
                    new Runnable() {

                      @Override
                      public void run() {
                        Bukkit.getPluginManager().callEvent(update);
                      }
                    });
          }
        } else if (cmd.equalsIgnoreCase("updatePlayerList")) {
          String players = args[0];
          Main.getPlugin(Main.class).onlinePlayers.clear();
          for (String all : players.split(",")) {
            if (all != null && !all.isEmpty()) {
              Main.getPlugin(Main.class).onlinePlayers.add(UUID.fromString(all));
            }
          }
        }
      } catch (IOException ex) {
        ex.printStackTrace();
        Bukkit.getServer().shutdown();
        return;
      }
    }
  }
}
