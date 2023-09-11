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

public class WrapperConnection implements Runnable {

	private Socket connection;
	
	public boolean authed = false;
	
	private double load = 0.0;
	
	private PrintWriter writer;
	private BufferedReader reader;
	
	
	public WrapperConnection(Socket client) {
		this.connection = client;
		try {
			writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), Charset.forName("UTF-8")), true);
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
		} catch(IOException ex) {}
	}
	
	@Override
	public void run() {
		startAuthenticate();
	}
	
	public void startAuthenticate() {
		try {
			String a = Encryptor.decrypt(reader.readLine(), CloudMaster.instance.key);
			if(a.startsWith("auth")) {
				String[] b = a.split(";");
				if(b[1].equals(CloudMaster.instance.key)) {
					authed = true;
					System.out.println("Wrapper connected [" + connection.getInetAddress().toString().replaceFirst("/", "") + ", " + connection.getPort() + "]");
					CloudMaster.instance.startup();
					startWork();
				} else {
					System.out.println("Wrapper connection refused. [Wrong ConnectionKey]");
					this.connection.close();
				}
			} else {
				System.out.println("Wrapper connection refused. [Wrong ConnectionKey]");
				this.connection.close();
			}
		} catch(IOException ex) {}
	}
	
	
	public void startWork() {
		while(true) {
			try {
				String read = reader.readLine();
				if(read == null) {
					System.out.println("Wrapper disconnected! [" + connection.getInetAddress().toString().replaceFirst("/", "") + ", " + connection.getPort() + "]");
					CloudMaster.instance.connectionManager.wrapperConnections.remove(this);
					if(CloudMaster.instance.connectionManager.wrapperConnections.size() <= 0) {
						CloudMaster.instance.started = false;
					}
					break;
				}
				String a = Encryptor.decrypt(read, CloudMaster.instance.key);
				String cmd = a.split(";")[0];
				String[] args = a.replaceFirst(cmd, "").replaceFirst(";", "").split(";");
				if(cmd.equalsIgnoreCase("load")) {
					double load = Double.parseDouble(args[0]);
					this.load = load;
				} else if(cmd.equalsIgnoreCase("startedServer")) {
					String servername = args[0];
					String gamemode = args[1];
					int port = Integer.parseInt(args[3]);
					CloudMaster.instance.serverManager.addServer(gamemode, servername, connection.getInetAddress().toString().replaceFirst("/", ""), port);
					for(ProxyConnection con : CloudMaster.instance.connectionManager.proxyConnections) {
						con.sendAddServer(servername, connection.getInetAddress().toString().replaceFirst("/", ""), port);
					}
				} else if(cmd.equalsIgnoreCase("stoppedServer")) {
					String servername = args[0];
					String gamemode = args[1];
					CloudMaster.instance.serverManager.removeServer(gamemode, servername);
					for(ProxyConnection con : CloudMaster.instance.connectionManager.proxyConnections) {
						con.sendRemoveServer(servername);
					}
				} else if(cmd.equalsIgnoreCase("finishForcemap")) {
					String servername = args[0];
					String gamemode = args[1];
					String map = args[2];
					if(!map.equalsIgnoreCase("NONE!=!")) {
						for(ServerConnection all : CloudMaster.instance.connectionManager.serverConnections) {
							if(all.getServername().equalsIgnoreCase(servername) && all.getGamemode().equalsIgnoreCase(gamemode)) {
								all.sendFinishForcemap(servername, map);
								break;
							}
						}
					} else {
						String maps = args[3];
						for(ServerConnection all : CloudMaster.instance.connectionManager.serverConnections) {
							if(all.getServername().equalsIgnoreCase(servername) && all.getGamemode().equalsIgnoreCase(gamemode)) {
								all.sendWrongForcemap(servername, maps);
								break;
							}
						}
					}
				}
				
			} catch(IOException ex) {
				System.out.println("Wrapper disconnected! [" + connection.getInetAddress().toString().replaceFirst("/", "") + ", " + connection.getPort() + "]");
				CloudMaster.instance.connectionManager.wrapperConnections.remove(this);
				if(CloudMaster.instance.connectionManager.wrapperConnections.size() <= 0) {
					CloudMaster.instance.started = false;
				}
				break;
			}
		}
	}
	
	public void sendCreateServer(String servername, String gamemode, String map) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Wrapper!");
			return;
		}
		System.out.println("Sended startServer [" + servername + "] Map: <" + map + ">");
		String cmd = "createServer;" + gamemode + ";" + servername + ";" + map;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public void sendRemoveServer(String servername) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Wrapper!");
			return;
		}
		String cmd = "removeServer;" + servername;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public void sendGetLoad() {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Wrapper!");
			return;
		}
		String cmd = "sendLoad";
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public void sendChangeMap(String servername, String gamemode, String map, boolean isForcemap) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Wrapper!");
			return;
		}
		String cmd = "changeMap;" + servername + ";" + gamemode + ";" + map + ";" + isForcemap;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public void sendServerStatusUpdate(String servername, String status) {
		if(!authed) {
			System.out.println("Trying to send command to not authenticated Wrapper!");
			return;
		}
		String cmd = "serverStatusUpdate;" + servername + ";" + status;
		writer.println(Encryptor.encrypt(cmd, CloudMaster.instance.key));
	}
	
	public double getLoad() {
		return load;
	}	
}
