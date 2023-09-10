package de.legitfinn.bedwars.cloud;

import java.io.File;

import org.bukkit.Bukkit;

import de.NightShadwo.SignAPI.Connectoin.SignAPI;
import de.NightShadwo.SignAPI.Events.SignServerListener;
import de.NightShadwo.SignAPI.Main.Types.InformationTyps;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.MapData;
import de.legitfinn.bedwars.methoden.ScoreboardSystem;
import de.legitfinn.ppermissions.main.main;

public class ServerListener implements SignServerListener{

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
				if(!args[1].split(" ")[0].equalsIgnoreCase(DataManager.map)) {
					DataManager.map = args[1].split(" ")[0];
					Bukkit.getServer().getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
						@Override
						public void run() {
							File dir = new File("Map/BedWars");
							if(dir.exists() && dir.isDirectory()) {
								MapData.load(new File("Map/BedWars/config.yml"));
							} else {
								MapData.load(new File(DataManager.map + "/BedWars/config.yml"));
							}
							System.out.println("Loaded Mapdata! MapName: " + MapData.getMapName());
							ScoreboardSystem.createLobbyScoreboard(true);
						}
					}, 10);
				} else {
					SignAPI.sendChange(InformationTyps.MAX_PLAYERS, (DataManager.teams * DataManager.maxTeamsize) + "", "own");
				}
			}
		}
	}

}
