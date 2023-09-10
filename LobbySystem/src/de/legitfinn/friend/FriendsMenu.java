package de.legitfinn.friend;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.legitfinn.lobby.PlayerData.PlayerData;
import de.legitfinn.lobby.main.main;
import de.legitfinn.lobby.scoreboard.ScoreboardSystem;
import de.legitfinn.ppermissions.main.PPermissions;

public class FriendsMenu implements Listener {
	
	public static String connectionCode = "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";
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getPlayer().getItemInHand().getType().equals(Material.SKULL_ITEM)) {
				e.setCancelled(true);
				openMainMenu(e.getPlayer(), 1);
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			if(e.getEntity() instanceof Player) {
				Player hit = (Player) e.getDamager();
				Player get = (Player) e.getEntity();
				if(!hit.getItemInHand().getType().equals(Material.SKULL_ITEM)) {
					return;
				}
				if(!FriendSQL.getRequests(get.getUniqueId()).contains(hit.getName()) && !FriendSQL.getFriends(get.getUniqueId()).contains(hit.getName())) {
					if(FriendSQL.getSetting(get.getUniqueId(), "REQUEST")) {
//						hit.sendMessage(FriendMessages.sent_request.getMessage().replace("%player%", PPermissions.getChatColor(get.getUniqueId()) + get.getName()));
//						FriendSQL.addRequest(hit.getUniqueId(), get.getUniqueId());
						sendGlobalMessage("addFriend", hit, get.getName());
					} else {
						hit.sendMessage("§7[§4Freunde§7] §cDer Spieler " + PPermissions.getChatColor(get.getUniqueId()) + get.getName() + "§c akzeptiert keine Freundschaftsnfragen.");
					}
				} else {
					if(FriendSQL.hasRequestOf(hit.getUniqueId(), get.getUniqueId())) {
						hit.sendMessage("§7[§4Freunde§7] §6" + PPermissions.getChatColor(get.getUniqueId()) + get.getName() + " §chat bereits eine Anfrage von Dir erhalten.");	
					} else if(FriendSQL.getFriends(hit.getUniqueId()).contains(PlayerData.getName(get.getUniqueId()))){
						hit.sendMessage("§7[§4Freunde§7] §cDu bist bereits mit " + PPermissions.getChatColor(get.getUniqueId()) + get.getName() + " §cbefreundet.");
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getCurrentItem() != null) {
			if(ChatColor.stripColor(e.getClickedInventory().getTitle()).toLowerCase().contains("deine freunde")) {
				e.setCancelled(true);
				ItemStack is = e.getCurrentItem();
				if(is.getType().equals(Material.SKULL_ITEM)) {
					if(is.hasItemMeta()) {
						SkullMeta meta = (SkullMeta) is.getItemMeta();
						Player p = (Player)e.getWhoClicked();
						if(is.getData().getData() == (byte) 3 && meta.getOwner().equalsIgnoreCase("mhf_arrowright")) {
							int site = Integer.parseInt(e.getClickedInventory().getTitle().replace("§6§lDeine Freunde §7Seite ", "")) + 1;
							if(site <= maxSitesFriend(p)) {
								openMainMenu(p, site);
							}
						} else if(is.getData().getData() == (byte) 3 && meta.getOwner().equalsIgnoreCase("mhf_arrowleft")) {
							int site = Integer.parseInt(e.getClickedInventory().getTitle().replace("§6§lDeine Freunde §7Seite ", "")) - 1;
							if(site >= 1) {
								openMainMenu(p, site);
							}
						} else if(e.getRawSlot() >= 9 && e.getRawSlot() <= 35) {
							if(is.getData().getData() != (byte)3) {
								String name = ChatColor.stripColor(meta.getDisplayName()).replace(" » Offline", "");
								openFriendMenu(p, name);
							} else {
								String name = meta.getOwner();
								openFriendMenu(p, name);
							}
						}
					}
				} else if(is.getType().equals(Material.REDSTONE_COMPARATOR)) {
					Player p = (Player)e.getWhoClicked();
					openSettings(p);
				} else if(is.getType().equals(Material.EMPTY_MAP)) {
					Player p = (Player)e.getWhoClicked();
					openRequests(p, 1);
				}
			} else if(ChatColor.stripColor(e.getClickedInventory().getTitle()).equalsIgnoreCase("Freunde Einstellungen")) {
				e.setCancelled(true);
				Player p = (Player)e.getWhoClicked();
				int i = e.getRawSlot();
				if(i == 10 || i == 19) {
					if(FriendSQL.getSetting(p.getUniqueId(), "REQUEST")) {
						FriendSQL.setSettings(p.getUniqueId(), "REQUEST", 0);
						p.sendMessage("§7[§4Freunde§7] §cFreundschaftsanfragen deaktiviert.");
					} else {
						FriendSQL.setSettings(p.getUniqueId(), "REQUEST", 1);
						p.sendMessage("§7[§4Freunde§7] §aFreundschaftsanfragen aktiviert.");
					}
					openSettings(p);
				} else if(i == 11 || i == 20) {
					if(FriendSQL.getSetting(p.getUniqueId(), "PARTY")) {
						FriendSQL.setSettings(p.getUniqueId(), "PARTY", 0);
						p.sendMessage("§7[§4Freunde§7] §cPartyanfragen deaktiviert.");
					} else {
						FriendSQL.setSettings(p.getUniqueId(), "PARTY", 1);
						p.sendMessage("§7[§4Freunde§7] §aPartyanfragen aktiviert.");
					}
					openSettings(p);
				} else if(i == 12 || i == 21) {
					if(FriendSQL.getSetting(p.getUniqueId(), "PRIVATEMSG")) {
						FriendSQL.setSettings(p.getUniqueId(), "PRIVATEMSG", 0);
						p.sendMessage("§7[§4Freunde§7] §cPrivate Nachrichten deaktiviert.");
					} else {
						FriendSQL.setSettings(p.getUniqueId(), "PRIVATEMSG", 1);
						p.sendMessage("§7[§4Freunde§7] §aPrivate Nachrichten aktiviert.");
					}
					openSettings(p);
				} else if(i == 14 || i == 23) {
					if(FriendSQL.getSetting(p.getUniqueId(), "TELEPORT")) {
						FriendSQL.setSettings(p.getUniqueId(), "TELEPORT", 0);
						p.sendMessage("§7[§4Freunde§7] §cNachjoinen deaktiviert.");
					} else {
						FriendSQL.setSettings(p.getUniqueId(), "TELEPORT", 1);
						p.sendMessage("§7[§4Freunde§7] §aNachjoinen aktiviert.");
					}
					openSettings(p);
				} else if(i == 15 || i == 24) {
					if(FriendSQL.getSetting(p.getUniqueId(), "CLAN")) {
						FriendSQL.setSettings(p.getUniqueId(), "CLAN", 0);
						p.sendMessage("§7[§4Freunde§7] §cClananfragen deaktiviert.");
					} else {
						FriendSQL.setSettings(p.getUniqueId(), "CLAN", 1);
						p.sendMessage("§7[§4Freunde§7] §aClananfragen aktiviert.");
					}
					openSettings(p);
				} else if(i == 16 || i == 25) {
					if(FriendSQL.getSetting(p.getUniqueId(), "NOTIFY")) {
						FriendSQL.setSettings(p.getUniqueId(), "NOTIFY", 0);
						p.sendMessage("§7[§4Freunde§7] §cOn-/Offline Nachrichten deaktiviert.");
					} else {
						FriendSQL.setSettings(p.getUniqueId(), "NOTIFY", 1);
						p.sendMessage("§7[§4Freunde§7] §aOn-/Offline Nachrichten aktiviert.");
					}
					openSettings(p);
				} else if(i == 27) {
					openMainMenu(p, 1);
				}
			} else if(e.getClickedInventory().getTitle().endsWith("§§§§§§")) {
				e.setCancelled(true);
				Player p = (Player)e.getWhoClicked();
				String name = ChatColor.stripColor(e.getClickedInventory().getTitle().replace("§§§§§§", ""));
				if(e.getCurrentItem().getType().equals(Material.BARRIER)) {
					FriendSQL.removeFriend(p.getUniqueId(), PlayerData.getUUID(name));
					p.sendMessage("§7[§4Freunde§7] §cDu hast die Freundschaft mit " + PPermissions.getChatColor(PlayerData.getUUID(name)) + name + "§c aufgelößt.");
					sendGlobalMessage("remove", p, name);
					openMainMenu(p, 1);
					ScoreboardSystem.updateScoreboard(p, false, false);
				} else if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
					if(e.getCurrentItem().hasItemMeta()) {
						SkullMeta meta = (SkullMeta) e.getCurrentItem().getItemMeta();
						if(meta.getOwner().equalsIgnoreCase("mhf_arrowleft")) {
							openMainMenu(p, 1);
						}
					}
				} else if(e.getCurrentItem().getType().equals(Material.CAKE)) {
					if(e.getCurrentItem().hasItemMeta()) {
						sendGlobalMessage("party", p, name);
					}
				} else if(e.getCurrentItem().getType().equals(Material.ENDER_PEARL)) {
					if(e.getCurrentItem().hasItemMeta()) {
						sendGlobalMessage("teleport", p, name);
					} 
				} else if(e.getCurrentItem().getType().equals(Material.LEATHER_HELMET)) {
					if(e.getCurrentItem().hasItemMeta()) {
						p.sendMessage("§7[§4Freunde§7] §cClanystem kommt noch.");
					}
				}
			} else if(e.getClickedInventory().getTitle().contains("§6§lOffene Anfragen")) {
				e.setCancelled(true);
				Player p = (Player)e.getWhoClicked();
				if(e.getRawSlot() == 45) {
					openMainMenu(p, 1);
				} else if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
					if(e.getCurrentItem().hasItemMeta()) {
						SkullMeta meta = (SkullMeta)e.getCurrentItem().getItemMeta();
						if(meta.getOwner().equalsIgnoreCase("mhf_arrowleft")) {
							int aksite = Integer.parseInt(e.getClickedInventory().getTitle().replace("§6§lOffene Anfragen §7Seite ", ""));
							int nxtpage = aksite - 1;
							if(nxtpage >= 1) {
								openRequests(p, nxtpage);
							}
						} else if(meta.getOwner().equalsIgnoreCase("mhf_arrowright")) {
							int sites = maxSitesRequest(p);
							int aksite = Integer.parseInt(e.getClickedInventory().getTitle().replace("§6§lOffene Anfragen §7Seite ", ""));
							int nxtpage = aksite + 1;
							if(nxtpage <= sites) {
								openRequests(p, nxtpage);
							}
						} else {
							String name = meta.getOwner();
							openPlayerRequest(p, name);
						}
					}
				} else if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)) {
					if(e.getCurrentItem().getData().getData() == (byte)14) {
						for(String all : FriendSQL.getRequests(p.getUniqueId())) {
							FriendSQL.removeFriend(p.getUniqueId(), PlayerData.getUUID(all));
							sendGlobalMessage("requestdeny", p, all);
						}
						openRequests(p, 1);
						p.sendMessage("§7[§4Freunde§7] §cDu hast erfolgreich alle Anfragen abgelehnt.");
					} else if(e.getCurrentItem().getData().getData() == (byte)5) {
						for(String all : FriendSQL.getRequests(p.getUniqueId())) {
							FriendSQL.turnToFriend(PlayerData.getUUID(all), p.getUniqueId());
							sendGlobalMessage("requestaccept", p, all);
						}
						openRequests(p, 1);
						p.sendMessage("§7[§4Freunde§7] §aDu hast erfolgreich alle Anfragen angenommen.");
					}
				}
			} else if(e.getClickedInventory().getTitle().endsWith("§§§§")) {
				e.setCancelled(true);
				Player p = (Player)e.getWhoClicked();
				String name = ((SkullMeta)e.getClickedInventory().getItem(4).getItemMeta()).getOwner();
				if(e.getRawSlot() == 18) {
					if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
						openRequests(p, 1);
					}
				} else {
					if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)) {
						if(e.getCurrentItem().getData().getData() == (byte)5) {
							FriendSQL.turnToFriend(PlayerData.getUUID(name), p.getUniqueId());
							p.sendMessage("§7[§4Freunde§7] §aDu hast die Freundschaft mit " + PPermissions.getChatColor(PlayerData.getUUID(name)) +  name + " §aakzeptiert.");
							sendGlobalMessage("requestaccept", p, name);
							openRequests(p, 1);
						} else if(e.getCurrentItem().getData().getData() == (byte)14) {
							FriendSQL.removeFriend(PlayerData.getUUID(name), p.getUniqueId());
							p.sendMessage("§7[§4Freunde§7] §cDu hast die Freundschaft mit " + PPermissions.getChatColor(PlayerData.getUUID(name)) + name + " §cabgelehnt.");
							sendGlobalMessage("requestdeny", p, name);
							openRequests(p, 1);
						}
					}
				}
			}
		}
	}
	
	
	static String[] names = "aktualisiert, LegitFinn".split(", ");
	
	public static int maxSitesFriend(Player p) {
		ArrayList<String> friends = FriendSQL.getFriends(p.getUniqueId());
		int i = friends.size() / 27;
		if((double)friends.size() / (double)27 > i) {
			i ++;
		}
		return i;
	}
	
	public static int maxSitesRequest(Player p) {
		ArrayList<String> friends = FriendSQL.getRequests(p.getUniqueId());
		int i = friends.size() / 27;
		if((double)friends.size() / (double)27 > i) {
			i ++;
		}
		return i;
	}
	
	public static void openMainMenu(Player p, int site) {
		ArrayList<String> all_friends = FriendSQL.getFriends(p.getUniqueId());
		ArrayList<String> site_friends = new ArrayList<String>();
		ArrayList<String> site_online = new ArrayList<String>();		
		int max = site * 27;
		int start = max - 27;
		
		int ia = 1;
		for(String all : all_friends) {
			String name = all; //UserData
			if(ia >= start) {
				site_friends.add(name);
			}
			ia ++;
		}
		for(String all : site_friends) {
			if(FriendSQL.isOnline(PlayerData.getUUID(all))) {
				site_online.add(all);
			}
		}
		
		Inventory inv = Bukkit.createInventory(null, 54, "§6§lDeine Freunde §7Seite " + site);
		
		ItemStack none = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
		ItemMeta nm = none.getItemMeta();
		nm.setDisplayName("§b");
		none.setItemMeta(nm);
		
		ItemStack settings = new ItemStack(Material.REDSTONE_COMPARATOR);
		ItemMeta sm = settings.getItemMeta();
		sm.setDisplayName("§7§l» §cEinstellungen");
		settings.setItemMeta(sm);
		
		ItemStack nxtpage = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta nmeta = (SkullMeta) nxtpage.getItemMeta();
		nmeta.setDisplayName("§7§lNächste Seite »");
		nmeta.setOwner("MHF_ARROWRIGHT");
		nxtpage.setItemMeta(nmeta);
		
		ItemStack bpage = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta bmeta = (SkullMeta)bpage.getItemMeta();
		bmeta.setDisplayName("§7§l« Vorherige Seite");
		bmeta.setOwner("MHF_ARROWLEFT");
		bpage.setItemMeta(bmeta);
		
		ItemStack req = new ItemStack(Material.EMPTY_MAP);
		ItemMeta reqm = req.getItemMeta();
		reqm.setDisplayName("§7§l» §eOffene Anfragen§7: §a" + FriendSQL.getRequests(p.getUniqueId()).size());
		req.setItemMeta(reqm);
		
		int ifr = 9;
		for(String all : site_online) {
			if(ifr > 35) {
				break;
			}
			if(site_online.contains(all)) {
				ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
				SkullMeta meta = (SkullMeta) is.getItemMeta();
				meta.setDisplayName(PPermissions.getChatColor(PlayerData.getUUID(all)) + all + " §7§l» §aOnline");
				List<String> lore = new ArrayList<String>();
				lore.add("§7Online auf: §8" + FriendSQL.getOnlineOn(p.getUniqueId()));
				meta.setLore(lore);
				meta.setOwner(all);
				is.setItemMeta(meta);
				inv.setItem(ifr, is);
			}
			ifr ++;	
		}	
		for(String all : site_friends) {
			if(ifr > 35) {
				break;
			}
			if(!site_online.contains(all)) {
				ItemStack is = new ItemStack(Material.SKULL_ITEM);
				SkullMeta meta = (SkullMeta) is.getItemMeta();
				meta.setDisplayName("§8" + all + " §7§l» §cOffline");
				List<String> lore = new ArrayList<String>();
				String[] lastonline = FriendSQL.getLastOnline(PlayerData.getUUID(all)).split(" ");
				lore.add("§7Zuletzt online am");
				lore.add("§8  ➥§7 "  + lastonline[0] + " um " + lastonline[1] + " Uhr");
				meta.setLore(lore);
				is.setItemMeta(meta);
				inv.setItem(ifr, is);
				ifr ++;
			}
		}
		
		inv.setItem(45, settings);
		inv.setItem(46, req);
		if(site < maxSitesFriend(p)) {
			inv.setItem(53, nxtpage);
		}
		if(site > 1) {
			inv.setItem(52, bpage);
		}
		int i = 0;
		for(ItemStack all : inv.getContents()) {
			if(i < 9 || (i > 35 && i < 45)) {
				if(all == null || all.getType().equals(Material.AIR)) {
					inv.setItem(i, none);
				}
			}
			i ++;
		}
		p.openInventory(inv);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				p.updateInventory();
			}
		}, 2);
	}
	
	public static void openSettings(Player p) {
		ItemStack request = new ItemStack(Material.EMPTY_MAP);
		ItemMeta reqm = request.getItemMeta();
		reqm.setDisplayName("§eFreundschaftsanfragen");
		request.setItemMeta(reqm);
		
		ItemStack party = new ItemStack(Material.CAKE);
		ItemMeta pm = party.getItemMeta();
		pm.setDisplayName("§5Partyanfragen");
		party.setItemMeta(pm);
		
		ItemStack msg = new ItemStack(Material.BOOK_AND_QUILL);
		ItemMeta mm = msg.getItemMeta();
		mm.setDisplayName("§aPrivate Nachrichten");
		msg.setItemMeta(mm);
		
		ItemStack tele = new ItemStack(Material.ENDER_PEARL);
		ItemMeta tm = tele.getItemMeta();
		tm.setDisplayName("§6Nachjoinen");
		tele.setItemMeta(tm);
		
		ItemStack clan = new ItemStack(Material.LEATHER_HELMET);
		ItemMeta cm = clan.getItemMeta();
		cm.setDisplayName("§cClananfragen");
		clan.setItemMeta(cm);
		
		ItemStack notify = new ItemStack(Material.WATCH);
		ItemMeta nm = notify.getItemMeta();
		nm.setDisplayName("§9On-/Offline Nachrichten");
		notify.setItemMeta(nm);
		
		ItemStack activ = new ItemStack(Material.INK_SACK, 1, (short)10);
		ItemMeta am = activ.getItemMeta();
		am.setDisplayName("§aAktiviert");
		activ.setItemMeta(am);
		
		ItemStack disable = new ItemStack(Material.INK_SACK, 1, (short)8);
		ItemMeta disablem = disable.getItemMeta();
		disablem.setDisplayName("§7Deaktiviert");
		disable.setItemMeta(disablem);
		
		ItemStack back = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta bm = (SkullMeta) back.getItemMeta();
		bm.setDisplayName("§cZurück");
		bm.setOwner("MHF_ARROWLEFT");
		back.setItemMeta(bm);
		
		Inventory inv = Bukkit.createInventory(null, 36, "§c§lFreunde Einstellungen");
		
		inv.setItem(10, request);
		inv.setItem(11, party);
		inv.setItem(12, msg);
		inv.setItem(14, tele);
		inv.setItem(15, clan);
		inv.setItem(16, notify);
		inv.setItem(27, back);
		
		if(FriendSQL.getSetting(p.getUniqueId(), "REQUEST")) {
			inv.setItem(19, activ);
		} else {
			inv.setItem(19, disable);
		}
		
		if(FriendSQL.getSetting(p.getUniqueId(), "PARTY")) {
			inv.setItem(20, activ);
		} else {
			inv.setItem(20, disable);
		}
		
		if(FriendSQL.getSetting(p.getUniqueId(), "PRIVATEMSG")) {
			inv.setItem(21, activ);
		} else {
			inv.setItem(21, disable);
		}
		
		if(FriendSQL.getSetting(p.getUniqueId(), "TELEPORT")) {
			inv.setItem(23, activ);
		} else {
			inv.setItem(23, disable);
		}
		
		if(FriendSQL.getSetting(p.getUniqueId(), "CLAN")) {
			inv.setItem(24, activ);
		} else {
			inv.setItem(24, disable);
		}
		
		if(FriendSQL.getSetting(p.getUniqueId(), "NOTIFY")) {
			inv.setItem(25, activ);
		} else {
			inv.setItem(25, disable);
		}		
		p.openInventory(inv);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				p.updateInventory();
			}
		}, 2);
	}
	
	public static void openFriendMenu(Player p, String name) {
		UUID uuid = null;
		if(name != null) {
			uuid = PlayerData.getUUID(name);
		}
		
		ItemStack delete = new ItemStack(Material.BARRIER);
		ItemMeta dm = delete.getItemMeta();
		dm.setDisplayName("§cFreundschaft auflösen");
		delete.setItemMeta(dm);
		
		ItemStack party = new ItemStack(Material.CAKE);
		ItemMeta pm = party.getItemMeta();
		pm.setDisplayName("§5In eine Party einladen");
		party.setItemMeta(pm);
		
		ItemStack teleport = new ItemStack(Material.ENDER_PEARL);
		ItemMeta tm = teleport.getItemMeta();
		tm.setDisplayName("§6Nachjoinen");
		teleport.setItemMeta(tm);
		
		ItemStack clan = new ItemStack(Material.LEATHER_HELMET);
		ItemMeta cm = clan.getItemMeta();
		cm.setDisplayName("§cClananfrage senden");
		clan.setItemMeta(cm);
		
		ItemStack back = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta bm = (SkullMeta) back.getItemMeta();
		bm.setDisplayName("§cZurück");
		bm.setOwner("MHF_ARROWLEFT");
		back.setItemMeta(bm);
		
		Inventory inv = Bukkit.createInventory(null, 27, "§6§l" + name + "§§§§§§");
		
		inv.setItem(18, back);
		
		if(uuid != null && name != null && FriendSQL.isOnline(uuid)) {
			inv.setItem(10, delete);
			inv.setItem(12, party);
			inv.setItem(14, teleport);
			inv.setItem(16, clan);
		} else {
			inv.setItem(13, delete);
		}
		p.openInventory(inv);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				p.updateInventory();
			}
		}, 2);
	}
	
	public static void openRequests(Player p, int site) {
		ItemStack none = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
		ItemMeta nm = none.getItemMeta();
		nm.setDisplayName("§b");
		none.setItemMeta(nm);
		
		ItemStack nxtpage = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta nmeta = (SkullMeta) nxtpage.getItemMeta();
		nmeta.setDisplayName("§7§lNächste Seite »");
		nmeta.setOwner("MHF_ARROWRIGHT");
		nxtpage.setItemMeta(nmeta);
		
		ItemStack bpage = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta bmeta = (SkullMeta)bpage.getItemMeta();
		bmeta.setDisplayName("§7§l« Vorherige Seite");
		bmeta.setOwner("MHF_ARROWLEFT");
		bpage.setItemMeta(bmeta);
		
		ItemStack back = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta bm = (SkullMeta) back.getItemMeta();
		bm.setDisplayName("§cZurück");
		bm.setOwner("MHF_ARROWLEFT");
		back.setItemMeta(bm);
		
		ItemStack accept = new ItemStack(Material.STAINED_CLAY, 1, (short)5);
		ItemMeta am = accept.getItemMeta();
		am.setDisplayName("§aAlle Anfrage annehmen");
		accept.setItemMeta(am);
		
		ItemStack deny = new ItemStack(Material.STAINED_CLAY, 1, (short)14);
		ItemMeta dm = deny.getItemMeta();
		dm.setDisplayName("§cAlle Anfrage ablehnen");
		deny.setItemMeta(dm);
		
		ArrayList<String> all_friends = FriendSQL.getRequests(p.getUniqueId());
		ArrayList<String> site_friends = new ArrayList<String>();		
		int max = site * 27;
		int start = max - 27;
		
		int ia = 1;
		for(String all : all_friends) {
			String name = all; //UserData
			if(ia >= start) {
				site_friends.add(name);
			}
			ia ++;
		}
		
		Inventory inv = Bukkit.createInventory(null, 54, "§6§lOffene Anfragen §7Seite " + site);
		
		if(site < maxSitesRequest(p)) {
			inv.setItem(53, nxtpage);
		}
		if(site > 1) {
			inv.setItem(52, bpage);
		}
		
		inv.setItem(48, accept);
		inv.setItem(50, deny);
		
		int ifr = 9;
		for(String all : site_friends) {
			if(ifr > 35) {
				break;
			}
			if(site_friends.contains(all)) {
				ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
				SkullMeta ism = (SkullMeta)is.getItemMeta();
				ism.setOwner(all);
				ism.setDisplayName(PPermissions.getChatColor(PlayerData.getUUID(all)) + all);
				is.setItemMeta(ism);
				inv.setItem(ifr, is);
			}
			ifr ++;
		}
		
		inv.setItem(45, back);
		int i = 0;
		for(ItemStack all : inv.getContents()) {
			if(i < 9 || (i > 35 && i < 45)) {
				if(all == null) {
					inv.setItem(i, none);
				}
			}
			i ++;
		}
		p.openInventory(inv);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			@Override
			public void run() {
				p.updateInventory();
			}
		}, 2);
	}
	
	public static void openPlayerRequest(Player p, String name) {
		
		ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta ism = (SkullMeta) is.getItemMeta();
		ism.setOwner(name);
		ism.setDisplayName("§a" + name);
		is.setItemMeta(ism);
		
		ItemStack accept = new ItemStack(Material.STAINED_CLAY, 1, (short)5);
		ItemMeta am = accept.getItemMeta();
		am.setDisplayName("§aAnfrage annehmen");
		accept.setItemMeta(am);
		
		ItemStack deny = new ItemStack(Material.STAINED_CLAY, 1, (short)14);
		ItemMeta dm = deny.getItemMeta();
		dm.setDisplayName("§cAnfrage ablehnen");
		deny.setItemMeta(dm);
		
		ItemStack back = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta bm = (SkullMeta) back.getItemMeta();
		bm.setDisplayName("§cZurück");
		bm.setOwner("MHF_ARROWLEFT");
		back.setItemMeta(bm);
		
		Inventory inv = Bukkit.createInventory(null, 27, "§6§l" + name + "§§§§");
		
		inv.setItem(4, is);
		inv.setItem(12, accept);
		inv.setItem(14, deny);
		inv.setItem(18, back);
		
		p.openInventory(inv);
		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				p.updateInventory();
			}
		}, 2);
	}
	
	public static void sendGlobalMessage(String func, Player p1, String p2) {
		if(func.equalsIgnoreCase("requestaccept")) {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			try{
				out.writeUTF("friend_legitfinn");
				out.writeUTF(connectionCode);
				out.writeUTF("requestaccept");
				out.writeUTF(p1.getName() + ";" + p2);
				p1.sendPluginMessage(main.getPlugin(main.class), "BungeeCord", b.toByteArray());
			} catch(IOException ex){}
		} else if(func.equalsIgnoreCase("requestdeny")) {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			try{
				out.writeUTF("friend_legitfinn");
				out.writeUTF(connectionCode);
				out.writeUTF("requestdeny");
				out.writeUTF(p1.getName() + ";" + p2);
				p1.sendPluginMessage(main.getPlugin(main.class), "BungeeCord", b.toByteArray());
			} catch(IOException ex){}
		} else if(func.equalsIgnoreCase("remove")) {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			try{
				out.writeUTF("friend_legitfinn");
				out.writeUTF(connectionCode);
				out.writeUTF("remove");
				out.writeUTF(p1.getName() + ";" + p2);
				p1.sendPluginMessage(main.getPlugin(main.class), "BungeeCord", b.toByteArray());
			} catch(IOException ex){}
		} else if(func.equalsIgnoreCase("teleport")) {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			try{
				out.writeUTF("friend_legitfinn");
				out.writeUTF(connectionCode);
				out.writeUTF("teleport");
				out.writeUTF(p1.getName() + ";" + p2);
				p1.sendPluginMessage(main.getPlugin(main.class), "BungeeCord", b.toByteArray());
			} catch(IOException ex){}
		} else if(func.equalsIgnoreCase("party")) {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			try{
				out.writeUTF("friend_legitfinn");
				out.writeUTF(connectionCode);
				out.writeUTF("party");
				out.writeUTF(p1.getName() + ";" + p2);
				p1.sendPluginMessage(main.getPlugin(main.class), "BungeeCord", b.toByteArray());
			} catch(IOException ex){}
		} else if(func.equalsIgnoreCase("addFriend")) {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			try{
				out.writeUTF("friend_legitfinn");
				out.writeUTF(connectionCode);
				out.writeUTF("addfriend");
				out.writeUTF(p1.getName() + ";" + p2);
				p1.sendPluginMessage(main.getPlugin(main.class), "BungeeCord", b.toByteArray());
			} catch(IOException ex) {}
		}	
	}
}
