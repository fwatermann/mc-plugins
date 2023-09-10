package de.legitfinn.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.legitfinn.lobby.items.ShopAdmin;
import de.legitfinn.ppermissions.main.PPermissions;

public class COMMAND_payments implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(PPermissions.isInGroup(p, "Admin")) {
				p.openInventory(ShopAdmin.getWaitingPayments(0, false, null));
			}
		} else {
			sender.sendMessage("񘿼Lobby �7� Die Console kann keine Inventare sehen...");
		}
		return true;
	}
	
	
	

}
