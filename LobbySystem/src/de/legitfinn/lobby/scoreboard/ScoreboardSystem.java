package de.legitfinn.lobby.scoreboard;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.legitfinn.friend.FriendSQL;
import de.legitfinn.lobby.SQL.CoinSQL;
import de.legitfinn.lobby.SQL.LobbySettingsSQL;
import de.legitfinn.lobby.items.Hotbar;
import de.legitfinn.ppermissions.main.PPermissions;
import de.legitfinn.ppermissions.main.PlayerRankChangeEvent;
import de.legitfinn.ppermissions.main.main;

public class ScoreboardSystem implements Listener {

	public static int ani = 1;
	private static boolean running = false;
	private static HashMap<Player, String> lastFriendString = new HashMap<Player, String>();
	private static HashMap<Player, String> lastCoinsString = new HashMap<Player, String>();
	private static HashMap<Player, String> lastRangString = new HashMap<Player, String>();
	
	public static void animation() {
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				for(Player all : Bukkit.getOnlinePlayers()) {
					updateScoreboard(all, false, true);
				}
				ani ++;
				if(ani > 2) {
					ani = 1;
				}
				animation();
			}
		}, 20*5);
	}
	
	@EventHandler
	public void onRank(PlayerRankChangeEvent e) {
		for(Player all : Bukkit.getOnlinePlayers()) {
			updateScoreboard(all, true, false);
		}
		Player p = Bukkit.getPlayer(e.getPlayer());
		if(p != null && p.isOnline()) {
			Hotbar.createHotbar(p);
		}
	}
	
	public static void updateScoreboard(Player p, boolean tablist, boolean animation) {
		if(!running) {
			animation();
			running = true;
		}
		Scoreboard sc = p.getScoreboard();
		if(tablist) {
			for(Team all : sc.getTeams()) {
				all.unregister();
			}
			for(Team all : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
				Team t = sc.registerNewTeam(all.getName());
				t.setPrefix(all.getPrefix());
				for(String ent : all.getEntries()) {
					t.addEntry(ent);
				}
			}	
		}
//		if(sc.getObjective("lobby") != null) {
//			sc.getObjective("lobby").unregister();
//		}
		Objective obj = sc.getObjective("lobby");
		if(obj != null) {
			if(!LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "SCOREBOARD")) {
				obj.setDisplaySlot(null);
			} else {
				obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			}
			obj.setDisplayName("§4§lPvPFun.net");
			obj.getScore("").setScore(12);
			obj.getScore("§6§lDein Rang:").setScore(11);
			if(lastRangString.containsKey(p)) {
				sc.resetScores(lastRangString.get(p));
			}
			String dd = " §f➥ " + PPermissions.getChatColor(p) + "" + PPermissions.getShowGroup(p);
			lastRangString.put(p, dd);
			obj.getScore(dd).setScore(10);
			obj.getScore(" ").setScore(9);
			if(lastCoinsString.containsKey(p)) {
				sc.resetScores(lastCoinsString.get(p));
			}
			String coinsString = " §f➥ §e" + CoinSQL.getCoins(p.getUniqueId());
			lastCoinsString.put(p, coinsString);
			obj.getScore("§6§lCoins:").setScore(8);
			obj.getScore(coinsString).setScore(7);
			obj.getScore("  ").setScore(6);
			obj.getScore("§6§lFreunde:").setScore(5);
			if(FriendSQL.getOnlineFriendCount(p.getUniqueId()) > 0) {
				if(lastFriendString.containsKey(p)) {
					sc.resetScores(lastFriendString.get(p));
				}
				String friendString = " §f➥ §a" + FriendSQL.getOnlineFriendCount(p.getUniqueId()) + "§7/§a" + FriendSQL.getFriends(p.getUniqueId()).size() + "§a online";
				lastFriendString.put(p, friendString);
				obj.getScore(friendString).setScore(4);
				obj.getScore("   ").setScore(3);
			} else {
				if(lastFriendString.containsKey(p)) {
					sc.resetScores(lastFriendString.get(p));
				}
				String friendString = " §f➥ §c0§7/§c" + FriendSQL.getFriends(p.getUniqueId()).size() + "§a online";
				lastFriendString.put(p, friendString);
				obj.getScore(friendString).setScore(4);
				obj.getScore("   ").setScore(3);
			}
			if(animation) {
				if(ani == 1) {
					sc.resetScores("§6§lTeamSpeak:");
					sc.resetScores(" §f➥ §bPvPFun.net");
					obj.getScore("§6§lWebsite:").setScore(2);
					obj.getScore(" §f➥ §bwww.PvPFun.net").setScore(1);
				} else if(ani == 2) {
					sc.resetScores("§6§lWebsite:");
					sc.resetScores(" §f➥ §bwww.PvPFun.net");
					obj.getScore("§6§lTeamSpeak:").setScore(2);
					obj.getScore(" §f➥ §bPvPFun.net").setScore(1);
				}
			} else {
				if(ani == 2) {
					sc.resetScores("§6§lTeamSpeak:");
					obj.getScore("§6§lWebsite:").setScore(2);
					sc.resetScores(" §f➥ §bPvPFun.net");
					obj.getScore(" §f➥ §bwww.PvPFun.net").setScore(1);
				} else if(ani == 1) {
					sc.resetScores("§6§lWebsite:");
					obj.getScore("§6§lTeamSpeak:").setScore(2);
					sc.resetScores(" §f➥ §bwww.PvPFun.net");
					obj.getScore(" §f➥ §bPvPFun.net").setScore(1);
				}
			}
		}

	}	
	
	public static void createScoreboard(Player p) {
		if(!running) {
			animation();
			running = true;
		}
		Scoreboard sc = Bukkit.getScoreboardManager().getNewScoreboard();
		for(Team all : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
			Team t = sc.registerNewTeam(all.getName());
			t.setPrefix(all.getPrefix());
			for(String ent : all.getEntries()) {
				t.addEntry(ent);
			}
		}	
		if(sc.getObjective("lobby") != null) {
			sc.getObjective("lobby").unregister();
		}
		Objective obj = sc.registerNewObjective("lobby", "lobby");
		obj.setDisplayName("§4§lPvPFun.net");
		obj.getScore("").setScore(12);
		obj.getScore("§6§lDein Rang:").setScore(11);
		obj.getScore(" §f➥ " + PPermissions.getChatColor(p) + "" + PPermissions.getShowGroup(p)).setScore(10);
		obj.getScore(" ").setScore(9);
		String coinsString = " §f➥ §e" + CoinSQL.getCoins(p.getUniqueId());
		lastCoinsString.put(p, coinsString);
		obj.getScore("§6§lCoins:").setScore(8);
		obj.getScore(coinsString).setScore(7);
		obj.getScore("  ").setScore(6);
		obj.getScore("§6§lFreunde:").setScore(5);
		if(FriendSQL.getOnlineFriendCount(p.getUniqueId()) > 0) {
			String friendString = " §f➥ §a" + FriendSQL.getOnlineFriendCount(p.getUniqueId()) + "§7/§a" + FriendSQL.getFriends(p.getUniqueId()).size() + "§a online";
			lastFriendString.put(p, friendString);
			obj.getScore(friendString).setScore(4);
			obj.getScore("   ").setScore(3);
		} else {
			String friendString = " §f➥ §c0§7/§c" + FriendSQL.getFriends(p.getUniqueId()).size() + "§a online";
			lastFriendString.put(p, friendString);
			obj.getScore(friendString).setScore(4);
			obj.getScore("   ").setScore(3);
		}
		if(ani == 1) {
			obj.getScore("§6§lWebsite:").setScore(2);
			obj.getScore(" §f➥ §bwww.PvPFun.net").setScore(1);
		} else if(ani == 2) {
			obj.getScore("§6§lTeamSpeak:").setScore(2);
			obj.getScore(" §f➥ §bPvPFun.net").setScore(1);
		}
		if(!LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "SCOREBOARD")) {
			obj.setDisplaySlot(null);
		} else {
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		}
		p.setScoreboard(sc);
	}
}
