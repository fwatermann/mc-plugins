package de.legitfinn.lobby.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.EntityType;

public class LobbyPetSQL {

	public static MySQL mysql = LobbyServerSQL.mysql;
	
	public static void connect() {
		mysql.update("CREATE TABLE IF NOT EXISTS PETSETTINGS(ID int primary key auto_increment, UUID varchar(128), PETTYPE int, SIZE int, NAME varchar(128))");
	}
	
	public static boolean isListed(UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT * FROM PETSETTINGS WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("UUID") != null;
			}
		} catch(SQLException ex){}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static void addEntry(UUID uuid, EntityType entype, int size, String name) {
		if(!isListed(uuid)) {
			mysql.update("INSERT INTO PETSETTINGS (UUID, PETTYPE, SIZE, NAME) VALUES ('" + uuid.toString() + "','" + entype.getTypeId() + "','" + size + "','" + name.replace("'", "#1#") + "');");
		} else {
			mysql.update("DELETE FROM PETSETTINGS WHERE UUID = '" + uuid.toString() + "'");
			addEntry(uuid, entype, size, name);
		}
	}
	
	public static void remove(UUID uuid) {
		mysql.update("DELETE FROM PETSETTINGS WHERE UUID = '" + uuid.toString() + "'");
	}
	
	public static String getPetName(UUID uuid) {
		if(isListed(uuid)) {
			try{
				ResultSet rs = mysql.query("SELECT * FROM PETSETTINGS WHERE UUID = '" + uuid.toString() + "'");
				if(rs.next()) {
					return rs.getString("NAME").replaceAll("#1#", "'");
				}
			} catch(SQLException ex){}
		}
		return null;		
	}
	
	@SuppressWarnings("deprecation")
	public static EntityType getPetType(UUID uuid) {
		if(isListed(uuid)) {
			try{
				ResultSet rs = mysql.query("SELECT * FROM PETSETTINGS WHERE UUID = '" + uuid.toString() + "'");
				if(rs.next()) {
					return EntityType.fromId(rs.getInt("PETTYPE"));
				}
			} catch(SQLException ex){}
		}
		return null;
	}
	
	public static int getSize(UUID uuid) {
		if(isListed(uuid)) {
			try{
				ResultSet rs = mysql.query("SELECT * FROM PETSETTINGS WHERE UUID = '" + uuid.toString() + "'");
				if(rs.next()) {
					return rs.getInt("SIZE");
				}
			} catch(SQLException ex){}
		}
		return 0;
	}	
}
