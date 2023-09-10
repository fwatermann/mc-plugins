package de.legitfinn.knockbackffa.cloud;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import de.NightShadwo.SignAPI.Events.SignServerListener;
import de.legitfinn.knockbackffa.listener.JoinListener;
import de.legitfinn.knockbackffa.main.DataManager;
import de.legitfinn.knockbackffa.methoden.MapItems;
import de.legitfinn.ppermissions.main.main;

public class ServerListener implements SignServerListener {

	@Override
	public void onReceive(String cmd, String[] args) {
	
		System.out.println("Received CloudCommand: " + cmd);
		String out = "";
		for(String all : args) {
			out += all + ", ";
		}
		System.out.println("Arguments: " + out);
		
		if(cmd.equalsIgnoreCase("map")) {
			Integer id = Integer.valueOf(args[0]);
			if(id == de.NightShadwo.SignAPI.Main.DataManager.id) {
				if(DataManager.mapname != null || !args[0].split(" ")[0].equalsIgnoreCase(DataManager.mapname)) {
					DataManager.mapname = args[1].split(" ")[0];
					Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
						@Override
						public void run() {
							DataManager.map = Bukkit.createWorld(new WorldCreator("Map"));
							DataManager.map.setDifficulty(Difficulty.PEACEFUL);
							DataManager.map.setGameRuleValue("doDaylightCycle", "false");
							DataManager.map.setFullTime(6000L);
							MapItems.loadLocations();
							MapItems.spawnItems();
							DataManager.loadingMap = false;
							Location loc = Bukkit.getWorld("Map").getSpawnLocation().clone().add(0.5, 0.25, 0.5);
							for(Player all : Bukkit.getOnlinePlayers()){
								all.teleport(loc);
								JoinListener.addItems(all);
							}
						}
					}, 10);
				}
			}
		}
		
		
	}

}
