package de.legitfinn.lobby.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Main.Types.InformationTyps;
import de.legitfinn.friend.FriendSQL;
import de.legitfinn.friend.FriendsMenu;
import de.legitfinn.lobby.PlayerData.PlayerData;
import de.legitfinn.lobby.SQL.CoinSQL;
import de.legitfinn.lobby.SQL.LobbyPetSQL;
import de.legitfinn.lobby.SQL.LobbyServerSQL;
import de.legitfinn.lobby.SQL.LobbySettingsSQL;
import de.legitfinn.lobby.SQL.LobbyShopSQL;
import de.legitfinn.lobby.SQL.ShopSQL;
import de.legitfinn.lobby.SQL.StatisticSQL;
import de.legitfinn.lobby.commands.COMMAND_bauen;
import de.legitfinn.lobby.commands.COMMAND_dailyreward;
import de.legitfinn.lobby.commands.COMMAND_flight;
import de.legitfinn.lobby.commands.COMMAND_payments;
import de.legitfinn.lobby.commands.COMMAND_setwarp;
import de.legitfinn.lobby.functions.ChatSystem;
import de.legitfinn.lobby.functions.DailyReward;
import de.legitfinn.lobby.functions.DoubleJump;
import de.legitfinn.lobby.functions.ParticleAnimations;
import de.legitfinn.lobby.functions.PetListener;
import de.legitfinn.lobby.items.AutoNick;
import de.legitfinn.lobby.items.Einstellungen;
import de.legitfinn.lobby.items.Gadgets;
import de.legitfinn.lobby.items.Hotbar;
import de.legitfinn.lobby.items.Navigator;
import de.legitfinn.lobby.items.ShopAdmin;
import de.legitfinn.lobby.items.SilentHub;
import de.legitfinn.lobby.items.Statistics;
import de.legitfinn.lobby.scoreboard.ScoreboardSystem;
import de.legitfinn.ppermissions.main.PPermissions;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class main extends JavaPlugin {

	public static ArrayList<Player> build = new ArrayList<Player>();
	public static boolean isSilentLobby = false;
	public static String LobbyPrefix = "§4§lLobby §7» §a";
	public static ArrayList<Player> particles = new ArrayList<Player>();
	public static ArrayList<Player> chat = new ArrayList<Player>();
	public static ArrayList<Player> pets = new ArrayList<Player>();
	public static ArrayList<Player> doubleJump = new ArrayList<Player>();
	public static HashMap<Player, Integer> visible = new HashMap<Player, Integer>();
	public static Inventory compass = null;
	
	public void onEnable() {
		ConfigManager.createDefault();
		registerListener();
		registerCommands();
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		FriendSQL.connect();
		PlayerData.connect();
		CoinSQL.connect();
		LobbyServerSQL.connect();
		LobbyShopSQL.connect();
		LobbyPetSQL.connect();
		ShopSQL.connect();
		StatisticSQL.connect();
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			ScoreboardSystem.createScoreboard(all);
			ScoreboardSystem.updateScoreboard(all, true, true);
		}
		isSilentLobby = ConfigManager.getBoolean("IsSilentLobby");;
		Gadgets.Animations();
		Gadgets.PetFollow();
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(LobbyPetSQL.isListed(all.getUniqueId())) {
				String name = LobbyPetSQL.getPetName(all.getUniqueId());
				EntityType et = LobbyPetSQL.getPetType(all.getUniqueId());
				int size = LobbyPetSQL.getSize(all.getUniqueId());
				Gadgets.spawnPet(all, et, size, name);
			}
			if(LobbySettingsSQL.getSettingsBoolean(all.getUniqueId(), "CHAT")) {
				chat.add(all);
			}
			if(LobbySettingsSQL.getSettingsBoolean(all.getUniqueId(), "PARTICLES")) {
				particles.add(all);
			}
			Hotbar.createHotbar(all);
		}
		if(!isSilentLobby) {
//			DailyReward.animation();
//			DailyReward.randomLocation();
		}
		
		Navigator.setupNavigator();
		
		if(isSilentLobby) {
			SignAPI.sendChange(InformationTyps.GAMEMODE, "SILENTLOBBY", "own");
		} else {
			SignAPI.sendChange(InformationTyps.GAMEMODE, "LOBBY", "own");
		}
		SignAPI.sendChange(InformationTyps.STATUS, "WAITING", "own");
		SignAPI.sendChange(InformationTyps.MAX_PLAYERS, Bukkit.getServer().getMaxPlayers() + "", "own");
		SignAPI.sendChange(InformationTyps.PLAYERS, "" + Bukkit.getOnlinePlayers().size(), "own");
	}
	
	@SuppressWarnings("deprecation")
	public void onDisable() {
		FriendSQL.mysql.close();
		PlayerData.mysql.close();
		LobbyServerSQL.mysql.close();
		StatisticSQL.mysql.close();
		if(Gadgets.flame != null) {
			Gadgets.flame.interrupt();
			Gadgets.flame.stop();
		}
		if(Gadgets.magic != null) {
			Gadgets.magic.interrupt();
			Gadgets.magic.stop();
		}
		if(Gadgets.emerald != null) {
			Gadgets.emerald.interrupt();
			Gadgets.emerald.stop();
		}
		if(Gadgets.rain != null) {
			Gadgets.rain.interrupt();
			Gadgets.rain.stop();
		}
		if(Gadgets.redstone != null) {
			Gadgets.redstone.interrupt();
			Gadgets.redstone.stop();
		}
		if(DailyReward.dthread != null) {
			DailyReward.dthread.interrupt();
			DailyReward.dthread.stop();
		}
		for(Player all : PetListener.player_pet.keySet()) {
			PetListener.player_pet.get(all).ent.remove();
			if(all.getPassenger() != null) {
				all.getPassenger().remove();
			}
		}
		for(Player all : Bukkit.getOnlinePlayers()) {
			for(Entity ent : all.getLocation().getWorld().getEntities()) {
				if(!ent.getType().equals(EntityType.ITEM_FRAME) && !ent.getType().equals(EntityType.PLAYER) && !ent.getType().equals(EntityType.ARMOR_STAND)) {
					ent.remove();
				}
			}
		}
//		if(DailyReward.dailyReward != null) {
//			DailyReward.dailyReward.getBlock().setType(DailyReward.oldDownMaterial);
//			DailyReward.dailyReward.getBlock().setData(DailyReward.oldDownData);
//			DailyReward.dailyReward.clone().add(0, 1, 0).getBlock().setType(DailyReward.oldTopMaterial);
//			DailyReward.dailyReward.clone().add(0, 1, 0).getBlock().setData(DailyReward.oldTopData);			
//		}
	}
	
	public void registerListener() {
		Bukkit.getPluginManager().registerEvents(new Navigator(), this);
		Bukkit.getPluginManager().registerEvents(new FriendsMenu(), this);
		Bukkit.getPluginManager().registerEvents(new Hotbar(), this);
		Bukkit.getPluginManager().registerEvents(new WorldListener(), this);
		Bukkit.getPluginManager().registerEvents(new SilentHub(), this);
		Bukkit.getPluginManager().registerEvents(new Einstellungen(), this);
		Bukkit.getPluginManager().registerEvents(new Gadgets(), this);
		Bukkit.getPluginManager().registerEvents(new DoubleJump(), this);
		Bukkit.getPluginManager().registerEvents(new ParticleAnimations(), this);
		Bukkit.getPluginManager().registerEvents(new PetListener(), this);
		Bukkit.getPluginManager().registerEvents(new ShopAdmin(), this);
//		Bukkit.getPluginManager().registerEvents(new DailyReward(), this);
		Bukkit.getPluginManager().registerEvents(new ChatSystem(), this);
		Bukkit.getPluginManager().registerEvents(new ScoreboardSystem(), this);
		Bukkit.getPluginManager().registerEvents(new AutoNick(), this);
		Bukkit.getPluginManager().registerEvents(new Statistics(), this);
		PPermissions.disableChat();
	}
	
	public void registerCommands() {
		Bukkit.getPluginCommand("fly").setExecutor(new COMMAND_flight());
		Bukkit.getPluginCommand("build").setExecutor(new COMMAND_bauen());
		Bukkit.getPluginCommand("dailyrewardarea").setExecutor(new COMMAND_dailyreward());
		Bukkit.getPluginCommand("payments").setExecutor(new COMMAND_payments());
		Bukkit.getPluginCommand("setwarp").setExecutor(new COMMAND_setwarp());
	}

	public static void playParicle(ArrayList<Player> to, EnumParticle particle, Location loc,float xoffset, float yoffset, float zoffset, float speed, int amount) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), xoffset, yoffset, zoffset, speed, amount, null);
		for(Player all : to) {
			if(particles.contains(all)) {
				((CraftPlayer)all).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}

	public static void playParicle(List<Player> to, EnumParticle particle, Location loc, float xoffset, float yoffset, float zoffset, float speed, int amount) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), xoffset, yoffset, zoffset, speed, amount, null);
		for(Player all : to) {
			if(particles.contains(all)) {
				((CraftPlayer)all).getHandle().playerConnection.sendPacket(packet);
			}			
		}
	}
	
	public static void playReddustParticle(List<Player> to, Location loc, float red, float green, float blue, int amount) {
		for(int i = 0; i < amount; i ++) {
			playParicle(to, EnumParticle.REDSTONE, loc, red, green, blue, 1.0F, 0);
		}
	}
	
	
}
