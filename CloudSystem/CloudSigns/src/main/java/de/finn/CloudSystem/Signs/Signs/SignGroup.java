package de.finn.CloudSystem.Signs.Signs;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import de.finn.CloudSystem.Signs.main.DataManager;

public class SignGroup {

	private ArrayList<ServerSign> signs = new ArrayList<ServerSign>();
	private String gamemode;
	
	private ArrayList<Server> waitingServers = new ArrayList<Server>();
	
	public SignGroup(String gamemode) {
		this.gamemode = gamemode;
		loadSigns();
	}
	
	private void loadSigns() {
		int i = 1;
		int has = 0;
		while(i < 1000) {
			if(DataManager.conf.isSet("Signs." + gamemode.toUpperCase() + "." + i + ".World")) {
				World w = Bukkit.getWorld(DataManager.conf.getString("Signs." + gamemode.toUpperCase() + "." + i + ".World"));
				int x = DataManager.conf.getInt("Signs." + gamemode.toUpperCase() + "." + i + ".X");
				int y = DataManager.conf.getInt("Signs." + gamemode.toUpperCase() + "." + i + ".Y");
				int z = DataManager.conf.getInt("Signs." + gamemode.toUpperCase() + "." + i + ".Z");
				has ++;
				Location loc = new Location(w,x,y,z);
				ServerSign s = new ServerSign(loc, gamemode.toUpperCase(), has);
				s.setGroup(this);
				signs.add(s);
				s.setServer(null);
			}
			i ++;
		}
	}
	
	public void updateForAnimation() {
		for(ServerSign all : signs) {
			all.update();
		}
	}
	
	public String getGamemode() {
		return this.gamemode;
	}
	
	public void addSign(ServerSign s) {
		signs.add(s);
	}
	
	public void removeSign(ServerSign s) {
		signs.remove(s);
	}
	
	public void lookUpSigns() {
		ArrayList<ServerSign> freeSigns = new ArrayList<ServerSign>();
		for(ServerSign all : signs) {
			if(!all.isUsed()) {
				freeSigns.add(all);
			}
		}
		for(Server s : waitingServers) {
			ServerSign ss = freeSigns.get(0);
			if(ss != null) {
				freeSigns.get(0).setServer(s);
				freeSigns.remove(0);
			}
		}
		for(ServerSign s : freeSigns) {
			s.setServer(null);
			s.update();
		}
	}
	
	public void addServer(Server server) {
		for(ServerSign all : signs) {
			if(!all.isUsed()) {
				all.setServer(server);
				return;
			}
		}
		waitingServers.add(server);
	}
	
	public void updateSign(Server s) {
		for(ServerSign all : signs) {
			all.update();
		}
	}
}
