package de.legitfinn.bedwars.shop;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.legitfinn.bedwars.main.DataManager;
import de.legitfinn.bedwars.methoden.GameState;
import de.legitfinn.bedwars.methoden.SpawnerType;
public class Shop implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		if(e.getRightClicked().getType().equals(EntityType.VILLAGER)) {
			if(DataManager.state.equals(GameState.Ingame)) {
				e.setCancelled(true);
				e.getPlayer().openInventory(getShop());
			}
		}
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null) {
			if(e.getClickedInventory().getTitle().equals(getShop().getTitle())) {
				e.setCancelled(true);
				if(e.getRawSlot() >= 9 && e.getRawSlot() <= 17) {
					ItemStack is = e.getCurrentItem();
					Player p = (Player)e.getWhoClicked();
					if(is.getType().equals(Material.SANDSTONE)) {
						p.openInventory(getBlocks());
					} else if(is.getType().equals(Material.GOLD_SWORD)) {
						p.openInventory(getSword());
					} else if(is.getType().equals(Material.CHAINMAIL_CHESTPLATE)) {
						p.openInventory(getArmor());
					} else if(is.getType().equals(Material.STONE_PICKAXE)) {
						p.openInventory(getPickaxes());
					} else if(is.getType().equals(Material.BOW)) {
						p.openInventory(getBow());
					} else if(is.getType().equals(Material.CAKE)) {
						p.openInventory(getFood());
					} else if(is.getType().equals(Material.CHEST)) {
						p.openInventory(getChest());
					} else if(is.getType().equals(Material.POTION)) {
						p.openInventory(getPotions());
					} else if(is.getType().equals(Material.TNT)) {
						p.openInventory(getSpecial());
					}
				}
				
			}
		}
	}
	
	public static Inventory getShop() {
		
		Inventory inv = Bukkit.createInventory(null, 27, "§7» §cShop");
		
		ItemStack none = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)7);
		ItemMeta nm = none.getItemMeta();
		nm.setDisplayName("§c");
		none.setItemMeta(nm);
		
		ItemStack blocks = getItemStack(Material.SANDSTONE, 1, (short)0, "§7» §cBlöcke §7§o<Klick>", new String[]{"§8» §7Hier kannst du","§8» §7Baumaterial kaufen."});
		ItemStack sword = getItemStack(Material.GOLD_SWORD, 1, (short)0, "§7» §cSchwerter §7§o<Klick>", new String[]{"§8» §7Hier kannst du","§8» §7Schwerter kaufen."});
		ItemMeta sm = sword.getItemMeta();
		sm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		sword.setItemMeta(sm);	
		ItemStack armor = getItemStack(Material.CHAINMAIL_CHESTPLATE, 1, (short)0, "§7» §cRüstung §7§o<Klick>", new String[]{"§8» §7Hier kannst du","§8» §7Rüstung kaufen."});
		ItemStack pickaxe = getItemStack(Material.STONE_PICKAXE, 1, (short)0, "§7» §cSpitzhacken §7§o<Klick>", new String[]{"§8» §7Hier kannst du","§8» §7Spitzhacken kaufen."});
		ItemMeta pm = pickaxe.getItemMeta();
		pm.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		pickaxe.setItemMeta(pm);
		ItemStack bow = getItemStack(Material.BOW, 1, (short)0, "§7» §cBogen §7§o<Klick>", new String[]{"§8» §7Hier kannst du","§8» §7Pfeil & Bogen kaufen."});
		ItemStack food = getItemStack(Material.CAKE, 1, (short)0, "§7» §cNahrung §7§o<Klick>", new String[]{"§8» §7Hier kannst du","§8» §7Nahrung kaufen."});
		ItemStack chest = getItemStack(Material.CHEST, 1, (short)0, "§7» §cKisten §7§o<Klick>", new String[]{"§8» §7Hier kannst du","§8» §7Kisten kaufen."});
		ItemStack potions = getItemStack(Material.POTION, 1, (short)8193, "§7» §cBlocks §7§o<Klick>", new String[]{"§8» §7Hier kannst du","§8» §7Tränke kaufen."});
		ItemMeta pom = potions.getItemMeta();
		pom.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		potions.setItemMeta(pom);
		ItemStack special = getItemStack(Material.TNT, 1, (short)0, "§7» §cBlocks §7§o<Klick>", new String[]{"§8» §7Hier kannst du","§8» §7spezielle Items kaufen."});
		for(int i = 0; i <= 8; i++) {
			inv.setItem(i, none);
		}
		for(int i = 18; i <= 26; i++) {
			inv.setItem(i, none);
		}
		inv.setItem(9, blocks);
		inv.setItem(10, sword);
		inv.setItem(11, armor);
		inv.setItem(12, pickaxe);
		inv.setItem(13, bow);
		inv.setItem(14, food);
		inv.setItem(15, chest);
		inv.setItem(16, potions);
		inv.setItem(17, special);
		
		return inv;
	}
	
	public static Inventory getBlocks() {
		Inventory inv = Bukkit.createInventory(null, getShop().getSize() + 27, getShop().getTitle());
		inv.setContents(getShop().getContents());
		
		ItemStack none = getItemStack(Material.STAINED_GLASS_PANE, 1, (short)7, "§c", null);
		
		ItemStack b1 = getItemStack(Material.SANDSTONE, 2, (short)0, null, null);
		ItemStack b2 = getItemStack(Material.ENDER_STONE, 1, (short)0, null, null);
		ItemStack b3 = getItemStack(Material.IRON_BLOCK, 1, (short)0, null, null);
		ItemStack b4 = getItemStack(Material.GLOWSTONE, 1, (short)0, null, null);
		ItemStack b5 = getItemStack(Material.STAINED_GLASS, 1, (short)0, null, null);
		ItemStack b6 = getItemStack(Material.SLIME_BLOCK, 1, (short)0, null, null);
		
		inv.setItem(28, b1); inv.setItem(28 + 9, getCost(SpawnerType.Bronze, 1));
		inv.setItem(29, b2); inv.setItem(29 + 9, getCost(SpawnerType.Bronze, 5));
		inv.setItem(30, b3); inv.setItem(30 + 9, getCost(SpawnerType.Eisen, 3));
		inv.setItem(31, b4); inv.setItem(31 + 9, getCost(SpawnerType.Bronze, 16));
		inv.setItem(32, b5); inv.setItem(32 + 9, getCost(SpawnerType.Bronze, 8));
		inv.setItem(33, b6); inv.setItem(33 + 9, getCost(SpawnerType.Eisen, 2));
		
		for(int i = 0; i <= inv.getSize() - 1; i ++) {
			if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				inv.setItem(i, none);
			}
		}
		return inv;
	}
	
	public static Inventory getSword() {
		Inventory inv = Bukkit.createInventory(null, getShop().getSize() + 27, getShop().getTitle());
		inv.setContents(getShop().getContents());
		
		ItemStack none = getItemStack(Material.STAINED_GLASS_PANE, 1, (short)7, "§c", null);
		
		ItemStack b1 = getItemStack(Material.STICK, 1, (short)0, null, null);
		b1 = enchantItem(b1, Enchantment.KNOCKBACK, 2);
		ItemStack b2 = getItemStack(Material.GOLD_SWORD, 1, (short)0, null, null);
		b2 = enchantItem(b2, Enchantment.DURABILITY, 3);
		ItemStack b3 = getItemStack(Material.GOLD_SWORD, 1, (short)0, null, null);
		b3 = enchantItem(b3, Enchantment.DAMAGE_ALL, 1);
		b3 = enchantItem(b3, Enchantment.DURABILITY, 3);
		ItemStack b4 = getItemStack(Material.GOLD_SWORD, 1, (short)0, null, null);
		b4 = enchantItem(b4, Enchantment.DAMAGE_ALL, 2);
		b4 = enchantItem(b4, Enchantment.DURABILITY, 3);
		ItemStack b5 = getItemStack(Material.IRON_SWORD, 1, (short)0, null, null);
		b5 = enchantItem(b5, Enchantment.DAMAGE_ALL, 2);
		b5 = enchantItem(b5, Enchantment.DURABILITY, 2);
		b5 = enchantItem(b5, Enchantment.KNOCKBACK, 1);
		
		inv.setItem(28, b1); inv.setItem(28 + 9, getCost(SpawnerType.Bronze, 16));
		inv.setItem(29, b2); inv.setItem(29 + 9, getCost(SpawnerType.Eisen, 1));
		inv.setItem(30, b3); inv.setItem(30 + 9, getCost(SpawnerType.Eisen, 3));
		inv.setItem(31, b4); inv.setItem(31 + 9, getCost(SpawnerType.Eisen, 5));
		inv.setItem(32, b5); inv.setItem(32 + 9, getCost(SpawnerType.Gold, 5));
		
		for(int i = 0; i <= inv.getSize() - 1; i ++) {
			if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				inv.setItem(i, none);
			}
		}
		return inv;
	}
	
	public static Inventory getArmor() {
		Inventory inv = Bukkit.createInventory(null, getShop().getSize() + 27, getShop().getTitle());
		inv.setContents(getShop().getContents());
		
		ItemStack none = getItemStack(Material.STAINED_GLASS_PANE, 1, (short)7, "§c", null);
		
		ItemStack b1 = getItemStack(Material.LEATHER_HELMET, 1, (short)0, null, null);
		b1 = enchantItem(b1, Enchantment.DURABILITY, 3);
		ItemStack b2 = getItemStack(Material.LEATHER_LEGGINGS, 1, (short)0, null, null);
		b2 = enchantItem(b2, Enchantment.DURABILITY, 3);
		ItemStack b3 = getItemStack(Material.LEATHER_BOOTS, 1, (short)0, null, null);
		b3 = enchantItem(b3, Enchantment.DURABILITY, 3);
		ItemStack b4 = getItemStack(Material.CHAINMAIL_CHESTPLATE, 1, (short)0, null, null);
		b4 = enchantItem(b4, Enchantment.DURABILITY, 3);
		ItemStack b5 = getItemStack(Material.CHAINMAIL_CHESTPLATE, 1, (short)0, null, null);
		b5 = enchantItem(b5, Enchantment.DURABILITY, 3);
		b5 = enchantItem(b5, Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		ItemStack b6 = getItemStack(Material.CHAINMAIL_CHESTPLATE, 1, (short)0, null, null);
		b6 = enchantItem(b6, Enchantment.DURABILITY, 3);
		b6 = enchantItem(b6, Enchantment.PROTECTION_ENVIRONMENTAL, 2);
		
		inv.setItem(28, b1); inv.setItem(28 + 9, getCost(SpawnerType.Bronze, 1));
		inv.setItem(29, b2); inv.setItem(29 + 9, getCost(SpawnerType.Bronze, 1));
		inv.setItem(30, b3); inv.setItem(30 + 9, getCost(SpawnerType.Bronze, 1));
		inv.setItem(31, b4); inv.setItem(31 + 9, getCost(SpawnerType.Eisen, 1));
		inv.setItem(32, b5); inv.setItem(32 + 9, getCost(SpawnerType.Eisen, 3));
		inv.setItem(33, b6); inv.setItem(33 + 9, getCost(SpawnerType.Eisen, 7));
		
		for(int i = 0; i <= inv.getSize() - 1; i ++) {
			if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				inv.setItem(i, none);
			}
		}
		return inv;
	}
	
	public static Inventory getPickaxes() {
		Inventory inv = Bukkit.createInventory(null, getShop().getSize() + 27, getShop().getTitle());
		inv.setContents(getShop().getContents());
		
		ItemStack none = getItemStack(Material.STAINED_GLASS_PANE, 1, (short)7, "§c", null);
		
		ItemStack b1 = getItemStack(Material.WOOD_PICKAXE, 1, (short)0, null, null);
		b1 = enchantItem(b1, Enchantment.DURABILITY, 1);
		b1 = enchantItem(b1, Enchantment.DIG_SPEED, 1);
		ItemStack b2 = getItemStack(Material.STONE_PICKAXE, 1, (short)0, null, null);
		b2 = enchantItem(b2, Enchantment.DURABILITY, 1);
		b2 = enchantItem(b2, Enchantment.DIG_SPEED, 1);
		ItemStack b3 = getItemStack(Material.IRON_PICKAXE, 1, (short)0, null, null);
		b3 = enchantItem(b3, Enchantment.DURABILITY, 1);
		b3 = enchantItem(b3, Enchantment.DIG_SPEED, 1);
		
		inv.setItem(28, b1); inv.setItem(28 + 9, getCost(SpawnerType.Bronze, 8));
		inv.setItem(29, b2); inv.setItem(29 + 9, getCost(SpawnerType.Eisen, 2));
		inv.setItem(30, b3); inv.setItem(30 + 9, getCost(SpawnerType.Gold, 1));
		
		for(int i = 0; i <= inv.getSize() - 1; i ++) {
			if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				inv.setItem(i, none);
			}
		}
		return inv;
	}
	
	public static Inventory getBow() {
		Inventory inv = Bukkit.createInventory(null, getShop().getSize() + 27, getShop().getTitle());
		inv.setContents(getShop().getContents());
		
		ItemStack none = getItemStack(Material.STAINED_GLASS_PANE, 1, (short)7, "§c", null);
		
		ItemStack b1 = getItemStack(Material.BOW, 1, (short)0, null, null);
		b1 = enchantItem(b1, Enchantment.DURABILITY, 1);
		b1 = enchantItem(b1, Enchantment.ARROW_INFINITE, 1);
		ItemStack b2 = getItemStack(Material.BOW, 1, (short)0, null, null);
		b2 = enchantItem(b2, Enchantment.DURABILITY, 1);
		b2 = enchantItem(b2, Enchantment.ARROW_DAMAGE, 1);
		b2 = enchantItem(b2, Enchantment.ARROW_INFINITE, 1);
		ItemStack b3 = getItemStack(Material.BOW, 1, (short)0, null, null);
		b3 = enchantItem(b3, Enchantment.ARROW_DAMAGE, 2);
		b3 = enchantItem(b3, Enchantment.DURABILITY, 1);
		b3 = enchantItem(b3, Enchantment.ARROW_KNOCKBACK, 1);
		b3 = enchantItem(b3, Enchantment.ARROW_INFINITE, 1);
		ItemStack b4 = getItemStack(Material.ARROW, 1, (short)0, null, null);
		
		inv.setItem(28, b1); inv.setItem(28 + 9, getCost(SpawnerType.Gold, 3));
		inv.setItem(29, b2); inv.setItem(29 + 9, getCost(SpawnerType.Gold, 7));
		inv.setItem(30, b3); inv.setItem(30 + 9, getCost(SpawnerType.Gold, 13));
		inv.setItem(31, b4); inv.setItem(31 + 9, getCost(SpawnerType.Gold, 1));
		
		for(int i = 0; i <= inv.getSize() - 1; i ++) {
			if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				inv.setItem(i, none);
			}
		}
		return inv;
	}
	
	public static Inventory getFood() {
		Inventory inv = Bukkit.createInventory(null, getShop().getSize() + 27, getShop().getTitle());
		inv.setContents(getShop().getContents());
		
		ItemStack none = getItemStack(Material.STAINED_GLASS_PANE, 1, (short)7, "§c", null);
		
		ItemStack b1 = getItemStack(Material.APPLE, 1, (short)0, null, null);
		ItemStack b2 = getItemStack(Material.GRILLED_PORK, 1, (short)0, null, null);
		ItemStack b3 = getItemStack(Material.CAKE, 1, (short)0, null, null);
		ItemStack b4 = getItemStack(Material.GOLDEN_APPLE, 1, (short)0, null, null);
		ItemStack b5 = getItemStack(Material.CARROT_ITEM, 2, (short)0, null, null);
		
		inv.setItem(28, b1); inv.setItem(28 + 9, getCost(SpawnerType.Bronze, 1));
		inv.setItem(29, b2); inv.setItem(29 + 9, getCost(SpawnerType.Bronze, 2));
		inv.setItem(30, b3); inv.setItem(30 + 9, getCost(SpawnerType.Eisen, 2));
		inv.setItem(31, b4); inv.setItem(31 + 9, getCost(SpawnerType.Gold, 2));
		inv.setItem(32, b5); inv.setItem(32 + 9, getCost(SpawnerType.Bronze, 1));
		
		for(int i = 0; i <= inv.getSize() - 1; i ++) {
			if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				inv.setItem(i, none);
			}
		}
		return inv;
	}
	
	public static Inventory getChest() {
		Inventory inv = Bukkit.createInventory(null, getShop().getSize() + 27, getShop().getTitle());
		inv.setContents(getShop().getContents());
		
		ItemStack none = getItemStack(Material.STAINED_GLASS_PANE, 1, (short)7, "§c", null);
		
		ItemStack b1 = getItemStack(Material.CHEST, 1, (short)0, null, null);
		ItemStack b2 = getItemStack(Material.ENDER_CHEST, 1, (short)0, "§aTeamkiste", null);
		
		inv.setItem(28, b1); inv.setItem(28 + 9, getCost(SpawnerType.Eisen, 2));
		inv.setItem(29, b2); inv.setItem(29 + 9, getCost(SpawnerType.Gold, 2));
		
		for(int i = 0; i <= inv.getSize() - 1; i ++) {
			if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				inv.setItem(i, none);
			}
		}
		return inv;
	}
	
	public static Inventory getPotions() {
		Inventory inv = Bukkit.createInventory(null, getShop().getSize() + 27, getShop().getTitle());
		inv.setContents(getShop().getContents());
		
		ItemStack none = getItemStack(Material.STAINED_GLASS_PANE, 1, (short)7, "§c", null);
		
		ItemStack b1 = getItemStack(Material.POTION, 1, (short)8261, null, null);
		ItemStack b2 = getItemStack(Material.POTION, 1, (short)8229, null, null);
		ItemStack b3 = getItemStack(Material.POTION, 1, (short)8194, null, null);
		ItemStack b4 = getItemStack(Material.POTION, 1, (short)8235, null, null);
		ItemStack b5 = getItemStack(Material.POTION, 1, (short)8201, null, null);
		
		inv.setItem(28, b1); inv.setItem(28 + 9, getCost(SpawnerType.Eisen, 3));
		inv.setItem(29, b2); inv.setItem(29 + 9, getCost(SpawnerType.Eisen, 5));
		inv.setItem(30, b3); inv.setItem(30 + 9, getCost(SpawnerType.Eisen, 8));
		inv.setItem(31, b4); inv.setItem(31 + 9, getCost(SpawnerType.Eisen, 16));
		inv.setItem(32, b5); inv.setItem(32 + 9, getCost(SpawnerType.Gold, 8));
		
		for(int i = 0; i <= inv.getSize() - 1; i ++) {
			if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				inv.setItem(i, none);
			}
		}
		return inv;
	}
	
	public static Inventory getSpecial() {
		Inventory inv = Bukkit.createInventory(null, getShop().getSize() + 27, getShop().getTitle());
		inv.setContents(getShop().getContents());
		
		ItemStack none = getItemStack(Material.STAINED_GLASS_PANE, 1, (short)7, "§c", null);
		
		ItemStack b1 = getItemStack(Material.LADDER, 1, (short)0, null, null);
		ItemStack b2 = getItemStack(Material.WEB, 1, (short)0, null, null);
		ItemStack b3 = getItemStack(Material.SULPHUR, 1, (short)0, "§7» §cBaseteleporter", new String[]{"§8» §7Teleportiert dich","§8» §7zurück in deine Basis."});
		ItemStack b4 = getItemStack(Material.STRING, 1, (short)0, "§7» §cFalle", new String[]{"§8» §7Lößt einen Alarm aus,","§8» §7sobald ein gegner über diese läuft."});
		ItemStack b5 = getItemStack(Material.BLAZE_ROD, 1, (short)0, "§7» §cRettungsplatform", new String[]{"§8» §7Rettet dich durch einen Rechtsklick","§8» §7vor einem Absturz."});
		ItemStack b6 = getItemStack(Material.ENDER_PEARL, 1, (short)0, null, null);
		ItemStack b7 = getItemStack(Material.TNT, 1, (short)0, null, null);
		ItemStack b8 = getItemStack(Material.FLINT_AND_STEEL, 1, (short)0, null, null);
		ItemStack b9 = getItemStack(Material.FISHING_ROD, 1, (short)0, null, null);
		
		inv.setItem(27, b1); inv.setItem(27 + 9, getCost(SpawnerType.Bronze, 2));
		inv.setItem(28, b2); inv.setItem(28 + 9, getCost(SpawnerType.Bronze, 16));
		inv.setItem(29, b3); inv.setItem(29 + 9, getCost(SpawnerType.Eisen, 8));
		inv.setItem(30, b4); inv.setItem(30 + 9, getCost(SpawnerType.Eisen, 4));
		inv.setItem(31, b5); inv.setItem(31 + 9, getCost(SpawnerType.Gold, 3));
		inv.setItem(32, b6); inv.setItem(32 + 9, getCost(SpawnerType.Gold, 14));
		inv.setItem(33, b7); inv.setItem(33 + 9, getCost(SpawnerType.Gold, 3));
		inv.setItem(34, b8); inv.setItem(34 + 9, getCost(SpawnerType.Eisen, 7));
		inv.setItem(35, b9); inv.setItem(35 + 9, getCost(SpawnerType.Eisen, 10));
		
		for(int i = 0; i <= inv.getSize() - 1; i ++) {
			if(inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
				inv.setItem(i, none);
			}
		}
		return inv;
	}
	
	
	
	
	public static ItemStack getItemStack(Material m, int amount, short data, String displayName, String[] lore) {
		List<String> a = new ArrayList<String>();
		if(lore != null) {
			for(String all : lore) {
				a.add(all);
			}
		}
		ItemStack ret = new ItemStack(m, amount, data);
		ItemMeta meta = ret.getItemMeta();
		if(displayName != null) {
			meta.setDisplayName(displayName);
		}
		meta.setLore(a);
		ret.setItemMeta(meta);
		
		return ret;
	}
	
	public static ItemStack enchantItem(ItemStack is, Enchantment ench, int i) {
		ItemMeta meta = is.getItemMeta();
		meta.addEnchant(ench, i, true);
		is.setItemMeta(meta);
		return is;
	}
	
	public static ItemStack getCost(SpawnerType type, int i) {
		
		if(type.equals(SpawnerType.Bronze)) {
			ItemStack is = new ItemStack(Material.CLAY_BRICK);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§cBronze");
			is.setItemMeta(meta);
			is.setAmount(i);
			return is;
		} else if(type.equals(SpawnerType.Eisen)) {
			ItemStack is = new ItemStack(Material.IRON_INGOT);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§7Eisen");
			is.setItemMeta(meta);
			is.setAmount(i);
			return is;
		} else if(type.equals(SpawnerType.Gold)) {
			ItemStack is = new ItemStack(Material.GOLD_INGOT);
			ItemMeta meta = is.getItemMeta();
			meta.setDisplayName("§6Gold");
			is.setItemMeta(meta);
			is.setAmount(i);
			return is;
		} else {
			return getItemStack(Material.STAINED_GLASS_PANE, 1, (short)7, "§c", null);
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
}
