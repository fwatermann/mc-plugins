package de.legitfinn.bedwars.methoden;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Main.Types;
import de.NightShadwo.SignAPI.Main.Types.InformationTyps;
import de.legitfinn.bedwars.listener.SpecialItems;
import de.legitfinn.bedwars.listener.WorldListener;
import de.legitfinn.bedwars.main.BedWars;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.sql.StatisticSQL;
import de.legitfinn.ppermissions.main.PPermissions;
import de.legitfinn.replaysystem.main.ReplayAPI;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class GameStart {

	public static void start() {
		DataManager.state = GameState.Ingame;
		PPermissions.disableTablist();
		PPermissions.disableChat();
		SpecialItems.startArmorStands();
		
		ScoreboardSystem.createLobbyScoreboard(true);
		for(Player all : Bukkit.getServer().getOnlinePlayers()) {
			if(!isInTeam(all)) {
				inRandomTeam(all);
			}
		}
		Bukkit.getServer().createWorld(new WorldCreator("Map"));
		for(String all : DataManager.team_players.keySet()) {
			Location spawn = MapData.getSpawn(all);
			for(Player p : DataManager.team_players.get(all)) {
				p.teleport(spawn);
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				p.setAllowFlight(false);
				p.setFlying(false);
				p.setGameMode(GameMode.SURVIVAL);
				StatisticSQL.addInt("BEDWARS", "GAMES", 1, p.getUniqueId());
				p.setFallDistance(0.0F);
				p.setFireTicks(0);
				p.resetMaxHealth();
				p.setHealth(20.0);
				p.setFoodLevel(20);
			}
			DataManager.team_bed.put(all, true);
			DataManager.team_bedloc.put(all, MapData.getBed(all));
		}
		for(Entity all : Bukkit.getWorld("Map").getEntities()) {
			if(all.getType().equals(EntityType.DROPPED_ITEM)) {
				all.remove();
			} else if(all.getType().equals(EntityType.VILLAGER)) {
				Villager v = (Villager)all;
				NBTTagCompound com = new NBTTagCompound();
				((CraftEntity)v).getHandle().e(com);
				com.setByte("NoAI", (byte)1);
				com.setByte("Silent", (byte)1);
				((CraftEntity)v).getHandle().f(com);
				Location loc = v.getLocation().getBlock().getLocation().clone();
				loc.add(0.5, 0, 0.5);
				Location base = MapData.getSpawn(getNearstTeam(loc));
				Vector vec = new Vector(base.getX() - loc.getX(), 0, base.getZ() - loc.getZ());
				v.getLocation().setDirection(vec);
				v.teleport(loc);
			}
		}
		WorldListener.inView.clear();
		DataManager.teams = MapData.getTeamCount();
		DataManager.maxTeamsize = MapData.getPPTeam();
		DataManager.map = MapData.getMapName();
		SpawnerManager.loadSpawners();
		SpawnerManager.startSpawners();
		ScoreboardSystem.createIngameScoreboard();
		ScoreboardSystem.startEndcountdown();
		Bukkit.getServer().getWorld("Map").setGameRuleValue("doDaylightCycle", "false");
		Bukkit.getServer().getWorld("Map").setTime(6000);
		SignAPI.sendChange(InformationTyps.STATUS, Types.Status.INGAME.toString(), "own");
		if(!ReplayAPI.getInstance().isRecording()) {
			ReplayAPI.getInstance().startRecording(Bukkit.getWorld("Map"), ReplayAPI.getInstance().getReplayId());
		}
		Bukkit.getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
			
			@Override
			public void run() {
				if(GameManager.isEnd()) {
					GameManager.endGame(true);
				}
			}
		}, 5 * 20);
	}
	
	public static void inRandomTeam(Player p) {
		String team = getLowestTeam();
		for(String all : DataManager.team_players.keySet()) {
			DataManager.team_players.get(all).remove(p);
		}
		DataManager.team_players.get(team).add(p);
		Team last = Bukkit.getServer().getScoreboardManager().getMainScoreboard().getEntryTeam(p.getName());
		if(last != null) {
			last.removeEntry(p.getName());
		}
		Bukkit.getServer().getScoreboardManager().getMainScoreboard().getTeam(team).addEntry(p.getName());
	}
	
	public static String getLowestTeam() {
		int min = 10000;
		String team = "none";
		ArrayList<String> lowTeams = new ArrayList<String>();
		for(String all : DataManager.team_players.keySet()) {
			if(DataManager.team_players.get(all).size() < min) {
				min = DataManager.team_players.get(all).size();
				team = all;
				lowTeams.clear();
				lowTeams.add(all);
			} else if(DataManager.team_players.get(all).size() == min){
				lowTeams.add(all);
			}
		}
		team = lowTeams.get(new Random().nextInt(lowTeams.size()));	
		for(String all : lowTeams) {
			System.out.println(all);
		}
		System.out.println("Team: " + team);
		return team;
	}
	
	
	public static boolean isInTeam(Player p) {
		for(String all : DataManager.team_players.keySet()) {
			if(DataManager.team_players.get(all).contains(p)) {
				return true;
			}
		}
		return false;
	}
	
	public static String getNearstTeam(Location loc) {
		double dis = 1000000.0D;
		String team = "";
		for(String all : DataManager.team_players.keySet()) {
			if(loc.distance(MapData.getSpawn(all)) < dis) {
				dis = loc.distance(MapData.getSpawn(all));
				team = all;
			}
		}
		return team;
	}
	
	
}
