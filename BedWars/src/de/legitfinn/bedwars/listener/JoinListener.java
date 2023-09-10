package de.legitfinn.bedwars.listener;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Main.Types.InformationTyps;
import de.legitfinn.NickApi.main.NickModul;
import de.legitfinn.bedwars.main.BedWars;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.ConfigManager;
import de.legitfinn.bedwars.methoden.Countdown;
import de.legitfinn.bedwars.methoden.GameState;
import de.legitfinn.bedwars.methoden.Hologram;
import de.legitfinn.bedwars.methoden.MapData;
import de.legitfinn.bedwars.shop.Shop;
import de.legitfinn.bedwars.sql.LobbySettingsSQL;
import de.legitfinn.bedwars.sql.StatisticSQL;
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
		if(DataManager.state.equals(GameState.Waiting)) {
			ItemStack team = new ItemStack(Material.BANNER);
			BannerMeta tm = (BannerMeta) team.getItemMeta();
			tm.setBaseColor(DyeColor.WHITE);
			tm.setDisplayName("§7» §cWähle dein Team §7§o<Rechtsklick>");
			team.setItemMeta(tm);
			
			ItemStack leave = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
			SkullMeta lm = (SkullMeta) leave.getItemMeta();
			lm.setDisplayName("§7» §cRunde verlassen §7§o<Rechtsklick>");
			lm.setOwner("MHF_ARROWRIGHT");
			leave.setItemMeta(lm);
			
			e.getPlayer().getInventory().clear();
			e.getPlayer().getInventory().setArmorContents(null);
			e.getPlayer().getInventory().setItem(0, team);
			e.getPlayer().getInventory().setItem(8, leave);
			Bukkit.getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
				
				@Override
				public void run() {
					e.getPlayer().updateInventory();
				}
			}, 2);
			Countdown.start();
			if(!NickModul.isNicked(e.getPlayer().getUniqueId())) {
				ReplayAPI.getInstance().broadCastMessage("§7» " + PPermissions.getChatColor(e.getPlayer()) + e.getPlayer().getName() + " §3hat die Runde §a§obetreten§3.");
			} else {
				ReplayAPI.getInstance().broadCastMessage("§7» " + PPermissions.getLowestChatColor() + e.getPlayer().getName() + " §3hat die Runde §a§obetreten§3.");
			}
			e.getPlayer().teleport(ConfigManager.getLobbySpawn());
			Scoreboard sc = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
			Team old = sc.getEntryTeam(e.getPlayer().getName());
			if(old != null) {
				old.removeEntry(e.getPlayer().getName());
			}
			sc.getTeam("noTeam").addEntry(e.getPlayer().getName());
			spawnHolo(e.getPlayer());
		} else if(DataManager.state.equals(GameState.Ingame)) {
			for(String teams : DataManager.team_players.keySet()) {
				for(Player all : DataManager.team_players.get(teams)) {
					all.hidePlayer(e.getPlayer());
				}
			}
			e.getPlayer().setAllowFlight(true);
			e.getPlayer().setFlying(true);
			e.getPlayer().teleport(MapData.getSpectator());
			e.getPlayer().spigot().setCollidesWithEntities(false);
			e.getPlayer().getInventory().clear();
			e.getPlayer().getInventory().setArmorContents(null);
			e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 2));
			
			ItemStack compass = Shop.getItemStack(Material.COMPASS, 1, (short)0, "§7» §cTeleporter", null);
			e.getPlayer().getInventory().setItem(0, compass);	
			Bukkit.getServer().getScoreboardManager().getMainScoreboard().getTeam("Spectator").addEntry(e.getPlayer().getName());
		}
		SignAPI.sendChange(InformationTyps.PLAYERS, Bukkit.getServer().getOnlinePlayers().size() + "", "own");
	}
	
	public static String getKd(int kills, int death) {
		
		String ret = kills + "";
		if(death > 0){
			double kd = (double) kills / (double) death;
			kd = Math.round(kd * 100) / 100;
			ret = kd + "";
		}
		
		
		return ret;
		
//		String ret = "0,00";
//		double a = (double)kills / (double)death;
//
//		ret = a + "";
//		String[] b = ret.split("\\.");
//		String c = "";
//		if(b.length > 1) {
//			c = b[0] + ",";
//			if(b[1].length() > 2) {
//				c += b[1].substring(0, 1);
//			} else {
//				c += b[1];
//			}
//		} else {
//			c = b[0] + ",00";
//		}
//		ret = c;	
//		
//		return ret;
	}
	
	public static String getWinProbability(int wins, int games) {
		String ret = wins + "%";
		if(games > 0){
			double kd = (double) wins / (double) games;
			kd = Math.round(kd * 100) / 100;
			ret = kd + "%";
		}
		
		
		return ret;
//		String ret = "0,00";
//		
//		if(games != 0 && wins != 0) {
//			double a = (double)((double)wins/(double)games) * 100;
//			if(a > 100) {
//				a = 100;
//				ret = "100,00";
//			} else if(a <= 0) {
//				a = 0;
//				ret = "0,00";
//			} else {
//				ret = a + "";
//				String[] b = ret.split("\\.");
//				String c = "";
//				if(b.length > 1) {
//					c = b[0] + ",";
//					if(b[1].length() > 2) {
//						c += b[1].substring(0, 1);
//					} else {
//						c += b[1];
//					}
//				}
//				ret = c;
//			}
//		} else {
//			ret = "0,00";
//		}
//		return ret + "%";
	}
	
	public static void spawnHolo(Player p) {
		String name = p.getName();
		if(NickModul.isNicked(p.getUniqueId())) {
			name = de.legitfinn.NickApi.main.main.uuid_name.get(p.getUniqueId());
		}
		String[] lines = new String[]{"§8●» §6Statistiken von §7" + name + " §8«●"
				, "§6Kills: §7" + StatisticSQL.getInt("BEDWARS", "KILLS", p.getUniqueId())
				, "§6Death: §7" + StatisticSQL.getInt("BEDWARS", "DEATH", p.getUniqueId())
				, "§6K/D: §7" + getKd(StatisticSQL.getInt("BEDWARS", "KILLS", p.getUniqueId()), StatisticSQL.getInt("BEDWARS", "DEATH", p.getUniqueId()))
				, "§6zerstörte Betten: §7" + StatisticSQL.getInt("BEDWARS", "BEDS", p.getUniqueId())
				, "§6Wins: §7" + StatisticSQL.getInt("BEDWARS", "WINS", p.getUniqueId())
				, "§6Games: §7" + StatisticSQL.getInt("BEDWARS", "GAMES", p.getUniqueId())
				, "§6Sieg zu §7" + getWinProbability(StatisticSQL.getInt("BEDWARS", "WINS", p.getUniqueId()), StatisticSQL.getInt("BEDWARS", "GAMES", p.getUniqueId()))};
		Hologram holo = new Hologram(p, lines, ConfigManager.getStatsLocation().add(0.5, -0.5, 0.5));
		DataManager.statisticholo.put(p, holo);
		holo.create();
	}
	
}
