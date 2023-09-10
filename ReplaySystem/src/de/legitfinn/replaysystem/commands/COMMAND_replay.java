package de.legitfinn.replaysystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.legitfinn.replaysystem.main.Main;
import de.legitfinn.replaysystem.main.ReplayAPI;
import de.legitfinn.replaysystem.methoden.ReplayPlayer;

public class COMMAND_replay implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender.hasPermission("replay.use")) {
			if(args.length == 0) {
				sender.sendMessage("§cReplay §7» §e/replay start");
				sender.sendMessage("§cReplay §7» §e/replay stop");
				sender.sendMessage("§cReplay §7» §e/replay play <ReplayId>");
			} else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("start")) {
					if(Main.canRecordReplay) {
						if(sender instanceof Player) {
							ReplayAPI.getInstance().startRecording(((Player)sender).getLocation().getWorld());
							sender.sendMessage("§7[§bReplaySystem§7] §9Replayaufnahme gestartet! ReplayId: §e" + Main.replayId);
						}
					} else {
						sender.sendMessage("§7[§bReplay§7] §cDie Replayaufnahme ist auf diesem Server deaktiviert.");
					}
				} else if(args[0].equalsIgnoreCase("stop")) {
					if(Main.canRecordReplay) {
						ReplayAPI.getInstance().stopRecording();
						sender.sendMessage("§7[§bReplaySystem§7] §9Replayaufnahme beendet! ReplayId: §e" + Main.replayId);
					} else {
						sender.sendMessage("§7[§bReplay§7] §cDie Replayaufnahme ist auf diesem Server deaktiviert.");
					}
				}
			} else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("play")) {
					if(Main.canPlayReplay) {
						if(ReplayPlayer.play(args[1])) {
							sender.sendMessage("§7[§bReplaySystem§7] §9Replayaufnahme wird nun abgespielt. ReplayId: §e" + args[1]);
						} else {
							sender.sendMessage("§7[§bReplaySystem§7] §cDieses Replay gibt es nicht");
						}
					} else {
						sender.sendMessage("§7[§bReplay§7] §cDas Abspielen von Replayaufnahmen ist auf diesem Server deaktiviert.");
					}
				}
			}
		}
		
		
		return true;
	}
	
	
}
