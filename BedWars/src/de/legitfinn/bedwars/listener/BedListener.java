package de.legitfinn.bedwars.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.GameState;
import de.legitfinn.bedwars.methoden.ScoreboardSystem;
import de.legitfinn.bedwars.methoden.TeamManager;
import de.legitfinn.bedwars.sql.StatisticSQL;
import de.legitfinn.replaysystem.main.ReplayAPI;

public class BedListener implements Listener {
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(DataManager.state.equals(GameState.Ingame)) {
			if(e.getBlock().getType().equals(Material.BED_BLOCK)) {
				String team = getTeamOfBed(e.getBlock());
				if(TeamManager.isSpectator(e.getPlayer())) {
					e.setCancelled(true);
					return;
				}
				if(team != null) {
					if(!team.equalsIgnoreCase(TeamManager.getTeamOfPlayer(e.getPlayer()))) {
						if(DataManager.team_bed.get(team)) {
							for(Player all : Bukkit.getOnlinePlayers()) {
								all.playSound(all.getLocation(), Sound.ENDERMAN_DEATH, 100, 1);
							}
							ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "§3Das Bett von Team " + TeamManager.getTeamColor(team) + team + " §3wurde von " + TeamManager.getTeamColor(TeamManager.getTeamOfPlayer(e.getPlayer())) + e.getPlayer().getName() + " §c§ozerstört§3.");
							StatisticSQL.addInt("BEDWARS", "BEDS", 1, e.getPlayer().getUniqueId());
							e.setCancelled(true);
							DataManager.team_bedloc.get(team)[0].getBlock().setType(Material.AIR);
							DataManager.team_bedloc.get(team)[1].getBlock().setType(Material.AIR);
							DataManager.team_bed.put(team, false);
							ScoreboardSystem.createIngameScoreboard();
						}
					} else {
						e.setCancelled(true);
						e.getPlayer().sendMessage(DataManager.prefix + "§cDu kannst dein §e§oeigenes Bett§c nicht zerstören! O.o");
					}
				} else {
					e.setCancelled(true);
				}
			}
		}
	}
	
	public static String getTeamOfBed(Block b) {
		Location loc = b.getLocation();
		
		for(String all : DataManager.team_bedloc.keySet()) {
			Location loc1 = DataManager.team_bedloc.get(all)[0];
			if(loc1.equals(loc)) {
				return all;
			}
			Location loc2 = DataManager.team_bedloc.get(all)[1];
			if(loc2.equals(loc)) {
				return all;
			}
		}
		return null;
	}
	
	
	
	

}
