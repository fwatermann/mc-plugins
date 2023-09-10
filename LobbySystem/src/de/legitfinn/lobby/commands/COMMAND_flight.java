package de.legitfinn.lobby.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.legitfinn.lobby.SQL.LobbySettingsSQL;
import de.legitfinn.lobby.main.main;
import de.legitfinn.ppermissions.main.PPermissions;

public class COMMAND_flight implements CommandExecutor{
	
	public static ArrayList<Player> flight = new ArrayList<Player>();
	
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(PPermissions.hasRight(p, "flight")) {
				if(flight.contains(p)) {
					flight.remove(p);
					p.setFlying(false);
					p.setAllowFlight(false);
					p.sendMessage(main.LobbyPrefix + "§cFliegen deaktiviert.");
					if(LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "DOUBLEJUMP")) {
						p.setAllowFlight(true);
					}
				} else {
					flight.add(p);
					p.setAllowFlight(true);
					p.setFlying(true);
					p.sendMessage(main.LobbyPrefix + "Fliegen aktiviert.");
				}
			}
			
		} else {
			sender.sendMessage("§4§lERROR §7» §cDie Konsole kann nicht fliegen.");
		}
		
		
		
		return true;
	}	
}
