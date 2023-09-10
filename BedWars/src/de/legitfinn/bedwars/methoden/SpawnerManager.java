package de.legitfinn.bedwars.methoden;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import de.legitfinn.bedwars.main.BedWars;
import de.legitfinn.bedwars.main.DataManager;
public class SpawnerManager {

	public static int gold;
	public static int eisen;
	public static int bronze;
	public static double bronzeSpeed = 1.0D;
	public static int bronzeCount = 0;
	
	
	public static void startSpawners() {
		startGold();
		startEisen();
		startBronze();
	}
	
	public static void stopSpawners() {
		Bukkit.getServer().getScheduler().cancelTask(gold);
		Bukkit.getServer().getScheduler().cancelTask(eisen);
		Bukkit.getServer().getScheduler().cancelTask(bronze);
	}
	
	public static void startGold() {
		gold = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BedWars.getPlugin(BedWars.class), new Runnable() {
			
			@Override
			public void run() {
				ItemStack gold = new ItemStack(Material.GOLD_INGOT);
				ItemMeta meta = gold.getItemMeta();
				meta.setDisplayName("§6Gold");
				gold.setItemMeta(meta);
				
				for(Location all : DataManager.loc_spawner.keySet()) {
					if(DataManager.loc_spawner.get(all).equals(SpawnerType.Gold)) {
						if(all != null) {
							Item i = all.getWorld().dropItem(all, gold);
							i.setVelocity(new Vector(0, 0.2, 0));
						}
					}
				}
			}
		}, 30 * 20, 30 * 20);
	}
	
	public static void startEisen() {
		eisen = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BedWars.getPlugin(BedWars.class), new Runnable() {
			
			@Override
			public void run() {
				ItemStack eisen = new ItemStack(Material.IRON_INGOT);
				ItemMeta meta = eisen.getItemMeta();
				meta.setDisplayName("§7Eisen");
				eisen.setItemMeta(meta);
				
				for(Location all : DataManager.loc_spawner.keySet()) {
					if(DataManager.loc_spawner.get(all).equals(SpawnerType.Eisen)) {
						if(all != null) {
							Item i = all.getWorld().dropItem(all, eisen);
							i.setVelocity(new Vector(0, 0.2, 0));
						}
					}
				}
			}
		}, 10 * 20, 10 * 20);
	}
	
	public static void startBronze() {
		bronze = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(BedWars.getPlugin(BedWars.class), new Runnable() {
			
			@Override
			public void run() {
				ItemStack bronze = new ItemStack(Material.CLAY_BRICK);
				ItemMeta meta = bronze.getItemMeta();
				meta.setDisplayName("§cBronze");
				bronze.setItemMeta(meta);
				
				for(Location all : DataManager.loc_spawner.keySet()) {
					if(DataManager.loc_spawner.get(all).equals(SpawnerType.Bronze)) {
						if(all != null) {
							Item i = all.getWorld().dropItem(all, bronze);
							i.setVelocity(new Vector(0, 0.2, 0));
						}
					}
				}
				bronzeCount ++;
				if(bronzeCount == 128) {
					bronzeSpeed = 0.5D;
					Bukkit.getServer().getScheduler().cancelTask(SpawnerManager.bronze);
					startBronze();
				}
			}
		}, 0, (long)(bronzeSpeed * 20));
	}
	
	public static void loadSpawners() {
		int a = 1;
		while(MapData.getSpawner(SpawnerType.Gold, a) != null) {
			a ++;
		}
		int b = 1;		
		while(MapData.getSpawner(SpawnerType.Eisen, b) != null) {
			b ++;
		}
		int c = 1;		
		while(MapData.getSpawner(SpawnerType.Bronze, c) != null) {
			c ++;
		}
		a--;
		b--;
		c--;
		
		for(int i = 0; i <= a; i++) {
			DataManager.loc_spawner.put(MapData.getSpawner(SpawnerType.Gold, i), SpawnerType.Gold);
		}
		for(int i = 0; i <= b; i++) {
			DataManager.loc_spawner.put(MapData.getSpawner(SpawnerType.Eisen, i), SpawnerType.Eisen);
		}
		for(int i = 0; i <= c; i++) {
			DataManager.loc_spawner.put(MapData.getSpawner(SpawnerType.Bronze, i), SpawnerType.Bronze);
		}
		System.out.println(a);
		System.out.println(b);
		System.out.println(c);
	}
	
	
	
}
