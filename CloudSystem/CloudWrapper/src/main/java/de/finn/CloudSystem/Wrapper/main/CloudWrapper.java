package de.finn.CloudSystem.Wrapper.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import de.finn.CloudSystem.Wrapper.Security.Encryptor;
import de.finn.CloudSystem.Wrapper.config.ConfigManager;
import de.finn.CloudSystem.Wrapper.logger.CloudLogger;

public class CloudWrapper {

	public static CloudWrapper instance;
	
	public ConfigManager config;
	public ServerManager serverManager;
	
	private String version = "1.0.0-BETA";
	
	public CloudWrapper() {
		instance = this;
		config = new ConfigManager(new File("./wrapper.ccfg"));
		start();
	}
	
	
	public Socket connection;
	public PrintWriter writer;
	public BufferedReader reader;
	
	public String masterIP = "127.0.0.1";
	public int port = 31215;
	public int serverport = 31216;
	public String key = randomKey();
	
	public void start() {
		//Logger
		System.setOut(new CloudLogger(System.out, false));
		System.setErr(new CloudLogger(System.out, true));	
		
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(Server all : serverManager.servername_server.values()) {
					all.stop();
				}
				try {
					Thread.sleep(1000);
					FileUtils.deleteDirectory(new File("./temp"));
				} catch (InterruptedException | IOException e) {}
			}
		}));
		
		startup();
		printLogo();
		
		config.load();
		if(!config.isSet("Connection.Master.IP")) {
			config.set("Connection.Master.IP", "127.0.0.1");
			config.set("Connection.Master.Port", 31215);
			config.set("Connection.Master.Key", randomKey());
			config.set("Connection.Server.Port", 31217);
		}
		config.load();
		serverManager = new ServerManager();
		serverManager.loadMaps();
		
		masterIP = config.getString("Connection.Master.IP");
		port = config.getInt("Connection.Master.Port");
		serverport = config.getInt("Connection.Server.Port");
		key = config.getString("Connection.Master.Key");
		
		
		try {
			connection = new Socket(masterIP, port);
			writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), Charset.forName("UTF-8")), true);
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
			writer.println(Encryptor.encrypt("auth;" + key, key));
		} catch(IOException ex) {}		
		
		if(writer == null || reader == null || !connection.isConnected()) {
			System.out.println("Could not connect to MasterServer! Check if MasterServer is online under IP-Address <" + masterIP + ">");
			System.out.println("Retrying in 5 seconds...");
			try {
				Thread.sleep(5000);
			} catch(InterruptedException ex) {}
			System.out.println("Rebooting!!!");
			try {
				Thread.sleep(500);
			} catch(InterruptedException ex) {}
			start();
			return;
		}
		
		while(true) {
			try {
				String read = reader.readLine();
				if(read == null) {
					System.out.println("MasterServer disconnected! Rebooting...");
					try {
						connection.close();
						reader.close();
						writer.close();
						reader = null;
						writer = null;
					} catch(IOException ex) {}
					start();
					return;
				}
				String a = Encryptor.decrypt(read, key);
				String cmd = a.split(";")[0];
				String[] args = a.replaceFirst(cmd, "").replaceFirst(";", "").split(";");
				System.out.println(a.replaceFirst(cmd, "").replaceFirst(";", "") + " [Command: " + cmd + "]");
				
				if(cmd.equalsIgnoreCase("createServer")) {
					String gamemode = args[0];
					String servername = args[1];
					String map = args[2];
					if(map.equalsIgnoreCase("RANDOM_Xau87")) {
						serverManager.startServer(servername, gamemode, serverManager.getRandomMap(gamemode.toUpperCase()), masterIP, serverport);
					} else {
						serverManager.startServer(servername, gamemode, map, masterIP, serverport);
					}
				} else if(cmd.equalsIgnoreCase("removeServer")) {
					String servername = args[0];
					serverManager.stopServer(servername);
				} else if(cmd.equalsIgnoreCase("sendLoad")) {
					OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
					send("load;" + os.getSystemLoadAverage());
				} else if(cmd.equalsIgnoreCase("changeMap")) {
					String servername = args[0];
					String gamemode = args[1];
					String map = args[2];
					boolean isForcemap = Boolean.parseBoolean(args[3]);
					serverManager.changeMap(servername, gamemode, map, isForcemap);
				} else if(cmd.equalsIgnoreCase("serverStatusUpdate")) {
					String servername = args[0];
					String status = args[1];
					if(serverManager.servername_server.containsKey(servername)) {
						serverManager.servername_server.get(servername).setStatus(status);
					}
				}
			} catch(IOException ex) {
				System.out.println("MasterServer disconnected! Rebooting...");
				try {
					connection.close();
					reader.close();
					writer.close();
					connection = null;
					reader = null;
					writer = null;
				} catch (IOException e) {}
				start();
				return;
			}
		}
	}
	
	public void send(String cmd) {
		if(writer != null) {
			writer.println(Encryptor.encrypt(cmd, key));
		}
	}
	
	public void startup() {
		
		File templates = new File("./templates/");
		if(!templates.exists()) templates.mkdir();
		
		File templatesMaps = new File("./templates/Maps");
		if(!templatesMaps.exists()) templatesMaps.mkdir();
		
		File templateServers = new File("./templates/Servers");
		if(!templateServers.exists()) templateServers.mkdir();
		
		File tmp = new File("./temp");
		if(tmp.exists()) {
			try {
				FileUtils.deleteDirectory(new File("./temp"));
				Thread.sleep(1000);
				System.out.println("Deleted [tmp] directory!");
			} catch(IOException | InterruptedException ex) {}
		}
		if(!tmp.exists()) tmp.mkdir();
		
	}
	
	public void printLogo() {
		System.out.println("   _____  _                    _   _____              _                   ");
		System.out.println("  / ____|| |                  | | / ____|            | |                  ");
		System.out.println(" | |     | |  ___   _   _   __| || (___   _   _  ___ | |_  ___  _ __ ___  ");
		System.out.println(" | |     | | / _ \\ | | | | / _` | \\___ \\ | | | |/ __|| __|/ _ \\| '_ ` _ \\ ");
		System.out.println(" | |____ | || (_) || |_| || (_| | ____) || |_| |\\__ \\| |_|  __/| | | | | |");
		System.out.println("  \\_____||_| \\___/  \\__,_| \\__,_||_____/  \\__, ||___/ \\__|\\___||_| |_| |_|");
		System.out.println("                                           __/ |                          ");
		System.out.println("                                          |___/                           ");
		System.out.println("[WRAPPER] Version: " + version + "");
		System.out.println("");
		System.out.println("");
	}
	
	public String randomKey() {
		String a = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0";
		String[] b = a.split(",");
		String c = "";
		Random d = new Random();
		while(c.length() <= 64) {
			c += b[d.nextInt(b.length)];
		}
		return c;
	}

}
