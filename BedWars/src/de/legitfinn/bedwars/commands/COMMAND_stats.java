package de.legitfinn.bedwars.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.legitfinn.NickApi.main.NickModul;
import de.legitfinn.bedwars.listener.JoinListener;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.sql.StatisticSQL;
import de.legitfinn.ppermissions.PlayerData.PlayerData;

public class COMMAND_stats implements CommandExecutor{

	public static HashMap<UUID, UUID> player_fakeuuid = new HashMap<UUID, UUID>();
	
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(args.length == 0) {
				String[] lines = new String[]{"§8●» §6Statistiken von §7" + p.getName() + " §8«●"
						, "§6Kills: §7" + StatisticSQL.getInt("BEDWARS", "KILLS", p.getUniqueId())
						, "§6Death: §7" + StatisticSQL.getInt("BEDWARS", "DEATH", p.getUniqueId())
						, "§6K/D: §7" + JoinListener.getKd(StatisticSQL.getInt("BEDWARS", "KILLS", p.getUniqueId()), StatisticSQL.getInt("BEDWARS", "DEATH", p.getUniqueId()))
						, "§6zerstörte Betten: §7" + StatisticSQL.getInt("BEDWARS", "BEDS", p.getUniqueId())
						, "§6Wins: §7" + StatisticSQL.getInt("BEDWARS", "WINS", p.getUniqueId())
						, "§6Games: §7" + StatisticSQL.getInt("BEDWARS", "GAMES", p.getUniqueId())
						, "§6Sieg zu §7" + JoinListener.getWinProbability(StatisticSQL.getInt("BEDWARS", "WINS", p.getUniqueId()), StatisticSQL.getInt("BEDWARS", "GAMES", p.getUniqueId()))};
				
				p.sendMessage(DataManager.prefix + "§6⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
				p.sendMessage(DataManager.prefix);
				p.sendMessage(DataManager.prefix + "§eStatistiken von §7" + p.getName());
				p.sendMessage(DataManager.prefix);
				p.sendMessage(DataManager.prefix + "§6" + lines[1]);
				p.sendMessage(DataManager.prefix + "§6" + lines[2]);
				p.sendMessage(DataManager.prefix + "§6" + lines[3]);
				p.sendMessage(DataManager.prefix + "§6" + lines[4]);
				p.sendMessage(DataManager.prefix + "§6" + lines[5]);
				p.sendMessage(DataManager.prefix + "§6" + lines[6]);
				p.sendMessage(DataManager.prefix + "§6" + lines[7]);
				p.sendMessage(DataManager.prefix);
				p.sendMessage(DataManager.prefix + "§6⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
			} else if(args.length == 1) {
				String name = args[0];
				UUID uuid = PlayerData.getUUID(name);
				String rname = PlayerData.getName(uuid);
				
				if(!NickModul.isNicked(uuid)) {
					if(uuid != null) {
						String[] lines = new String[]{"§8●» §6Statistiken von §7" + rname + " §8«●"
								, "§6Kills: §7" + StatisticSQL.getInt("BEDWARS", "KILLS", uuid)
								, "§6Death: §7" + StatisticSQL.getInt("BEDWARS", "DEATH", uuid)
								, "§6K/D: §7" + JoinListener.getKd(StatisticSQL.getInt("BEDWARS", "KILLS", uuid), StatisticSQL.getInt("BEDWARS", "DEATH", uuid))
								, "§6zerstörte Betten: §7" + StatisticSQL.getInt("BEDWARS", "BEDS", uuid)
								, "§6Wins: §7" + StatisticSQL.getInt("BEDWARS", "WINS", uuid)
								, "§6Games: §7" + StatisticSQL.getInt("BEDWARS", "GAMES", uuid)
								, "§6Sieg zu §7" + JoinListener.getWinProbability(StatisticSQL.getInt("BEDWARS", "WINS", uuid), StatisticSQL.getInt("BEDWARS", "GAMES", uuid))};
						
						p.sendMessage(DataManager.prefix + "§6⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
						p.sendMessage(DataManager.prefix);
						p.sendMessage(DataManager.prefix + "§eStatistiken von §7" + rname);
						p.sendMessage(DataManager.prefix);
						p.sendMessage(DataManager.prefix + "§6" + lines[1]);
						p.sendMessage(DataManager.prefix + "§6" + lines[2]);
						p.sendMessage(DataManager.prefix + "§6" + lines[3]);
						p.sendMessage(DataManager.prefix + "§6" + lines[4]);
						p.sendMessage(DataManager.prefix + "§6" + lines[5]);
						p.sendMessage(DataManager.prefix + "§6" + lines[6]);
						p.sendMessage(DataManager.prefix + "§6" + lines[7]);
						p.sendMessage(DataManager.prefix);
						p.sendMessage(DataManager.prefix + "§6⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
					} else {
						p.sendMessage(DataManager.prefix + "§cDieser Spieler hat noch kein BedWars auf diesem Netzwerk gespielt... :/");
					}
				} else {
					
					if(!player_fakeuuid.containsKey(uuid)) {
						player_fakeuuid.put(uuid, StatisticSQL.getRandomStatsUUID("BEDWARS"));
					}
					
					rname = de.legitfinn.NickApi.main.main.uuid_nickname.get(uuid);
					
					UUID rnd = player_fakeuuid.get(uuid);
					
					String[] lines = new String[]{"§8●» §6Statistiken von §7" + rname + " §8«●"
							, "§6Kills: §7" + StatisticSQL.getInt("BEDWARS", "KILLS", rnd)
							, "§6Death: §7" + StatisticSQL.getInt("BEDWARS", "DEATH", rnd)
							, "§6K/D: §7" + JoinListener.getKd(StatisticSQL.getInt("BEDWARS", "KILLS", rnd), StatisticSQL.getInt("BEDWARS", "DEATH", rnd))
							, "§6zerstörte Betten: §7" + StatisticSQL.getInt("BEDWARS", "BEDS", rnd)
							, "§6Wins: §7" + StatisticSQL.getInt("BEDWARS", "WINS", rnd)
							, "§6Games: §7" + StatisticSQL.getInt("BEDWARS", "GAMES", rnd)
							, "§6Sieg zu §7" + JoinListener.getWinProbability(StatisticSQL.getInt("BEDWARS", "WINS", rnd), StatisticSQL.getInt("BEDWARS", "GAMES", rnd))};
					
					p.sendMessage(DataManager.prefix + "§6⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
					p.sendMessage(DataManager.prefix);
					p.sendMessage(DataManager.prefix + "§eStatistiken von §7" + rname);
					p.sendMessage(DataManager.prefix);
					p.sendMessage(DataManager.prefix + "§6" + lines[1]);
					p.sendMessage(DataManager.prefix + "§6" + lines[2]);
					p.sendMessage(DataManager.prefix + "§6" + lines[3]);
					p.sendMessage(DataManager.prefix + "§6" + lines[4]);
					p.sendMessage(DataManager.prefix + "§6" + lines[5]);
					p.sendMessage(DataManager.prefix + "§6" + lines[6]);
					p.sendMessage(DataManager.prefix + "§6" + lines[7]);
					p.sendMessage(DataManager.prefix);
					p.sendMessage(DataManager.prefix + "§6⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
				}
			}			
		} else {
			sender.sendMessage(DataManager.prefix + "§cDie Konsole hat keine Statistiken.");
		}
		
		return true;
	}
	
}
