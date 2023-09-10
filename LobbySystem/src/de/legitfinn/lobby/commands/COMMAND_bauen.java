package de.legitfinn.lobby.commands;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.legitfinn.lobby.items.Hotbar;
import de.legitfinn.lobby.main.main;
import de.legitfinn.ppermissions.main.PPermissions;

public class COMMAND_bauen implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(PPermissions.hasRight(p, "lobby_build")) {
				if(main.build.contains(p)) {
					main.build.remove(p);
					p.sendMessage("§7§4Lobby §7» §cBauen §4deaktiviert§c.");
					p.getInventory().clear();
					p.getInventory().setArmorContents(null);
					Hotbar.createHotbar(p);
					p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1.0F);
					p.setGameMode(GameMode.ADVENTURE);
					COMMAND_flight.flight.remove(p);
				} else {
					main.build.add(p);
					COMMAND_flight.flight.add(p);
					p.sendMessage("§7§4Lobby §7» §aBauen §2aktiviert§a.");
					p.getInventory().clear();
					p.getInventory().setArmorContents(null);
					p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1F, 1.0F);
					p.setGameMode(GameMode.CREATIVE);
					
				}
			}
		} else {
			sender.sendMessage("§7§4Lobby §7» §cDie Console kann nicht bauen...");
		}
		return true;
	}
	
	
	

}
