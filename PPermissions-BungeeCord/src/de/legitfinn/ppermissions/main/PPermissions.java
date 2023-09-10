package de.legitfinn.ppermissions.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import de.legitfinn.ppermissions.mysql.SQL;

public class PPermissions {
	
	public static ArrayList<String> getAllGroupsOfPlayer(UUID uuid) {
		ArrayList<String> ret = new ArrayList<String>();
		for(String all : SQL.getGroupListOfPlayer(uuid)) {
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
	
	public static void addToGroupPermanently(UUID uuid, String group) {
		SQL.addToGroup(uuid, group);
	}
	
	public static void addToGroupTemporaly(UUID uuid, String group, Date expire) {
		SQL.addToGroup(uuid, group, expire);
	}
	
	public static void removeFromGroup(UUID uuid, String group) {
		SQL.removeFromGroup(uuid, group);
	}
	
	public static boolean isTeamGroup(String group) {
		return SQL.isTeamGroup(group) == 1;
	}
	
	public static boolean isInTeamGroup(UUID uuid) {
		for(String all : getAllGroupsOfPlayer(uuid)) {
			if(isTeamGroup(all)) {
				return true;
			}
		}
		return false;
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
	
	public static String getChatColor(UUID uuid) {
		String group = getShowGroup(SQL.getGroupListOfPlayer(uuid));
		String prefix = SQL.getGroupColor(group);
		return prefix;
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
	
	public static boolean hasRight(UUID uuid, String right) {
		for(String all : getAllGroupsOfPlayer(uuid)) {
			if(SQL.hasGroupRight(all, right)) {
				return true;
			}
		}
		if(SQL.hasPlayerRight(uuid, right)) {
			return true;
		}
		return false;
	}
	
	
}
