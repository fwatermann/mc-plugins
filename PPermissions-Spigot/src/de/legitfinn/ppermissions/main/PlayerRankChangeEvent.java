package de.legitfinn.ppermissions.main;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerRankChangeEvent extends Event{

	public static HandlerList handlers = new HandlerList();
	UUID uniqueid;
	
	public PlayerRankChangeEvent(UUID uuid) {
		uniqueid = uuid;
	}
	
	public UUID getPlayer() {
		return this.uniqueid;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
