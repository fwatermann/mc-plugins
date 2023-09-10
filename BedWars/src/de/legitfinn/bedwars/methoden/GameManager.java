package de.legitfinn.bedwars.methoden;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Main.Types;
import de.NightShadwo.SignAPI.Main.Types.InformationTyps;
import de.legitfinn.NickApi.main.NickModul;
import de.legitfinn.bedwars.listener.WorldListener;
import de.legitfinn.bedwars.main.BedWars;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.sql.StatisticSQL;
import de.legitfinn.ppermissions.main.PPermissions;
import de.legitfinn.replaysystem.main.ReplayAPI;

public class GameManager {

	
	public static boolean isEnd() {
		int teams = TeamManager.getLivingTeams();
		if(teams <= 1) {
			return true;
		}
		return false;
	}
	
	public static String getWinner() {
		for(String all : DataManager.team_players.keySet()) {
			if(DataManager.team_players.get(all).size() > 0) {
				return all;
			}
		}
		return null;
	}
	
	public static void endGame(boolean isTie) {
		DataManager.state = GameState.Shutdown;
		String winner = getWinner();
		Location lobby = ConfigManager.getLobbySpawn();
		ReplayAPI.getInstance().stopRecording();
		Top10.update();
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.teleport(lobby);
			for(Player p : Bukkit.getOnlinePlayers()) {
				all.showPlayer(p);
			}
			all.getInventory().clear();
			all.getInventory().setArmorContents(null);
			for(PotionEffect pe : all.getActivePotionEffects()) {
				all.removePotionEffect(pe.getType());
			}
			all.resetMaxHealth();
			all.setHealth(20.0);
			all.setFoodLevel(20);
			all.setFallDistance(0.0F);
		}
		for(Player all : WorldListener.inView.keySet()) {
			all.setGameMode(GameMode.SURVIVAL);
			all.setAllowFlight(false);
			all.setFlying(false);
		}

		if(!isTie) {
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + TeamManager.getTeamColor(winner) + "⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix);
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix);
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + TeamManager.getTeamColor(winner) + winner + "§3 hat die Runde gewonnen!");
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix);
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix);
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + TeamManager.getTeamColor(winner) + "⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
			for(Player all : DataManager.team_players.get(winner)) {
				StatisticSQL.addInt("BEDWARS", "WINS", 1, all.getUniqueId());
			}
		} else {
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "§3⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix);
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix);
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "Es hat kein Team gewonnen,");
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "da die Zeit abgelaufen ist.");
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix);
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix);
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "§3⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
		}
		Bukkit.getScheduler().cancelTask(ScoreboardSystem.countId);
		ScoreboardSystem.createLobbyScoreboard(false);
		PPermissions.enableChat();
		EndCountdown.start();
		Bukkit.getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
			
			@Override
			public void run() {
				for(Player all : Bukkit.getOnlinePlayers()) {
					if(NickModul.isNicked(all.getUniqueId())) {
						NickModul.unNickPlayer(all, true);
					}
				}
			}
		}, 10);
		Bukkit.getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
			@Override
			public void run() {
				PPermissions.enableTablist();
			}
		}, 20);
		SignAPI.sendChange(InformationTyps.STATUS, Types.Status.SHUTDOWN.toString(), "own");
	}
	
	
	
	
	
}
