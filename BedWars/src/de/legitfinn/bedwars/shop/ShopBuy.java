package de.legitfinn.bedwars.shop;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import de.legitfinn.bedwars.methoden.SpawnerType;
import de.legitfinn.bedwars.methoden.TeamManager;

public class ShopBuy implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInv(InventoryClickEvent e) {
		if(e.getClickedInventory() != null) {
			if(e.getClickedInventory().getTitle().equals(Shop.getShop().getTitle())) {
				e.setCancelled(true);
				if(e.getRawSlot() >= 27 && e.getRawSlot() <= 44) {
					ItemStack is = e.getClickedInventory().getItem(e.getRawSlot());
					Player p = (Player)e.getWhoClicked();
					if(e.getRawSlot() >= 36 && e.getRawSlot() <= 44) {
						is = e.getClickedInventory().getItem(e.getRawSlot() - 9);;
					}
					if(is.getType().equals(Material.STAINED_GLASS_PANE)) {
						return;
					}
					Material m = is.getType();
					int amount = 1;
					if(e.getClick().isShiftClick()) {
						amount = 64;
					}
					if(m.equals(Material.SANDSTONE)) {
						buyItem(p, is, amount, SpawnerType.Bronze, 1);
					} else if(m.equals(Material.ENDER_STONE)) {
						buyItem(p, is, amount, SpawnerType.Bronze, 5);
					} else if(m.equals(Material.IRON_BLOCK)) {
						buyItem(p, is, amount, SpawnerType.Eisen, 3);
					} else if(m.equals(Material.GLOWSTONE)) {
						buyItem(p, is, amount, SpawnerType.Bronze, 16);
					} else if(m.equals(Material.STAINED_GLASS)) {
						buyItem(p, is, amount, SpawnerType.Bronze, 8);
					} else if(m.equals(Material.SLIME_BLOCK)) {
						buyItem(p, is, amount, SpawnerType.Eisen, 2);
					} else if(m.equals(Material.STICK)) {
						buyItem(p, is, amount, SpawnerType.Bronze, 16);
					} else if(m.equals(Material.GOLD_SWORD)) {
						if(is.containsEnchantment(Enchantment.DAMAGE_ALL)) {
							if(is.getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 1) {
								buyItem(p, is, amount, SpawnerType.Eisen, 3);
							} else if(is.getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 2) {
								buyItem(p, is, amount, SpawnerType.Eisen, 5);
							}
						} else {
							buyItem(p, is, amount, SpawnerType.Eisen, 1);
						}
					} else if(m.equals(Material.IRON_SWORD)) {
						buyItem(p, is, amount, SpawnerType.Gold, 5);
					} else if(m.equals(Material.LEATHER_HELMET)) {
						ItemStack item = is.clone();
						LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
						meta.setColor(DyeColor.getByWoolData(TeamManager.ChatColorToColorByte(TeamManager.getTeamColor(TeamManager.getTeamOfPlayer(p)))).getColor());
						item.setItemMeta(meta);
						buyItem(p, item, amount, SpawnerType.Bronze, 1);
					} else if(m.equals(Material.LEATHER_BOOTS)) {
						ItemStack item = is.clone();
						LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
						meta.setColor(DyeColor.getByWoolData(TeamManager.ChatColorToColorByte(TeamManager.getTeamColor(TeamManager.getTeamOfPlayer(p)))).getColor());
						item.setItemMeta(meta);
						buyItem(p, item, amount, SpawnerType.Bronze, 1);
					} else if(m.equals(Material.LEATHER_LEGGINGS)) {
						ItemStack item = is.clone();
						LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
						meta.setColor(DyeColor.getByWoolData(TeamManager.ChatColorToColorByte(TeamManager.getTeamColor(TeamManager.getTeamOfPlayer(p)))).getColor());
						item.setItemMeta(meta);
						buyItem(p, item, amount, SpawnerType.Bronze, 1);
					} else if(m.equals(Material.CHAINMAIL_CHESTPLATE)) {
						if(is.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) {
							if(is.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) == 1) {
								buyItem(p, is, amount, SpawnerType.Eisen, 3);
							} else if(is.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) == 2) {
								buyItem(p, is, amount, SpawnerType.Eisen, 7);
							}
						} else {
							buyItem(p, is, amount, SpawnerType.Eisen, 1);
						}
					} else if(m.equals(Material.WOOD_PICKAXE)) {
						buyItem(p, is, amount, SpawnerType.Bronze, 8);
					} else if(m.equals(Material.STONE_PICKAXE)) {
						buyItem(p, is, amount, SpawnerType.Eisen, 2);
					} else if(m.equals(Material.IRON_PICKAXE)) {
						buyItem(p, is, amount, SpawnerType.Gold, 1);
					} else if(m.equals(Material.ARROW)) {
						buyItem(p, is, amount, SpawnerType.Gold, 1);
					} else if(m.equals(Material.BOW)) {
						if(is.containsEnchantment(Enchantment.ARROW_DAMAGE)) {
							if(is.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) == 1) {
								buyItem(p, is, amount, SpawnerType.Gold, 7);
							} else if(is.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) == 2) {
								buyItem(p, is, amount, SpawnerType.Gold, 13);
							}
						} else {
							buyItem(p, is, amount, SpawnerType.Gold, 3);
						}
					} else if(m.equals(Material.APPLE)) {
						buyItem(p, is, amount, SpawnerType.Bronze, 1);
					} else if(m.equals(Material.GRILLED_PORK)) {
						buyItem(p, is, amount, SpawnerType.Bronze, 2);
					} else if(m.equals(Material.CAKE)) {
						buyItem(p, is, amount, SpawnerType.Eisen, 2);
					} else if(m.equals(Material.GOLDEN_APPLE)) {
						buyItem(p, is, amount, SpawnerType.Gold, 2);
					} else if(m.equals(Material.CARROT_ITEM)) {
						buyItem(p, is, amount, SpawnerType.Bronze, 1);
					} else if(m.equals(Material.CHEST)) {
						buyItem(p, is, amount, SpawnerType.Eisen, 2);
					} else if(m.equals(Material.ENDER_CHEST)) {
						buyItem(p, is, amount, SpawnerType.Gold, 2);
					} else if(m.equals(Material.POTION)) {
						byte data = is.getData().getData();
						if(data == (byte)8261) {
							buyItem(p, is, amount, SpawnerType.Eisen, 3);
						} else if(data == (byte)8229) {
							buyItem(p, is, amount, SpawnerType.Eisen, 5);
						} else if(data == (byte)8194) {
							buyItem(p, is, amount, SpawnerType.Eisen, 8);
						} else if(data == (byte)8235) {
							buyItem(p, is, amount, SpawnerType.Eisen, 16);
						} else if(data == (byte)8201) {
							buyItem(p, is, amount, SpawnerType.Gold, 8);
						}
					} else if(m.equals(Material.LADDER)) {
						buyItem(p, is, amount, SpawnerType.Bronze, 2);
					} else if(m.equals(Material.WEB)) {
						buyItem(p, is, amount, SpawnerType.Bronze, 16);
					} else if(m.equals(Material.SULPHUR)) {
						buyItem(p, is, amount, SpawnerType.Eisen, 8);
					} else if(m.equals(Material.STRING)) {
						buyItem(p, is, amount, SpawnerType.Eisen, 4);
					} else if(m.equals(Material.BLAZE_ROD)) {
						buyItem(p, is, amount, SpawnerType.Gold, 3);
					} else if(m.equals(Material.ENDER_PEARL)) {
						buyItem(p, is, amount, SpawnerType.Gold, 14);
					} else if(m.equals(Material.TNT)) {
						buyItem(p, is, amount, SpawnerType.Gold, 3);
					} else if(m.equals(Material.FLINT_AND_STEEL)) {
						buyItem(p, is, amount, SpawnerType.Eisen, 7);
					} else if(m.equals(Material.FISHING_ROD)) {
						buyItem(p, is, amount, SpawnerType.Eisen, 10);
					}
				}
			}
		}
	}
	
	public static boolean hasRessources(Player p, SpawnerType type, int amount) {
		Material m = Material.CLAY_BRICK;
		if(type.equals(SpawnerType.Eisen)) {
			m = Material.IRON_INGOT;
		} else if(type.equals(SpawnerType.Gold)) {
			m = Material.GOLD_INGOT;
		}
		
		int i = 0;
		for(ItemStack all : p.getInventory().getContents()) {
			if(all != null) {
				if(all.getType().equals(m)) {
					i += all.getAmount();
				}
			}
		}
		if(i >= amount) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void buyItem(Player p, ItemStack is, int amount, SpawnerType type, int cost) {
		if(amount > 1) {
			int i = 0;
			while(hasRessources(p, type, cost) && i < amount && (i + is.getAmount()) <= amount) {
				buyItemWithoutCost(p, is);
				i += is.getAmount();
				removeCost(p, type, cost);
			}
			p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
		} else {
			if(hasRessources(p, type, cost)) {
				buyItemWithoutCost(p, is);
				removeCost(p, type, cost);
				p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
			}
		}
	}
	
	public static void buyItemWithoutCost(Player p, ItemStack is) {
		p.getInventory().addItem(is);
	}
	
	public static void removeCost(Player p, SpawnerType type, int cost) {
		int i = 0;
		int slot = 0;
		Material m = Material.CLAY_BRICK;
		if(type.equals(SpawnerType.Eisen)) {
			m = Material.IRON_INGOT;
		} else if(type.equals(SpawnerType.Gold)) {
			m = Material.GOLD_INGOT;
		}
		for(ItemStack all : p.getInventory().getContents()) {
			if(all != null) {
				if(all.getType().equals(m)) {
					if(all.getAmount() <= (cost - i)) {
						i += all.getAmount();
						p.getInventory().setItem(slot, null);
					} else if(all.getAmount() > (cost - i)){
						int rest = (cost - i);
						i += all.getAmount() - rest;
						all.setAmount(all.getAmount() - rest);
						break;
					}
				}
			}
			slot ++;
		}
	}
	
	
	
}
