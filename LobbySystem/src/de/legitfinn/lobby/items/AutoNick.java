package de.legitfinn.lobby.items;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

import de.legitfinn.lobby.SQL.LobbySettingsSQL;
import de.legitfinn.ppermissions.main.PPermissions;

public class AutoNick implements Listener {
	
	public static HashMap<Player, Long> last = new HashMap<Player, Long>();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getPlayer().getItemInHand().getType().equals(Material.NAME_TAG)) {
				if(PPermissions.isInGroup(e.getPlayer(), "Premium+") || PPermissions.isInGroup(e.getPlayer(), "YouTuber") || PPermissions.isInTeamGroup(e.getPlayer())) {
					if(e.getPlayer().getItemInHand().hasItemMeta()) {
						if(last.containsKey(e.getPlayer())) {
							if(System.currentTimeMillis() - last.get(e.getPlayer()) < 1000) {
								e.getPlayer().sendMessage("§4§lAutoNick §7» §cDu kannst deinen §5Automatischen Nicknamen §cnur jede Sekunde de-/aktivieren.");
								return;
							}
						}
						last.remove(e.getPlayer());
						last.put(e.getPlayer(), System.currentTimeMillis());
						ItemMeta meta = e.getPlayer().getItemInHand().getItemMeta();
	 					if(LobbySettingsSQL.getSettingsBoolean(e.getPlayer().getUniqueId(), "NICK")) {
							meta.setDisplayName("§c⬛⬛⬛ §5§lAutomatischer Nickname §c⬛⬛⬛");
							LobbySettingsSQL.setSettings(e.getPlayer().getUniqueId(), "NICK", 0);
							e.getPlayer().sendMessage("§4§lAutoNick §7» §5Automatischer Nickname §cdeaktiviert.");
							e.getPlayer().getItemInHand().setItemMeta(meta);
						} else {
							meta.setDisplayName("§a⬛⬛⬛ §5§lAutomatischer Nickname §a⬛⬛⬛");
							LobbySettingsSQL.setSettings(e.getPlayer().getUniqueId(), "NICK", 1);
							e.getPlayer().sendMessage("§4§lAutoNick §7» §5Automatischer Nickname §aaktiviert.");
							e.getPlayer().getItemInHand().setItemMeta(meta);
						}
					}				
				} else {
					e.getPlayer().setItemInHand(null);
					Hotbar.createHotbar(e.getPlayer());
				}
			}
		}
	}
	
	

}
