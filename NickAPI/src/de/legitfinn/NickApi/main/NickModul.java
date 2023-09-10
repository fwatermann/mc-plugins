package de.legitfinn.NickApi.main;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.EnumDifficulty;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutRespawn;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
import net.minecraft.server.v1_8_R3.WorldType;

public class NickModul {

	
	public static void nickPlayerLogin(Player p) {
		if(!isNicked(p.getUniqueId())) {
			CraftPlayer cp = (CraftPlayer)p;
			String name = getName();
			String realname = cp.getName();
			main.nickname_name.put(name, realname);
			main.nickname_uuid.put(name, cp.getUniqueId());
			main.uuid_name.put(cp.getUniqueId(), realname);
			main.uuid_nickname.put(cp.getUniqueId(), name);
			cp.setDisplayName(name);
			
			try{
				getField(GameProfile.class, "name").set(cp.getProfile(), name);
				String[] text = PlayerData.getRandomTexture();
				cp.getProfile().getProperties().clear();
				cp.getProfile().getProperties().put("textures", new Property("textures", text[0], text[1]));
			} catch(Exception ex){
				ex.printStackTrace();
			}
			cp.sendMessage("");
			cp.sendMessage("§7[§cNICK§7] §cDu wirst nun als §e" + name + "§c angezeit.");
			cp.sendMessage("");
		} else {
			throw new IllegalStateException("Player is already nicked!");
		}
	}
	
	public static void nickPlayerOnline(Player p, boolean reloadSkin) {
		if(!isNicked(p.getUniqueId())) {
			CraftPlayer cp = (CraftPlayer)p;
			String realname = cp.getName();
			String name = getName();
			main.nickname_name.put(name, realname);
			main.nickname_uuid.put(name, cp.getUniqueId());
			main.uuid_name.put(cp.getUniqueId(), realname);
			main.uuid_nickname.put(cp.getUniqueId(), name);
			cp.setDisplayName(name);
			
			
			PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(new int[] {cp.getEntityId()});
			playPacket(destroy, p);
			
			PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, cp.getHandle());
			playPacket(info, null);
			
			try{
				getField(GameProfile.class, "name").set(cp.getProfile(), name);
				String[] text = PlayerData.getRandomTexture();
				cp.getProfile().getProperties().clear();
				cp.getProfile().getProperties().put("textures", new Property("textures", text[0], text[1]));
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			new BukkitRunnable() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					PacketPlayOutPlayerInfo info2 = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, cp.getHandle());
					playPacket(info2, null);
					
					PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(cp.getHandle());
					playPacket(spawn, p);
					
					if(reloadSkin) {
						Location loc = p.getLocation();
						Integer sel = p.getInventory().getHeldItemSlot();
						PacketPlayOutRespawn repsawn = new PacketPlayOutRespawn(0, EnumDifficulty.getById(p.getLocation().getWorld().getDifficulty().getValue()),
								WorldType.getType(p.getLocation().getWorld().getWorldType().name()), EnumGamemode.getById(p.getGameMode().getValue()));
						cp.getHandle().playerConnection.sendPacket(repsawn);
						cp.teleport(loc);
						cp.getInventory().setHeldItemSlot(sel);
						cp.updateInventory();
					}
					
					cp.sendMessage("");
					cp.sendMessage("§7[§cNICK§7] §cDu wirst nun als §e" + name + "§c angezeit.");
					cp.sendMessage("");
				}
			}.runTaskLater(main.getPlugin(main.class), 4L);
		} else {
			throw new IllegalStateException("Player is already nicked!");
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void unNickPlayer(Player p, boolean reloadSkin) {
		if(isNicked(p.getUniqueId())) {
			CraftPlayer cp = (CraftPlayer)p;
			String realname = main.uuid_name.get(cp.getUniqueId());
			cp.setDisplayName(realname);
			
			PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(new int[] {cp.getEntityId()});
			playPacket(destroy, p);
			
			PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, cp.getHandle());
			playPacket(info, null);

			try{
				getField(GameProfile.class, "name").set(cp.getProfile(), realname);
				String[] text = PlayerData.getTexture(p.getUniqueId());
				cp.getProfile().getProperties().clear();
				cp.getProfile().getProperties().put("textures", new Property("textures", text[0], text[1]));
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					PacketPlayOutPlayerInfo info2 = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, cp.getHandle());
					playPacket(info2, null);
					
					PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(cp.getHandle());
					playPacket(spawn, p);
					
					if(reloadSkin) {
						Location loc = p.getLocation();
						Integer sel = p.getInventory().getHeldItemSlot();
						PacketPlayOutRespawn repsawn = new PacketPlayOutRespawn(0, EnumDifficulty.getById(p.getLocation().getWorld().getDifficulty().getValue()),
								WorldType.getType(p.getLocation().getWorld().getWorldType().name()), EnumGamemode.getById(p.getGameMode().getValue()));
						cp.getHandle().playerConnection.sendPacket(repsawn);
						cp.teleport(loc);
						cp.getInventory().setHeldItemSlot(sel);
						cp.updateInventory();
					}
					
					main.uuid_name.remove(cp.getUniqueId());
					main.nickname_uuid.remove(main.uuid_nickname.get(cp.getUniqueId()));
					main.nickname_name.remove(main.uuid_nickname.get(cp.getUniqueId()));
					main.uuid_nickname.remove(cp.getUniqueId());		
					
					cp.sendMessage("");
					cp.sendMessage("§7[§cNICK§7] §cDein Nickname wurde entfernt.");
					cp.sendMessage("");
				}
			}.runTaskLater(main.getPlugin(main.class), 4L);			
		} else {
			throw new IllegalStateException("Player is not nicked!");
		}
	}
	
	private static String getName() {
		String name = main.getRandomName();
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(all.getName().equalsIgnoreCase(name)) {
				return getName();
			}
		}
		return name;
	}
	
	public static boolean isNicked(UUID uuid) {
		if(main.uuid_name.containsKey(uuid)) {
			return true;
		}
		return false;
	}
	
	private static void playPacket(Packet<?> packet, Player not) {
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(not != null && !all.getUniqueId().equals(not.getUniqueId())) {
				((CraftPlayer)all).getHandle().playerConnection.sendPacket(packet);
			} else {
				if(not == null) {
					((CraftPlayer)all).getHandle().playerConnection.sendPacket(packet);
				}
			}
		}
	}
	
	private static Field getField(Class<?> clazz, String name) {
		try{
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		} catch(SecurityException | NoSuchFieldException ex) {}
		return null;
	}
	
}
