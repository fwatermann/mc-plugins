package de.legitfinn.replaysystem.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import de.legitfinn.replaysystem.main.Main;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

public class NPC extends Reflections {

	int entityID;
	Location location;
	GameProfile gameprofile;
	String name;
	boolean onTablist = false;

	public NPC(String name, Location location, int id, UUID uuid) {
		if(id == 0) {
			entityID = (int) Math.ceil(Math.random() * 1000) + 2000;
		} else {
			entityID = id;
		}
		this.name = name;
		gameprofile = new GameProfile(uuid, name);
		this.location = location;
	}
	
	public NPC(String name, Location location, int id, UUID uuid, String[] textures) {
		if(id == 0) {
			entityID = (int) Math.ceil(Math.random() * 1000) + 2000;
		} else {
			entityID = id;
		}
		this.name = name;
		gameprofile = new GameProfile(UUID.randomUUID(), name);
		gameprofile.getProperties().put("textures", new Property("textures", textures[0], textures[1]));
		this.location = location;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public String getName() {
		return name;
	}

	public static String randomString() {
		String abc = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,1,2,3,4,5,6,7,8,9,0,_";
		String[] alphabet = abc.split(",");
		String s = "";

		while (s.length() < 10) {
			Random rnd = new Random();
			s = s + alphabet[rnd.nextInt(alphabet.length)];
		}
		return s;
	}
	
	public boolean isOnTablist() {
		return onTablist;
	}
	
	public void setLocation(Location loc) {
		this.location = loc;
	}

	public void spawn() {
		Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), new Runnable() {
			
			@Override
			public void run() {	
				PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
//				Random rnd = new Random();
				Location loc = location.clone();
				setValue(packet, "a", entityID);
				setValue(packet, "b", gameprofile.getId());
				setValue(packet, "c", (int) MathHelper.floor(loc.getX() * 32.0D));
				setValue(packet, "d", (int) MathHelper.floor(loc.getY() * 32.0D));
				setValue(packet, "e", (int) MathHelper.floor(loc.getZ() * 32.0D));
				setValue(packet, "f", (byte) ((int) (loc.getYaw() * 256.0F / 360.0F)));
				setValue(packet, "g", (byte) ((int) (loc.getPitch() * 256.0F / 360.0F)));
				setValue(packet, "h", 0);
				DataWatcher w = new DataWatcher(null);
				w.a(6, (float) 20);
				w.a(10, (byte) Byte.MAX_VALUE);
				setValue(packet, "i", w);
				addToTablist();
				for(Player all : Bukkit.getOnlinePlayers()) {
					sendPacket(packet, all);
				}
//				rmvFromTablist();
		
				DataWatcher dw = new DataWatcher(null);
				dw.a(0, (Object) (byte) 32);
				dw.a(1, (Object) (short) 0);
				dw.a(8, (Object) (byte) 0);
		
//				if (!visible) {
//					PacketPlayOutEntityMetadata packet0 = new PacketPlayOutEntityMetadata(entityID, dw, true);
//					((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet0);
//				}
				teleport(location);
			}
		});
	}
	
	public void setEquipment(ItemStack[] armor, ItemStack sword) {
		ItemStack helmet = armor[0];
		ItemStack chest = armor[1];
		ItemStack leg = armor[2];
		ItemStack boots = armor[3];
		
		PacketPlayOutEntityEquipment packet2 = new PacketPlayOutEntityEquipment();
		setValue(packet2, "a", entityID);
		setValue(packet2, "b", 4);
		setValue(packet2, "c", helmet.getType() == Material.AIR || helmet == null ? CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)) : CraftItemStack.asNMSCopy(helmet));

		PacketPlayOutEntityEquipment packet3 = new PacketPlayOutEntityEquipment();
		setValue(packet3, "a", entityID);
		setValue(packet3, "b", 3);
		setValue(packet3, "c", chest.getType() == Material.AIR || chest == null ? CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)) : CraftItemStack.asNMSCopy(chest));

		PacketPlayOutEntityEquipment packet4 = new PacketPlayOutEntityEquipment();
		setValue(packet4, "a", entityID);
		setValue(packet4, "b", 2);
		setValue(packet4, "c", leg.getType() == Material.AIR || leg == null ? CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)) : CraftItemStack.asNMSCopy(leg));

		PacketPlayOutEntityEquipment packet5 = new PacketPlayOutEntityEquipment();
		setValue(packet5, "a", entityID);
		setValue(packet5, "b", 1);
		setValue(packet5, "c", boots.getType() == Material.AIR || boots == null ? CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)) : CraftItemStack.asNMSCopy(boots));
		
		PacketPlayOutEntityEquipment packet6 = new PacketPlayOutEntityEquipment();
		setValue(packet6, "a", entityID);
		setValue(packet6, "b", 0);
		setValue(packet6, "c", sword.getType() == Material.AIR || sword == null ? CraftItemStack.asNMSCopy(new ItemStack(Material.AIR)) : CraftItemStack.asNMSCopy(sword));
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			sendPacket(packet2, all);
			sendPacket(packet3, all);
			sendPacket(packet4, all);
			sendPacket(packet5, all);
			sendPacket(packet6, all);
		}
	}
	
	
	public void swingArm() {
		PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
		Reflections.setValue(packet, "a", entityID);
		Reflections.setValue(packet, "b", 0);				
		for(Player all : Bukkit.getOnlinePlayers()) {
			sendPacket(packet, all);
		}
	}
	
	public void setVisibility(boolean b) {
		if(!b) {
			DataWatcher dw = new DataWatcher(null);
			dw.a(0, (Object) (byte)32);
			dw.a(1, (Object) (byte)0);
			dw.a(8, (Object) (byte)0);
			PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(this.entityID, dw, true);
			for(Player all : Bukkit.getOnlinePlayers()) {
				sendPacket(packet, all);
			}
		}
	}
	
	public void setHealth(int health) {
		DataWatcher watcher = new DataWatcher(null);
		watcher.a(6, (Float)(float)health);
		PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(this.entityID, watcher, true);
		for(Player all : Bukkit.getOnlinePlayers()) {
			sendPacket(packet, all);
		}
	}
	
	public void teleport(Location location) {
		
		PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
		setValue(packet, "a", entityID);
		setValue(packet, "b", getFixLocation(location.getX()));
		setValue(packet, "c", getFixLocation(location.getY()));
		setValue(packet, "d", getFixLocation(location.getZ()));
		setValue(packet, "e", getFixRotation(location.getYaw()));
		setValue(packet, "f", getFixRotation(location.getPitch()));

//		sendPacket(packet1, player);
		for(Player all : Bukkit.getOnlinePlayers()) {
			sendPacket(packet, all);
		}
		headRotation(location.getYaw(), location.getPitch());
		this.location = location;
	}

	public void headRotation(float yaw, float pitch) {
		PacketPlayOutEntityLook packet = new PacketPlayOutEntityLook(entityID, getFixRotation(yaw),
				getFixRotation(pitch), true);
		PacketPlayOutEntityHeadRotation packetHead = new PacketPlayOutEntityHeadRotation();
		setValue(packetHead, "a", entityID);
		setValue(packetHead, "b", getFixRotation(yaw));

		for(Player all : Bukkit.getOnlinePlayers()) {
			sendPacket(packet, all);
			sendPacket(packetHead, all);
		}
	}
	
	public void playAnimation(int i) {
		PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
		setValue(packet, "a", entityID);
		setValue(packet, "b", i);
		for(Player all : Bukkit.getOnlinePlayers()) {
			sendPacket(packet, all);
		}
	}
	
	public boolean fire = false;
	public boolean sneak = false;
	public boolean sprint = false;
	public boolean usingItem = false;
	
	public void playMetadata(int i) {
		byte b = 0x00;
		if(i == 1) {
			fire = true;
		}
		if(i == 2) {
			sneak = true;
		}
		if(i == 3) {
			sprint = true;
		}
		if(i == 4) {
			usingItem = true;
		}
		if(i == -1) {
			fire = false;
		}
		if(i == -2) {
			sneak = false;
		}
		if(i == -3) {
			sprint = false;
		}
		if(i == -4) {
			usingItem = false;
		}
		if(fire) {
			b += 0x01;
		}
		if(sneak) {
			b += 0x02;
		}
		if(sprint) {
			b += 0x08;
		}
		if(usingItem) {
			b += 0x10;
		}
		DataWatcher dw = new DataWatcher(null);
		dw.a(0, b);
		PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entityID, dw, true);
		for(Player all : Bukkit.getOnlinePlayers()) {
			sendPacket(packet, all);
		}
	}
	
	public void resetMetadata() {
		fire = false;
		sneak = false;
		sprint = false;
		usingItem = false;
		playMetadata(0);
	}
	
	public void shootEntity(EntityType t, Vector v, boolean crit) {
		Location loc = location.clone().add(0, 1.8, 0);
		if(t.equals(EntityType.ENDER_PEARL)) {
			t = EntityType.SNOWBALL;
		} else if(t.equals(EntityType.ARROW)) {
			Arrow ar = (Arrow) loc.getWorld().spawnEntity(loc, t);
			ar.setCritical(crit);
			return;
		}
		Entity ent = loc.getWorld().spawnEntity(loc, t);
		ent.setVelocity(v);
	}

	public void destroy() {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { entityID });
		rmvFromTablist();
		for(Player all : Bukkit.getOnlinePlayers()) {
			sendPacket(packet, all);
		}
	}

	public void addToTablist() {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameprofile, 1, EnumGamemode.NOT_SET, CraftChatMessage.fromString(gameprofile.getName())[0]);
		@SuppressWarnings("unchecked")
		List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(
				packet, "b");
		players.add(data);

		setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
		setValue(packet, "b", players);

		for(Player all : Bukkit.getOnlinePlayers()) {
			sendPacket(packet, all);
		}
		onTablist = true;
	}

	public void rmvFromTablist() {
		PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
		setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
		setValue(packet, "b", Arrays.asList(packet.new PlayerInfoData(gameprofile, 1, EnumGamemode.NOT_SET,
				ChatSerializer.a(gameprofile.getName()))));
		for(Player all : Bukkit.getOnlinePlayers()) {
			sendPacket(packet, all);
		}
		onTablist = false;
	}

	public int getEntityId() {
		return entityID;
	}
	
	public Material getRandomHelmet() {
		Random rnd = new Random();
		int i = rnd.nextInt(6);
		if(i == 0) {
			return Material.LEATHER_HELMET;
		}
		if(i == 1) {
			return Material.CHAINMAIL_HELMET;
		}
		if(i == 2) {
			return Material.GOLD_HELMET;
		}
		if(i == 3) {
			return Material.IRON_HELMET;
		}
		if(i == 4) {
			return Material.DIAMOND_HELMET;
		}
		if(i == 5) {
			return Material.PUMPKIN;
		}
		return Material.AIR;
	}
	
	public Material getRandomChestplate() {
		Random rnd = new Random();
		int i = rnd.nextInt(6);
		if(i == 0) {
			return Material.LEATHER_CHESTPLATE;
		}
		if(i == 1) {
			return Material.CHAINMAIL_CHESTPLATE;
		}
		if(i == 2) {
			return Material.GOLD_CHESTPLATE;
		}
		if(i == 3) {
			return Material.IRON_CHESTPLATE;
		}
		if(i == 4) {
			return Material.DIAMOND_CHESTPLATE;
		}
		return Material.AIR;
	}
	
	public Material getRandomLeggings() {
		Random rnd = new Random();
		int i = rnd.nextInt(6);
		if(i == 0) {
			return Material.LEATHER_LEGGINGS;
		}
		if(i == 1) {
			return Material.CHAINMAIL_LEGGINGS;
		}
		if(i == 2) {
			return Material.GOLD_LEGGINGS;
		}
		if(i == 3) {
			return Material.IRON_LEGGINGS;
		}
		if(i == 4) {
			return Material.DIAMOND_LEGGINGS;
		}
		return Material.AIR;
	}
	
	public Material getRandomBoots() {
		Random rnd = new Random();
		int i = rnd.nextInt(6);
		if(i == 0) {
			return Material.LEATHER_BOOTS;
		}
		if(i == 1) {
			return Material.CHAINMAIL_BOOTS;
		}
		if(i == 2) {
			return Material.GOLD_BOOTS;
		}
		if(i == 3) {
			return Material.IRON_BOOTS;
		}
		if(i == 4) {
			return Material.DIAMOND_BOOTS;
		}
		return Material.AIR;
	}
	
	public Material getRandomSword() {
		Random rnd = new Random();
		int i = rnd.nextInt(6);
		if(i == 0) {
			return Material.WOOD_SWORD;
		}
		if(i == 1) {
			return Material.STONE_SWORD;
		}
		if(i == 2) {
			return Material.GOLD_SWORD;
		}
		if(i == 3) {
			return Material.IRON_SWORD;
		}
		if(i == 4) {
			return Material.DIAMOND_SWORD;
		}
		return Material.IRON_SPADE;
	}

}