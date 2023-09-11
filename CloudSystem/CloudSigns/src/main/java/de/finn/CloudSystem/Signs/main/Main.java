package de.finn.CloudSystem.Signs.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import de.finn.CloudSystem.Signs.Signs.CreateListener;
import de.finn.CloudSystem.Signs.Signs.SignGroup;
import de.finn.CloudSystem.Signs.updateListeners.UpdateListener;

public class Main extends JavaPlugin {

	public void onEnable() {
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		initGamemodes();
		Bukkit.getPluginManager().registerEvents(new CreateListener(), this);
		Bukkit.getPluginManager().registerEvents(new UpdateListener(), this);
//		animation();
	}
	
	public void onDisable() {
		
	}
	
	public void initGamemodes() {
		DataManager.gamemode_signGroup.put("BEDWARS", new SignGroup("BEDWARS"));
		DataManager.gamemode_signGroup.put("SKYWARS", new SignGroup("SKYWARS"));
		DataManager.gamemode_signGroup.put("KNOCKIT", new SignGroup("KNOCKIT"));
		DataManager.gamemode_signGroup.put("MLG", new SignGroup("MLG"));
		DataManager.gamemode_signGroup.put("ONELINE", new SignGroup("ONELINE"));
		DataManager.gamemode_signGroup.put("BUILDFFA", new SignGroup("BUILDFFA"));
		DataManager.gamemode_signGroup.put("DARKHUNT", new SignGroup("DARKHUNT"));
		DataManager.gamemode_signGroup.put("LOBBY", new SignGroup("LOBBY"));
	}
	
	public void animation() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for(String gamemode : DataManager.gamemode_signGroup.keySet()) {
					DataManager.gamemode_signGroup.get(gamemode).updateForAnimation();
				}
				DataManager.animation ++;
				if(DataManager.animation > 25) {
					DataManager.animation = 0;
				}
			}
		}, 0, 5);
	}
	
	
}
