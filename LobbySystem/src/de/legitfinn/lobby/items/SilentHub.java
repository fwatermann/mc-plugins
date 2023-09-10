package de.legitfinn.lobby.items;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.legitfinn.lobby.SQL.LobbyServerSQL;
import de.legitfinn.lobby.SQL.StatisticSQL;
import de.legitfinn.lobby.main.main;
import de.legitfinn.ppermissions.main.PPermissions;

public class SilentHub implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getPlayer().getItemInHand().getType().equals(Material.TNT)) {
				if(!main.build.contains(e.getPlayer())) {
					e.setCancelled(true);
					if(PPermissions.isInTeamGroup(e.getPlayer()) || PPermissions.isInGroup(e.getPlayer(), "YouTuber")) {
						changeSilentHub(e.getPlayer());
					} else {
						e.getPlayer().setItemInHand(null);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(!LobbyServerSQL.playerListed(e.getPlayer().getUniqueId())) {
			LobbyServerSQL.listPlayer(e.getPlayer().getUniqueId(), e.getPlayer().getLocation(), main.isSilentLobby);
		}
		LobbyServerSQL.setLocation(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
		
		HashMap<String, int[]> copy = new HashMap<String, int[]>();
		copy.putAll(StatisticSQL.cache);
		for(String s : copy.keySet()){
			if(s.contains(e.getPlayer().getUniqueId().toString())){
				StatisticSQL.cache.remove(s);
			}
		}
		copy.clear();
	}
	
	public static void connectToSilentLobby(Player p) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("CloudToSilentLobby");
			out.writeUTF("2288-sfunnet");
			out.writeUTF(p.getUniqueId().toString());
			p.sendPluginMessage(main.getPlugin(main.class), "BungeeCord", b.toByteArray());
		} catch(IOException ex){}
	}
	
	public static void connectToPublicLobby(Player p) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("CloudToPublicLobby");
			out.writeUTF("2288-sfunnet");
			out.writeUTF(p.getUniqueId().toString());
			p.sendPluginMessage(main.getPlugin(main.class), "BungeeCord", b.toByteArray());
		} catch(IOException ex){}
	}
	
	public void changeSilentHub(Player p) {
		if(!main.isSilentLobby) {
			if(!LobbyServerSQL.playerListed(p.getUniqueId())) {
				LobbyServerSQL.listPlayer(p.getUniqueId(), p.getLocation(), main.isSilentLobby);
			}
			LobbyServerSQL.setInSilent(p.getUniqueId(), true);
			LobbyServerSQL.setLocation(p.getUniqueId(), p.getLocation());
			p.sendMessage("§4§lSilent §7» §5Du bist nun in der §a§oSilentlobby.");
			connectToSilentLobby(p);
		} else {
			if(!LobbyServerSQL.playerListed(p.getUniqueId())) {
				LobbyServerSQL.listPlayer(p.getUniqueId(), p.getLocation(), main.isSilentLobby);
			}
			LobbyServerSQL.setInSilent(p.getUniqueId(), false);
			LobbyServerSQL.setLocation(p.getUniqueId(), p.getLocation());
			p.sendMessage("§4§lSilent §7» §5Du bist nun auf einer §a§oPubliclobby.");
			connectToPublicLobby(p);
		}
	}

}
