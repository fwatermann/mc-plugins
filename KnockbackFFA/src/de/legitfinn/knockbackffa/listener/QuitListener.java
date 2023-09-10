package de.legitfinn.knockbackffa.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Main.Types.InformationTyps;
import de.legitfinn.NickApi.main.NickModul;
import de.legitfinn.knockbackffa.methoden.Kits;
import de.legitfinn.ppermissions.main.PPermissions;
import de.legitfinn.replaysystem.main.ReplayAPI;

public class QuitListener implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);
		if(NickModul.isNicked(e.getPlayer().getUniqueId())) {
			ReplayAPI.getInstance().broadCastMessage("§7« " + PPermissions.getLowestChatColor() + e.getPlayer().getName() + " §3hat das Spiel verlassen.");
		} else {
			ReplayAPI.getInstance().broadCastMessage("§7« " + PPermissions.getChatColor(e.getPlayer()) + e.getPlayer().getName() + " §3hat das Spiel verlassen.");
		}
		e.getPlayer().getInventory().clear();
		e.getPlayer().getInventory().setArmorContents(null);
		Kits.player_kills.remove(e.getPlayer());
		SignAPI.sendChange(InformationTyps.PLAYERS, (Bukkit.getServer().getOnlinePlayers().size() - 1) + "", "own");
	}
	
	
	
}
