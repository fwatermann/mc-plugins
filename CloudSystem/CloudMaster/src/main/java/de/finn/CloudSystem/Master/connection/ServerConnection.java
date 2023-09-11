package de.finn.CloudSystem.Master.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;

import de.finn.CloudSystem.Master.Security.Encryptor;
import de.finn.CloudSystem.Master.main.CloudMaster;

public class ServerConnection implements Runnable {

	private Socket connection;
	
	private boolean authed = false;
	
	private PrintWriter writer;
	private BufferedReader reader;
	
	private String servername = "";
	private String gamemode = "";
	private String map = "";
	private String status = "";
	private int onPlayer = 0;
	private int maxPlayer = 0;
	
	public ServerConnection(Socket client) {
		this.connection = client;
		try {
			writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), Charset.forName("UTF-8")), true);
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
		} catch(IOException ex) {}
	}
	
	
	public void startAuthenticate() {
		try {
			String a = Encryptor.decrypt(reader.readLine(), CloudMaster.instance.key);
			if(a.startsWith("auth")) {
				String[] b = a.split(";");
				if(b[1].equals(CloudMaster.instance.key)) {
					authed = true;
					System.out.println("Server connected [" + connection.getInetAddress().toString().replaceFirst("/", "") + ", " + connection.getPort() + "]");
					sendUpdatePlayerList(CloudMaster.instance.connectionManager.players);
					startWork();
				} else {
					System.out.println("Server connection refused. [Wrong ConnectionKey]");
					this.connection.close();
				}
			} else {
				System.out.println("Server connection refused. [Wrong ConnectionKey]");
				this.connection.close();
			}
		} catch(IOException ex) {
			System.out.println("Server connection refused. [Wrong ConnectionKey]");
			try {
				this.connection.close();
			} catch (IOException e) {}
		}
	}
	
	public void startWork() {
		while(true) {
			try {
				String read = reader.readLine();
				if(read == null) {
					for(ServerConnection all : CloudMaster.instance.connectionManager.serverConnections) {
						if(all.getServername().toLowerCase().contains("lobby") && all.getServername().toLowerCase().contains("#")) {
							all.sendChangeStatus(servername, "ending");
							System.out.println("Send update to " + all.getServername() + " (" + servername + ">updateStatus)");
						}
					}
					System.out.println("Server disconnected! [" + connection.getInetAddress().toString().replaceFirst("/", "") + ", " + connection.getPort() + "]");
					CloudMaster.instance.connectionManager.serverConnections.remove(this);
					CloudMaster.instance.serverManager.removeServer(gamemode, servername);
					
					for(ProxyConnection all : CloudMaster.instance.connectionManager.proxyConnections) {
						all.sendRemoveServer(servername);
					}
					for(WrapperConnection all : CloudMaster.instance.connectionManager.wrapperConnections) {
						all.sendRemoveServer(servername);
					}					
					if(CloudMaster.instance.connectionManager.serverConnections.size() <= 0) {
						CloudMaster.instance.started = false;
					}
					if(CloudMaster.instance.serverManager.gamemode_serverIds.get(gamemode).size() < CloudMaster.instance.serverManager.gamemode_minServer.get(gamemode)) {
						CloudMaster.instance.startServers(gamemode, 1, "RANDOM_Xau87");
					}
					break;
				}
				String a = Encryptor.decrypt(read, CloudMaster.instance.key);
				String cmd = a.split(";")[0];
				String[] args = a.replaceFirst(cmd, "").replaceFirst(";", "").split(";");
				
				if(cmd.equalsIgnoreCase("updateOnPlayers")) {
					int on = Integer.parseInt(args[0]);
					for(ServerConnection all : CloudMaster.instance.connectionManager.serverConnections) {
						if(all.getServername().toLowerCase().contains("lobby") && all.getServername().toLowerCase().contains("#")) {
							all.sendChangeOnPlayer(servername, on);
						}
					}
					this.onPlayer = on;
				} else if(cmd.equalsIgnoreCase("updateMap")) {
					String map = args[0];
					//send SignSystem
					for(ServerConnection all : CloudMaster.instance.connectionManager.serverConnections) {
						if(all.getServername().toLowerCase().contains("lobby") && all.getServername().toLowerCase().contains("#")) {
							all.sendChangeMap(servername, map);
						}
					}
					this.map = map;
				} else if(cmd.equalsIgnoreCase("updateMaxPlayers")) {
					int max = Integer.parseInt(args[0]);
					//send SignSystem
					for(ServerConnection all : CloudMaster.instance.connectionManager.serverConnections) {
						if(all.getServername().toLowerCase().contains("lobby") && all.getServername().toLowerCase().contains("#")) {
							all.sendChangeMaxPlayer(servername, max);
						}
					}
					this.maxPlayer = max;
				} else if(cmd.equalsIgnoreCase("updateStatus")) {
					String status = args[0];
					System.out.println("[" + servername + "] Status changed to [" + status + "]");
					//send SignSystem
					//Bei Stopped löschen + von Proxy entfernen
					for(ServerConnection all : CloudMaster.instance.connectionManager.serverConnections) {
						if(all.getServername().toLowerCase().contains("lobby") && all.getServername().toLowerCase().contains("#")) {
							all.sendChangeStatus(servername, status);
						}
					}	
					for(WrapperConnection all : CloudMaster.instance.connectionManager.wrapperConnections) {
						all.sendServerStatusUpdate(this.servername, status);
					}	
					this.status = status;
					if(status.equalsIgnoreCase("ingame")) {
						CloudMaster.instance.startServers(this.gamemode, 1, "RANDOM_Xau87");
					}
				} else if(cmd.equalsIgnoreCase("info")) {
					this.servername = args[0];
					this.gamemode = args[1];
					this.map = args[2];
					
					if((args[0].toLowerCase().contains("lobby") || args[0].toLowerCase().contains("hub")) && args[0].toLowerCase().contains("#")) {
						for(ServerConnection all : CloudMaster.instance.connectionManager.serverConnections) {
							if(all.getStatus() != null && !all.getStatus().isEmpty()) {
								sendChangeStatus(all.getServername(), all.getStatus());
								sendChangeMap(all.getServername(), all.getMap());
								sendChangeMaxPlayer(all.getServername(), all.getMaxPlayer());
								sendChangeOnPlayer(all.getServername(), all.getOnPlayer());
								try {
									Thread.sleep(1000);
								} catch(InterruptedException ex) {}
							}
						}
					}
				} else if(cmd.equalsIgnoreCase("forceMap")) {
					String map = args[0];
					this.map = map;
					for(WrapperConnection all : CloudMaster.instance.connectionManager.wrapperConnections) {
						all.sendChangeMap(this.servername, this.gamemode, map, true);
					}		
				}
			} catch(IOException ex) {
				for(ServerConnection all : CloudMaster.instance.connectionManager.serverConnections) {
					if(all.getServername().toLowerCase().contains("lobby") && all.getServername().toLowerCase().contains("#")) {
						all.sendChangeStatus(servername, "ending");
						System.out.println("Send update to " + all.getServername() + " (" + servername + ">updateStatus)");
					}
				}
				System.out.println("Server disconnected! [" + connection.getInetAddress().toString().replaceFirst("/", "") + ", " + connection.getPort() + "]");
				CloudMaster.instance.connectionManager.serverConnections.remove(this);
				CloudMaster.instance.serverManager.removeServer(gamemode, servername);
				
				for(ProxyConnection all : CloudMaster.instance.connectionManager.proxyConnections) {
					all.sendRemoveServer(servername);
				}
				for(WrapperConnection all : CloudMaster.instance.connectionManager.wrapperConnections) {
					all.sendRemoveServer(servername);
				}				
				if(CloudMaster.instance.connectionManager.serverConnections.size() <= 0) {
					CloudMaster.instance.started = false;
				}
				if(CloudMaster.instance.serverManager.gamemode_serverIds.get(gamemode).size() < CloudMaster.instance.serverManager.gamemode_minServer.get(gamemode)) {
					CloudMaster.instance.startServers(gamemode, 1, "default");
				}
				break;
			}
		}
	}
	
	public boolean enoughServer(String gamemode) {
		int i = 0;
		for(ServerConnection con : CloudMaster.instance.connectionManager.serverConnections) {
			if(con.getGamemode() != null) {
				if(con.getGamemode().equalsIgnoreCase(gamemode)) {
					if(con.getStatus() != null) {
						if(con.getStatus().equalsIgnoreCase("waiting")) {
							i ++;
						}
					}
				}
			}
		}
		if(i < CloudMaster.instance.serverManager.gamemode_minServer.get(gamemode)) {
			return false;
		} else {
			return true;
		}
	}
	
	public void sendChangeMap(String servername, String map) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Server!");
			return;
		}
		String cmd = "changeMap;" + servername + ";" + map;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public void sendChangeMaxPlayer(String servername, int i) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Server!");
			return;
		}
		String cmd = "changeMaxPlayer;" + servername + ";" + i;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public void sendChangeOnPlayer(String servername, int i) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Server!");
			return;
		}
		String cmd = "changeOnPlayer;" + servername + ";" + i;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public void sendChangeStatus(String servername, String status) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Server!");
			return;
		}
		String cmd = "changeStatus;" + servername + ";" + status;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public void sendFinishForcemap(String servername, String map) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Server!");
			return;
		}
		String cmd = "forcemapFinish;" + servername + ";" + map;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public void sendWrongForcemap(String servername, String maps) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Server!");
			return;
		}
		String cmd = "forcemapFinish;" + servername + ";" + "NONE!=!;" + maps;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public void sendUpdatePlayerList(String players) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Server!");
			return;
		}
		String cmd = "updatePlayerList;" + players;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	
	public String getGamemode() {
		return this.gamemode;
	}
	
	public String getMap() {
		return this.map;
	}
	
	public String getServername() {
		return this.servername;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public int getMaxPlayer() {
		return maxPlayer;
	}
	
	public int getOnPlayer() {
		return onPlayer;
	}


	@Override
	public void run() {
		startAuthenticate();
	}
	
	
	
	
	
}
