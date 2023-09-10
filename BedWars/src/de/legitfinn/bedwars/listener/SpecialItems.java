package de.legitfinn.bedwars.listener;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import de.legitfinn.bedwars.main.BedWars;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.Countdown;
import de.legitfinn.bedwars.methoden.GameState;
import de.legitfinn.bedwars.methoden.MapData;
import de.legitfinn.bedwars.methoden.TeamManager;
import de.legitfinn.bedwars.shop.Shop;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class SpecialItems implements Listener {

	public static ArrayList<FallingBlock> exblocks = new ArrayList<FallingBlock>();
	public static HashMap<String, ArrayList<Block>> team_teamchest = new HashMap<String, ArrayList<Block>>();
	public static HashMap<String, Inventory> teamInvs = new HashMap<String, Inventory>();
	public static HashMap<Block, ArmorStand> teamchest_armorstand = new HashMap<Block, ArmorStand>();
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		if(!e.getEntity().getType().equals(EntityType.PRIMED_TNT)) {
			if(e.getEntity().getType().equals(EntityType.FIREBALL)) {
				if(e.getEntity().getCustomName().equalsIgnoreCase("TrollGranate")) {
					return;
				}
			}
			e.setCancelled(true);
		} else {
			for(Block all : e.blockList()) {
				if(DataManager.placedBlocks.contains(all)) {
					Vector v = all.getLocation().toVector().subtract(e.getEntity().getLocation().toVector()).normalize().multiply(1.0);
					FallingBlock fb = all.getLocation().getWorld().spawnFallingBlock(all.getLocation(), all.getType(), all.getData());
					all.setType(Material.AIR);
					fb.setVelocity(v);
					fb.setDropItem(false);
					exblocks.add(fb);
				}
			}
			e.blockList().clear();
		}
	}
	
	@EventHandler
	public void onLand(EntityChangeBlockEvent e) {
		if(e.getEntity() instanceof FallingBlock) {
			FallingBlock fb = (FallingBlock)e.getEntity();
			if(exblocks.contains(fb)) {
				e.setCancelled(true);
				exblocks.remove(fb);
			}
		}
	}
	
	public static HashMap<Player, Long> lastRettung = new HashMap<Player, Long>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
			if(DataManager.state.equals(GameState.Ingame)) {
				String team = TeamManager.getTeamOfPlayer(e.getPlayer());
				if(team != null) {
					if(e.getPlayer().getItemInHand().getType().equals(Material.BLAZE_ROD)) {
						e.setCancelled(true);
						if(lastRettung.containsKey(e.getPlayer())) {
							if(System.currentTimeMillis() - lastRettung.get(e.getPlayer()) < 3000) {
								e.getPlayer().sendMessage(DataManager.prefix + "§cDu kannst eine Rettungsplattform nur §4§oalle 3 Sekunden §cverwenden.");
								return;
							} else {
								lastRettung.put(e.getPlayer(), System.currentTimeMillis());
							}
						} else {
							lastRettung.put(e.getPlayer(), System.currentTimeMillis());
						}
						if(e.getPlayer().getItemInHand().getAmount() > 1) {
							e.getPlayer().getItemInHand().setAmount(e.getPlayer().getItemInHand().getAmount() - 1);
						} else {
							e.getPlayer().setItemInHand(null);
						}
						
						Block b = e.getPlayer().getLocation().clone().add(0, -5, 0).getBlock();
						Block b1 = b.getLocation().add(1, 0, 0).getBlock();
						Block b2 = b.getLocation().add(2, 0, 0).getBlock();
						Block b3 = b.getLocation().add(-1, 0, 0).getBlock();
						Block b4 = b.getLocation().add(-2, 0, 0).getBlock();
						Block b5 = b.getLocation().add(0, 0, 1).getBlock();
						Block b6 = b.getLocation().add(0, 0, 2).getBlock();
						Block b7 = b.getLocation().add(0, 0, -1).getBlock();
						Block b8 = b.getLocation().add(0, 0, -2).getBlock();
						Block b9 = b.getLocation().add(1, 0, 1).getBlock();
						Block b10 = b.getLocation().add(1, 0, -1).getBlock();
						Block b11 = b.getLocation().add(-1, 0, 1).getBlock();
						Block b12 = b.getLocation().add(-1, 0, -1).getBlock();
						byte c = TeamManager.ChatColorToColorByte(TeamManager.getTeamColor(team));
						b.setType(Material.STAINED_GLASS); b.setData(c);
						b1.setType(Material.STAINED_GLASS); b1.setData(c);
						b2.setType(Material.STAINED_GLASS); b2.setData(c);
						b3.setType(Material.STAINED_GLASS); b3.setData(c);
						b4.setType(Material.STAINED_GLASS); b4.setData(c);
						b5.setType(Material.STAINED_GLASS); b5.setData(c);
						b6.setType(Material.STAINED_GLASS); b6.setData(c);
						b7.setType(Material.STAINED_GLASS); b7.setData(c);
						b8.setType(Material.STAINED_GLASS); b8.setData(c);
						b9.setType(Material.STAINED_GLASS); b9.setData(c);
						b10.setType(Material.STAINED_GLASS); b10.setData(c);
						b11.setType(Material.STAINED_GLASS); b11.setData(c);
						b12.setType(Material.STAINED_GLASS); b12.setData(c);
						DataManager.placedBlocks.add(b1);
						DataManager.placedBlocks.add(b2);
						DataManager.placedBlocks.add(b3);
						DataManager.placedBlocks.add(b4);
						DataManager.placedBlocks.add(b5);
						DataManager.placedBlocks.add(b6);
						DataManager.placedBlocks.add(b7);
						DataManager.placedBlocks.add(b8);
						DataManager.placedBlocks.add(b9);
						DataManager.placedBlocks.add(b10);
						DataManager.placedBlocks.add(b11);
						DataManager.placedBlocks.add(b12);
						DataManager.placedBlocks.add(b);
					} else if(e.getPlayer().getItemInHand().getType().equals(Material.SULPHUR)) {
						if(!inBaseTeleport.containsKey(e.getPlayer())) {
							inBaseTeleport.put(e.getPlayer(), e.getPlayer().getInventory().first(e.getPlayer().getItemInHand()));
							teleportToBase(e.getPlayer(), e.getPlayer().getInventory().first(e.getPlayer().getItemInHand()));
						} else {
							inBaseTeleport.remove(e.getPlayer());
						}
					}
				}
			}
		}
	}
	
	public static HashMap<String, ArrayList<Block>> team_traps = new HashMap<String, ArrayList<Block>>();

	public static int yaw = 0;
	public static int cid;
	public static void startArmorStands() {
		cid = Bukkit.getScheduler().scheduleSyncRepeatingTask(BedWars.getPlugin(BedWars.class), new Runnable() {
			
			@Override
			public void run() {
				for(ArmorStand all : teamchest_armorstand.values()) {
					Location to = all.getLocation().clone();
					to.setPitch(0.0F);
					to.setYaw(yaw);
					all.teleport(to);
					BedWars.playParicle(all.getWorld().getPlayers(), EnumParticle.FLAME, all.getEyeLocation(), 0.2F, 0.2F, 0.2F, 0.01F, 2);
				}
				if(!DataManager.state.equals(GameState.Ingame)) {
					Bukkit.getScheduler().cancelTask(cid);
				}
				yaw += 5;
			}
		}, 0, 1);
	}
	
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(!DataManager.state.equals(GameState.Ingame)) {
			return;
		}
		if(e.getBlock().getType().equals(Material.TRIPWIRE)) {
			String team = TeamManager.getTeamOfPlayer(e.getPlayer());
			if(team != null) {
				if(!team_traps.containsKey(team)) {
					team_traps.put(team, new ArrayList<Block>());
				}
				team_traps.get(team).add(e.getBlock());			
			} else {
				e.setCancelled(true);
			}
		} else if(e.getBlock().getType().equals(Material.ENDER_CHEST)) {
			if(!TeamManager.isSpectator(e.getPlayer())) {
				String team = TeamManager.getTeamOfPlayer(e.getPlayer());
				if(!team_teamchest.containsKey(team)) {
					team_teamchest.put(team, new ArrayList<Block>());
				}
				team_teamchest.get(team).add(e.getBlock());
				ArmorStand as = e.getBlock().getLocation().getWorld().spawn(e.getBlock().getLocation().clone().add(0.5, 0.0, 0.5), ArmorStand.class);
				as.setVisible(false);
				as.setGravity(false);
				as.setSmall(true);
				as.setHelmet(new ItemStack(Material.STAINED_CLAY, 1, TeamManager.ChatColorToColorByte(TeamManager.getTeamColor(team))));
				teamchest_armorstand.put(e.getBlock(), as);
			}
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(!DataManager.state.equals(GameState.Ingame)) {
			return;
		}
		if(!TeamManager.isSpectator(e.getPlayer())) {
			String team = TeamManager.getTeamOfPlayer(e.getPlayer());
			if(e.getBlock().getType().equals(Material.ENDER_CHEST)) {
				String own = isTeamChest(e.getBlock());
				if(own != null) {
					if(own.equalsIgnoreCase(team)) {
						e.setCancelled(true);
						e.getBlock().setType(Material.AIR);
						Item i = e.getBlock().getLocation().getWorld().dropItem(e.getBlock().getLocation().clone().add(0.5, 0.5, 0.5), Shop.getItemStack(Material.ENDER_CHEST, 1, (short)0, "§aTeamkiste", null));
						i.setVelocity(new Vector(0, 0.2, 0));
						teamchest_armorstand.get(e.getBlock()).remove();
						teamchest_armorstand.remove(e.getBlock());
						team_teamchest.get(team).remove(e.getBlock());
					} else {
						e.setCancelled(true);
						Countdown.sendActionBar(e.getPlayer(), "§cDies ist keine Teamkiste von deinem Team.");
					}
				} else {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onArmorStand(PlayerArmorStandManipulateEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onOpen(PlayerInteractEvent e) {
		if(!DataManager.state.equals(GameState.Ingame)) {
			return;
		}
		if(TeamManager.isSpectator(e.getPlayer())) {
			return;
		}
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
				String t = isTeamChest(e.getClickedBlock());
				if(t != null) {
					String team = TeamManager.getTeamOfPlayer(e.getPlayer());
					if(t.equalsIgnoreCase(team)) {
						e.setCancelled(true);
						e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CHEST_OPEN, 1, 1);
						if(!teamInvs.containsKey(team)) {
							teamInvs.put(team, Bukkit.createInventory(null, 27, TeamManager.getTeamColor(team) + team + "'s §7Teamkiste"));
						}
						e.getPlayer().openInventory(teamInvs.get(team));
					} else {
						e.setCancelled(true);
						Countdown.sendActionBar(e.getPlayer(), "§cDies ist keine Teamkiste von deinem Team.");
					}
				}
			}
		}
	}
	
	public String isTeamChest(Block b) {
		if(!b.getType().equals(Material.ENDER_CHEST)) {
			return null;
		}
		for(String all : team_teamchest.keySet()) {
			if(team_teamchest.get(all).contains(b)) {
				return all;
			}
		}
		return null;
	}
	
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.getPlayer().getLocation().getBlock().getType().equals(Material.TRIPWIRE)) {
			String team = TeamManager.getTeamOfPlayer(e.getPlayer());
			if(team != null) {
				String owner = null;
				for(String all : team_traps.keySet()) {
					if(team_traps.get(all).contains(e.getPlayer().getLocation().getBlock())) {
						owner = all;
						break;
					}
				}
				if(owner !=null && !owner.equalsIgnoreCase(team)) {
					e.getPlayer().getLocation().getBlock().setType(Material.AIR);
					for(Player all : DataManager.team_players.get(owner)) {
						all.sendMessage(DataManager.prefix + "§cEine Falle von deinem Team wurde ausgelößt!");
						all.playSound(e.getPlayer().getLocation(), Sound.CREEPER_HISS, 1, 1);
					}
					e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10*20, 5));
					e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10*20, 5));
					e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10*20, 2));
					e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ANVIL_LAND, 1, 1);
				}
			}
		}
		if(inBaseTeleport.containsKey(e.getPlayer())) {
			if(e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
				e.getPlayer().getInventory().getItem(inBaseTeleport.get(e.getPlayer())).setType(Material.SULPHUR);
				inBaseTeleport.remove(e.getPlayer());
			}
		}
 	}
	
	@EventHandler
	public void onInv(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player)e.getWhoClicked();
			if(e.getClickedInventory() != null) {
				if(inBaseTeleport.containsKey(p)) {
					if(e.getCurrentItem().getType().equals(Material.GLOWSTONE_DUST)) {
						if(e.getCurrentItem().hasItemMeta()) {
							if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7» §cBaseteleporter")) {
								e.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onItemMove(InventoryMoveItemEvent e) {
		if(e.getSource().first(e.getItem()) != -1) {
			if(inBaseTeleport.containsKey((Player)e.getSource().getHolder())) {
				if(e.getItem().getType().equals(Material.GLOWSTONE_DUST)) {
					if(e.getItem().hasItemMeta()) {
						if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7» §cBaseteleporter")) {
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(inBaseTeleport.containsKey(e.getPlayer())) {
			if(e.getItemDrop().getItemStack().getType().equals(Material.GLOWSTONE_DUST)) {
				if(e.getItemDrop().getItemStack().hasItemMeta()) {
					if(e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("§7» §cBaseteleporter")) {
						e.setCancelled(true);
					}
				}
			}
		}
 	}
	
	public static HashMap<Player, Integer> inBaseTeleport = new HashMap<Player, Integer>();
	
	public static void teleportToBase(Player p, int slot) {
		String team = TeamManager.getTeamOfPlayer(p);
		if(team != null) {
			p.getInventory().setItem(slot, Shop.getItemStack(Material.GLOWSTONE_DUST, p.getInventory().getItem(slot).getAmount(), (short)0, p.getInventory().getItem(slot).getItemMeta().getDisplayName(), null));
			Thread th = new Thread(new Runnable() {
				
				@Override
				public void run() {
					double d = 5.0;
					Location base = MapData.getSpawn(team);
					while(d > 0 && inBaseTeleport.containsKey(p)) {
						float yaw = 0f;
						while(yaw < 360) {
							Location loc1 = p.getLocation().clone().add(0, 1.5, 0);
							loc1.setPitch(0.0f);
							loc1.setYaw(yaw);
							loc1.add(loc1.getDirection().multiply(1.0).normalize());
							BedWars.playParicle(loc1.getWorld().getPlayers(), EnumParticle.FIREWORKS_SPARK, loc1, 0.0F, 0.0F, 0.0F, 0.01F, 1);
							
							Location loc2 = base.clone().add(0, 1.5, 0);
							loc2.setPitch(0.0f);
							loc2.setYaw(yaw);
							loc2.add(loc2.getDirection().multiply(1.0).normalize());
							BedWars.playParicle(loc2.getWorld().getPlayers(), EnumParticle.FIREWORKS_SPARK, loc2, 0.0F, 0.0F, 0.0F, 0.01F, 1);
							
							yaw += 10;
						}
						Countdown.sendActionBar(p, "§eTeleportation in: §c§o" + new DecimalFormat("#.##").format(d) + " Sekunden");
						if(((int)d) == 5 || ((int)d) == 4 || ((int)d) == 3 || ((int)d) == 2 || ((int)d) == 1) {
							for(Player all : Bukkit.getOnlinePlayers()) {
								all.playSound(all.getLocation(), Sound.NOTE_BASS, 1.0F, 1.0F);
							}
						} else {
							p.playSound(p.getLocation(), Sound.NOTE_STICKS, 1.0F, 1.0F);
						}
						try{
							Thread.sleep(100);
						} catch(InterruptedException ex){}
						d -= 0.1;
					}
					if(inBaseTeleport.containsKey(p)) {
						p.teleport(base);
						p.getInventory().getItem(slot).setType(Material.SULPHUR);
						if(p.getInventory().getItem(slot).getAmount() == 1) {
							p.getInventory().setItem(slot, null);
						} else {
							p.getInventory().getItem(slot).setAmount(p.getInventory().getItem(slot).getAmount() - 1);
						}
						inBaseTeleport.remove(p);
					}
					return;
				}
			});
			th.start();
		}
	}
	
	
	
	
	
}
