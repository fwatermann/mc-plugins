/*
	
	Copyright© PPermissions by LegitFinn

*/

package de.legitfinn.ppermissions.main;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.legitfinn.ppermissions.PlayerData.PlayerData;
import de.legitfinn.ppermissions.commands.COMMAND_pp;
import de.legitfinn.ppermissions.listener.ChatListener;
import de.legitfinn.ppermissions.listener.LoginListener;
import de.legitfinn.ppermissions.methoden.ScoreboardSystem;
import de.legitfinn.ppermissions.mysql.SQL;
import de.legitfinn.ppermissions.perms.PermListener;
import de.legitfinn.ppermissions.perms.PermissibleBase;

public class main extends JavaPlugin {
	
	public static boolean chat = true;
	
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("§c§lCopyright© PPermissons by LeqitFinn");
		Bukkit.getPluginManager().registerEvents(new LoginListener(), this);
		Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
		Bukkit.getPluginManager().registerEvents(new PermListener(), this);
		new SQL();
		PlayerData.connect();
		SQL.createTables();
		ScoreboardSystem.genereateTeams();
		Bukkit.getPluginCommand("ppermissions").setExecutor(new COMMAND_pp());;
		for(String all : SQL.getAllGroups()) {
			SQL.mysql.update("CREATE TABLE IF NOT EXISTS GROUP_" + all.replace("+", "PLUS") + "(ID int primary key auto_increment, PERM varchar(256))");
		}
		for(Player all : Bukkit.getOnlinePlayers()) {
			updatePerms(all);
		}
	}
	
	public void onDisable() {
		PlayerData.mysql.close();
		SQL.mysql.close();
	}
	
	public static void updatePerms(Player p) {
		CraftHumanEntity che = (CraftHumanEntity)p;
		try{
			Field perm = che.getClass().getSuperclass().getDeclaredField("perm");
			perm.setAccessible(true);
			perm.set(che, new PermissibleBase(che));
			perm.setAccessible(false);
		} catch(Exception ex){}
	}
	

}
