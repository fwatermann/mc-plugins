package de.finn.CloudSystem.Spigot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CloudSignUpdateStatusEvent extends Event {

  public static HandlerList handlers = new HandlerList();

  private String servername;
  private String status;

  public CloudSignUpdateStatusEvent(String servername, String status) {
    this.servername = servername;
    this.status = status;
  }

  public String getServername() {
    return this.servername;
  }

  public String getStatus() {
    return this.status;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
