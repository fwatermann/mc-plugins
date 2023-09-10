package de.legitfinn.bedwars.commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.legitfinn.bedwars.main.BedWars;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.MapData;
import de.legitfinn.bedwars.methoden.SpawnerType;

public class COMMAND_bedwars implements CommandExecutor {

	public static ArrayList<Player> inCreate = new ArrayList<Player>();
	public static HashMap<Player, String> mapSetup = new HashMap<Player, String>();
	public static HashMap<Player, HashMap<String, ChatColor>> player_mapSetup = new HashMap<Player, HashMap<String, ChatColor>>();
	public static HashMap<Player, Integer> player_beds = new HashMap<Player, Integer>();
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender.hasPermission("bedwars.setup")) {
			if(sender instanceof Player) {
				Player p = (Player)sender;
				
				if(args.length == 0) {
					p.sendMessage(DataManager.prefix + "☲☲☲☲☲☲☲☲☲☲☲☲☲⚊ §eHilfe §9⚊☲☲☲☲☲☲☲☲☲☲☲☲☲☲");
					p.sendMessage(DataManager.prefix);
					p.sendMessage(DataManager.prefix + "§e/bedwars createMap <MapName> <Teams> <PperTeam> \n" + DataManager.prefix + "  §e<Builder> §7» <Builder> Leerzeichen mit \"_\"");
					p.sendMessage(DataManager.prefix + "§e/bedwars map <Map>");
					p.sendMessage(DataManager.prefix);
					p.sendMessage(DataManager.prefix + "☲☲☲☲☲☲☲☲☲☲☲☲☲⚊ §eHilfe §9⚊☲☲☲☲☲☲☲☲☲☲☲☲☲☲");
				} else if(args.length == 1) {
					if(!inCreate.contains(p)) {
						p.sendMessage(DataManager.prefix + "☲☲☲☲☲☲☲☲☲☲☲☲☲⚊ §eHilfe §9⚊☲☲☲☲☲☲☲☲☲☲☲☲☲☲");
						p.sendMessage(DataManager.prefix);
						p.sendMessage(DataManager.prefix + "§e/bedwars createMap <MapName> <Teams> <PperTeam> \n" + DataManager.prefix + "  §e<Builder> §7» <Builder> Leerzeichen mit \"_\"");
						p.sendMessage(DataManager.prefix + "§e/bedwars map <Map>");
						p.sendMessage(DataManager.prefix);
						p.sendMessage(DataManager.prefix + "☲☲☲☲☲☲☲☲☲☲☲☲☲⚊ §eHilfe §9⚊☲☲☲☲☲☲☲☲☲☲☲☲☲☲");
					} else {
						if(args[0].equalsIgnoreCase("setspectator")) {
							String map = mapSetup.get(p);
							MapData.load(new File(map + "/BedWars/config.yml"));
							MapData.setSpectator(p.getLocation());
							p.sendMessage(DataManager.prefix + "§aSpectatorspawn gespeichert. Weiter mit §e§o/bedwars setSpawner <type> <id>");
						} else if(args[0].equalsIgnoreCase("setlobby")) {
							File file = new File("plugins/BedWars/config.yml");
							FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
							conf.set("Lobby.World", p.getLocation().getWorld().getName());
							conf.set("Lobby.X", (p.getLocation().getBlockX() + 0.5D));
							conf.set("Lobby.Y", (p.getLocation().getBlockY() + 0.5D));
							conf.set("Lobby.Z", (p.getLocation().getBlockZ() + 0.5D));
							conf.set("Lobby.Yaw", p.getLocation().getYaw());
							conf.set("Lobby.Pitch", p.getLocation().getPitch());
							try{
								conf.save(file);
							} catch(IOException ex){}
							p.sendMessage(DataManager.prefix + "§aMap §e§o" + mapSetup.get(p) + "§a fertig erstellt!");
							String map = mapSetup.get(p);
							MapData.load(new File(map + "/BedWars/config.yml"));
							MapData.setTeams(player_mapSetup.get(p));
							inCreate.remove(p);
							player_mapSetup.remove(p);
							mapSetup.remove(p);
							player_beds.remove(p);
						} else if(args[0].equalsIgnoreCase("setstatistic") || args[0].equalsIgnoreCase("setstats")) {
							File file = new File("plugins/BedWars/config.yml");
							FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
							conf.set("Lobby.Stats.World", p.getLocation().getWorld());
							conf.set("Lobby.Stats.X", (double)(p.getLocation().getBlock().getX() + 0.5D));
							conf.set("Lobby.Stats.Y", (double)(p.getLocation().getBlock().getY() + 0.5D));
							conf.set("Lobby.Stats.Z", (double)(p.getLocation().getBlock().getZ() + 0.5D));
							try{
								conf.save(file);
							} catch(IOException ex){}
							p.sendMessage(DataManager.prefix + "§aStatistik-Hologram Position gesetzt.");
						}
					}
				} else if(args.length == 2) {
					if(args[0].equalsIgnoreCase("map") || args[0].equalsIgnoreCase("world")) {
						String n = args[1];
						if(Bukkit.getWorld(n) != null) {
							p.teleport(Bukkit.getWorld(n).getSpawnLocation());
						} else {
							File wdir = new File(n);
							if(wdir.exists() && wdir.isDirectory()) {
								Bukkit.createWorld(new WorldCreator(n));
								p.sendMessage(DataManager.prefix + "§cWelt wird geladen...");
								Bukkit.getServer().getScheduler().runTaskLater(BedWars.getPlugin(BedWars.class), new Runnable() {
									@Override
									public void run() {
										p.sendMessage(DataManager.prefix + "§aWelt geladen. §eTeleportiere!");
										p.teleport(Bukkit.getWorld(n).getSpawnLocation());
									}
								}, 2*20);
							} else {
								p.sendMessage(DataManager.prefix + "§cEs konnte keine Karte mit dem Namen §e§o" + n + "§c gefunden werden! ⚠ Es wird auf Groß-/Kleinschreibung geachtet!");
							}
						}
					} else {
						if(!inCreate.contains(p)) {
							p.sendMessage(DataManager.prefix + "☲☲☲☲☲☲☲☲☲☲☲☲☲⚊ §eHilfe §9⚊☲☲☲☲☲☲☲☲☲☲☲☲☲☲");
							p.sendMessage(DataManager.prefix);
							p.sendMessage(DataManager.prefix + "§e/bedwars createMap <MapName> <Teams> <PperTeam> \n" + DataManager.prefix + "  §e<Builder> §7» <Builder> Leerzeichen mit \"_\"");
							p.sendMessage(DataManager.prefix + "§e/bedwars map <Map>");
							p.sendMessage(DataManager.prefix);
							p.sendMessage(DataManager.prefix + "☲☲☲☲☲☲☲☲☲☲☲☲☲⚊ §eHilfe §9⚊☲☲☲☲☲☲☲☲☲☲☲☲☲☲");
						} else {
							if(args[0].equalsIgnoreCase("setbed")) {
								String map = mapSetup.get(p);
								String team = args[1];
								MapData.load(new File(map + "/BedWars/config.yml"));
								if(p.getLocation().getBlock().getType().equals(Material.BED_BLOCK)) {
									Block b1 = p.getLocation().getBlock();
									Block b2 = null;
									if(b1.getRelative(BlockFace.NORTH).getType().equals(Material.BED_BLOCK)) {
										b2 = b1.getRelative(BlockFace.NORTH);
									} else if(b1.getRelative(BlockFace.EAST).getType().equals(Material.BED_BLOCK)) {
										b2 = b1.getRelative(BlockFace.EAST);
									} else if(b1.getRelative(BlockFace.SOUTH).getType().equals(Material.BED_BLOCK)) {
										b2 = b1.getRelative(BlockFace.SOUTH);
									} else if(b1.getRelative(BlockFace.WEST).getType().equals(Material.BED_BLOCK)) {
										b2 = b1.getRelative(BlockFace.WEST);
									} else {
										p.sendMessage(DataManager.prefix + "§cEs konnte nicht die zweite Hälfte des Bettes ermittelt werden. Ist das Bett verbuggt?");
									}
									if(b2 != null) {
										Location[] locs = new Location[]{b1.getLocation(), b2.getLocation()};
										MapData.setBed(team, locs);
										p.sendMessage(DataManager.prefix + "§aBett von Team §e§o" + team + " §agespeichert.");
										if(!player_beds.containsKey(p)) {
											player_beds.put(p, 1);
										} else {
											player_beds.put(p, player_beds.get(p) + 1);
										}
										if(player_beds.get(p) >= MapData.getTeamCount()) {
											p.sendMessage(DataManager.prefix + "§aAlle Betten gesetzt! Weiter mit §e§o/bedwars setspectator");
										}
 									}
								} else {
									p.sendMessage(DataManager.prefix + "§cStelle dich auf ein Bett, um das Bett zu speichern.");
								}
							}
						}
					}
				} else if(args.length == 5) {
					if(args[0].equalsIgnoreCase("createMap")) {
						if(inCreate.contains(p)) {
							p.sendMessage(DataManager.prefix + "§cDu erstellst bereits eine BedWars Karte. Beende bitte erst das Setup!");
						} else {
							String map = args[1];
							String builder = args[4];
							int teams = 0;
							int ppt = 0;
							try{
								teams = Integer.parseInt(args[2]);
							} catch(NumberFormatException ex){
								p.sendMessage("§e§o" + args[2] + " §cist keine Zahl!");
							}
							try{
								ppt = Integer.parseInt(args[3]);
							} catch(NumberFormatException ex){
								p.sendMessage("§e§o" + args[3] + " §cist keine Zahl!");
							}
							MapData.load(new File(p.getLocation().getWorld().getName() + "/BedWars/config.yml"));
							MapData.setMapName(map);
							MapData.setTeamCount(teams);
							MapData.setPperTeam(ppt);
							MapData.setBuilder(builder);
							p.sendMessage(DataManager.prefix + "§aSaved Map §e§o" + map + "§a. Weiter geht es mit §e§o/bedwars setspawn <Team> <TeamColor>");
							inCreate.add(p);
							mapSetup.put(p, map);
							player_mapSetup.put(p, new HashMap<String, ChatColor>());
						}
					}
				} else if(args.length == 3) {
					if(!inCreate.contains(p)) {
						p.sendMessage(DataManager.prefix + "☲☲☲☲☲☲☲☲☲☲☲☲☲⚊ §eHilfe §9⚊☲☲☲☲☲☲☲☲☲☲☲☲☲☲");
						p.sendMessage(DataManager.prefix);
						p.sendMessage(DataManager.prefix + "§e/bedwars createMap <MapName> <Teams> <PperTeam> \n" + DataManager.prefix + "  §e<Builder> §7» <Builder> Leerzeichen mit \"_\"");
						p.sendMessage(DataManager.prefix + "§e/bedwars map <Map>");
						p.sendMessage(DataManager.prefix);
						p.sendMessage(DataManager.prefix + "☲☲☲☲☲☲☲☲☲☲☲☲☲⚊ §eHilfe §9⚊☲☲☲☲☲☲☲☲☲☲☲☲☲☲");
					} else {
						if(args[0].equalsIgnoreCase("setspawn")) {
							String map = mapSetup.get(p);
							MapData.load(new File(map + "/BedWars/config.yml"));
							String team = args[1];
							ChatColor cc = ChatColor.valueOf(args[2]);
							player_mapSetup.get(p).put(team, cc);
							MapData.setSpawn(team, p.getLocation());
							if(player_mapSetup.get(p).size() == MapData.getTeamCount()) {
								p.sendMessage(DataManager.prefix + "§aDu hast alle Spawns gesetzt. Weiter geht es mit §e§o/bedwars setbed <Team>");
							}
						} else if(args[0].equalsIgnoreCase("setspawner")) {
							SpawnerType st = SpawnerType.valueOf(args[1]);
							int id = -1;
							String map = mapSetup.get(p);
							MapData.load(new File(map + "/BedWars/config.yml"));
							try{
								id = Integer.parseInt(args[2]);
							} catch(NumberFormatException ex){
								p.sendMessage("§e§o" + args[2] + " §cist keine Zahl!");
								return true;
							}
							MapData.setSpawner(p.getLocation(), st, id);
							p.sendMessage(DataManager.prefix + "§aSpawner §e§o" + st.toString() + id + " gespeichert. §aAlle Spawner gesetzt? » §e/bw setlobby");
						}
					}
				}
			} else {
				sender.sendMessage(DataManager.prefix + "§cDu musst diesen Befehl mit einem Spieler ausführen.");
			}
			
			
		} else {
			sender.sendMessage(DataManager.prefix + "§cDu hast nicht die benötigte Berechtigung um diesen Befehl auszuführen.");
		}
		
		
		return true;
	}
	
	
}
