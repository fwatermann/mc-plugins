package de.legitfinn.knockbackffa.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Main.Main;
import de.NightShadwo.SignAPI.Main.Types.InformationTyps;
import de.legitfinn.NickApi.main.NickModul;
import de.legitfinn.knockbackffa.SQL.LobbySettingsSQL;
import de.legitfinn.knockbackffa.main.DataManager;
import de.legitfinn.knockbackffa.main.main;
import de.legitfinn.knockbackffa.methoden.ConfigManager;
import de.legitfinn.knockbackffa.methoden.ScoreboardSystem;
import de.legitfinn.ppermissions.main.PPermissions;
import de.legitfinn.replaysystem.main.ReplayAPI;

public class JoinListener implements Listener {

	@EventHandler
	public void onLogin(PlayerLoginEvent e) {
		if(e.getPlayer().hasPermission("nick.allow")) {
			if(LobbySettingsSQL.getSettingsBoolean(e.getPlayer().getUniqueId(), "NICK")) {
				NickModul.nickPlayerLogin(e.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		if(NickModul.isNicked(e.getPlayer().getUniqueId())) {
			ReplayAPI.getInstance().broadCastMessage("§7» " + PPermissions.getLowestChatColor() + e.getPlayer().getName() + " §3hat das Spiel betreten.");
		} else {
			ReplayAPI.getInstance().broadCastMessage("§7» " + PPermissions.getChatColor(e.getPlayer().getUniqueId()) + e.getPlayer().getName() + " §3hat das Spiel betreten.");
		}
		if(!DataManager.loadingMap) {
			e.getPlayer().teleport(ConfigManager.getSpawn());
		} else {
			e.getPlayer().teleport(Bukkit.getWorlds().get(0).getSpawnLocation().clone().add(0.5, 0.5, 0.5));
		}
		addItems(e.getPlayer());
		SignAPI.sendChange(InformationTyps.PLAYERS, Bukkit.getServer().getOnlinePlayers().size() + "", "own");
		e.getPlayer().setGameMode(GameMode.ADVENTURE);
		ScoreboardSystem.createScoreboard(e.getPlayer());
		for(Player all : Bukkit.getOnlinePlayers()) {
			ScoreboardSystem.updateScoreboard(all, true);
			all.hidePlayer(e.getPlayer());
		}
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				for(Player all : Bukkit.getOnlinePlayers()) {
					all.showPlayer(e.getPlayer());
				}
			}
		}, 5);
	}
	
	public static void addItems(Player p) {
		ItemStack leave = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta lm = (SkullMeta) leave.getItemMeta();
		lm.setOwner("MHF_ARROWRIGHT");
		lm.setDisplayName("§7» §cSpiel verlassen §7§o<Rechtsklick>");
		leave.setItemMeta(lm);
		
		ItemStack kits = new ItemStack(Material.CHEST);
		ItemMeta km = kits.getItemMeta();
		km.setDisplayName("§7» §aAusrüstungen §7§o<Rechtsklick>");
		kits.setItemMeta(km);
		
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.getInventory().setItem(0, kits);
		p.getInventory().setItem(8, leave);
	}
	
	
	
	
	
	
}
