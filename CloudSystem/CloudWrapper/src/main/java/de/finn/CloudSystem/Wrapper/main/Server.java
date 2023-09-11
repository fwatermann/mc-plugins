package de.finn.CloudSystem.Wrapper.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;

public class Server {

	private String servername;
	private String gamemode;
	private String map;
	private String status = "WAITING";
	private int port;
	private int cloudport;
	
	private long waitToNextCheck = 8000;
	private Thread checkThread = null;
	
	private Process process;
	private boolean running = true;
	
	private String masterIP = "127.0.0.1";
	private String externalIP = "127.0.0.1";
	
	public Server(String servername, String gamemode, String map, int port, String masterIP, int cloudport) {
		this.servername = servername;
		this.gamemode = gamemode;
		this.map = map;
		this.port = port;
		this.masterIP = masterIP;
		this.cloudport = cloudport;
		externalIP = getExternalIp();
		this.masterIP.split("");
	}
	
	private String getExternalIp() {
		try {
			URL wimip = new URL("http://checkip.amazonaws.com/");
			BufferedReader in = new BufferedReader(new InputStreamReader(wimip.openStream()));
			String ip = in.readLine();
			if(in != null) {
				in.close();
			}
			return ip;
		} catch(IOException ex) {
			return "127.0.0.1";
		}
	}

	public String getServername() {
		externalIP.length();
		return servername;
	}

	public String getGamemode() {
		return gamemode;
	}

	public String getMap() {
		return map;
	}

