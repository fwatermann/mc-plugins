package de.legitfinn.bedwars.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.NightShadwo.SignAPI.Connectoin.Map;
import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Main.Types.Gamemode;
import de.NightShadwo.SignAPI.Main.Types.InformationTyps;
import de.legitfinn.bedwars.main.DataManager;

public class COMMAND_forcemap implements CommandExecutor{

	public static long Last = 0L;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender.hasPermission("forcemap")) {
			if(System.currentTimeMillis() - Last < 3000) {
				sender.sendMessage(DataManager.prefix + "§cDie Map kann nur jede §e§o3Sekunden §r§cgewechselt werden.");
			} else {
				String map = args[0];
				ArrayList<Map> maps = SignAPI.getMaps(Gamemode.BEDWARS);
				boolean exist = false;
				String mn = "";
				for(Map m : maps){
					if(m.name().equalsIgnoreCase(map)){
						exist = true;
						mn = m.name();
						break;
					}
				}
				if(exist){
					SignAPI.sendChange(InformationTyps.MAP, mn, "own");
					Last = System.currentTimeMillis();
					sender.sendMessage(DataManager.prefix + "§3Die Map wurde §egewechselt§3.");
				}else {
					sender.sendMessage(DataManager.prefix + "§cDiese Map existiert nicht. Mögliche Mapnamen:");
					for(Map m : maps){
						int player = m.teams();
						if(player == -1){
							player = m.ppt();
						}
						sender.sendMessage("§7- §e" + m.name() + "§7» §3Spieler: §e" + player);
					}
				}
			}
		}
		
		
		return true;
	}

}
