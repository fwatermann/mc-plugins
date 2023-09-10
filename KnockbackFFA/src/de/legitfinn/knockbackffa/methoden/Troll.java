package de.legitfinn.knockbackffa.methoden;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Troll implements Listener {

	public static ArrayList<Player> players = new ArrayList<Player>();
	public static ArrayList<Player> vanish = new ArrayList<Player>();
	public static ArrayList<Player> noknock = new ArrayList<Player>();
	public static ArrayList<Player> nodamage = new ArrayList<Player>();
	
	public static HashMap<Player, ArrayList<Block>> blocks = new HashMap<Player, ArrayList<Block>>();
	public static HashMap<Block, Material> block_material = new HashMap<Block, Material>();
	public static HashMap<Block, Byte> block_data = new HashMap<Block, Byte>();
	
	@SuppressWarnings("deprecation")
	public static void resetBlocks(Player p) {
		if(blocks.containsKey(p)) {
			for(Block all : blocks.get(p)) {
				if(block_material.containsKey(all)) {
					all.setType(block_material.get(all));
					if(block_data.containsKey(all)) {
						all.setData(block_data.get(all));
					}
				}
				if(block_data.containsKey(all)) {
					all.setData(block_data.get(all));
				}
			}
		}	
	}
	
	@SuppressWarnings("deprecation")
	public static void addBlock(Player p, Block b) {
		if(!blocks.containsKey(p)) {
			blocks.put(p, new ArrayList<Block>());
		}
		
		blocks.get(p).add(b);
		block_material.put(b, b.getType());
		block_data.put(b, b.getData());
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player)e.getEntity();
			if(players.contains(p)) {
				if(noknock.contains(p) && !nodamage.contains(p)) {
					e.setCancelled(true);
					double a = p.getHealth();
					p.damage(1.0D);
					p.setHealth(a);
				} else if(nodamage.contains(p)) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	
	
	
	
	
}
