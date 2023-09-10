package de.legitfinn.bedwars.listener;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import de.NightShadwo.SignAPI.Main.Types.Status;
import de.legitfinn.bedwars.main.BedWars;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.ConfigManager;
import de.legitfinn.bedwars.methoden.GameManager;
import de.legitfinn.bedwars.methoden.GameState;
import de.legitfinn.bedwars.methoden.MapData;
import de.legitfinn.bedwars.methoden.ScoreboardSystem;
import de.legitfinn.bedwars.methoden.TeamManager;
import de.legitfinn.bedwars.shop.Shop;
import de.legitfinn.bedwars.sql.StatisticSQL;
import de.legitfinn.replaysystem.main.ReplayAPI;

public class RespawnListener implements Listener {

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		if(DataManager.state.equals(GameState.Ingame)) {
			String team = TeamManager.getTeamOfPlayer(e.getPlayer());
			if(team != null) {
				if(DataManager.team_bed.get(team)) {
					 e.setRespawnLocation(MapData.getSpawn(team));
				} else {
					DataManager.team_players.get(team).remove(e.getPlayer());
					ScoreboardSystem.createIngameScoreboard();
					if(DataManager.team_players.get(team).size() == 0) {
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + TeamManager.getTeamColor(team) + team + " §3ist ausgeschieden!");
						if(TeamManager.getLivingTeams() > 1) {
							ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "Es sind noch §e" + TeamManager.getLivingTeams() + " Teams §3am Leben.");
						} else {
							ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "Es ist noch §e" + TeamManager.getLivingTeams() + " Team §3am Leben.");
						}
					}
					if(GameManager.isEnd()) {
						GameManager.endGame(false);
						e.setRespawnLocation(ConfigManager.getLobbySpawn());
						e.getPlayer().teleport(ConfigManager.getLobbySpawn());
					} else {
						toSpectator(e.getPlayer());
						e.setRespawnLocation(MapData.getSpectator());
					}
				}
			}
		} else {
			e.setRespawnLocation(ConfigManager.getLobbySpawn());
		}
	}

	public static void toSpectator(Player p) {
		Bukkit.getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
			
			@Override
			public void run() {
				if(DataManager.state.equals(Status.SHUTDOWN)) {
					return;
				}
				Team t = Bukkit.getServer().getScoreboardManager().getMainScoreboard().getEntryTeam(p.getName());
				if(t != null) {
					t.removeEntry(p.getName());
				}
				p.setAllowFlight(true);
				p.setFlying(true);
				p.teleport(MapData.getSpectator());
				p.spigot().setCollidesWithEntities(false);
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999, 2));					
				Bukkit.getServer().getScoreboardManager().getMainScoreboard().getTeam("Spectator").addEntry(p.getName());
				ItemStack compass = Shop.getItemStack(Material.COMPASS, 1, (short)0, "§7» §cTeleporter", null);
				p.getInventory().setItem(0, compass);	
				for(Player all : Bukkit.getOnlinePlayers()) {
					if(!TeamManager.isSpectator(all)) {
						all.hidePlayer(p);
					}
				}
			}
		}, 10);
	}
	
	
	public static HashMap<Player, Player> lastDamager = new HashMap<Player, Player>();
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if(DataManager.state.equals(GameState.Ingame)) {
			String team = TeamManager.getTeamOfPlayer(e.getEntity());
			if(team != null) {
				if(e.getEntity().getKiller() == null) {
					if(lastDamager.containsKey(e.getEntity())) {
						Player killer = lastDamager.get(e.getEntity());
						if(DataManager.troll.contains(killer)) {
							ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + TeamManager.getTeamColor(team) + e.getEntity().getName() + " §3wurde von §b§lxXTheTrollerXx §3getötet.");
						} else {
							ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + TeamManager.getTeamColor(team) + e.getEntity().getName() + " §3wurde von " + TeamManager.getTeamColor(TeamManager.getTeamOfPlayer(killer)) + killer.getName() + " §3getötet.");
							if(!DataManager.team_bed.get(team)) {
								StatisticSQL.addInt("BEDWARS", "KILLS", 1, killer.getUniqueId());
								StatisticSQL.addInt("BEDWARS", "DEATH", 1, e.getEntity().getUniqueId());
								killer.playSound(killer.getLocation(), Sound.LEVEL_UP, 1, 1);
							}
						}
						lastDamager.remove(e.getEntity());
					} else {
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + TeamManager.getTeamColor(team) + e.getEntity().getName() + " §3ist gestorben.");
					}
				} else {
					if(DataManager.troll.contains(e.getEntity().getKiller())) {
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + TeamManager.getTeamColor(team) + e.getEntity().getName() + " §3wurde von §b§lxXTheTrollerXx §3getötet.");
					} else {
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + TeamManager.getTeamColor(team) + e.getEntity().getName() + " §3wurde von " + TeamManager.getTeamColor(TeamManager.getTeamOfPlayer(e.getEntity().getKiller())) + e.getEntity().getKiller().getName() + " §3getötet.");
						if(!DataManager.team_bed.get(team)) {
							StatisticSQL.addInt("BEDWARS", "KILLS", 1, e.getEntity().getKiller().getUniqueId());
							StatisticSQL.addInt("BEDWARS", "DEATH", 1, e.getEntity().getUniqueId());
							e.getEntity().getKiller().playSound(e.getEntity().getKiller().getLocation(), Sound.LEVEL_UP, 1, 1);
						}
					}
				}
			}
		}
		e.getDrops().clear();
		e.setDroppedExp(0);
		e.setDeathMessage(null);
		Bukkit.getServer().getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
			@Override
			public void run() {
				e.getEntity().spigot().respawn();
			}
		}, 5);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(DataManager.state.equals(GameState.Ingame)) {
			if(e.getEntity() instanceof Player) {
				Player ent = (Player)e.getEntity();
				if(e.getDamager() instanceof Player) {
					Player dmg = (Player)e.getDamager();
					if(TeamManager.getTeamOfPlayer(ent).equals(TeamManager.getTeamOfPlayer(dmg)) && !DataManager.troll.contains(dmg)) {
						e.setCancelled(true);
						return;
					}
					if(!TeamManager.isSpectator(dmg) || DataManager.troll.contains(dmg)) {
						lastDamager.put(ent, dmg);
					} else {
						e.setCancelled(true);
					}
				} else if(e.getDamager() instanceof Arrow) {
					Arrow a = (Arrow)e.getDamager();
					if(a.getShooter() instanceof Player) {
						Player dmg = (Player)a.getShooter();
						if(TeamManager.getTeamOfPlayer(dmg).equals(TeamManager.getTeamOfPlayer(ent))) {
							e.setCancelled(true);
							a.remove();
						}
						lastDamager.put(ent, dmg);
					}
				}
			}
		}
	}
	
	
	
	
}
