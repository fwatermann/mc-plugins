package de.legitfinn.ppermissions.commands;

import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.legitfinn.ppermissions.PlayerData.PlayerData;
import de.legitfinn.ppermissions.main.PPermissions;
import de.legitfinn.ppermissions.main.PlayerRankChangeEvent;
import de.legitfinn.ppermissions.methoden.ScoreboardSystem;
import de.legitfinn.ppermissions.mysql.SQL;

//Plugin by LeqitFinn

public class COMMAND_pp implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof ConsoleCommandSender
				|| PPermissions.canEditPermissions(SQL.getGroupListOfPlayer(((Player) sender).getUniqueId()))) {
			if (args.length == 0 || args.length == 1) {
				sender.sendMessage("§cPPerms §7» §6/pp setrank <name> <rank> §7» §eSetzt den Rang eines Spielers.");
				sender.sendMessage(
						"§cPPerms §7» §6/pp addrank <name> <rank> [time] §7» §eTimeformat: 10y / 10mo / 10d / 10h / 10min / 10sec");
				sender.sendMessage(
						"§cPPerms §7» §6/pp removerank <name> <rank> §7» §eFügt einem Spieler einen Rang hinzu.");
				sender.sendMessage("§cPPerms §7» §6/pp user <name> §7» §eZeigt informationen von einem Spieler an.");
				sender.sendMessage("§cPPerms §7» §6/pp group <group> <addPerm/removePerm> <perm> §7» §eFügt oder löscht eine Permission einer Gruppe");
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("user")) {
					UUID uuid = PlayerData.getUUID(args[1]);
					if (uuid == null) {
						sender.sendMessage("§cPPerms §7» §4Dieser Spieler war noch nicht auf dem Server!");
						return true;
					}
					String name = PlayerData.getName(uuid);
					String msg = "§cPPerms §7» Ränge von " + name + ": ";
					for (String all : SQL.getGroupListOfPlayer(uuid)) {
						msg += SQL.getGroupColor(SQL.getGroupInformation(all, "name"))
								+ SQL.getGroupInformation(all, "name") + "§7, ";
					}
					msg = msg.substring(0, msg.length() - 2);
					sender.sendMessage(msg);
				} else {
					sender.sendMessage("§cPPerms §7» §6/pp setrank <name> <rank> §7» §eSetzt den Rang eines Spielers.");
					sender.sendMessage(
							"§cPPerms §7» §6/pp addrank <name> <rank> [time] §7» §eTimeformat: 10y / 10mo / 10d / 10h / 10min / 10sec");
					sender.sendMessage(
							"§cPPerms §7» §6/pp removerank <name> <rank> §7» §eFügt einem Spieler einen Rang hinzu.");
					sender.sendMessage(
							"§cPPerms §7» §6/pp user <name> §7» §eZeigt informationen von einem Spieler an.");
					sender.sendMessage("§cPPerms §7» §6/pp group <group> <addPerm/removePerm> <perm> §7» §eFügt oder löscht eine Permission einer Gruppe");
				}
			}
			if (args.length == 3) {
				if (args[0].equalsIgnoreCase("setrank")) {
					UUID uuid = PlayerData.getUUID(args[1]);
					if (uuid == null) {
						sender.sendMessage("§cPPerms §7» §4Dieser Spieler war noch nicht auf dem Server!");
						return true;
					}
					String group = args[2];
					String name = PlayerData.getName(uuid);
					if (!SQL.groupExists(group)) {
						sender.sendMessage("§cPPerms §7» §4Dieser Rank/Gruppe existiert nicht!");
						return true;
					}
					SQL.removeFromAllGroups(uuid);
					SQL.addToGroup(uuid, group);
					Player tp = Bukkit.getPlayer(uuid);
					if (tp != null && tp.isOnline()) {
						ScoreboardSystem.addToTeam(tp);
					}
					Bukkit.getPluginManager().callEvent(new PlayerRankChangeEvent(uuid));
					sender.sendMessage("§cPPerms §7» §a" + name + " hat nun den Rang §7\"§6" + group + "§7\"§a.");
				} else if (args[0].equalsIgnoreCase("addrank")) {
					UUID uuid = PlayerData.getUUID(args[1]);
					if (uuid == null) {
						sender.sendMessage("§cPPerms §7» §4Dieser Spieler war noch nicht auf dem Server!");
						return true;
					}
					String name = PlayerData.getName(uuid);
					String group = args[2];
					if (!SQL.groupExists(group)) {
						sender.sendMessage("§cPPerms §7» §4Dieser Rank/Gruppe existiert nicht!");
						return true;
					}
					if (PPermissions.isInGroup(uuid, group)) {
						SQL.removeFromGroup(uuid, group);
					}
					SQL.addToGroup(uuid, group);
					Player tp = Bukkit.getPlayer(uuid);
					if (tp != null && tp.isOnline()) {
						ScoreboardSystem.addToTeam(tp);
					}
					Bukkit.getPluginManager().callEvent(new PlayerRankChangeEvent(uuid));
					sender.sendMessage("§cPPerms §7» §a" + name + " wurde zu §7\"§6" + group + "§7\"§a hinzugefügt.");
				} else if (args[0].equalsIgnoreCase("removerank")) {
					UUID uuid = PlayerData.getUUID(args[1]);
					if (uuid == null) {
						sender.sendMessage("§cPPerms §7» §4Dieser Spieler war noch nicht auf dem Server!");
						return true;
					}
					String name = PlayerData.getName(uuid);
					String group = args[2];
					if (!SQL.groupExists(group)) {
						sender.sendMessage("§cPPerms §7» §4Dieser Rank/Gruppe existiert nicht!");
						return true;
					}
					if (!PPermissions.isInGroup(uuid, group)) {
						sender.sendMessage("§cPPerms §7» §4" + name + " ist nicht in dieser Gruppe!");
						return true;
					}
					SQL.removeFromGroup(uuid, group);
					Bukkit.getPluginManager().callEvent(new PlayerRankChangeEvent(uuid));
					sender.sendMessage("§cPPerms §7» §c" + name + " wurde aus §7\"§6" + group + "§7\"§c entfernt.");
					if (SQL.getGroupListOfPlayer(uuid)[0].isEmpty()) {
						SQL.addToGroup(uuid, "Spieler");
					}
					Player tp = Bukkit.getPlayer(uuid);
					if (tp != null && tp.isOnline()) {
						ScoreboardSystem.addToTeam(tp);
					}
				} else {
					sender.sendMessage("§cPPerms §7» §6/pp setrank <name> <rank> §7» §eSetzt den Rang eines Spielers.");
					sender.sendMessage(
							"§cPPerms §7» §6/pp addrank <name> <rank> [time] §7» §eTimeformat: 10y / 10mo / 10d / 10h / 10min / 10sec");
					sender.sendMessage(
							"§cPPerms §7» §6/pp removerank <name> <rank> §7» §eFügt einem Spieler einen Rang hinzu.");
					sender.sendMessage(
							"§cPPerms §7» §6/pp user <name> §7» §eZeigt informationen von einem Spieler an.");
					sender.sendMessage("§cPPerms §7» §6/pp group <group> <addPerm/removePerm> <perm> §7» §eFügt oder löscht eine Permission einer Gruppe");
				}
			} else if (args.length == 4) {
				if (args[0].equalsIgnoreCase("addrank")) {
					UUID uuid = PlayerData.getUUID(args[1]);
					if (uuid == null) {
						sender.sendMessage("§cPPerms §7» §4Dieser Spieler war noch nicht auf dem Server!");
						return true;
					}
					String name = PlayerData.getName(uuid);
					String group = args[2];
					Date d = getExpireDate(args[3]);
					if (d != null) {
						if (!SQL.groupExists(group)) {
							sender.sendMessage("§cPPerms §7» §4Dieser Rank/Gruppe existiert nicht!");
							return true;
						}
						if (PPermissions.isInGroup(uuid, group)) {
							SQL.removeFromGroup(uuid, group);
						}
						SQL.addToGroup(uuid, group, d);
						Player tp = Bukkit.getPlayer(uuid);
						if (tp != null && tp.isOnline()) {
							ScoreboardSystem.addToTeam(tp);
						}
						Bukkit.getPluginManager().callEvent(new PlayerRankChangeEvent(uuid));
						sender.sendMessage(
								"§cPPerms §7» §a" + name + " wurde zu §7\"§6" + group + "§7\"§a hinzugefügt.");
					} else {
						sender.sendMessage("§cPPerms §7» §4Ungültiges Timeformat!");
						sender.sendMessage("§cPPerms §7» §eTimeformat: 10y / 10mo / 10d / 10h / 10min / 10sec");
					}
				} else if (args[0].equalsIgnoreCase("group")) {
					String group = args[1];
					String func = args[2];
					String perm = args[3];
					if (SQL.groupExists(group)) {
						if (func.equalsIgnoreCase("add") || func.equalsIgnoreCase("addPerm")) {
							SQL.mysql.update("DELETE FROM GROUP_" + group.replace("+", "PLUS") + " WHERE PERM = '"
									+ perm.toLowerCase() + "'");
							SQL.mysql.update("INSERT INTO GROUP_" + group.replace("+", "PLUS") + "(PERM) VALUES ('"
									+ perm.toLowerCase() + "')");
							sender.sendMessage("§cPPerms §7» §aBerechtigung §6§o\"" + perm + "\" §a zu §6§o\"" + group
									+ "\" §ahinzugefügt.");
						} else if (func.equalsIgnoreCase("removePerm") || func.equalsIgnoreCase("remove")
								|| func.equalsIgnoreCase("rm") || func.equalsIgnoreCase("delete")
								|| func.equalsIgnoreCase("del")) {
							SQL.mysql.update("DELETE FROM GROUP_" + group.replace("+", "PLUS") + " WHERE PERM = '"
									+ perm.toLowerCase() + "'");
							sender.sendMessage("§cPPerms §7» §cBerechtigung §6§o\"" + perm + "\" §cvon §6§o\"" + group
									+ "\" §centfernt.");
						} else {
							sender.sendMessage("§cPPerms §7» §6/pp group <group> <addPerm/removePerm> <perm> §7» §eFügt oder löscht eine Permission einer Gruppe");
						}
					} else {
						sender.sendMessage("§cPPerms §7» §4Dieser Rank/Gruppe existiert nicht!");
					}
				} else {
					sender.sendMessage("§cPPerms §7» §6/pp setrank <name> <rank> §7» §eSetzt den Rang eines Spielers.");
					sender.sendMessage(
							"§cPPerms §7» §6/pp addrank <name> <rank> [time] §7» §eTimeformat: 10y / 10mo / 10d / 10h / 10min / 10sec");
					sender.sendMessage(
							"§cPPerms §7» §6/pp removerank <name> <rank> §7» §eFügt einem Spieler einen Rang hinzu.");
					sender.sendMessage(
							"§cPPerms §7» §6/pp user <name> §7» §eZeigt informationen von einem Spieler an.");
					sender.sendMessage("§cPPerms §7» §6/pp group <group> <addPerm/removePerm> <perm> §7» §eFügt oder löscht eine Permission einer Gruppe");
				}
			}
		} else {
			sender.sendMessage("§cPPerms §7» §4Du hast keine Berechtigung um diesen Befehl auszuführen!");
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	private Date getExpireDate(String a) {
		if (a.endsWith("y")) {
			int b = Integer.parseInt(a.replace("y", ""));
			Date c = new Date();
			c.setYear(c.getYear() + b);
			return c;
		} else if (a.endsWith("mo")) {
			int b = Integer.parseInt(a.replace("mo", ""));
			Date c = new Date();
			c.setMonth(c.getMonth() + b);
			return c;
		} else if (a.endsWith("d")) {
			int b = Integer.parseInt(a.replace("d", ""));
			Date c = new Date();
			c.setDate(c.getDate() + b);
			return c;
		} else if (a.endsWith("h")) {
			int b = Integer.parseInt(a.replace("h", ""));
			Date c = new Date();
			c.setHours(c.getHours() + b);
			return c;
		} else if (a.endsWith("min")) {
			int b = Integer.parseInt(a.replace("min", ""));
			Date c = new Date();
			c.setMinutes(c.getMinutes() + b);
			return c;
		} else if (a.endsWith("sec")) {
			int b = Integer.parseInt(a.replace("sec", ""));
			Date c = new Date();
			c.setSeconds(c.getSeconds() + b);
			return c;
		} else {
			return null;
		}
	}

}
