package de.legitfinn.knockbackffa.main;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class DataManager {

	public static String prefix = "§5KnockFFA §7» §3";
	public static int Kit = 1;
	public static ArrayList<Player> ingame = new ArrayList<Player>();
	
	public static boolean loadingMap = true;
	public static World map = null;
	public static String mapname = "";
	
	public static ArrayList<String> alreadyMaps = new ArrayList<String>();
	public static boolean testmode = false;
	
	public static int KitAmount = 5;
	
	
	
}
