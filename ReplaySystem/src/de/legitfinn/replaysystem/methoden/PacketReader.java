package de.legitfinn.replaysystem.methoden;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import de.legitfinn.replaysystem.main.Main;
import de.legitfinn.replaysystem.utils.FullPlayerMoveEvent;

public class PacketReader {

	public static void register() {
		List<PacketType> l = new ArrayList<PacketType>();
		l.add(PacketType.Play.Client.FLYING);
		l.add(PacketType.Play.Client.POSITION);
		l.add(PacketType.Play.Client.POSITION_LOOK);
		l.add(PacketType.Play.Client.LOOK);
		
		Main.protocol.addPacketListener(new PacketAdapter(Main.getPlugin(Main.class), l) {
			
			public void onPacketReceiving(PacketEvent e) {
				Bukkit.getPluginManager().callEvent(new FullPlayerMoveEvent(e.getPlayer()));
			}
			
		});
	}
	
	public static void register2() {
		List<PacketType> l = new ArrayList<PacketType>();
		l.add(PacketType.Play.Client.BLOCK_PLACE);
		l.add(PacketType.Play.Client.BLOCK_DIG);
		l.add(PacketType.Play.Client.HELD_ITEM_SLOT);
		
		Main.protocol.addPacketListener(new PacketAdapter(Main.getPlugin(Main.class), l) {
			
			public void onPacketReceiving(PacketEvent e) {
				if(e.getPacketType().equals(PacketType.Play.Client.BLOCK_PLACE)) {
					if(e.getPlayer().getItemInHand().getType().toString().toLowerCase().contains("sword")) {
						ReplayRecorder.recordMetadata(e.getPlayer(), 4);
					} else if(e.getPlayer().getItemInHand().getType().toString().toLowerCase().contains("bow")) {
						if(!hasArrows(e.getPlayer()) && !e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
							return;
						}
						ReplayRecorder.recordMetadata(e.getPlayer(), 4);
					}
				} else if(e.getPacketType().equals(PacketType.Play.Client.BLOCK_DIG)) {
					if(e.getPlayer().getItemInHand().getType().toString().toLowerCase().contains("sword")) {
						ReplayRecorder.recordMetadata(e.getPlayer(), -4);
					} else if(e.getPlayer().getItemInHand().getType().toString().toLowerCase().contains("bow")) {
						ReplayRecorder.recordMetadata(e.getPlayer(), -4);
					}
				} else if(e.getPacketType().equals(PacketType.Play.Client.HELD_ITEM_SLOT)) {
					ReplayRecorder.recordMetadata(e.getPlayer(), -4);
				}
			}
			
		});
	}
	
	public static boolean hasArrows(Player p) {
		for(ItemStack all : p.getInventory().getContents()) {
			if(all != null) {
				if(all.getType().equals(Material.ARROW)) {
					return true;
				}
			}
		}
		return false;
	}
	
}
