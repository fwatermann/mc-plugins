package de.legitfinn.bedwars.methoden;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;

import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.sql.StatisticSQL;
import de.legitfinn.ppermissions.PlayerData.PlayerData;
import de.legitfinn.replaysystem.main.ReplayAPI;

public class Top10 {

	
	
	@SuppressWarnings("deprecation")
	public static void update() {
		Location a = ConfigManager.getTopLocation();
		if(a != null) {
			String dir = ConfigManager.getTopDirection();
			if(dir != null) {
				int x = 0;
				int z = 0;
				if(dir.equalsIgnoreCase("x+")) {
					x = 1;
					z = 0;
				} else if(dir.equalsIgnoreCase("x-")) {
					x = -1;
					z = 0;
				} else if(dir.equalsIgnoreCase("z+")) {
					x = 0;
					z = 1;
				} else if(dir.equalsIgnoreCase("z-")) {
					x = 0;
					z = -1;
				}
				
				byte bf = ConfigManager.getTopFace();
				if(bf != -1) {
					Location c = a.clone();
					HashMap<Integer, UUID> places = StatisticSQL.getPlaces(10, "WINS", "BEDWARS");
					
					for(int b = 1; b <= 10; b ++) {
						if(places.containsKey(b)) {
							if(b != 1) {
								c.add(x, 0, z);
							}
							c.getBlock().setType(Material.WALL_SIGN);
							c.getBlock().setData(bf);
							Sign s = (Sign) c.getBlock().getState();
							getBackBlock(s).setType(Material.STAINED_CLAY);
							getBackBlock(s).setData((byte)3);
							getBackBlock(s).getRelative(BlockFace.UP).setType(Material.STAINED_CLAY);
							getBackBlock(s).getRelative(BlockFace.UP).setData((byte)3);
							s.setLine(0, "<-" + b + "->");
							s.setLine(1, PlayerData.getName(places.get(b)));
							s.setLine(2, "Wins: " + StatisticSQL.getInt("BEDWARS", "WINS", places.get(b)));
							s.setLine(3, "Kills: " + StatisticSQL.getInt("BEDWARS", "KILLS", places.get(b)));
							s.update(true);
							c.clone().add(0, 1, 0).getBlock().setType(Material.SKULL);
							Skull skull = (Skull) c.clone().add(0, 1, 0).getBlock().getState();
							skull.setSkullType(SkullType.PLAYER);
							skull.setOwner(PlayerData.getName(places.get(b)));
							skull.update(true);
						}
					}
				} else {
					ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "§cEin Fehler ist aufgetreten. Bitte gebe einem Administrator oder Developer Bescheid. \n§n§cFehler: Top10 - Facing not set!");
				}
			} else {
				ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "§cEin Fehler ist aufgetreten. Bitte gebe einem Administrator oder Developer Bescheid. \n§n§cFehler: Top10 - Direction not set!");
			}
		} else {
			ReplayAPI.getInstance().broadCastMessage(DataManager.prefix + "§cEin Fehler ist aufgetreten! Bitte gebe einem Administratoren oder Developer Bescheid.\n§n§cFehler: Top10 - Signs not set!");
		}
	}
	
	@SuppressWarnings("deprecation")
	private static Block getBackBlock(Sign sign) {
		if(sign.getBlock().getData() <= 2) {
			return sign.getBlock().getRelative(BlockFace.SOUTH);
		} else if(sign.getBlock().getData() == (byte)3) {
			return sign.getBlock().getRelative(BlockFace.NORTH);
		} else if(sign.getBlock().getData() == (byte)4) {
			return sign.getBlock().getRelative(BlockFace.EAST);
		} else if(sign.getBlock().getData() == (byte)5) {
			return sign.getBlock().getRelative(BlockFace.WEST);
		}
		return null;
	}
	
}
