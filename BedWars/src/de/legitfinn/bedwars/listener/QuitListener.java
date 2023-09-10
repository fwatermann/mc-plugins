package de.legitfinn.bedwars.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Main.Types.InformationTyps;
import de.legitfinn.NickApi.main.NickModul;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.GameManager;
import de.legitfinn.bedwars.methoden.GameState;
import de.legitfinn.bedwars.methoden.ScoreboardSystem;
import de.legitfinn.bedwars.methoden.TeamManager;
import de.legitfinn.ppermissions.main.PPermissions;
import de.legitfinn.replaysystem.main.ReplayAPI;

public class QuitListener implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		if(!DataManager.state.equals(GameState.Ingame)) {
			if(!NickModul.isNicked(e.getPlayer().getUniqueId())) {
				ReplayAPI.getInstance().broadCastMessage("§7« " + PPermissions.getChatColor(e.getPlayer()) + e.getPlayer().getName() + " §3hat die Runde §c§overlassen§3.");
			} else {
				ReplayAPI.getInstance().broadCastMessage("§7« " + PPermissions.getLowestChatColor() + e.getPlayer().getName() + " §3hat die Runde §c§overlassen§3.");
			}
			for(String all : DataManager.team_players.keySet()) {
				DataManager.team_players.get(all).remove(e.getPlayer());
			}
			SignAPI.sendChange(InformationTyps.PLAYERS, (Bukkit.getServer().getOnlinePlayers().size() - 1) + "", "own");
		} else if(DataManager.state.equals(GameState.Ingame)) {
			String team = TeamManager.getTeamOfPlayer(e.getPlayer());
			if(team != null) {
				DataManager.team_players.get(team).remove(e.getPlayer());
				if(DataManager.team_players.get(team).size() >= 1) {
					ReplayAPI.getInstance().broadCastMessage("§7« " + TeamManager.getTeamColor(TeamManager.getTeamOfPlayer(e.getPlayer())) + e.getPlayer().getName() + " §3hat Team " + TeamManager.getTeamColor(team) + team 
							+ " §3im Stich gelassen! §7[§e" + (DataManager.team_players.get(team).size() - 1) + "§f/§e" + DataManager.maxTeamsize + "§7]");
				} else {
					ReplayAPI.getInstance().broadCastMessage("§7« " + TeamManager.getTeamColor(team) + e.getPlayer().getName() + " §3hat die Runde §4§overlassen");
					ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + TeamManager.getTeamColor(team) + team + " §3ist ausgeschieden!");
					if(TeamManager.getLivingTeams() > 1) {
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "Es sind noch §e" + TeamManager.getLivingTeams() + " Teams §3am Leben.");
					} else {
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "Es sind noch §e" + TeamManager.getLivingTeams() + " Team §3am Leben.");
					}
					if(GameManager.isEnd()) {
						GameManager.endGame(false);
					}
				}
				ScoreboardSystem.createIngameScoreboard();
			}
			SignAPI.sendChange(InformationTyps.PLAYERS, getIngamePlayers() + "", "own");
		}
		if(Bukkit.getServer().getOnlinePlayers().size() - 1 <= 0) {
			SignAPI.sendChange(InformationTyps.MAX_PLAYERS, "-1", "own");
		}
	}
	
	public static int getIngamePlayers() {
		int i = 0;
		for(String team : DataManager.team_players.keySet()) {
			i += DataManager.team_players.get(team).size();
		}	
		return i;
	}
	
}
