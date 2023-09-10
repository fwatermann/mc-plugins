package de.legitfinn.replaysystem.methoden;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.CraftSound;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import de.legitfinn.replaysystem.main.Main;
import de.legitfinn.replaysystem.main.ReplayAPI;
import de.legitfinn.replaysystem.utils.NPC;
import de.legitfinn.replaysystem.utils.Utf8YamlConfiguration;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;

public class ReplayPlayer implements Listener {

	public static HashMap<String, String> ms_data = new HashMap<String, String>();
	public static Thread playThread;
	public static long at = 0;	
	public static int delay = 1;
	public static long duration = -1;
	public static boolean pause = false;
	
	public static HashMap<UUID, Integer> uuid_entid = new HashMap<UUID, Integer>();
	public static HashMap<UUID, NPC> uuid_npc = new HashMap<UUID, NPC>();
	public static HashMap<NPC, Location> npc_startloc = new HashMap<NPC, Location>();
	
	public UUID getUUIDofNPC(NPC npc) {
		for(UUID uuid : uuid_npc.keySet()) {
			if(uuid_npc.get(uuid).equals(npc)) {
				return uuid;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	public static boolean play(String id) {
		if(!new File(Main.path + "/" + id).exists()) {
			return false;
		}
		
		File file = new File(Main.path + "/" + id + "/replay.data");
		FileConfiguration repl = Utf8YamlConfiguration.loadConfiguration(file);
		duration = repl.getLong("Replay.duration");
		
		try {
			FileUtils.deleteDirectory(new File("./replayWorld"));
			FileUtils.copyDirectory(new File(Main.path + "/" + id + "/world") , new File("./replayWorld"));
			new File("./replayWorld/uid.dat").delete();
		} catch (IOException e) {
			return false;
		}	
		
		Bukkit.getServer().createWorld(new WorldCreator("replayWorld"));
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.teleport(Bukkit.getWorld("replayWorld").getSpawnLocation());
			all.setMaxHealth(4.0D);
			all.setHealth(4.0D);
			all.setFoodLevel(20);
			all.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
		}	
		
		Scoreboard sc = Bukkit.getScoreboardManager().getMainScoreboard();
		for(Team all : sc.getTeams()) {
			all.unregister();
		}
		for(String all : repl.getStringList("Scoreboard.teams.list")) {
			String[] args = all.split(";");
			String name = ReplayAPI.getInstance().toSonderString(args[0]);
			String prefix = ReplayAPI.getInstance().toSonderString(args[1]);
			String suffix = ReplayAPI.getInstance().toSonderString(args[2]);
			System.out.println(name + " | " + prefix + " | " + suffix);
			Team t = sc.registerNewTeam(name);
			t.setPrefix(prefix);
			t.setSuffix(suffix);
			for(String ent : args[3].replace("[", "").replace("]", "").split("<!=>")) {
				t.addEntry(ent);
			}
		}
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable() {
			
			@Override
			public void run() {
				for(String all : repl.getStringList("Players.list")) {
					String[] args = all.split(";");
					UUID uuid = UUID.fromString(args[0]);
					String name = args[1];
					String world = args[2];
					double x = Double.parseDouble(args[3]);
					double y = Double.parseDouble(args[4]);
					double z = Double.parseDouble(args[5]);
					uuid_entid.put(uuid, uuid_entid.size() + 10000);
					Location loc = new Location(Bukkit.getWorld("replayWorld"), x, y, z);
					NPC npc;
					if(args.length >= 8) {
						npc = new NPC(name, loc, uuid_entid.get(uuid), uuid, new String[]{args[6], args[7]});
					} else {
						npc = new NPC(name, loc, uuid_entid.get(uuid), uuid);
					}
					npc_startloc.put(npc, loc);
					uuid_npc.put(uuid, npc);
					npc.spawn();
				}
			}
		}, 20);
		
		try {
			FileReader read = new FileReader(new File(Main.path + "/" + id + "/replay.repl"));
			BufferedReader reader = new BufferedReader(read);
			String tmp = "";
			while((tmp = reader.readLine()) != null) {
				String ms = tmp.split("<834843>")[0];
				ms_data.put(ms, tmp.split("<834843>")[1]);
			}
			reader.close();
		} catch (IOException e) {}
		
		ItemStack pause = new ItemStack(Material.WOOL, 1, (short)14);
		ItemMeta pm = pause.getItemMeta();
		pm.setDisplayName("§cReplay pausieren");
		pause.setItemMeta(pm);
		
		ItemStack vor = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta vm = (SkullMeta) vor.getItemMeta();
		vm.setOwner("MHF_ARROWRIGHT");
		vm.setDisplayName("§7Vorspulen [5 Sekunden] §8»»»");
		vor.setItemMeta(vm);
		
		ItemStack back = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta bm = (SkullMeta)back.getItemMeta();
		bm.setOwner("MHF_ARROWLEFT");
		bm.setDisplayName("§8««« §7Zurückspulen [5 Sekunden]");
		back.setItemMeta(bm);
		
		ItemStack tracker = new ItemStack(Material.COMPASS);
		ItemMeta tm = tracker.getItemMeta();
		tm.setDisplayName("§6Teleporter");
		tracker.setItemMeta(tm);
		
		ItemStack restart = new ItemStack(Material.PISTON_BASE);
		ItemMeta rm = restart.getItemMeta();
		rm.setDisplayName("§cReplay neustarten");
		restart.setItemMeta(rm);
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.setGameMode(GameMode.ADVENTURE);
			all.setAllowFlight(true);
			all.setFlying(true);
			all.spigot().setCollidesWithEntities(false);
			all.getInventory().clear();
			all.getInventory().setArmorContents(null);
			
			all.getInventory().setItem(0, restart);
			all.getInventory().setItem(2, back);
			all.getInventory().setItem(4, pause);
			all.getInventory().setItem(6, vor);
			all.getInventory().setItem(8, tracker);		
		}
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable() {
			@Override
			public void run() {
				startPlayer();
			}
		}, 1*20);
		return true;
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(at <= 0) {
				return;
			}
			e.setCancelled(true);
			ItemStack is = e.getPlayer().getItemInHand();
			
