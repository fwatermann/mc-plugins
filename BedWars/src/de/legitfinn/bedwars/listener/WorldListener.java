package de.legitfinn.bedwars.listener;

import java.util.HashMap;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.ConfigManager;
import de.legitfinn.bedwars.methoden.GameState;
import de.legitfinn.bedwars.methoden.TeamManager;

public class WorldListener implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(DataManager.state.equals(GameState.Ingame)) {
			if(!TeamManager.isSpectator(e.getPlayer()) || DataManager.troll.contains(e.getPlayer())) {
				if(!DataManager.placedBlocks.contains(e.getBlock()) && !e.getBlock().getType().equals(Material.BED_BLOCK) && !DataManager.troll.contains(e.getPlayer())) {
					e.setCancelled(true);
//					e.getPlayer().sendMessage(DataManager.prefix + "Du darfst §e§o" + e.getBlock().getType().toString() + " §3nicht zerstören.");
					DataManager.placedBlocks.remove(e.getBlock());
				}
			} else {
				e.setCancelled(true);
			}
		} else {
			e.setCancelled(true);
		}
		if(TeamManager.isSpectator(e.getPlayer()) && !DataManager.troll.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		if(!DataManager.state.equals(GameState.Ingame)) {
			e.setFoodLevel(20);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(DataManager.state.equals(GameState.Ingame)) {
			DataManager.placedBlocks.add(e.getBlock());
			if(e.getBlock().getType().equals(Material.STAINED_GLASS)) {
				String team = TeamManager.getTeamOfPlayer(e.getPlayer());
				if(team != null) {
					e.getBlock().setData(LobbyFunctions.ChatColorToColorByte(TeamManager.getTeamColor(team)));
				} else {
					e.getBlock().setData((byte)0);
				}
			}
		} else {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPickUp(PlayerPickupItemEvent e) {
		if(!DataManager.state.equals(GameState.Ingame)) {
			e.setCancelled(true);
		} else {
			if(TeamManager.isSpectator(e.getPlayer())) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(!DataManager.state.equals(GameState.Ingame)) {
			e.setCancelled(true);
		} else {
			if(TeamManager.isSpectator(e.getPlayer())) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(!DataManager.state.equals(GameState.Ingame)) {
			if(e.getTo().getY() <= 0) {
				e.getPlayer().teleport(ConfigManager.getLobbySpawn());
			}
		} else if(DataManager.state.equals(GameState.Ingame)) {
			if(e.getTo().getY() <= 0) {
				if(!TeamManager.isSpectator(e.getPlayer())) {
					if(!e.getPlayer().isDead()) {
						e.getPlayer().damage(50000.0D);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.PHYSICAL)) {
			Block b = e.getPlayer().getLocation().getBlock();
			if(!(b.getType().equals(Material.STONE_PLATE) || b.getType().equals(Material.WOOD_PLATE) || b.getType().equals(Material.GOLD_PLATE) || b.getType().equals(Material.IRON_PLATE))) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(!DataManager.state.equals(GameState.Ingame)) {
			e.setCancelled(true);
		} else {
			if(e.getEntity().getType().equals(EntityType.VILLAGER)) {
				e.setCancelled(true);
			}
		}
		if(e.getEntity() instanceof Player) {
			Player p = (Player)e.getEntity();
			if(TeamManager.isSpectator(p)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onWeather(WeatherChangeEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onSpawn(CreatureSpawnEvent e) {
		if(!e.getSpawnReason().equals(SpawnReason.SPAWNER_EGG) && !e.getEntity().getType().equals(EntityType.ARMOR_STAND)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		e.setCancelled(true);
	}
	
	public static HashMap<Player, Player> inView = new HashMap<Player, Player>();
	
	@EventHandler
	public void onInteract(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Player) {
			if(e.getDamager() instanceof Player) {
				Player p = (Player) e.getEntity();
				Player dmg = (Player) e.getDamager();
				if(DataManager.state.equals(GameState.Ingame)) {
					if(TeamManager.isSpectator(dmg)) {
						inView.put(dmg, p);
						dmg.setGameMode(GameMode.SPECTATOR);
						dmg.setSpectatorTarget(p);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onSneak(PlayerToggleSneakEvent e) {
		if(inView.containsKey(e.getPlayer())) {
			if(TeamManager.isSpectator(e.getPlayer())) {
				e.getPlayer().setGameMode(GameMode.SURVIVAL);
				e.getPlayer().setAllowFlight(true);
				e.getPlayer().setFlying(true);
				inView.remove(e.getPlayer());
			}
		}
 	}
	
	@EventHandler
	public void onDie(PlayerDeathEvent e) {
		if(inView.containsValue(e.getEntity())) {
			for(Player all : inView.keySet()) {
				if(inView.get(all).equals(e.getEntity())) {
					all.setGameMode(GameMode.SURVIVAL);
					all.setAllowFlight(true);
					all.setFlying(true);
					inView.remove(all);
				}
			}
		}
	}
	
	
	
	
}
