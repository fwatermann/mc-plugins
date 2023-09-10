package de.legitfinn.replaysystem.methoden;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import com.mojang.authlib.properties.Property;

import de.legitfinn.replaysystem.main.Main;
import de.legitfinn.replaysystem.main.ReplayAPI;
import de.legitfinn.replaysystem.utils.FullPlayerMoveEvent;
import de.legitfinn.replaysystem.utils.Utf8YamlConfiguration;

public class ReplayRecorder implements Listener {

	public static long start = -1L;
	public static File file;
	public static FileConfiguration replay;
	
	public static HashMap<String, String> buffer = new HashMap<String, String>();
	
	public static void start(World w, String id) {
		if(id == null) {
			Main.replayId = Main.generateRandomId();
		} else {
			Main.replayId = id;
		}
		w.save();
		try {
			FileUtils.copyDirectory(w.getWorldFolder(), new File(Main.path + "/" + Main.replayId + "/world"));
		} catch (IOException e) {}
		file = new File(Main.path + "/" + Main.replayId + "/replay.data");
		replay = Utf8YamlConfiguration.loadConfiguration(file);
		
		ArrayList<String> players = new ArrayList<String>();
		for(Player all : Bukkit.getOnlinePlayers()) {
			String a = all.getUniqueId().toString() + ";" + all.getName() + ";" + all.getLocation().getWorld().getName() + ";" + all.getLocation().getX() + ";" + all.getLocation().getY() + ";" + all.getLocation().getZ();
			Collection<Property> tex = ((CraftPlayer)all).getProfile().getProperties().get("textures");
			String textures = "";
			for(Property b : tex) {
				if(!textures.isEmpty()) {
					textures += ";" + b.getValue();
					textures += ";" + b.getSignature();
				} else {
					textures = "";
					textures += ";" + b.getValue();
					textures += ";" + b.getSignature();
				}
			}
			a += textures;
			players.add(a);
		}
		replay.set("Players.list", players);
		
		Scoreboard sc = Bukkit.getScoreboardManager().getMainScoreboard();
		ArrayList<String> scTeams = new ArrayList<String>();		
		for(Team all : sc.getTeams()) {
			String a = ReplayAPI.getInstance().toRecordString(all.getName()) + ";" + ReplayAPI.getInstance().toRecordString(all.getPrefix()) + ";" + ReplayAPI.getInstance().toRecordString(all.getSuffix());
			String entrys = "[";
			for(String ent : all.getEntries()) {
				if(entrys.equalsIgnoreCase("[")) {
					entrys += ent;
				} else {
					entrys += "<!=>" + ent;
				}
			}
			entrys += "]";
			a += ";" + entrys;
			a = ReplayAPI.getInstance().toRecordString(a);
			scTeams.add(a);
		}
		replay.set("Scoreboard.teams.list", scTeams);
		
		Objective obj = sc.getObjective(DisplaySlot.SIDEBAR);
		if(obj != null) {
			String a = obj.getName() + ";" + obj.getDisplayName();
			for(String all : sc.getEntries()) {
				if(obj.getScore(all) != null) {
					System.out.println(all);
				}
			}
			a.isEmpty();
		} 
		
		
		
		
		try{
			replay.save(file);
		} catch(IOException ex){}
		
		start = System.currentTimeMillis();
		System.out.println(">>");
		System.out.println(">>");
		System.out.println(">> Replay Aufnahme gestartet! ReplayId: " + Main.replayId);
		System.out.println(">>");
		System.out.println(">>");
	}
	
