package de.legitfinn.lobby.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.legitfinn.lobby.SQL.LobbySettingsSQL;
import de.legitfinn.lobby.commands.COMMAND_flight;
import de.legitfinn.lobby.functions.DoubleJump;
import de.legitfinn.lobby.functions.PetListener;
import de.legitfinn.lobby.main.main;
import de.legitfinn.lobby.scoreboard.ScoreboardSystem;
import de.legitfinn.ppermissions.main.PPermissions;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

public class Einstellungen implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getPlayer().getItemInHand().getType().equals(Material.REDSTONE_COMPARATOR)) {
				e.setCancelled(true);
				openSettings(e.getPlayer());
				e.getPlayer().updateInventory();
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(LobbySettingsSQL.getSettingsBoolean(e.getPlayer().getUniqueId(), "PARTICLES")) {
			main.particles.add(e.getPlayer());
		}
		if(LobbySettingsSQL.getSettingsBoolean(e.getPlayer().getUniqueId(), "CHAT")) {
			main.chat.add(e.getPlayer());
		}
		if(LobbySettingsSQL.getSettingsBoolean(e.getPlayer().getUniqueId(), "DOUBLEJUMP")) {
			main.doubleJump.add(e.getPlayer());
		}
		if(LobbySettingsSQL.getSettingsBoolean(e.getPlayer().getUniqueId(), "PETS")) {
			main.pets.add(e.getPlayer());
		}
		main.visible.remove(e.getPlayer());
		main.visible.put(e.getPlayer(), LobbySettingsSQL.getSettingsInt(e.getPlayer().getUniqueId(), "VISIBLE"));
		for(Player all : Bukkit.getOnlinePlayers()) {
			updateVisible(all);
		}
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(main.pets.contains(all) || main.isSilentLobby) {
				hidePets(all);
			}
		}
		
	}
	
	public static void hidePets(Player p) {
		for(Player all : PetListener.player_pet.keySet()) {
			if(!all.equals(p)) {
				PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] {PetListener.player_pet.get(all).ent.getEntityId()});
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}
	
	public static void showPets(Player p) {
		for(Player all : PetListener.player_pet.keySet()) {
			if(!all.equals(p)) {
				PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(((CraftLivingEntity)PetListener.player_pet.get(all).ent).getHandle());
				((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}
	
	public static void updateVisible(Player p) {
		if(main.visible.containsKey(p)) {
			int set = main.visible.get(p);
			if(set == 1) {
				for(Player all : Bukkit.getOnlinePlayers()) {
					p.hidePlayer(all);
				}
			} else if(set == 2) {
				for(Player all : Bukkit.getOnlinePlayers()) {
					p.showPlayer(all);
				}
			} else if(set == 3) {
				for(Player all : Bukkit.getOnlinePlayers()) {
					p.hidePlayer(all);
					if(PPermissions.isInGroup(all, "Premium") || PPermissions.isInGroup(all, "Premium+") || PPermissions.isInGroup(all, "YouTuber") || PPermissions.isInTeamGroup(all)) {
						p.showPlayer(all);
					}
				}
			} else if(set == 4) {
				for(Player all : Bukkit.getOnlinePlayers()) {
					p.hidePlayer(all);
					if(PPermissions.isInGroup(all, "YouTuber") || PPermissions.isInTeamGroup(all)) {
						p.showPlayer(all);
					}
				}
			} else if(set == 5) {
				for(Player all : Bukkit.getOnlinePlayers()) {
					p.hidePlayer(all);
					if(PPermissions.isInTeamGroup(all)) {
						p.showPlayer(all);
					}
				}
			} else if(set == 6) {
				for(Player all : Bukkit.getOnlinePlayers()) {
					p.hidePlayer(all);
					if(PPermissions.isInGroup(all, "Admin")) {
						p.showPlayer(all);
					}
				}
			}
		} else {
			main.visible.put(p, LobbySettingsSQL.getSettingsInt(p.getUniqueId(), "VISIBLE"));
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null) {
			if(e.getCurrentItem() != null) {
				if(e.getClickedInventory().getTitle().equals("§cEinstellungen")) {
					e.setCancelled(true);
					int slot = e.getRawSlot();
					Player p = (Player)e.getWhoClicked();
					if(slot == 10 || slot == 19) {
						if(LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "DOUBLEJUMP")) {
							LobbySettingsSQL.setSettings(p.getUniqueId(), "DOUBLEJUMP", 0);
							if(!COMMAND_flight.flight.contains(p)) {
								p.setAllowFlight(false);
							}
							main.doubleJump.remove(p);
							DoubleJump.inJump.remove(p);
						} else {
							LobbySettingsSQL.setSettings(p.getUniqueId(), "DOUBLEJUMP", 1);
							if(p.isOnGround()) {
								p.setAllowFlight(true);
							} else {
								DoubleJump.inJump.add(p);
							}
							main.doubleJump.add(p);
						}
						openSettings(p);
					} else if(slot == 11 || slot == 20) {
						if(LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "SCOREBOARD")) {
							LobbySettingsSQL.setSettings(p.getUniqueId(), "SCOREBOARD", 0);
							ScoreboardSystem.updateScoreboard(p, true, false);
						} else {
							LobbySettingsSQL.setSettings(p.getUniqueId(), "SCOREBOARD", 1);
							ScoreboardSystem.updateScoreboard(p, true, false);
						}
						openSettings(p);
					} else if(slot == 12 || slot == 21) {
						openVisible(p);
					} else if(slot == 14 || slot == 23) {
						if(LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "PETS")) {
							LobbySettingsSQL.setSettings(p.getUniqueId(), "PETS", 0);
							main.pets.add(p);
							hidePets(p);
						} else {
							LobbySettingsSQL.setSettings(p.getUniqueId(), "PETS", 1);
							main.pets.remove(p);
							showPets(p);
						}
						openSettings(p);
					} else if(slot == 15 || slot == 24) {
						if(LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "PARTICLES")) {
							LobbySettingsSQL.setSettings(p.getUniqueId(), "PARTICLES", 0);
							main.particles.remove(p);
						} else {
							LobbySettingsSQL.setSettings(p.getUniqueId(), "PARTICLES", 1);
							if(!main.particles.contains(p)) {
								main.particles.add(p);
							}
						}
						openSettings(p);
					} else if(slot == 16 || slot == 25) {
						if(LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "CHAT")) {
							LobbySettingsSQL.setSettings(p.getUniqueId(), "CHAT", 0);
							main.chat.remove(p);
						} else {
							LobbySettingsSQL.setSettings(p.getUniqueId(), "CHAT", 1);
							if(!main.chat.contains(p)) {
								main.chat.add(p);
							}
						}
						openSettings(p);
					}
				} else if(e.getClickedInventory().getTitle().equalsIgnoreCase("§bSichtbarkeit")) {
					e.setCancelled(true);
					int slot = e.getRawSlot();
					Player p = (Player)e.getWhoClicked();
					if(slot == 10 || slot == 19) {
						LobbySettingsSQL.setSettings(p.getUniqueId(), "VISIBLE", 1);
						openVisible(p);
						main.visible.remove(p);
						main.visible.put(p, 1);
						updateVisible(p);
					} else if(slot == 11 || slot == 20) {
						LobbySettingsSQL.setSettings(p.getUniqueId(), "VISIBLE", 2);
						openVisible(p);
						main.visible.remove(p);
						main.visible.put(p, 2);
						updateVisible(p);
					} else if(slot == 12 || slot == 21) {
						LobbySettingsSQL.setSettings(p.getUniqueId(), "VISIBLE", 3);
						openVisible(p);
						main.visible.remove(p);
						main.visible.put(p, 3);
						updateVisible(p);
					} else if(slot == 14 || slot == 23) {
						LobbySettingsSQL.setSettings(p.getUniqueId(), "VISIBLE", 4);
						openVisible(p);
						main.visible.remove(p);
						main.visible.put(p, 4);
						updateVisible(p);
					} else if(slot == 15 || slot == 24) {
						LobbySettingsSQL.setSettings(p.getUniqueId(), "VISIBLE", 5);
						openVisible(p);
						main.visible.remove(p);
						main.visible.put(p, 5);
						updateVisible(p);
					} else if(slot == 16 || slot == 25) {
						LobbySettingsSQL.setSettings(p.getUniqueId(), "VISIBLE", 6);
						openVisible(p);
						main.visible.remove(p);
						main.visible.put(p, 6);
						updateVisible(p);
					}
				}
			}
		}
	}
	
	
	public static void openSettings(Player p) {
		
		ItemStack jump = new ItemStack(Material.FEATHER);
		ItemMeta jm = jump.getItemMeta();
		jm.setDisplayName("§9§lDoppelsprung");
		jump.setItemMeta(jm);
		
		ItemStack scboard = new ItemStack(Material.SIGN);
		ItemMeta sm = scboard.getItemMeta();
		sm.setDisplayName("§c§lScoreboard");
		scboard.setItemMeta(sm);
		
		ItemStack visible = new ItemStack(Material.NETHER_STAR);
		ItemMeta vm = visible.getItemMeta();
		vm.setDisplayName("§a§lSichtbarkeit");
		visible.setItemMeta(vm);
		
		ItemStack pets = new ItemStack(Material.MONSTER_EGG);
		ItemMeta pm = pets.getItemMeta();
		pm.setDisplayName("§6§lHaustiere");
		pets.setItemMeta(pm);
		
		ItemStack particle = new ItemStack(Material.BLAZE_POWDER);
		ItemMeta pam = particle.getItemMeta();
		pam.setDisplayName("§b§lPartikel");
		particle.setItemMeta(pam);
		
		ItemStack chat = new ItemStack(Material.BOOK_AND_QUILL);
		ItemMeta cm = chat.getItemMeta();
		cm.setDisplayName("§e§lChat");
		chat.setItemMeta(cm);
		
		ItemStack enabled = new ItemStack(Material.INK_SACK, 1, (short)10);
		ItemMeta em = enabled.getItemMeta();
		em.setDisplayName("§aAktiviert");
		enabled.setItemMeta(em);
		
		ItemStack disabled = new ItemStack(Material.INK_SACK, 1, (short)8);
		ItemMeta dm = disabled.getItemMeta();
		dm.setDisplayName("§7Deaktiviert");
		disabled.setItemMeta(dm);
		
		ItemStack not = new ItemStack(Material.BARRIER);
		ItemMeta nm = not.getItemMeta();
		nm.setDisplayName("§cNicht verfügbar.");
		not.setItemMeta(nm);
		
		
		Inventory inv = Bukkit.createInventory(null, 36, "§cEinstellungen");
		
		inv.setItem(10, jump);
		inv.setItem(11, scboard);
		inv.setItem(12, visible);
		inv.setItem(14, pets);
		inv.setItem(15, particle);
		inv.setItem(16, chat);
		
		if(!LobbySettingsSQL.isListed(p.getUniqueId())) {
			LobbySettingsSQL.addToList(p.getUniqueId(), 1, 1, 1, 1, 1, 1);
		}
		
		if(LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "DOUBLEJUMP")) {
			inv.setItem(19, enabled);
		} else {
			inv.setItem(19, disabled);
		}
		
		if(LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "SCOREBOARD")) {
			inv.setItem(20, enabled);
		} else {
			inv.setItem(20, disabled);
		}
		
		inv.setItem(21, disabled);		
		
		if(LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "PETS")) {
			inv.setItem(23, enabled);
		} else {
			inv.setItem(23, disabled);
		}
		
		if(LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "PARTICLES")) {
			inv.setItem(24, enabled);
		} else {
			inv.setItem(24, disabled);
		}
		
		if(LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "CHAT")) {
			inv.setItem(25, enabled);
		} else {
			inv.setItem(25, disabled);
		}
		
		p.openInventory(inv);		
	}
	
	public void openVisible(Player p) {
		
		ItemStack no = new ItemStack(Material.STAINED_CLAY, 1, (short)9);
		ItemMeta nm = no.getItemMeta();
		nm.setDisplayName("§8» §7Keine Spieler anzeigen");
		no.setItemMeta(nm);
		
		ItemStack all = new ItemStack(Material.STAINED_CLAY, 1, (short)5);
		ItemMeta am = all.getItemMeta();
		am.setDisplayName("§8» §aAlle Spieler anzeigen");
		all.setItemMeta(am);
		
		ItemStack prm = new ItemStack(Material.STAINED_CLAY, 1, (short)4);
		ItemMeta pm = prm.getItemMeta();
		pm.setDisplayName("§8» §6Premium Spieler anzeigen.");
		List<String> lore1 = new ArrayList<String>();
		lore1.add("§8» §7Zeigt Premium, Premium+, YouTuber und Teammitglieder");
		pm.setLore(lore1);
		prm.setItemMeta(pm);
		
		ItemStack yt = new ItemStack(Material.STAINED_CLAY, 1, (short)10);
		ItemMeta ym = yt.getItemMeta();
		ym.setDisplayName("§8» §5YouTuber anzeigen");
		List<String> lore2 = new ArrayList<String>();
		lore2.add("§8» §7Zeigt YouTuber und Teammitglieder");
		ym.setLore(lore2);
		yt.setItemMeta(ym);
		
		ItemStack team = new ItemStack(Material.STAINED_CLAY, 1, (short)1);
		ItemMeta tm = team.getItemMeta();
		List<String> lore3 = new ArrayList<String>();
		lore3.add("§8» §7Zeigt Teammitglieder an");
		tm.setLore(lore3);
		tm.setDisplayName("§8» §cTeammitglieder anzeigen");
		team.setItemMeta(tm);
		
		ItemStack admin = new ItemStack(Material.STAINED_CLAY, 1, (short)14);
		ItemMeta adm = admin.getItemMeta();
		adm.setDisplayName("§8» §4Admins anzeigen");
		List<String> lore4 = new ArrayList<String>();
		lore4.add("§8» §7Zeigt nur Administratoren an.");
		adm.setLore(lore4);
		admin.setItemMeta(adm);
		
		ItemStack enabled = new ItemStack(Material.INK_SACK, 1, (short)10);
		ItemMeta em = enabled.getItemMeta();
		em.setDisplayName("§aAktiviert");
		enabled.setItemMeta(em);
		
		ItemStack disabled = new ItemStack(Material.INK_SACK, 1, (short)8);
		ItemMeta dm = disabled.getItemMeta();
		dm.setDisplayName("§7Deaktiviert");
		disabled.setItemMeta(dm);
		
		Inventory inv = Bukkit.createInventory(null, 36, "§bSichtbarkeit");
		
		inv.setItem(10, no);
		inv.setItem(11, all);
		inv.setItem(12, prm);
		inv.setItem(14, yt);
		inv.setItem(15, team);
		inv.setItem(16, admin);
		
		inv.setItem(19, disabled);
		inv.setItem(20, disabled);
		inv.setItem(21, disabled);
		inv.setItem(23, disabled);
		inv.setItem(24, disabled);
		inv.setItem(25, disabled);
		
		int set = LobbySettingsSQL.getSettingsInt(p.getUniqueId(), "VISIBLE");
		
		if(set == 1) {
			inv.setItem(19, enabled);
		} else if(set == 2) {
			inv.setItem(20, enabled);
		} else if(set == 3) {
			inv.setItem(21, enabled);
		} else if(set == 4) {
			inv.setItem(23, enabled);
		} else if(set == 5) {
			inv.setItem(24, enabled);
		} else if(set == 6) {
			inv.setItem(25, enabled);
		}	
		
		p.openInventory(inv);		
	}
	
	

}
