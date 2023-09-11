package de.finn.CloudSystem.Wrapper.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ServerManager {

	public HashMap<String, Server> servername_server = new HashMap<String, Server>();
	public HashMap<String, ArrayList<Server>> gamemode_servers = new HashMap<String, ArrayList<Server>>();
	public HashMap<String, ArrayList<String>> gamemode_maps = new HashMap<String, ArrayList<String>>();
	
	public ArrayList<String> blocked_ports = new ArrayList<String>();
	public ArrayList<String> blackList_ports = new ArrayList<String>();
	
	public void startServer(String servername, String gamemode, String map, String masterIP, int masterPort) {
		int port = getPort();
		Server server = new Server(servername, gamemode, map, port, masterIP, masterPort);
		servername_server.put(servername, server);
		if(!gamemode_servers.containsKey(gamemode)) {
			gamemode_servers.put(gamemode, new ArrayList<Server>());
		}
		gamemode_servers.get(gamemode).add(server);
		server.create();
		server.start();
		CloudWrapper.instance.send("startedServer;" + servername + ";" + gamemode + ";" + map + ";" + port);
	}
	
	public void stopServer(String servername) {
		Server server = null;
		String rname = servername;
		for(String all : servername_server.keySet()) {
			if(all.equalsIgnoreCase(servername)) {
				server = servername_server.get(all);
				rname = all;
				break;
			}
		}
		if(server != null)  {
			server.stop();
			servername_server.remove(rname);
			gamemode_servers.get(getGamemode(rname)).remove(server);
			blocked_ports.remove(server.getPort() + "");
			CloudWrapper.instance.send("stoppedServer;" + servername + ";" + server.getGamemode());
		}
	}
	
	public void changeMap(String servername, String gamemode, String map, boolean isForcemap) {
		if(map.equalsIgnoreCase("RANDOM_Zx873")) {
			String lastMap = CloudWrapper.instance.serverManager.servername_server.get(servername).getMap();
			System.out.println("LastMap: " + lastMap);
			map = getRandomMap(gamemode);
			if(gamemode_maps.get(gamemode.toUpperCase()).size() > 1) {
				while(map.equalsIgnoreCase(lastMap)) {
					map = getRandomMap(gamemode);
					System.out.println("Maybe: " + map);
				}
			}
			System.out.println("Its " + map);
		}
		Server server = null;
		for(String all : servername_server.keySet()) {
			if(all.equalsIgnoreCase(servername)) {
				server = servername_server.get(all);
				break;
			}
		}
		if(isForcemap) {
			String emap = getMapExists(gamemode, map);
			map = emap;
		}
		server.changeMap(gamemode, map, isForcemap);
	}
	
	public void loadMaps() {		
		for(File gm : new File("./templates/Maps").listFiles()) {
			if(!gamemode_maps.containsKey(gm.getName().toUpperCase())) {
				gamemode_maps.put(gm.getName().toUpperCase(), new ArrayList<String>());
			}
			for(File maps : new File("./templates/Maps/" + gm.getName() + "/").listFiles()) {
				if(maps.isDirectory()) {
					if(!maps.getName().equalsIgnoreCase("default")) {
						gamemode_maps.get(gm.getName().toUpperCase()).add(maps.getName());
					}
				}
			}
		}	
	}
	
	public String getRandomMap(String gamemode) {
		Random rnd = new Random();
		if(gamemode_maps.containsKey(gamemode.toUpperCase())) {
			if(gamemode_maps.get(gamemode.toUpperCase()).size() > 0) {
				String map = gamemode_maps.get(gamemode.toUpperCase()).get(rnd.nextInt(gamemode_maps.get(gamemode.toUpperCase()).size()));
				int i = 0;
				while(serverWithMapExists(gamemode, map) && i < 50) {
					map = gamemode_maps.get(gamemode.toUpperCase()).get(rnd.nextInt(gamemode_maps.get(gamemode.toUpperCase()).size()));
					i ++;
				}
				return map;
			} else {
				return "default";
			}
		} else {
			return "default";
		}
	}
	
	public boolean serverWithMapExists(String gamemode, String map) {
		if(!gamemode_servers.containsKey(gamemode)) {
			return false;
		}
		for(Server all : gamemode_servers.get(gamemode)) {
			if(all.getStatus().equalsIgnoreCase("WAITING") && all.getMap().equalsIgnoreCase(map)) {
				return true;
			}
		}
		return false;
	}
	
	public  String getMapExists(String gamemode, String map) {
		String ret = null;
		if(gamemode_maps.containsKey(gamemode.toUpperCase())) {
			for(String all : gamemode_maps.get(gamemode.toUpperCase())) {
				if(all.equalsIgnoreCase(map)) {
					ret = all;
					break;
				}
			}
		}
		return ret;
	}
	
	@Deprecated
	public String getServerName(String gamemode) {
		
		if(gamemode_servers.containsKey(gamemode)) {
			int i = 1;
			while(servername_server.containsKey(gamemode + "#" + i)) {
				i ++;
			}
			return gamemode + "#" + i;
		}		
		return gamemode + "#" + "xXx";
	}
	
	private String getGamemode(String servername) {
		for(String all : gamemode_servers.keySet()) {
			for(Server sll : gamemode_servers.get(all)) {
				if(sll.getServername().equalsIgnoreCase(servername)) {
					return all;
				}
			}
		}
		return "none";
	}
	
	private Integer getPort() {
		int i = 40000;
		while(blocked_ports.contains(i + "")) i++;
		blocked_ports.add(i + "");
		return i;
	}
	
	
}
