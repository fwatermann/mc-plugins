package de.finn.CloudSystem.Signs.updateListeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.finn.CloudSystem.Signs.Signs.Server;
import de.finn.CloudSystem.Signs.main.DataManager;
import de.finn.CloudSystem.Spigot.event.CloudSignUpdateMapEvent;
import de.finn.CloudSystem.Spigot.event.CloudSignUpdateMaxPlayerEvent;
import de.finn.CloudSystem.Spigot.event.CloudSignUpdateOnPlayerEvent;
import de.finn.CloudSystem.Spigot.event.CloudSignUpdateStatusEvent;

public class UpdateListener implements Listener {

	private HashMap<String, Server> servername_server = new HashMap<String, Server>();
	
	@EventHandler
	public void onUpdateStatus(CloudSignUpdateStatusEvent e) {
		
		String servername = e.getServername();
		String gamemode = getGamemode(servername);
		String status = e.getStatus();
		
		if(status.equalsIgnoreCase("waiting")) {
			if(!servername_server.containsKey(servername)) {
				Server server = new Server(servername, "", 0, 0, status);
				servername_server.put(servername, server);
				System.out.println(gamemode.toUpperCase());
				DataManager.gamemode_signGroup.get(gamemode.toUpperCase()).addServer(server);
			} else {
				servername_server.get(servername).setStatus(status);
				DataManager.gamemode_signGroup.get(gamemode.toUpperCase()).updateSign(servername_server.get(servername));
			}
		} else {
			if(servername_server.containsKey(servername)) {
				servername_server.get(servername).setStatus(status);
				DataManager.gamemode_signGroup.get(gamemode.toUpperCase()).updateSign(servername_server.get(servername));
				servername_server.remove(servername);
			}
		}
		if(DataManager.debug) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(all.hasPermission("network.debug")) {
					all.sendMessage("§7[DEBUG]: §eUpdateStatus <" + servername + "> <" + gamemode.toUpperCase() + "> <" + status + ">" );
				}
			}
			System.out.println("[DEBUG]: UpdateStatus <" + servername + "> <" + gamemode.toUpperCase() + "> <" + status + ">" );
		}
	}
	
	@EventHandler
	public void onUpdateOnPlayer(CloudSignUpdateOnPlayerEvent e) {
		String servername = e.getServername();
		String gamemode = getGamemode(servername);
		int on = e.getOnPlayers();
		
		if(servername_server.containsKey(servername)) {
			Server server = servername_server.get(servername);
			server.setOnPlayer(on);
			DataManager.gamemode_signGroup.get(gamemode.toUpperCase()).updateSign(server);
		}
		if(DataManager.debug) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(all.hasPermission("network.debug")) {
					all.sendMessage("§7[DEBUG]: §eUpdateOnPlayer <" + servername + "> <" + gamemode.toUpperCase() + "> <" + on + ">" );
				}
			}
			System.out.println("[DEBUG]: UpdateOnPlayers <" + servername + "> <" + gamemode.toUpperCase() + "> <" + on + ">" );
		}
	}
	
	@EventHandler
	public void onUpdateMaxPlayer(CloudSignUpdateMaxPlayerEvent e) {
		String servername = e.getServername();
		String gamemode = getGamemode(servername);
		int max = e.getMaxPlayers();
		
		if(servername_server.containsKey(servername)) {
			Server server = servername_server.get(servername);
			server.setMaxPlayer(max);			
			DataManager.gamemode_signGroup.get(gamemode.toUpperCase()).updateSign(server);
		}
		if(DataManager.debug) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(all.hasPermission("network.debug")) {
					all.sendMessage("§7[DEBUG]: §eUpdateMaxPlayer <" + servername + "> <" + gamemode.toUpperCase() + "> <" + max + ">" );
				}
			}
			System.out.println("[DEBUG]: UpdateMaxPlayer <" + servername + "> <" + gamemode.toUpperCase() + "> <" + max + ">" );
		}
	}
	
	@EventHandler
	public void onUpdateMap(CloudSignUpdateMapEvent e) {
		String servername = e.getServername();
		String gamemode = getGamemode(servername);
		String map = e.getMap();
		
		if(servername_server.containsKey(servername)) {
			Server server = servername_server.get(servername);
			server.setMap(map);			
			DataManager.gamemode_signGroup.get(gamemode.toUpperCase()).updateSign(server);
		}
		if(DataManager.debug) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(all.hasPermission("network.debug")) {
					all.sendMessage("§7[DEBUG]: §eUpdateMap <" + servername + "> <" + gamemode.toUpperCase() + "> <" + map + ">" );
				}
			}
			System.out.println("[DEBUG]: UpdateMap <" + servername + "> <" + gamemode.toUpperCase() + "> <" + map + ">" );
		}
	}
	
	
	private String getGamemode(String servername) {
		return servername.split("#")[0];
	}
	
}
