package de.finn.CloudSystem.Spigot.main;

public class CloudAPI {

  public static String STATUS_WAITING = "WAITING";
  public static String STATUS_INGAME = "INGAME";
  public static String STATUS_ENDING = "ENDING";
  public static String STATIS_OFFLINE = "OFFLINE";

  public static void updateMaxPlayers(int maxPlayers) {
    ConnectionManager.getInstance().send("updateMaxPlayers;" + maxPlayers);
  }

  public static void updateOnPlayers(int onPlayers) {
    ConnectionManager.getInstance().send("updateOnPlayers;" + onPlayers);
  }

  public static void updateStatus(String status) {
    ConnectionManager.getInstance().send("updateStatus;" + status);
  }

  public static void updateMap(String map) {
    ConnectionManager.getInstance().send("updateMap;" + map);
  }

  public static void forceMap(String map) {
    ConnectionManager.getInstance().send("forceMap;" + map);
  }

  public static void sendInfos(String servername, String gamemode, String map) {
    ConnectionManager.getInstance().send("info;" + servername + ";" + gamemode + ";" + map);
  }

  public static String getMasterIP() {
    return ConnectionManager.getInstance().getMasterIP();
  }
}
