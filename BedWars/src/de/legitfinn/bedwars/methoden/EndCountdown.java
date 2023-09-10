package de.legitfinn.bedwars.methoden;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.legitfinn.bedwars.main.BedWars;
import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.replaysystem.main.ReplayAPI;

public class EndCountdown {

	public static int start = 15;
	public static int second = start;
	public static int id;
	public static boolean running = false;

	public static void start() {
		if (running) {
			return;
		}
		running = true;
		second++;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(BedWars.getPlugin(BedWars.class), new Runnable() {

			@Override
			public void run() {
				second--;
				if ((second > 1 && (second + "").endsWith("0")) || (second < 6 && second > 1)) {
					ReplayAPI.getInstance().broadCastMessage(
							DataManager.prefix + "§cDer Server fährt in §e" + second + " Sekunden §cherunter");
				} else if (second == 1) {
					ReplayAPI.getInstance().broadCastMessage(
							DataManager.prefix + "§cDer Server fährt in §e" + second + " Sekunde §cherunter");
				}
				for (Player all : Bukkit.getOnlinePlayers()) {
					all.setLevel(second);
					all.setExp((1F / (float) start) * second);
				}
				if (second == 0) {
					Bukkit.getServer().shutdown();
				}
			}
		}, 0, 20);
	}

}
