package de.legitfinn.bedwars.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.legitfinn.bedwars.main.BedWars;

public class StatisticSQL {

	public static MySQL mysql;
	
	public static void connect() {
		mysql = new MySQL(SignAPI.getIp(), "PlayerStatistics", "PlayerStatistics", "LRAP7HGLhAMSvQ9j", BedWars.getPlugin(BedWars.class));
		mysql.update("CREATE TABLE IF NOT EXISTS BEDWARS(ID int primary key auto_increment, UUID varchar(128), KILLS int Default '0', DEATH int Default '0', WINS int Default '0', GAMES int Default '0', BEDS int Default '0')");
	}
	
	public static int getInt(String gamemode, String part, UUID uuid) {
		if(!isListed(gamemode, uuid)) {
			createEntry(gamemode, uuid);
		}
		try{
			ResultSet rs = mysql.query("SELECT * FROM " + gamemode + " WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getInt(part);
			}
		} catch(SQLException ex){}
		return -1;
	}
	
	public static void createEntry(String gamemode, UUID uuid) {
		mysql.update("INSERT INTO " + gamemode + "(UUID) VALUES ('" + uuid.toString() + "')");
	}
	
	public static boolean isListed(String gamemode, UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT * FROM " + gamemode + " WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("UUID") != null;
			}
		} catch(SQLException ex){}
		return false;
	}
	
	public static void setInt(String gamemode, String part, int amount, UUID uuid) {
		if(!isListed(gamemode, uuid)) {
			createEntry(gamemode, uuid);
		}
		mysql.update("UPDATE " + gamemode + " SET " + part + " = '" + amount + "' WHERE UUID = '" + uuid.toString() + "'");
	}
	
	public static void removeInt(String gamemode, String part, int amount, UUID uuid) {
		int a = getInt(gamemode, part, uuid);
		int b = amount;
		int c = a - b;
		setInt(gamemode, part, c, uuid);
	}
	
	public static void addInt(String gamemode, String part, int amount, UUID uuid) {
		int a = getInt(gamemode, part, uuid);
		int b = amount;
		int c = a + b;
		setInt(gamemode, part, c, uuid);
	}
	
	public static Integer getPlace(String gamemode, String part, UUID uuid){
		int i = -1;
		
		try{
			ResultSet rs = mysql.query("SELECT * FROM " + gamemode + " ORDER BY " + part + " DESC");
			while(rs.next()){
				if(i == -1){
					i = 1;
				}else {
					i++;
				}
				UUID u = UUID.fromString(rs.getString("UUID"));
				if(u.toString().equals(uuid.toString())){
					return i;
				}
			}
		} catch(SQLException ex){}
		
		return i;
	}
	
	public static HashMap<Integer, UUID> getPlaces(int highest, String part, String gamemode){
		
		HashMap<Integer, UUID> ret = new HashMap<Integer, UUID>();
		
		try{
			ResultSet rs = mysql.query("SELECT * FROM " + gamemode + " ORDER BY " + part + " DESC LIMIT " + highest);
			int p = 0;
			while(rs.next()){
				p++;
				ret.put(p, UUID.fromString(rs.getString("UUID")));
			}
		} catch(SQLException ex){}
		
		return ret;
		
	}
	
	public static UUID getUUIDOfPlace(String gamemode, String part, int place){
		try{
			ResultSet rs = mysql.query("SELECT * FROM " + gamemode + " ORDER BY " + part + " DESC LIMIT " + (place - 1) + ", 1");
			if(rs.next()){
				return UUID.fromString(rs.getString("UUID"));
			}
		} catch(SQLException ex){}
		
		return null;
	}
	
	public static Integer getExistingPlayers(String gamemode){
		try{
			ResultSet rs = mysql.query("SELECT COUNT(*) AS Players FROM " + gamemode);
			if(rs.next()){
				return rs.getInt("Players");
			}
		} catch(SQLException ex){}
		return -1;
	}
	
	public static UUID getRandomStatsUUID(String gamemode) {
		int count = 0;
		try {
			ResultSet rs = mysql.query("SELECT COUNT(*) AS amount FROM " + gamemode);
			if(rs.next()) {
				count = rs.getInt(count);
			}
			rs.close();
		} catch(SQLException ex) {}
		int entry = new Random().nextInt(count);
		UUID uuid = null;
		try {
			ResultSet rs = mysql.query("SELECT UUID FROM " + gamemode + " ORDER BY DESC LIMIT " + entry + ", 1");
			if(rs.next()) {
				uuid = UUID.fromString(rs.getString("UUID"));
			}
			rs.close();
		} catch(SQLException ex) {}
		return uuid;
	}
	
}
