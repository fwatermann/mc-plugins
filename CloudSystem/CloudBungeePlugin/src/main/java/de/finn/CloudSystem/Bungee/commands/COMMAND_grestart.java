package de.finn.CloudSystem.Bungee.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_grestart extends Command {

  public COMMAND_grestart(String name) {
    super(name);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {

    if (sender.hasPermission("network.proxy.restart.347574390112")) {
      List<String> cmd = new ArrayList<String>();
      cmd.add("sh");
      cmd.add("start.sh");
      ProcessBuilder pb = new ProcessBuilder(cmd);
      try {
        pb.start();
      } catch (IOException e) {
      }
      BungeeCord.getInstance().stop();
    }
  }
}