	public int getPort() {
		return port;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public void create() {
		try {
			File servert = new File("./templates/Servers/" + gamemode);
			if(!servert.exists()) servert.mkdir();
			
			File mapt = new File("./templates/Maps/" + gamemode + "/" + map);
			if(!mapt.exists()) mapt.mkdirs();
			
			FileUtils.copyDirectory(new File("./templates/Servers/" + gamemode), new File("./temp/" + gamemode + "/" + servername));
			FileUtils.copyDirectory(new File("./templates/Maps/" + gamemode + "/" + map), new File("./temp/" + gamemode + "/" + servername + "/Map"));
			
			for(File all : new File("./templates/defaultPlugins/").listFiles()) {
				if(all.isFile()) {
					FileUtils.copyFile(all, new File("./temp/" + gamemode + "/" + servername + "/plugins/" + all.getName()));
				} else if(all.isDirectory()) {
					FileUtils.copyDirectory(all, new File("./temp/" + gamemode + "/" + servername + "/plugins/" + all.getName()));
				}
			}		
			
			//Server-Properties

			Properties prop = new Properties();
			
			File pr = new File("./temp/" + gamemode + "/" + servername + "/server.properties");
			if(!pr.exists()) pr.createNewFile();
			
			prop.load(new FileInputStream(new File("./temp/" + gamemode + "/" + servername + "/server.properties")));
			prop.put("server-ip", "");
			prop.put("server-port", "" + port);
			prop.put("online-mode", "false");
			prop.put("cloud-port", "" + cloudport);
			prop.put("cloud-gamemode", gamemode);
			prop.put("cloud-servername", servername);
			prop.put("cloud-map", map);
			prop.put("spawn-protection", "0");
			prop.store(new FileOutputStream(new File("./temp/" + gamemode + "/" + servername + "/server.properties")), "Properties changed by CloudSystem (by Finn W.)");

			FileConfiguration conf = YamlConfiguration.loadConfiguration(new File("./temp/" + gamemode + "/" + servername + "/spigot.yml"));
			conf.set("world-settings.default.anti-xray.enabled", false);
			conf.set("messages.unknown-command", "�7[�bCloud�7] �cUnbekannter Befehl. Mit �e\"/help\" �cbekommst du weitere Hilfe.");
			conf.save(new File("./temp/" + gamemode + "/" + servername + "/spigot.yml"));
		
		} catch(IOException ex) {}
	}
	
	public void start() {
		try {
			System.out.println("Starting Server");
			ProcessBuilder builder = new ProcessBuilder(new String[] {"java", "-server", "-jar", "spigot.jar"});
			builder.directory(new File("./temp/" + gamemode + "/" + servername + "/"));
			process = builder.start();
			System.out.println("Started Server");
			checkRunning();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void checkRunning() {
		checkThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					if(!running) break;
					try {
						Thread.sleep(waitToNextCheck);
					} catch (InterruptedException e) {}
					if(!running) break;
					waitToNextCheck = 30000L;
					if(process != null) {
						boolean stop = false;
						if(!process.isAlive()) {
							System.out.println("Check running [" + servername + "] > NOT ALIVE! DELETING!");
							stop = true;
						} else {
							System.out.println("Check running [" + servername + "] > ALIVE!");
						}
						analyseLog();
						if(stop) CloudWrapper.instance.serverManager.stopServer(servername);
					}
				}
			}
		});
		checkThread.setName("CheckRunning - " + servername);
		checkThread.start();
	}
	
	public void stop() {
		if(process != null) {
			process.destroy();
			process.destroyForcibly();
		}
		if(running) {
			running = false;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy_HH:mm:ss");
				FileUtils.copyDirectory(new File("./temp/" + gamemode + "/" + servername + "/logs"), new File("./keepLogs/" + gamemode + "/" + sdf.format(new Date()) + "_(" + servername + ")"));
				FileUtils.deleteDirectory(new File("./temp/" + gamemode + "/" + servername));
			} catch (IOException e) {}
		}
	}
	
	public void changeMap(String gamemode, String map, boolean isForcemap) {
		if(map != null) {
			try {
				File gmd = new File("./temp/" + this.gamemode);
				if(!gmd.exists()) gmd.mkdir();			
				FileUtils.deleteDirectory(new File("./temp/" + this.gamemode + "/" + servername + "/Map"));
				FileUtils.copyDirectory(new File("./templates/Maps/" + gamemode + "/" + map), new File("./temp/" + this.gamemode + "/" + servername + "/Map"));			
			} catch(IOException ex) {}
			
			this.map = map;
			if(isForcemap) {
				CloudWrapper.instance.send("finishForcemap;" + this.servername + ";" + this.gamemode + ";" + map);
			}
		} else {
			String maps = "";
			for(String all : CloudWrapper.instance.serverManager.gamemode_maps.get(gamemode)) {
				maps += "<>" + all;
			}
			maps = maps.replaceFirst("<>", "");
			CloudWrapper.instance.send("finishForcemap;" + this.servername + ";" + this.gamemode + ";NONE!=!;" + maps);
		}
	}
	
	private void analyseLog() {
		File file = new File("./temp/" + gamemode + "/" + servername + "/logs/latest.log");
		if(file.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("UTF-8")));
				String tmp = null;
				boolean badPort = false;
				boolean cloud = false;
				boolean mysql = false;
				while((tmp = reader.readLine()) != null) {
					if(tmp.toLowerCase().contains("**** FAILED TO BIND TO PORT!".toLowerCase())) {
						badPort = true;
					}
					if(tmp.toLowerCase().contains("Could not create connection to database server. Attempted reconnect 3 times. Giving up.".toLowerCase())) {
						mysql = true;
					}
					if(tmp.toLowerCase().contains("Error occurred while enabling CloudSpigotConnector".toLowerCase())) {
						cloud = false;
					}
				}
				reader.close();
				
				if(badPort) {
					CloudWrapper.instance.serverManager.blackList_ports.add("" + port);
					System.out.println("BLACKLISTED PORT: " + port + " (because of blocked port)");
					CloudWrapper.instance.serverManager.stopServer(servername);
				}
				if(cloud) {
					System.out.println("Server crashed because connection to cloud does not work!");
					CloudWrapper.instance.serverManager.stopServer(servername);
				}
				if(mysql) {
					System.out.println("Server crashed because mysql connection does not work!");
					CloudWrapper.instance.serverManager.stopServer(servername);
				}
				
				
			} catch (IOException e) {
				System.out.println("Error while analysing Log of " + servername + ": " + e.getMessage());
			}		
		}
	}
	
	
	
	
}
