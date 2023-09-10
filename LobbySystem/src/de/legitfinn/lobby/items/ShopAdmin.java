package de.legitfinn.lobby.items;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.legitfinn.lobby.PlayerData.PlayerData;
import de.legitfinn.lobby.SQL.ShopSQL;
import de.legitfinn.lobby.functions.AnvilContainer;
import de.legitfinn.lobby.functions.PetListener;

public class ShopAdmin implements Listener{
	
	public static ArrayList<Player> filter = new ArrayList<>();
	
	@EventHandler
	public void onClick(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		if(e.getClickedInventory() != null){
			if(e.getCurrentItem() != null){
				if(e.getCurrentItem().hasItemMeta()){
					if(e.getClickedInventory().getTitle().contains("§bPayments §7» §bWAITING")){
						e.setCancelled(true);
						if(e.getCurrentItem().getType().equals(Material.IRON_DOOR)){
							String page = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
							page = page.split(" ")[2];
							String filter = e.getClickedInventory().getItem(4).getItemMeta().getDisplayName();
							boolean filtered = false;
							if(filter.contains(":")){
								filter = ChatColor.stripColor(filter).split(" ")[2];
								filtered = true;
							}else {
								filter = null;
							}
							p.openInventory(getWaitingPayments(Integer.parseInt(page) - 1, filtered, filter));
						}else if(e.getCurrentItem().getType().equals(Material.ANVIL)){
							e.setCancelled(true);
							EntityPlayer cp = ((CraftPlayer)p).getHandle();
							AnvilContainer con = new AnvilContainer(cp);
							int c = cp.nextContainerCounter();
							cp.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing", new Object[0]), 0));
							cp.activeContainer = con;
							cp.activeContainer.windowId = c;
							cp.activeContainer.addSlotListener(cp);
							
							ItemStack item = new ItemStack(Material.PAPER);
							ItemMeta m = item.getItemMeta();
							List<String> lore = new ArrayList<>();
							lore.add("§7WAITING");
							m.setLore(lore);
							m.setDisplayName("Suchbegriff eingeben...");
							item.setItemMeta(m);
							
							p.getOpenInventory().getTopInventory().addItem(item);
							filter.add(p);
						}else if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)){
							e.setCancelled(true);
							if(e.getCurrentItem().getItemMeta().getDisplayName().contains("Ange")){
								p.openInventory(getAcceptedPayments(0, false, null));
							}else {
								p.openInventory(getDeniedPayments(0, false, null));
							}
						}else {
							e.setCancelled(true);
							p.openInventory(statusinv(e.getCurrentItem(), "WAITING"));
						}
					}else if(e.getClickedInventory().getType().equals(InventoryType.ANVIL)){
						if(!PetListener.namingpet.contains(p) && !Gadgets.pay.contains(p) && filter.contains(p)){
							if(e.getCurrentItem().getItemMeta().getDisplayName() != null){
								String search = e.getCurrentItem().getItemMeta().getDisplayName();
								filter.remove(p);
								String type = e.getCurrentItem().getItemMeta().getLore().get(0);
								type = ChatColor.stripColor(type);
								if(type.equalsIgnoreCase("Waiting")){
									p.openInventory(getWaitingPayments(0, true, search));
								}else if(type.equalsIgnoreCase("Denied")){
									p.openInventory(getDeniedPayments(0, true, search));
								}else {
									p.openInventory(getAcceptedPayments(0, true, search));
								}
							}
						}
					}else if(e.getClickedInventory().getTitle().equals("§bPayments §7» §bBearbeitung")){
						e.setCancelled(true);
						if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)){
							if(e.getCurrentItem().getItemMeta().getDisplayName().contains("BEST")){
								String name = e.getClickedInventory().getItem(13).getItemMeta().getLore().get(0);
								name = ChatColor.stripColor(name);
								name = name.split(" ")[2];
								String date = e.getClickedInventory().getItem(13).getItemMeta().getLore().get(2);name = ChatColor.stripColor(name);
								date = ChatColor.stripColor(date);
								date = date.split(" ")[2] + " " + date.split(" ")[3];
								ShopSQL.acceptPayment(true, PlayerData.getUUID(name), date);
							}else {
								String name = e.getClickedInventory().getItem(13).getItemMeta().getLore().get(0);
								name = ChatColor.stripColor(name);
								name = name.split(" ")[2];
								String date = e.getClickedInventory().getItem(13).getItemMeta().getLore().get(2);name = ChatColor.stripColor(name);
								date = ChatColor.stripColor(date);
								date = date.split(" ")[2] + " " + date.split(" ")[3];
								ShopSQL.acceptPayment(false, PlayerData.getUUID(name), date);
							}
							String type = e.getCurrentItem().getItemMeta().getLore().get(0);
							type = ChatColor.stripColor(type);
							if(type.equalsIgnoreCase("Waiting")){
								p.openInventory(getWaitingPayments(0, false, null));
							}else if(type.equalsIgnoreCase("Denied")){
								p.openInventory(getDeniedPayments(0, false, null));
							}else {
								p.openInventory(getAcceptedPayments(0, false, null));
							}
						}
					}else if(e.getClickedInventory().getTitle().contains("§bPayments §7» §bACCEPTED")){
						e.setCancelled(true);
						if(e.getCurrentItem().getType().equals(Material.IRON_DOOR)){
							String page = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
							page = page.split(" ")[2];
							String filter = e.getClickedInventory().getItem(4).getItemMeta().getDisplayName();
							boolean filtered = false;
							if(filter.contains(":")){
								filter = ChatColor.stripColor(filter).split(" ")[2];
								filtered = true;
							}else {
								filter = null;
							}
							p.openInventory(getAcceptedPayments(Integer.parseInt(page) - 1, filtered, filter));
						}else if(e.getCurrentItem().getType().equals(Material.ANVIL)){
							e.setCancelled(true);
							EntityPlayer cp = ((CraftPlayer)p).getHandle();
							AnvilContainer con = new AnvilContainer(cp);
							int c = cp.nextContainerCounter();
							cp.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing", new Object[0]), 0));
							cp.activeContainer = con;
							cp.activeContainer.windowId = c;
							cp.activeContainer.addSlotListener(cp);
							
							ItemStack item = new ItemStack(Material.PAPER);
							ItemMeta m = item.getItemMeta();
							List<String> lore = new ArrayList<>();
							lore.add("§7ACCEPTED");
							m.setLore(lore);
							m.setDisplayName("Suchbegriff eingeben...");
							item.setItemMeta(m);
							
							p.getOpenInventory().getTopInventory().addItem(item);
							filter.add(p);
						}else if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)){
							e.setCancelled(true);
							if(e.getCurrentItem().getItemMeta().getDisplayName().contains("Abg")){
								p.openInventory(getDeniedPayments(0, false, null));
							}else {
								p.openInventory(getWaitingPayments(0, false, null));
							}
						}else {
							e.setCancelled(true);
							p.openInventory(statusinv(e.getCurrentItem(), "ACCEPTED"));
						}
					}else if(e.getClickedInventory().getTitle().contains("§bPayments §7» §bDENIED")){
						e.setCancelled(true);
						if(e.getCurrentItem().getType().equals(Material.IRON_DOOR)){
							String page = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
							page = page.split(" ")[2];
							String filter = e.getClickedInventory().getItem(4).getItemMeta().getDisplayName();
							boolean filtered = false;
							if(filter.contains(":")){
								filter = ChatColor.stripColor(filter).split(" ")[2];
								filtered = true;
							}else {
								filter = null;
							}
							p.openInventory(getDeniedPayments(Integer.parseInt(page) - 1, filtered, filter));
						}else if(e.getCurrentItem().getType().equals(Material.ANVIL)){
							e.setCancelled(true);
							EntityPlayer cp = ((CraftPlayer)p).getHandle();
							AnvilContainer con = new AnvilContainer(cp);
							int c = cp.nextContainerCounter();
							cp.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing", new Object[0]), 0));
							cp.activeContainer = con;
							cp.activeContainer.windowId = c;
							cp.activeContainer.addSlotListener(cp);
							
							ItemStack item = new ItemStack(Material.PAPER);
							ItemMeta m = item.getItemMeta();
							List<String> lore = new ArrayList<>();
							lore.add("§7DENIED");
							m.setLore(lore);
							m.setDisplayName("Suchbegriff eingeben...");
							item.setItemMeta(m);
							
							p.getOpenInventory().getTopInventory().addItem(item);
							filter.add(p);
						}else if(e.getCurrentItem().getType().equals(Material.STAINED_CLAY)){
							e.setCancelled(true);
							if(e.getCurrentItem().getItemMeta().getDisplayName().contains("Ange")){
								p.openInventory(getAcceptedPayments(0, false, null));
							}else {
								p.openInventory(getWaitingPayments(0, false, null));
							}
						}else {
							e.setCancelled(true);
							p.openInventory(statusinv(e.getCurrentItem(), "DENIED"));
						}
					}
				}
			}
		}
	}
	
	public static Inventory getDeniedPayments(int page, boolean filter, String fs){
		Inventory inv = Bukkit.createInventory(null, 9*6, "§bPayments §7» §bDENIED §7(" + (page + 1) + ")");
		
		ArrayList<String> pay = ShopSQL.getPaymentsDENIED();
		int s = pay.size();
		
		int slot = 9;
		for(int i = 0;i < s; i++){
			ArrayList<String> p = getNewest(pay);
			if((i == 0 && page == 0) | (i > page*32 && slot < 33)){
				String[] tmp = p.get(0).split("§<-->§");
				String uuid = tmp[0];
				String type = tmp[1];
				String price = tmp[2];
				String date = tmp[3];
				String code = tmp[5];
				
				if(!filter || PlayerData.getName(UUID.fromString(uuid)).contains(fs) || type.contains(fs) || price.contains(fs) || date.contains(fs) || code.contains(fs)){
					Material m = Material.GOLD_INGOT;
					if(type.contains("Prem")){
						m = Material.GOLDEN_APPLE;
					}
					ItemStack item = new ItemStack(m);
					if(type.contains("Life")){
						item = new ItemStack(m, 64, (short) 1);
					}else if(type.contains("7 Tage")){
						item.setAmount(7);
					}else if(type.contains("30 Tage")){
						item.setAmount(30);
					}
					
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(type);
					List<String> lore = new ArrayList<>();
					lore.add("§7» §cSpieler: §6" + PlayerData.getName(UUID.fromString(uuid)));
					lore.add("§7» §cPreis: §6" + price);
					lore.add("§7» §cDatum: §6" + date);
					lore.add("§7» §cCode: §6" + code);
					meta.setLore(lore);
					item.setItemMeta(meta);
					
					inv.setItem(slot, item);
					slot++;
				}
			}
			pay.remove(p.get(0));
		}
		
		ItemStack vor = new ItemStack(Material.IRON_DOOR);
		ItemMeta vorm = vor.getItemMeta();
		vorm.setDisplayName("§7» §cSeite " + (page + 2));
		vor.setItemMeta(vorm);
		
		ItemStack back = new ItemStack(Material.IRON_DOOR);
		ItemMeta backm = back.getItemMeta();
		backm.setDisplayName("§7» §cSeite " + page);
		back.setItemMeta(backm);
		
		ItemStack fil = new ItemStack(Material.ANVIL);
		ItemMeta film = fil.getItemMeta();
		if(fs != null){
			film.setDisplayName("§7» §cFilter: " + fs);
		}else {
			film.setDisplayName("§7» §cFilter");
		}
		fil.setItemMeta(film);
		
		ItemStack acc = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		ItemMeta accm = acc.getItemMeta();
		accm.setDisplayName("§7» §aAngenommen");
		acc.setItemMeta(accm);
		
		ItemStack den = new ItemStack(Material.STAINED_CLAY, 1, (short) 9);
		ItemMeta denm = den.getItemMeta();
		denm.setDisplayName("§7» §7Wartend");
		den.setItemMeta(denm);
		
		inv.setItem(4, fil);
		inv.setItem(6*9-1, vor);
		if(page > 0){
			inv.setItem(6*9-2, back);
		}
		inv.setItem(6*9-8, acc);
		inv.setItem(6*9-9, den);
		
		return inv;
	}
	
	public static Inventory getAcceptedPayments(int page, boolean filter, String fs){
		Inventory inv = Bukkit.createInventory(null, 9*6, "§bPayments §7» §bACCEPTED §7(" + (page + 1) + ")");
		
		ArrayList<String> pay = ShopSQL.getPaymentsACCEPTED();
		int s = pay.size();
		
		int slot = 9;
		for(int i = 0;i < s; i++){
			ArrayList<String> p = getNewest(pay);
			if((i == 0 && page == 0) | (i > page*32 && slot < 33)){
				String[] tmp = p.get(0).split("§<-->§");
				String uuid = tmp[0];
				String type = tmp[1];
				String price = tmp[2];
				String date = tmp[3];
				String code = tmp[5];
				
				if(!filter || PlayerData.getName(UUID.fromString(uuid)).contains(fs) || type.contains(fs) || price.contains(fs) || date.contains(fs) || code.contains(fs)){
					Material m = Material.GOLD_INGOT;
					if(type.contains("Prem")){
						m = Material.GOLDEN_APPLE;
					}
					ItemStack item = new ItemStack(m);
					if(type.contains("Life")){
						item = new ItemStack(m, 64, (short) 1);
					}else if(type.contains("7 Tage")){
						item.setAmount(7);
					}else if(type.contains("30 Tage")){
						item.setAmount(30);
					}
					
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(type);
					List<String> lore = new ArrayList<>();
					lore.add("§7» §cSpieler: §6" + PlayerData.getName(UUID.fromString(uuid)));
					lore.add("§7» §cPreis: §6" + price);
					lore.add("§7» §cDatum: §6" + date);
					lore.add("§7» §cCode: §6" + code);
					meta.setLore(lore);
					item.setItemMeta(meta);
					
					inv.setItem(slot, item);
					slot++;
				}
			}
			pay.remove(p.get(0));
		}
		
		ItemStack vor = new ItemStack(Material.IRON_DOOR);
		ItemMeta vorm = vor.getItemMeta();
		vorm.setDisplayName("§7» §cSeite " + (page + 2));
		vor.setItemMeta(vorm);
		
		ItemStack back = new ItemStack(Material.IRON_DOOR);
		ItemMeta backm = back.getItemMeta();
		backm.setDisplayName("§7» §cSeite " + page);
		back.setItemMeta(backm);
		
		ItemStack fil = new ItemStack(Material.ANVIL);
		ItemMeta film = fil.getItemMeta();
		if(fs != null){
			film.setDisplayName("§7» §cFilter: " + fs);
		}else {
			film.setDisplayName("§7» §cFilter");
		}
		fil.setItemMeta(film);
		
		ItemStack acc = new ItemStack(Material.STAINED_CLAY, 1, (short) 9);
		ItemMeta accm = acc.getItemMeta();
		accm.setDisplayName("§7» §7Wartend");
		acc.setItemMeta(accm);
		
		ItemStack den = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
		ItemMeta denm = den.getItemMeta();
		denm.setDisplayName("§7» §cAbgelehnt");
		den.setItemMeta(denm);
		
		inv.setItem(4, fil);
		inv.setItem(6*9-1, vor);
		if(page > 0){
			inv.setItem(6*9-2, back);
		}
		inv.setItem(6*9-8, acc);
		inv.setItem(6*9-9, den);
		
		return inv;
	}
	
	public static Inventory getWaitingPayments(int page, boolean filter, String fs){
		Inventory inv = Bukkit.createInventory(null, 9*6, "§bPayments §7» §bWAITING §7(" + (page + 1) + ")");
		
		ArrayList<String> pay = ShopSQL.getPaymentsWAITING();
		int s = pay.size();
		
		int slot = 9;
		for(int i = 0;i < s; i++){
			ArrayList<String> p = getOldest(pay);
			if((i == 0 && page == 0) | (i > page*32 && slot < 33)){
				String[] tmp = p.get(0).split("§<-->§");
				String uuid = tmp[0];
				String type = tmp[1];
				String price = tmp[2];
				String date = tmp[3];
				String code = tmp[5];
				
				if(!filter || PlayerData.getName(UUID.fromString(uuid)).contains(fs) || type.contains(fs) || price.contains(fs) || date.contains(fs) || code.contains(fs)){
					Material m = Material.GOLD_INGOT;
					if(type.contains("Prem")){
						m = Material.GOLDEN_APPLE;
					}
					ItemStack item = new ItemStack(m);
					if(type.contains("Life")){
						item = new ItemStack(m, 64, (short) 1);
					}else if(type.contains("7 Tage")){
						item.setAmount(7);
					}else if(type.contains("30 Tage")){
						item.setAmount(30);
					}
					
					ItemMeta meta = item.getItemMeta();
					meta.setDisplayName(type);
					List<String> lore = new ArrayList<>();
					lore.add("§7» §cSpieler: §6" + PlayerData.getName(UUID.fromString(uuid)));
					lore.add("§7» §cPreis: §6" + price);
					lore.add("§7» §cDatum: §6" + date);
					lore.add("§7» §cCode: §6" + code);
					meta.setLore(lore);
					item.setItemMeta(meta);
					
					inv.setItem(slot, item);
					slot++;
				}
			}
			pay.remove(p.get(0));
		}
		
		ItemStack vor = new ItemStack(Material.IRON_DOOR);
		ItemMeta vorm = vor.getItemMeta();
		vorm.setDisplayName("§7» §cSeite " + (page + 2));
		vor.setItemMeta(vorm);
		
		ItemStack back = new ItemStack(Material.IRON_DOOR);
		ItemMeta backm = back.getItemMeta();
		backm.setDisplayName("§7» §cSeite " + page);
		back.setItemMeta(backm);
		
		ItemStack fil = new ItemStack(Material.ANVIL);
		ItemMeta film = fil.getItemMeta();
		if(fs != null){
			film.setDisplayName("§7» §cFilter: " + fs);
		}else {
			film.setDisplayName("§7» §cFilter");
		}
		fil.setItemMeta(film);
		
		ItemStack acc = new ItemStack(Material.STAINED_CLAY, 1, (short) 5);
		ItemMeta accm = acc.getItemMeta();
		accm.setDisplayName("§7» §aAngenommen");
		acc.setItemMeta(accm);
		
		ItemStack den = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
		ItemMeta denm = den.getItemMeta();
		denm.setDisplayName("§7» §cAbgelehnt");
		den.setItemMeta(denm);
		
		inv.setItem(4, fil);
		inv.setItem(6*9-1, vor);
		if(page > 0){
			inv.setItem(6*9-2, back);
		}
		inv.setItem(6*9-8, acc);
		inv.setItem(6*9-9, den);
		
		return inv;
	}
	
	public static ArrayList<String> getOldest(ArrayList<String> payments){
		ArrayList<String> ret = new ArrayList<>();
		
		HashMap<Integer, Long> time = new HashMap<>();
		HashMap<Integer, String> time1 = new HashMap<>();
		int id = 0;
		for(String s : payments){
			String[] tmp = s.split("§<-->§");
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			try {
				Date d = sdf.parse(tmp[3]);
				time.put(id, d.getTime());
				time1.put(id, s);
				id++;
				
			} catch (ParseException e) {}
		}
		
		long smallest = Long.MAX_VALUE;
		int ids = -1;
		for(Integer l : time.keySet()){
			if(time.get(l) < smallest){
				ids = l;
				smallest = time.get(l);
			}
		}
		if(ids > -1){
			for(Integer l : time.keySet()){
				if(time.get(l) == smallest){
					ret.add(time1.get(l));
				}
			}
		}		
		return ret;
	}
	
	public static ArrayList<String> getNewest(ArrayList<String> payments){
		ArrayList<String> ret = new ArrayList<>();
		
		HashMap<Integer, Long> time = new HashMap<>();
		HashMap<Integer, String> time1 = new HashMap<>();
		int id = 0;
		for(String s : payments){
			String[] tmp = s.split("§<-->§");
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			try {
				Date d = sdf.parse(tmp[3]);
				time.put(id, d.getTime());
				time1.put(id, s);
				id++;
				
			} catch (ParseException e) {}
		}
		
		long smallest = Long.MIN_VALUE;
		int ids = -1;
		for(Integer l : time.keySet()){
			if(time.get(l) > smallest){
				ids = l;
				smallest = time.get(l);
			}
		}
		if(ids > -1){
			for(Integer l : time.keySet()){
				if(time.get(l) == smallest){
					ret.add(time1.get(l));
				}
			}
		}		
		return ret;
	}
	
	public static Inventory statusinv(ItemStack item, String type) {
		Inventory inv = Bukkit.createInventory(null, 36, "§bPayments §7» §bBearbeitung");
		
		ItemStack yes = new ItemStack(Material.STAINED_CLAY, 1, (short)5);
		ItemMeta ym = yes.getItemMeta();
		List<String> lore = new ArrayList<>();
		lore.add("§7" + type);
		ym.setLore(lore);
		ym.setDisplayName("§aBESTÄTIGEN");
		yes.setItemMeta(ym);
		
		ItemStack no = new ItemStack(Material.STAINED_CLAY, 1, (short)14);
		ItemMeta nm = no.getItemMeta();
		nm.setLore(lore);
		nm.setDisplayName("§cABLEHNEN");
		no.setItemMeta(nm);
		
		inv.setItem(10, yes);
		inv.setItem(11, yes);
		inv.setItem(13, item);
		inv.setItem(15, no);
		inv.setItem(16, no);
		inv.setItem(19, yes);
		inv.setItem(20, yes);
		inv.setItem(24, no);
		inv.setItem(25, no);
		
		return inv;
	}

}
