package de.finn.CloudSystem.Bungee.commands;

import de.finn.CloudSystem.Bungee.main.Main;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_alert extends Command {

  public COMMAND_alert(String name) {
    super(name);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void execute(CommandSender sender, String[] args) {

    if (sender.hasPermission("network.alert") || sender.hasPermission("network.broadcast")) {
      String msg = "";
      for (int i = 0; i < args.length; i++) {
        msg += " " + args[i];
      }
      msg = msg.replaceFirst(" ", "");
      BungeeCord.getInstance().broadcast("§7[§bCloud§7] §e§m✤-------------------------------§e✤");
      BungeeCord.getInstance().broadcast("§7[§bCloud§7]");
      BungeeCord.getInstance()
          .broadcast("§7[§bCloud§7] §6§l" + Main.config.getString("Network.Name"));
      BungeeCord.getInstance().broadcast("§7[§bCloud§7]");
      for (String all : msg.split("\n")) {
        BungeeCord.getInstance().broadcast("§7[§bCloud§7] §c" + all);
      }
      BungeeCord.getInstance().broadcast("§7[§bCloud§7]");
      BungeeCord.getInstance().broadcast("§7[§bCloud§7] §e§m✤-------------------------------§e✤");
    } else {
      sender.sendMessage("§7[§bCloud§7] §cDu hast keine Berechtigung diesen Befehl auszufüghren.");
    }
  }
}
