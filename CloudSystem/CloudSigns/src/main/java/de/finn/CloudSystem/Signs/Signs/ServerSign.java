package de.finn.CloudSystem.Signs.Signs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.finn.CloudSystem.Signs.main.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
public class ServerSign implements Listener {

	private Location loc;
	private String gamemode;
	private int id;
	private Server server;
	private String[] lines = new String[4];
	private SignGroup group;
	private int animationLocation = 0;
	
	public ServerSign(Location loc, String gamemode, int id) {
		this.loc = loc;
		this.gamemode = gamemode;
		this.id = id;
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin(Main.class));
		animationLocation = new Random().nextInt(26);
	}
	
	private ArrayList<Player> inSight = new ArrayList<Player>();
	private ArrayList<Player> joinDelay = new ArrayList<Player>();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getClickedBlock().getType().toString().endsWith("_SIGN")) {
				if(e.getClickedBlock().getState() instanceof Sign) {
					Sign s = (Sign)e.getClickedBlock().getState();
					if(s.getLocation().equals(loc)) {
						if(server != null) {
							if(server.getOnPlayer() >= server.getMaxPlayer()) {
								if(!e.getPlayer().hasPermission("network.joinFull")) {
									e.getPlayer().sendMessage("§cDieser Server ist voll! Kaufe dir §6§lPremium§c oder §lLegende §cum volle Server zu betreten.");
									TextComponent t1 = new TextComponent("§cUnseren Shop findest du unter: ");
									TextComponent t2 = new TextComponent("§cshop.logi-lounge.de");
									t2.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§e* Klick * §cÖffne den Shop in deinem Browser.").create()));
									t2.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, "https://shop.logi-lounge.de"));
									t1.addExtra(t2);
									e.getPlayer().spigot().sendMessage(t1);
								} else {
									connectPlayer(e.getPlayer(), server.getServername());
								}
							} else {
								connectPlayer(e.getPlayer(), server.getServername());
							}
						} else {
							e.getPlayer().sendMessage("§cAn diesem Schild befindet sich kein Server!");
						}
					}
 				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		joinDelay.add(e.getPlayer());
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable() {
			
			@Override
			public void run() {
				joinDelay.remove(e.getPlayer());
				if(e.getPlayer().getLocation().distance(loc) < 20) {
					inSight.add(e.getPlayer());
					update(e.getPlayer());
				}
			}
		}, 15);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.getTo().distance(loc) < 20) {
			if(!inSight.contains(e.getPlayer())) {
				inSight.add(e.getPlayer());
				update(e.getPlayer());
			}
		} else if(e.getTo().distance(loc) > 20) {
			if(inSight.contains(e.getPlayer())) {
				inSight.remove(e.getPlayer());
				unload(e.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		if(e.getTo().distance(loc) > 20) {
			if(inSight.contains(e.getPlayer())) {
				inSight.remove(e.getPlayer());
				unload(e.getPlayer());
			}
		}
	}
	
	public void unload(Player p) {
		p.sendSignChange(loc, new String[] {"","Schild ausser", "Reichweite", ""});
	}
	
	private void connectPlayer(Player p, String server) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("Connect");
			out.writeUTF(server);
			p.sendPluginMessage(Main.getPlugin(Main.class), "BungeeCord", b.toByteArray());
		} catch(IOException ex) {}
	}
	
	public void setGroup(SignGroup group) {
		this.group = group;
	}
	
	public boolean isUsed() {
		return server != null;
	}
	
	public void setServer(Server server) {
		this.server = server;
		update();
	}
	
	public Location getLoc() {
		return loc;
	}

	public String getGamemode() {
		return gamemode;
	}

	public int getId() {
		return id;
	}

	public Server getServer() {
		return server;
	}

	public String[] getLines() {
		return lines;
	}
	
	public void animation() {
	
		int i = animationLocation;
		
		if(i == 0) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l●   ●   ●   ●§b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 1) {
			lines[0] = "§9";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l ●   ●   ●   §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 2) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l  ●   ●   ●  §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 3) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l   ●   ●   ● §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 4) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l    ●   ●   ●§b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 5) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l     ●   ●   §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 6) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l      ●   ●  §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 7) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l       ●   ● §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 8) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l        ●   ●§b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 9) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l         ●   §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 10) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l          ●  §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 11) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l           ● §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 12) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l            ●§b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 13) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l             §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 14) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l●            §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 15) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l ●           §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 16) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l  ●          §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 17) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l   ●         §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 18) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l●   ●        §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 19) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l ●   ●       §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 20) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l  ●   ●      §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 21) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l   ●   ●     §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 22) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l●   ●   ●    §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 23) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l ●   ●   ●   §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 24) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l  ●   ●   ●  §b§l】";
			lines[3] = "§8§m-------------";
		} else if(i == 25) {
			lines[0] = "§8§m-------------";
			lines[1] = "§8Wird geladen...";
			lines[2] = "§b§l【§c§l   ●   ●   ● §b§l】";
			lines[3] = "§8§m-------------";
		}
		animationLocation ++;
		if(animationLocation > 25) {
			animationLocation = 0;
		}
	}
	
	public void update() {
		if(server != null) {
			if(!server.getStatus().equalsIgnoreCase("waiting")) {
				this.server = null;
				this.group.lookUpSigns();
			}
		}
		
		Sign s = (Sign)loc.getBlock().getState();
		generateLines(s);
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(inSight.contains(all) && !joinDelay.contains(all)) {
				all.sendSignChange(loc, lines);
			} else {
				if(all.getLocation().distance(loc) < 20 && !joinDelay.contains(all)) {
					inSight.add(all);
					all.sendSignChange(loc, lines);
				}
			}
		}
//		s.setLine(0, lines[0]);
//		s.setLine(1, lines[1]);
//		s.setLine(2, lines[2]);
//		s.setLine(3, lines[3]);
//		s.update(true, true);
	}
	
	public void update(Player p) {
		if(!joinDelay.contains(p) && (inSight.contains(p) || p.getLocation().distance(loc) < 20)) {
			Sign s = (Sign)loc.getBlock().getState();
			generateLines(s);
			p.sendSignChange(loc, lines);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void generateLines(Sign sign) {
		if(isUsed()) {
			String line1 = "- " + server.getServername() + " -";
			String line2 = "";
			if(server.getOnPlayer() >= server.getMaxPlayer()) {
				line2 = "[§6BETRETEN§r]";
				getBackBlock(sign).setType(Material.ORANGE_TERRACOTTA);
			} else {
				line2 = "[§aBETRETEN§r]";
				getBackBlock(sign).setType(Material.GREEN_TERRACOTTA);
			}
			String line3 = server.getMap();
			String line4 = server.getOnPlayer() + " §l/ §r" + server.getMaxPlayer();
			lines[0] = line1;
			lines[1] = line2;
			lines[2] = line3;
			lines[3] = line4;
		} else {
			String line1 = "§3§m------------";
			String line2 = "Server wird";
			String line3 = "geladen§1...";
			String line4 = "§3§m------------";
			lines[0] = line1;
			lines[1] = line2;
			lines[2] = line3;
			lines[3] = line4;
			getBackBlock(sign).setType(Material.LIGHT_BLUE_TERRACOTTA);
//			animation();
		}
	}
	
	@SuppressWarnings("deprecation")
	private Block getBackBlock(Sign sign) {
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
