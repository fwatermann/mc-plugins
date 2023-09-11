package de.finn.CloudSystem.Bungee.commands;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_ping extends Command {

  public COMMAND_ping(String name) {
    super(name);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void execute(CommandSender sender, String[] args) {

    if (sender instanceof ProxiedPlayer) {
      if (args.length == 0) {
        ProxiedPlayer pp = (ProxiedPlayer) sender;
        pp.sendMessage("§aDeine Pingzeit: §e" + pp.getPing() + "ms");
      } else if (args.length == 1) {
        String name = args[0];
        ProxiedPlayer pp = BungeeCord.getInstance().getPlayer(name);
        if (pp != null && pp.isConnected()) {
          sender.sendMessage("§a" + pp.getName() + "'s Pingzeit: §e" + pp.getPing() + "ms");
        }
      }
    }
  }
}
