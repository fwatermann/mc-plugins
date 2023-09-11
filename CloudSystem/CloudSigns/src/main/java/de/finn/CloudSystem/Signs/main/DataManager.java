package de.finn.CloudSystem.Signs.main;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.finn.CloudSystem.Signs.Signs.SignGroup;

public class DataManager {

	public static HashMap<String, SignGroup> gamemode_signGroup = new HashMap<String, SignGroup>();
	
	public static File config = new File("./plugins/Signs/signs.cfg");
	public static FileConfiguration conf = YamlConfiguration.loadConfiguration(config);
	
	public static int animation = 0;
	
	public static boolean debug = false;
	
}
