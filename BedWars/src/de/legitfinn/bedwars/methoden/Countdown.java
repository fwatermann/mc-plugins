package de.legitfinn.bedwars.methoden;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.legitfinn.bedwars.main.BedWars;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.replaysystem.main.ReplayAPI;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class Countdown {

	public static int start = 60;
	public static int second = start;
	public static int id;
	public static boolean running = false;
	public static boolean startCommand = false;
	
	public static void start() {
		if(running) {
			return;
		}
		running = true;
		second ++;
		if(Bukkit.getOnlinePlayers().size() >= 2 || startCommand) {
			id = Bukkit.getScheduler().scheduleSyncRepeatingTask(BedWars.getPlugin(BedWars.class), new Runnable() {
				
				@Override
				public void run() {
					second --;
					if(Bukkit.getOnlinePlayers().size() < 2 && !startCommand) {
						second = start;
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix);
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "§cEs sind nicht genügend Spieler in der Runde.");
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix);
						Bukkit.getScheduler().cancelTask(id);
						running = false;
						return;
					}
					if(!canStart()) {
						second = start;
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix);
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "§cEs sind nicht genügend Teams in der Runde");
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "§cvertreten. Es müssen mindestens zwei Teams mit");
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "§cje einem Spieler vertreten sein.");
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix);
						Bukkit.getScheduler().cancelTask(id);
						running = false;
						return;
					}
					if((second > 1 && (second + "").endsWith("0")) || (second < 6 && second > 1)) { 
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "Die Runde startet in §e" + second  + " Sekunden");
						for(Player all : Bukkit.getOnlinePlayers()) {
							all.playSound(all.getLocation(), Sound.NOTE_BASS, 1, 1);
						}
					} else if(second == 1){
						ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "Die Runde startet in §eeiner Sekunde");
						for(Player all : Bukkit.getOnlinePlayers()) {
							all.playSound(all.getLocation(), Sound.NOTE_BASS, 1, 1);
						}
					}
					for(Player all : Bukkit.getOnlinePlayers()) {
						all.setLevel(second);
						all.setExp((1F / (float)start) * second);
						sendActionBar(all, "§e" + second + " Sekunden§7: " + getSchedulString());
					}
					if(second == 0) {
						second = start;
						Bukkit.getScheduler().cancelTask(id);
						running = false;
						GameStart.start();
						for(Player all : Bukkit.getOnlinePlayers()) {
							all.playSound(all.getLocation(), Sound.LEVEL_UP, 1, 1);
						}
					}
				}
			}, 0, 20);
		} else {
			second = start;
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "§cEs sind nicht genügend Spieler in der Runde.");
			running = false;
		}
	}
	
	public static String getSchedulString() {
		String ret = "§a";
		while(ret.length()-2 < (start) - ((double)second)) {
			ret += "|";
		}
		ret += "§c";
		while(ret.length()-4 < (start)) {
			ret += "|";
		}
		return ret;
	}
	
	public static void sendActionBar(Player p, String msg) {
		IChatBaseComponent ic = ChatSerializer.a("{\"text\":\"" + msg + "\"}");
		PacketPlayOutChat packet = new PacketPlayOutChat(ic, (byte)2);
		((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	}
	
	public static boolean canStart() {
		for(String all : DataManager.team_players.keySet()) {
			if(DataManager.team_players.get(all).size() > 0) {
				if(DataManager.team_players.get(all).size() == Bukkit.getOnlinePlayers().size()) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	
}
