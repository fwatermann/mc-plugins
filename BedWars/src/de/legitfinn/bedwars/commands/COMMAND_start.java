package de.legitfinn.bedwars.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.Countdown;
import de.legitfinn.bedwars.methoden.GameState;

public class COMMAND_start implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender.hasPermission("bedwars.start")) {
			if(DataManager.state.equals(GameState.Waiting)) {
				if(Countdown.second > 10) {
					sender.sendMessage(DataManager.prefix + "Du hast die Runde vorzeitig gestartet.");
					Countdown.startCommand = true;
					Countdown.second = 11;
				} else {
					sender.sendMessage(DataManager.prefix + "§cDer Countdown ist schon unter §e§o10 Sekunden§c.");
				}
			} else {
				sender.sendMessage(DataManager.prefix + "§cDie Runde hat bereits begonnen!");
			}
		}
		
		
		return true;
	}
	
	
}
