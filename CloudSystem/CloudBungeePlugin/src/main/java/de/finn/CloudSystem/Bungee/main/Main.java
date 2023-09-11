package de.finn.CloudSystem.Bungee.main;

import de.finn.CloudSystem.Bungee.Security.Encryptor;
import de.finn.CloudSystem.Bungee.commands.COMMAND_alert;
import de.finn.CloudSystem.Bungee.commands.COMMAND_cloud;
import de.finn.CloudSystem.Bungee.commands.COMMAND_glist;
import de.finn.CloudSystem.Bungee.commands.COMMAND_grestart;
import de.finn.CloudSystem.Bungee.commands.COMMAND_lobby;
import de.finn.CloudSystem.Bungee.commands.COMMAND_ping;
import de.finn.CloudSystem.Bungee.commands.COMMAND_refreshPing;
import de.finn.CloudSystem.Bungee.commands.COMMAND_wartung;
import de.finn.CloudSystem.Bungee.config.ConfigManager;
import de.finn.CloudSystem.Bungee.listener.ConnectListener;
import de.finn.CloudSystem.Bungee.listener.PingListener;
import de.finn.CloudSystem.PlayerData.PlayerData;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.UUID;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {

  public static String ping_line1 = "";
  public static String ping_line2 = "";
  public static String ping_wartung_line1 = "";
  public static String ping_wartung_line2 = "";
  public static int ping_maxPlayer = 100;
  public static int ping_fakePlayer = 0;
  public static int maxPlayer = 120;
  public static String name = "Logi-Lounge.de";

  public void onEnable() {
    System.out.println("Started CloudConnection");
    PlayerData.connect(this);
    Thread th =
        new Thread(
            new Runnable() {

              @Override
              public void run() {
                start();
              }
            });
    th.start();


    this.getProxy().getPluginManager().registerListener(this, new ConnectListener());
    this.getProxy().getPluginManager().registerListener(this, new PingListener());
    this.getProxy().getPluginManager().registerCommand(this, new COMMAND_lobby("lobby"));
    this.getProxy().getPluginManager().registerCommand(this, new COMMAND_lobby("leave"));
    this.getProxy().getPluginManager().registerCommand(this, new COMMAND_lobby("l"));
    this.getProxy().getPluginManager().registerCommand(this, new COMMAND_lobby("hub"));
    this.getProxy().getPluginManager().registerCommand(this, new COMMAND_glist("glist"));
    this.getProxy().getPluginManager().registerCommand(this, new COMMAND_glist("list"));
    this.getProxy().getPluginManager().registerCommand(this, new COMMAND_ping("ping"));
    this.getProxy().getPluginManager().registerCommand(this, new COMMAND_ping("latenz"));
    this.getProxy()
        .getPluginManager()
        .registerCommand(this, new COMMAND_wartung("wartung"));
    this.getProxy()
        .getPluginManager()
        .registerCommand(this, new COMMAND_wartung("wartungen"));
    this.getProxy()
        .getPluginManager()
        .registerCommand(this, new COMMAND_wartung("wartungsarbeiten"));
    this.getProxy()
        .getPluginManager()
        .registerCommand(this, new COMMAND_wartung("maintenance"));
    this.getProxy()
        .getPluginManager()
        .registerCommand(this, new COMMAND_refreshPing("refreshping"));
    this.getProxy()
        .getPluginManager()
        .registerCommand(this, new COMMAND_grestart("grestart"));
    this.getProxy().getPluginManager().registerCommand(this, new COMMAND_alert("alert"));
    this.getProxy()
        .getPluginManager()
        .registerCommand(this, new COMMAND_alert("broadcast"));
    this.getProxy().getPluginManager().registerCommand(this, new COMMAND_cloud("cloud"));
  }

  public void onDisable() {
    PlayerData.mysql.close();
    if (connection != null && connection.isConnected()) {
      try {
        connection.close();
        reader.close();
        writer.close();
      } catch (IOException e) {
      }
    }
  }

  public static ConfigManager config;

  private static Socket connection;
  private static PrintWriter writer;
  private static BufferedReader reader;
  private static String key;

  @SuppressWarnings("deprecation")
  public void start() {
    config = new ConfigManager(new File("./cloud.ccfg"));
    config.load();
    if (!config.isSet("Connection.Master.IP")) {
      config.set("Connection.Master.IP", "127.0.0.1");
      config.set("Connection.Master.Port", 31217);
      config.set("Connection.Master.Key", "<put CloudKey of Masterserver here>");
      config.set("Network.wartung", "false");
      config.set("Network.motd.line1", "§bCloudSystem");
      config.set("Network.motd.line2", "§8> By HerrVergesslich");
      config.set("Network.wartung.motd.line1", "§bCloudSystem  §8[§4§lWartung§8]");
      config.set("Network.wartung.motd.line2", "§8> By HerrVergesslich");
      config.set("Network.ping.MaxPlayer", 100);
      config.set("Network.ping.FakePlayer", 0);
      config.set("Network.MaxPlayer", 120);
      config.set("Network.Name", "Logi-Lounge.de");
      config.reload();
    }

    String masterIP = config.getString("Connection.Master.IP");
    Integer masterPort = config.getInt("Connection.Master.Port");
    key = config.getString("Connection.Master.Key");

    ping_line1 = config.getString("Network.motd.line1");
    ping_line2 = config.getString("Network.motd.line2");
    ping_wartung_line1 = config.getString("Network.wartung.motd.line1");
    ping_wartung_line2 = config.getString("Network.wartung.motd.line2");
    ping_maxPlayer = config.getInt("Network.ping.MaxPlayer");
    ping_fakePlayer = config.getInt("Network.ping.FakePlayer");
    maxPlayer = config.getInt("Network.MaxPlayer");
    name = config.getString("Network.Name");

    PingListener.wartung = config.getBoolean("Network.wartung");

    System.out.println("IP: " + masterIP);
    try {
      connection = new Socket(masterIP, masterPort);
      writer =
          new PrintWriter(
              new OutputStreamWriter(connection.getOutputStream(), Charset.forName("UTF-8")), true);
      reader =
          new BufferedReader(
              new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

      if (writer == null || reader == null || !connection.isConnected()) {
        System.out.println("Could not connect to CloudMaster!");
        System.out.println("Retrying in 5 seconds...");
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        System.out.println("Retrying...");
        start();
        return;
      }

      // auth
      writer.println(Encryptor.encrypt("auth;" + key, key));

      while (true) {
        String read = reader.readLine();
        if (read == null) {
          for (ProxiedPlayer all : this.getProxy().getPlayers()) {
            all.disconnect("§cCloudError » §8Lost connection to CloudMaster!");
          }
          connection.close();
          reader.close();
          writer.close();
          connection = null;
          reader = null;
          writer = null;
          System.out.println("Could not connect to CloudMaster!");
          System.out.println("Retrying in 5 seconds...");
          try {
            Thread.sleep(5000);
          } catch (InterruptedException e) {
          }
          System.out.println("Retrying...");
          start();
          return;
        }
        String a = Encryptor.decrypt(read, key);
        String cmd = a.split(";")[0];
        String[] args = a.replaceFirst(cmd, "").replaceFirst(";", "").split(";");

        if (cmd.equalsIgnoreCase("addServer")) {
          String servername = args[0];
          String address = args[1];
          int port = Integer.parseInt(args[2]);
          ServerInfo info =
              this.getProxy()
                  .constructServerInfo(servername, new InetSocketAddress(address, port), "", false);
          this.getProxy().getServers().put(servername, info);
          System.out.println("[Cloud]: Added Server <" + servername + ">");
        } else if (cmd.equalsIgnoreCase("removeServer")) {
          String servername = args[0];
          this.getProxy().getServers().remove(servername);
          System.out.println("[Cloud]: Removed Server <" + servername + ">");
        } else if (cmd.equalsIgnoreCase("kickPlayer")) {
          String uuid = args[0];
          ProxiedPlayer pp = this.getProxy().getPlayer(UUID.fromString(uuid));
          if (pp != null && pp.isConnected()) {
            pp.disconnect("§cCloudSystem » §cKicked by Cloud.");
          }
        } else if (cmd.equalsIgnoreCase("movePlayer")) {
          String uuid = args[0];
          String server = args[1];
          ProxiedPlayer pp = this.getProxy().getPlayer(UUID.fromString(uuid));
          if (pp != null && pp.isConnected()) {
            ServerInfo info = this.getProxy().getServerInfo(server);
            if (info != null) {
              pp.connect(info);
            }
          }
        }
      }
    } catch (IOException ex) {
      for (ProxiedPlayer all : this.getProxy().getPlayers()) {
        all.disconnect("§cCloudError » §8Lost connection to CloudMaster!");
      }
      try {
        if (connection != null) connection.close();
        if (reader != null) reader.close();
        if (writer != null) writer.close();
        connection = null;
        reader = null;
        writer = null;
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println("Could not connect to CloudMaster!");
      System.out.println("Retrying in 5 seconds...");
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
      }
      System.out.println("Retrying...");
      start();
      return;
    }
  }

  public static void sendOnline() {
    if (connection.isConnected() && writer != null && reader != null) {
      String a = "";
      for (ProxiedPlayer all : ProxyServer.getInstance().getPlayers()) {
        a += "," + all.getUniqueId().toString();
      }
      a = a.replaceFirst(",", "");
      writer.println(Encryptor.encrypt("updatePlayerList;" + a, key));
    }
  }

  public static void send(String cmd) {
    if (connection.isConnected() && writer != null && reader != null) {
      String a = Encryptor.encrypt(cmd, key);
      writer.println(a);
    }
  }
}
