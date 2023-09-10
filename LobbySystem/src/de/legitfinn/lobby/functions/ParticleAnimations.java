package de.legitfinn.lobby.functions;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.legitfinn.lobby.main.main;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class ParticleAnimations implements Listener {

	public static HashMap<Player, String> player_particle = new HashMap<Player, String>();
	public static HashMap<Player, Long> lastMove = new HashMap<Player, Long>();
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ() || e.getFrom().getY() != e.getTo().getY()) {
			lastMove.put(e.getPlayer(), System.currentTimeMillis());
		} else {
			return;
		}
		if(!DoubleJump.inJump.contains(e.getPlayer())) {
			if(player_particle.containsKey(e.getPlayer())) {
				String particle = player_particle.get(e.getPlayer());
				if(particle.equalsIgnoreCase("flame")) {
					main.playParicle(e.getPlayer().getLocation().getWorld().getPlayers(), EnumParticle.FLAME, e.getPlayer().getLocation().clone().add(0, 0.25, 0), 0.1F, 0.1F, 0.1F, 0.01F, 10);
				} else if(particle.equalsIgnoreCase("magic")) {
					main.playParicle(e.getPlayer().getLocation().getWorld().getPlayers(), EnumParticle.SPELL_WITCH, e.getPlayer().getLocation().clone().add(0, 0.25, 0), 0.1F, 0.1F, 0.1F, 0.0F, 10);
				} else if(particle.equalsIgnoreCase ("emerald")) {	
					main.playParicle(e.getPlayer().getLocation().getWorld().getPlayers(), EnumParticle.VILLAGER_HAPPY, e.getPlayer().getLocation().clone().add(0, 0.25, 0), 0.1F, 0.1F, 0.1F, 0.0F, 10);
				} else if(particle.equalsIgnoreCase("rain")) {
					main.playParicle(e.getPlayer().getLocation().getWorld().getPlayers(), EnumParticle.WATER_WAKE, e.getPlayer().getLocation().clone().add(0, 0.25, 0), 0.1F, 0.1F, 0.1F, 0.0F, 10);
				} else if(particle.equalsIgnoreCase("redstone")) {
					main.playParicle(e.getPlayer().getLocation().getWorld().getPlayers(), EnumParticle.REDSTONE, e.getPlayer().getLocation(), 0.25F, 0.25F, 0.25F, 0.0F, 5);
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(player_particle.containsKey(e.getPlayer())) {
			player_particle.remove(e.getPlayer());
		}
	}

	public static Thread flame;
	public static Thread magic;
	public static Thread emerald;
	public static Thread rain;
	public static Thread redstone;
	
	public static void Animations() {
		flame = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int rot = 0;
				double multi = 1;
				double height = 0.0D;
				while(true) {
					for(Player all : lastMove.keySet()) {
						if(player_particle.containsKey(all)) {
							if(player_particle.get(all).equalsIgnoreCase("flame")) {
								if(System.currentTimeMillis() - lastMove.get(all) > 500) {
									Location loc = all.getLocation().clone();
									loc.setYaw(rot);
									loc.setPitch(0);
									loc.add(loc.getDirection().multiply(multi));
									loc.add(0, height, 0);
									main.playParicle(loc.getWorld().getPlayers(), EnumParticle.FLAME, loc, 0F, 0F, 0F, 0F, 1);
								}
							}
						}
					}
					rot += 30;
					if(rot >= 360) {
						multi -= 0.08;
						rot = 0;
						height += 0.2;
						if(height > 2.5) {
							for(Player all : lastMove.keySet()) {
								if(player_particle.containsKey(all)) {
									if(player_particle.get(all).equalsIgnoreCase("flame")) {
										if(System.currentTimeMillis() - lastMove.get(all) > 500) {
											main.playParicle(all.getLocation().getWorld().getPlayers(), EnumParticle.FLAME, all.getLocation().clone().add(0, 2.5, 0), 0F, 0F, 0F, 0.1F, 50);
										}
									}
								}
							}
							
							
							height = 0.25;
							multi = 1;
						}
					}
					try{
						Thread.sleep(4);
					} catch(InterruptedException ex){}
				}
			}
		});
		flame.start();
		
		magic = new Thread(new Runnable() {
			
			@Override
			public void run() {

				float vert = 0;
				float vstep = 2;
				while(true) {
					
					for(Player all : player_particle.keySet()) {
						if(lastMove.containsKey(all)) {
							if(player_particle.get(all).equalsIgnoreCase("magic")) {
								if(System.currentTimeMillis() - lastMove.get(all) > 500) {
									int rot = 0;
									while(rot < 360) {
										Location loc1 = all.getLocation().clone().add(0, 1, 0);
										loc1.setYaw(rot);
										loc1.setPitch(vert);
										loc1.add(loc1.getDirection().multiply(1.0D).normalize());
//										main.playParicle(loc1.getWorld().getPlayers(), EnumParticle.SPELL_WITCH, loc1, 0F, 0F, 0F, 0F, 1);
										main.playReddustParticle(loc1.getWorld().getPlayers(), loc1, 0.8F, 0.2F, 1.0F, 1);
										main.playParicle(loc1.getWorld().getPlayers(), EnumParticle.FLAME, loc1, 0.0F, 0.0F, 0.0F, 0.0F, 1);
										
										Location loc2 = all.getLocation().clone().add(0, 1, 0);
										loc2.setYaw(rot);
										loc2.setPitch(vert);
										loc2.add(loc2.getDirection().multiply(-1.0D).normalize());
//										main.playParicle(loc2.getWorld().getPlayers(), EnumParticle.SPELL_WITCH, loc2, 0F, 0F, 0F, 0F, 1);
										main.playReddustParticle(loc2.getWorld().getPlayers(), loc2, 0.8F, 0.2F, 1.0F, 1);
										main.playParicle(loc2.getWorld().getPlayers(), EnumParticle.FLAME, loc2, 0.0F, 0.0F, 0.0F, 0.0F, 1);
										rot += 30;
									}
								}
							}
						}
 					}
					
					vert += vstep;
					
					if(vert >= 90) {
						vstep = -2.0F;
					} else if(vert > 85) {
						if(vstep > 0) {
							vstep = 1.0F;
						}
					} else if(vert > 80) {
						if(vstep > 0) {
							vstep = 1.5F;
						}
					} else if(vert <= 0) {
						vstep = 2.0F;
					}
					
					try{
							Thread.sleep(50);
					} catch(InterruptedException ex){}
				}
				

			}
		});
		magic.start();
		
		emerald = new Thread(new Runnable() {
			
			@Override
			public void run() {
				double height = 0.0;
				double step = 0.05;
				
				while(true) {
					float rot = 0.0F;
					while(rot < 360) {
						for(Player all : lastMove.keySet()) {
							if(player_particle.containsKey(all) && System.currentTimeMillis() - lastMove.get(all) >= 500) {
								if(player_particle.get(all).equalsIgnoreCase("emerald")) {
									Location loc = all.getLocation().clone();
									loc.add(0, height, 0);
									loc.setPitch(0);
									loc.setYaw(rot);
									loc.add(loc.getDirection().multiply(1.0D).normalize());
									main.playReddustParticle(loc.getWorld().getPlayers(), loc, 0.01F, 1.0F, 0.0F, 1);
								}
							}
						}
						rot += 7.2F;
					}
					height += step;
					if(height >= 2) {
						step = -0.05;
					} else if(height <= 0) {
						step = 0.05;
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {}
				}
			}
		});
		emerald.start();
		
		rain = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true) {
					for(Player all : player_particle.keySet()) {
						if(player_particle.get(all).equalsIgnoreCase("rain") && lastMove.containsKey(all)) {
							if(System.currentTimeMillis() - lastMove.get(all) > 500) {
								Location loc1 = all.getLocation().clone().add(0, 3, 0);
								main.playParicle(loc1.getWorld().getPlayers(), EnumParticle.CLOUD, loc1, 0.5F, 0.25F, 0.5F, 0.0F, 25);
								main.playParicle(loc1.getWorld().getPlayers(), EnumParticle.DRIP_WATER, loc1, 0.25F, 0.1F, 0.25F, 0.0F, 10);
							}
						}
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {}
				}
			}
		});
		rain.start();
		
		redstone = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int rot = 0;
				while(true) {
					for(Player all : player_particle.keySet()) {
						if(player_particle.get(all).equalsIgnoreCase("redstone") && lastMove.containsKey(all)) {
							if(System.currentTimeMillis() - lastMove.get(all) > 500) {
								Location loc1 = all.getLocation().clone().add(0, 1, 0);
								loc1.setYaw(rot);
								loc1.setPitch(0);
								loc1.add(loc1.getDirection().multiply(1.0).normalize());
								main.playReddustParticle(loc1.getWorld().getPlayers(), loc1, 1.0F, 0.0F, 0.0F, 10);
							}
						}
					}
					rot += 5;
					if(rot >= 360) {
						rot = 0;
					}
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {}
				}
			}			
		});
		redstone.start();
		
		
		
	}
	
}
