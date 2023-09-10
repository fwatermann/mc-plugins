package de.legitfinn.ppermissions.methoden;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import de.legitfinn.NickApi.main.NickModul;
import de.legitfinn.ppermissions.listener.ChatListener;
import de.legitfinn.ppermissions.mysql.SQL;

public class ScoreboardSystem {

	private static HashMap<String, Team> teams = new HashMap<String, Team>();
	public static boolean activeTab = false;
	
	public static void genereateTeams() {
		for(Team all : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
			all.unregister();
		}
		teams.clear();
		for(String all : SQL.getAllGroups()) {
			Team team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(SQL.getSortIdOfGroup(all) + all);
			team.setPrefix(SQL.getGroupPrefix(all));
			teams.put(all, team);
			System.out.println(all + " | " + team.getName());
		}
		
		activeTab = true;
		for(Player all : Bukkit.getOnlinePlayers()) {
			addToTeam(all);
		}
	}
	
	public static void addToTeam(Player p) {
		if(!activeTab) {
			return;
		}
		Team t = Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(p.getName());
		if(t != null) {
			t.removeEntry(p.getName());
		}
		if(!NickModul.isNicked(p.getUniqueId())) {
			String group = getShowGroup(SQL.getGroupListOfPlayer(p.getUniqueId()));		
			if(teams.containsKey(group)) {
				teams.get(group).addEntry(p.getName());
			} else {
				genereateTeams();
			}
		} else {
			String group = ChatListener.getLowestGroup();
			if(teams.containsKey(group)) {
				teams.get(group).addEntry(p.getName());
			} else {
				genereateTeams();
			}
		}
	}
	
	public static String getShowGroup(String[] groups) {
		int i = 100000;
		String group = groups[0];
		for(String all : groups) {
			String a = SQL.getGroupInformation(all, "name");
			int sid = SQL.getSortIdOfGroup(a);
			if(sid < i) {
				i = sid;
				group = a;
			}
		}
		return group;
	}
	
	public static void delete() {
		activeTab = false;
		for(String all : teams.keySet()) {
			Team t = teams.get(all);
			t.unregister();
		}
		teams.clear();
	}
	
}
