package de.legitfinn.lobby.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.legitfinn.lobby.main.ConfigManager;
import de.legitfinn.lobby.main.main;

public class Navigator implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getPlayer().getItemInHand().getType().equals(Material.COMPASS)) {
				openNavigator(e.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null) {
			if(e.getCurrentItem() != null) {
				if(e.getRawSlot() == e.getSlot()) {
					if(e.getClickedInventory().getTitle().equalsIgnoreCase("§e§lWähle einen Spielmodus")) {
						e.setCancelled(true);
						int slot = e.getRawSlot();
						Player p = (Player)e.getWhoClicked();
						if(slot == 21) {
							Location loc = ConfigManager.getLocation("Warp.SG");
							if(loc != null) {
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							} else {
								p.sendMessage("§4§lLobby §7» §cDie Position für diesen Warp wurde noch nicht gesetzt.");
								p.playSound(p.getLocation(), Sound.FIZZ, 1, 2);
							}
						} else if(slot == 12) {
							Location loc = ConfigManager.getLocation("Warp.BW");
							if(loc != null) {
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							} else {
								p.sendMessage("§4§lLobby §7» §cDie Position für diesen Warp wurde noch nicht gesetzt.");
								p.playSound(p.getLocation(), Sound.FIZZ, 1, 2);
							}
						} else if(slot == 15) {
							Location loc = ConfigManager.getLocation("Warp.Tit");
							if(loc != null) {
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							} else {
								p.sendMessage("§4§lLobby §7» §cDie Position für diesen Warp wurde noch nicht gesetzt.");
								p.playSound(p.getLocation(), Sound.FIZZ, 1, 2);
							}
						} else if(slot == 8) {
							Location loc = ConfigManager.getLocation("Warp.CW");
							if(loc != null) {
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							} else {
								p.sendMessage("§4§lLobby §7» §cDie Position für diesen Warp wurde noch nicht gesetzt.");
								p.playSound(p.getLocation(), Sound.FIZZ, 1, 2);
							}
						} else if(slot == 11) {
							Location loc = ConfigManager.getLocation("Warp.SW");
							if(loc != null) {
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							} else {
								p.sendMessage("§4§lLobby §7» §cDie Position für diesen Warp wurde noch nicht gesetzt.");
								p.playSound(p.getLocation(), Sound.FIZZ, 1, 2);
							}
						} else if(slot == 14) {
							Location loc = ConfigManager.getLocation("Warp.1vs1");
							if(loc != null) {
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							} else {
								p.sendMessage("§4§lLobby §7» §cDie Position für diesen Warp wurde noch nicht gesetzt.");
								p.playSound(p.getLocation(), Sound.FIZZ, 1, 2);
							}
						} else if(slot == 26) {
							Location loc = ConfigManager.getLocation("Warp.FB");
							if(loc != null) {
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							} else {
								p.sendMessage("§4§lLobby §7» §cDie Position für diesen Warp wurde noch nicht gesetzt.");
								p.playSound(p.getLocation(), Sound.FIZZ, 1, 2);
							}
						} else if(slot == 22) {
							Location loc = ConfigManager.getLocation("Warp.KFFA");
							if(loc != null) {
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							} else {
								p.sendMessage("§4§lLobby §7» §cDie Position für diesen Warp wurde noch nicht gesetzt.");
								p.playSound(p.getLocation(), Sound.FIZZ, 1, 2);
							}
						} else if(slot == 23) {
							Location loc = ConfigManager.getLocation("Warp.OITC");
							if(loc != null) {
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							} else {
								p.sendMessage("§4§lLobby §7» §cDie Position für diesen Warp wurde noch nicht gesetzt.");
								p.playSound(p.getLocation(), Sound.FIZZ, 1, 2);
							}
						} else if(slot == 13) {
							Location loc = ConfigManager.getLocation("Warp.KPVP");
							if(loc != null) {
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							} else {
								p.sendMessage("§4§lLobby §7» §cDie Position für diesen Warp wurde noch nicht gesetzt.");
								p.playSound(p.getLocation(), Sound.FIZZ, 1, 2);
							}
						} else if(slot == 5) {
							Location loc = ConfigManager.getLocation("Warp.Spawn");
							if(loc != null) {
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							} else {
								p.sendMessage("§4§lLobby §7» §cDie Position für diesen Warp wurde noch nicht gesetzt.");
								p.playSound(p.getLocation(), Sound.FIZZ, 1, 2);
							}
						} else if(slot == 3) {
							Location loc = ConfigManager.getLocation("Warp.Community");
							if(loc != null) {
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							} else {
								p.sendMessage("§4§lLobby §7» §cDie Position für diesen Warp wurde noch nicht gesetzt.");
								p.playSound(p.getLocation(), Sound.FIZZ, 1, 2);
							}
						} else if(slot == 18) {
							Location loc = ConfigManager.getLocation("Warp.Bewerben");
							if(loc != null) {
								p.teleport(loc);
								p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1, 1);
							} else {
								p.sendMessage("§4§lLobby §7» §cDie Position für diesen Warp wurde noch nicht gesetzt.");
								p.playSound(p.getLocation(), Sound.FIZZ, 1, 2);
							}
						}
	 				}
				}
			}
		}
	}
	
	public static void setupNavigator(){
		
		Inventory inv = Bukkit.createInventory(null, 27, "§e§lWähle einen Spielmodus");
		
		ItemStack sg = new ItemStack(Material.CHEST);
		ItemMeta sgm = sg.getItemMeta();
		sgm.setDisplayName("§7§l» §aSurvivalGames");
		sg.setItemMeta(sgm);
		
		ItemStack bw = new ItemStack(Material.BED);
		ItemMeta bwm = bw.getItemMeta();
		bwm.setDisplayName("§7§l» §cBedWars");
		bw.setItemMeta(bwm);
		
		ItemStack ti = new ItemStack(Material.WEB);
		ItemMeta tim = ti.getItemMeta();
		tim.setDisplayName("§7§l» §aTrainIt");
		ti.setItemMeta(tim);
		
		ItemStack sw = new ItemStack(Material.GRASS);
		ItemMeta swm = sw.getItemMeta();
		swm.setDisplayName("§7§l» §bSkyWars");
		sw.setItemMeta(swm);
		
		ItemStack fb = new ItemStack(Material.STONE_SWORD);
		ItemMeta fbm = fb.getItemMeta();
		fbm.setDisplayName("§7§l» §ePvP-Training");
		fb.setItemMeta(fbm);
		
		ItemStack kp = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta kpm = kp.getItemMeta();
		kpm.setDisplayName("§7§l» §9KitPvP");
		kp.setItemMeta(kpm);
		
		List<String> lorec = new ArrayList<String>();
		lorec.add("§7§l» §cOne in the chamber");
		lorec.add("§7§l» §7MobArena");
		lorec.add("§7§l» §7IslandFight");
		lorec.add("§7§l» §7BedrockTowerBattle");
		lorec.add("§7§l» §7Capture the flag");
		
		ItemStack oi = new ItemStack(Material.BOW);
		ItemMeta oim = oi.getItemMeta();
		oim.setDisplayName("§7§l» §cAndere Minigames");
		oim.setLore(lorec);;
		oi.setItemMeta(oim);
		
		ItemStack bt = new ItemStack(Material.FISHING_ROD);
		ItemMeta btm = bt.getItemMeta();
		btm.setDisplayName("§7§l» §9KnockbackFFA");
		bt.setItemMeta(btm);
		
		ItemStack vs = new ItemStack(Material.GOLD_CHESTPLATE);
		ItemMeta vsm = vs.getItemMeta();
		vsm.setDisplayName("§7§l» §e1vs1");
		vs.setItemMeta(vsm);
		
		ItemStack cw = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta cwm = (LeatherArmorMeta) cw.getItemMeta();
		cwm.setDisplayName("§7§l» §cClanWars");
		cwm.setColor(Color.ORANGE);
		cw.setItemMeta(cwm);
		
		ItemStack cm = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta cmm = (SkullMeta) cm.getItemMeta();
		cmm.setDisplayName("§7§l» §6Community");
		cm.setItemMeta(cmm);
		
		ItemStack sp = new ItemStack(Material.MAGMA_CREAM);
		ItemMeta spm = sp.getItemMeta();
		spm.setDisplayName("§7§l» §aSpawn");
		sp.setItemMeta(spm);
		
		ItemStack be = new ItemStack(Material.WORKBENCH);
		ItemMeta bem = be.getItemMeta();
		bem.setDisplayName("§7§l» §bBewerben");
		be.setItemMeta(bem);
		
		ItemStack backup = oi.clone();
		
		inv.setItem(21, sg);
		inv.setItem(12, bw);
		inv.setItem(15, ti);
		inv.setItem(8, cw);
		inv.setItem(11, sw);
		inv.setItem(14, vs);
		inv.setItem(26, fb);
		inv.setItem(22, bt);
		inv.setItem(23, oi);
		inv.setItem(13, kp);
		inv.setItem(5, sp);
		inv.setItem(3, cm);
		inv.setItem(18, be);
				
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				ItemStack item = inv.getItem(23);
				ItemMeta meta = item.getItemMeta();
				if(item.getType() == Material.BONE){
					item.setType(Material.WATCH);


					List<String> lore = new ArrayList<String>();
					lore.add("§7§l» §7One in the chamber");
					lore.add("§7§l» §7MobArena");
					lore.add("§7§l» §cIslandFight");
					lore.add("§7§l» §7BedrockTowerBattle");
					lore.add("§7§l» §7Capture the flag");
					meta.setLore(lore);
					
					item.setItemMeta(meta);
				}else if(item.getType() == Material.WATCH){
					item.setType(Material.BEDROCK);
					
					List<String> lore = new ArrayList<String>();
					lore.add("§7§l» §7One in the chamber");
					lore.add("§7§l» §7MobArena");
					lore.add("§7§l» §7IslandFight");
					lore.add("§7§l» §cBedrockTowerBattle");
					lore.add("§7§l» §7Capture the flag");
					meta.setLore(lore);
					
					item.setItemMeta(meta);
				}else if(item.getType() == Material.BEDROCK){
					item.setType(Material.BANNER);
					item.setDurability((short) 2);

					List<String> lore = new ArrayList<String>();
					lore.add("§7§l» §7One in the chamber");
					lore.add("§7§l» §7MobArena");
					lore.add("§7§l» §7IslandFight");
					lore.add("§7§l» §7BedrockTowerBattle");
					lore.add("§7§l» §cCapture the flag");
					meta.setLore(lore);
					
					item.setItemMeta(meta);
				}else if(item.getType() == Material.BANNER){
					inv.setItem(23, backup.clone());
				}else if(item.getType() == Material.BOW){
					item.setType(Material.BONE);

					List<String> lore = new ArrayList<String>();
					lore.add("§7§l» §7One in the chamber");
					lore.add("§7§l» §cMobArena");
					lore.add("§7§l» §7IslandFight");
					lore.add("§7§l» §7BedrockTowerBattle");
					lore.add("§7§l» §7Capture the flag");
					meta.setLore(lore);
					
					item.setItemMeta(meta);
				}
				main.compass = inv;
			}
		}, 0, 20);
		
	}
	
	public void openNavigator(Player p) {
		
		
		p.openInventory(main.compass);
		
	}
	
	

}
