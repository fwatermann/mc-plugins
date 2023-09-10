package de.legitfinn.lobby.functions;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import de.legitfinn.lobby.SQL.LobbySettingsSQL;
import de.legitfinn.lobby.commands.COMMAND_flight;
import de.legitfinn.lobby.main.main;
import net.minecraft.server.v1_8_R3.EnumParticle;
public class DoubleJump implements Listener {
	
	public static ArrayList<Player> inJump = new ArrayList<Player>();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(LobbySettingsSQL.getSettingsBoolean(e.getPlayer().getUniqueId(), "DOUBLEJUMP")) {
			e.getPlayer().setAllowFlight(true);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.getPlayer().setAllowFlight(false);
	}
	
	@EventHandler
	public void onJump(PlayerToggleFlightEvent e) {
		if(!COMMAND_flight.flight.contains(e.getPlayer())) {
			if(main.doubleJump.contains(e.getPlayer())) {
				e.setCancelled(true);
				e.getPlayer().setAllowFlight(false);
				e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(2.0).normalize().setY(0.75));
				inJump.add(e.getPlayer());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(e.getPlayer().isOnGround() && inJump.contains(e.getPlayer())) {
			if(main.doubleJump.contains(e.getPlayer())) {
				e.getPlayer().setAllowFlight(true);
			}
			inJump.remove(e.getPlayer());
		} else if(inJump.contains(e.getPlayer())) {
			main.playParicle((ArrayList<Player>)e.getPlayer().getLocation().getWorld().getPlayers(), EnumParticle.FIREWORKS_SPARK, e.getPlayer().getLocation().clone().subtract(0, 0.25, 0), 0.0F, 0.0F, 0.0F, 0.01F, 10);
		}
	}
	
	
	
}
