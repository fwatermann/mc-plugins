package de.legitfinn.knockbackffa.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.legitfinn.knockbackffa.main.DataManager;
import de.legitfinn.knockbackffa.methoden.Kits;

public class COMMAND_savekit implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = null;
		if(sender instanceof Player){
			p = (Player) sender;
		}
		
		if(p == null){
			sender.sendMessage(DataManager.prefix + "§cDu musst ein Spieler sein, um das Kit zu speichern.");
		}else {
			if(args.length == 0){
				Kits.openKits(p);
			}else if(args.length > 0){
				try{
					int kit = Integer.parseInt(args[0]);
					if(kit < 1 || kit > 5){
						p.sendMessage(DataManager.prefix + "§cBitte gebe eine Zahl von 1 bis 5 an.");
						return true;
					}
					p.openInventory(Kits.getKitCurrent(p, kit));
					p.sendMessage(DataManager.prefix + "§3Wenn du mit dem Sortieren deines Kits fertig bist, dann schließe das Inventar.");
				}catch (NumberFormatException e){
					p.sendMessage(DataManager.prefix + "§cBitte gebe eine Zahl von 1 bis 5 an.");
					return true;
				}
			}
		}
	
		return true;
	}

}
