package de.legitfinn.ppermissions.PlayerData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import de.legitfinn.ppermissions.main.main;

public class PlayerData {

	public static MySQL mysql;
	
	public static void connect() {
		mysql = new MySQL("127.0.0.1", "PlayerData1", "PlayerData1", "Kk1QxhxyGsOdw7xQ", main.main);
		mysql.update("CREATE TABLE IF NOT EXISTS PlayerData(UUID varchar(128), NAME varchar(128))");
		mysql.update("CREATE TABLE IF NOT EXISTS TEXTURE(UUID varchar(128), VALUE varchar(1024), SIGNATUR varchar(1024))");
	}
	
	
	public static boolean isListedUUID(UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT UUID FROM PlayerData WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("UUID") != null;
			}
		} catch(SQLException ex){}
		return false;
	}
	
	public static void updateEntry(UUID uuid, String name) {
		if(!isListedUUID(uuid)) {
			mysql.update("INSERT INTO PlayerData(UUID, NAME) VALUES ('" + uuid.toString() + "','" + name + "')");
		} else {
			mysql.update("UPDATE PlayerData SET NAME = '" + name + "' WHERE UUID = '" + uuid.toString() + "'");
		}
	}
	
	public static String getName(UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT NAME FROM PlayerData WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("NAME");
			}
		} catch(SQLException ex){}
		return uuid.toString();
	}
	
	public static UUID getUUID(String name) {
		try{
			ResultSet rs = mysql.query("SELECT UUID FROM PlayerData WHERE NAME = '" + name + "'");
			if(rs.next()) {
				return UUID.fromString(rs.getString("UUID"));
			}
		} catch(SQLException ex){}
		return null;
	}
	
	public static boolean isInTextureDatabase(UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT UUID FROM TEXTURE WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("UUID") != null;
			}
		} catch(SQLException ex) {}
		return false;
	}
	
	public static void setSkinTexture(UUID uuid, String value, String signatur) {
		if(!isInTextureDatabase(uuid)) {
			mysql.update("INSERT INTO TEXTURE(UUID, VALUE, SIGNATUR) VALUES ('" + uuid.toString() + "','" + value + "','" + signatur + "')");
		} else {
			mysql.update("UPDATE TEXTURE SET VALUE = '" + value + "' WHERE UUID = '" + uuid.toString() + "'");
			mysql.update("UPDATE TEXTURE SET SIGNATUR = '" + signatur + "' WHERE UUID = '" + uuid.toString() + "'");
		}
	}
	
	public static String getValue(UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT VALUE FROM TEXTURE WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("VALUE");
			}
		} catch(SQLException ex) {}
		return null;
	}
	
	public static String getSignatur(UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT SIGNATUR FROM TEXTURE WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("SIGNATUR");
			}
		} catch(SQLException ex) {}
		return null;
	}
	
	public static String[] getRandomTexture() {
		ArrayList<String> uuids = new ArrayList<String>();
		try{
			ResultSet rs = mysql.query("SELECT UUID FROM TEXTURE");
			while(rs.next()) {
				uuids.add(rs.getString("UUID"));
			}
		} catch(SQLException ex) {}
		Random rnd = new Random();
		UUID uuid = UUID.fromString(uuids.get(rnd.nextInt(uuids.size())));
		String signatur = getSignatur(uuid);
		String value = getValue(uuid);
		return new String[] {value, signatur};
	}
	
	public static String[] getTexture(UUID uuid) {
		String signatur = getSignatur(uuid);
		String value = getValue(uuid);
		return new String[] {value, signatur};
	}
	 
}
