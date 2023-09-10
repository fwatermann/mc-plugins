package de.legitfinn.bedwars.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.legitfinn.NickApi.main.NickModul;
import de.legitfinn.bedwars.main.BedWars;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.GameState;
import de.legitfinn.bedwars.methoden.TeamManager;

public class COMMAND_nick implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(p.hasPermission("nick.allow")) {
				if(NickModul.isNicked(p.getUniqueId())) {
					String team = TeamManager.getTeamOfPlayer(p);
					if(team != null) {
						Scoreboard sc = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
						Team t = sc.getEntryTeam(p.getName());
						t.removeEntry(p.getName());
						NickModul.unNickPlayer(p, true);
						Bukkit.getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
							@Override
							public void run() {
								t.addEntry(p.getName());
							}
						}, 10);
					} else if(DataManager.state.equals(GameState.Waiting)) {
						Scoreboard sc = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
						Team t = sc.getEntryTeam(p.getName());
						t.removeEntry(p.getName());
						NickModul.unNickPlayer(p, true);
						Bukkit.getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
							@Override
							public void run() {
								t.addEntry(p.getName());
							}
						}, 10);
					}
				} else {
					String team = TeamManager.getTeamOfPlayer(p);
					if(team != null) {
						Scoreboard sc = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
						Team t = sc.getEntryTeam(p.getName());
						t.removeEntry(p.getName());
						NickModul.nickPlayerOnline(p, true);
						Bukkit.getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
							@Override
							public void run() {
								t.addEntry(p.getName());
							}
						}, 10);
					} else if(DataManager.state.equals(GameState.Waiting)) {
						Scoreboard sc = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
						Team t = sc.getEntryTeam(p.getName());
						t.removeEntry(p.getName());
						NickModul.nickPlayerOnline(p, true);
						Bukkit.getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
							@Override
							public void run() {
								t.addEntry(p.getName());
							}
						}, 10);
					}
				}
			}
		}
		return true;
	}
	
	
}
