package de.finn.CloudSystem.Signs.Signs;

import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import de.finn.CloudSystem.Signs.main.DataManager;

public class CreateListener implements Listener {

	@EventHandler
	public void onCreate(SignChangeEvent e) {
		if(e.getPlayer().hasPermission("network.admin")) {
			if(e.getLine(0).equalsIgnoreCase("[cloudsign]")) {
				String gamemode = e.getLine(1);
				Integer id = Integer.parseInt(e.getLine(2));
				createSign(e.getBlock().getLocation(), gamemode, id);
				e.setLine(0, null);
				e.setLine(1, "Server wird");
				e.setLine(2, "geladen...");
				e.setLine(3, null);
			}			
		}		
	}
	
	public void createSign(Location loc, String gamemode, int id) {
		FileConfiguration conf = DataManager.conf;
		conf.set("Signs." + gamemode.toUpperCase() + "." + id + ".World", loc.getWorld().getName());
		conf.set("Signs." + gamemode.toUpperCase() + "." + id + ".X", loc.getBlockX());
		conf.set("Signs." + gamemode.toUpperCase() + "." + id + ".Y", loc.getBlockY());
		conf.set("Signs." + gamemode.toUpperCase() + "." + id + ".Z", loc.getBlockZ());
		try {
			conf.save(DataManager.config);
		} catch(IOException ex) {}	
	}
}
