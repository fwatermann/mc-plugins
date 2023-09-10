package de.legitfinn.lobby.main;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import de.legitfinn.lobby.SQL.LobbySettingsSQL;
import de.legitfinn.lobby.commands.COMMAND_flight;

public class WorldListener implements Listener {
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(!main.build.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if(!main.build.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(!main.build.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(!main.build.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onWeather(WeatherChangeEvent e) {
		e.setCancelled(true);
		e.getWorld().setGameRuleValue("doDaylightCycle", "false");
		e.getWorld().setTime(3500);
	}
	
	@EventHandler
	public void onExpl(BlockExplodeEvent e) {
		e.blockList().clear();
	}
	
	@EventHandler
	public void onExpl(EntityExplodeEvent e) {
		e.blockList().clear();
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.PHYSICAL)) {
			if(e.getPlayer().getLocation().getBlock().getType().equals(Material.WOOD_PLATE) || e.getPlayer().getLocation().getBlock().getType().equals(Material.STONE_PLATE) 
					|| e.getPlayer().getLocation().getBlock().getType().equals(Material.IRON_PLATE) || e.getPlayer().getLocation().getBlock().getType().equals(Material.GOLD_PLATE)) {
				return;
			}
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		e.setFoodLevel(20);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getCurrentItem() != null) {
			Player p = (Player)e.getWhoClicked();
			if(!main.build.contains(p)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.getTo().getY() <= 0) {
			Location loc = ConfigManager.getLocation("Warp.BW");
			e.getPlayer().teleport(loc);
		}
	}
	
	
	@EventHandler
	public void onGamemode(PlayerGameModeChangeEvent e) {
		if(e.getNewGameMode().equals(GameMode.CREATIVE) || e.getNewGameMode().equals(GameMode.SPECTATOR)) {
			COMMAND_flight.flight.add(e.getPlayer());
		} else {
			COMMAND_flight.flight.remove(e.getPlayer());
			Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
				@Override
				public void run() {
					if(LobbySettingsSQL.getSettingsBoolean(e.getPlayer().getUniqueId(), "DOUBLEJUMP")) {
						e.getPlayer().setAllowFlight(true);
					}
				}
			}, 1);
		}
	}
	

}
