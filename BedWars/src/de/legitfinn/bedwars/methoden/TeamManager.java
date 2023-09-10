package de.legitfinn.bedwars.methoden;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import de.legitfinn.bedwars.main.DataManager;

public class TeamManager {

	public static String getTeamOfPlayer(Player p) {
		for(String all : DataManager.team_players.keySet()) {
			if(DataManager.team_players.get(all).contains(p)) {
				return all;
			}
		}
		return null;
	}
	
	public static ChatColor getTeamColor(String team) {
		if(DataManager.team_color.containsKey(team)) {
			return DataManager.team_color.get(team);
		}
		return ChatColor.WHITE;
	}
	
	public static int getLivingTeams() {
		int i = DataManager.teams;
		for(String all : DataManager.team_players.keySet()) {
			if(DataManager.team_players.get(all).size() <= 0) {
				i --;
			}
		}
		return i;
	}
	
	public static byte ChatColorToColorByte(ChatColor c) {
		if(c.equals(ChatColor.AQUA)) {
			return (byte)9;
		} else if(c.equals(ChatColor.WHITE)) {
			return (byte)0;
		} else if(c.equals(ChatColor.GOLD)) {
			return (byte)1;
		} else if(c.equals(ChatColor.LIGHT_PURPLE)) {
			return (byte)2;
		} else if(c.equals(ChatColor.BLUE)) {
			return (byte)3;
		} else if(c.equals(ChatColor.YELLOW)) {
			return (byte)4;
		} else if(c.equals(ChatColor.GREEN)) {
			return (byte)5;
		} else if(c.equals(ChatColor.LIGHT_PURPLE)) {
			return (byte)6;
		} else if(c.equals(ChatColor.DARK_GRAY)) {
			return (byte)7;
		} else if(c.equals(ChatColor.GRAY)) {
			return (byte)8;
		} else if(c.equals(ChatColor.DARK_AQUA)) {
			return (byte)9;
		} else if(c.equals(ChatColor.DARK_PURPLE)) {
			return (byte)10;
		} else if(c.equals(ChatColor.BLUE)) {
			return (byte)11;
		} else if(c.equals(ChatColor.DARK_GREEN)) {
			return (byte)13;
		} else if(c.equals(ChatColor.RED)) {
			return (byte)14;
		} else if(c.equals(ChatColor.BLACK)) {
			return (byte)15;
		} else {
			return (byte)0;
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
