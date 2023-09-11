package de.finn.CloudSystem.Spigot.main;

import de.finn.CloudSystem.Coins.CoinSQL;
import de.finn.CloudSystem.Friends.FriendSQL;
import de.finn.CloudSystem.Lobby.LobbySettingsSQL;
import de.finn.CloudSystem.PlayerData.PlayerData;
import de.finn.CloudSystem.Statistics.StatisticSQL;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

  public ConnectionManager connectionManager;
  public ArrayList<UUID> onlinePlayers = new ArrayList<UUID>();
  public static String gamemode;

  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
    Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    try {
      Properties prop = new Properties();
      prop.load(new FileInputStream(new File("server.properties")));
      gamemode = prop.getProperty("cloud-gamemode");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    connectionManager = new ConnectionManager();
    Thread th = new Thread(connectionManager);
    th.setName("MasterConnection");
    th.start();
    try {
      Thread.sleep(500);
    } catch (InterruptedException ex) {
    }
    connectDatabases();
  }

  private void connectDatabases() {
    try {
      CoinSQL.connect();
    } catch (Exception ex) {
    }
    try {
      FriendSQL.connect();
    } catch (Exception ex) {
    }
    try {
      PlayerData.connect();
    } catch (Exception ex) {
    }
    try {
      StatisticSQL.connect();
    } catch (Exception ex) {
    }
    try {
      LobbySettingsSQL.connect();
    } catch (Exception ex) {
    }
  }

  private void disconnectDatabases() {
    CoinSQL.mysql.close();
    FriendSQL.mysql.close();
    PlayerData.mysql.close();
    StatisticSQL.mysql.close();
    LobbySettingsSQL.mysql.close();
  }

  public void onDisable() {
    disconnectDatabases();
    connectionManager.disconnect();
  }

  // Only proxy join
  @EventHandler
  public void onLogin(PlayerLoginEvent e) {
    if (!onlinePlayers.contains(e.getPlayer().getUniqueId())) {
      e.disallow(
          Result.KICK_OTHER,
          "§cCloudError §8» §7Du hast dich nicht über unsere Proxy zu unserem Server verbunden.\n§7Bitte verbinde dich über §6§nLogi-Lounge.de:25565");
      return;
    }
  }
}
