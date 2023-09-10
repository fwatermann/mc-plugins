package de.legitfinn.knockbackffa.methoden;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import de.legitfinn.knockbackffa.main.DataManager;
import de.legitfinn.knockbackffa.main.main;
import de.legitfinn.replaysystem.main.Main;

public class MapItems {

	public static boolean hasListed = false;
	public static int id, time;
	
	public static ArrayList<Location> locs = new ArrayList<Location>();
	
	public static void spawnItems() {
		time = 20 * 20;
		int add = 30 - Bukkit.getOnlinePlayers().size();
		time += add;
		
		if(!hasListed) {
			time = 10;
		}
		
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(main.class), new Runnable() {
			
			@Override
			public void run() {
				if(time == 0){
					if(hasListed && locs.size() > 0) {
						Random rnd = new Random();
						Location loc = locs.get(rnd.nextInt(locs.size()));
						
						for(Entity all : loc.getWorld().getEntities()) {
							if(all.getType().equals(EntityType.DROPPED_ITEM)) {
								if(all.getLocation().distance(loc) < 2) {
									return;
								}
							}
						}					
						Item item = loc.getWorld().dropItem(loc, getRandomItem());
						item.teleport(loc);			
					} else {
						Location loc = ConfigManager.getSpawn().clone();
						loc.add(0, 10, 0);
						Item item = loc.getWorld().dropItem(loc, getRandomItem());
						item.teleport(loc);
						Vector v = randomVector();
						item.setVelocity(v);				
					}
					time = 20*20;
					int add = 30 - Bukkit.getOnlinePlayers().size();
					time += add;
				}
				time--;
			}
		}, 0, 20);
		
		
	}
	
	public static Vector randomVector() {
		Random rnd = new Random();
		int a = rnd.nextInt(2) -1;
		if(a == 0) {
			a = 1;
		}
		int b = rnd.nextInt(2) - 1;
		if(b == 0) {
			b = 1;
		}
		Vector v = new Vector(rnd.nextDouble() * a, rnd.nextDouble(), rnd.nextDouble() * b);
		return v;
	}
	
	
	public static void loadLocations() {
		hasListed = false;
		
		File file = new File("./Map/KnockbackFFA/items.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(cfg.isSet("spawns")) {
			ArrayList<Location> a = new ArrayList<Location>();
			for(String all : cfg.getStringList("spawns")) {
				String[] coords = all.split(";");
				double x = Double.parseDouble(coords[0]);
				double y = Double.parseDouble(coords[1]);
				double z = Double.parseDouble(coords[2]);
				
				Location loc = new Location(DataManager.map, x, y, z);
				System.out.println("Loaded Location: " + loc);
				a.add(loc);
			}
			locs.clear();
			locs.addAll(a);
			System.out.println(locs.size() + " | " + a.size());
			if(a.size() > 0) {
				hasListed = true;
			}
		} else {
			hasListed = false;
			ArrayList<String> demo = new ArrayList<String>();
			cfg.set("spawns", demo);
			try {
				cfg.save(file);
			} catch(IOException ex) {}
		}
	}
	
	public static ItemStack getRandomItem() {
		
		ItemStack gran = new ItemStack(Material.EGG, 1);
		ItemMeta granm = gran.getItemMeta();
		granm.setDisplayName("§bGranate");
		gran.setItemMeta(granm);
		
		ItemStack jetp = new ItemStack(Material.FLINT_AND_STEEL);
		ItemMeta jetpm = jetp.getItemMeta();
		jetpm.setDisplayName("§6Jetpack");
		jetpm.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		jetpm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		jetp.setItemMeta(jetpm);
		
		ItemStack enter = new ItemStack(Material.FISHING_ROD);
		ItemMeta em = enter.getItemMeta();
		em.setDisplayName("§aEnterhaken");
		em.spigot().setUnbreakable(true);
		enter.setItemMeta(em);
		
		ItemStack mine = new ItemStack(Material.WOOD_PLATE);
		ItemMeta minem = mine.getItemMeta();
		minem.setDisplayName("§6Mine");
		minem.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		minem.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		mine.setItemMeta(minem);
		
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		
		list.add(gran);
		list.add(jetp);
		list.add(enter);
		list.add(mine);
		
		Random rnd = new Random();
		return list.get(rnd.nextInt(list.size()));		
	}
	
	
	
	
	
}
