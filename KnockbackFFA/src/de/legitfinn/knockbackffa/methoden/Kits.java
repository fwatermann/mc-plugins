package de.legitfinn.knockbackffa.methoden;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import de.NightShadwo.SignAPI.Connectoin.Map;
import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Main.Types.Gamemode;
import de.NightShadwo.SignAPI.Main.Types.InformationTyps;
import de.legitfinn.knockbackffa.SQL.StatisticSQL;
import de.legitfinn.knockbackffa.listener.JoinListener;
import de.legitfinn.knockbackffa.listener.WorldListener;
import de.legitfinn.knockbackffa.main.DataManager;
import de.legitfinn.knockbackffa.main.main;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;

public class Kits implements Listener {

	public static HashMap<Player, Integer> player_kills = new HashMap<Player, Integer>();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		
		if(e.getPlayer().getItemInHand().getType().equals(Material.CHEST)) {
			if(e.getPlayer().getLocation().distance(ConfigManager.getSpawn()) <= 15) {
				e.setCancelled(true);
				openKits(e.getPlayer());
			}
		} else if(e.getPlayer().getItemInHand().getType().equals(Material.SKULL_ITEM)) {
			if(e.getPlayer().getItemInHand().hasItemMeta()) {
				SkullMeta meta = (SkullMeta)e.getPlayer().getItemInHand().getItemMeta();
				if(meta.getDisplayName().equalsIgnoreCase("§7» §cSpiel verlassen §7§o<Rechtsklick>")) {
					e.getPlayer().kickPlayer("Runde verlassen");
				}
			}
		} else if(e.getPlayer().getItemInHand().getType().equals(Material.FLINT_AND_STEEL)) {
			if(!e.getPlayer().getAllowFlight()) {
				e.getPlayer().setItemInHand(null);
				e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().normalize().setY(2));
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(!DataManager.ingame.contains(e.getPlayer())) {
			if(e.getTo().getWorld().equals(ConfigManager.getSpawn().getWorld())) {
				if(e.getTo().distance(ConfigManager.getSpawn()) > 15) {
					DataManager.ingame.add(e.getPlayer());
					e.getPlayer().setGameMode(GameMode.SURVIVAL);
					setKit(e.getPlayer(), DataManager.Kit);
				}
			}
		} else if(DataManager.ingame.contains(e.getPlayer())) {
			if(e.getTo().getY() <= 0) {
				if(!e.getPlayer().isDead()) {
					e.getPlayer().damage(e.getPlayer().getHealth());
				}
			}
		}
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player)e.getWhoClicked();
			if(e.getClickedInventory() != null && e.getCurrentItem() != null) {
				if(e.getClickedInventory().getTitle().equals("§6Wähle dein Kit")) {
					e.setCancelled(true);
					int slot = e.getRawSlot();
					
					if((slot >= 2 && slot <= 6) || (slot >= 11 && slot <= 15) || (slot >= 11+9 && slot <= 15+9)) {
						if(slot <= 8) {
							slot += 9;
						} else if(slot >= 18) {
							slot -= 9;
						}
						
						int kit = slot - 10;
						
						p.openInventory(Kits.getKitCurrent(p, kit));
						p.sendMessage(DataManager.prefix + "§3Wenn du mit dem Sortieren deines Kits fertig bist, schließe das Inventar.");
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e){
		String name = ChatColor.stripColor(e.getInventory().getName());
		if(name.contains("Kit Nr.: ")){
			int kit = Integer.parseInt(name.replace("Kit Nr.: ", ""));
			boolean b = StatisticSQL.saveKit(kit, ((Player)e.getPlayer()).getUniqueId(), e.getInventory());
			if(b){
				e.getPlayer().sendMessage(DataManager.prefix + "§3Kit §a§ngespeichert§r§3.");
			}
		}
	}
	
	@EventHandler
	public void onInvMove(InventoryClickEvent e){
		if(e.getClickedInventory() != null){
			if (e.getSlot() == e.getRawSlot() || e.getSlotType().equals(SlotType.QUICKBAR)) {
				if (e.getCurrentItem() != null) {
					Player p = (Player) e.getWhoClicked();
					if(!DataManager.ingame.contains(p)){
						if(e.getClickedInventory().getType().equals(InventoryType.PLAYER)){
							e.setCancelled(true);
						}
						if(e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)){
							e.setCancelled(true);
						}
					}
				}
			}
		}else {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemMove(InventoryMoveItemEvent e){
		if(!e.getSource().getName().equals(e.getDestination().getName())){
			e.setCancelled(true);
		}
		if(e.getDestination() == null || e.getSource() == null){
			e.setCancelled(true);
		}
	}
	
	public static Inventory getKitCurrent(Player p, int kit){
		ItemStack knu = new ItemStack(Material.STICK);
		ItemMeta knum = knu.getItemMeta();
		knum.setDisplayName("§bKnüppel");
		knum.addEnchant(Enchantment.KNOCKBACK, 2, true);
		knu.setItemMeta(knum);
		
		Inventory ret = Bukkit.createInventory(null, 9, "§3Kit §7Nr.: " + kit);
		Inventory a = StatisticSQL.getKit(kit, p.getUniqueId());
		if(a == null){
			if(kit == 1) {
				ret.addItem(knu);
			} else if(kit == 2) {
				ret.addItem(knu);
				
				ItemStack enter = new ItemStack(Material.FISHING_ROD);
				ItemMeta em = enter.getItemMeta();
				em.setDisplayName("§aEnterhaken");
				em.spigot().setUnbreakable(true);
				enter.setItemMeta(em);
				
				ret.addItem(enter);
			} else if(kit == 3) {
				
				ItemStack kar = new ItemStack(Material.CARROT_ITEM);
				ItemMeta karm = kar.getItemMeta();
				karm.setDisplayName("§bKarrote");
				karm.addEnchant(Enchantment.KNOCKBACK, 2, true);
				kar.setItemMeta(karm);
				ret.addItem(kar);
				ret.addItem(new ItemStack(Material.SNOW_BALL, 16));			
			} else if(kit == 4) {
				ret.addItem(knu);
				
				ItemStack bow = new ItemStack(Material.BOW);
				ItemMeta bm = bow.getItemMeta();
				bm.setDisplayName("§bBogen");
				bm.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
				bm.spigot().setUnbreakable(true);
				bow.setItemMeta(bm);
				
				ret.addItem(bow);
				ret.addItem(new ItemStack(Material.ARROW, 2));
			} else if(kit == 5) {
				ret.addItem(knu);
				
				ret.addItem(new ItemStack(Material.TNT, 5));
				
				ItemStack gran = new ItemStack(Material.EGG, 1);
				ItemMeta granm = gran.getItemMeta();
				granm.setDisplayName("§bGranate");
				gran.setItemMeta(granm);
				
				ret.addItem(gran);
			}
		}else {
			int slot = 0;
			for(ItemStack i : a.getContents()){
				if(i != null && !i.getType().equals(Material.AIR)){
					ret.setItem(slot, i);
				}
				slot++;
			}
		}
		return ret;
	}
	
	public void setKit(Player p, int kit) {
		ItemStack knu = new ItemStack(Material.STICK);
		ItemMeta knum = knu.getItemMeta();
		knum.setDisplayName("§bKnüppel");
		knum.addEnchant(Enchantment.KNOCKBACK, 2, true);
		knu.setItemMeta(knum);
		
		Inventory inv = StatisticSQL.getKit(kit, p.getUniqueId());
		
		if(inv == null){
			if(kit == 1) {
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				p.getInventory().addItem(knu);
			} else if(kit == 2) {
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				p.getInventory().addItem(knu);
				
				ItemStack enter = new ItemStack(Material.FISHING_ROD);
				ItemMeta em = enter.getItemMeta();
				em.setDisplayName("§aEnterhaken");
				em.spigot().setUnbreakable(true);
				enter.setItemMeta(em);
				
				p.getInventory().addItem(enter);
			} else if(kit == 3) {
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				
				ItemStack kar = new ItemStack(Material.CARROT_ITEM);
				ItemMeta karm = kar.getItemMeta();
				karm.setDisplayName("§bKarrote");
				karm.addEnchant(Enchantment.KNOCKBACK, 2, true);
				kar.setItemMeta(karm);
				p.getInventory().addItem(kar);
				p.getInventory().addItem(new ItemStack(Material.SNOW_BALL, 16));			
			} else if(kit == 4) {
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				p.getInventory().addItem(knu);
				
				ItemStack bow = new ItemStack(Material.BOW);
				ItemMeta bm = bow.getItemMeta();
				bm.setDisplayName("§bBogen");
				bm.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
				bm.spigot().setUnbreakable(true);
				bow.setItemMeta(bm);
				
				p.getInventory().addItem(bow);
				p.getInventory().addItem(new ItemStack(Material.ARROW, 2));
			} else if(kit == 5) {
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				p.getInventory().addItem(knu);
				
				p.getInventory().addItem(new ItemStack(Material.TNT, 5));
				
				ItemStack gran = new ItemStack(Material.EGG, 1);
				ItemMeta granm = gran.getItemMeta();
				granm.setDisplayName("§bGranate");
				gran.setItemMeta(granm);
				
				p.getInventory().addItem(gran);
			}
		}else {
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			int slot = 0;
			for(ItemStack i : inv.getContents()){
				if(i != null && !i.getType().equals(Material.AIR)){
					p.getInventory().setItem(slot, i);
				}
				slot++;
			}
		}
	}
	
	
	public static void openKits(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, "§6Wähle dein Kit");
		int kit = DataManager.Kit;
		
		ItemStack k1 = new ItemStack(Material.STICK);
		ItemMeta m1 = k1.getItemMeta();
		m1.setDisplayName("§8» §7Knüppel Kit");
		m1.setLore(getLore(new String[] {"§f• §7Knüppel [Knockback II]"}));
		if(kit == 1) {
			m1.setDisplayName("§7» §aKnüppel Kit");
			m1.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			m1.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		k1.setItemMeta(m1);
		
		ItemStack k2 = new ItemStack(Material.FISHING_ROD);
		ItemMeta m2 = k2.getItemMeta();
		m2.setDisplayName("§8» §7Enterhaken Kit");
		m2.setLore(getLore(new String[] {"§f• §7Knüppel [Knockback II]", "§f• §7Enterhaken"}));
		if(kit == 2) {
			m2.setDisplayName("§7» §aEnterhaken Kit");
			m2.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			m2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		k2.setItemMeta(m2);
		
		ItemStack k3 = new ItemStack(Material.SNOW_BALL);
		ItemMeta m3 = k3.getItemMeta();
		m3.setDisplayName("§8» §7Schneeman Kit");
		m3.setLore(getLore(new String[] {"§f• §716x Schneeball", "§f• §7Karrote [Knockback II]"}));
		if(kit == 3) {
			m3.setDisplayName("§7» §aSchneeman Kit");
			m3.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			m3.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		k3.setItemMeta(m3);
		
		
		ItemStack k4 = new ItemStack(Material.BOW);
		ItemMeta m4 = k4.getItemMeta();
		m4.setDisplayName("§8» §7Bogenschütze Kit");
		m4.setLore(getLore(new String[] {"§f• §7Knüppel [Knockback II]", "§f• §7Bogen [Punch II]", "§f• §72x Pfeile"}));
		if(kit == 4) {
			m4.setDisplayName("§7» §aBogenschütze Kit");
			m4.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			m4.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		k4.setItemMeta(m4);
		
		ItemStack k5 = new ItemStack(Material.TNT);
		ItemMeta m5 = k5.getItemMeta();
		m5.setDisplayName("§8» §7Bombenleger Kit");
		m5.setLore(getLore(new String[] {"§f• §7Knüppel [Knockback II]", "§f• §75x TNT", "§f• §7Granate"}));
		if(kit == 5) {
			m5.setDisplayName("§7» §aBombenleger Kit");
			m5.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			m5.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		k5.setItemMeta(m5);
		
		ItemStack activ = new ItemStack(Material.INK_SACK, 1, (short) 10);
		ItemMeta am = activ.getItemMeta();
		am.setDisplayName("§7» §aAusgewählt");
		activ.setItemMeta(am);
		
		ItemStack inactiv = new ItemStack(Material.INK_SACK, 1, (short)8);
		ItemMeta ia = inactiv.getItemMeta();
		ia.setDisplayName("§8» §7Nicht ausgewählt");
		inactiv.setItemMeta(ia);
		
		ItemStack block = new ItemStack(Material.BARRIER);
		ItemMeta bm = block.getItemMeta();
		bm.setDisplayName("§7» §4Nicht verfügbar");
		bm.setLore(getLore(new String[] {"§7» §cNicht genügend Kills", "§7» §cin dieser Runde"}));
		block.setItemMeta(bm);
		
		inv.setItem(8 + 3, k1);
		inv.setItem(8 + 4, k2);
		inv.setItem(8 + 5, k3);
		inv.setItem(8 + 6, k4);
		inv.setItem(8 + 7, k5);

		p.openInventory(inv);		
	}
	
	public static List<String> getLore(String[] lines) {
		List<String> ret = new ArrayList<String>();
		for(String all : lines) {
			ret.add(all);
		}
		return ret;
	}

	
	@EventHandler
	public void onEnterHaken(PlayerFishEvent e) {
		if(!e.getState().equals(PlayerFishEvent.State.FISHING)) {
			if(e.getCaught() != null) {
				if(e.getCaught() instanceof Player) {
					Player dmg = (Player)e.getCaught();
					Vector v = e.getPlayer().getLocation().toVector().subtract(dmg.getLocation().toVector());
					v = v.normalize().multiply(2).setY(0.5);
					dmg.setVelocity(v);
					e.getPlayer().setItemInHand(null);
					e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_BREAK, 100.0F, 1.0F);
				}
			} else {
				if(!e.getHook().getLocation().clone().subtract(0, 0.5, 0).getBlock().getType().equals(Material.AIR)) {
					Vector v = e.getHook().getLocation().toVector().subtract(e.getPlayer().getLocation().toVector());
					v = v.normalize().multiply(2).setY(0.5);
					e.getPlayer().setVelocity(v);
					e.getPlayer().setItemInHand(null);
				}
			}
		}
	}
	
	public static int kseconds = 1;
	public static int kminutes = 10;
	public static int mseconds = 1;
	public static int mminutes = 20;
	public static int cid1 = -1;
	public static int cid2 = -2;
	
	public static void kitChangeCounter() {
		cid1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				if(Bukkit.getOnlinePlayers().size() > 0) {
					kseconds --;
				}
				if(kminutes == 0 && kseconds == -1) {
					kminutes = 10;
					kseconds = 0;
					Bukkit.getScheduler().cancelTask(cid1);		
					changeKit();
				}
				if(kseconds == -1) {
					kminutes --;
					kseconds = 59;
				}
				String msg = "§7Mapwechsel: §e" + mminutes + " min " + mseconds + " sek §8● §7Kitwechsel: §e" + kminutes + " min " + kseconds + " sek";
				for(Player all : Bukkit.getOnlinePlayers()) {
					sendActionBar(all, msg);
				}
			}
		}, 0, 20);
	}
	
	public static void changeMapCounter() {
		cid2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				if(Bukkit.getOnlinePlayers().size() > 0) {
					mseconds --;
				}
				if(mminutes == 0 && mseconds == -1) {
					mminutes = 20;
					mseconds = 0;
					Bukkit.getScheduler().cancelTask(cid2);
					changeMap();
				}
				if(mseconds == -1) {
					mminutes --;
					mseconds = 59;
				}
				String msg = "§7Mapwechsel: §e" + mminutes + " min " + mseconds + " sek §8● §7Kitwechsel: §e" + kminutes + " min " + kseconds + " sek";
				for(Player all : Bukkit.getOnlinePlayers()) {
					sendActionBar(all, msg);
				}
				
			}
		}, 0, 20);
	}
	
