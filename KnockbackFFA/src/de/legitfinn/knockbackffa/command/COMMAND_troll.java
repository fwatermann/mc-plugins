package de.legitfinn.knockbackffa.command;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.legitfinn.knockbackffa.methoden.Troll;

public class COMMAND_troll implements CommandExecutor {

	public static String trollPrefix = "§cTroll §7» §3";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			if(sender.hasPermission("knockbackffa.troll")) {
				Player p = (Player)sender;
				
				if(args.length == 0) {
					sendTrollHelp(sender);
					return true;
				} else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("on")) {
						if(!Troll.players.contains(p)) {
							Troll.players.add(p);
							p.sendMessage(trollPrefix + "Trollmodus §aaktiviert§3.");
						} else {
							p.sendMessage(trollPrefix + "§cTrollmodus ist §ebereits aktiviert§c.");
						}
					} else if(args[0].equalsIgnoreCase("off")) {
						if(Troll.players.contains(p)) {
							if(Troll.nodamage.contains(p)) {
								p.performCommand("troll nodamage");
							}
							if(Troll.noknock.contains(p)) {
								p.performCommand("troll noknock");
							}
							if(Troll.vanish.contains(p)) {
								p.performCommand("troll vanish");
							}
							Troll.players.remove(p);
							Troll.resetBlocks(p);
							p.setGameMode(GameMode.ADVENTURE);
							p.sendMessage(trollPrefix + "Trollmodus §cdeaktiviert§3.");
						} else {
							p.sendMessage(trollPrefix + "§cTrollmodus ist §ebereits deaktiviert§c.");
						}
					} else if(args[0].equalsIgnoreCase("vanish")) {
						if(!Troll.players.contains(p)) {
							p.sendMessage(trollPrefix + "§cDu musst zuerst den Trollmodus aktivieren.");
							return true;
						}
						if(!Troll.vanish.contains(p)) {
							Troll.vanish.add(p);
							for(Player all : Bukkit.getOnlinePlayers()) {
								if(!all.equals(p)) {
									all.hidePlayer(p);
								}
							}
							p.sendMessage(trollPrefix + "Vanishmode §aaktiviert§3.");
						} else {
							Troll.vanish.remove(p);
							for(Player all : Bukkit.getOnlinePlayers()) {
								if(!all.equals(p)) {
									all.showPlayer(p);
								}
							}
							p.sendMessage(trollPrefix + "Vanishmode §cdeaktivert§3.");
						}
					} else if(args[0].equalsIgnoreCase("noKnock") || args[0].equalsIgnoreCase("noKnockback")) {
						if(!Troll.players.contains(p)) {
							p.sendMessage(trollPrefix + "§cDu musst zuerst den Trollmodus aktivieren.");
							return true;
						}
						if(!Troll.noknock.contains(p)) {
							Troll.noknock.add(p);
							p.sendMessage(trollPrefix + "NoKnockback §aaktiviert§3.");
						} else {
							Troll.noknock.remove(p);
							p.sendMessage(trollPrefix + "NoKnockback §cdeaktivert§3.");
						}
					} else if(args[0].equalsIgnoreCase("noDmg") || args[0].equalsIgnoreCase("noDamage")) {
						if(!Troll.players.contains(p)) {
							p.sendMessage(trollPrefix + "§cDu musst zuerst den Trollmodus aktivieren.");
							return true;
						}
						if(!Troll.nodamage.contains(p)) {
							Troll.nodamage.add(p);
							p.sendMessage(trollPrefix + "NoDamage §aaktiviert§3.");
						} else {
							Troll.nodamage.remove(p);
							p.sendMessage(trollPrefix + "NoDamage §cdeaktivert§3.");
						}
					} else if(args[0].equalsIgnoreCase("tools")) {
						if(!Troll.players.contains(p)) {
							p.sendMessage(trollPrefix + "§cDu musst zuerst den Trollmodus aktivieren.");
							return true;
						}
						
						ItemStack gran = new ItemStack(Material.FIREBALL);
						ItemMeta gram = gran.getItemMeta();
						gram.setDisplayName("§cTrollGranate");
						gram.addEnchant(Enchantment.THORNS, 1, true);
						gram.addItemFlags(ItemFlag.HIDE_ENCHANTS);
						gran.setItemMeta(gram);
						
						ItemStack shot = new ItemStack(Material.IRON_AXE);
						ItemMeta shotm = shot.getItemMeta();
						shotm.setDisplayName("§cArrowGun");
						shotm.addEnchant(Enchantment.THORNS, 1, true);
						shotm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
						shot.setItemMeta(shotm);
						
						ItemStack tnt = new ItemStack(Material.TNT);
						ItemMeta tntm = tnt.getItemMeta();
						tntm.setDisplayName("§cTNT Werfer");
						tntm.addEnchant(Enchantment.THORNS, 1, true);
						tntm.addItemFlags(ItemFlag.HIDE_ENCHANTS);
						tnt.setItemMeta(tntm);
						
						p.getInventory().addItem(gran);
						p.getInventory().addItem(shot);
						p.getInventory().addItem(tnt);
						p.sendMessage(trollPrefix + "Du hast TrollTools erhalten.");						
					} else if(args[0].equalsIgnoreCase("gm")) {
						if(!Troll.players.contains(p)) {
							p.sendMessage(trollPrefix + "§cDu musst zuerst den Trollmodus aktivieren.");
							return true;
						}
						if(p.getGameMode().equals(GameMode.CREATIVE)) {
							p.setGameMode(GameMode.SURVIVAL);
							p.sendMessage(trollPrefix + "Dein Spielmodus ist nun §eSurvival");
						} else {
							p.setGameMode(GameMode.CREATIVE);
							p.sendMessage(trollPrefix + "Dein Spielmodus ist nun §eKreativ");
						}
					}
				}				
			}			
		} else {
			sender.sendMessage(trollPrefix + "§cDiser Befehl kann nur von einem Spieler mit der Berechtigung ausgeführt werden.");
		}
		
		
		return true;
	}


	public void sendTrollHelp(CommandSender cs) {
		
		cs.sendMessage("§b☰☰☰☰☰☰☰⚌⚌⚌⚊⚊§c TrollSystem §b⚊⚊⚌⚌⚌☰☰☰☰☰☰☰");
		cs.sendMessage("");
		cs.sendMessage("§c/troll <on/off> §7» §cDe-/Aktiviert den Trollmodus");
		cs.sendMessage("§c/troll vanish   §7» §cDe-/Aktiviert den Vanishmode");
		cs.sendMessage("§c/troll gm       §7» §cGamemode Kreativ");
		cs.sendMessage("§c/troll noKnock  §7» §cVermeidung von Rückstoß");
		cs.sendMessage("§c/troll noDamage §7» §cVermeidung von Schaden");
		cs.sendMessage("§c/troll tools    §7» §cErhalten einer Auswahl von TrollItems");
		cs.sendMessage("");
		cs.sendMessage("§b☰☰☰☰☰☰☰⚌⚌⚌⚊⚊§c TrollSystem §b⚊⚊⚌⚌⚌☰☰☰☰☰☰☰");
	}
	
	
	
}
