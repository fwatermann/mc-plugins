package de.legitfinn.bedwars.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.legitfinn.NickApi.main.NickModul;
import de.legitfinn.NickApi.main.main;
import de.legitfinn.bedwars.main.BedWars;
import de.legitfinn.bedwars.methoden.TeamManager;

public class NickRefresh implements Listener {

	@EventHandler
	public void onLoginName(PlayerLoginEvent e) {
		if(main.nickname_uuid.containsKey(e.getPlayer().getName())) {
			UUID uuid = main.nickname_uuid.get(e.getPlayer().getName());
			Player p = Bukkit.getPlayer(uuid);
			refreshNick(p);
		}
	}
	
	public static void refreshNick(Player p) {
		String team = TeamManager.getTeamOfPlayer(p);
		if(team != null) {
			Scoreboard sc = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
			Team t = sc.getEntryTeam(p.getName());
			t.removeEntry(p.getName());
			NickModul.unNickPlayer(p, false);
			Bukkit.getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
				
				@Override
				public void run() {
					NickModul.nickPlayerOnline(p, true);
					Bukkit.getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
						@Override
						public void run() {
							t.addEntry(p.getName());
						}
					}, 10);
				}
			}, 10);
		} else {
			Scoreboard sc = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
			Team t = sc.getEntryTeam(p.getName());
			t.removeEntry(p.getName());
			NickModul.unNickPlayer(p, false);
			Bukkit.getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
				
				@Override
				public void run() {
					NickModul.nickPlayerOnline(p, true);
					Bukkit.getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
						@Override
						public void run() {
							t.addEntry(p.getName());
						}
					}, 10);
				}
			}, 10);
		}
	}
	
	
	
}
