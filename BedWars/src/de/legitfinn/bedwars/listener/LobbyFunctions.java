package de.legitfinn.bedwars.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.Countdown;
import de.legitfinn.bedwars.methoden.GameState;
import de.legitfinn.bedwars.methoden.MapData;
import de.legitfinn.bedwars.methoden.TeamManager;

public class LobbyFunctions implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(DataManager.state.equals(GameState.Waiting)) {
			if(e.getPlayer().getItemInHand().getType().equals(Material.BANNER)) {
				if(e.getPlayer().getInventory().getHeldItemSlot() == 0) {
					openTeamwahl(e.getPlayer());
				}
			} else if(e.getPlayer().getItemInHand().getType().equals(Material.SKULL_ITEM)) {
				if(e.getPlayer().getInventory().getHeldItemSlot() == 8) {
					e.getPlayer().kickPlayer(DataManager.prefix + "Server verlassen.");
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(!DataManager.state.equals(GameState.Ingame)) {
			e.setCancelled(true);
			if(e.getClickedInventory() != null && e.getCurrentItem() != null) {
				if(e.getClickedInventory().getTitle().equalsIgnoreCase("§7» §cWähle dein Team") && DataManager.state.equals(GameState.Waiting)) {
					e.setCancelled(true);
					if(e.getCurrentItem().getType().equals(Material.WOOL)) {
						Player p = (Player)e.getWhoClicked();
						String team = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
						if(DataManager.team_players.get(team).size() >= DataManager.maxTeamsize) {
							p.sendMessage(DataManager.prefix + "§cDas Team " + TeamManager.getTeamColor(team) + team + " §cist bereits voll.");
							return;
						}
						byte a = e.getCurrentItem().getData().getData();
						BannerMeta bm = (BannerMeta) p.getInventory().getItem(0).getItemMeta();
						bm.setBaseColor(byteColorToDyeColor(a));
						p.getInventory().getItem(0).setItemMeta(bm);
						
						Scoreboard sc = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
						Team old = sc.getEntryTeam(p.getName());
						if(old != null) {
							old.removeEntry(p.getName());
						}
						Team neu = sc.getTeam(team);
						if(neu != null) {
							neu.addEntry(p.getName());
						}
						for(String all : DataManager.team_players.keySet()) {
							if(DataManager.team_players.get(all).contains(p)) {
								DataManager.team_players.get(all).remove(p);
								p.sendMessage(DataManager.prefix + "Du hast " + TeamManager.getTeamColor(all) + "§oTeam " + all + " §3verlassen");
							}
						}						
						DataManager.team_players.get(team).add(p);
						p.sendMessage(DataManager.prefix + "Du bist " + TeamManager.getTeamColor(team) + "§oTeam " + team + " §3beigetreten.");
						Countdown.start();
					}
				}
			}
		}
	}
	
	public static void openTeamwahl(Player p) {
		Inventory inv = Bukkit.createInventory(null, getSlots(MapData.getTeamCount()), "§7» §cWähle dein Team");
		
		ItemStack none = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)7);
		ItemMeta nm = none.getItemMeta();
		nm.setDisplayName("§c");
		none.setItemMeta(nm);
		for(int i = 0; i <= 8; i ++) {
			inv.setItem(i, none);
		}
		for(int i = inv.getSize()-9; i < inv.getSize(); i ++) {
			inv.setItem(i, none);
		}
		HashMap<String, ChatColor> teams = MapData.getTeam();
		for(String all : teams.keySet()) {
			ItemStack team = new ItemStack(Material.WOOL, 1, ChatColorToColorByte(teams.get(all)));
			ItemMeta tm = team.getItemMeta();
			tm.setDisplayName(teams.get(all) + all);
			List<String> names = new ArrayList<String>();
			int i = 1;
			for(Player pls : DataManager.team_players.get(all)) {
				names.add("§a" + i + ". " + TeamManager.getTeamColor(all) + pls.getName());
				i ++;
			}
			tm.setLore(names);
			team.setItemMeta(tm);
			inv.addItem(team);
		}
		p.openInventory(inv);
	}
	
	public static int getSlots(int teams) {
		int slot = 9;
		while(teams > slot) {
			slot += 9;
		}
		slot += 18;
		return slot;
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
	
	@SuppressWarnings("deprecation")
	public static DyeColor byteColorToDyeColor(byte b) {
		return DyeColor.getByWoolData(b);
	}

}
