package de.legitfinn.bedwars.commands;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.TeamManager;

public class COMMAND_troll implements CommandExecutor, Listener { 
	
	public static String trollPrefix = "§cTroll §7» §3";
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender.hasPermission("bedwars.troll")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				if(args.length == 0) {
					sendTrollHelp(p);
				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("on") || args[0].equalsIgnoreCase("off")) {
						if(args[0].equalsIgnoreCase("on")) {
							DataManager.troll.remove(p);
							DataManager.troll.add(p);
							p.sendMessage(trollPrefix + "Trollmodus §aaktiviert§3.");
							for(Player all : Bukkit.getOnlinePlayers()) {
								if(!all.equals(p)) {
									all.hidePlayer(p);
								}
							}
						} else if(args[0].equalsIgnoreCase("off")) {
							DataManager.troll.remove(p);
							p.sendMessage(trollPrefix + "Trollmodus §cdeaktiviert§3.");
							for(Player all : Bukkit.getOnlinePlayers()) {
								if(!all.equals(p)) {
									all.showPlayer(p);
								}
							}
						}
					} else if(args[0].equalsIgnoreCase("granate")) {
						if(DataManager.troll.contains(p)) {
							p.sendMessage(trollPrefix + "Granate wurde gespawnt.");
							Fireball fb = p.getLocation().getWorld().spawn(p.getLocation().clone().add(p.getLocation().getDirection().multiply(1).normalize()), Fireball.class);
							fb.setCustomName("TrollGranate");
							fb.setCustomNameVisible(false);
						} else {
							p.sendMessage(trollPrefix + "Du musst erst den Trollmodus aktivieren.");
						}
					} else {
						sendTrollHelp(p);
					}
 				} else if(args.length == 2) {
 					if(args[0].equalsIgnoreCase("crazy")) {
 						Player sp = Bukkit.getPlayer(args[1]);
 						if(sp != null && sp.isOnline()) {
 							if(!crazy_lastLoc.containsKey(sp)) {
 								crazy_lastLoc.put(sp, sp.getLocation().clone());
 								startCrazy();
 								p.sendMessage(trollPrefix + "§e§o" + sp.getName() + " §3ist nun §cCRAZY§3!");
 								sp.setAllowFlight(true);
 								sp.setFlying(true);
 								sp.setGameMode(GameMode.SURVIVAL);
 							} else {
 								sp.teleport(crazy_lastLoc.get(sp));
 								sp.setGameMode(GameMode.SURVIVAL);
 								crazy_lastLoc.remove(sp);
 								p.sendMessage(trollPrefix + "§e§o" + sp.getName() + " §3ist nun nicht mehr §cCRAZY§3!");
 								String team = TeamManager.getTeamOfPlayer(sp);
 								if(team != null) {
 									sp.setAllowFlight(false);
 									sp.setFlying(false);
 								}
 							}
 						}
 					}
 				}
			}
		} else {
			sender.sendMessage(Bukkit.getServer().spigot().getConfig().getString("messages.whitelist"));
		}
		return true;
	}

	public static void sendTrollHelp(Player p) {
		p.sendMessage("§b☰☰☰☰☰☰☰⚌⚌⚌⚊⚊§c TrollSystem §b⚊⚊⚌⚌⚌☰☰☰☰☰☰☰");
		p.sendMessage("");
		p.sendMessage("§c/troll <on/off> §7» §cDe-/Aktiviert den Trollmodus.");
		p.sendMessage("§c/troll granate §7» §cSpawnt eine Granate.");
		p.sendMessage("§c/troll crazy <player> §7» §cLässt einen Spieler crazy werden.");
		p.sendMessage("");
		p.sendMessage("§b☰☰☰☰☰☰☰⚌⚌⚌⚊⚊§c TrollSystem §b⚊⚊⚌⚌⚌☰☰☰☰☰☰☰");
		
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(crazy_lastLoc.containsKey(e.getPlayer())) {
			if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getY() != e.getTo().getY() || e.getFrom().getZ() != e.getTo().getZ()) {
				e.getPlayer().teleport(e.getFrom());
			}
		}
	}
	
	@EventHandler
	public void onFlight(PlayerToggleFlightEvent e) {
		if(crazy_lastLoc.containsKey(e.getPlayer())) {
			e.setCancelled(true);
		}
	}
	
	public static boolean isCrazyRunning = false;
	public static HashMap<Player, Location> crazy_lastLoc = new HashMap<Player, Location>();
	
	public static void startCrazy() {
		if(isCrazyRunning) {
			return;
		}
		isCrazyRunning = true;
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(crazy_lastLoc.size() > 0) {
					for(Player all : crazy_lastLoc.keySet()) {
						Random rnd = new Random();
						int i = rnd.nextInt(5);
						if(i == 0) {
							all.teleport(crazy_lastLoc.get(all).clone().add(0, 2, 0));
						} else if(i == 1) {
							all.teleport(crazy_lastLoc.get(all).clone().add(0, -2, 0));
						} else if(i == 2) {
							all.teleport(crazy_lastLoc.get(all).clone().add(0, 1, 3));
						} else if(i == 3) {
							all.teleport(crazy_lastLoc.get(all).clone().add(3, 1, 0));
						} else if(i == 4) {
							all.teleport(crazy_lastLoc.get(all).clone().add(3, -1, 3));
						} else if(i == 5) {
							all.teleport(crazy_lastLoc.get(all).clone().add(-3, -2, -1));
						}
					}
					try{
						Thread.sleep(500);
					} catch(InterruptedException ex){}
				}
				isCrazyRunning = false;
				return;
			}
		});
		th.start();
	}
	
	
	
	
	
}
