package de.legitfinn.knockbackffa.methoden;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.legitfinn.knockbackffa.SQL.StatisticSQL;

public class ScoreboardSystem {

	private static HashMap<Player, Integer> lastStreak = new HashMap<Player, Integer>();
	private static HashMap<Player, Integer> lastKills = new HashMap<Player, Integer>();
	private static HashMap<Player, Integer> lastDeath = new HashMap<Player, Integer>();
	private static HashMap<Player, Double> lastKd = new HashMap<Player, Double>();
	
	public static void createScoreboard(Player p) {
		Scoreboard msc = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
		Scoreboard sc = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		for(Team all : msc.getTeams()) {
			Team t = sc.registerNewTeam(all.getName());
			t.setDisplayName(all.getDisplayName());
			t.setPrefix(all.getPrefix());
			t.setSuffix(all.getSuffix());
			for(String en : all.getEntries()) {
				t.addEntry(en);
			}
		}
		
		int kills = StatisticSQL.getInt("KNOCKBACKFFA", "kills", p.getUniqueId());
		int death = StatisticSQL.getInt("KNOCKBACKFFA", "death", p.getUniqueId());
		int streak = StatisticSQL.getInt("KNOCKBACKFFA", "MAXSTREAK", p.getUniqueId());
		double kd = (double)kills / (double)death;
		kd = Math.round(kd * 1000) / (double)1000;
		
		lastStreak.put(p, kills);
		lastDeath.put(p, death);
		lastKills.put(p, kills);
		lastKd.put(p, kd);
		
		Objective obj = sc.registerNewObjective("knockbackffa", "dummy");
		obj.setDisplayName("§c§lKnockbackFFA");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		obj.getScore(getSpacer(1)).setScore(12);
		obj.getScore("§eBeste Serie:").setScore(11);
		obj.getScore("  §§§c" + streak).setScore(10);
		obj.getScore(getSpacer(2)).setScore(9);
		obj.getScore("§eKills:").setScore(8);
		obj.getScore("  §c" + kills).setScore(7);
		obj.getScore(getSpacer(3)).setScore(6);
		obj.getScore("§eDeath:").setScore(5);
		obj.getScore("  §§§§§c" + death).setScore(4);
		obj.getScore(getSpacer(4)).setScore(3);
		obj.getScore("§eK/D:").setScore(2);
		obj.getScore("  §§§§§§§c" + kd).setScore(1);
		
		p.setScoreboard(sc);
	}
	
	public static void updateScoreboard(Player p, boolean tablist) {
		Scoreboard sc = p.getScoreboard();
		
		if(tablist) {
			Scoreboard msc = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
			for(Team all : sc.getTeams()) {
				all.unregister();
			}
			for(Team all : msc.getTeams()) {
				Team t = sc.registerNewTeam(all.getName());
				t.setDisplayName(all.getDisplayName());
				t.setPrefix(all.getPrefix());
				t.setSuffix(all.getSuffix());
				for(String en : all.getEntries()) {
					t.addEntry(en);
				}
			}
		}
		
		Objective obj = sc.getObjective("knockbackffa");
		
		if(obj == null) {
			createScoreboard(p);
		}	
		
		int kills = StatisticSQL.getInt("KNOCKBACKFFA", "kills", p.getUniqueId());
		int death = StatisticSQL.getInt("KNOCKBACKFFA", "death", p.getUniqueId());
		int streak = StatisticSQL.getInt("KNOCKBACKFFA", "MAXSTREAK", p.getUniqueId());
		double kd = (double)kills / (double)death;
		kd = Math.round(kd * 1000) / (double)1000;
		
		if(lastStreak.get(p) != streak) {
			sc.resetScores("  §§§c" + lastStreak.get(p));
			obj.getScore("  §§§c" + streak).setScore(10);
			lastStreak.put(p, streak);
		}
		if(lastKills.get(p) != kills) {
			sc.resetScores("  §c" + lastKills.get(p));
			obj.getScore("  §c" + kills).setScore(7);;
			lastKills.put(p, kills);
		}
		if(lastDeath.get(p) != death) {
			sc.resetScores("  §§§§§c" + lastDeath.get(p));
			obj.getScore("  §§§§§c" + death).setScore(4);;
			lastDeath.put(p, death);
		}
		if(lastKd.get(p) != kd) {
			sc.resetScores("  §§§§§§§c" + lastKd.get(p));
			obj.getScore("  §§§§§§§c" + kd).setScore(1);;
			lastKd.put(p, kd);
		}
	}
	
	public static String getSpacer(int id) {
		int i = 1;
		String ret = "";
		while(i <= id) {
			ret += "§§";
			i ++;
		}
		return ret;
	}
	
	
}
