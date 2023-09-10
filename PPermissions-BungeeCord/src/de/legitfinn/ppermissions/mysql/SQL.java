package de.legitfinn.ppermissions.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import de.legitfinn.ppermissions.main.main;

public class SQL {
	
	public static MySQL mysql;
//	public static MySQL mysql = new MySQL("127.0.0.1", "PPermissions", "PPermissions", "JCefzbS5EKbYebbE", main.getPlugin(main.class));
	
	public static void connect() {
		mysql = new MySQL("127.0.0.1", "PPermissions", "PPermissions", "mJDUmdDQnXJb39FGBmYu", main.main);
		mysql.update("CREATE TABLE IF NOT EXISTS Groups(NAME varchar(128), PREFIX varchar(128), COLOR varchar(128), SORTID int, IS_TEAM boolean, CAN_EDIT_USER_GROUPS boolean)");
		mysql.update("CREATE TABLE IF NOT EXISTS Users(UUID varchar(128), GROUPS varchar(128))");
	}
	
	public static boolean groupExists(String group) {
		try{
			ResultSet rs = mysql.query("SELECT * FROM Groups WHERE NAME = '" + group + "'");
			if(rs.next()) {
				if(rs.getString("NAME").equals(group)) {
					return true;
				}
			}
		} catch(SQLException ex){}
		return false;
	}
	
	public static ArrayList<String> getAllGroups() {
		ArrayList<String> ret = new ArrayList<String>();
		try{
			ResultSet rs = mysql.query("SELECT * FROM Groups");
			while(rs.next()) {
				ret.add(rs.getString("NAME"));
			}
		} catch(SQLException ex){}
		return ret;
	}
	
