package de.legitfinn.lobby.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LobbySettingsSQL {

	public static MySQL mysql = LobbyServerSQL.mysql;
	
	public static boolean isListed(UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT * FROM SETTINGS WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("UUID") != null;
			}
		} catch(SQLException ex){}
		return false;
	}
	
	public static void addToList(UUID uuid, int jump, int scboard, int visible, int pets, int particles, int chat) {
		mysql.update("DELETE FROM SETTINGS WHERE UUID = '" + uuid.toString() + "'");
		mysql.update("INSERT INTO SETTINGS (UUID, DOUBLEJUMP, SCOREBOARD, VISIBLE, PETS, PARTICLES, CHAT) VALUES ('" + uuid.toString() + "','" + jump + "','" + scboard + "','" + visible + "','" + pets + "','" + particles + "','" + chat + "')");
	}
	
	public static boolean getSettingsBoolean(UUID uuid, String setting) {
		if(!isListed(uuid)) {
			addToList(uuid, 1, 1, 2, 1, 1, 1);
		}
		int ret = 0;
		try{
			ResultSet rs = mysql.query("SELECT * FROM SETTINGS WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				ret = rs.getInt(setting);
			}
		} catch(SQLException ex){}
		return ret == 1;
	}
	
	public static int getSettingsInt(UUID uuid, String setting) {
		if(!isListed(uuid)) {
			addToList(uuid, 1, 1, 2, 1, 1, 1);
		}
		int ret = 0;
		try{
			ResultSet rs = mysql.query("SELECT * FROM SETTINGS WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				ret = rs.getInt(setting);
			}
		} catch(SQLException ex){}
		return ret;
	}
	
	public static void setSettings(UUID uuid, String setting, int value) {
		if(!isListed(uuid)) {
			addToList(uuid, 1, 1, 2, 1, 1, 1);
		}
		mysql.update("UPDATE SETTINGS SET " + setting + " = '" + value + "' WHERE UUID = '" + uuid.toString() + "'");
	}
}
