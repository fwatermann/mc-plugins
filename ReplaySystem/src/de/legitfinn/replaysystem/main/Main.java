package de.legitfinn.replaysystem.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import de.legitfinn.replaysystem.commands.COMMAND_replay;
import de.legitfinn.replaysystem.methoden.PacketReader;
import de.legitfinn.replaysystem.methoden.ReplayPlayer;
import de.legitfinn.replaysystem.methoden.ReplayRecorder;

public class Main extends JavaPlugin {

	//Variables
	public static ProtocolManager protocol;
	public static String path = "/home/PvPFun/replays";
//	public static String path = "./replays";
	public static String replayId = "";
	public static boolean canPlayReplay = true;
	public static boolean canRecordReplay = true;
	public static HashMap<String, String> sonder_record = new HashMap<String, String>();
	
	public void onEnable() {
		try{
			FileUtils.deleteDirectory(new File("./replayWorld"));
		} catch(IOException ex){}
		Bukkit.getPluginCommand("replay").setExecutor(new COMMAND_replay());
		replayId = generateRandomId();
		protocol = ProtocolLibrary.getProtocolManager();
		PacketReader.register();
		PacketReader.register2();
		Bukkit.getPluginManager().registerEvents(new ReplayRecorder(), this);
		Bukkit.getPluginManager().registerEvents(new ReplayPlayer(), this);
		loadSonderSymbols();
	}
	
	public void onDisable() {
		
	}
	
	
	public static String generateRandomId() {
		String[] abc = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0".split(",");
		String ret = "";
		Random rnd = new Random();
		while(ret.length() <= 8) {
			ret += abc[rnd.nextInt(abc.length)];
		}
		replayId = ret;
		return ret;
	}

	public void loadSonderSymbols() {
		sonder_record.put("█", "/>FBlock/>");
		sonder_record.put("❤", "/>FHart/>");
		sonder_record.put("❥", "/>HHart/>");
		sonder_record.put("✔", "/>Harken/>");
		sonder_record.put("✖", "/>BCross/>");
		sonder_record.put("✗", "/>Cross1/>");
		sonder_record.put("✘", "/>Cross2/>");
		sonder_record.put("➤", "/>FArrowRight/>");
		sonder_record.put("⬛", "/>HBlock/>");
		sonder_record.put("⬜", "/>HBlockEmpty/>");
		sonder_record.put("»", "/>DArrowRight/>");
		sonder_record.put("«", "/>DArrowLeft/>");
		sonder_record.put("§", "&");
	}
	
	
}

