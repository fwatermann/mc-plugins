package de.legitfinn.lobby.functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import de.legitfinn.lobby.SQL.LobbyPetSQL;
import de.legitfinn.lobby.items.Gadgets;
import de.legitfinn.lobby.items.ShopAdmin;
import de.legitfinn.lobby.main.main;
import de.legitfinn.ppermissions.main.PPermissions;
import net.minecraft.server.v1_8_R3.ChatMessage;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutOpenWindow;

public class PetListener implements Listener {


	public static HashMap<Player, Pet> player_pet = new HashMap<Player, Pet>();
	public static ArrayList<Player> namingpet = new ArrayList<Player>();
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if(player_pet.containsKey(e.getPlayer())) {
			player_pet.get(e.getPlayer()).removePet();
			player_pet.remove(e.getPlayer());
			if(e.getPlayer().getPassenger() != null && e.getPlayer().getPassenger().getType().equals(EntityType.ARMOR_STAND)) {
				e.getPlayer().getPassenger().remove();
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(LobbyPetSQL.isListed(e.getPlayer().getUniqueId())) {
			String name = LobbyPetSQL.getPetName(e.getPlayer().getUniqueId());
			EntityType et = LobbyPetSQL.getPetType(e.getPlayer().getUniqueId());
			int size = LobbyPetSQL.getSize(e.getPlayer().getUniqueId());
			Gadgets.spawnPet(e.getPlayer(), et, size, name);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		Pet pet = getPet(e.getRightClicked());
		if(pet != null) {
			e.setCancelled(true);
			if(!pet.getOwner().equals(e.getPlayer())) {
				if(PPermissions.hasRight(e.getPlayer(), "seePetOwner")) {
					e.getPlayer().sendMessage("§4§lLobby §7» §cDieses Haustier gehört " + PPermissions.getChatColor(pet.getOwner()) + pet.getOwner().getName() + "§c.");
				}
			} else {
				openPetEdit(e.getPlayer(), pet.ent);
			}
		}
	}
	
	@EventHandler
	public void onArmor(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked().getType().equals(EntityType.ARMOR_STAND)) {
			e.setCancelled(true);
			Bukkit.getPluginManager().callEvent(new PlayerInteractEvent(e.getPlayer(), Action.RIGHT_CLICK_AIR, e.getPlayer().getItemInHand(), null, null));
			if(e.getPlayer().getPassenger() != null && e.getPlayer().getPassenger().equals(e.getRightClicked())) {
				if(e.getPlayer().getPassenger().getPassenger() != null && e.getPlayer().getPassenger().getPassenger().equals(player_pet.get(e.getPlayer()).ent)) {
					openPetEdit(e.getPlayer(), e.getPlayer().getPassenger().getPassenger());
				}
			}
		}
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getCurrentItem() != null) {
			if(e.getClickedInventory().getTitle().equalsIgnoreCase("§7» §cHaustier bearbeiten")) {
				Player p = (Player)e.getWhoClicked();
				e.setCancelled(true);
				if(e.getCurrentItem().getType().equals(Material.BARRIER)) {
					player_pet.get(p).removePet();
					player_pet.remove(p);
					p.closeInventory();
					LobbyPetSQL.remove(p.getUniqueId());
				} else if(e.getCurrentItem().getType().equals(Material.WHEAT)) {
					player_pet.get(p).setSize(player_pet.get(p).getSize() + 1);
				} else if(e.getCurrentItem().getType().equals(Material.NAME_TAG)) {
					EntityPlayer cp = ((CraftPlayer)p).getHandle();
					AnvilContainer con = new AnvilContainer(cp);
					int c = cp.nextContainerCounter();
					cp.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, "minecraft:anvil", new ChatMessage("Repairing", new Object[0]), 0));
					cp.activeContainer = con;
					cp.activeContainer.windowId = c;
					cp.activeContainer.addSlotListener(cp);
					
					ItemStack paper = new ItemStack(Material.PAPER);
					ItemMeta pm = paper.getItemMeta();
					pm.setDisplayName(player_pet.get(p).name.replace("§", "&"));
					paper.setItemMeta(pm);
					
					namingpet.add(p);
					p.getOpenInventory().getTopInventory().addItem(new ItemStack[]{paper});
				} else if(e.getCurrentItem().getType().equals(Material.SKULL_ITEM)) {
					if(p.getPassenger() != null && p.getPassenger().getType().equals(EntityType.ARMOR_STAND)) {
						p.getPassenger().remove();
					} else {
						ArmorStand as = p.getLocation().getWorld().spawn(p.getLocation(), ArmorStand.class);
						as.setSmall(true);
						as.setVisible(false);
						as.setPassenger(player_pet.get(p).ent);
						p.setPassenger(as);
					}
					p.closeInventory();
				} else if(e.getCurrentItem().getType().equals(Material.MINECART)) {
					if(player_pet.get(p).ent.getPassenger() != null && player_pet.get(p).ent.getPassenger().getEntityId() == p.getEntityId()) {
						player_pet.get(p).ent.setPassenger(null);
					} else {
						player_pet.get(p).ent.setPassenger(((Entity)p));
					}
				}
			} else if(e.getClickedInventory().getType().equals(InventoryType.ANVIL)) {
				Player p = (Player)e.getWhoClicked();
				e.setCancelled(true);
				if(namingpet.contains(p)) {
					if(e.getCurrentItem().getType().equals(Material.PAPER) && e.getCurrentItem().hasItemMeta()) {
						String name = e.getCurrentItem().getItemMeta().getDisplayName().replace("&", "§");
						Pet pet = player_pet.get(p);
						pet.renamePet(name, true);
						LobbyPetSQL.addEntry(p.getUniqueId(), pet.getType(), pet.getSize(), name);
						p.sendMessage("§4§lLobby §7» §aDein Haustier heißt nun §6§o" + name);
						e.getClickedInventory().clear();
						p.closeInventory();
						namingpet.remove(p);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if(e.getInventory().getType().equals(InventoryType.ANVIL)) {
			e.getInventory().clear();
			if(namingpet.contains(p)) {
				namingpet.remove(p);
			}
			Gadgets.pay.remove(p);
			ShopAdmin.filter.remove(p);
		}
	}
	
	
	public static void openPetEdit(Player p, Entity pet) {
		if(player_pet.containsKey(p)) {
			Inventory inv = Bukkit.createInventory(null, 27, "§7» §cHaustier bearbeiten");
			
			ItemStack name = new ItemStack(Material.NAME_TAG);
			ItemMeta nm = name.getItemMeta();
			nm.setDisplayName("§7» §cNamen ändern");
			List<String> lore1 = new ArrayList<String>();
			lore1.add("§7» aktueller Name: " + player_pet.get(p).getName());
			nm.setLore(lore1);;
			name.setItemMeta(nm);
			
			ItemStack size = new ItemStack(Material.WHEAT);
			ItemMeta sm = size.getItemMeta();
			sm.setDisplayName("§7» §cGröße verändern");
			size.setItemMeta(sm);
			
			ItemStack remove = new ItemStack(Material.BARRIER);
			ItemMeta rm = remove.getItemMeta();
			rm.setDisplayName("§7» §cHaustier entfernen");
			List<String> lore2 = new ArrayList<String>();
			lore2.add("§7» Diese Funktion löscht dein Haustier komplett.");
			lore2.add("§7» Somit auch deine Einstellungen.");
			rm.setLore(lore2);
			remove.setItemMeta(rm);
			
			ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
			SkullMeta stm = (SkullMeta) stack.getItemMeta();
			stm.setOwner("MHF_ARROWUP");
			stm.setDisplayName("§7» §cStacken");
			List<String> lore3 = new ArrayList<String>();
			lore3.add("§7» Setzt dein Haustier auf");
			lore3.add("§7» deinen Kopf.");
			stm.setLore(lore3);
			stack.setItemMeta(stm);
			
			ItemStack unstack = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
			SkullMeta usm = (SkullMeta) unstack.getItemMeta();
			usm.setOwner("MHF_ARROWDOWN");
			usm.setDisplayName("§7» §cUnstack");
			List<String> lore5 = new ArrayList<String>();
			lore5.add("§7» Setzt dein Haustier");
			lore5.add("§7» vom Kopf ab.");
			usm.setLore(lore5);
			unstack.setItemMeta(usm);
			
			ItemStack ride = new ItemStack(Material.MINECART);
			ItemMeta rim = ride.getItemMeta();
			rim.setDisplayName("§7» §cReiten");
			List<String> lore4 = new ArrayList<String>();
			lore4.add("§7» Setzt dich auf dein Haustier");
			rim.setLore(lore4);
			ride.setItemMeta(rim);
			
				
			inv.setItem(11, name);
			inv.setItem(12, size);
			if(p.getPassenger() != null && p.getPassenger().getType().equals(EntityType.ARMOR_STAND)) {
				inv.setItem(13, unstack);
			} else {
				inv.setItem(13, stack);
			}
			inv.setItem(14, ride);
			inv.setItem(15, remove);
			p.openInventory(inv);
			Bukkit.getScheduler().runTaskLater(main.getPlugin(main.class), new Runnable() {
				
				@Override
				public void run() {
					p.updateInventory();
				}
			}, 2);		
		}
	}
	
	
	public Pet getPet(Entity ent) {
		for(Pet all : player_pet.values()) {
			if(all.ent.equals(ent)) {
				return all;
			}
		}
		return null;
	}
	
	
}