			if(is.getType().equals(Material.WOOL)) {
				if(pause) {
					ItemStack pause = new ItemStack(Material.WOOL, 1, (short)14);
					ItemMeta pm = pause.getItemMeta();
					pm.setDisplayName("§cReplay pausieren");
					pause.setItemMeta(pm);
					for(Player all : Bukkit.getOnlinePlayers()) {
						all.getInventory().setItem(4, pause);
					}
					ReplayPlayer.pause = !ReplayPlayer.pause;
				} else {
					ItemStack pause = new ItemStack(Material.WOOL, 1, (short)5);
					ItemMeta pm = pause.getItemMeta();
					pm.setDisplayName("§cReplay vortsetzen");
					pause.setItemMeta(pm);
					for(Player all : Bukkit.getOnlinePlayers()) {
						all.getInventory().setItem(4, pause);
					}
					ReplayPlayer.pause = !ReplayPlayer.pause;
				}
			} else if(is.getType().equals(Material.SKULL_ITEM)) {
				if(is.hasItemMeta()) {
					SkullMeta meta = (SkullMeta) is.getItemMeta();
					if(meta.getOwner().equalsIgnoreCase("MHF_ARROWLEFT")) {
						for(Player all : Bukkit.getOnlinePlayers()) {
							sendActionBar(all, "§4§l§nDas Zurückspulen kann Blockfehler hervorrufen!");
						}
						backspulen(5000);
					} else if(meta.getOwner().equalsIgnoreCase("MHF_ARROWRIGHT")) {
						for(Player all : Bukkit.getOnlinePlayers()) {
							sendActionBar(all, "§a§l§nVorgespult §7[+5 Sekunden]");
						}
						vorspulen(5000);
					}
				}
			} else if(is.getType().equals(Material.COMPASS)) {
				int i = 9;
				while(i < npc_startloc.size()) {
					i += 9;
				}
				Inventory inv = Bukkit.createInventory(null, i, "Player Tracker");
				for(NPC all : npc_startloc.keySet()) {
					ItemStack npcs = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
					SkullMeta npcm = (SkullMeta) npcs.getItemMeta();
					npcm.setOwner(all.getName());
					List<String> lore = new ArrayList<String>();
					lore.add("§8" + getUUIDofNPC(all));
					npcm.setLore(lore);
					npcm.setDisplayName("§a" + all.getName());
					npcs.setItemMeta(npcm);
					inv.addItem(npcs);
				}
				e.getPlayer().openInventory(inv);
 			} else if(is.getType().equals(Material.PISTON_BASE)) {
				backspulen(at);
 			}
			
		}
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getCurrentItem() != null) {
			if(at <= 0) {
				return;
			}
			e.setCancelled(true);
			if(e.getClickedInventory().getTitle().equalsIgnoreCase("Player Tracker")) {
				if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
					SkullMeta sm = (SkullMeta) e.getCurrentItem().getItemMeta();
					UUID uuid = UUID.fromString(ChatColor.stripColor(sm.getLore().get(0)));
					e.getWhoClicked().teleport(uuid_npc.get(uuid).getLocation());
					e.getWhoClicked().closeInventory();
				}
			}
		}
	}
	
	public static void vorspulen(long ms) {
		long n = at + ms;
		while(at <= n && at < duration) {
			String is = ms_data.get(at + "");
			if(is != null) {
				handlePackets(at + "", is, false);
			}
			if(true) {
				int i = 1;
				while(ms_data.containsKey(at + "_" + i)) {
					handlePackets(at + "", ms_data.get(at + "_" + i), false);
					i ++;
				}
			}
			at ++;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void backspulen(long ms) {
		long n = at - ms;
		if(n < 0) {
			n = 0;
		}
		for(Entity all : Bukkit.getWorld("replayWorld").getEntities()) {
			if(all.getType().equals(EntityType.DROPPED_ITEM)) {
				all.remove();
			}
		}
		for(String all : beforeBlock_location.keySet()) {
			Location loc = beforeBlock_location.get(all);
			loc.getBlock().setType(beforeBlock_material.get(all));
			loc.getBlock().setData(beforeBlock_byte.get(all));
		}
		for(NPC all : npc_startloc.keySet()) {
			all.resetMetadata();
			all.teleport(npc_startloc.get(all));
		}
		beforeBlock_byte.clear();
		beforeBlock_location.clear();
		beforeBlock_material.clear();
		at = 0;
		while(at <= n && at >= 0) {
			String is = ms_data.get(at + "");
			if(is != null) {
				handlePackets(at + "", is, true);
			}
			int x = 0;
			if(true) {
				int i = 1;
				while(ms_data.containsKey(at + "_" + i)) {
					handlePackets(at + "", ms_data.get(at + "_" + i), true);
					i ++;
				}
				x ++;
				if(x >= 250) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {}
					x = 0;
				}
			}
			at ++;
		}
	}
	
	public static HashMap<String, Location> beforeBlock_location = new HashMap<String, Location>();
	public static HashMap<String, Material> beforeBlock_material = new HashMap<String, Material>();
	public static HashMap<String, Byte> beforeBlock_byte = new HashMap<String, Byte>();
	
	@SuppressWarnings("deprecation")
	private static void startPlayer() {
		playThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(at <= duration) {
					String is = ms_data.get(at + "");
					if(is != null) {
						handlePackets(at + "", is, false);
					}
					if(true) {
						int i = 1;
						while(ms_data.containsKey(at + "_" + i)) {
							handlePackets(at + "_" + i, ms_data.get(at + "_" + i), false);
							i ++;
						}
					}
					if(!pause) {
						at ++;
						try {
							Thread.sleep(delay);
						} catch (InterruptedException e) {}
					} else {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {}
					}
					for(Player all : Bukkit.getOnlinePlayers()) {
						all.setLevel((int)(at / 1000));
						if(duration > 0) {
							float exp = ((float)1 / (float)duration) * (float)at;
							all.setExp(exp);
						}
					}
				}
				Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), new Runnable() {
					
					@Override
					public void run() {
						for(Player all : Bukkit.getOnlinePlayers()) {
							all.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
							all.getInventory().clear();
							all.getInventory().setArmorContents(null);
							all.setFallDistance(0.0F);
							all.setMaxHealth(20.0);
							all.setHealth(20.0);
							all.kickPlayer("§7[§bReplay§7] §cDas Replay ist beendet.");
						}
					}
				});

				Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable() {
					@Override
					public void run() {
						Bukkit.getServer().spigot().restart();
					}
				}, 10);
				playThread.stop();
			}
		});
		playThread.start();
	}
	
	public static HashMap<UUID, Block> walk = new HashMap<UUID, Block>();
	public static HashMap<Integer, Integer> item_item = new HashMap<Integer, Integer>();

	@SuppressWarnings({ "deprecation", "unused" })
	public static void handlePackets(String ms, String is, boolean isBack) {
		if(is == null) {
			return;
		}
		Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), new Runnable() {
			
			@Override
			public void run() {
				String[] args = is.split(";");
				if(args[0].equalsIgnoreCase("Movement")) {
					UUID uuid = UUID.fromString(args[1]);
					String world = args[2];
					double x = Double.parseDouble(args[3]);
					double y = Double.parseDouble(args[4]);
					double z = Double.parseDouble(args[5]);
					float yaw = Float.parseFloat(args[6]);
					float pitch = Float.parseFloat(args[7]);
					Location loc = new Location(Bukkit.getWorld("replayWorld"), x, y, z, yaw, pitch);
					uuid_npc.get(uuid).teleport(loc);
					
					if(!loc.clone().add(0, -0.01, 0).getBlock().getType().equals(Material.AIR)) {
						Block b = loc.clone().add(0, -0.01, 0).getBlock();
						if(!walk.containsKey(uuid)) {
							walk.put(uuid, b);
							try {
								playWalkSound(loc.clone().add(0, -0.01, 0).getBlock());
							} catch (Throwable e) {}
						} else {
							if(!walk.get(uuid).equals(b)) {
								try {
									playWalkSound(loc.clone().add(0, -0.01, 0).getBlock());
								} catch (Throwable e) {}
								walk.put(uuid, b);
							}
						}
					} else {
						walk.remove(uuid);
					}
				} else if(args[0].equalsIgnoreCase("Animation")) {
					UUID uuid = UUID.fromString(args[1]);
					int id = Integer.parseInt(args[2]);
					uuid_npc.get(uuid).playAnimation(id);
					if(id == 1) {
						uuid_npc.get(uuid).getLocation().getWorld().playSound(uuid_npc.get(uuid).getLocation(), Sound.HURT_FLESH, 1, 1);
					}
				} else if(args[0].equalsIgnoreCase("Metadata")) {
					UUID uuid = UUID.fromString(args[1]);
					int id = Integer.parseInt(args[2]);
					uuid_npc.get(uuid).playMetadata(id);
				} else if(args[0].equalsIgnoreCase("Equipment")) {
					UUID uuid = UUID.fromString(args[1]);
					ItemStack helmet = getItemStackFromString(args[2]);
					ItemStack chest = getItemStackFromString(args[3]);
					ItemStack leg = getItemStackFromString(args[4]);
					ItemStack boots = getItemStackFromString(args[5]);
					ItemStack hand = getItemStackFromString(args[6]);
					uuid_npc.get(uuid).setEquipment(new ItemStack[]{boots, leg, chest, helmet}, hand);		
				} else if(args[0].equalsIgnoreCase("Shoot")) {
					UUID uuid = UUID.fromString(args[1]);
					double x = Double.parseDouble(args[2]);
					double y = Double.parseDouble(args[3]);
					double z = Double.parseDouble(args[4]);
					Vector v = new Vector(x,y,z);
					EntityType type = EntityType.valueOf(args[5]);
					Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), new Runnable() {
						
						@Override
						public void run() {
							boolean crit = false;
							if(args.length == 6) {
								crit = Boolean.parseBoolean(args[5]);
							}
							uuid_npc.get(uuid).shootEntity(type, v, crit);	
						}
					});		
				} else if(args[0].equalsIgnoreCase("blockPlace")) {
					UUID uuid = UUID.fromString(args[1]);
					Material m = Material.getMaterial(args[2]);
					Byte data = Byte.parseByte(args[3]);
					int x = Integer.parseInt(args[4]);
					int y = Integer.parseInt(args[5]);
					int z = Integer.parseInt(args[6]);
					Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), new Runnable() {
						
						@Override
						public void run() {
							Location loc = new Location(Bukkit.getWorld("replayWorld"), x, y, z);
							if(!beforeBlock_location.containsValue(loc)) {
								beforeBlock_location.put(ms, loc);
								beforeBlock_byte.put(ms, loc.getBlock().getData());
								beforeBlock_material.put(ms, loc.getBlock().getType());	
							}		
							loc.getBlock().setType(m);
							loc.getBlock().setData(data);
							try{
								placeBlock(loc.getBlock());
							} catch(Throwable ex){}
						}
					});	
				} else if(args[0].equalsIgnoreCase("blockBreak")) {
					UUID uuid = UUID.fromString(args[1]);
					int x = Integer.parseInt(args[2]);
					int y = Integer.parseInt(args[3]);
					int z = Integer.parseInt(args[4]);
					Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), new Runnable() {
						
						@Override
						public void run() {
							Location loc = new Location(Bukkit.getWorld("replayWorld"), x, y, z);
							if(!beforeBlock_location.containsValue(loc)) {
								beforeBlock_location.put(ms, loc);
								beforeBlock_byte.put(ms, loc.getBlock().getData());
								beforeBlock_material.put(ms, loc.getBlock().getType());	
							}	
							Block b = loc.getBlock();
							try{
								breakBlock(b);
							} catch(Throwable ex){}
							loc.getBlock().setType(Material.AIR);
						}
					});	
				} else if(args[0].equalsIgnoreCase("itemDrop")) {
					int id = Integer.parseInt(args[1]);
					ItemStack itemstack = getItemStackFromString(args[2]);
					int amount = Integer.parseInt(args[3]);
					double x = Double.parseDouble(args[4]);
					double y = Double.parseDouble(args[5]);
					double z = Double.parseDouble(args[6]);
					double vx = Double.parseDouble(args[7]);
					double vy = Double.parseDouble(args[8]);
					double vz = Double.parseDouble(args[9]);
					Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), new Runnable() {
						@Override
						public void run() {
							Location loc = new Location(Bukkit.getWorld("replayWorld"), x, y, z);
							Vector v = new Vector(vx,vy,vz);
							Item ent = loc.getWorld().dropItem(loc, itemstack);
							ent.setVelocity(new Vector(0,0,0));
							item_item.put(id, ent.getEntityId());
							ent.setVelocity(v);
						}
					});
				} else if(args[0].equalsIgnoreCase("itemPickup")) {
					int idr = Integer.parseInt(args[1]);
					if(item_item.containsKey(idr)) { 
						int id = item_item.get(idr);
						double vx = Double.parseDouble(args[2]);
						double vy = Double.parseDouble(args[3]);
						double vz = Double.parseDouble(args[4]);
						Vector v = new Vector(vx,vy,vz);
						for(Entity all : Bukkit.getWorld("replayWorld").getEntities()) {
							if(all.getType().equals(EntityType.DROPPED_ITEM)) {
								if(all.getEntityId() == id) {
									all.getLocation().getWorld().playSound(all.getLocation(), Sound.ITEM_PICKUP, 1, 1);
									all.setVelocity(v);
									Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable() {
										@Override
										public void run() {
											all.remove();
										}
									}, 2);
									item_item.remove(idr);
									break;
								}
							}
						}
					}
				} else if(args[0].equalsIgnoreCase("brMessage")) {
					String msg = ReplayAPI.getInstance().toRecordString(args[1].replace(";", "<_:>"));
					Bukkit.broadcastMessage(msg);
				}
			}
		});
	}
		
	public static void breakBlock(Block b) throws Throwable {
        for(Sound sound : Sound.values()) {
            Field f = CraftSound.class.getDeclaredField("sounds");
            f.setAccessible(true);
            
            String[] sounds = (String[]) f.get(null);
            Method getBlock = CraftBlock.class.getDeclaredMethod("getNMSBlock");
            getBlock.setAccessible(true);
            Object nmsBlock = getBlock.invoke(b);
            net.minecraft.server.v1_8_R3.Block block = (net.minecraft.server.v1_8_R3.Block) nmsBlock;
 
            if(block.stepSound.getBreakSound()
                    .equals(sounds[sound.ordinal()])) {
                b.getWorld().playSound(b.getLocation(), sound, 1, 1);
            }
        }
    }
	public static void placeBlock(Block b) throws Throwable {
        for(Sound sound : Sound.values()) {
            Field f = CraftSound.class.getDeclaredField("sounds");
            f.setAccessible(true);
            
            String[] sounds = (String[]) f.get(null);
            Method getBlock = CraftBlock.class.getDeclaredMethod("getNMSBlock");
            getBlock.setAccessible(true);
            Object nmsBlock = getBlock.invoke(b);
            net.minecraft.server.v1_8_R3.Block block = (net.minecraft.server.v1_8_R3.Block) nmsBlock;
 
            if(block.stepSound.getPlaceSound()
                    .equals(sounds[sound.ordinal()])) {
                b.getWorld().playSound(b.getLocation(), sound, 1, 1);
            }
        }
    }
	public static void playWalkSound(Block b) throws Throwable {
        for(Sound sound : Sound.values()) {
            Field f = CraftSound.class.getDeclaredField("sounds");
            f.setAccessible(true);
            
            String[] sounds = (String[]) f.get(null);
            Method getBlock = CraftBlock.class.getDeclaredMethod("getNMSBlock");
            getBlock.setAccessible(true);
            Object nmsBlock = getBlock.invoke(b);
            net.minecraft.server.v1_8_R3.Block block = (net.minecraft.server.v1_8_R3.Block) nmsBlock;
 
            if(block.stepSound.getStepSound()
                    .equals(sounds[sound.ordinal()])) {
                b.getWorld().playSound(b.getLocation(), sound, 1, 1);
            }
        }
    }
	
	
	public static ItemStack getItemStackFromString(String s) {
		String a = s.replace("{", "").replace("}", "");
		String[] b = a.split("<!=>");
		
		Material c = Material.getMaterial(b[0]);
		byte d = Byte.parseByte(b[1]);
		ItemStack is = new ItemStack(c, 1, d);
		if(b.length >= 3) {
			String[] e = b[2].split("-:-");
			ItemMeta meta = is.getItemMeta();		
			for(String f : e) {
				String[] g = f.replace("[", "").replace("]", "").split("&=&");
				if(g.length == 2) {
					Enchantment h = Enchantment.getByName(g[0]);
					int i = Integer.parseInt(g[1]);
					meta.addEnchant(h, i, true);
				}
			}
			is.setItemMeta(meta);
		}
		if(b.length >= 4 && is.getType().toString().contains("LEATHER_")) {
			String[] e = b[3].split(":-:");
			int red = Integer.parseInt(e[0]);
			int green = Integer.parseInt(e[1]);
			int blue = Integer.parseInt(e[2]);
			Color color = Color.fromBGR(blue, green, red);
			LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
			meta.setColor(color);
			is.setItemMeta(meta);
		}
		return is;
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
	
	@EventHandler
	public void onItem(ItemMergeEvent e) {
		if(at != 0) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(at > 0) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(at > 0) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		if(at > 0) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(at > 0) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(at > 0) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		if(at > 0) {
			e.setFoodLevel(20);
		}
	}
	
	public static void sendActionBar(Player p, String msg) {
		IChatBaseComponent ic = ChatSerializer.a("{\"text\":\"" + msg + "\"}");
		PacketPlayOutChat packet = new PacketPlayOutChat(ic, (byte)2);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	}
	
}
