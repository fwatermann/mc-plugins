package de.legitfinn.ppermissions.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.bukkit.entity.Player;

import de.legitfinn.ppermissions.methoden.ScoreboardSystem;
import de.legitfinn.ppermissions.mysql.SQL;

public class PPermissions {

	public static boolean isInGroup(Player p, String group) {
		String[] groups = SQL.getGroupListOfPlayer(p.getUniqueId());
		for(String all : groups) {
			if(!all.isEmpty()) {
				String name = SQL.getGroupInformation(all, "name");
				try{
					if(SQL.isExpired(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(SQL.getGroupInformation(all, "expire")))) {
						SQL.removeFromGroup(p.getUniqueId(), SQL.getGroupInformation(all, "name"));
						continue;
					}
				} catch(ParseException | NullPointerException ex){}
				if(name.equalsIgnoreCase(group)) {
					return true;
				}
			}
		}		
		return false;
	}
	
	public static ArrayList<String> getAllGroupsOfPlayer(Player p) {
		ArrayList<String> ret = new ArrayList<String>();
		for(String all : SQL.getGroupListOfPlayer(p.getUniqueId())) {
			String name = SQL.getGroupInformation(all, "name");
			ret.add(name);
		}
		return ret;
	}
	
	public static boolean isInGroup(UUID p, String group) {
		String[] groups = SQL.getGroupListOfPlayer(p);
		for(String all : groups) {
			if(!all.isEmpty()) {
				String name = SQL.getGroupInformation(all, "name");
				try{
					if(SQL.isExpired(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(SQL.getGroupInformation(all, "expire")))) {
						SQL.removeFromGroup(p, SQL.getGroupInformation(all, "name"));
						continue;
					}
				} catch(ParseException | NullPointerException ex){}
				if(name.equalsIgnoreCase(group)) {
					return true;
				}
			}
		}		
		return false;
	}
	
	public static void addToGroupPermanently(Player p, String group) {
		SQL.addToGroup(p.getUniqueId(), group);
	}
	
	public static void addToGroupTemporaly(Player p, String group, Date expire) {
		SQL.addToGroup(p.getUniqueId(), group, expire);
	}
	
	public static void removeFromGroup(Player p, String group) {
		SQL.removeFromGroup(p.getUniqueId(), group);
	}
	
	public static void disableTablist() {
		ScoreboardSystem.delete();
	}
	
	public static void enableTablist() {
		ScoreboardSystem.genereateTeams();
	}
	
	public static void disableChat() {
		main.chat = false;
	}
	
	public static void enableChat() {
		main.chat = true;
	}
	
	public static void addGroups() {
		ScoreboardSystem.genereateTeams();
	}
	
	public static boolean isTeamGroup(String group) {
		return SQL.isTeamGroup(group) == 1;
	}
	
	public static boolean isInTeamGroup(Player p) {
		for(String all : getAllGroupsOfPlayer(p)) {
			if(isTeamGroup(all)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getShowGroup(Player p) {
		return ScoreboardSystem.getShowGroup(SQL.getGroupListOfPlayer(p.getUniqueId()));
	}
	
	public static String getChatColor(Player p) {
		String group = ScoreboardSystem.getShowGroup(SQL.getGroupListOfPlayer(p.getUniqueId()));
		String prefix = SQL.getGroupColor(group);
		return prefix;
	}
	
	public static String getChatColor(UUID uuid) {
		String group = ScoreboardSystem.getShowGroup(SQL.getGroupListOfPlayer(uuid));
		String prefix = SQL.getGroupColor(group);
		return prefix;
	}
	
	public static String getLowestChatColor() {
		String group = "";
		int high = -1;
		for(String all : SQL.getAllGroups()) {
			if(SQL.getSortIdOfGroup(all) > high) {
				high = SQL.getSortIdOfGroup(all);
				group = all;
			}
		}
		return SQL.getGroupColor(group);
	}
	
	public static Integer getSortIdOfGroup(String group){
		return SQL.getSortIdOfGroup(group);
	}
	
	public static boolean canEditPermissions(String group) {
		return SQL.canEditPerms(group) == 1;
	}
	
	public static boolean canEditPermissions(String[] group) {
		for(String all : group) {
			String a = SQL.getGroupInformation(all, "name");
			if(SQL.canEditPerms(a) == 1) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasRight(Player p, String right) {
		for(String all : getAllGroupsOfPlayer(p)) {
			if(SQL.hasGroupRight(all, right)) {
				return true;
			}
		}
		if(SQL.hasPlayerRight(p.getUniqueId(), right)) {
			return true;
		}
		
		return false;
	}	
	
	public static void loadNickGroups(Player p) {
		ScoreboardSystem.addToTeam(p);
	}
	
}
