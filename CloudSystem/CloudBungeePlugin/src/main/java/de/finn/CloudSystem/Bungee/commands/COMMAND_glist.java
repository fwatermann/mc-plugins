package de.finn.CloudSystem.Bungee.commands;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_glist extends Command {

  public COMMAND_glist(String name) {
    super(name);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void execute(CommandSender sender, String[] args) {
    sender.sendMessage(
        "§aEs sind zurzeit §e" + BungeeCord.getInstance().getOnlineCount() + " Spieler §aonline.");
  }
}
