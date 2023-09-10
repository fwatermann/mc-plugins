package de.legitfinn.bedwars.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.legitfinn.bedwars.main.BedWars;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.TeamManager;
public class CompassListener implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(TeamManager.isSpectator(e.getPlayer())) {
			if(e.getPlayer().getItemInHand().getType().equals(Material.COMPASS)) {
				openCompass(e.getPlayer());
			}
		}
	}
	
	public static void openCompass(Player p) {
		Inventory inv = Bukkit.createInventory(null, getSlots(getIngamePlayers()), "§7» §cTeleporter");
		for(String team : DataManager.team_players.keySet()) {
			for(Player all : DataManager.team_players.get(team)) {
				ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
				SkullMeta meta = (SkullMeta) is.getItemMeta();
				meta.setDisplayName("§7» " + TeamManager.getTeamColor(team) + all.getName());
				meta.setOwner(all.getName());
				is.setItemMeta(meta);
				inv.addItem(is);
			}
		}
		
		p.openInventory(inv);
		Bukkit.getServer().getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
			
			@Override
			public void run() {
				p.updateInventory();
			}
		}, 5L);
		
	}
	
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null) {
			if(e.getCurrentItem() != null) {
				if(e.getClickedInventory().getTitle().equalsIgnoreCase("§7» §cTeleporter")) {
					e.setCancelled(true);
					if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
						if(e.getCurrentItem().hasItemMeta()) {
							SkullMeta meta = (SkullMeta)e.getCurrentItem().getItemMeta();
							Player to = Bukkit.getPlayer(meta.getOwner());
							if(to != null && to.isOnline()) {
								((Player)e.getWhoClicked()).teleport(to.getLocation());
							}
						}
					}
				}
			}
		}
	}
	

	public static int getSlots(int count) {
		int slot = 9;
		while(count > slot) {
			slot += 9;
		}
		return slot;
	}
	
	public static int getIngamePlayers() {
		int i = 0;
		for(String team : DataManager.team_players.keySet()) {
			i += DataManager.team_players.get(team).size();
		}
		return i;
	}
	
	
}
