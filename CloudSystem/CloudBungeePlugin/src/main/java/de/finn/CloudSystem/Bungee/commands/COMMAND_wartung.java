package de.finn.CloudSystem.Bungee.commands;

import de.finn.CloudSystem.Bungee.listener.PingListener;
import de.finn.CloudSystem.Bungee.main.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_wartung extends Command {

  public COMMAND_wartung(String name) {
    super(name);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void execute(CommandSender sender, String[] args) {

    if (sender.hasPermission("network.wartung")) {

      if (args.length == 0) {
        sender.sendMessage("§c/wartung <on/off>");
      } else if (args.length == 1) {
        String status = args[0];

        if (status.equalsIgnoreCase("on")) {
          if (!Main.config.getBoolean("Network.wartung")) {
            Main.config.set("Network.wartung", "true");
            Main.config.rewriteConfig();
            Main.config.reload();
            PingListener.wartung = true;
            sender.sendMessage("§cWartungen wurden §aaktiviert§c!");

            for (ProxiedPlayer all : BungeeCord.getInstance().getPlayers()) {
              if (!all.hasPermission("network.wartung")
                  && !all.hasPermission("network.wartung.keep")) {
                all.disconnect("§e§l" + Main.name + "\n\n§4WARTUNGSARBEITEN");
              }
            }
          } else {
            sender.sendMessage("§cWartungen sind bereits aktiviert!");
          }
        } else if (status.equalsIgnoreCase("off")) {
          if (Main.config.getBoolean("Network.wartung")) {
            Main.config.set("Network.wartung", "false");
            Main.config.rewriteConfig();
            Main.config.reload();
            sender.sendMessage("§cWartungen wurden §4deaktiviert§c!");
            PingListener.wartung = false;
          } else {
            sender.sendMessage("§cWartungen sind bereits deaktiviert!");
          }
        }
      }
    }
  }
}
