package de.legitfinn.knockbackffa.listener;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import de.legitfinn.NickApi.main.NickModul;
import de.legitfinn.knockbackffa.SQL.StatisticSQL;
import de.legitfinn.knockbackffa.main.DataManager;
import de.legitfinn.knockbackffa.main.main;
import de.legitfinn.knockbackffa.methoden.ConfigManager;
import de.legitfinn.knockbackffa.methoden.Kits;
import de.legitfinn.knockbackffa.methoden.MapItems;
import de.legitfinn.knockbackffa.methoden.ScoreboardSystem;
import de.legitfinn.ppermissions.main.PPermissions;
public class DeathListener implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();
		e.setDeathMessage(null);
		e.getEntity().getInventory().clear();
		e.getEntity().getInventory().setArmorContents(null);
		Kits.player_kills.put(player, 0);
		if(player.getKiller() != null) {
			Player killer = (Player)player.getKiller();
			String name = PPermissions.getChatColor(player) + player.getName();
			if(NickModul.isNicked(player.getUniqueId())) {
				name = PPermissions.getLowestChatColor() + player.getName();
			}
			if(!killer.equals(player)) {
				if(!Kits.player_kills.containsKey(killer)) {
					Kits.player_kills.put(killer, 0);
				}
				Kits.player_kills.put(killer, Kits.player_kills.get(killer) + 1);
				killer.setLevel(Kits.player_kills.get(killer));
				String kname = PPermissions.getChatColor(killer) + killer.getName();
				if(NickModul.isNicked(killer.getUniqueId())) {
					kname = PPermissions.getLowestChatColor() + killer.getName();
				}
				String[] msg = getDeathMessage();
				player.sendMessage(DataManager.prefix + msg[0].replace("%name%", name).replace("%kname%", kname));
				killer.sendMessage(DataManager.prefix + msg[1].replace("%kname%", kname).replace("%name%", name));
				StatisticSQL.addInt("KNOCKBACKFFA", "KILLS", 1, killer.getUniqueId());
				announceStreak(killer);
			} else {
				player.sendMessage(DataManager.prefix + name + " §3hat Selbstmord begangen... *heul*...");
			}
			
			if(DataManager.Kit == 4) {
				if(killer.getLocation().distance(ConfigManager.getSpawn()) > 15) {
					killer.getInventory().addItem(new ItemStack(Material.ARROW, 1));
				}
			}
			
			if(!DataManager.testmode) {
				StatisticSQL.addInt("KNOCKBACKFFA", "DEATH", 1, player.getUniqueId());
				if(Kits.player_kills.containsKey(killer)) {
					if(Kits.player_kills.get(killer) > StatisticSQL.getInt("KNOCKBACKFFA", "MAXSTREAK", killer.getUniqueId())) {
						StatisticSQL.setInt("KNOCKBACKFFA", "MAXSTREAK", Kits.player_kills.get(killer), killer.getUniqueId());
					}
				}
			}
			ScoreboardSystem.updateScoreboard(player, false);
			ScoreboardSystem.updateScoreboard(killer, false);
 		} else {
			String name = PPermissions.getChatColor(player) + player.getName();
			if(NickModul.isNicked(player.getUniqueId())) {
				name = PPermissions.getLowestChatColor() + player.getName();
			}
			player.sendMessage(DataManager.prefix + name + " §3fiel zu tief.");
			if(!DataManager.testmode) {
				StatisticSQL.addInt("KNOCKBACKFFA", "DEATH", 1, player.getUniqueId());
			}
			ScoreboardSystem.updateScoreboard(player, false);
		}
		e.setKeepInventory(false);
		e.getDrops().clear();

		Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
			@Override
			public void run() {
				e.getEntity().spigot().respawn();
			}
		}, 1);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(ConfigManager.getSpawn());
		JoinListener.addItems(e.getPlayer());
		DataManager.ingame.remove(e.getPlayer());
		e.getPlayer().setGameMode(GameMode.ADVENTURE);
	}
	
	public static String[] getDeathMessage() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(new String[] {"§a§lDu §3wurdest von %kname% §3weggeklatscht.", "%name% §3wurde von §a§ldir §3weggeklatscht."});
		list.add(new String[] {"§a§lDu §3wurdest von %kname% §3weg geschleudert.", "%name% §3wurde von §a§ldir §3weg geschleudert."});
		list.add(new String[] {"§a§lDu §3wurdest von %kname% §3auf den Mond geschickt.", "%name% §3wurde von §a§ldir §3auf den Mond geschickt."});
		list.add(new String[] {"§a§lDu §3wurdest von %kname% §3ins NIX befördert.", "%name% §3wurde von §a§ldir §3ins NIX befördert"});
		list.add(new String[] {"§a§lDu §3wurdest von %kname% §3erniedrigt.", "%name% §3wurde von §a§ldir §3erniedrigt."});
		list.add(new String[] {"%kname% §3hat §a§ldich §3ins Reich der Toten entführt.", "§a§lDu §3hast %name% §3ins Reich der Toten entführt."});
		list.add(new String[] {"%kname%'s §3Skill ist so lächerlich, dass §a§ldu dich §3§ntot§3 gelacht hast.", "§a§lDein §3Skill ist so lächerlich, dass %name% §3sich §ntot§3 gelacht hat."});
		list.add(new String[] {"%kname% §3hat §a§ldich §3um einen Kopf kürzer gemacht.", "§a§lDu §3hast %name% §3um einen Kopf kürzer gemacht."});
		list.add(new String[] {"%kname% §3hat §a§ldich §3auf geschliaaaaaaaaazt", "§a§lDu §3hast %name% §3auf geschliaaaaaaaaazt."});
		list.add(new String[] {"§a§lDu§3: §3MAMAAAA... *heul*... Der %kname%§3 hat mich getötet!", "%name%§3: MAMAAAA... *heul*... %kname% §3hat mich getötet!"});
		list.add(new String[] {"%kname% §3hat §a§ldich §3töter als tot geschlagen", "§a§lDu §3hast %name% §3töter als tot geschlagen."});
		list.add(new String[] {"§a§lDu §3hast einen Stock von %kname% §3auf den Kopf bekommen.", "%name% §3hat einen Stock von §a§ldir §3auf den Kopf bekommen."});
		Random rnd = new Random();
		return list.get(rnd.nextInt(list.size()));
	}
	
	public static void announceStreak(Player p) {
		
		if(Kits.player_kills.containsKey(p)) {
			if(Kits.player_kills.get(p) >= 5) {
				int kills = Kits.player_kills.get(p);
				
				if((double)kills / 5d == (int)((double)kills / 5d)) {
					String name = p.getName();
					if(NickModul.isNicked(p.getUniqueId())) {
						name = PPermissions.getLowestChatColor() + p.getName();
					} else {
						name = PPermissions.getChatColor(p) + p.getName();
					}
					Bukkit.broadcastMessage(DataManager.prefix + ">");
					Bukkit.broadcastMessage(DataManager.prefix + "> §a§lDer Spieler " + name + " §a§l hat eine §e§l" + kills + "er Serie§a§l!");
					Bukkit.broadcastMessage(DataManager.prefix + ">");
					if(DataManager.ingame.contains(p)){
						p.getInventory().addItem(MapItems.getRandomItem());
					}
				}
				
				
			}
		}
		
		
	}
	
	
	
	
}
