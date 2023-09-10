package de.legitfinn.lobby.functions;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.legitfinn.lobby.SQL.CoinSQL;
import de.legitfinn.lobby.SQL.LobbyShopSQL;
import de.legitfinn.lobby.main.ConfigManager;
import de.legitfinn.lobby.main.main;
import de.legitfinn.lobby.scoreboard.ScoreboardSystem;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;

public class DailyReward implements Listener {
	
	public static Location dailyReward;
	public static Material oldTopMaterial;
	public static Material oldDownMaterial;
	public static byte oldTopData;
	public static byte oldDownData;
	public static ArrayList<ArmorStand> tagas = new ArrayList<ArmorStand>();
	public static ArrayList<ArmorStand> rewardStands = new ArrayList<ArmorStand>();
	public static ArrayList<Player> tagseeplayer = new ArrayList<Player>();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getPlayer().getItemInHand().getType().equals(Material.IRON_HOE)) {
				if(e.getPlayer().getItemInHand().hasItemMeta() && e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§4§l» §6DailyRewardSetter §9#1")) {
					ConfigManager.set("DailyReward.area.loc1.X", e.getPlayer().getLocation().getBlockX());
					ConfigManager.set("DailyReward.area.loc1.Y", e.getPlayer().getLocation().getBlockY());
					ConfigManager.set("DailyReward.area.loc1.Z", e.getPlayer().getLocation().getBlockZ());
				} else if(e.getPlayer().getItemInHand().hasItemMeta() && e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§4§l» §6DailyRewardSetter §9#2")) {
					ConfigManager.set("DailyReward.area.loc2.X", e.getPlayer().getLocation().getBlockX());
					ConfigManager.set("DailyReward.area.loc2.Y", e.getPlayer().getLocation().getBlockY());
					ConfigManager.set("DailyReward.area.loc2.Z", e.getPlayer().getLocation().getBlockZ());
				}
			}
		}
	}
	
	public static void randomLocation() {
		int x1 = ConfigManager.getInt("DailyReward.area.loc1.X");
		int z1 = ConfigManager.getInt("DailyReward.area.loc1.Z");
		
		int x2 = ConfigManager.getInt("DailyReward.area.loc2.X");
		int z2 = ConfigManager.getInt("DailyReward.area.loc2.Z");
		
		if(x1 > x2) {
			System.out.println("x1");
			if(z1 > z2) {
				System.out.println("z1");
				//x+ y+ z+
				Random rnd = new Random();
				int x = rnd.nextInt(x1-x2);
				int z = rnd.nextInt(z1-z2);
				dailyReward = getRewardLocation(x1-x, z1-z);
			} else {
				System.out.println("z2");
				//x+ y+ z-
				Random rnd = new Random();
				int x = rnd.nextInt(x1-x2);
				int z = rnd.nextInt(z2-z1);
				dailyReward = getRewardLocation(x1-x, z1+z);
			}
		} else {
			System.out.println("x2");
			if(z1 > z2) {
				System.out.println("z1");
				//x- y+ z+
				Random rnd = new Random();
				int x = rnd.nextInt(x2-x1);
				int z = rnd.nextInt(z1-z2);
				dailyReward = getRewardLocation(x1+x, z1-z);
			} else {
				System.out.println("z2");
				//x- y+ z-
				Random rnd = new Random();
				int x = rnd.nextInt(x2-x1);
				int z = rnd.nextInt(z2-z1);
				dailyReward = getRewardLocation(x1+x, z1+z);
			}
		}
//		System.out.println(dailyReward.getBlockX() + " | " + dailyReward.getBlockZ());
//		Block top = dailyReward.clone().add(0, 1, 0).getBlock();
//		oldTopMaterial = top.getType();
//		oldTopData = top.getData();
//		
//		Block down = dailyReward.getBlock();
//		oldDownMaterial = down.getType();
//		oldDownData = down.getData();
//		
//		dailyReward.getBlock().setType(Material.REDSTONE_BLOCK);
//		dailyReward.clone().add(0, 1, 0).getBlock().setType(Material.REDSTONE_LAMP_ON);
		spawnDailyReward();
	}
	
	public static Location getRewardLocation(int x, int z) {
		Location loc = new Location(Bukkit.getWorld("world"), x, 270, z);
		while(loc.getBlock().getType().equals(Material.AIR) || loc.getBlock().getType().equals(Material.BARRIER) || !loc.getBlock().getType().isSolid()) {
			loc.subtract(0, 1, 0);
		}
		return loc;
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		for(ArmorStand as : tagas) {
			if(as.getLocation().distance(e.getTo()) < 5) {
				if(!tagseeplayer.contains(e.getPlayer())) {
					showTag(e.getPlayer(), tagas);
					tagseeplayer.add(e.getPlayer());
					break;
				}
			} else {
				if(tagseeplayer.contains(e.getPlayer())) {
					hideTag(e.getPlayer(), tagas);
					tagseeplayer.remove(e.getPlayer());
					break;
				}
			}
		}
	}
	
	public void showTag(Player p, ArrayList<ArmorStand> as) {
		for(ArmorStand all : as) {
			DataWatcher dw = new DataWatcher(null);
			dw.a(3, (byte)1);
			PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(all.getEntityId(), dw, true);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(meta);
		}
	}
	
	public void hideTag(Player p, ArrayList<ArmorStand> as) {
		for(ArmorStand all : as) {
			DataWatcher dw = new DataWatcher(null);
			dw.a(3, (byte)0);
			PacketPlayOutEntityMetadata meta = new PacketPlayOutEntityMetadata(all.getEntityId(), dw, true);
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(meta);
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
			ArmorStand as = (ArmorStand)e.getRightClicked();
			if(rewardStands.contains(as)) {
				e.setCancelled(true);
				if(LobbyShopSQL.canGetDaily(e.getPlayer().getUniqueId())) {
					CoinSQL.addCoins(e.getPlayer().getUniqueId(), 1000);
					ScoreboardSystem.updateScoreboard(e.getPlayer(), false, false);
					LobbyShopSQL.getDailyReward(e.getPlayer().getUniqueId());
					e.getPlayer().sendMessage("§4§lLobby §7» §aDu hast §6§o1000 Coins §aerhalten!");
					e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 1F, 1F);
				} else {
					e.getPlayer().sendMessage("§4§lLobby §7» §cDu hast deine §oTägliche Belohnung §cschon erhalten.");
				}
			}
		}
	}
	
	
	public static void spawnDailyReward() {
		for(Entity all : dailyReward.getWorld().getEntities()) {
			if(all.getType().equals(EntityType.ARMOR_STAND)) {
				ArmorStand a = (ArmorStand)all;
				if(a.getCustomName() != null) {
					if(a.getCustomName().equalsIgnoreCase("DailyReward#pvpfun.net33721") || a.getCustomName().equals("§cTägliche Belohung") || a.getCustomName().equals("§b§o<Rechtsklick>")) {
						a.remove();
					}
				}
			}
		}		
		Location loc = dailyReward.clone().add(0.5, 0.4, 0.5);
		ArmorStand a1 = loc.getWorld().spawn(loc.clone().add(0, 0.5, 0), ArmorStand.class);
		ArmorStand a2 = loc.getWorld().spawn(loc.clone().add(0, -0.75, 0), ArmorStand.class);
		ArmorStand a3 = loc.getWorld().spawn(loc.clone().add(0, 0, 0), ArmorStand.class);
		ArmorStand a4 = loc.getWorld().spawn(loc.clone().add(0, 0, 0), ArmorStand.class);
		ArmorStand a5 = loc.getWorld().spawn(loc.clone().add(0, 0, 0), ArmorStand.class);
		ArmorStand a6 = loc.getWorld().spawn(loc.clone().add(0, 0, 0), ArmorStand.class);
		ArmorStand a7 = loc.getWorld().spawn(loc.clone().add(0, 0, 0), ArmorStand.class);
		ArmorStand a8 = loc.getWorld().spawn(loc.clone().add(0, -0.25, 0), ArmorStand.class);
		tagas.clear();
		rewardStands.clear();
		
		ItemStack present = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
		SkullMeta pm = (SkullMeta) present.getItemMeta();
		pm.setOwner("MHF_PRESENT2");
		present.setItemMeta(pm);
		rewardStands.add(a1);
		rewardStands.add(a2);
		tagas.add(a7);
		tagas.add(a8);
		rewardStands.add(a7);
		rewardStands.add(a8);
		rewardStands.add(a3);
		rewardStands.add(a4);
		rewardStands.add(a5);
		rewardStands.add(a6);
		
		a1.setHelmet(present);
		a2.setHelmet(new ItemStack(Material.IRON_BLOCK));
		a3.setHelmet(new ItemStack(Material.SEA_LANTERN));
		a4.setHelmet(new ItemStack(Material.SEA_LANTERN));
		a5.setHelmet(new ItemStack(Material.SEA_LANTERN));
		a6.setHelmet(new ItemStack(Material.SEA_LANTERN));
		a1.setVisible(false);
		a2.setVisible(false);
		a3.setVisible(false);
		a4.setVisible(false);
		a5.setVisible(false);
		a6.setVisible(false);
		a7.setVisible(false);
		a8.setVisible(false);
		a1.setSmall(true);
		a3.setSmall(true);
		a4.setSmall(true);
		a5.setSmall(true);
		a6.setSmall(true);
		a1.setCustomName("DailyReward#pvpfun.net33721");
		a2.setCustomName("DailyReward#pvpfun.net33721");
		a3.setCustomName("DailyReward#pvpfun.net33721");
		a4.setCustomName("DailyReward#pvpfun.net33721");
		a5.setCustomName("DailyReward#pvpfun.net33721");
		a6.setCustomName("DailyReward#pvpfun.net33721");
		a7.setCustomName("§cTägliche Belohung");
		a8.setCustomName("§b§o<Rechtsklick>");
		a1.setCustomNameVisible(false);
		a2.setCustomNameVisible(false);
		a3.setCustomNameVisible(false);
		a4.setCustomNameVisible(false);
		a5.setCustomNameVisible(false);
		a6.setCustomNameVisible(false);
		a7.setCustomNameVisible(false);
		a8.setCustomNameVisible(false);
		a1.setGravity(false);
		a2.setGravity(false);
		a3.setGravity(false);
		a4.setGravity(false);
		a5.setGravity(false);
		a6.setGravity(false);
		a7.setGravity(false);
		a8.setGravity(false);
	}
	
	public static Thread dthread;
	
	public static void animation() {
		dthread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int rot = 0;
				float rot2 = 0;
				while(true) {
					int i = 1;
					for(ArmorStand all : rewardStands) {
						Location loc = all.getLocation();
						if(all.getHelmet().getType().equals(Material.SKULL_ITEM)) {
							loc.setYaw(rot + 45);
						} else if(all.getHelmet().getType().equals(Material.IRON_BLOCK)){
							loc.setYaw(rot);
							main.playParicle(loc.getWorld().getPlayers(), EnumParticle.FLAME, loc.clone().add(0, 2, 0), 0.0F, 0.0F, 0.0F, 0.01F, 1);
						} else if(all.getHelmet().getType().equals(Material.SEA_LANTERN)) {
							loc.setPitch(0);
							loc.setYaw(rot2 + (90 * i));
							loc.add(loc.getDirection().multiply(0.01));
							i ++;
							main.playReddustParticle(loc.getWorld().getPlayers(), loc.clone().add(0, 0.8, 0), 0.001F, 1.00F, 1.00F, 5);
						}
						all.teleport(loc);
					}
					rot ++;
					rot2 += 0.5F;
					if(rot >= 360) {
						rot = 0;
					}
					if(rot2 >= 360) {
						rot2 = 0;
					}
					try{
						Thread.sleep(10);
					} catch(InterruptedException ex){}
				}
			}
		});
		dthread.start();
		//26772
	}
	
	
	
}