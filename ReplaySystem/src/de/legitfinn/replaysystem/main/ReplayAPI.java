package de.legitfinn.replaysystem.main;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.World;

import de.legitfinn.replaysystem.methoden.ReplayPlayer;
import de.legitfinn.replaysystem.methoden.ReplayRecorder;

public class ReplayAPI {

	private static ReplayAPI api = new ReplayAPI();
	
	public static ReplayAPI getInstance() {
		return api;
	}
	
	public void broadCastMessage(String msg) {
		Bukkit.broadcastMessage(msg);
		ReplayRecorder.recordBroadcastMessage(msg);
	}
	
	public void startRecording(World w, String id) {
		ReplayRecorder.start(w, id);
	}
	
	public void startRecording(World w) {
		if(Main.canRecordReplay) {
			if(!isRecording()) {
				ReplayRecorder.start(w, null);
			} else {
				Bukkit.broadcastMessage("§7[§bReplay§7] §cEs kann keine Replayaufnahme gestartet werden, wenn bereits eine Replayaufnahme läuft.");
			}
		} else {
			Bukkit.broadcastMessage("§7[§bReplay§7] §cDie Replayaufnahme ist auf diesem Server deaktiviert.");
		}
	}
	
	public boolean isRecording() {
		return ReplayRecorder.start != -1;
	}
	
	public void stopRecording() {
		if(isRecording()) {
			ReplayRecorder.stop();
		} else {
			Bukkit.broadcastMessage("§7[§bReplay§7] §cUm eine Replayaufnahme zu stoppen, muss erst eine Replayaufnahme gestartet werden.");
		}
	}
	
	public void playReplay(String id) {
		if(Main.canPlayReplay) {
			ReplayPlayer.play(id);
		} else {
			Bukkit.broadcastMessage("§7[§bReplay§7] §cDas Abspielen von Replays ist auf diesem Server deaktiviert.");
		}
	}
	
	public String generateNewRandomId() {
		String[] abc = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0".split(",");
		String ret = "";
		Random rnd = new Random();
		while(ret.length() <= 8) {
			ret += abc[rnd.nextInt(abc.length)];
		}
		Main.replayId = ret;
		return ret;
	}
	
	public String getReplayId() {
		return Main.replayId;
	}
	
	public String toRecordSymbol(String sonder) {
		return Main.sonder_record.get(sonder);
	}
	
	public String toSonderSymbolFromRecordSymbol(String record) {
		for(String all : Main.sonder_record.keySet()) {
			if(Main.sonder_record.get(all).equalsIgnoreCase(record)) {
				return all;
			}
		}
		return null;
	}
	
	public String toRecordString(String s) {
		String ret = s;
		for(String all : Main.sonder_record.keySet()) {
			ret = ret.replace(all, Main.sonder_record.get(all));
		}
		return ret;
	}
	
	public String toSonderString(String s) {
		String ret = s;
		for(String all : Main.sonder_record.keySet()) {
			ret = ret.replace(Main.sonder_record.get(all), all);
		}
		return ret;
	}
	
	
}
