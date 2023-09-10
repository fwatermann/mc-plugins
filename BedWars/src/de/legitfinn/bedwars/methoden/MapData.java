package de.legitfinn.bedwars.methoden;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Main.Types.InformationTyps;
import de.legitfinn.bedwars.main.DataManager;
public class MapData {

	public static File file;
	public static FileConfiguration config;
	
	public static void load(File a) {
		file = a;
		config = YamlConfiguration.loadConfiguration(file);
		DataManager.team_color = getTeam();
		DataManager.team_players.clear();
		for(String all : getTeam().keySet()) {
			DataManager.team_players.put(all, new ArrayList<Player>());
		}
		
		SignAPI.sendChange(InformationTyps.MAP, getMapName() + " " + getTeamCount() + "x" + getPPTeam(), "own");
		SignAPI.sendChange(InformationTyps.MAX_PLAYERS, getPPTeam() * getTeamCount() + "", "own");
		DataManager.teams = getTeamCount();
		DataManager.maxTeamsize = getPPTeam();
		DataManager.map = getMapName();
	}
	
	public static int getTeamCount() {
		return config.getInt("teams.count");
	}
	
	public static int getPPTeam() {
		return config.getInt("teams.ppt");
	}
	
	public static HashMap<String, ChatColor> getTeam() {
		HashMap<String, ChatColor> ret = new HashMap<String, ChatColor>();
		for(String all : config.getStringList("teams.list")) {
			String[] a = all.split(";");
			ret.put(a[0], chatCodeToChatColor(a[1]));
		}
		return ret;
	}
	
	public static Location getSpawn(String team) {
		World w = Bukkit.getWorld(config.getString("teams.spawns." + team + ".World"));
		double x = config.getDouble("teams.spawns." + team  + ".X");
		double y = config.getDouble("teams.spawns." + team  + ".Y");
		double z = config.getDouble("teams.spawns." + team  + ".Z");
		float yaw = config.getInt("teams.spawns." + team  + ".Yaw");
		float pitch = config.getInt("teams.spawns." + team  + ".Pitch");
	
		Location loc = new Location(w, x, y, z, yaw, pitch);
		return loc;
	}
	
	public static Location[] getBed(String team) {
		World w = Bukkit.getWorld(config.getString("teams.bed." + team + ".1.World"));
		int x1 = config.getInt("teams.bed." + team + ".1.X");
		int y1 = config.getInt("teams.bed." + team + ".1.Y");
		int z1 = config.getInt("teams.bed." + team + ".1.Z");
		int x2 = config.getInt("teams.bed." + team + ".2.X");
		int y2 = config.getInt("teams.bed." + team + ".2.Y");
		int z2 = config.getInt("teams.bed." + team + ".2.Z");
		
		Location[] ret = new Location[]{new Location(w, x1, y1, z1), new Location(w, x2, y2, z2)};
		return ret;	
	}
	
	public static void setSpawn(String team, Location loc) {
		config.set("teams.spawns." + team + ".World", "Map");
		config.set("teams.spawns." + team + ".X", (loc.getBlockX() + 0.5D));
		config.set("teams.spawns." + team + ".Y", (loc.getBlockY() + 0.5D));
		config.set("teams.spawns." + team + ".Z", (loc.getBlockZ() + 0.5D));
		config.set("teams.spawns." + team + ".Yaw", loc.getYaw());
		config.set("teams.spawns." + team + ".Pitch", loc.getPitch());
		try{
			config.save(file);
		} catch(IOException ex){}
	}
	
	public static void setSpectator(Location loc) {
		config.set("Map.spectator.World", "Map");
		config.set("Map.spectator.X", (loc.getBlockX() + 0.5));
		config.set("Map.spectator.Y", (loc.getBlockY() + 0.5));
		config.set("Map.spectator.Z", (loc.getBlockZ() + 0.5));
		try{
			config.save(file);
		} catch(IOException ex){}
	}
	
