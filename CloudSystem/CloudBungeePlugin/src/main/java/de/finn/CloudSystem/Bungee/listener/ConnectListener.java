package de.finn.CloudSystem.Bungee.listener;

import de.finn.CloudSystem.Bungee.main.Main;
import de.finn.CloudSystem.PlayerData.PlayerData;
import de.legitfinn.ppermissions.main.PPermissions;
import java.util.ArrayList;
import java.util.Random;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ConnectListener implements Listener {

  @SuppressWarnings("deprecation")
  @EventHandler
  public void onConnect(ServerConnectEvent e) {
    Main.sendOnline();
    if (e.getPlayer().getServer() == null) {
      ServerInfo to = getRandomLobby();
      if (to == null) {
        e.getPlayer()
            .disconnect("§cCloudError » §8Es konnte keine Lobby für dich gefunden werden! :/");
        return;
      } else {
        System.out.println(
            "Connecting to: "
                + to.getAddress().getAddress().toString()
                + ", "
                + to.getAddress().getPort());
        e.setTarget(to);
      }
    }
  }

  @SuppressWarnings("deprecation")
  @EventHandler
  public void onLogin(LoginEvent e) {
    PlayerData.updateEntry(e.getConnection().getUniqueId(), e.getConnection().getName());

    if (Main.config.getBoolean("Network.wartung")) {
      if (!PPermissions.hasPermission(e.getConnection().getUniqueId(), "network.wartung")
          && !PPermissions.hasPermission(e.getConnection().getUniqueId(), "network.wartung.keep")) {
        e.setCancelled(true);
        e.setCancelReason("§cCloudError » §8Wartungsarbeiten");
        return;
      }
    }
    if ((BungeeCord.getInstance().getOnlineCount() + Main.ping_fakePlayer) >= Main.ping_maxPlayer
        && BungeeCord.getInstance().getOnlineCount() < Main.maxPlayer) {
      if (!PPermissions.hasPermission(e.getConnection().getUniqueId(), "network.joinFull")) {
        e.setCancelled(true);
        e.setCancelReason(
            "§cDas Netzwerk hat seine Spielergrenze von §6"
                + Main.ping_maxPlayer
                + " §cerreicht.\n\n§cKaufe dir einen Rang, um das volle Netzwerk zu betreten.");
      }
    } else if (BungeeCord.getInstance().getOnlineCount() >= Main.maxPlayer) {
      e.setCancelled(true);
      e.setCancelReason(
          "§cDas Netzwerk hat seine Spielergrenze von §6"
              + Main.maxPlayer
              + " §cerreicht.\n\n§cLeider hilft bei dieser Spielerzahl auch kein höherer Rang...");
    }
  }

  @EventHandler
  public void onQuit(ServerDisconnectEvent e) {
    Main.sendOnline();
  }

  @SuppressWarnings({"deprecation"})
  @EventHandler
  public void onKick(ServerKickEvent e) {
    ServerInfo to = getRandomLobby();
    int i = 0;

    if (to != null) {
      while (to.equals(e.getKickedFrom()) && i < 100) {
        to = getRandomLobby();
        i++;
      }
      if (to != null && to != e.getKickedFrom()) {
        e.setCancelServer(to);
        e.setCancelled(true);
        return;
      } else {
        e.getPlayer()
            .disconnect("§cCloudError » §8Es konnte keine Lobby für dich gefunden werden! :/");
        e.setCancelled(true);
        return;
      }
    } else {
      e.getPlayer()
          .disconnect("§cCloudError » §8Es konnte keine Lobby für dich gefunden werden! :/");
      e.setCancelled(true);
      return;
    }
  }

  private ServerInfo getRandomLobby() {
    ArrayList<ServerInfo> lobbys = new ArrayList<ServerInfo>();
    for (String all : BungeeCord.getInstance().getServers().keySet()) {
      if ((all.toLowerCase().contains("lobby") || all.toLowerCase().contains("hub"))
          && all.toLowerCase().contains("#")) {
        lobbys.add(BungeeCord.getInstance().getServers().get(all));
      }
    }
    Random rnd = new Random();
    if (lobbys.size() < 1) {
      return null;
    }
    ServerInfo ret = lobbys.get(rnd.nextInt(lobbys.size()));
    return ret;
  }
}
