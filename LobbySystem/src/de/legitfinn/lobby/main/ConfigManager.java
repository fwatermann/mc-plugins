package de.legitfinn.lobby.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

	public static File file = new File("plugins/LobbySystem/config.yml");
	public static FileConfiguration config = YamlConfiguration.loadConfiguration(file);
	
	public static boolean getBoolean(String opt) {
		return config.getBoolean(opt);
	}
	public static int getInt(String opt) {
		return config.getInt(opt);
	}
	public static String getString(String opt) {
		return config.getString(opt);
	}
	public static ArrayList<?> getArrayList(String opt) {
		return (ArrayList<?>) config.getList(opt);
	}
	public static Location getLocation(String opt) {
		if(!config.isSet(opt + ".X")) {
			return null;
		}
		World w = Bukkit.getWorld(config.getString(opt + ".World"));
		double x = config.getDouble(opt + ".X");
		double y = config.getDouble(opt + ".Y");
		double z = config.getDouble(opt + ".Z");
		Location loc = new Location(w,x,y,z);
		if(config.isSet(opt + ".Yaw")) {
			loc.setYaw(config.getInt(opt + ".Yaw"));
		}
		if(config.isSet(opt + ".Pitch")) {
			loc.setPitch(config.getInt(opt + ".Pitch"));
		}
		return loc;
	}
	
	public static void setLocation(String opt, Location loc) {
		set(opt + ".World", loc.getWorld().getName());
		set(opt + ".X", loc.getX());
		set(opt + ".Y", loc.getY());
		set(opt + ".Z", loc.getZ());
		set(opt + ".Yaw", loc.getYaw());
		set(opt + ".Pitch", loc.getPitch());
	}
	
	public static void set(String opt, Object obj) {
		config.set(opt, obj);
		try{
			config.save(file);
		} catch(IOException ex){}
	}
	
	public static void createDefault() {
		if(!config.isSet("IsSilentLobby")) {
			config.set("IsSilentLobby", false);
		}
		if(!config.isSet("LobbyId")) {
			config.set("LobbyId", 01);
		}
		try{
			config.save(file);
		} catch(IOException ex){}
	}
	
}
