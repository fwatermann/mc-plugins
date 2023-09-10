package de.legitfinn.knockbackffa.command;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.NightShadwo.SignAPI.Connectoin.Map;
import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Main.Types.Gamemode;
import de.legitfinn.knockbackffa.main.DataManager;
import de.legitfinn.knockbackffa.methoden.Kits;

public class COMMAND_kffaadmin implements CommandExecutor{

	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender.hasPermission("knockbackffa.admin")) {
			
			if(args.length == 0 || args.length == 1) {
				sendHelp(sender);
			} else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("changekit")) {
					int kit = DataManager.Kit;
					if(!isNumber(args[1])) {
						sender.sendMessage(DataManager.prefix + "§c\"§e" + args[1] + "§c\" §cist keine Zahl!");
					} else {
						kit = Integer.parseInt(args[1]);
						Kits.changeKit(kit);
						sender.sendMessage(DataManager.prefix + "§aDas Kit wurden auf Kit-" + kit + " gesetzt.");
					}
					
				} else if(args[0].equalsIgnoreCase("changemap")) {
					String map = DataManager.mapname;
					ArrayList<String> maps = new ArrayList<String>();
					for(Map all : SignAPI.getMaps(Gamemode.KNOCKBACKFFA)) {
						maps.add(all.name().toLowerCase());
					}
					if(!maps.contains(args[1].toLowerCase())) {
						sender.sendMessage(DataManager.prefix + "§cDie Map \"§e" + args[1] + "§c\" existiert nicht.");
						sender.sendMessage(DataManager.prefix + "§cVerfügbare Maps:");
						for(Map all : SignAPI.getMaps(Gamemode.KNOCKBACKFFA)) {
							sender.sendMessage(DataManager.prefix + "§8● §a" + all.name());
						}
					} else {
						map = args[1];
						for(Map all : SignAPI.getMaps(Gamemode.KNOCKBACKFFA)) {
							if(all.name().equalsIgnoreCase(map)) {
								Kits.changeMap(all);
								break;
							}
						}
						sender.sendMessage(DataManager.prefix + "§aDie Map wurde auf \"§e" + map + "§e\"");
					}
				} else {
					sendHelp(sender);
				}
			} else {
				sendHelp(sender);
			}
			
		}
		
		
		return false;
	}
	
	public void sendHelp(CommandSender sender) {
		
		sender.sendMessage("§b☰☰☰☰☰☰☰⚌⚌⚌⚊⚊§c TrollSystem §b⚊⚊⚌⚌⚌☰☰☰☰☰☰☰");
		sender.sendMessage("");
		sender.sendMessage("§e/kffaadmin changekit <kitId>");
		sender.sendMessage("§e/kffaadmin changeMap <Mapname>");
		sender.sendMessage("");
		sender.sendMessage("§b☰☰☰☰☰☰☰⚌⚌⚌⚊⚊§c TrollSystem §b⚊⚊⚌⚌⚌☰☰☰☰☰☰☰");
	}
	
	public boolean isNumber(String s) {
		String tmp = s;
		for(int i = 0; i <= 9; i ++) {
			tmp = tmp.replace(i + "", "");
		}
		if(tmp.length() > 0) {
			return false;
		} else {
			return true;
		}
	}
	
	
	
}
