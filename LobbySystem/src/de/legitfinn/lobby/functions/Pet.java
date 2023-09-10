package de.legitfinn.lobby.functions;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Wolf;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.Navigation;
import net.minecraft.server.v1_8_R3.PathEntity;

public class Pet {

	public Entity ent;
	public String name;
	public Player p;
	public EntityType et;
	public boolean big;
	public int size = 1;
	
	public Pet(Entity e, Player p) {
		this.p = p;
		this.ent = e;
		this.et = e.getType();
		this.name = "§c" + p.getName() + "'s Pet";
		NBTTagCompound comp = new NBTTagCompound();
		((CraftEntity)ent).getHandle().e(comp);
		comp.setInt("Silent", 1);
		((CraftEntity)ent).getHandle().f(comp);
	}
	
	public void renamePet(String name, boolean show) {
		this.ent.setCustomName(name);
		this.name = name;
		showName(show);
	}
	
	public void showName(boolean b) {
		this.ent.setCustomNameVisible(b);
	}
	
	public void removePet() {
		this.ent.remove();
	}
	
	public EntityType getType() {
		return this.et;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Player getOwner() {
		return this.p;
	}
	
	public boolean isBig() {
		return this.big;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSmall() {
		this.big = false;
	}
	
	public void setBig() {
		this.big = true;
	}
	
	public void stack() {
		p.setPassenger(ent);
	}
	
	public void setSize(int i) {
		this.size = i;
		if(et.equals(EntityType.SLIME)) {
			if(i > 3) {
				i = 1;
				this.size = i;
			}
			Slime slime = (Slime)ent;
			slime.setSize(i);
		} else {
			Animals ani = (Animals)ent;
			if(ani.isAdult()) {
				ani.setBaby();
				ani.setAge(Integer.MIN_VALUE);
				setSmall();
			} else {
				ani.setAdult();
				setBig();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void follow(double speed) {
		if(et.equals(EntityType.WOLF)) {
			Wolf a = (Wolf)ent;
			a.setSitting(false);
		}
		if(et.equals(EntityType.OCELOT)) {
			Ocelot a = (Ocelot)ent;
			a.setSitting(false);
		}
		Location loc = p.getLocation().clone();
		loc.setYaw(loc.getYaw() + 90);
		loc.setPitch(0);
		loc.add(loc.getDirection().multiply(1.0).normalize());
		loc.setYaw(loc.getYaw() - 90);
		if(ent.getLocation().distance(loc) > 15.0D && p.isOnGround()) {
			this.ent.teleport(loc);
			return;
		}
		if(ent.getLocation().distance(loc) <= 2.0D) {
			return;
		}
		LivingEntity e = (LivingEntity)ent;
		EntityInsentient ei = (EntityInsentient)((CraftEntity)e).getHandle();
		Navigation navi = (Navigation)ei.getNavigation();
		navi.a(true);
		PathEntity path = navi.a(loc.getX(), loc.getY(), loc.getZ());
		navi.a(path, speed);
	}
	
}
