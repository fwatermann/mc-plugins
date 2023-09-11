package de.finn.CloudSystem.Bungee.commands;

import de.finn.CloudSystem.Bungee.main.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_refreshPing extends Command {

  public COMMAND_refreshPing(String name) {
    super(name);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {

    if (sender.hasPermission("network.serverping")) {
      Main.config.reload();
      Main.ping_line1 = Main.config.getString("Network.motd.line1");
      Main.ping_line2 = Main.config.getString("Network.motd.line2");
      Main.ping_maxPlayer = Main.config.getInt("Network.ping.MaxPlayer");
      Main.ping_fakePlayer = Main.config.getInt("Network.ping.FakePlayer");
    }
  }
}
