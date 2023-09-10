package de.legitfinn.lobby.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.legitfinn.lobby.functions.DailyReward;
import de.legitfinn.ppermissions.main.PPermissions;

public class COMMAND_dailyreward implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(PPermissions.hasRight(p, "dailyrewardarea") || p.hasPermission("lobby.dailyrewardarea")) {
				if(args.length == 0) {
					p.sendMessage("§4§lLobby §7» §c/dailyreward <1/2/newLoc>");
				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("1")) {
						ItemStack is = new ItemStack(Material.IRON_HOE);
						ItemMeta meta = is.getItemMeta();
						meta.setDisplayName("§4§l» §6DailyRewardSetter §9#1");
						is.setItemMeta(meta);
						p.getInventory().addItem(is);
					} else if(args[0].equalsIgnoreCase("2")) {
						ItemStack is = new ItemStack(Material.IRON_HOE);
						ItemMeta meta = is.getItemMeta();
						meta.setDisplayName("§4§l» §6DailyRewardSetter §9#2");
						is.setItemMeta(meta);
						p.getInventory().addItem(is);
					} else if(args[0].equalsIgnoreCase("newLoc")){
//						if(DailyReward.dailyReward != null) {
//							DailyReward.dailyReward.getBlock().setType(DailyReward.oldDownMaterial);
//							DailyReward.dailyReward.getBlock().setData(DailyReward.oldDownData);
//							DailyReward.dailyReward.clone().add(0, 1, 0).getBlock().setType(DailyReward.oldTopMaterial);
//							DailyReward.dailyReward.clone().add(0, 1, 0).getBlock().setData(DailyReward.oldTopData);			
//						}
						DailyReward.randomLocation();
						p.sendMessage("§4§lLobby §7» §aEs wurde eine neue Position für den DailyReward gewählt. §o[" + DailyReward.dailyReward.getBlockX() + ", " + DailyReward.dailyReward.getBlockY() + ", "  + DailyReward.dailyReward.getBlockY() + "]");
					} else {
						p.sendMessage("§4§lLobby §7» §c/dailyreward <1/2/newLoc>");
					}
				} else {
					p.sendMessage("§4§lLobby §7» §c/dailyreward <1/2>");
				}
			} else {
				p.sendMessage("§4§lLobby §7» §cDu hast keine Rechte um diesen Befehl auszuführen.");
			}
		}
		
		return true;
	}

}
