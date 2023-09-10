package de.legitfinn.bedwars.methoden;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {

	public static File file = new File("plugins/BedWars/config.yml");
	public static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
	
	public static Location getLobbySpawn() {
		World w = Bukkit.getWorld("world");
		double x = cfg.getDouble("Lobby.X");
		double y = cfg.getDouble("Lobby.Y");
		double z = cfg.getDouble("Lobby.Z");
		int yaw = cfg.getInt("Lobby.Yaw");
		int pitch = cfg.getInt("Lobby.Pitch");
		return new Location(w,x,y,z,yaw,pitch);
	}
	
	public static Location getStatsLocation() {
		World w = Bukkit.getWorld("world");
		int x = cfg.getInt("Lobby.Stats.X");
		int y = cfg.getInt("Lobby.Stats.Y");
		int z = cfg.getInt("Lobby.Stats.Z");
		return new Location(w,x,y,z);
	}
	
	public static Location getTopLocation() {
		if(cfg.isSet("Top.World")) {
			World w = Bukkit.getWorld(cfg.getString("Top.World"));
			int x = cfg.getInt("Top.X");
			int y = cfg.getInt("Top.Y");
			int z = cfg.getInt("Top.Z");
			return new Location(w,x,y,z);
		} else {
			return null;
		}
	}
	
	public static String getTopDirection() {
		if(cfg.isSet("Top.Direction")) {
			return cfg.getString("Top.Direction");
		} else {
			return null;
		}
	}
	
	public static byte getTopFace() {
		if(cfg.isSet("Top.Face")) {
			return (byte) cfg.getInt("Top.Face");
		} else {
			return (byte)-1;
		}
	}
	
	
	
}
