package de.legitfinn.bedwars.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Main.Types;
import de.NightShadwo.SignAPI.Main.Types.Gamemode;
import de.NightShadwo.SignAPI.Main.Types.InformationTyps;
import de.legitfinn.bedwars.cloud.ServerListener;
import de.legitfinn.bedwars.commands.COMMAND_bedwars;
import de.legitfinn.bedwars.commands.COMMAND_forcemap;
import de.legitfinn.bedwars.commands.COMMAND_nick;
import de.legitfinn.bedwars.commands.COMMAND_start;
import de.legitfinn.bedwars.commands.COMMAND_stats;
import de.legitfinn.bedwars.commands.COMMAND_troll;
import de.legitfinn.bedwars.listener.BedListener;
import de.legitfinn.bedwars.listener.ChatListener;
import de.legitfinn.bedwars.listener.CompassListener;
import de.legitfinn.bedwars.listener.JoinListener;
import de.legitfinn.bedwars.listener.LobbyFunctions;
import de.legitfinn.bedwars.listener.QuitListener;
import de.legitfinn.bedwars.listener.RespawnListener;
import de.legitfinn.bedwars.listener.SpecialItems;
import de.legitfinn.bedwars.listener.WorldListener;
import de.legitfinn.bedwars.methoden.GameState;
import de.legitfinn.bedwars.methoden.Top10;
import de.legitfinn.bedwars.shop.Shop;
import de.legitfinn.bedwars.shop.ShopBuy;
import de.legitfinn.bedwars.sql.LobbySettingsSQL;
import de.legitfinn.bedwars.sql.StatisticSQL;
import de.legitfinn.ppermissions.main.PPermissions;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class BedWars extends JavaPlugin {

	public void onEnable() {
		PPermissions.disableTablist();
		SignAPI.registerEvent(new ServerListener());
		SignAPI.sendChange(InformationTyps.GAMEMODE, Gamemode.BEDWARS.toString(), "own");
		SignAPI.sendChange(InformationTyps.STATUS, Types.Status.WAITING.toString(), "own");
		SignAPI.sendChange(InformationTyps.MAX_PLAYERS, "-1", "own");
		SignAPI.sendChange(InformationTyps.PLAYERS, "0", "own");
		DataManager.state = GameState.Waiting;
		register();
		LobbySettingsSQL.connect();
		StatisticSQL.connect();
		Top10.update();
	}
	
	public void onDisable() {
		Bukkit.getServer().unloadWorld("Map", false);
		for(Player all : Bukkit.getServer().getOnlinePlayers()) {
			all.kickPlayer(DataManager.prefix + "§9Runde beendet.");
		}
	}
	
	public void register() {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new JoinListener(), this);
		pm.registerEvents(new QuitListener(), this);
		pm.registerEvents(new WorldListener(), this);
		pm.registerEvents(new LobbyFunctions(), this);
		pm.registerEvents(new RespawnListener(), this);
		pm.registerEvents(new BedListener(), this);
		pm.registerEvents(new Shop(), this);
		pm.registerEvents(new ShopBuy(), this);
		pm.registerEvents(new SpecialItems(), this);
		pm.registerEvents(new COMMAND_troll(), this);
		pm.registerEvents(new ChatListener(), this);
		pm.registerEvents(new CompassListener(), this);
		
		
		Bukkit.getServer().getPluginCommand("bedwars").setExecutor(new COMMAND_bedwars());
		Bukkit.getServer().getPluginCommand("start").setExecutor(new COMMAND_start());
		Bukkit.getServer().getPluginCommand("troll").setExecutor(new COMMAND_troll());
		Bukkit.getServer().getPluginCommand("nick").setExecutor(new COMMAND_nick());
		Bukkit.getServer().getPluginCommand("stats").setExecutor(new COMMAND_stats());
		Bukkit.getServer().getPluginCommand("forcemap").setExecutor(new COMMAND_forcemap());
	}
	
	public static void playParicle(ArrayList<Player> to, EnumParticle particle, Location loc,float xoffset, float yoffset, float zoffset, float speed, int amount) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), xoffset, yoffset, zoffset, speed, amount, null);
		for(Player all : to) {
			((CraftPlayer)all).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void playParicle(List<Player> to, EnumParticle particle, Location loc, float xoffset, float yoffset, float zoffset, float speed, int amount) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), xoffset, yoffset, zoffset, speed, amount, null);
		for(Player all : to) {
			((CraftPlayer)all).getHandle().playerConnection.sendPacket(packet);		
		}
	}
	
	public static void playReddustParticle(List<Player> to, Location loc, float red, float green, float blue, int amount) {
		for(int i = 0; i < amount; i ++) {
			playParicle(to, EnumParticle.REDSTONE, loc, red, green, blue, 1.0F, 0);
		}
	}
	
	
}
