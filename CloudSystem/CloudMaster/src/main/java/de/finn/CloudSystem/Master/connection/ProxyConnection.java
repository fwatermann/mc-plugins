package de.finn.CloudSystem.Master.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;

import de.finn.CloudSystem.Master.Security.Encryptor;
import de.finn.CloudSystem.Master.main.CloudMaster;

public class ProxyConnection implements Runnable{

	private Socket connection;
	private boolean authed = false;
	
	private PrintWriter writer;
	private BufferedReader reader;
	
	
	public ProxyConnection(Socket client) {
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
					System.out.println("Proxy connected [" + connection.getInetAddress().toString().replaceFirst("/", "") + ", " + connection.getPort() + "]");
					for(String s : CloudMaster.instance.serverManager.servers) {
						sendAddServer(s, CloudMaster.instance.serverManager.serverId_adresse.get(s), CloudMaster.instance.serverManager.serverId_port.get(s));
						System.out.println("Added server to Proxy [" + s + "]");
					}
					startWork();
				} else {
					System.out.println("Proxy connection refused. [Wrong ConnectionKey]");
				}
			}
		} catch(IOException ex) {}
	}
	
	public void startWork() {
		while(true) {
			try {
				String a = reader.readLine();
				if(a == null) {
					System.out.println("Proxy disconnected! [" + connection.getInetAddress().toString().replaceFirst("/", "") + ", " + connection.getPort() + "]");
					CloudMaster.instance.connectionManager.proxyConnections.remove(this);
					return;
				}
				String read = Encryptor.decrypt(a, CloudMaster.instance.key);
				String cmd = read.split(";")[0];
				String[] args = read.replaceFirst(cmd, "").replaceFirst(";", "").split(";");
				
				if(cmd.equalsIgnoreCase("updatePlayerList")) {
					CloudMaster.instance.connectionManager.players = args[0];
					for(ServerConnection all : CloudMaster.instance.connectionManager.serverConnections) {
						all.sendUpdatePlayerList(args[0]);
					}
				} else if(cmd.equalsIgnoreCase("startServer")) {
					String gamemode = args[0];
					String map = args[1];
					if(map.equalsIgnoreCase("RANDOM32863478")) map = "RANDOM_Xau87";
					CloudMaster.instance.startServers(gamemode, 1, map);
				} else if(cmd.equalsIgnoreCase("stopServer")) {
					String servername = args[0];
					for(WrapperConnection all : CloudMaster.instance.connectionManager.wrapperConnections) {
						all.sendRemoveServer(servername);
					}
				} else if(cmd.equalsIgnoreCase("restartGamemode")) {
					String gamemode = args[0];
					Thread stopThread = new Thread(new Runnable() {
						@SuppressWarnings("unchecked")
						@Override
						public void run() {
							ArrayList<String> servers = (ArrayList<String>) CloudMaster.instance.serverManager.servers.clone();
							for(String all : servers) {
								if(all.toLowerCase().startsWith(gamemode.toLowerCase())) {
									for(WrapperConnection wc : CloudMaster.instance.connectionManager.wrapperConnections) {
										wc.sendRemoveServer(all);
									}
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {}
								}
							}
						}
					});
					stopThread.start();
				}
			} catch (IOException e) {
				System.out.println("Proxy disconnected! [" + connection.getInetAddress().toString().replaceFirst("/", "") + ", " + connection.getPort() + "]");
				CloudMaster.instance.connectionManager.proxyConnections.remove(this);
				return;
			}
		}
	}
	
	public void sendAddServer(String servername, String address, int port) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Proxy!");
			return;
		}
		String cmd = "addServer;" + servername + ";" + address + ";" + port;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public void sendRemoveServer(String servername) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Proxy!");
			return;
		}
		String cmd = "removeServer;" + servername;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public void sendKickPlayer(String uuid) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Proxy!");
			return;
		}
		String cmd = "kickPlayer;" + uuid;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public void sendMovePlayer(String uuid, String server) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Proxy!");
			return;
		}
		String cmd = "movePlayer;" + uuid + ";" + server;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}

	@Override
	public void run() {
		startAuthenticate();
	}
	
}
