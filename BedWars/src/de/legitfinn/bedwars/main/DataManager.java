package de.legitfinn.bedwars.main;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import de.legitfinn.bedwars.methoden.GameState;
import de.legitfinn.bedwars.methoden.Hologram;
import de.legitfinn.bedwars.methoden.SpawnerType;

public class DataManager {

	public static HashMap<String, ArrayList<Player>> team_players = new HashMap<String, ArrayList<Player>>();
	public static GameState state = GameState.Loading;
	public static String prefix = "§bBedWars §7» §3";
	public static int maxTeamsize = 4;
	public static int teams = 4;
	public static HashMap<String, ChatColor> team_color = new HashMap<String, ChatColor>();
	public static ArrayList<Block> placedBlocks = new ArrayList<Block>();
	public static String map = "none";
	public static HashMap<String, Boolean> team_bed = new HashMap<String, Boolean>();
	public static HashMap<String, Location[]> team_bedloc = new HashMap<String, Location[]>();
	public static HashMap<Location, SpawnerType> loc_spawner = new HashMap<Location, SpawnerType>();
	public static ArrayList<Player> troll = new ArrayList<Player>();
	public static HashMap<Player, Hologram> statisticholo = new HashMap<Player, Hologram>();	
}
