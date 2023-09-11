package de.finn.CloudSystem.Master.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Random;

import de.finn.CloudSystem.Master.config.ConfigManager;
import de.finn.CloudSystem.Master.connection.ConnectionManager;
import de.finn.CloudSystem.Master.connection.ServerManager;
import de.finn.CloudSystem.Master.connection.WrapperConnection;
import de.finn.CloudSystem.Master.logger.CloudLogger;

public class CloudMaster {

	public static CloudMaster instance;
	public ConfigManager config;
	public String version = "1.0.0 - BETA";
	
	public ConnectionManager connectionManager;
	public ServerManager serverManager;
	
	public boolean started = false;
	
	public CloudMaster() {
		instance = this;
		config = new ConfigManager(new File("./cloud.ccfg"));
		start();
	}
	
	public int wrapperPort = 31215;
	public int serverPort = 31216;
	public int proxyPort = 31217;
	public String key = "cloudKey";
	
	private void start() {
		//Logger
		System.setOut(new CloudLogger(System.out, false));
		System.setErr(new CloudLogger(System.out, true));
		
		printLogo();
		System.out.println("Loading configuarion...");
		config.load();
		if(!config.isSet("Connection.Wrapper.Port")) {
			System.out.println("Creating configuration...");
			config.set("Connection.Wrapper.Port", 31215);
			config.set("Connection.Server.Port", 31216);
			config.set("Connection.Proxy.Port", 31217);
			config.set("Connection.Key", randomKey());
			config.rewriteConfig();
			System.out.println("Successfull created configuration.");
		}
		config.reload();
		this.wrapperPort = config.getInt("Connection.Wrapper.Port");
		this.serverPort = config.getInt("Connection.Server.Port");
		this.proxyPort = config.getInt("Connection.Proxy.Port");
		this.key = config.getString("Connection.Key");
		System.out.println("Successfull loaded configuration.");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		System.out.println("Waiting for Wrappers to connect...");
		connectionManager = new ConnectionManager();
		serverManager = new ServerManager();
		connectionManager.start();	
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
		System.out.println("[MASTER] Version: " + version + "");
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
	
	public void startup() {
		System.out.println("Starting Startup... [" + started + "]");
		if(started) {
			return;
		}		
		started = true;
		
		Thread th = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					File f = new File("./servers.ccfg");
					if(!f.exists()) {
						f.createNewFile();
						PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f, true), Charset.forName("UTF-8")), true);
						writer.println("#Startup Konfiguration");
						writer.println("#Nutze <GAMEMODE>=<SERVERAMOUNT>=<MIN-SERVERAMOUNT>");
						writer.println("LOBBY=1");
						writer.flush();
						writer.close();
					}
					FileReader fr = new FileReader(f);
					BufferedReader reader = new BufferedReader(fr);
					String tmp = null;
					while((tmp = reader.readLine()) != null) {
						if(!tmp.toLowerCase().startsWith("#")) {
							String[] args = tmp.split("=");
							if(args.length == 3) {
								String gamemode = args[0].toUpperCase();
								int amount = Integer.parseInt(args[1]);
								int min = Integer.parseInt(args[2]);
								serverManager.gamemode_minServer.put(gamemode.toUpperCase(), min);
								startServers(gamemode, amount, "RANDOM_Xau87");
							}
						}
					}
					reader.close();
					fr.close();
				} catch(IOException ex) {}				
			}
		});		
		th.setName("StartupThread");
		th.start();
	}
	
	public void startServers(String gamemode, int amount, String map) {
		Random rnd = new Random();
		for(int i = 1; i <= amount; i ++) {
			if(connectionManager.wrapperConnections.size() <= 0) {
				started = false;
				return;					
			}
			WrapperConnection con = connectionManager.wrapperConnections.get(rnd.nextInt(connectionManager.wrapperConnections.size()));
			con.sendCreateServer(serverManager.getServername(gamemode), gamemode, map);
			con.sendGetLoad();
			try {
				Thread.sleep(5000);
			} catch(InterruptedException ex) {}
		}
	}

}
