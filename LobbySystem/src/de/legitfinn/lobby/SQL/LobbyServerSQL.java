package de.legitfinn.lobby.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.legitfinn.lobby.main.main;

public class LobbyServerSQL {

	public static MySQL mysql;
	
	public static void connect() {
		mysql = new MySQL(SignAPI.getIp(), "LobbySystem", "LobbySystem", "iOWCXY2HJaoSp1aP", main.getPlugin(main.class));
		mysql.update("CREATE TABLE IF NOT EXISTS SETTINGS(ID int primary key auto_increment, UUID varchar(128), DOUBLEJUMP int, SCOREBOARD int, VISIBLE int, PETS int, PARTICLES int, CHAT int)"); // <-------
		mysql.update("CREATE TABLE IF NOT EXISTS PLAYERS(ID int primary key auto_increment, UUID varchar(128), LOCX DOUBLE, LOCY DOUBLE, LOCZ DOUBLE, INSILENT BOOLEAN)");
	}
	
	public static boolean playerListed(UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT * FROM PLAYERS WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getString("UUID") != null;
			}
			return false;
		} catch(SQLException ex){}
		return false;
	}
	
	public static void listPlayer(UUID uid, Location loc, boolean insilent) {
		String uuid = uid.toString();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		int silent = 0;
		if(insilent) {
			silent = 1;
		}
		mysql.update("DELETE FROM PLAYERS WHERE UUID = '" + uuid + "'");
		mysql.update("INSERT INTO PLAYERS(UUID, LOCX, LOCY, LOCZ, LOCYAW, LOCPITCH, INSILENT) VALUES ('" + uuid + "','" + x + "','" + y +"','" + z + "','" + yaw + "','" + pitch + "','" + silent + "')");
	}
	
	public static Location getLobbyLocation(UUID uuid, World w) {
		try{
			ResultSet rs = mysql.query("SELECT * FROM PLAYERS WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				double x = rs.getDouble("LOCX");
				double y = rs.getDouble("LOCY");
				double z = rs.getDouble("LOCZ");
				float yaw = rs.getFloat("LOCYAW");
				float pitch = rs.getFloat("LOCPITCH");
				Location ret = new Location(w, x, y, z, yaw, pitch);
				return ret;
			}
		} catch(SQLException ex){}
		return w.getSpawnLocation();
	}
	
	public static boolean getInSilent(UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT * FROM PLAYERS WHERE UUID = '" + uuid.toString() + "'");
			if(rs.next()) {
				return rs.getInt("INSILENT") == 1;
			}
		} catch(SQLException ex){}
		return false;		
	}
	
	public static void setInSilent(UUID uuid, boolean b) {
		if(b) {
			mysql.update("UPDATE PLAYERS SET INSILENT = '1' WHERE UUID = '" + uuid.toString() + "'");
		} else {
			mysql.update("UPDATE PLAYERS SET INSILENT = '0' WHERE UUID = '" + uuid.toString() + "'");
		}
	}
	
	public static void setLocation(UUID uuid, Location loc) {
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float yaw = loc.getYaw();
		float pitch = loc.getPitch();
		mysql.update("UPDATE PLAYERS SET LOCX = '" + x + "' WHERE UUID = '" + uuid + "'");
		mysql.update("UPDATE PLAYERS SET LOCY = '" + y + "' WHERE UUID = '" + uuid + "'");
		mysql.update("UPDATE PLAYERS SET LOCZ = '" + z + "' WHERE UUID = '" + uuid + "'");
		mysql.update("UPDATE PLAYERS SET LOCYAW = '" + yaw + "' WHERE UUID = '" + uuid + "'");
		mysql.update("UPDATE PLAYERS SET LOCPITCH = '" + pitch + "' WHERE UUID = '" + uuid + "'");
	}
	
	
}
