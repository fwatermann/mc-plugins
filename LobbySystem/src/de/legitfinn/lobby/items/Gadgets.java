package de.legitfinn.lobby.items;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.legitfinn.lobby.SQL.CoinSQL;
import de.legitfinn.lobby.SQL.LobbyPetSQL;
import de.legitfinn.lobby.SQL.LobbySettingsSQL;
import de.legitfinn.lobby.SQL.LobbyShopSQL;
import de.legitfinn.lobby.SQL.ShopSQL;
import de.legitfinn.lobby.functions.AnvilContainer;
import de.legitfinn.lobby.functions.ParticleAnimations;
import de.legitfinn.lobby.functions.Pet;
import de.legitfinn.lobby.functions.PetListener;
import de.legitfinn.lobby.main.main;
import de.legitfinn.lobby.scoreboard.ScoreboardSystem;
import de.legitfinn.ppermissions.main.PPermissions;

public class Gadgets extends ParticleAnimations implements Listener  {
	
	public static ArrayList<Player> pay = new ArrayList<Player>();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getPlayer().getItemInHand().getType().equals(Material.CHEST)) {
				if(!main.build.contains(e.getPlayer())) {
					openGadgets(e.getPlayer());
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null) {
			if(e.getCurrentItem() != null) {
				if(e.getClickedInventory().getTitle().equalsIgnoreCase("§7» §bGadgets")) {
					e.setCancelled(true);
					int slot = e.getRawSlot();
					Player p = (Player)e.getWhoClicked();
					if(slot == 11) {
						openParticles(p);
					} else if(slot == 13) {
						openHeads(p);
					} else if(slot == 15) {
						openPets(p);
					} else if(slot == 22){
						openShopPayment(p);
					}
				} else if(e.getClickedInventory().getTitle().equalsIgnoreCase("§bGadgets §7» §bBoots")) {
					e.setCancelled(true);
					int slot = e.getRawSlot();
					Player p = (Player)e.getWhoClicked();
					if(slot == 11) {
						if(!LobbyShopSQL.hasBought("BOOTS", p.getUniqueId(), 1)) {
							if(CoinSQL.getCoins(p.getUniqueId()) >= 1500) {
								buyBoots(e.getCurrentItem(), 1, 1500, p);
							}
							p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							return;
						}
						p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
						if(player_particle.containsKey(p)) {
							if(player_particle.get(p).equalsIgnoreCase("flame")) {
								player_particle.remove(p);
								p.getInventory().setBoots(null);
							} else {
								player_particle.remove(p);
								player_particle.put(p, "flame");
								p.getInventory().setBoots(e.getCurrentItem());
								System.out.println("activ 1");
							}
						} else {
							player_particle.remove(p);
							player_particle.put(p, "flame");
							p.getInventory().setBoots(e.getCurrentItem());
							System.out.println("activ 2");
						}
					} else if(slot == 12) {
						if(!LobbyShopSQL.hasBought("BOOTS", p.getUniqueId(), 2)) {
							if(CoinSQL.getCoins(p.getUniqueId()) >= 1500) {
								buyBoots(e.getCurrentItem(), 2, 1500, p);
							}
							p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							return;
						}
						p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
						if(player_particle.containsKey(p)) {
							if(player_particle.get(p).equalsIgnoreCase("magic")) {
								player_particle.remove(p);
								p.getInventory().setBoots(null);
							} else {
								player_particle.remove(p);
								player_particle.put(p, "magic");
								p.getInventory().setBoots(e.getCurrentItem());
							}
						} else {
							player_particle.remove(p);
							player_particle.put(p, "magic");
							p.getInventory().setBoots(e.getCurrentItem());
						}
					} else if(slot == 13) {
						if(!LobbyShopSQL.hasBought("BOOTS", p.getUniqueId(), 3)) {
							if(CoinSQL.getCoins(p.getUniqueId()) >= 1500) {
								buyBoots(e.getCurrentItem(), 3, 1500, p);
							}
							p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							return;
						}
						p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
						if(player_particle.containsKey(p)) {
							if(player_particle.get(p).equalsIgnoreCase("emerald")) {
								player_particle.remove(p);
								p.getInventory().setBoots(null);
							} else {
								player_particle.remove(p);
								player_particle.put(p, "emerald");
								p.getInventory().setBoots(e.getCurrentItem());
							}
						} else {
							player_particle.remove(p);
							player_particle.put(p, "emerald");
							p.getInventory().setBoots(e.getCurrentItem());
						}
					} else if(slot == 14) {
						if(!LobbyShopSQL.hasBought("BOOTS", p.getUniqueId(), 4)) {
							if(CoinSQL.getCoins(p.getUniqueId()) >= 1500) {
								buyBoots(e.getCurrentItem(), 4, 1500, p);
							}
							p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							return;
						}
						p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
						if(player_particle.containsKey(p)) {
							if(player_particle.get(p).equalsIgnoreCase("rain")) {
								player_particle.remove(p);
								p.getInventory().setBoots(null);
							} else {
								player_particle.remove(p);
								player_particle.put(p, "rain");
								p.getInventory().setBoots(e.getCurrentItem());
							}
						} else {
							player_particle.remove(p);
							player_particle.put(p, "rain");
							p.getInventory().setBoots(e.getCurrentItem());
						}
					} else if(slot == 15) {
						if(!LobbyShopSQL.hasBought("BOOTS", p.getUniqueId(), 5)) {
							if(CoinSQL.getCoins(p.getUniqueId()) >= 1500) {
								buyBoots(e.getCurrentItem(), 5, 1500, p);
							}
							p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							return;
						}
						p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
						if(player_particle.containsKey(p)) {
							if(player_particle.get(p).equalsIgnoreCase("redstone")) {
								player_particle.remove(p);
								p.getInventory().setBoots(null);
							} else {
								player_particle.remove(p);
								player_particle.put(p, "redstone");
								p.getInventory().setBoots(e.getCurrentItem());
							}
						} else {
							player_particle.remove(p);
							player_particle.put(p, "redstone");
							p.getInventory().setBoots(e.getCurrentItem());
						}
					}
				} else if(e.getClickedInventory().getTitle().equalsIgnoreCase("§bGadgets §7» §bHeads")) {
					Player p = (Player)e.getWhoClicked();
					e.setCancelled(true);
					if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
						ItemStack is = e.getCurrentItem();
						SkullMeta m = (SkullMeta) is.getItemMeta();
						String owner = m.getOwner();
						if(owner.equalsIgnoreCase("Gronkh")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 1)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("Gronkh", 1, 1000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("GermanLetsPlay")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 2)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("GermanLetsPlay", 2, 2000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("Minusduude")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 3)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("Minusduude", 3, 1000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("HerrBergmann")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 4)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("HerrBergmann", 4, 3000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("izzi")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 5)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("izzi", 5, 2000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("rewinside")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 6)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("rewinside", 6, 2000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("paluten")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 7)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("paluten", 7, 4000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("Sturmwaffelhd")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 8)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("SturmwaffelHD", 8, 4000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("gommehd")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 9)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("GommeHD", 9, 3000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("doctorbenx")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 10)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("doctorbenx", 10, 3000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("dnertv")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 11)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("dnertv", 11, 5000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("sparkofphoenix")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 12)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("sparkofphoenix", 12, 4000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("reved")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 13)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("reved", 13, 2000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("derkev")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 14)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("derkev", 14, 2000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("mcexpertde")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 15)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("mcexpertde", 15, 3000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("ungespielt")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 16)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("ungespielt", 16, 5000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("cuzimsara")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 17)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("cuzimsara", 17, 1000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("bastighg")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 18)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("bastighg", 18, 2000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("Venicraft")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 19)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("Venicraft", 19, 2000, p);
								}
							}
						} else if(owner.equalsIgnoreCase("fr3akzlp")) {
							if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 20)) {
								p.getInventory().setHelmet(is);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							} else {
								p.playSound(p.getLocation(), Sound.FIZZ, 1F, 2F);
								if(CoinSQL.getCoins(p.getUniqueId()) >= 1000) {
									buyHead("fr3akzlp", 20, 1000, p);
								}
							}
						}
					} else if(e.getCurrentItem().getType().equals(Material.BARRIER)) {
						p.getInventory().setHelmet(null);
						p.playSound(p.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
					}
				} else if(e.getClickedInventory().getTitle().equalsIgnoreCase("§bGadgets §7» §bPets")) {
					e.setCancelled(true);
					Player p = (Player)e.getWhoClicked();
					int slot = e.getRawSlot();
					if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
						if(slot == 4){
							if(!p.hasPermission("system.premium")) {
								p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							}else {
								spawnPet(p, EntityType.PIG_ZOMBIE, 1, null);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
							}
						}else if(slot == 11) {
							if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 1)) {
								if(CoinSQL.getCoins(p.getUniqueId()) >= 2500) {
									buyPet(e.getCurrentItem(), 1, 2500, p);
								}
								p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							} else {
								spawnPet(p, EntityType.COW, 1, null);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
							}
						} else if(slot == 12) {
							if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 2)) {
								if(CoinSQL.getCoins(p.getUniqueId()) >= 2500) {
									buyPet(e.getCurrentItem(), 2, 2500, p);
								}
								p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							} else {
								spawnPet(p, EntityType.SHEEP, 1, null);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
							}
						} else if(slot == 13) {
							if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 3)) {
								if(CoinSQL.getCoins(p.getUniqueId()) >= 2500) {
									buyPet(e.getCurrentItem(), 3, 2500, p);
								}
								p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							} else {
								spawnPet(p, EntityType.HORSE, 1, null);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
							}
						} else if(slot == 14) {
							if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 4)) {
								if(CoinSQL.getCoins(p.getUniqueId()) >= 2500) {
									buyPet(e.getCurrentItem(), 4, 2500, p);
								}
								p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							} else {
								spawnPet(p, EntityType.MUSHROOM_COW, 1, null);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
							}
						} else if(slot == 15) {
							if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 5)) {
								if(CoinSQL.getCoins(p.getUniqueId()) >= 2500) {
									buyPet(e.getCurrentItem(), 5, 2500, p);
								}
								p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							} else {
								spawnPet(p, EntityType.OCELOT, 1, null);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
							}
						} else if(slot == 20) {
							if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 6)) {
								if(CoinSQL.getCoins(p.getUniqueId()) >= 2500) {
									buyPet(e.getCurrentItem(), 6, 2500, p);
								}
								p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							} else {
								spawnPet(p, EntityType.WOLF, 1, null);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
							}
						} else if(slot == 21) {
							if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 7)) {
								if(CoinSQL.getCoins(p.getUniqueId()) >= 2500) {
									buyPet(e.getCurrentItem(), 7, 2500, p);
								}
								p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							} else {
								spawnPet(p, EntityType.PIG, 1, null);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
							}
						} else if(slot == 22) {
							if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 8)) {
								if(CoinSQL.getCoins(p.getUniqueId()) >= 2500) {
									buyPet(e.getCurrentItem(), 8, 2500, p);
								}
								p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							} else {
								spawnPet(p, EntityType.RABBIT, 1, null);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
							}
						} else if(slot == 23) {
							if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 9)) {
								if(CoinSQL.getCoins(p.getUniqueId()) >= 2500) {
									buyPet(e.getCurrentItem(), 9, 2500, p);
								}
								p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							} else {
								spawnPet(p, EntityType.CHICKEN, 1, null);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
							}
						} else if(slot == 24) {
							if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 10)) {
								if(CoinSQL.getCoins(p.getUniqueId()) >= 2500) {
									buyPet(e.getCurrentItem(), 10, 2500, p);
								}
								p.playSound(p.getLocation(), Sound.FIZZ, 1.0F, 1.0F);
							} else {
								spawnPet(p, EntityType.SLIME, 1, null);
								p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
							}
						}
					}
					
				} else if(e.getClickedInventory().getTitle().equalsIgnoreCase("§bHeads §7» §bKaufen")) {
					e.setCancelled(true);
					Player p = (Player)e.getWhoClicked();
					
					int id = 0;
					int price = 0;
					ItemMeta meta = e.getClickedInventory().getItem(13).getItemMeta();
					id  = meta.getEnchantLevel(Enchantment.DAMAGE_ALL);
					price = meta.getEnchantLevel(Enchantment.DURABILITY);
					
					if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)) {
						if(e.getCurrentItem().getData().getData() == (byte)5) {
							CoinSQL.removeCoins(p.getUniqueId(), price);
							LobbyShopSQL.buyItem("HEADS", p.getUniqueId(), id);
							p.playSound(p.getLocation(), Sound.ANVIL_USE, 1F, 1F);
							ScoreboardSystem.updateScoreboard(p, false, false);
							openHeads(p);
						} else {
							openHeads(p);
						}
					}
				} else if(e.getClickedInventory().getTitle().equalsIgnoreCase("§bBoots §7» §bKaufen")) {
					e.setCancelled(true);
					Player p = (Player)e.getWhoClicked();
					
					int id = 0;
					int price = 0;
					ItemMeta meta = e.getClickedInventory().getItem(13).getItemMeta();
					id  = meta.getEnchantLevel(Enchantment.DAMAGE_ALL);
					price = meta.getEnchantLevel(Enchantment.DURABILITY);
					
					if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)) {
						if(e.getCurrentItem().getData().getData() == (byte)5) {
							CoinSQL.removeCoins(p.getUniqueId(), price);
							LobbyShopSQL.buyItem("BOOTS", p.getUniqueId(), id);
							p.playSound(p.getLocation(), Sound.ANVIL_USE, 1F, 1F);
							ScoreboardSystem.updateScoreboard(p, false, false);
							openParticles(p);
						} else {
							openParticles(p);
						}
					}
				} else if(e.getClickedInventory().getTitle().equalsIgnoreCase("§bPets §7» §bKaufen")) {
					e.setCancelled(true);
					Player p = (Player)e.getWhoClicked();
					
					int id = 0;
					int price = 0;
					ItemMeta meta = e.getClickedInventory().getItem(13).getItemMeta();
					id  = meta.getEnchantLevel(Enchantment.DAMAGE_ALL);
					price = meta.getEnchantLevel(Enchantment.DURABILITY);
					
					if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)) {
						if(e.getCurrentItem().getData().getData() == (byte)5) {
							CoinSQL.removeCoins(p.getUniqueId(), price);
							LobbyShopSQL.buyItem("PETS", p.getUniqueId(), id);
							p.playSound(p.getLocation(), Sound.ANVIL_USE, 1F, 1F);
							ScoreboardSystem.updateScoreboard(p, false, false);
							openPets(p);
						} else {
							openPets(p);
						}
					}
				}else if(e.getClickedInventory().getTitle().equalsIgnoreCase("§bShop §7» §bZahlungsart")){
					Player p = (Player)e.getWhoClicked();
					if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)){
						p.sendMessage("§4§lShop §7» §aFühre dein Einkauf hier fort: §n§6http://pvpfunshop.buycraft.net/");
					}else if(e.getCurrentItem().getType().equals(Material.PAPER)){
						openShopCat(p);
					}
					e.setCancelled(true);
				}else if(e.getClickedInventory().getTitle().equalsIgnoreCase("§bShop §7» §bKategorien")){
					Player p = (Player)e.getWhoClicked();
					if(e.getCurrentItem().getType().equals(Material.GOLDEN_APPLE)){
						openShopPrem(p);
					}else if(e.getCurrentItem().getType().equals(Material.GOLD_INGOT)){
						openShopDon(p);
					}
					e.setCancelled(true);
				}else if(e.getClickedInventory().getType().equals(InventoryType.ANVIL)){
					Player p = (Player)e.getWhoClicked();
					if(!PetListener.namingpet.contains(p) && !pay.contains(p) && !ShopAdmin.filter.contains(p)){
						if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName() != null) {
							String amount = e.getCurrentItem().getItemMeta().getDisplayName();
							amount = amount.replace(",", ".");
							try{
								double amou = Double.parseDouble(amount);
								String num = new DecimalFormat("#.##").format(amou);
								amou = Double.parseDouble(num);
								if(amou == 0 || amou > 10000){
									p.sendMessage("§4§lShop §7» §cBitte gebe eine Zahl zwischen 0 und 10000 mit maximal zwei Nachkommastellen ein");
									return;
								}
								openShopBuy(p, Material.GOLD_INGOT, "Spende", amou + "");
							}catch(NumberFormatException ex){
								p.sendMessage("§4§lShop §7» §cBitte gebe eine Zahl zwischen 0 und 10000 mit maximal zwei Nachkommastellen ein");
								return;
							}
						}
					}else if(!PetListener.namingpet.contains(p) && pay.contains(p) && !ShopAdmin.filter.contains(p)){
						if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName() != null) {
							String c = e.getCurrentItem().getItemMeta().getDisplayName();
							c = c.replace("-", "");
							if(c.length() < 16 || c.length() > 16 || c.contains(".")){
								p.sendMessage("§4§lShop §7» §cBitte gebe den 16-stelligen Code deiner PaySafeCard ein.");
							}else {
								pay.remove(p);
								String type = e.getCurrentItem().getItemMeta().getLore().get(0);
								String price = e.getCurrentItem().getItemMeta().getLore().get(1);
								type = ChatColor.stripColor(type);
								price = ChatColor.stripColor(price);
								String tt = type;
								type = type.split(" ")[2];
								price = price.split(" ")[2];
								for(int i = 3; i < tt.split(" ").length; i++){
									type = type + " " + tt.split(" ")[i];
								}
								/*
								 * lore.add("§7» Typ: §c" + type);
								 * lore.add("§7» Kosten: §c" + price);
								 */
								shopconfirm(e.getCurrentItem().getType(), type, Double.parseDouble(price), p, c);
							}
						}
					}
					e.setCancelled(true);
				}else if(e.getClickedInventory().getTitle().equalsIgnoreCase("§bShop §7» §bPremium")){
					Player p = (Player)e.getWhoClicked();
					String cost = e.getCurrentItem().getItemMeta().getLore().get(0);
					cost = ChatColor.stripColor(cost);
					cost = cost.split(" ")[2];
					openShopBuy(p, Material.GOLDEN_APPLE, ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()), cost);
					e.setCancelled(true);
				}else if(e.getClickedInventory().getTitle().equalsIgnoreCase("§bShop §7» §bBestätigung")){
					e.setCancelled(true);
					Player p = (Player)e.getWhoClicked();
					Date d = new Date(System.currentTimeMillis());
					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
					String date = sdf.format(d);
					ShopSQL.addPayment(p.getUniqueId(), e.getClickedInventory().getItem(13).getItemMeta().getDisplayName(), e.getClickedInventory().getItem(22).getItemMeta().getDisplayName().replace("§aKosten: ", ""), e.getClickedInventory().getItem(22).getItemMeta().getLore().get(0).replace("§7» §cPaySafeCard-Code: §6", ""), date);
					p.closeInventory();
				}
			}
		}
	}
	
	public static void buyHead(String owner, int id, int price, Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, "§bHeads §7» §bKaufen");
		
		ItemStack yes = new ItemStack(Material.STAINED_CLAY, 1, (short)5);
		ItemMeta ym = yes.getItemMeta();
		ym.setDisplayName("§aBESTÄTIGEN");
		yes.setItemMeta(ym);
		
		ItemStack no = new ItemStack(Material.STAINED_CLAY, 1, (short)14);
		ItemMeta nm = no.getItemMeta();
		nm.setDisplayName("§cABBRECHEN");
		no.setItemMeta(nm);
		
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta im = (SkullMeta) item.getItemMeta();
		im.setDisplayName("§5" + owner);
		im.setOwner(owner);
		im.addEnchant(Enchantment.DAMAGE_ALL, id, true);
		im.addEnchant(Enchantment.DURABILITY, price, true);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(im);
		
		ItemStack priceis = new ItemStack(Material.GOLD_INGOT);
		ItemMeta pm = priceis.getItemMeta();
		pm.setDisplayName("§aPreis: §6" + price + " Coins");
		priceis.setItemMeta(pm);
		
		inv.setItem(10, yes);
		inv.setItem(11, yes);
		inv.setItem(13, item);
		inv.setItem(15, no);
		inv.setItem(16, no);
		inv.setItem(19, yes);
		inv.setItem(20, yes);
		inv.setItem(22, priceis);
		inv.setItem(24, no);
		inv.setItem(25, no);
		
		p.openInventory(inv);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				p.updateInventory();
			}
		}, 2);
	}

	public static void buyBoots(ItemStack is, int id, int price, Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, "§bBoots §7» §bKaufen");
		
		ItemStack yes = new ItemStack(Material.STAINED_CLAY, 1, (short)5);
		ItemMeta ym = yes.getItemMeta();
		ym.setDisplayName("§aBESTÄTIGEN");
		yes.setItemMeta(ym);
		
		ItemStack no = new ItemStack(Material.STAINED_CLAY, 1, (short)14);
		ItemMeta nm = no.getItemMeta();
		nm.setDisplayName("§cABBRECHEN");
		no.setItemMeta(nm);
		
		ItemMeta im = is.getItemMeta();
		im.addEnchant(Enchantment.DAMAGE_ALL, id, true);
		im.addEnchant(Enchantment.DURABILITY, price, true);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		if(is.getType().equals(Material.POTION)) {
			im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		}
		is.setItemMeta(im);
		
		ItemStack priceis = new ItemStack(Material.GOLD_INGOT);
		ItemMeta pm = priceis.getItemMeta();
		pm.setDisplayName("§aPreis: §6" + price + " Coins");
		priceis.setItemMeta(pm);
		
		inv.setItem(10, yes);
		inv.setItem(11, yes);
		inv.setItem(13, is);
		inv.setItem(15, no);
		inv.setItem(16, no);
		inv.setItem(19, yes);
		inv.setItem(20, yes);
		inv.setItem(22, priceis);
		inv.setItem(24, no);
		inv.setItem(25, no);
		
		p.openInventory(inv);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				p.updateInventory();
			}
		}, 2);
	}
	
	public static void buyPet(ItemStack is, int id, int price, Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, "§bPets §7» §bKaufen");
		
		ItemStack yes = new ItemStack(Material.STAINED_CLAY, 1, (short)5);
		ItemMeta ym = yes.getItemMeta();
		ym.setDisplayName("§aBESTÄTIGEN");
		yes.setItemMeta(ym);
		
		ItemStack no = new ItemStack(Material.STAINED_CLAY, 1, (short)14);
		ItemMeta nm = no.getItemMeta();
		nm.setDisplayName("§cABBRECHEN");
		no.setItemMeta(nm);
		
		ItemMeta im = is.getItemMeta();
		im.addEnchant(Enchantment.DAMAGE_ALL, id, true);
		im.addEnchant(Enchantment.DURABILITY, price, true);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		if(is.getType().equals(Material.POTION)) {
			im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		}
		is.setItemMeta(im);
		
		ItemStack priceis = new ItemStack(Material.GOLD_INGOT);
		ItemMeta pm = priceis.getItemMeta();
		pm.setDisplayName("§aPreis: §6" + price + " Coins");
		priceis.setItemMeta(pm);
		
		inv.setItem(10, yes);
		inv.setItem(11, yes);
		inv.setItem(13, is);
		inv.setItem(15, no);
		inv.setItem(16, no);
		inv.setItem(19, yes);
		inv.setItem(20, yes);
		inv.setItem(22, priceis);
		inv.setItem(24, no);
		inv.setItem(25, no);
		
		p.openInventory(inv);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				p.updateInventory();
			}
		}, 2);
	}
	
	
	
	public static void openGadgets(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, "§7» §bGadgets");
		
		ItemStack particles = new ItemStack(Material.BLAZE_POWDER);
		ItemMeta pam = particles.getItemMeta();
		pam.setDisplayName("§7» §cBoots");
		particles.setItemMeta(pam);
		
		ItemStack pets = new ItemStack(Material.MONSTER_EGG);
		ItemMeta pem = pets.getItemMeta();
		pem.setDisplayName("§7» §cHaustiere");
		pets.setItemMeta(pem);
		
		ItemStack heads = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta hem = (SkullMeta)heads.getItemMeta();
		hem.setDisplayName("§7» §cKöpfe");
		hem.setOwner(p.getName());
		heads.setItemMeta(hem);
		
		ItemStack shop = new ItemStack(Material.DIAMOND);
		ItemMeta shopm = shop.getItemMeta();
		shopm.setDisplayName("§7» §6Premium/Spenden");
		List<String> lore = new ArrayList<String>();
		String[] groups = de.legitfinn.ppermissions.mysql.SQL.getGroupListOfPlayer(p.getUniqueId());
		String expire = null;
		for(String all : groups) {
			if(de.legitfinn.ppermissions.mysql.SQL.getGroupInformation(all, "name").equalsIgnoreCase("premium")) {
				expire = de.legitfinn.ppermissions.mysql.SQL.getGroupInformation(all, "expire");
			}
		}
		if(PPermissions.isInGroup(p, "Premium")){
			lore.add("§7» Du besitzt Premium.");
			if(expire == null){
				lore.add("§7» Dein Premium ist §o§cpermanent§r§7.");
			}else {
				lore.add("§7» Dein Premium läuft ab am: §o§c" + expire.split(" ")[0] + " §r§7um §o§c" + expire.split(" ")[1] + "Uhr§r§7.");
			}
			lore.add("§7» Klicke auf das Item, um für den Server zu spenden.");
		} else {
			lore.add("§7» Du besitzt kein Premium.");
			lore.add("§7» Klicke auf das Item, um Premium zu erwerben");
			lore.add("§7» oder für den Server zu spenden.");
		}
		if(ShopSQL.isWaiting(p.getUniqueId())){
			if(ShopSQL.waitingamount(p.getUniqueId()) > 1){
				lore.add("§7» Deine offenen Bestellungen werden §n§cbearbeitet§r§7.");
			}else {
				lore.add("§7» Deine offene Bestellung wird §n§cbearbeitet§r§7.");
			}
		}else {
			lore.add("§7» Es sind keine offene Bestellung vorhanden.");
		}
		shopm.setLore(lore);
		shop.setItemMeta(shopm);
		
		inv.setItem(11, particles);
		inv.setItem(13, heads);
		inv.setItem(15, pets);
		inv.setItem(22, shop);

		p.openInventory(inv);	
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				p.updateInventory();
			}
		}, 2);
	}
	
	//Zahlungsart
	public static void openShopPayment(Player p){
		Inventory inv = Bukkit.createInventory(null, 9*3, "§bShop §7» §bZahlungsart");
		
		ItemStack prem = new ItemStack(Material.STAINED_CLAY, 1, (short) 11);
		ItemMeta premm = prem.getItemMeta();
		premm.setDisplayName("§7» §9PayPal");
		prem.setItemMeta(premm);
		
		ItemStack don = new ItemStack(Material.PAPER);
		ItemMeta donm = don.getItemMeta();
		donm.setDisplayName("§7» §9PaySafeCard");
		don.setItemMeta(donm);
		
		inv.setItem(11, prem);
		inv.setItem(15, don);
		
		p.openInventory(inv);
	}
	
	public static void shopconfirm(Material m, String type, double price, Player p, String code) {
		Inventory inv = Bukkit.createInventory(null, 36, "§bShop §7» §bBestätigung");
		
		ItemStack yes = new ItemStack(Material.STAINED_CLAY, 1, (short)5);
		ItemMeta ym = yes.getItemMeta();
		ym.setDisplayName("§aBESTÄTIGEN");
		yes.setItemMeta(ym);
		
		ItemStack no = new ItemStack(Material.STAINED_CLAY, 1, (short)14);
		ItemMeta nm = no.getItemMeta();
		nm.setDisplayName("§cABBRECHEN");
		no.setItemMeta(nm);
		
		ItemStack what = new ItemStack(m);
		if(type.contains("Life")){
			what = new ItemStack(m, 64, (short) 1);
		}else if(type.contains("7 Tage")){
			what.setAmount(7);
		}else if(type.contains("30 Tage")){
			what.setAmount(30);
		}
		ItemMeta meta = what.getItemMeta();
		meta.setDisplayName("§7» §c" + type);
		what.setItemMeta(meta);
		ItemStack priceis = new ItemStack(Material.GOLD_INGOT);
		ItemMeta pm = priceis.getItemMeta();
		pm.setDisplayName("§aKosten: §6" + price + " Euro");
		List<String> lore = new ArrayList<>();
		lore.add("§7» §cPaySafeCard-Code: §6" + code);
		pm.setLore(lore);
		priceis.setItemMeta(pm);
		
		inv.setItem(10, yes);
		inv.setItem(11, yes);
		inv.setItem(13, what);
		inv.setItem(15, no);
		inv.setItem(16, no);
		inv.setItem(19, yes);
		inv.setItem(20, yes);
		inv.setItem(22, priceis);
		inv.setItem(24, no);
		inv.setItem(25, no);
		
		p.openInventory(inv);
		
	}
	
	public static void openShopCat(Player p){
		Inventory inv = Bukkit.createInventory(null, 9*3, "§bShop §7» §bKategorien");
		
		ItemStack prem = new ItemStack(Material.GOLDEN_APPLE);
		ItemMeta premm = prem.getItemMeta();
		premm.setDisplayName("§7» §cPremium");
		prem.setItemMeta(premm);
		
		ItemStack don = new ItemStack(Material.GOLD_INGOT);
		ItemMeta donm = don.getItemMeta();
		donm.setDisplayName("§7» §cSpenden");
		don.setItemMeta(donm);
		
		inv.setItem(11, prem);
		inv.setItem(15, don);
		
		p.openInventory(inv);
	}
	
	public static void openShopPrem(Player p){
		Inventory inv = Bukkit.createInventory(null, 9*3, "§bShop §7» §bPremium");
		
		List<String> lore = new ArrayList<>();
		
		ItemStack prem = new ItemStack(Material.GOLDEN_APPLE);
		ItemMeta premm = prem.getItemMeta();
		premm.setDisplayName("§cPremium §7- §c7 Tage");
		lore.add("§7» Kosten: §c2 Euro");
		premm.setLore(lore);
		lore.clear();
		prem.setItemMeta(premm);
		prem.setAmount(7);
		
		ItemStack prem30 = new ItemStack(Material.GOLDEN_APPLE);
		ItemMeta premm30 = prem30.getItemMeta();
		premm30.setDisplayName("§cPremium §7- §c30 Tage");
		lore.add("§7» Kosten: §c5 Euro");
		premm30.setLore(lore);
		lore.clear();
		prem30.setItemMeta(premm30);
		prem30.setAmount(30);
		
		ItemStack premp = new ItemStack(Material.GOLDEN_APPLE, 64, (short) 1);
		ItemMeta premmp = premp.getItemMeta();
		premmp.setDisplayName("§cPremium §7- §cLifetime");
		lore.add("§7» Kosten: §c20 Euro");
		premmp.setLore(lore);
		lore.clear();
		premp.setItemMeta(premmp);
		
		inv.setItem(11, prem);
		inv.setItem(13, prem30);
		inv.setItem(15, premp);
		
		p.openInventory(inv);
	}
	
	public static void openShopDon(Player p){
		EntityPlayer cp = ((CraftPlayer)p).getHandle();
		AnvilContainer con = new AnvilContainer(cp);
		int c = cp.nextContainerCounter();
		cp.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing", new Object[0]), 0));
		cp.activeContainer = con;
		cp.activeContainer.windowId = c;
		cp.activeContainer.addSlotListener(cp);
		
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta m = item.getItemMeta();
		m.setDisplayName("Betrag eingeben...");
		item.setItemMeta(m);
		
		p.getOpenInventory().getTopInventory().addItem(item);
	}
	
	public static void openShopBuy(Player p, Material mat, String type, String price){
		EntityPlayer cp = ((CraftPlayer)p).getHandle();
		AnvilContainer con = new AnvilContainer(cp);
		int c = cp.nextContainerCounter();
		cp.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing", new Object[0]), 0));
		cp.activeContainer = con;
		cp.activeContainer.windowId = c;
		cp.activeContainer.addSlotListener(cp);
		
		ItemStack item = new ItemStack(mat);
		if(type.contains("Life")){
			item = new ItemStack(mat, 64, (short) 1);
		}
		ItemMeta m = item.getItemMeta();
		m.setDisplayName("Code eingeben...");
		List<String> lore = new ArrayList<>();
		lore.add("§7» Typ: §c" + type);
		lore.add("§7» Kosten: §c" + price + " Euro");
		m.setLore(lore);
		item.setItemMeta(m);
		if(type.contains("7 Tage")){
			item.setAmount(7);
		}else if(type.contains("30 Tage")){
			item.setAmount(30);
		}
		
		p.getOpenInventory().getTopInventory().addItem(item);
		pay.add(p);
	}
	
	public static void openParticles(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, "§bGadgets §7» §bBoots");


		List<String> buy1 = new ArrayList<String>();
		List<String> has = new ArrayList<String>();
		buy1.add("§7» §9Kaufen für §61.500 Coins");
		has.add("§7» §aGekauft");
		
		
		ItemStack flame = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta fm = (LeatherArmorMeta) flame.getItemMeta();
		fm.setDisplayName("§6§lFlammenboots");
		fm.setColor(Color.fromRGB(244, 173, 66));
		fm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		if(LobbyShopSQL.hasBought("BOOTS", p.getUniqueId(), 1)) {
			fm.setLore(has);
		} else {
			fm.setLore(buy1);
		}
		flame.setItemMeta(fm);
		
		ItemStack magic = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta mm = (LeatherArmorMeta) magic.getItemMeta();
		mm.setDisplayName("§5§lMagieboots");
		mm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		mm.setColor(Color.fromRGB(178, 35, 176));
		if(LobbyShopSQL.hasBought("BOOTS", p.getUniqueId(), 2)) {
			mm.setLore(has);
		} else {
			mm.setLore(buy1);
		}
		magic.setItemMeta(mm);
		
		ItemStack emerald = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta em = (LeatherArmorMeta) emerald.getItemMeta();
		em.setDisplayName("§a§lGrüne Boots");
		em.setColor(Color.fromRGB(60, 204, 44));
		em.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		if(LobbyShopSQL.hasBought("BOOTS", p.getUniqueId(), 3)) {
			em.setLore(has);
		} else {
			em.setLore(buy1);
		}
		emerald.setItemMeta(em);
		
		ItemStack rain = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta rm = (LeatherArmorMeta) rain.getItemMeta();
		rm.setDisplayName("§9§lRegenboots");
		rm.setColor(Color.fromRGB(43, 137, 204));
		rm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		if(LobbyShopSQL.hasBought("BOOTS", p.getUniqueId(), 4)) {
			rm.setLore(has);
		} else {
			rm.setLore(buy1);
		}
		rain.setItemMeta(rm);
		
		ItemStack redstone = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta rem = (LeatherArmorMeta) redstone.getItemMeta();
		rem.setDisplayName("§c§lRote Boots");
		rem.setColor(Color.fromRGB(163, 21, 21));
		rem.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		if(LobbyShopSQL.hasBought("BOOTS", p.getUniqueId(), 5)) {
			rem.setLore(has);
		} else {
			rem.setLore(buy1);
		}
		redstone.setItemMeta(rem);
		
		ItemStack cs = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta csm = (SkullMeta) cs.getItemMeta();
		csm.setDisplayName("§7Unbekannte Boots");
		csm.setOwner("MHF_QUESTION");
		cs.setItemMeta(csm);
		
		inv.setItem(11, flame);
		inv.setItem(12, magic);
		inv.setItem(13, emerald);
		inv.setItem(14, rain);
		inv.setItem(15, redstone);
		inv.setItem(20, cs);
		inv.setItem(21, cs);
		inv.setItem(22, cs);
		inv.setItem(23, cs);
		inv.setItem(24, cs);
		
		p.openInventory(inv);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				p.updateInventory();
			}
		}, 2);
	}
	
	public static void openHeads(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, "§bGadgets §7» §bHeads");

		List<String> buy1 = new ArrayList<String>();
		List<String> buy2 = new ArrayList<String>();
		List<String> buy3 = new ArrayList<String>();
		List<String> buy4 = new ArrayList<String>();
		List<String> buy5 = new ArrayList<String>();
		List<String> has = new ArrayList<String>();
		buy1.add("§7» §9Kaufen für §61.000 Coins");
		buy2.add("§7» §9Kaufen für §62.000 Coins");
		buy3.add("§7» §9Kaufen für §63.000 Coins");
		buy4.add("§7» §9Kaufen für §64.000 Coins");
		buy5.add("§7» §9Kaufen für §65.000 Coins");
		has.add("§7» §aGekauft");
		
		ItemStack head1 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m1 = (SkullMeta) head1.getItemMeta();
		m1.setOwner("Gronkh");
		m1.setDisplayName("§5Gronkh");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 1)) {
			m1.setLore(has);
		} else {
			m1.setLore(buy1);
		}
		head1.setItemMeta(m1);
		
		ItemStack head2 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m2 = (SkullMeta) head2.getItemMeta();
		m2.setOwner("GermanLetsPlay");
		m2.setDisplayName("§5GermanLetsPlay");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 2)) {
			m2.setLore(has);
		} else {
			m2.setLore(buy2);
		}
		head2.setItemMeta(m2);
		
		ItemStack head3 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m3 = (SkullMeta) head3.getItemMeta();
		m3.setDisplayName("§5Minusduude");
		m3.setOwner("minusduude");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 3)) {
			m3.setLore(has);
		} else {
			m3.setLore(buy1);
		}
		head3.setItemMeta(m3);
		
		ItemStack head4 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m4 = (SkullMeta) head4.getItemMeta();
		m4.setDisplayName("§5HerrBergmann");
		m4.setOwner("HerrBergmann");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 4)) {
			m4.setLore(has);
		} else {
			m4.setLore(buy3);
		}
		head4.setItemMeta(m4);
		
		ItemStack head5 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m5 = (SkullMeta) head5.getItemMeta();
		m5.setDisplayName("§5Izzi");
		m5.setOwner("izzi");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 5)) {
			m5.setLore(has);
		} else {
			m5.setLore(buy2);
		}
		head5.setItemMeta(m5);

		ItemStack head6 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m6 = (SkullMeta) head6.getItemMeta();
		m6.setDisplayName("§5Rewinside");
		m6.setOwner("rewinside");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 6)) {
			m6.setLore(has);
		} else {
			m6.setLore(buy2);
		}
		head6.setItemMeta(m6);
		
		ItemStack head7 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m7 = (SkullMeta) head7.getItemMeta();
		m7.setDisplayName("§5Paluten");
		m7.setOwner("Paluten");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 7)) {
			m7.setLore(has);
		} else {
			m7.setLore(buy4);
		}
		head7.setItemMeta(m7);
		
		ItemStack head8 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m8 = (SkullMeta) head8.getItemMeta();
		m8.setDisplayName("§5SturmwaffelHD");
		m8.setOwner("SturmwaffelHD");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 8)) {
			m8.setLore(has);
		} else {
			m8.setLore(buy4);
		}
		head8.setItemMeta(m8);
		
		ItemStack head9 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m9 = (SkullMeta) head9.getItemMeta();
		m9.setDisplayName("§5GommeHD");
		m9.setOwner("GommeHD");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 9)) {
			m9.setLore(has);
		} else {
			m9.setLore(buy3);
		}
		head9.setItemMeta(m9);
		
		ItemStack head10 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m10 = (SkullMeta) head10.getItemMeta();
		m10.setDisplayName("§5DoctorBenx");
		m10.setOwner("DoctorBenx");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 10)) {
			m10.setLore(has);
		} else {
			m10.setLore(buy3);
		}
		head10.setItemMeta(m10);
		
		ItemStack head11 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m11 = (SkullMeta) head11.getItemMeta();
		m11.setDisplayName("§5Dner");
		m11.setOwner("DnerTV");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 11)) {
			m11.setLore(has);
		} else {
			m11.setLore(buy5);
		}
		head11.setItemMeta(m11);
		
		ItemStack head12 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m12 = (SkullMeta) head12.getItemMeta();
		m12.setDisplayName("§5SparkOfPhoenix");
		m12.setOwner("SparkOfPhoenix");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 12)) {
			m12.setLore(has);
		} else {
			m12.setLore(buy4);
		}
		head12.setItemMeta(m12);
		
		ItemStack head13 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m13 = (SkullMeta) head13.getItemMeta();
		m13.setDisplayName("§5Reved");
		m13.setOwner("Reved");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 13)) {
			m13.setLore(has);
		} else {
			m13.setLore(buy2);
		}
		head13.setItemMeta(m13);
		
		ItemStack head14 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m14 = (SkullMeta) head14.getItemMeta();
		m14.setDisplayName("§5LPmitKev");
		m14.setOwner("DerKev");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 14)) {
			m14.setLore(has);
		} else {
			m14.setLore(buy2);
		}
		head14.setItemMeta(m14);
		
		ItemStack head15 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m15 = (SkullMeta) head15.getItemMeta();
		m15.setDisplayName("§5MCExpertDE");
		m15.setOwner("MCExpertDE");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 15)) {
			m15.setLore(has);
		} else {
			m15.setLore(buy3);
		}
		head15.setItemMeta(m15);
		
		ItemStack head16 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m16 = (SkullMeta) head16.getItemMeta();
		m16.setDisplayName("§5Ungespielt");
		m16.setOwner("ungespielt");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 16)) {
			m16.setLore(has);
		} else {
			m16.setLore(buy5);
		}
		head16.setItemMeta(m16);
		
		ItemStack head17 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m17 = (SkullMeta) head17.getItemMeta();
		m17.setDisplayName("§5CuzImSara");
		m17.setOwner("CuzImSara");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 17)) {
			m17.setLore(has);
		} else {
			m17.setLore(buy1);
		}
		head17.setItemMeta(m17);
		
		ItemStack head18 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m18 = (SkullMeta) head18.getItemMeta();
		m18.setDisplayName("§5BastiGHG");
		m18.setOwner("BastiGHG");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 18)) {
			m18.setLore(has);
		} else {
			m18.setLore(buy2);
		}
		head18.setItemMeta(m18);
		
		ItemStack head19 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m19 = (SkullMeta) head19.getItemMeta();
		m19.setDisplayName("§5VeniCraft");
		m19.setOwner("venicraft");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 19)) {
			m19.setLore(has);
		} else {
			m19.setLore(buy2);
		}
		head19.setItemMeta(m19);
		
		ItemStack head20 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m20 = (SkullMeta) head20.getItemMeta();
		m20.setDisplayName("§5Fr3akzLP");
		m20.setOwner("Fr3akzLP");
		if(LobbyShopSQL.hasBought("HEADS", p.getUniqueId(), 20)) {
			m20.setLore(has);
		} else {
			m20.setLore(buy1);
		}
		head20.setItemMeta(m20);
		
		ItemStack remove = new ItemStack(Material.BARRIER);
		ItemMeta rm = remove.getItemMeta();
		rm.setDisplayName("§7» §cKöpfe absetzen");
		remove.setItemMeta(rm);
		
		inv.setItem(11, head1);
		inv.setItem(12, head2);
		inv.setItem(13, head3);
		inv.setItem(14, head4);
		inv.setItem(15, head5);
		inv.setItem(20, head6);
		inv.setItem(21, head7);
		inv.setItem(22, head8);
		inv.setItem(23, head9);
		inv.setItem(24, head10);
		inv.setItem(29, head11);
		inv.setItem(30, head12);
		inv.setItem(31, head13);
		inv.setItem(32, head14);
		inv.setItem(33, head15);
		inv.setItem(38, head16);
		inv.setItem(39, head17);
		inv.setItem(40, head18);
		inv.setItem(41, head19);
		inv.setItem(42, head20);
		inv.setItem(49, remove);
		
		p.openInventory(inv);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				p.updateInventory();
			}
		}, 2);
	}
	
	public static void openPets(Player p) {
		Inventory inv = Bukkit.createInventory(null, 36, "§bGadgets §7» §bPets");


		List<String> buy1 = new ArrayList<String>();
		List<String> has = new ArrayList<String>();
		buy1.add("§7» §9Kaufen für §62.500 Coins");
		has.add("§7» §aGekauft");
		
		ItemStack pet1 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m1 = (SkullMeta) pet1.getItemMeta();
		m1.setOwner("MHF_COW");
		m1.setDisplayName("§bKuh");
		if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 1)) {
			m1.setLore(buy1);
		} else {
			m1.setLore(has);
		}
		pet1.setItemMeta(m1);

		ItemStack pet2 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m2 = (SkullMeta) pet1.getItemMeta();
		m2.setOwner("MHF_SHEEP");
		m2.setDisplayName("§bSchaf");
		if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 2)) {
			m2.setLore(buy1);
		} else {
			m2.setLore(has);
		}
		pet2.setItemMeta(m2);

		ItemStack pet3 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m3 = (SkullMeta) pet1.getItemMeta();
		m3.setOwner("gavertoso");
		m3.setDisplayName("§bPferd");
		if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 3)) {
			m3.setLore(buy1);
		} else {
			m3.setLore(has);
		}
		pet3.setItemMeta(m3);

		ItemStack pet4 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m4 = (SkullMeta) pet1.getItemMeta();
		m4.setOwner("MHF_MUSHROOMCOW");
		m4.setDisplayName("§bPilzkuh");
		if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 4)) {
			m4.setLore(buy1);
		} else {
			m4.setLore(has);
		}
		pet4.setItemMeta(m4);

		ItemStack pet5 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m5 = (SkullMeta) pet1.getItemMeta();
		m5.setOwner("MHF_OCELOT");
		m5.setDisplayName("§bKatze");
		if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 5)) {
			m5.setLore(buy1);
		} else {
			m5.setLore(has);
		}
		pet5.setItemMeta(m5);

		ItemStack pet6 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m6 = (SkullMeta) pet1.getItemMeta();
		m6.setOwner("MHF_WOLF");
		m6.setDisplayName("§bHund");
		if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 6)) {
			m6.setLore(buy1);
		} else {
			m6.setLore(has);
		}
		pet6.setItemMeta(m6);

		ItemStack pet7 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m7 = (SkullMeta) pet1.getItemMeta();
		m7.setOwner("MHF_PIG");
		m7.setDisplayName("§bSchwein");
		if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 7)) {
			m7.setLore(buy1);
		} else {
			m7.setLore(has);
		}
		pet7.setItemMeta(m7);

		ItemStack pet8 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m8 = (SkullMeta) pet1.getItemMeta();
		m8.setOwner("MHF_RABBIT");
		m8.setDisplayName("§bHase");
		if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 8)) {
			m8.setLore(buy1);
		} else {
			m8.setLore(has);
		}
		pet8.setItemMeta(m8);

		ItemStack pet9 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m9 = (SkullMeta) pet1.getItemMeta();
		m9.setOwner("MHF_CHICKEN");
		m9.setDisplayName("§cHuhn");
		if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 9)) {
			m9.setLore(buy1);
		} else {
			m9.setLore(has);
		}
		pet9.setItemMeta(m9);

		ItemStack pet10 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m10 = (SkullMeta) pet1.getItemMeta();
		m10.setOwner("MHF_SLIME");
		m10.setDisplayName("§bSchleim");
		if(!LobbyShopSQL.hasBought("PETS", p.getUniqueId(), 10)) {
			m10.setLore(buy1);
		} else {
			m10.setLore(has);
		}
		pet10.setItemMeta(m10);
		
		ItemStack pet11 = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta m11 = (SkullMeta) pet1.getItemMeta();
		m11.setOwner("MHF_PigZombie");
		m11.setDisplayName("§dSchweinezombie");
		if(!p.hasPermission("system.premium")) {
			List<String> lore = new ArrayList<String>();
			lore.add("§7» §6Premium Haustier");
			lore.add("§7» Du benötigst Premium für");
			lore.add("§7» dieses Haustier");
			m11.setLore(lore);
		} else {
			m11.setLore(has);
		}
		pet11.setItemMeta(m11);
		
		inv.setItem(4, pet11);
		inv.setItem(11, pet1);
		inv.setItem(12, pet2);
		inv.setItem(13, pet3);
		inv.setItem(14, pet4);
		inv.setItem(15, pet5);
		inv.setItem(20, pet6);
		inv.setItem(21, pet7);
		inv.setItem(22, pet8);
		inv.setItem(23, pet9);
		inv.setItem(24, pet10);
		
		p.openInventory(inv);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				p.updateInventory();
			}
		}, 2);
	}
	
	private static HashMap<Player, Pet> player_pet = PetListener.player_pet;
	
	public static void spawnPet(Player p, EntityType et, int size, String name) {
		if(player_pet.containsKey(p)) {
			player_pet.get(p).removePet();
			player_pet.remove(p);
		}
		Location loc = p.getLocation().clone();
		loc.setYaw(loc.getYaw() + 90);
		loc.setPitch(0);
		loc.add(loc.getDirection().multiply(1.0D).normalize());
		loc.setYaw(loc.getYaw() - 90);
		Entity ent = loc.getWorld().spawnEntity(loc, et);
		if(ent.getType().equals(EntityType.OCELOT)) {
			Ocelot a = (Ocelot)ent;
			a.setBaby();
			a.setTamed(true);
			a.setAge(Integer.MIN_VALUE);
			a.setSitting(false);
		}
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(main.pets.contains(all)) {
				Einstellungen.hidePets(all);
			}
		}
		Pet pet = new Pet(ent, p);
		pet.setSize(size);
		if(name != null) {
			pet.renamePet(name, true);
		}
		LobbyPetSQL.addEntry(p.getUniqueId(), et, size, pet.getName());
		player_pet.put(p, pet);
	}
	
	@EventHandler
	public void onJoinPet(PlayerJoinEvent e) {
		if(!LobbySettingsSQL.getSettingsBoolean(e.getPlayer().getUniqueId(), "PETS")) {
			for(Player all : player_pet.keySet()) {
				Entity ent = player_pet.get(all).ent;
				PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[]{ent.getEntityId()});
				((CraftPlayer)e.getPlayer()).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(player_pet.containsKey(e.getPlayer())) {
			Pet pet = player_pet.get(e.getPlayer());
			pet.follow(1.75);
		}
	}
	
	public static void PetFollow() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				for(Player all : player_pet.keySet()) {
					Pet p = player_pet.get(all);
					p.follow(1.25);
				}
			}
		}, 0, 5);
	}
	
	

}
