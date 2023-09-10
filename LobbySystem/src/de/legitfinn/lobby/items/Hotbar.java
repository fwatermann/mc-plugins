package de.legitfinn.lobby.items;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.legitfinn.lobby.SQL.LobbyServerSQL;
import de.legitfinn.lobby.SQL.LobbySettingsSQL;
import de.legitfinn.lobby.main.main;
import de.legitfinn.lobby.scoreboard.ScoreboardSystem;
import de.legitfinn.ppermissions.main.PPermissions;

public class Hotbar implements Listener {

	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		if(!LobbyServerSQL.playerListed(e.getPlayer().getUniqueId())) {
			LobbyServerSQL.listPlayer(e.getPlayer().getUniqueId(), Bukkit.getWorld("world").getSpawnLocation(), false);
		}
		if(LobbyServerSQL.getInSilent(e.getPlayer().getUniqueId())) {
			if(!main.isSilentLobby) {
				Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
					@Override
					public void run() {
						System.out.println("Silentplayer: " + e.getPlayer().getName());
						e.getPlayer().sendMessage("§4§lSilent §7» §5Du bist weiterhin in der §a§oSilentlobby.");
						SilentHub.connectToSilentLobby(e.getPlayer());
					}
				}, 1);
				return;
			}
		}
		Location loc = LobbyServerSQL.getLobbyLocation(e.getPlayer().getUniqueId(), e.getPlayer().getLocation().getWorld());
		e.getPlayer().teleport(loc.clone().add(0, 0.5, 0));
		
		ScoreboardSystem.createScoreboard(e.getPlayer());
		for(Player all : Bukkit.getOnlinePlayers()) {
			ScoreboardSystem.updateScoreboard(all, true, false);
		}
		createHotbar(e.getPlayer());
		if(main.isSilentLobby) {
			for(Player all : Bukkit.getOnlinePlayers()) {
				if(!all.equals(e.getPlayer())) {
					all.hidePlayer(e.getPlayer());
					e.getPlayer().hidePlayer(all);
				}
			}
		}
	}
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(!all.equals(e.getPlayer())) {
				ScoreboardSystem.updateScoreboard(all, true, false);
			}
		}
		e.getPlayer().getInventory().clear();
		e.getPlayer().getInventory().setArmorContents(null);
		e.setQuitMessage(null);
	}
	
	public static void createHotbar(Player p) {
		if(PPermissions.isInTeamGroup(p) || PPermissions.getShowGroup(p).equalsIgnoreCase("youtuber")) {
			openYouTuber(p);
		} else if(PPermissions.getShowGroup(p).equalsIgnoreCase("Premium+")) {
			openPremiumPlus(p);
		} else {
			openPlayer(p);
		}
		p.setGameMode(GameMode.ADVENTURE);
	}
	
	public static void openYouTuber(Player p) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setLevel(0);
		p.setExp(0.0F);
		
		ItemStack tele = new ItemStack(Material.COMPASS);
		ItemMeta tm = tele.getItemMeta();
		tm.setDisplayName("§6§lNavigator");
		tele.setItemMeta(tm);
		
		ItemStack stati = new ItemStack(Material.BOOK);
		ItemMeta sm = stati.getItemMeta();
		sm.setDisplayName("§9§lStatistiken");
		stati.setItemMeta(sm);
		
		ItemStack silent = new ItemStack(Material.TNT);
		ItemMeta sim = silent.getItemMeta();
		if(main.isSilentLobby) {
			sim.setDisplayName("§a§lPublicLobby");
		} else {
			sim.setDisplayName("§c§lSilentlobby");
		}
		silent.setItemMeta(sim);
		
		ItemStack gadgets = new ItemStack(Material.CHEST);
		ItemMeta gm = gadgets.getItemMeta();
		gm.setDisplayName("§b§lGadgets");
		gadgets.setItemMeta(gm);
		
		ItemStack nick = new ItemStack(Material.NAME_TAG);
		ItemMeta nm = nick.getItemMeta();
		if(!LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "NICK")) {
			nm.setDisplayName("§c⬛⬛⬛ §5§lAutomatischer Nickname §c⬛⬛⬛");
		} else {
			nm.setDisplayName("§a⬛⬛⬛ §5§lAutomatischer Nickname §a⬛⬛⬛");
		}
		nick.setItemMeta(nm);
		
		ItemStack settings = new ItemStack(Material.REDSTONE_COMPARATOR);
		ItemMeta sem = settings.getItemMeta();
		sem.setDisplayName("§c§lEinstellungen");
		settings.setItemMeta(sem);
		
		ItemStack friends = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta fm = (SkullMeta) friends.getItemMeta();
		fm.setOwner(p.getName());
		fm.setDisplayName("§6§lFreunde");
		friends.setItemMeta(fm);
		
		Inventory inv = p.getInventory();
		inv.setItem(0, tele);
		inv.setItem(1, stati);
		inv.setItem(3, silent);
		inv.setItem(4, gadgets);
		inv.setItem(5, nick);
		inv.setItem(7, settings);
		inv.setItem(8, friends);
	}
	
	public static void openPremiumPlus(Player p) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setLevel(0);
		p.setExp(0.0F);
		
		ItemStack tele = new ItemStack(Material.COMPASS);
		ItemMeta tm = tele.getItemMeta();
		tm.setDisplayName("§6§lNavigator");
		tele.setItemMeta(tm);
		
		ItemStack stati = new ItemStack(Material.BOOK);
		ItemMeta sm = stati.getItemMeta();
		sm.setDisplayName("§9§lStatistiken");
		stati.setItemMeta(sm);
		
		ItemStack gadgets = new ItemStack(Material.CHEST);
		ItemMeta gm = gadgets.getItemMeta();
		gm.setDisplayName("§b§lGadgets");
		gadgets.setItemMeta(gm);
		
		ItemStack nick = new ItemStack(Material.NAME_TAG);
		ItemMeta nm = nick.getItemMeta();
		if(!LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "NICK")) {
			nm.setDisplayName("§c⬛⬛⬛ §5§lAutomatischer Nickname §c⬛⬛⬛");
		} else {
			nm.setDisplayName("§a⬛⬛⬛ §5§lAutomatischer Nickname §a⬛⬛⬛");
		}
		nick.setItemMeta(nm);
		
		ItemStack settings = new ItemStack(Material.REDSTONE_COMPARATOR);
		ItemMeta sem = settings.getItemMeta();
		sem.setDisplayName("§c§lEinstellungen");
		settings.setItemMeta(sem);
		
		ItemStack friends = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta fm = (SkullMeta) friends.getItemMeta();
		fm.setOwner(p.getName());
		fm.setDisplayName("§6§lFreunde");
		friends.setItemMeta(fm);
		
		Inventory inv = p.getInventory();
		inv.setItem(0, tele);
		inv.setItem(1, stati);
		inv.setItem(4, gadgets);
		inv.setItem(5, nick);
		inv.setItem(7, settings);
		inv.setItem(8, friends);
	}
	
	public static void openPlayer(Player p) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.setLevel(0);
		p.setExp(0.0F);
		
		ItemStack tele = new ItemStack(Material.COMPASS);
		ItemMeta tm = tele.getItemMeta();
		tm.setDisplayName("§6§lNavigator");
		tele.setItemMeta(tm);
		
		ItemStack stati = new ItemStack(Material.BOOK);
		ItemMeta sm = stati.getItemMeta();
		sm.setDisplayName("§9§lStatistiken");
		stati.setItemMeta(sm);
		
		ItemStack silent = new ItemStack(Material.TNT);
		ItemMeta sim = silent.getItemMeta();
		sim.setDisplayName("§c§lSilentlobby");
		silent.setItemMeta(sim);
		
		ItemStack gadgets = new ItemStack(Material.CHEST);
		ItemMeta gm = gadgets.getItemMeta();
		gm.setDisplayName("§b§lGadgets");
		gadgets.setItemMeta(gm);
		
		ItemStack nick = new ItemStack(Material.NAME_TAG);
		ItemMeta nm = nick.getItemMeta();
		if(!LobbySettingsSQL.getSettingsBoolean(p.getUniqueId(), "NICK")) {
			nm.setDisplayName("§c⬛⬛⬛ §5§lAutomatischer Nickname §c⬛⬛⬛");
		} else {
			nm.setDisplayName("§a⬛⬛⬛ §5§lAutomatischer Nickname §a⬛⬛⬛");
		}
		nick.setItemMeta(nm);
		
		ItemStack settings = new ItemStack(Material.REDSTONE_COMPARATOR);
		ItemMeta sem = settings.getItemMeta();
		sem.setDisplayName("§c§lEinstellungen");
		settings.setItemMeta(sem);
		
		ItemStack friends = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta fm = (SkullMeta) friends.getItemMeta();
		fm.setOwner(p.getName());
		fm.setDisplayName("§6§lFreunde");
		friends.setItemMeta(fm);
		
		Inventory inv = p.getInventory();
		inv.setItem(0, tele);
		inv.setItem(1, stati);
		inv.setItem(4, gadgets);
		inv.setItem(7, settings);
		inv.setItem(8, friends);
	}
	
	
	
	
	
	
	
}
