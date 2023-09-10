package de.legitfinn.NickApi.main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class COMMAND_showNick implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender.hasPermission("NickAPI.showNick")) {
			if(args.length == 0) {
				sender.sendMessage(" ");
				sender.sendMessage("§7[§cNickAPI§7] §aDiese Spieler sind genickt:");
				for(String all : main.nickname_name.keySet()) {
					sender.sendMessage("§7[§cNickAPI§7] §3" + all + " = " + main.nickname_name.get(all));
				}
				sender.sendMessage(" ");
			} else if(args.length == 1) {
				String name = getCaseName(args[0]);
				if(main.nickname_name.containsKey(name)) {
					sender.sendMessage(" ");
					sender.sendMessage("§7[§cNickAPI§7] §3" + name + " = " + main.nickname_name.get(name));
					sender.sendMessage(" ");
				} else {
					sender.sendMessage(" ");
					sender.sendMessage("§7[§cNickAPI§7] §cDieser Spieler ist §oNICHT §cgenickt.");
					sender.sendMessage(" ");
				}
			}
		} else {
			sender.sendMessage("§7[§cNickAPi§7] §cDu hast keine Berechtigung die Nicknamen zu sehen.");
		}
		return true;
	}
	
	public String getCaseName(String name) {
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(all.getName().equalsIgnoreCase(name)) {
				return all.getName();
			}
		}
		return name;
	}
	

}
