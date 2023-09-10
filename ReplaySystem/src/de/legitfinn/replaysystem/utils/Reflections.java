package de.legitfinn.replaysystem.utils;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.Packet;


public class Reflections {
	public static void setValue(Object obj, String name, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
		}
	}

	public static Object getValue(Object obj, String name) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
		}
		return null;
	}

	public static void sendPacket(Packet<?> packet, Player player) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
    public static int getFixLocation(double pos){
        return (int)MathHelper.floor(pos * 32.0D);
    }
    public static byte getFixRotation(float yawpitch){
        return (byte) ((int) (yawpitch * 256.0F / 360.0F));
    }

}