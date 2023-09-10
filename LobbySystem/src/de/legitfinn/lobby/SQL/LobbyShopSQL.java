package de.legitfinn.lobby.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class LobbyShopSQL {

	public static MySQL mysql = CoinSQL.mysql;
	
	public static void connect() {
		mysql.update("CREATE TABLE IF NOT EXISTS BOOTS(ID int primary key auto_increment, UUID varchar(128), ITEMS varchar(128))");
		mysql.update("CREATE TABLE IF NOT EXISTS HEADS(ID int primary key auto_increment, UUID varchar(128), ITEMS varchar(128))");
		mysql.update("CREATE TABLE IF NOT EXISTS PETS(ID int primary key auto_increment, UUID varchar(128), ITEMS varchar(128))");
		mysql.update("CREATE TABLE IF NOT EXISTS DAILYREWARD(ID int primary key auto_increment, UUID varchar(128), TIME long)");
	}
	
	
	public static boolean isListed(String table, UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT * FROM " + table + " WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("UUID") != null;
			}
		} catch(SQLException ex){}
		return false;
	}
	
	public static void createEntry(UUID uuid) {
		mysql.update("INSERT INTO BOOTS(UUID) VALUES ('" + uuid.toString() + "')");
		mysql.update("INSERT INTO HEADS(UUID) VALUES ('" + uuid.toString() + "')");
		mysql.update("INSERT INTO PETS(UUID) VALUES ('" + uuid.toString() + "')");
	}
	
	private static String getBuyString(String table, UUID uuid) {
		if(!isListed(table, uuid)) {
			createEntry(uuid);
		}
		try{
			ResultSet rs = mysql.query("SELECT * FROM " + table + " WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("ITEMS");
			}
		} catch(SQLException ex){}
		return null;
	}
	
	public static boolean hasBought(String table, UUID uuid, int id) {
		String bought = getBuyString(table, uuid);
		if(bought != null) {
			String[] code = bought.split(";");
			if(code.length < id) {
				String a = bought;
				while(a.split(";").length < id) {
					a += ";0";
				}
				System.out.println(a);
				code = a.split(";");
				bought = a;
				setBoughtString(table, uuid, bought);
			}
			int i = Integer.parseInt(code[id-1]);
			return i == 1;
		}
		return false;
	}
	
	public static void buyItem(String table, UUID uuid, int id) {
		String bought = getBuyString(table, uuid);
		if(bought != null) {
			String[] code = bought.split(";");
			if(code.length < id) {
				String a = bought;
				while(a.split(";").length < id) {
					a += ";0";
				}
				System.out.println(a);
				code = a.split(";");
				bought = a;
				setBoughtString(table, uuid, bought);
			}
			code[id-1] = "1";
			String c = "";
			for(String all : code) {
				if(!c.isEmpty()) {
					c += ";" + all;
				} else {
					c += all;
				}
			}
			c.replaceFirst(";","");
			setBoughtString(table, uuid, c);
		}
	}
	
	public static void setBoughtString(String table, UUID uuid, String bought) {
		if(!isListed(table, uuid)) {
			createEntry(uuid);
		}
		mysql.update("UPDATE " + table + " SET ITEMS = '" + bought + "' WHERE UUID = '" + uuid.toString() + "'");
	}
	
	public static boolean canGetDaily(UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT * FROM DAILYREWARD WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("UUID") == null;
			}
		} catch(SQLException ex){}
		try{
			ResultSet rs = mysql.query("SELECT * FROM DAILYREWARD WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				long time = rs.getLong("TIME");
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				if(sdf.format(new Date()).equalsIgnoreCase(sdf.format(new Date(time)))) {
					return false;
				} else {
					return true;
				}
			}
		} catch(SQLException ex){}
		return true;
	}
	
	public static void getDailyReward(UUID uuid) {
		mysql.update("DELETE FROM DAILYREWARD WHERE UUID = '" + uuid.toString() + "'");
		mysql.update("INSERT INTO DAILYREWARD (UUID, TIME) VALUES ('" + uuid.toString() + "','" + System.currentTimeMillis() + "')");
	}
	
	
}
