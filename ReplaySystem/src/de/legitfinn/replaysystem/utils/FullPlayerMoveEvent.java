package de.legitfinn.replaysystem.utils;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FullPlayerMoveEvent extends Event implements Cancellable{
	public static HandlerList handlers = new HandlerList();
	Player player;
	boolean canceled = false;

	public FullPlayerMoveEvent(Player p) {
		this.player = p;
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return canceled;
	}

	@Override
	public void setCancelled(boolean arg0) {
		this.canceled = arg0;
	}
}
