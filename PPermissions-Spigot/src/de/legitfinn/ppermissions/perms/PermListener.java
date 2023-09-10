package de.legitfinn.ppermissions.perms;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import de.legitfinn.ppermissions.main.main;


public class PermListener implements Listener {
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		main.updatePerms(p);
	}
	
	

}
