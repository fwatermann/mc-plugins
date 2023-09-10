package de.legitfinn.knockbackffa.main;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Main.Types;
import de.NightShadwo.SignAPI.Main.Types.Gamemode;
import de.NightShadwo.SignAPI.Main.Types.InformationTyps;
import de.legitfinn.knockbackffa.SQL.LobbySettingsSQL;
import de.legitfinn.knockbackffa.SQL.StatisticSQL;
import de.legitfinn.knockbackffa.cloud.ServerListener;
import de.legitfinn.knockbackffa.command.COMMAND_kffaadmin;
import de.legitfinn.knockbackffa.command.COMMAND_nick;
import de.legitfinn.knockbackffa.command.COMMAND_savekit;
import de.legitfinn.knockbackffa.command.COMMAND_stats;
import de.legitfinn.knockbackffa.command.COMMAND_troll;
import de.legitfinn.knockbackffa.listener.DeathListener;
import de.legitfinn.knockbackffa.listener.JoinListener;
import de.legitfinn.knockbackffa.listener.QuitListener;
import de.legitfinn.knockbackffa.listener.WorldListener;
import de.legitfinn.knockbackffa.methoden.ConfigManager;
import de.legitfinn.knockbackffa.methoden.Kits;
import de.legitfinn.knockbackffa.methoden.Troll;
public class main extends JavaPlugin {

	public static int maxPlayers = 25;
	public static ArrayList<Player> troll = new ArrayList<Player>();
	
	public void onEnable() {
		SignAPI.registerEvent(new ServerListener());
		SignAPI.sendChange(InformationTyps.GAMEMODE, Gamemode.KNOCKBACKFFA.toString(), "own");
		SignAPI.sendChange(InformationTyps.STATUS, Types.Status.WAITING.toString(), "own");
		SignAPI.sendChange(InformationTyps.MAX_PLAYERS, "" + maxPlayers, "own");
		SignAPI.sendChange(InformationTyps.PLAYERS, "0", "own");
		SignAPI.sendChange(InformationTyps.MAP, "Regenbogen", "own");
		
		ConfigManager.loadConfig();
		LobbySettingsSQL.connect();
		StatisticSQL.connect();
		
		register();
		Kits.changeMapCounter();
		Kits.kitChangeCounter();
		Kits.changeKit(new Random().nextInt(DataManager.KitAmount) + 1);
		Bukkit.getWorlds().get(0).setDifficulty(Difficulty.PEACEFUL);
	}
	
	public void onDisable() {
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.kickPlayer("Runde beendet");
		}
	}
	
	
	
	
	public void register() {
		Bukkit.getPluginCommand("troll").setExecutor(new COMMAND_troll());
		Bukkit.getPluginCommand("nick").setExecutor(new COMMAND_nick());
		Bukkit.getPluginCommand("kffaadmin").setExecutor(new COMMAND_kffaadmin());
		Bukkit.getPluginCommand("stats").setExecutor(new COMMAND_stats());
		Bukkit.getPluginCommand("savekit").setExecutor(new COMMAND_savekit());
		
		
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Troll(), this);
		pm.registerEvents(new WorldListener(), this);
		pm.registerEvents(new JoinListener(), this);
		pm.registerEvents(new QuitListener(), this);
		pm.registerEvents(new DeathListener(), this);
		pm.registerEvents(new Kits(), this);
	}
}
