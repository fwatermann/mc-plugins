package de.legitfinn.lobby.SQL;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.common.primitives.Ints;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.legitfinn.lobby.main.main;

public class StatisticSQL {

	public static MySQL mysql;
	public static HashMap<String, int[]> cache = new HashMap<>();
	
	public static void connect() {
		mysql = new MySQL(SignAPI.getIp(), "PlayerStatistics", "PlayerStatistics", "LRAP7HGLhAMSvQ9j", main.getPlugin(main.class));
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
		} catch(Exception ex){}
		return -1;
	}
	
	public static int[] getStatisticAsIntArray(String gamemode, String[] fields, UUID uuid) {
		String hashmap = uuid.toString() + "/./" + gamemode.toLowerCase();
		if(!cache.containsKey(hashmap)){
			if(!isListed(gamemode, uuid)) {
				createEntry(gamemode, uuid);
			}
			List<Integer> list = new ArrayList<Integer>();
			try {
				ResultSet rs = mysql.query("SELECT * FROM " + gamemode + " WHERE UUID = '" + uuid.toString() + "'");
				if(rs.next()) {
					for(String all : fields) {
						list.add(rs.getInt(all));
					}
				}
				rs.close();
				int[] ret = Ints.toArray(list);
				cache.put(hashmap, ret);
				return ret;
			} catch(Exception ex) {}
			for(int i = 0; i < fields.length; i++) {
				list.add(-1);
			}
			int[] ret = Ints.toArray(list);
			cache.put(hashmap, ret);
			return ret;
		}else {
			return cache.get(hashmap);
		}
	}
	
	public static double getDouble(String gamemode, String part, UUID uuid) {
		if(!isListed(gamemode, uuid)) {
			createEntry(gamemode, uuid);
		}
		try{
			ResultSet rs = mysql.query("SELECT * FROM " + gamemode + " WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getDouble(part);
			}
		} catch(Exception ex){}
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
		} catch(Exception ex){}
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
		} catch(Exception ex){}
		
		return i;
	}
	
	public static Integer getPlaceASC(String gamemode, String part, UUID uuid){
		int i = -1;
		
		try{
			ResultSet rs = mysql.query("SELECT * FROM " + gamemode + " ORDER BY " + part + " ASC");
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
		} catch(Exception ex){}
		
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
		} catch(Exception ex){}
		
		return ret;
		
	}
	
	public static UUID getUUIDOfPlace(String gamemode, String part, int place){
		try{
			ResultSet rs = mysql.query("SELECT * FROM " + gamemode + " ORDER BY " + part + " DESC LIMIT " + (place - 1) + ", 1");
			if(rs.next()){
				return UUID.fromString(rs.getString("UUID"));
			}
		} catch(Exception ex){}
		
		return null;
	}
	
	public static Integer getExistingPlayers(String gamemode){
		try{
			ResultSet rs = mysql.query("SELECT COUNT(*) AS Players FROM " + gamemode);
			if(rs.next()){
				return rs.getInt("Players");
			}
		} catch(Exception ex){}
		return -1;
	}
	
	
	
}
