package de.finn.CloudSystem.Spigot.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CloudForcemapFinishEvent extends Event {

  public static HandlerList handlers = new HandlerList();

  private String servername;
  private String mapname;
  private String gamemode;
  private boolean successfully;
  private String maps;

  public CloudForcemapFinishEvent(
      String servername, String maps, String mapname, String gamemode, boolean successfully) {
    this.servername = servername;
    this.mapname = mapname;
    this.gamemode = gamemode;
    this.successfully = successfully;
    this.maps = maps;
  }

  //	public CloudForcemapFinishEvent(String servername, String maps, String mapname, boolean
  // successfully) {
  //		this.servername = servername;
  //		this.maps = maps;
  //		this.mapname = mapname;
  //		this.gamemode = null;
  //		this.successfully = successfully;
  //	}

  public String getServername() {
    return servername;
  }

  public String getMapname() {
    return mapname;
  }

  public String getGamemode() {
    return gamemode;
  }

  public String getMaps() {
    return this.maps;
  }

  public boolean getSuccessfully() {
    return successfully;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