	public static Location getSpectator() {
		World w = Bukkit.getWorld(config.getString("Map.spectator.World"));
		double x = config.getDouble("Map.spectator.X");
		double y = config.getDouble("Map.spectator.Y");
		double z = config.getDouble("Map.spectator.Z");
		return new Location(w,x,y,z);
	}
	
	
	public static void setBed(String team, Location[] locs) {
		config.set("teams.bed." + team + ".1.World", "Map");
		config.set("teams.bed." + team + ".1.X", locs[0].getBlockX());
		config.set("teams.bed." + team + ".1.Y", locs[0].getBlockY());
		config.set("teams.bed." + team + ".1.Z", locs[0].getBlockZ());
		config.set("teams.bed." + team + ".2.X", locs[1].getBlockX());
		config.set("teams.bed." + team + ".2.Y", locs[1].getBlockY());
		config.set("teams.bed." + team + ".2.Z", locs[1].getBlockZ());
		try{
			config.save(file);
		} catch(IOException ex){}
	}
	
	public static void setSpawner(Location loc, SpawnerType st, int i) {
		String a = "spawner." + st.toString() + "." + i;
		config.set(a + ".World", "Map");
		config.set(a + ".X", loc.getBlockX() + 0.5D);
		config.set(a + ".Y", loc.getBlockY() + 0.25D);
		config.set(a + ".Z", loc.getBlockZ() + 0.5D);
		try{
			config.save(file);
		} catch(IOException ex){}
	}
	
	public static Location getSpawner(SpawnerType st, int i) {
		String a = "spawner." + st.toString() + "." + i;
		if(config.isSet(a + ".World")) {
			World w = Bukkit.getWorld("Map");
			double x = config.getDouble(a + ".X");
			double y = config.getDouble(a + ".Y");
			double z = config.getDouble(a + ".Z");
			return new Location(w, x, y, z);
		} else {
			return null;
		}	
	}
	
	
	public static void setMapName(String name) {
		config.set("Map.name", name);
		try{
			config.save(file);
		} catch(IOException ex){}
	}
	
	public static String getMapName() {
		return config.getString("Map.name");
	}
	
	public static void setTeamCount(int i) {
		config.set("teams.count", i);
		try{
			config.save(file);
		} catch(IOException ex){}
	}
	
	public static void setTeams(HashMap<String, ChatColor> teams) {
		ArrayList<String> list = new ArrayList<String>();
		for(String all : teams.keySet()) {
			list.add(all + ";" + teams.get(all).toString());
		}
		config.set("teams.list", list);
		try{
			config.save(file);
		} catch(IOException ex){}
	}
	
	public static void setPperTeam(int i) {
		config.set("teams.ppt", i);
		try{
			config.save(file);
		} catch(IOException ex){}
	}
	
	public static void setBuilder(String builder) {
		config.set("Map.builder", builder);
		try{
			config.save(file);
		} catch(IOException ex){}		
	}
	
	public static String getBuilder() {
		return config.getString("Map.builder");
	}
	
	public static ChatColor chatCodeToChatColor(String code) {
		String c = code.replace("§", "").toLowerCase();
		if(c.equals("a")) {
			return ChatColor.GREEN;
		} else if(c.equals("b")) {
			return ChatColor.AQUA;
		} else if(c.equals("c")) {
			return ChatColor.RED;
		} else if(c.equals("d")) {
			return ChatColor.LIGHT_PURPLE;
		} else if(c.equals("e")) {
			return ChatColor.YELLOW;
		} else if(c.equals("f")) {
			return ChatColor.WHITE;
		} else if(c.equals("k")) {
			return ChatColor.MAGIC;
		} else if(c.equals("m")) {
			return ChatColor.STRIKETHROUGH;
		} else if(c.equals("n")) {
			return ChatColor.UNDERLINE;
		} else if(c.equals("l")) {
			return ChatColor.BOLD;
		} else if(c.equals("o")) {
			return ChatColor.ITALIC;
		} else if(c.equals("r")) {
			return ChatColor.RESET;
		} else if(c.equals("0")) {
			return ChatColor.BLACK;
		} else if(c.equals("1")) {
			return ChatColor.DARK_BLUE;
		} else if(c.equals("2")) {
			return ChatColor.DARK_GREEN;
		} else if(c.equals("3")) {
			return ChatColor.DARK_AQUA;
		} else if(c.equals("4")) {
			return ChatColor.DARK_RED;
		} else if(c.equals("5")) {
			return ChatColor.DARK_PURPLE;
		} else if(c.equals("6")) {
			return ChatColor.GOLD;
		} else if(c.equals("7")) {
			return ChatColor.GRAY;
		} else if(c.equals("8")) {
			return ChatColor.DARK_GRAY;
		} else if(c.equals("9")) {
			return ChatColor.BLUE;
		} else {
			return ChatColor.WHITE;
		}
	}
	
	
}
