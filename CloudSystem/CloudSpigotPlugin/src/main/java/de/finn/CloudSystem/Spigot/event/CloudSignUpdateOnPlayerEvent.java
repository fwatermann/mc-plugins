package de.finn.CloudSystem.Spigot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CloudSignUpdateOnPlayerEvent extends Event {

  public static HandlerList handlers = new HandlerList();

  private String servername;
  private int onplayers;

  public CloudSignUpdateOnPlayerEvent(String servername, int onplayers) {
    this.servername = servername;
    this.onplayers = onplayers;
  }

  public String getServername() {
    return this.servername;
  }

  public int getOnPlayers() {
    return this.onplayers;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
