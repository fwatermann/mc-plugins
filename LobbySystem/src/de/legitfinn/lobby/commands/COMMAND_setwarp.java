package de.legitfinn.lobby.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.legitfinn.lobby.main.ConfigManager;
import de.legitfinn.ppermissions.main.PPermissions;

public class COMMAND_setwarp implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player)sender;
			
			if(PPermissions.hasRight(p, "lobbysetwarp")) {
				if(args.length == 0) {
					p.sendMessage("§4§lLobby §7» §c/setwarp <Community,Spawn,CW,SW,BW,1vs1,KPVP,Tit,Bewerben,SG,KFFA,OITC>");				
				} else if(args.length == 1){
					String warp = args[0];
					if(warp.equalsIgnoreCase("Community")) {
						ConfigManager.set("Warp.Community.World", p.getLocation().getWorld().getName());
						ConfigManager.set("Warp.Community.X", (p.getLocation().getBlockX() + 0.5));
						ConfigManager.set("Warp.Community.Y", (p.getLocation().getBlockY() + 0.5));
						ConfigManager.set("Warp.Community.Z", (p.getLocation().getBlockZ() + 0.5));
						ConfigManager.set("Warp.Community.Yaw", p.getLocation().getYaw());
						ConfigManager.set("Warp.Community.Pitch", p.getLocation().getPitch());
						p.sendMessage("§4§lLobby §7» §aWarp für §6§oCommunity §agesetzt.");
					} else if(warp.equalsIgnoreCase("Spawn")) {
						ConfigManager.set("Warp.Spawn.World", p.getLocation().getWorld().getName());
						ConfigManager.set("Warp.Spawn.X", (p.getLocation().getBlockX() + 0.5));
						ConfigManager.set("Warp.Spawn.Y", (p.getLocation().getBlockY() + 0.5));
						ConfigManager.set("Warp.Spawn.Z", (p.getLocation().getBlockZ() + 0.5));
						ConfigManager.set("Warp.Spawn.Yaw", p.getLocation().getYaw());
						ConfigManager.set("Warp.Spawn.Pitch", p.getLocation().getPitch());
						p.sendMessage("§4§lLobby §7» §aWarp für §6§oSpawn §agesetzt.");
					} else if(warp.equalsIgnoreCase("CW")) {
						ConfigManager.set("Warp.CW.World", p.getLocation().getWorld().getName());
						ConfigManager.set("Warp.CW.X", (p.getLocation().getBlockX() + 0.5));
						ConfigManager.set("Warp.CW.Y", (p.getLocation().getBlockY() + 0.5));
						ConfigManager.set("Warp.CW.Z", (p.getLocation().getBlockZ() + 0.5));
						ConfigManager.set("Warp.CW.Yaw", p.getLocation().getYaw());
						ConfigManager.set("Warp.CW.Pitch", p.getLocation().getPitch());
						p.sendMessage("§4§lLobby §7» §aWarp für §6§oClanWar §agesetzt.");
					} else if(warp.equalsIgnoreCase("SW")) {
						ConfigManager.set("Warp.SW.World", p.getLocation().getWorld().getName());
						ConfigManager.set("Warp.SW.X", (p.getLocation().getBlockX() + 0.5));
						ConfigManager.set("Warp.SW.Y", (p.getLocation().getBlockY() + 0.5));
						ConfigManager.set("Warp.SW.Z", (p.getLocation().getBlockZ() + 0.5));
						ConfigManager.set("Warp.SW.Yaw", p.getLocation().getYaw());
						ConfigManager.set("Warp.SW.Pitch", p.getLocation().getPitch());
						p.sendMessage("§4§lLobby §7» §aWarp für §6§oSkyWars §agesetzt.");
					} else if(warp.equalsIgnoreCase("BW")) {
						ConfigManager.set("Warp.BW.World", p.getLocation().getWorld().getName());
						ConfigManager.set("Warp.BW.X", (p.getLocation().getBlockX() + 0.5));
						ConfigManager.set("Warp.BW.Y", (p.getLocation().getBlockY() + 0.5));
						ConfigManager.set("Warp.BW.Z", (p.getLocation().getBlockZ() + 0.5));
						ConfigManager.set("Warp.BW.Yaw", p.getLocation().getYaw());
						ConfigManager.set("Warp.BW.Pitch", p.getLocation().getPitch());
						p.sendMessage("§4§lLobby §7» §aWarp für §6§oBedWars §agesetzt.");
					} else if(warp.equalsIgnoreCase("1vs1")) {
						ConfigManager.set("Warp.1vs1.World", p.getLocation().getWorld().getName());
						ConfigManager.set("Warp.1vs1.X", (p.getLocation().getBlockX() + 0.5));
						ConfigManager.set("Warp.1vs1.Y", (p.getLocation().getBlockY() + 0.5));
						ConfigManager.set("Warp.1vs1.Z", (p.getLocation().getBlockZ() + 0.5));
						ConfigManager.set("Warp.1vs1.Yaw", p.getLocation().getYaw());
						ConfigManager.set("Warp.1vs1.Pitch", p.getLocation().getPitch());
						p.sendMessage("§4§lLobby §7» §aWarp für §6§o1vs1 §agesetzt.");
					} else if(warp.equalsIgnoreCase("KPVP")) {
						ConfigManager.set("Warp.KPVP.World", p.getLocation().getWorld().getName());
						ConfigManager.set("Warp.KPVP.X", (p.getLocation().getBlockX() + 0.5));
						ConfigManager.set("Warp.KPVP.Y", (p.getLocation().getBlockY() + 0.5));
						ConfigManager.set("Warp.KPVP.Z", (p.getLocation().getBlockZ() + 0.5));
						ConfigManager.set("Warp.KPVP.Yaw", p.getLocation().getYaw());
						ConfigManager.set("Warp.KPVP.Pitch", p.getLocation().getPitch());
						p.sendMessage("§4§lLobby §7» §aWarp für §6§oKPVP §agesetzt.");
					} else if(warp.equalsIgnoreCase("Tit")) {
						ConfigManager.set("Warp.Tit.World", p.getLocation().getWorld().getName());
						ConfigManager.set("Warp.Tit.X", (p.getLocation().getBlockX() + 0.5));
						ConfigManager.set("Warp.Tit.Y", (p.getLocation().getBlockY() + 0.5));
						ConfigManager.set("Warp.Tit.Z", (p.getLocation().getBlockZ() + 0.5));
						ConfigManager.set("Warp.Tit.Yaw", p.getLocation().getYaw());
						ConfigManager.set("Warp.Tit.Pitch", p.getLocation().getPitch());
						p.sendMessage("§4§lLobby §7» §aWarp für §6§oTrainIt §agesetzt.");
					} else if(warp.equalsIgnoreCase("Bewerben")) {
						ConfigManager.set("Warp.Bewerben.World", p.getLocation().getWorld().getName());
						ConfigManager.set("Warp.Bewerben.X", (p.getLocation().getBlockX() + 0.5));
						ConfigManager.set("Warp.Bewerben.Y", (p.getLocation().getBlockY() + 0.5));
						ConfigManager.set("Warp.Bewerben.Z", (p.getLocation().getBlockZ() + 0.5));
						ConfigManager.set("Warp.Bewerben.Yaw", p.getLocation().getYaw());
						ConfigManager.set("Warp.Bewerben.Pitch", p.getLocation().getPitch());
						p.sendMessage("§4§lLobby §7» §aWarp für §6§oBewerben §agesetzt.");
					} else if(warp.equalsIgnoreCase("SG")) {
						ConfigManager.set("Warp.SG.World", p.getLocation().getWorld().getName());
						ConfigManager.set("Warp.SG.X", (p.getLocation().getBlockX() + 0.5));
						ConfigManager.set("Warp.SG.Y", (p.getLocation().getBlockY() + 0.5));
						ConfigManager.set("Warp.SG.Z", (p.getLocation().getBlockZ() + 0.5));
						ConfigManager.set("Warp.SG.Yaw", p.getLocation().getYaw());
						ConfigManager.set("Warp.SG.Pitch", p.getLocation().getPitch());
						p.sendMessage("§4§lLobby §7» §aWarp für §6§oSurvivalGames §agesetzt.");
					} else if(warp.equalsIgnoreCase("KFFA")) {
						ConfigManager.set("Warp.KFFA.World", p.getLocation().getWorld().getName());
						ConfigManager.set("Warp.KFFA.X", (p.getLocation().getBlockX() + 0.5));
						ConfigManager.set("Warp.KFFA.Y", (p.getLocation().getBlockY() + 0.5));
						ConfigManager.set("Warp.KFFA.Z", (p.getLocation().getBlockZ() + 0.5));
						ConfigManager.set("Warp.KFFA.Yaw", p.getLocation().getYaw());
						ConfigManager.set("Warp.KFFA.Pitch", p.getLocation().getPitch());
						p.sendMessage("§4§lLobby §7» §aWarp für §6§oKnockbackFFA §agesetzt.");
					} else if(warp.equalsIgnoreCase("OITC")) {
						ConfigManager.set("Warp.OITC.World", p.getLocation().getWorld().getName());
						ConfigManager.set("Warp.OITC.X", (p.getLocation().getBlockX() + 0.5));
						ConfigManager.set("Warp.OITC.Y", (p.getLocation().getBlockY() + 0.5));
						ConfigManager.set("Warp.OITC.Z", (p.getLocation().getBlockZ() + 0.5));
						ConfigManager.set("Warp.OITC.Yaw", p.getLocation().getYaw());
						ConfigManager.set("Warp.OITC.Pitch", p.getLocation().getPitch());
						p.sendMessage("§4§lLobby §7» §aWarp für §6§oOneInTheChamber §agesetzt.");
					} else {
						p.sendMessage("§4§lLobby §7» §c/setwarp <Community,Spawn,CW,SW,BW,1vs1,KPVP,Tit,Bewerben,SG,KFFA,OITC>");	
					}
				} else {
					p.sendMessage("§4§lLobby §7» §c/setwarp <Community,Spawn,CW,SW,BW,1vs1,KPVP,Tit,Bewerben,SG,KFFA,OITC>");	
				}
			} else {
				p.sendMessage("§4§lLobby §7» §cDu hast keine Rechte diesen Befehl auszuführen.");
			}
		}
		return true;
	}
	
	
}
