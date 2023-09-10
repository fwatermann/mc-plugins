package de.legitfinn.ppermissions.perms;

import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;

import de.legitfinn.ppermissions.main.PPermissions;
import de.legitfinn.ppermissions.mysql.SQL;

public class PermissibleBase extends org.bukkit.permissions.PermissibleBase{

	private final ServerOperator operator;
	
	public PermissibleBase(ServerOperator opable) {
		super(opable);
		operator = opable;
	}
	
	@Override
	public boolean hasPermission(String perm) {
		Player player = (Player)operator;
		String p = perm.toLowerCase();
		if(PPermissions.hasRight(player, p)) {
			return true;
		}
		for(String all : PPermissions.getAllGroupsOfPlayer(player)) {
			if(SQL.hasGroupPerm(all, p)) {
				return true;
			}
		}
		if(p.equals("minecraft.command.gamemode") || p.equals("minecraft.command.tp") || p.equals("minecraft.command.effect") 
				|| p.equals("minecraft.command.give") || p.equals("minecraft.command.deop") || p.equals("minecraft.command.op")) {
			
			if(player.isOp()) {
				return true;
			}
		}
		if(p.equalsIgnoreCase("bukkit.broadcast.user")){
			return true;
		}
		
		
		return false;
	}
}
