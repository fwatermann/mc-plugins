package de.legitfinn.bedwars.methoden;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;

public class Hologram {

	private ArrayList<EntityArmorStand> all = new ArrayList<EntityArmorStand>();;
	private HashMap<Integer, EntityArmorStand> lines_armorstands = new HashMap<Integer, EntityArmorStand>();
	private String[] lines;
	private Player p;
	private Location loc;
	
	public Hologram(Player p, String[] lines, Location loc) {
		this.lines = lines;
		this.p = p;
		this.loc = loc.clone().subtract(0, 0.75, 0);
	}
	
	public void create() {
		destroy();
		double y = 0.25*lines.length;
		int l = 0;
		for(String line : lines) {
			l ++;
			EntityArmorStand as = new EntityArmorStand(((CraftWorld)loc.getWorld()).getHandle(), loc.getX(), loc.getY() + y, loc.getZ());
			as.setGravity(false);
			as.setCustomName(line);
			as.setCustomNameVisible(true);
			as.setInvisible(true);
			as.setSmall(true);
			y -= 0.25;
			all.add(as);
			lines_armorstands.put(l, as);
		}
		
		for(EntityArmorStand as : all) {
			PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(as);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
		}		
	}
	
	public void destroy() {
		for(EntityArmorStand as : all) {
			as.getWorld().removeEntity(as);
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(as.getId());
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
		}
		all.clear();
		lines_armorstands.clear();
	}
	
}
