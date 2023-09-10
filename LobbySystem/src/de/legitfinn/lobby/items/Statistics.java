package de.legitfinn.lobby.items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.legitfinn.lobby.SQL.StatisticSQL;

public class Statistics implements Listener {
	
	public static ArrayList<Player> loading = new ArrayList<Player>();
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(e.getPlayer().getItemInHand().getType().equals(Material.BOOK)) {
				e.setCancelled(true);
				openStatistics(e.getPlayer());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void openStatistics(Player p) {
		UUID uuid = p.getUniqueId();
		
		if(loading.contains(p)){
			return;
		}
		boolean incache = false;
		for(String s : StatisticSQL.cache.keySet()){
			if(s.contains(p.getUniqueId().toString())){
				incache = true;
				break;
			}
		}
		if(!incache){
			p.sendTitle("§cLoading...", "");
			loading.add(p);
		}
		
		List<String> tmp = new ArrayList<String>();

		
		List<String> bw = new ArrayList<String>();
		int[] bwi = StatisticSQL.getStatisticAsIntArray("BEDWARS", new String[] {"KILLS", "DEATH", "WINS", "GAMES"},  p.getUniqueId());
		bw.add("§8» §6Kills: " + bwi[0]);
		bw.add("§8» §eTode: " + bwi[1]);
		bw.add("§8» §6K/D: " + getKd(bwi[0], bwi[1]));
		bw.add("§8» §eSiege: " + bwi[2]);
		bw.add("§8» §6Runden: " + bwi[3]);
		bw.add("§8» §eSiegwahrscheinlichkeit: " + getWinProbability(bwi[2], bwi[3]) + "%");
		tmp.clear();
		for(String s : bw){
			tmp.add(s.replace("-1", "---"));
		}
		bw.clear();
		bw.addAll(tmp);
		
		List<String> sw = new ArrayList<String>();
		int[] swi = StatisticSQL.getStatisticAsIntArray("SKYWARS", new String[] {"KILLS", "DEATH", "WINS", "GAMES"}, uuid);
		sw.add("§8» §6Kills: " + swi[0]);
		sw.add("§8» §eTode: " + swi[1]);
		sw.add("§8» §6K/D: " + getKd(swi[0], swi[1]));
		sw.add("§8» §eSiege: " + swi[2]);
		sw.add("§8» §6Runden: " + swi[3]);
		sw.add("§8» §eSiegwahrscheinlichkeit: " + getWinProbability(swi[2], swi[3]) + "%");
		tmp.clear();
		for(String s : sw){
			tmp.add(s.replace("-1", "---"));
		}
		sw.clear();
		sw.addAll(tmp);
		
		List<String> vs = new ArrayList<String>();
		int[] vsi = StatisticSQL.getStatisticAsIntArray("ONEVSONE", new String[] {"KILLS", "DEATH", "WINS", "GAMES"}, uuid);
		vs.add("§8» §6Kills: " + vsi[0]);
		vs.add("§8» §eTode: " + vsi[1]);
		vs.add("§8» §6K/D: " + getKd(vsi[0], vsi[1]));
		vs.add("§8» §eSiege: " + vsi[2]);
		vs.add("§8» §6Runden: " + vsi[3]);
		vs.add("§8» §eSiegwahrscheinlichkeit: " + getWinProbability(vsi[2], vsi[3]) + "%");
		tmp.clear();
		for(String s : vs){
			tmp.add(s.replace("-1", "---"));
		}
		vs.clear();
		vs.addAll(tmp);
		
		List<String> kpvp = new ArrayList<String>();
		int[] kpvi = StatisticSQL.getStatisticAsIntArray("KITPVP", new String[] {"KILLS", "DEATH", "KILLS"}, uuid);
		kpvp.add("§8» §6Kills: " + kpvi[0]);
		kpvp.add("§8» §eTode: " + kpvi[1]);
		kpvp.add("§8» §6K/D: " + getKd(kpvi[0], kpvi[1]));
		tmp.clear();
		for(String s : kpvp){
			tmp.add(s.replace("-1", "---"));
		}
		kpvp.clear();
		kpvp.addAll(tmp);

		List<String> trit = new ArrayList<String>();
		int[] triti = StatisticSQL.getStatisticAsIntArray("TRAINIT", new String[] {"KILLS", "DEATH", "WINS", "GAMES", "MODULES"}, uuid);
		trit.add("§8» §6Kills: " + triti[0]);
		trit.add("§8» §eTode: " + triti[1]);
		trit.add("§8» §6K/D: " + getKd(triti[0], triti[1]));
		trit.add("§8» §eSiege: " + triti[2]);
		trit.add("§8» §6Runden: " + triti[3]);
		trit.add("§8» §eSiegwahrscheinlichkeit: " + getWinProbability(triti[2], triti[3]) + "%");
		trit.add("§8» §6Geschaffte Module: " + triti[4]);
		tmp.clear();
		for(String s : trit){
			tmp.add(s.replace("-1", "---"));
		}
		trit.clear();
		trit.addAll(tmp);

		List<String> sg = new ArrayList<String>();
		int[] sgi = StatisticSQL.getStatisticAsIntArray("SURVIVALGAMES", new String[] {"KILLS", "DEATH", "WINS", "GAMES", "CHEST"}, uuid);
		sg.add("§8» §6Kills: " + sgi[0]);
		sg.add("§8» §eTode: " + sgi[1]);
		sg.add("§8» §6K/D: " + getKd(sgi[0], sgi[1]));
		sg.add("§8» §eSiege: " + sgi[2]);
		sg.add("§8» §6Runden: " + sgi[3]);
		sg.add("§8» §eSiegwahrscheinlichkeit: " + getWinProbability(sgi[2], sgi[3]) + "%");
		sg.add("§8» §6Geöffnete Kisten: " + sgi[4]);
		tmp.clear();
		for(String s : sg){
			tmp.add(s.replace("-1", "---"));
		}
		sg.clear();
		sg.addAll(tmp);

		List<String> kffa = new ArrayList<String>();
		int[] kffai = StatisticSQL.getStatisticAsIntArray("KNOCKBACKFFA", new String[] {"KILLS", "DEATH", "MAXSTREAK"},  uuid);
		kffa.add("§8» §6Kills: " + kffai[0]);
		kffa.add("§8» §eTode: " + kffai[1]);
		kffa.add("§8» §6K/D: " + getKd(kffai[0], kffai[1]));
		kffa.add("§8» §eBeste Serie: " + kffai[2]);
		tmp.clear();
		for(String s : kffa){
			tmp.add(s.replace("-1", "---"));
		}
		kffa.clear();
		kffa.addAll(tmp);
		

		List<String> oitc = new ArrayList<String>();
		int[] oitci = StatisticSQL.getStatisticAsIntArray("ONEINTHECHAMBER", new String[] {"KILLS", "DEATH", "WINS", "GAMES", "HEADSHOTS"}, uuid);
		oitc.add("§8» §6Kills: " + oitci[0]);
		oitc.add("§8» §eTode: " + oitci[1]);
		oitc.add("§8» §6K/D: " + getKd(oitci[0], oitci[1]));
		oitc.add("§8» §eSiege: " + oitci[2]);
		oitc.add("§8» §6Runden: " + oitci[3]);
		oitc.add("§8» §eSiegwahrscheinlichkeit: " + getWinProbability(oitci[2], oitci[3]) + "%");
		oitc.add("§8» §6Kopftreffer: " + oitci[4]);
		tmp.clear();
		for(String s : oitc){
			tmp.add(s.replace("-1", "---"));
		}
		oitc.clear();
		oitc.addAll(tmp);

		List<String> marena = new ArrayList<String>();
		int[] mari = StatisticSQL.getStatisticAsIntArray("MOBARENA", new String[] {"MAXWAVE", "KILLS", "GAMES"}, uuid);
		marena.add("§8» §6Platzierung: " + StatisticSQL.getPlace("MOBARENA", "MAXWAVE", uuid));
		marena.add("§8» §eHöchste Welle: " + mari[0]);
		marena.add("§8» §6Mobkills: " + mari[1]);
		marena.add("§8» §eGespielte Runden: " + mari[2]);
		tmp.clear();
		for(String s : marena){
			tmp.add(s.replace("-1", "---"));
		}
		marena.clear();
		marena.addAll(tmp);

		List<String> isf = new ArrayList<String>();
		int[] isfi = StatisticSQL.getStatisticAsIntArray("ISLANDFIGHT", new String[] {"KILLS", "DEATH", "WINS", "GAMES"}, uuid);
		isf.add("§8» §6Platzierung: " + StatisticSQL.getPlace("ISLANDFIGHT", "WINS", uuid));
		isf.add("§8» §eKills: " + isfi[0]);
		isf.add("§8» §6Tode: " + isfi[1]);
		isf.add("§8» §eK/D" + getKd(isfi[0], isfi[1]));
		isf.add("§8» §eSiege: " + isfi[2]);
		isf.add("§8» §6Gepsielte Runden: " + isfi[2]);
		isf.add("§8» §6Siegwahrscheinlichkeit: " + getWinProbability(isfi[2], isfi[3]) + "%");
		tmp.clear();
		for(String s : isf){
			tmp.add(s.replace("-1", "---"));
		}
		isf.clear();
		isf.addAll(tmp);

		List<String> btb = new ArrayList<String>();
		int[] btbi = StatisticSQL.getStatisticAsIntArray("BEDROCKTOWERBATTLE", new String[] {"KILLS", "DEATH", "WINS", "GAMES"}, uuid);
		btb.add("§8» §6Platzierung: " + StatisticSQL.getPlace("BEDROCKTOWERBATTLE", "WINS", uuid));
		btb.add("§8» §eKills: " + btbi[0]);
		btb.add("§8» §6Tode: " + btbi[1]);
		btb.add("§8» §eK/D" + getKd(btbi[0], btbi[0]));
		btb.add("§8» §eSiege: " + btbi[2]);
		btb.add("§8» §6Gepsielte Runden: " + btbi[3]);
		btb.add("§8» §6Siegwahrscheinlichkeit: " + getWinProbability(btbi[2], btbi[3]) + "%");
		tmp.clear();
		for(String s : btb){
			tmp.add(s.replace("-1", "---"));
		}
		btb.clear();
		btb.addAll(tmp);

		List<String> ctf = new ArrayList<String>();
		int[] ctfi = StatisticSQL.getStatisticAsIntArray("CAPTURETHEFLAG", new String[] {"KILLS", "DEATH", "WINS", "GAMES", "FLAGS"}, uuid);
		ctf.add("§8» §6Platzierung: " + StatisticSQL.getPlace("CAPTURETHEFLAG", "WINS", uuid));
		ctf.add("§8» §eKills: " + ctfi[0]);
		ctf.add("§8» §6Tode: " + ctfi[1]);
		ctf.add("§8» §eK/D: " + getKd(ctfi[0], ctfi[1]));
		ctf.add("§8» §6Eroberte Flaggen: " + ctfi[4]);
		ctf.add("§8» §eSiege: " + ctfi[2]);
		ctf.add("§8» §6Gepsielte Runden: " + ctfi[3]);
		ctf.add("§8» §6Siegwahrscheinlichkeit: " + getWinProbability(ctfi[2], ctfi[3]) + "%");
		tmp.clear();
		for(String s : ctf){
			tmp.add(s.replace("-1", "---"));
		}
		ctf.clear();
		ctf.addAll(tmp);

		List<String> pvptrai = new ArrayList<String>();
		pvptrai.add("§8» §6Platzierung Fastbridgen: " + StatisticSQL.getPlaceASC("PVPT_FASTBRIDGEN", "BESTTIME", uuid));
		pvptrai.add("§8» §eBeste Fastbridgezeit: " + StatisticSQL.getDouble("PVPT_FASTBRIDGEN", "BESTTIME", uuid));
		pvptrai.add("§8» §6Platzierung Knockback: " + StatisticSQL.getPlace("PVPT_KNOCKBACK", "WINS", uuid));
		pvptrai.add("§8» §ePlatzierung MLGs: " + StatisticSQL.getPlace("PVPT_MLG", "MLGS", uuid));
		pvptrai.add("§8» §6Geschaffte MLGs: " + StatisticSQL.getInt("PVPT_MLG", "MLGS", uuid));
		pvptrai.add("§8» §ePlatzierung Bogenschießen: " + StatisticSQL.getInt("PVPT_BOW", "HITS", uuid));
		pvptrai.add("§8» §6Geschoßene Pfeile: " + StatisticSQL.getInt("PVPT_BOW", "SHOOTS", uuid));
		tmp.clear();
		for(String s : pvptrai){
			tmp.add(s.replace("-1.0", "---").replace("-1", "---"));
		}
		pvptrai.clear();
		pvptrai.addAll(tmp);

		Inventory inv = Bukkit.createInventory(null, 45, "§bDeine Statistiken");
		
		inv.setItem(12, getItemStack(Material.BED, bw, "§7» §cBedWars"));
		inv.setItem(13, getItemStack(Material.GRASS, sw, "§7» §bSkyWars"));
		inv.setItem(14, getItemStack(Material.GOLD_CHESTPLATE, vs, "§7» §e1vs1"));
		inv.setItem(19, getItemStack(Material.DIAMOND_SWORD, kpvp, "§7» §9KitPvP"));
		inv.setItem(20, getItemStack(Material.WEB, trit, "§7» §aTrainIt"));
		inv.setItem(21, getItemStack(Material.CHEST, sg, "§7» §aSG"));
		inv.setItem(22, getItemStack(Material.FISHING_ROD, kffa, "§7» §9KnockbackFFA"));
		inv.setItem(23, getItemStack(Material.BOW, oitc, "§7» §c§lO§cne §lI§cn §lT§che§l C§chamber"));
		inv.setItem(24, getItemStack(Material.BONE, marena, "§7» §5Mobarena"));
		inv.setItem(25, getItemStack(Material.BEDROCK, btb, "§7» §8BedrockTowerBattle"));
		inv.setItem(30, getItemStack(Material.BANNER, ctf, "§7» §2Capture The Flag"));
		inv.setItem(31, getItemStack(Material.WATCH, isf, "§7» §3IslandFight"));
		inv.setItem(32, getItemStack(Material.STONE_SWORD, pvptrai, "§7» §aPvPTrainting"));
		
		p.openInventory(inv);
		p.sendTitle("", "");
		loading.remove(p);
	}
	
	public static ItemStack getItemStack(Material m, List<String> lore, String displayname) {
		ItemStack ret = new ItemStack(m);
		ItemMeta meta = ret.getItemMeta();
		meta.setDisplayName(displayname);
		if(lore != null) {
			meta.setLore(lore);
		}
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
		ret.setItemMeta(meta);
		return ret;
	}
	
	public static String getKd(int kills, int death) {
		double kd = (double)kills / (double)death;
		kd = Math.round(kd * 100) / 100;
		return "" + kd;
	}
	
	public static String getWinProbability(int wins, int games) {
		if(games > 0) {
			double a = (double)wins / (double)games;
			double b = Math.round(a * 100) / 100;
			return "" + b;
		} else {
			return "0.00";
		}
	}
	
	

}
