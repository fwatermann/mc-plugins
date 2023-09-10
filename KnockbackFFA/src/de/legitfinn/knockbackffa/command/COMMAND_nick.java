package de.legitfinn.knockbackffa.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.legitfinn.NickApi.main.NickModul;
import de.legitfinn.knockbackffa.main.main;
import de.legitfinn.knockbackffa.methoden.ScoreboardSystem;
import de.legitfinn.ppermissions.main.PPermissions;

public class COMMAND_nick implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(p.hasPermission("nick.allow")) {
				if(args.length == 0) {
					if(NickModul.isNicked(p.getUniqueId())) {
						NickModul.unNickPlayer(p, true);
						Bukkit.getServer().getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
							@Override
							public void run() {
								PPermissions.loadNickGroups(p);
								for(Player all : Bukkit.getOnlinePlayers()) {
									ScoreboardSystem.updateScoreboard(all, true);
								}
							}
						}, 10);
						COMMAND_stats.randomvalues.remove(p.getUniqueId());
					} else {
						NickModul.nickPlayerOnline(p, true);
						Bukkit.getServer().getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
							@Override
							public void run() {
								PPermissions.loadNickGroups(p);
								for(Player all : Bukkit.getOnlinePlayers()) {
									ScoreboardSystem.updateScoreboard(all, true);
								}
							}
						}, 10);
					}	
				}
			}			
		}
		return false;
	}

}
