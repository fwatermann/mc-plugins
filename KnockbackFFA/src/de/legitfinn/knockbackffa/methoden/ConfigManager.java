package de.legitfinn.knockbackffa.methoden;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.legitfinn.knockbackffa.main.DataManager;

public class ConfigManager {

	private static File file = new File("./plugins/KnockFFA/config.yml");
	private static FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
	
	public static void loadConfig() {
		if(!conf.getKeys(false).contains("Location")) {
			conf.set("Location.World", Bukkit.getWorlds().get(0).getName());
			conf.set("Location.X", Bukkit.getWorlds().get(0).getSpawnLocation().getX());
			conf.set("Location.Y", Bukkit.getWorlds().get(0).getSpawnLocation().getY());
			conf.set("Location.Z", Bukkit.getWorlds().get(0).getSpawnLocation().getZ());
		}
		if(!conf.getKeys(false).contains("Game")) {
			conf.set("Game.MaxPlayer", 25);
		}
		try{
			conf.save(file);
		} catch(IOException ex){}
	}
	
	public static Location getSpawn() {
		if(DataManager.map != null) {
			return DataManager.map.getSpawnLocation().clone().add(0.5, 0.5, 0.5);
		} else {
			return null;
		}
	}
	
	
	
	
	
	
}
