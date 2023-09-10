package de.legitfinn.NickApi.main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class PlayerData {

	public static MySQL mysql;
	
	public static boolean isListedUUID(UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT UUID FROM PlayerData WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("UUID") != null;
			}
		} catch(SQLException ex){}
		return false;
	}
	
	public static ArrayList<String> getAllNames() {
		ArrayList<String> ret = new ArrayList<String>();
		try{
			ResultSet rs = mysql.query("SELECT NAME FROM PlayerData");
			while(rs.next()) {
				ret.add(rs.getString("NAME"));
			}
		} catch(SQLException ex){}	
		return ret;
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
