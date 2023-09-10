package de.legitfinn.ppermissions.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import de.legitfinn.NickApi.main.NickModul;
import de.legitfinn.ppermissions.main.main;
import de.legitfinn.ppermissions.methoden.ScoreboardSystem;
import de.legitfinn.ppermissions.mysql.SQL;

@SuppressWarnings("deprecation")
public class ChatListener implements Listener {
	
	@EventHandler
	public void onChat(PlayerChatEvent e) {
		if(main.chat) {
			if(!NickModul.isNicked(e.getPlayer().getUniqueId())) {
				String group = ScoreboardSystem.getShowGroup(SQL.getGroupListOfPlayer(e.getPlayer().getUniqueId()));
				String prefix = SQL.getGroupPrefix(group);
				e.setCancelled(true);
				Bukkit.broadcastMessage(prefix + e.getPlayer().getName() + " §8» §7" + e.getMessage());
			} else {
				String group = getLowestGroup();
				String prefix = SQL.getGroupPrefix(group);
				e.setCancelled(true);
				Bukkit.broadcastMessage(prefix + e.getPlayer().getName() + " §8» §7" + e.getMessage());
			}
		}
	}
	
	public static String getLowestGroup() {
		int i = 0;
		String group = "";
		for(String all : SQL.getAllGroups()) {
			if(SQL.getSortIdOfGroup(all) > i) {
				group = all;
				i = SQL.getSortIdOfGroup(all);
			}
		}
		return group;
	}
	
	
	
}
