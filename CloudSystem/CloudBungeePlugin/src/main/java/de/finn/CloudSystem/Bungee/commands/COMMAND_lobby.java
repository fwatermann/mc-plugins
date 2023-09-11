package de.finn.CloudSystem.Bungee.commands;

import java.util.ArrayList;
import java.util.Random;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_lobby extends Command {

  public COMMAND_lobby(String name) {
    super(name);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void execute(CommandSender sender, String[] args) {

    if (sender instanceof ProxiedPlayer) {
      ServerInfo lobby = getRandomLobby();
      ProxiedPlayer pp = (ProxiedPlayer) sender;
      if (lobby != null) {
        pp.connect(lobby);
      } else {
        pp.sendMessage("§cCloudError » §8Es konnte keine Lobby für dich gefunden werden. :/");
      }
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
