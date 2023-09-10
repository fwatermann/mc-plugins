package de.legitfinn.bedwars.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.GameState;
import de.legitfinn.bedwars.methoden.TeamManager;
import de.legitfinn.replaysystem.main.ReplayAPI;

@SuppressWarnings("deprecation")
public class ChatListener implements Listener {

	@EventHandler
	public void onChat(PlayerChatEvent e) {
		if(DataManager.state.equals(GameState.Ingame)) {
			String team = TeamManager.getTeamOfPlayer(e.getPlayer());
			e.setCancelled(true);
			
			if(team != null) {
				if(e.getMessage().startsWith("@a") || e.getMessage().startsWith("@all") || e.getMessage().startsWith("@")) {
					String msg = e.getMessage().replaceFirst("@all", "").replaceFirst("@a", "").replaceFirst("@", "");
					ReplayAPI.getInstance().broadCastMessage("§8[§7ALL§8] " + TeamManager.getTeamColor(team) + e.getPlayer().getName() + "§7» §r" + msg);
					
				} else {
					for(Player all : DataManager.team_players.get(team)) {
						all.sendMessage(TeamManager.getTeamColor(team) + e.getPlayer().getName() + "§7» §r" + e.getMessage());
					}
					System.out.println("TEAMCHAT [" + team + "]: " + e.getPlayer() + ": " +  e.getMessage());
				}
			} else {
				for(Player all : Bukkit.getOnlinePlayers()) {
					if(isSpectator(all)) {
						all.sendMessage("§8[§c✖§8] §8" + e.getPlayer().getName() + "§7» §r" + e.getMessage());
					}
				}
				System.out.println("SPECTATOR: " + e.getPlayer() + ": " +  e.getMessage());
			}
		}
	}
	
	public static boolean isSpectator(Player p) {
		for(String all : DataManager.team_players.keySet()) {
			if(DataManager.team_players.get(all).contains(p)) {
				return false;
			}
		}	
		return true;
	}
	
	
	
	
}