	public static void changeKit() {
		int kitAmount = DataManager.KitAmount;
		Random rnd = new Random();
		DataManager.Kit = rnd.nextInt(kitAmount) + 1;
		kitChangeCounter();
		if(!DataManager.loadingMap) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				all.teleport(ConfigManager.getSpawn());
				DataManager.ingame.remove(all);
				JoinListener.addItems(all);
				sendTitle(all, "§e§lKitwechsel", "", 10, 60, 10);
			}
		}
	}
	
	public static void changeKit(int i) {
		kminutes = 10;
		kseconds = 0;
		DataManager.Kit = i;
		if(!DataManager.loadingMap) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				JoinListener.addItems(all);
				DataManager.ingame.remove(all);
				sendTitle(all, "§e§lKitwechsel", "", 10, 60, 10);
				all.teleport(ConfigManager.getSpawn());
			}
		}
	}
	
	
	public static void changeMap() {
		DataManager.loadingMap = true;
		for(Player all : Bukkit.getOnlinePlayers()) {
			DataManager.ingame.remove(all);
			all.setFallDistance(0.0F);
			all.teleport(Bukkit.getWorlds().get(0).getSpawnLocation().clone().add(0.5, 0.25, 0.5));
			JoinListener.addItems(all);
			sendTitle(all, "§e§lMapwechsel", "§c§lBitte warten...", 10, 40, 10);
		}
		Map map = getNewMap();
		Bukkit.unloadWorld("Map", false);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				try {
					FileUtils.deleteDirectory(new File("./Map"));
				} catch (IOException e) {}
			}
		}, 10);
		
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				SignAPI.sendChange(InformationTyps.MAP, map.name(), "own");
			}
		}, 20);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				DataManager.map.setDifficulty(Difficulty.PEACEFUL);
				Location loc = Bukkit.getWorld("Map").getSpawnLocation().clone().add(0.5, 0.25, 0.5);
				DataManager.loadingMap = false;
				for(Block all : WorldListener.blocks) {
					all.setType(Material.BARRIER);
				}
				WorldListener.blocks.clear();
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.teleport(loc);
					JoinListener.addItems(all);
				}
				MapItems.loadLocations();
				changeMapCounter();
			}
		}, 40);
	}
	
	public static void changeMap(Map map) {
		mminutes = 20;
		mseconds = 0;
		Bukkit.getScheduler().cancelTask(cid2);
		DataManager.loadingMap = true;
		for(Player all : Bukkit.getOnlinePlayers()) {
			DataManager.ingame.remove(all);
			all.setFallDistance(0.0F);
			all.teleport(Bukkit.getWorlds().get(0).getSpawnLocation().clone().add(0.5, 0.25, 0.5));
			JoinListener.addItems(all);
			sendTitle(all, "§e§lMapwechsel", "§c§lBitte warten...", 10, 40, 10);
		}
		Bukkit.unloadWorld("Map", false);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				try {
					FileUtils.deleteDirectory(new File("./Map"));
				} catch (IOException e) {}
			}
		}, 10);
		
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				SignAPI.sendChange(InformationTyps.MAP, map.name(), "own");
			}
		}, 20);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				DataManager.map = Bukkit.getWorld("Map");
				DataManager.map.setDifficulty(Difficulty.PEACEFUL);
				Location loc = Bukkit.getWorld("Map").getSpawnLocation().clone().add(0.5, 0.25, 0.5);
				DataManager.loadingMap = false;
				for(Block all : WorldListener.blocks) {
					all.setType(Material.BARRIER);
				}
				WorldListener.blocks.clear();
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.teleport(loc);
					JoinListener.addItems(all);
				}
				changeMapCounter();
				MapItems.loadLocations();
			}
		}, 40);
	}
	
	public static Map getNewMap() {
		
		Random rnd = new Random();
		if(DataManager.alreadyMaps.size() >= SignAPI.getMaps(Gamemode.KNOCKBACKFFA).size()) {
			DataManager.alreadyMaps.clear();
		}
		Map nmap = SignAPI.getMaps(Gamemode.KNOCKBACKFFA).get(rnd.nextInt(SignAPI.getMaps(Gamemode.KNOCKBACKFFA).size()));
		if(!DataManager.alreadyMaps.contains(nmap.name())) {
			DataManager.alreadyMaps.add(nmap.name());
			return nmap;
		} else {
			return getNewMap();
		}
	}
	
	
	public static void sendActionBar(Player p, String msg) {
		IChatBaseComponent ic = ChatSerializer.a("{\"text\":\"" + msg + "\"}");
		PacketPlayOutChat packet = new PacketPlayOutChat(ic, (byte)2);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	}
	
	public static void sendTitle(Player p, String title, String subtitle, int fadein, int stay, int fadeout) {
		PacketPlayOutTitle p1 = new PacketPlayOutTitle(EnumTitleAction.TIMES, ChatSerializer.a(""), fadein, stay, fadeout);
		PacketPlayOutTitle p2 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("[{\"text\":\"" + subtitle + "\"}]"));
		PacketPlayOutTitle p3 = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("[{\"text\":\"" + title + "\"}]"));
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(p1);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(p2);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(p3);
	}
	
	
	
}
