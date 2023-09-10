package de.legitfinn.knockbackffa.command;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.NightShadwo.SignAPI.Main.Types.Gamemode;
import de.legitfinn.NickApi.main.NickModul;
import de.legitfinn.knockbackffa.SQL.StatisticSQL;
import de.legitfinn.knockbackffa.main.DataManager;
import de.legitfinn.ppermissions.PlayerData.PlayerData;

public class COMMAND_stats implements CommandExecutor{

	public static HashMap<UUID, UUID> randomvalues = new HashMap<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player p = null;
		if(sender instanceof Player){
			p = (Player) sender;
		}
		
		if(p == null){
			sender.sendMessage(DataManager.prefix + "§cDu musst ein Spieler sein, um dir Stats anzusehen.");
		}else {
			UUID stats = null;
			boolean change = false;
			String real = "";
			if(args.length == 1){
				Player ps = Bukkit.getPlayer(args[0]);
				if(ps != null){
					stats = ps.getUniqueId();
					if(NickModul.isNicked(ps.getUniqueId())){
						change = true;
						real = ps.getName();
						if(!randomvalues.containsKey(stats)){
							int i = StatisticSQL.getExistingPlayers(Gamemode.KNOCKBACKFFA.toString());
							if(i > -1){
								int random = new Random().nextInt(i);
								UUID t = StatisticSQL.getUUIDOfPlace(Gamemode.KNOCKBACKFFA.toString(), "KILLS", random);	
								randomvalues.put(stats, t);
							}else {
								stats = ps.getUniqueId();
							}
						}
					}else {
						stats = ps.getUniqueId();
					}
				}else {
					stats = PlayerData.getUUID(args[0]);
				}
			}else if(args.length == 0){
				stats = p.getUniqueId();
			}else {
				p.sendMessage("§cBenutze /stats oder /stats <spielername>");
				return true;
			}
			
			if(stats == null){
				p.sendMessage(DataManager.prefix + "§cDieser Spieler hat noch nie KnockbackFFA gespielt.");
				return true;
			}
			
			if(change && randomvalues.containsKey(stats)){
				stats = randomvalues.get(stats);
			}
			
			int streak = StatisticSQL.getInt(Gamemode.KNOCKBACKFFA.toString(), "MAXSTREAK", stats);
			int kills = StatisticSQL.getInt(Gamemode.KNOCKBACKFFA.toString(), "KILLS", stats);
			int death = StatisticSQL.getInt(Gamemode.KNOCKBACKFFA.toString(), "DEATH", stats);
			double kd = kills;
			
			if(death > 0){
				kd = (double)kills / (double) death;
				kd = Math.round(kd * 1000.0D) / 1000.0D;
			}
			
			p.sendMessage("§7»» §eKnockbackFFA §7- §eStats");
			if(change){
				p.sendMessage("§3Spieler §7» §e" + real);
			}else {
				p.sendMessage("§3Spieler §7» §e" + PlayerData.getName(stats));
			}
			p.sendMessage("§3Beste Streak §7» §e" + streak);
			p.sendMessage("§3Kills §7» §e" + kills);
			p.sendMessage("§3Tode §7» §e" + death);
			p.sendMessage("§3K/D §7» §e" + kd);
			
		}
	
		return true;
	}
	
}
