package de.legitfinn.ppermissions.main;

import de.legitfinn.ppermissions.mysql.SQL;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PermissionListener implements Listener {

	@EventHandler
	public void onPerm(PermissionCheckEvent e) {
		if(e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer pp = (ProxiedPlayer)e.getSender();
			String perm = e.getPermission();
			for(String all : PPermissions.getAllGroupsOfPlayer(pp.getUniqueId())) {
				if(SQL.hasGroupPerm(all, perm)) {
					e.setHasPermission(true);
					return;
				}
			}
			e.setHasPermission(false);
			return;
		} else {
			e.setHasPermission(true);
			return;
		}
	}
	
	
}
