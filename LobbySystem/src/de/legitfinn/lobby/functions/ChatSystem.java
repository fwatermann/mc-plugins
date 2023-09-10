package de.legitfinn.lobby.functions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import de.legitfinn.lobby.main.main;
import de.legitfinn.ppermissions.main.PPermissions;
import de.legitfinn.ppermissions.mysql.SQL;

@SuppressWarnings("deprecation")
public class ChatSystem implements Listener {
	
	@EventHandler
	public void onChat(PlayerChatEvent e) {
		e.setCancelled(true);
		if(!main.isSilentLobby) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(main.chat.contains(all)) {
					all.sendMessage(SQL.getGroupPrefix(PPermissions.getShowGroup(e.getPlayer())) + e.getPlayer().getName() + " §8» §7" + e.getMessage());
				}
			}
		} else {
			e.getPlayer().sendMessage("§4§lSilentlobby §7» §cIn der Silentlobby ist der Chat deaktiviert.");
		}
	}
}
