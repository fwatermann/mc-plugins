package de.legitfinn.bedwars.methoden;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.legitfinn.bedwars.main.BedWars;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.replaysystem.main.ReplayAPI;
import net.md_5.bungee.api.ChatColor;

public class ScoreboardSystem {

	public static void createLobbyScoreboard(boolean teams) {
		Scoreboard sc = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
		if(teams) {
			for(Team all : sc.getTeams()) {
				all.unregister();
			}
			for(String all : DataManager.team_color.keySet()) {
				Team t = sc.registerNewTeam(all);
				t.setPrefix(DataManager.team_color.get(all) + "█ §7| " + DataManager.team_color.get(all));
				for(Player p : DataManager.team_players.get(all)) {
					t.addEntry(p.getName());
				}
			}
			Team spec = sc.registerNewTeam("Spectator");
			spec.setPrefix("§8✖ §7| §8");
			spec.setCanSeeFriendlyInvisibles(true);
			
			Team noTeam = sc.registerNewTeam("noTeam");
			noTeam.setPrefix("§8? §7| §8");
		}
		
		for(Objective all : sc.getObjectives()) {
			all.unregister();
		}
		
		Objective lobby = sc.registerNewObjective("Lobby", "dummy");
		lobby.setDisplayName("§7» §cBedWars §7«");
		lobby.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		lobby.getScore("§bMap:").setScore(5);
		lobby.getScore("  §c" + DataManager.map).setScore(4);
		lobby.getScore("§c").setScore(3);
		lobby.getScore("§bErbaut von:").setScore(2);
		String builder = MapData.getBuilder();
		if(builder == null || builder.equalsIgnoreCase("null")) {
			lobby.getScore("  §cUnknown").setScore(1);		
		} else {
			lobby.getScore("  §c" + builder.replace("_", " ")).setScore(1);	
		}
	}
	
	public static void createIngameScoreboard() {
		Scoreboard sc = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
		for(Objective all : sc.getObjectives()) {
			all.unregister();
		}
		
		Objective obj = sc.registerNewObjective("Ingame", "dummy");
		obj.setDisplayName("§7» §cBedWars §7«");
		String replayId = ReplayAPI.getInstance().getReplayId();
		
		obj.getScore("§bReplay-Id:").setScore(DataManager.maxTeamsize + 8);
		obj.getScore("   §6" + replayId).setScore(DataManager.maxTeamsize + 7);
		obj.getScore("§c").setScore(DataManager.maxTeamsize + 6);
		obj.getScore("§bRestzeit:").setScore(DataManager.maxTeamsize + 5);
		obj.getScore(lastTimeString).setScore(DataManager.maxTeamsize + 4);
		obj.getScore("§a").setScore(DataManager.maxTeamsize + 3);
		obj.getScore("§bTeams:").setScore(DataManager.maxTeamsize + 2);
		obj.getScore("§b").setScore(DataManager.maxTeamsize + 1);
		for(String all : DataManager.team_color.keySet()) {
			if(DataManager.team_bed.get(all)) {
				obj.getScore("   " + DataManager.team_color.get(all) + all + " §7» §a§l✔").setScore(DataManager.team_players.get(all).size());
			} else {
				obj.getScore("   " + DataManager.team_color.get(all) + all + " §7» §c§l✘").setScore(DataManager.team_players.get(all).size());
			}
		}
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	public static int seks = 0;
	public static int mins = 60;
	public static int countId;
	public static String lastTimeString = "  §660:00";
	public static int lastCode = 0;
	
	public static void startEndcountdown() {
		countId = Bukkit.getScheduler().scheduleSyncRepeatingTask(BedWars.getPlugin(BedWars.class), new Runnable() {
			
			@Override
			public void run() {
				Scoreboard sc = Bukkit.getScoreboardManager().getMainScoreboard();
				sc.resetScores(lastTimeString);
				seks --;
				if(seks < 0) {
					seks = 59;
					mins --;
				}
				String time = "  §6" + getTimeString();
				if(mins <= 0 && seks <= 60) {
					if(lastCode == 0) {
						lastCode = 1;
						time = "  §c" + getTimeString(); 
					} else if(lastCode == 1) {
						lastCode = 0;
						time = "  §6" + getTimeString();
					}
				}
				lastTimeString = time;
				Objective obj = sc.getObjective(DisplaySlot.SIDEBAR);
				if(obj != null) {
					obj.getScore(time).setScore(DataManager.maxTeamsize + 4);
				}
				if(seks <= 0 && mins <= 0) {
					Bukkit.getScheduler().cancelTask(countId);
					GameManager.endGame(true);
				}
			}
		}, 20, 20);
	}
	
	public static String getTimeString() {
		String ret = "";
		if(mins < 10) {
			ret += "0" + mins + ":";
		} else {
			ret += mins + ":";
		}
		if(seks < 10) {
			ret += "0" + seks;
		} else {
			ret += seks;
		}
		return ret;
	}
	
	public static int[] getColorOfTeamColor(ChatColor c) {
		if(c.equals(ChatColor.AQUA)) {
			return new int[]{66,241,244};
		}
		
		
		
		return new int[]{1,1,1};
	}
	
	
	
	
}