	public static void stop() {
		long duration = System.currentTimeMillis() - start;
		replay.set("Replay.duration", duration);
		start = -1;
		
		ArrayList<String> pack = new ArrayList<String>();
		for(String all : buffer.keySet()) {
			String a = all + "<834843>" + buffer.get(all);
			pack.add(a);
		}
		buffer.clear();
//		replay.set("Replay.data", pack);
		
		try {
//			PrintWriter writer = new PrintWriter(new FileOutputStream(new File(Main.path + "/" + Main.replayId + "/replay.repl")));
			Writer w = new OutputStreamWriter(new FileOutputStream(new File(Main.path + "/" + Main.replayId + "/replay.repl")), "UTF-8");
			for(String all : pack) {
				w.write(all + "\n");
				w.flush();
			}
			w.close();
		} catch (IOException e) {}
		try{
			replay.save(file);
			System.out.println(">>");
			System.out.println(">>");
			System.out.println(">> Replay gespeichert! ReplayId: " + Main.replayId);
			System.out.println(">>");
			System.out.println(">>");
		} catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public static void recordeMovement(Player p, Location loc) {
		if(start != -1) {
			long ms = System.currentTimeMillis() - start;
			if(!buffer.containsKey(ms + "")) {
				String a = "Movement;" + p.getUniqueId().toString() + ";" + loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
				buffer.put(ms + "", a);
			} else {
				int i = 1;
				while(buffer.containsKey(ms + "_" + i)) {
					i ++;
				}
				String a = "Movement;" + p.getUniqueId().toString() + ";" + loc.getWorld().getName() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
				buffer.put(ms + "_" + i, a);
			}
		}
	}
	
	public static void recordAnimation(Player p, int i) {
		if(start != -1) {
			long ms = System.currentTimeMillis() - start;
			if(!buffer.containsKey(ms + "")) {
				String a = "Animation;" + p.getUniqueId().toString() + ";" + i;
				buffer.put(ms + "", a);
			} else {
				int i1 = 1;
				while(buffer.containsKey(ms + "_" + i1)) {
					i1 ++;
				}
				String a = "Animation;" + p.getUniqueId().toString() + ";" + i;
				buffer.put(ms + "_" + i1, a);
			}
		}
	}
	
	public static void recordMetadata(Player p, int i) {
		if(start != -1) {
			long ms = System.currentTimeMillis() - start;
			if(!buffer.containsKey(ms + "")) {
				String a = "Metadata;" + p.getUniqueId().toString() + ";" + i;
				buffer.put(ms + "", a);
			} else {
				int i1 = 1;
				while(buffer.containsKey(ms + "_" + i1)) {
					i1 ++;
				}
				String a = "Metadata;" + p.getUniqueId().toString() + ";" + i;
				buffer.put(ms + "_" + i1, a);
			}
		}
	}
	
	public static void recordEquipment(Player p, ItemStack[] armor, ItemStack hand) {
		if(start != -1) {
			long ms = System.currentTimeMillis() - start;
			if(!buffer.containsKey(ms + "")) {
				String a = "Equipment;" + p.getUniqueId().toString() + ";" + getStringOfItemStack(armor[0])  + ";" + getStringOfItemStack(armor[1]) + ";" + getStringOfItemStack(armor[2]) + ";" + getStringOfItemStack(armor[3]) + ";" + getStringOfItemStack(hand);
				buffer.put(ms + "", a);
			} else {
				int i1 = 1;
				while(buffer.containsKey(ms + "_" + i1)) {
					i1 ++;
				}
				String a = "Equipment;" + p.getUniqueId().toString() + ";" + getStringOfItemStack(armor[0])  + ";" + getStringOfItemStack(armor[1]) + ";" + getStringOfItemStack(armor[2]) + ";" + getStringOfItemStack(armor[3]) + ";" + getStringOfItemStack(hand);
				buffer.put(ms + "_" + i1, a);
			}
		}
	}
	
	public static void recordShoot(Player p, EntityType type, Vector v) {
		if(start != -1) {
			long ms = System.currentTimeMillis() - start;
			if(!buffer.containsKey(ms + "")) {
				String a = "Shoot;" + p.getUniqueId().toString() + ";" + v.getX() + ";" + v.getY() + ";" + v.getZ() + ";" + type.toString();
				buffer.put(ms + "", a);
			} else {
				int i1 = 1;
				while(buffer.containsKey(ms + "_" + i1)) {
					i1 ++;
				}
				String a = "Shoot;" + p.getUniqueId().toString() + ";" + v.getX() + ";" + v.getY() + ";" + v.getZ() + ";" + type.toString();
				buffer.put(ms + "_" + i1, a);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void recordBlockPlace(Player p, Block b) {
		if(start != -1) {
			long ms = System.currentTimeMillis() - start;
			if(!buffer.containsKey(ms + "")) {
				String a = "BlockPlace;" + p.getUniqueId().toString() + ";" + b.getType().toString() + ";" + b.getData() + ";" + b.getX() + ";" + b.getY() + ";" + b.getZ();
				buffer.put(ms + "", a);
			} else {
				int i1 = 1;
				while(buffer.containsKey(ms + "_" + i1)) {
					i1 ++;
				}
				String a = "BlockPlace;" + p.getUniqueId().toString() + ";" + b.getType().toString() + ";" + b.getData() + ";" + b.getX() + ";" + b.getY() + ";" + b.getZ();
				buffer.put(ms + "_" + i1, a);
			}
		}
	}
	
	public static void recordBlockBreak(Player p, Block b) {
		if(start != -1) {
			long ms = System.currentTimeMillis() - start;
			if(!buffer.containsKey(ms + "")) {
				String a = "blockBreak;" + p.getUniqueId().toString() + ";" + b.getX() + ";" + b.getY() + ";" + b.getZ();
				buffer.put(ms + "", a);
			} else {
				int i1 = 1;
				while(buffer.containsKey(ms + "_" + i1)) {
					i1 ++;
				}
				String a = "blockBreak;" + p.getUniqueId().toString() + ";" + b.getX() + ";" + b.getY() + ";" + b.getZ();
				buffer.put(ms + "_" + i1, a);
			}
		}
	}
	
	public static void recordSpawnItem(Item item) {
		if(start != -1) {
			long ms = System.currentTimeMillis() - start;
			if(!buffer.containsKey(ms + "")) {
				String a = "itemDrop;" + item.getEntityId() + ";" + getStringOfItemStack(item.getItemStack()) + ";" + item.getItemStack().getAmount() + ";" + item.getLocation().getX() + ";" +  + item.getLocation().getY() + ";" +  + item.getLocation().getZ() + ";" + item.getVelocity().getX() + ";" + item.getVelocity().getY() + ";" + item.getVelocity().getZ();
				buffer.put(ms + "", a);
			} else {
				int i1 = 1;
				while(buffer.containsKey(ms + "_" + i1)) {
					i1 ++;
				}
				String a = "itemDrop;" + item.getEntityId() + ";" + getStringOfItemStack(item.getItemStack()) + ";" + item.getItemStack().getAmount() + ";" + item.getLocation().getX() + ";" +  + item.getLocation().getY() + ";" +  + item.getLocation().getZ() + ";" + item.getVelocity().getX() + ";" + item.getVelocity().getY() + ";" + item.getVelocity().getZ();
				buffer.put(ms + "_" + i1, a);
			}
		}
	}
	
	public static void recordPickUpItem(Player p, Item item) {
		if(start != -1) {
			long ms = System.currentTimeMillis() - start;
			if(!buffer.containsKey(ms + "")) {
				Vector v = p.getLocation().toVector().subtract(item.getLocation().toVector());
				String a = "itemPickup;" + item.getEntityId() + ";" + v.getX() + ";" + v.getY() + ";" + v.getZ();
				buffer.put(ms + "", a);
			} else {
				int i1 = 1;
				while(buffer.containsKey(ms + "_" + i1)) {
					i1 ++;
				}
				Vector v = p.getLocation().toVector().subtract(item.getLocation().toVector());
				String a = "itemPickup;" + item.getEntityId() + ";" + v.getX() + ";" + v.getY() + ";" + v.getZ();
				buffer.put(ms + "_" + i1, a);
			}
		}
	}
	
	public static void recordBroadcastMessage(String msg) {
		if(start != -1) {
			long ms = System.currentTimeMillis() - start;
			if(!buffer.containsKey(ms + "")) {
				String a = "brMessage;" + ReplayAPI.getInstance().toRecordString(msg.replace(";", "<_:>"));
				buffer.put(ms + "", a);
			} else {
				int i1 = 1;
				while(buffer.containsKey(ms + "_" + i1)) {
					i1 ++;
				}
				String a = "brMessage;" + ReplayAPI.getInstance().toRecordString(msg.replace(";", "<_:>"));
				buffer.put(ms + "_" + i1, a);
			}
		}
	}
	
	public static ArrayList<Player> burning = new ArrayList<Player>();
	public static HashMap<Player, ItemStack[]> lastArmor = new HashMap<Player, ItemStack[]>();
	public static HashMap<Player, ItemStack> lastHand = new HashMap<Player, ItemStack>();
	public static HashMap<Player, Location> lastLocation = new HashMap<Player, Location>();
	
	
	@EventHandler
	public void onMove(FullPlayerMoveEvent e) {
		if(!lastLocation.containsKey(e.getPlayer())) {
			lastLocation.put(e.getPlayer(), e.getPlayer().getLocation());
			recordeMovement(e.getPlayer(), e.getPlayer().getLocation());
		} else {
			if(!lastLocation.get(e.getPlayer()).equals(e.getPlayer().getLocation())) {
				recordeMovement(e.getPlayer(), e.getPlayer().getLocation());
				lastLocation.put(e.getPlayer(), e.getPlayer().getLocation());
			}
		}
		if(e.getPlayer().getFireTicks() >= 0 && !burning.contains(e.getPlayer())) {
			recordMetadata(e.getPlayer(), 1);
			burning.add(e.getPlayer());
		} else if(e.getPlayer().getFireTicks() <= 0 && burning.contains(e.getPlayer())) {
			recordMetadata(e.getPlayer(), -1);
			burning.remove(e.getPlayer());
		}
		
		if(start != -1) {
			if(!lastArmor.containsKey(e.getPlayer())) {
				lastArmor.put(e.getPlayer(), e.getPlayer().getInventory().getArmorContents());
				lastHand.put(e.getPlayer(), e.getPlayer().getItemInHand());
				recordEquipment(e.getPlayer(), e.getPlayer().getInventory().getArmorContents(), e.getPlayer().getItemInHand());
				System.out.println(1);
			} else if(!lastHand.containsKey(e.getPlayer())) {
				lastHand.put(e.getPlayer(), e.getPlayer().getItemInHand());
				recordEquipment(e.getPlayer(), e.getPlayer().getInventory().getArmorContents(), e.getPlayer().getItemInHand());
				System.out.println(2);
			}
			
			if(!isSameContent(lastArmor.get(e.getPlayer()), e.getPlayer().getInventory().getArmorContents())) {
				recordEquipment(e.getPlayer(), e.getPlayer().getInventory().getArmorContents(), e.getPlayer().getItemInHand());
				lastArmor.put(e.getPlayer(), e.getPlayer().getInventory().getArmorContents());
				lastHand.put(e.getPlayer(), e.getPlayer().getItemInHand());
				System.out.println(3);
			} else if(!isSameItemStack(e.getPlayer().getItemInHand(), lastHand.get(e.getPlayer()))) {
				recordEquipment(e.getPlayer(), e.getPlayer().getInventory().getArmorContents(), e.getPlayer().getItemInHand());
				System.out.println(4 + " | " + e.getPlayer().getItemInHand() + " | " + lastHand.get(e.getPlayer()));
				lastArmor.put(e.getPlayer(), e.getPlayer().getInventory().getArmorContents());
				lastHand.put(e.getPlayer(), e.getPlayer().getItemInHand());
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player)e.getEntity();
			recordAnimation(p, 1);
		}
	}
	
	@EventHandler
	public void onInt(PlayerAnimationEvent e) {
		if(e.getAnimationType().equals(PlayerAnimationType.ARM_SWING)) {
			recordAnimation(e.getPlayer(), 0);
		}
	}
	
	@EventHandler
	public void toggleSneak(PlayerToggleSneakEvent e) {
		if(e.isSneaking()) {
			recordMetadata(e.getPlayer(), 2);
		} else {
			recordMetadata(e.getPlayer(), -2);
		}
	}
	@EventHandler
	public void toggleSprint(PlayerToggleSneakEvent e) {
		if(e.isSneaking()) {
			recordMetadata(e.getPlayer(), 3);
		} else {
			recordMetadata(e.getPlayer(), -3);
		}
	}
	@EventHandler
	public void onShoot(ProjectileLaunchEvent e) {
		if(e.getEntity().getShooter() instanceof Player) {
			Player p = (Player)e.getEntity().getShooter();
			recordShoot(p, e.getEntityType(), e.getEntity().getVelocity());
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		recordBlockBreak(e.getPlayer(), e.getBlock());
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		recordBlockPlace(e.getPlayer(), e.getBlock());
	}
	
	@EventHandler
	public void onSpawn(EntitySpawnEvent e) {
		if(e.getEntity().getType().equals(EntityType.DROPPED_ITEM)) {
			recordSpawnItem((Item)e.getEntity());
		}
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		recordPickUpItem(e.getPlayer(), e.getItem());
	}
	
	@EventHandler
	public void onItem(ItemMergeEvent e) {
		if(start != -1) {
			e.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static String getStringOfItemStack(ItemStack is) {
		String a = "{" + is.getType().toString() + "<!=>" + is.getData().getData() + "<!=>";
		
		if(is.hasItemMeta()) {
			for(Enchantment all : is.getItemMeta().getEnchants().keySet()) {
				String b = "[" + all.getName() + "&=&" + is.getEnchantmentLevel(all) + "]-:-";
				a += b;
			}
		}
		if(is.getType().toString().contains("LEATHER_")) {
			if(is.hasItemMeta()) {
				LeatherArmorMeta m = (LeatherArmorMeta) is.getItemMeta();
				int red = m.getColor().getRed();
				int green = m.getColor().getGreen();
				int blue = m.getColor().getBlue();
				String color = "<!=>" + red + ":-:" + green + ":-:" + blue;
				a += color;
			}
		}
		a += "}";
		return a;
	}
	
	public static boolean isSameContent(ItemStack[] a, ItemStack[] b) {
		if(a.length != b.length) {
			return false;
		}
		for(int i = 0; i < a.length; i ++) {
			if(!a[i].equals(b[i])) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isSameItemStack(ItemStack a, ItemStack b) {
		if(!a.toString().equalsIgnoreCase(b.toString())) {
			return false;
		}
		return true;
	}
	
	
	
	
	
}

