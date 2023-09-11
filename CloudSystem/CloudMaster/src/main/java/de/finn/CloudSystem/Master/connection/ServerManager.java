package de.finn.CloudSystem.Master.connection;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerManager {

	public HashMap<String, ArrayList<String>> gamemode_serverIds = new HashMap<String, ArrayList<String>>();	
	public HashMap<String, Integer> gamemode_minServer = new HashMap<String, Integer>();
	public HashMap<String, String> serverId_adresse = new HashMap<String, String>();
	public HashMap<String, Integer> serverId_port = new HashMap<String, Integer>();
	public ArrayList<String> servers = new ArrayList<String>();
	
	public String getServername(String gamemode) {
		gamemode = gamemode.toUpperCase();
		
		if(!gamemode_serverIds.containsKey(gamemode)) gamemode_serverIds.put(gamemode, new ArrayList<String>());
		
		int i = 1;
		while(gamemode_serverIds.get(gamemode).contains(gamemode + "#" + i)) {
			i ++;
		}
		
		String serverName = gamemode + "#" + i;
		return serverName;
	}
	
	public void addServer(String gamemode, String servername, String adresse, int port) {
		gamemode = gamemode.toUpperCase();
		gamemode_serverIds.get(gamemode).add(servername);
		serverId_adresse.put(servername, adresse);
		serverId_port.put(servername, port);
		servers.add(servername);
	}
	
	public void removeServer(String gamemode, String servername) {
		gamemode = gamemode.toUpperCase();
		gamemode_serverIds.get(gamemode).remove(servername);
		serverId_adresse.remove(servername);
		serverId_port.remove(servername);
		servers.remove(servername);
	}
	
	
	
	
}
