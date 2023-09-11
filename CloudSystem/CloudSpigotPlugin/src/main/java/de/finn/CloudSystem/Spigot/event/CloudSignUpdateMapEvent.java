package de.finn.CloudSystem.Spigot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CloudSignUpdateMapEvent extends Event {

  public static HandlerList handlers = new HandlerList();

  private String servername;
  private String map;

  public CloudSignUpdateMapEvent(String servername, String map) {
    this.servername = servername;
    this.map = map;
  }

  public String getServername() {
    return this.servername;
  }

  public String getMap() {
    return this.map;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