	public static boolean userExists(UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT * FROM Users WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				if(UUID.fromString(rs.getString("UUID")).equals(uuid)) {
					return true;
				}
			}
		} catch(SQLException ex){}
		return false;
	}
	
	public static String getGroupOfSortId(int id) {
		try{
			ResultSet rs = mysql.query("SELECT NAME FROM Groups WHERE SORTID = '" + id + "'");
			if(rs.next()) {
				return rs.getString("NAME");
			}
		} catch(SQLException ex){}
		return null;
	}
	
	public static int getSortIdOfGroup(String group) {
		try{
			ResultSet rs = mysql.query("SELECT SORTID FROM Groups WHERE NAME = '" + group + "'");
			if(rs.next()) {
				return rs.getInt("SORTID");
			}
		} catch(SQLException ex){}
		return -1;
	}
	
	
	public static void addGroup(String name, String prefix, String color) {
		if(!groupExists(name)) {
			mysql.update("INSERT INTO Groups (NAME, PREFIX, COLOR) VALUES ('" + name + "','" + prefix + "','" + color + "')");
		} else {
			if(!getGroupPrefix(name).equals(prefix)) {
				setGroupPrefix(name, prefix);
			}
			if(!getGroupColor(name).equals(color)) {
				setGroupColor(name, color);
			}
		}
	}
	
	public static String getGroupPrefix(String group) {
		try{
			ResultSet rs = mysql.query("SELECT PREFIX FROM Groups WHERE NAME = '" + group + "'");
			if(rs.next()) {
				return rs.getString("PREFIX");
			}
		} catch(SQLException ex){}
		return "UNKNOWN | ";
	}
	
	public static String getGroupColor(String group) {
		try{
			ResultSet rs = mysql.query("SELECT COLOR FROM Groups WHERE NAME = '" + group + "'");
			if(rs.next()) {
				return rs.getString("COLOR");
			}
		} catch(SQLException ex){}
		return "§fUNKNOWN";
	}
	
	public static void setGroupColor(String group, String color) {
		if(groupExists(group)) {
			mysql.update("UPDATE Groups SET COLOR = '" + color + "' WHERE NAME = '" + group + "'");
		}
	}
	
	public static void setGroupPrefix(String group, String prefix) {
		if(groupExists(group)) {
			mysql.update("UPDATE Groups SET PREFIX = '" + prefix + "' WHERE NAME = '" + group + "'");
		}
	}
	
	public static void addUser(UUID uuid, String groups) {
		if(!userExists(uuid)) {
			mysql.update("INSERT INTO Users(UUID, GROUPS) VALUES ('" + uuid.toString() + "','" + groups + "')");
		}
	}
	
	public static String getGroupsOfPlayer(UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT GROUPS FROM Users WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("GROUPS");
			}
		} catch(SQLException ex){}
		return null;
	}
	public static String[] getGroupListOfPlayer(UUID uuid) {
		if(userExists(uuid)) {
			try{
				ResultSet rs = mysql.query("SELECT GROUPS FROM Users WHERE UUID = '" + uuid.toString() + "'");
				if(rs.next()) {
					return rs.getString("GROUPS").split(";");
				}
			} catch(SQLException ex){}
		}
		return "".split("");
	}
	
	public static void addToGroup(UUID uuid, String group) {
		if(userExists(uuid)) {
			String groups = getGroupsOfPlayer(uuid);
			if(groups.isEmpty()) {
				groups = "{name=" + group + "}";
			} else {
				groups += ";{name=" + group + "}";
			}
			mysql.update("UPDATE Users SET GROUPS = '" + groups + "' WHERE UUID = '" + uuid.toString() + "'");
		}
	}
	
	public static void addToGroup(UUID uuid, String group, Date expire) {
		String a = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(expire);
		UUID b = uuid;
		String c = "{name=" + group + "!>expire=" + a + "}";
		if(userExists(uuid)) {
			String groups = getGroupsOfPlayer(b);
			if(groups.isEmpty()) {
				groups = c;
			} else {
				groups += ";" + c;
			}
			mysql.update("UPDATE Users SET GROUPS = '" + groups + "' WHERE UUID = '" + uuid.toString() + "'");
		}
	}
	
	public static void removeFromGroup(UUID uuid, String group) {
		if(userExists(uuid)) {
			String groups = getGroupsOfPlayer(uuid);
			if(groups.contains(group)) {
				if(!groups.equals(group)) {
					String[] a = groups.split(";");
					String b = "";
					for(String all : a) {
						if(!getGroupInformation(all, "name").equalsIgnoreCase(group)) {
							if(b.isEmpty()) {
								b = all;
							} else {
								b += ";" + all;
							}
						}
					}
					mysql.update("UPDATE Users SET GROUPS = '" + b + "' WHERE UUID = '" + uuid.toString() + "'");
				} 
			}
		}
	}
	
	public static int isTeamGroup(String group) {
		try{
			ResultSet rs = mysql.query("SELECT IS_TEAM FROM Groups WHERE NAME = '" + group + "'");
			if(rs.next()) {
				return rs.getInt("IS_TEAM");
			}
		} catch(SQLException ex){}
		return 0;
	}
	
	public static int canEditPerms(String group) {
		try{
			ResultSet rs = mysql.query("SELECT CAN_EDIT_USER_GROUPS FROM Groups WHERE NAME = '" + group + "'");
			if(rs.next()) {
				return rs.getInt("CAN_EDIT_USER_GROUPS");
			}
		} catch(SQLException ex){}
		return 0;
	}
	
	public static void removeFromAllGroups(UUID uuid) {
		mysql.update("UPDATE Users SET GROUPS = '' WHERE UUID = '" + uuid.toString() + "'");
	}
	
	public static boolean isExpired(Date d) {
		if(d.before(new Date())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getGroupInformation(String groupstring, String info) {
		String[] a = groupstring.replace("{", "").replace("}", "").split("!>");
		HashMap<String, String> info_value = new HashMap<String, String>();
		for(String all : a) {
			String[] b = all.split("=");
			String c = b[0];
			String d = b[1];
			info_value.put(c, d);
		}
		for(String all : info_value.keySet()) {
			if(all.equalsIgnoreCase(info)) {
				return info_value.get(all);
			}
		}
		return null;
	}
	
	public static boolean hasGroupRight(String group, String right) {
		if(spalteExists("Groups", right)) {
			try{
				ResultSet rs = mysql.query("SELECT " + right + " FROM Groups WHERE NAME = '" + group + "'");
				if(rs.next()) {
					int i = rs.getInt(right);
					return i == 1;
				}
			} catch(NullPointerException | SQLException ex){}
		}
		return false;
	}
	
	public static boolean hasPlayerRight(UUID uuid, String right) {
		if(spalteExists("Users", right)) {
			try{
				ResultSet rs = mysql.query("SELECT " + right + " FROM Users WHERE UUID = '" + uuid.toString() + "'");
				if(rs.next()) {
					int i = rs.getInt(right);
					return i == 1;
				}
			} catch(NullPointerException | SQLException ex){}
		}
		
		return false;
	}
	
	public static boolean hasGroupPerm(String group, String perm) {
		if(tabelExists("GROUP_" + group)) {
			try{
				ResultSet rs = mysql.query("SELECT * FROM GROUP_" + group.replace("+", "PLUS") + " WHERE PERM = '" + perm + "'");
				if(rs.next()) {
					if(rs.getString("PERM").equalsIgnoreCase(perm)) {
						return true;
					}
				}
			} catch(SQLException ex){}	
			return false;
		} else {
			System.out.println("Tabelle existiert nicht! Name: " + group.replace("+", "PLUS"));
			return false;
		}
	}
	
	private static boolean spalteExists(String table, String name) {
		try{
			ResultSet rs =  mysql.query("SELECT * FROM " + table);
			if(rs.next()) {
				int count = rs.getMetaData().getColumnCount();
				int i = 1;
				while(i <= count) {
					String a = rs.getMetaData().getColumnName(i);
					if(a.equalsIgnoreCase(name)) {
						return true;
					}
					i ++;
				}
			}
		} catch(SQLException ex){}
		return false;
 	}
	
	private static boolean tabelExists(String tabel) {
		try{
			ResultSet rs = mysql.query("SHOW TABLES LIKE '" + tabel.replace("+", "PLUS") + "'");
			if(rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch(SQLException ex){}
		return false;
	}
	
	
}
