package de.legitfinn.ppermissions.main;

import de.legitfinn.ppermissions.mysql.SQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

public class main extends Plugin{

	public static Plugin main;
	
	public void onEnable() {
		main = this;
		SQL.connect();
		BungeeCord.getInstance().getPluginManager().registerListener(this, new PermissionListener());
	}
	
	public void onDisable() {
		SQL.mysql.close();
	}
	
}
