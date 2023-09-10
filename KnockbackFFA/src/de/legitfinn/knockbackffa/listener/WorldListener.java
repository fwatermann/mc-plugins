package de.legitfinn.knockbackffa.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import de.legitfinn.knockbackffa.main.DataManager;
import de.legitfinn.knockbackffa.methoden.ConfigManager;
import de.legitfinn.knockbackffa.methoden.Troll;

public class WorldListener implements Listener {

	public HashMap<Block, Player> mines = new HashMap<Block, Player>();
	
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(Bukkit.getWorlds().get(0).equals(e.getBlock().getWorld())) {
			e.setCancelled(true);
			return;
		}
		if(!Troll.players.contains(e.getPlayer())) {
			e.setCancelled(true);
		} else {
			Troll.addBlock(e.getPlayer(), e.getBlock());
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(Bukkit.getWorlds().get(0).equals(e.getBlock().getWorld())) {
			e.setCancelled(true);
			return;
		}
		if(!Troll.players.contains(e.getPlayer())) {
			if(e.getBlock().getType().equals(Material.TNT)) {
				e.getBlock().setType(Material.AIR);
				TNTPrimed tnt = e.getBlock().getWorld().spawn(e.getBlock().getLocation().clone().add(0.5, 0, 0.5), TNTPrimed.class);
				tnt.setFuseTicks(60);
				tnt.setFireTicks(50000);
				tnt.setCustomName(e.getPlayer().getUniqueId().toString());
				tnt.setCustomName("killer;" + e.getPlayer().getUniqueId());
			}
			if(e.getBlock().getType().equals(Material.FIRE)){
				e.setCancelled(true);
			}
		} else {
			Troll.addBlock(e.getPlayer(), e.getBlock());
		}
		if(e.getBlock().getType().equals(Material.WOOD_PLATE)) {
			mines.put(e.getBlock(), e.getPlayer());
		}
	}
	
	@EventHandler
	public void onExplode1(EntityExplodeEvent e) {
		if(e.getEntity().getType().equals(EntityType.PRIMED_TNT)) {
			e.blockList().clear();
			for(Entity all : e.getEntity().getNearbyEntities(3.5, 5, 3.5)) {
				Location loc1 = all.getLocation().clone();
				Location loc2 = e.getEntity().getLocation().clone();
				if(all instanceof Player) {
					Player p = (Player) all;
					if(DataManager.ingame.contains(p)){
						all.setVelocity(loc1.toVector().subtract(loc2.toVector()).normalize().multiply(1.5).setY(1.5));
					}
					if(e.getEntity().getCustomName().toLowerCase().startsWith("killer;")) {
						UUID uuid = UUID.fromString(e.getEntity().getCustomName().split(";")[1]);
						Player kill = Bukkit.getServer().getPlayer(uuid);
						if(kill != null && kill.isOnline()) {
							((Player) all).damage(0.0D, kill);
						}
					}
				}else {
					all.setVelocity(loc1.toVector().subtract(loc2.toVector()).normalize().multiply(1.5).setY(1.5));
				}
			}
		} else {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if(e.getEntity().getType().equals(EntityType.EGG)) {
			TNTPrimed tnt = e.getEntity().getLocation().getWorld().spawn(e.getEntity().getLocation().clone().add(0.5, 0, 0.5), TNTPrimed.class);
			tnt.setFuseTicks(0);
			if(e.getEntity().getShooter() instanceof Player) {
				Player shoot = (Player)e.getEntity().getShooter();
				tnt.setCustomName("killer;" + shoot.getUniqueId());
			}
		}else if(e.getEntity().getType().equals(EntityType.ARROW)) {
			e.getEntity().remove();
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity().getLocation().distance(ConfigManager.getSpawn()) <= 15) {
			e.setCancelled(true);
		} else {
			e.setDamage(0.0D);
		}
		if(e.getEntity().getLocation().getWorld().equals(Bukkit.getWorlds().get(0))) {
			e.setCancelled(true);
		}
		if(e.getCause().equals(DamageCause.FALL)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(!Troll.players.contains(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPick(PlayerPickupItemEvent e) {
//		if(!Troll.players.contains(e.getPlayer())) {
//			e.setCancelled(true);
//		}
	}
	
	//WaitingLobby
	
	public static ArrayList<Block> blocks = new ArrayList<Block>();
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(DataManager.loadingMap) {
			if(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.BARRIER)) {
				blocks.add(e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN));
				e.getPlayer().getLocation().getBlock().getRelative(BlockFace.DOWN).setType(Material.QUARTZ_BLOCK);
			}
		} else {
			if(e.getPlayer().getLocation().getBlock().getType().equals(Material.WOOD_PLATE)) {
				if(mines.containsKey(e.getPlayer().getLocation().getBlock())) {
					e.getPlayer().getLocation().getBlock().setType(Material.AIR);
					TNTPrimed tnt = e.getPlayer().getLocation().getWorld().spawn(e.getPlayer().getLocation(), TNTPrimed.class);
					tnt.setFuseTicks(0);
					tnt.setCustomName("killer;" + mines.get(e.getPlayer().getLocation().getBlock()).getUniqueId().toString());
					mines.remove(e.getPlayer().getLocation().getBlock());
				}
			}
		}
	}
	
	@EventHandler
	public void onChicken(CreatureSpawnEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onWeather(WeatherChangeEvent e) {
		e.setCancelled(true);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
