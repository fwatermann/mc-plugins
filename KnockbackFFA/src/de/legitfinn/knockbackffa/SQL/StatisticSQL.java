package de.legitfinn.knockbackffa.SQL;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.legitfinn.knockbackffa.main.DataManager;
import de.legitfinn.knockbackffa.main.main;

public class StatisticSQL {

	public static MySQL mysql;
	
	public static void connect() {
		mysql = new MySQL(SignAPI.getIp(), "PlayerStatistics", "PlayerStatistics", "LRAP7HGLhAMSvQ9j", main.getPlugin(main.class));
		mysql.update("CREATE TABLE IF NOT EXISTS KNOCKBACKFFA(ID int primary key auto_increment, UUID varchar(128), KILLS int Default '0', DEATH int Default '0', MAXSTREAK int Default '0')");
		mysql.update("CREATE TABLE IF NOT EXISTS KNOCKBACKFFAKITS(ID int primary key auto_increment, UUID varchar(128), KIT int Default '0', INVENTORY varchar(65000) Default 'none')");
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
	
	public static boolean hasKitsaved(int kit, UUID uuid) {
		try{
			ResultSet rs = mysql.query("SELECT * FROM KNOCKBACKFFAKITS WHERE UUID = '" + uuid.toString() + "' AND KIT = " + kit);
			return rs.next();
		} catch(SQLException ex){}
		return false;
	}
	
	public static boolean saveKit(int kit, UUID uuid, Inventory inv) {
		
		String invs = null;
		try{
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
	        
	        dataOutput.writeInt(inv.getSize());
	        
	        for (int i = 0; i < inv.getSize(); i++) {
	            dataOutput.writeObject(inv.getItem(i));
	        }
	        
	        dataOutput.close();
	        invs = Base64Coder.encodeLines(outputStream.toByteArray());
		}catch (Exception e){
			Player p = Bukkit.getPlayer(uuid);
			if(p != null){
				p.sendMessage(DataManager.prefix + "§cFehler beim Speichern des Kits.");
			}
			return false;
		}
		
		if(hasKitsaved(kit, uuid)){
			mysql.update("UPDATE KNOCKBACKFFAKITS SET INVENTORY = '" + invs + "' WHERE UUID = '" + uuid.toString() + "' AND KIT = " + kit);
		}else {
			mysql.update("INSERT INTO KNOCKBACKFFAKITS(UUID,KIT,INVENTORY) VALUES ('" + uuid.toString() + "','" + kit + "','" + invs + "')");
		}
		return true;
	}
	
	public static Inventory getKit(int kit, UUID uuid){
		
		try{
			String data = null;
			ResultSet rs = mysql.query("SELECT * FROM KNOCKBACKFFAKITS WHERE UUID = '" + uuid.toString() + "' AND KIT = " + kit);
			if(rs.next()) {
				data = rs.getString("INVENTORY");
			}
			
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
	        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
	        Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

	        for (int i = 0; i < inventory.getSize(); i++) {
	            inventory.setItem(i, (ItemStack) dataInput.readObject());
	        }
	        
	        dataInput.close();
	        return inventory;
		}catch (Exception e){}
        return null;
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
			ResultSet rs = mysql.query("SELECT * FROM " + gamemode + " ORDER BY " + part + " DESC LIMIT " + (place) + ", 1");
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
	
	
	
}
