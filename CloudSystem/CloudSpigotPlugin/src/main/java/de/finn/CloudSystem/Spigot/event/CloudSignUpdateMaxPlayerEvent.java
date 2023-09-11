package de.finn.CloudSystem.Spigot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CloudSignUpdateMaxPlayerEvent extends Event {

  public static HandlerList handlers = new HandlerList();

  private String servername;
  private int maxPlayers;

  public CloudSignUpdateMaxPlayerEvent(String servername, int maxPlayers) {
    this.servername = servername;
    this.maxPlayers = maxPlayers;
  }

  public String getServername() {
    return this.servername;
  }

  public int getMaxPlayers() {
    return this.maxPlayers;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
