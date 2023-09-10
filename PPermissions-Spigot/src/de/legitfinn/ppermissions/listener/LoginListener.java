package de.legitfinn.ppermissions.listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import de.legitfinn.ppermissions.PlayerData.PlayerData;
import de.legitfinn.ppermissions.methoden.ScoreboardSystem;
import de.legitfinn.ppermissions.mysql.SQL;

public class LoginListener implements Listener {
	
	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if(!SQL.userExists(e.getPlayer().getUniqueId())) {
			System.out.println("Creating User... {UUID: " + e.getPlayer().getUniqueId() + "}");
			SQL.mysql.update("INSERT INTO Users (UUID, GROUPS) VALUES ('" + e.getPlayer().getUniqueId() + "','{name=" + ChatListener.getLowestGroup() + "}')");
		}
		if(!PlayerData.isListedUUID(e.getPlayer().getUniqueId())) {
			PlayerData.updateEntry(e.getPlayer().getUniqueId(), e.getPlayer().getName());
		}
		try{
			for(String all : SQL.getGroupListOfPlayer(e.getPlayer().getUniqueId())) {
				Date d = null;
				String exp = SQL.getGroupInformation(all, "expire");
				if(exp != null) {
					d = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(exp);
					if(d.before(new Date())) {
						SQL.removeFromGroup(e.getPlayer().getUniqueId(), SQL.getGroupInformation(all, "name"));
					}
				}

			}
		} catch(ParseException ex) {}
		logGroupsOfPlayer(e.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent e) {
		if(Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(e.getPlayer().getName()) == null) {
			ScoreboardSystem.addToTeam(e.getPlayer());
		} else {
			Bukkit.getScoreboardManager().getMainScoreboard().getEntryTeam(e.getPlayer().getName()).removeEntry(e.getPlayer().getName());
			ScoreboardSystem.addToTeam(e.getPlayer());
		}
	}
	
	public static void logGroupsOfPlayer(Player p) {
		String msg = "Groups: ";
		for(String all : SQL.getGroupListOfPlayer(p.getUniqueId())) {
			msg += SQL.getGroupColor(SQL.getGroupInformation(all, "name")) + SQL.getGroupInformation(all, "name") + "§7, ";
		}
		msg = msg.substring(0, msg.length() - 2);
		Bukkit.getConsoleSender().sendMessage(msg);
	}

}
