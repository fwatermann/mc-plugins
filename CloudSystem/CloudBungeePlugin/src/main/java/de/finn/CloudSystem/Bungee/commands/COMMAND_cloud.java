package de.finn.CloudSystem.Bungee.commands;

import de.finn.CloudSystem.Bungee.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_cloud extends Command {

  public COMMAND_cloud(String name) {
    super(name);
  }

  private String prefix = "§7[§bCloud§7] §e";

  @SuppressWarnings("deprecation")
  @Override
  public void execute(CommandSender sender, String[] args) {

    if (sender.hasPermission("cloudsystem.command")) {
      if (args.length <= 1) {
        sendHelp(sender);
      } else if (args.length == 2) {
        if (args[0].equalsIgnoreCase("startServer")) {
          String gamemode = args[1];
          sendStartServer(gamemode, null);
          sender.sendMessage(
              prefix
                  + "Es wird nun ein \""
                  + gamemode.toUpperCase()
                  + "-Server\" mit einer zufälligen Map gestartet. §7(Falls vorhanden)");
        } else if (args[0].equalsIgnoreCase("stopServer")) {
          String server = args[1];
          sendStopServer(server);
          sender.sendMessage(
              prefix
                  + "Der Server \""
                  + server.toUpperCase()
                  + "\" wird gestoppt. §7(Falls vorhanden)");
        } else if (args[0].equalsIgnoreCase("restartGamemode")) {
          String gamemode = args[1];
          sendRestart(gamemode);
          sender.sendMessage(
              prefix
                  + "Es werden alle Server des Spielmoduses "
                  + gamemode.toUpperCase()
                  + " neugestartet. §7(Falls vorhanden und nicht Ingame)");
        } else {
          sendHelp(sender);
        }
      } else if (args.length == 3) {
        if (args[0].equalsIgnoreCase("startServer")) {
          String gamemode = args[1];
          String map = args[2];
          sendStartServer(gamemode, map);
          sender.sendMessage(
              prefix
                  + "Es wird nun ein "
                  + gamemode.toUpperCase()
                  + "-Server mit der Map \""
                  + map
                  + "\" gestartet. §7(Falls vorhanden)");
        } else {
          sendHelp(sender);
        }
      } else {
        sendHelp(sender);
      }
    } else {
      sender.sendMessage(prefix + "§cDu hast keine Berechtigung diesen Befehl auszuführen.");
    }
  }

  @SuppressWarnings("deprecation")
  public void sendHelp(CommandSender sender) {
    sender.sendMessage(prefix + "§7§m>-------------------------------------<");
    sender.sendMessage(prefix);
    sender.sendMessage(prefix + "/cloud startServer <Spielmodus> [Map]");
    sender.sendMessage(prefix + "/cloud stopServer <Server-ID>");
    sender.sendMessage(prefix + "/cloud restartGamemode <Spielmodus>");
    sender.sendMessage(prefix + "§m/cloud lockGamemode <Spielmodus>");
    sender.sendMessage(prefix);
    sender.sendMessage(prefix + "§7§m>-------------------------------------<");
  }

  private void sendStartServer(String gamemode, String map) {
    String cmd =
        "startServer;" + gamemode.toUpperCase() + ";" + (map != null ? map : "RANDOM32863478");
    Main.send(cmd);
  }

  private void sendStopServer(String servername) {
    String cmd = "stopServer;" + servername;
    Main.send(cmd);
  }

  private void sendRestart(String gamemode) {
    String cmd = "restartGamemode;" + gamemode.toUpperCase();
    Main.send(cmd);
  }
}
