package de.finn.CloudSystem.Friends;

import de.finn.CloudSystem.PlayerData.PlayerData;
import de.finn.CloudSystem.Spigot.config.MySQLConfig;
import de.finn.CloudSystem.Spigot.main.Main;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class FriendSQL {

  public static MySQL mysql;

  public static void connect() {
    String[] credentials = MySQLConfig.getCredentials("Freunde");
    mysql =
        new MySQL(
            credentials[0],
            credentials[1],
            credentials[2],
            credentials[3],
            Main.getPlugin(Main.class));
    mysql.update(
        "CREATE TABLE IF NOT EXISTS SETTINGS(ID int AUTO_INCREMENT primary key, UUID varchar(128), REQUEST int, PARTY int, PRIVATEMSG int, TELEPORT int, CLAN int, NOTIFY int)");
    mysql.update(
        "CREATE TABLE IF NOT EXISTS DATA(ID int AUTO_INCREMENT primary key, UUID varchar(128), ISONLINE int, LASTONLINE varchar(128), ONLINEON varchar(128))");
  }

  public static boolean isInSettings(UUID uuid) {
    try {
      ResultSet rs =
          mysql.query("SELECT UUID FROM SETTINGS WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        return rs.getString("UUID") != null;
      }
    } catch (SQLException ex) {
    }
    return false;
  }

  public static boolean isInData(UUID uuid) {
    try {
      ResultSet rs = mysql.query("SELECT UUID FROM DATA WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        return rs.getString("UUID") != null;
      }
    } catch (SQLException ex) {
    }
    return false;
  }

  public static void addPlayerToSettings(
      UUID uuid, int request, int party, int msg, int teleport, int clan, int notify) {
    mysql.update(
        "INSERT INTO SETTINGS (UUID, REQUEST, PARTY, PRIVATEMSG, TELEPORT, CLAN, NOTIFY) VALUES ('"
            + uuid.toString()
            + "','"
            + request
            + "','"
            + party
            + "','"
            + msg
            + "','"
            + teleport
            + "','"
            + clan
            + "','"
            + notify
            + "')");
  }

  public static void addPlayerToData(UUID uuid, int isonline, String lastOnline, String onlineOn) {
    mysql.update(
        "INSERT INTO DATA (UUID, ISONLINE, LASTONLINE, ONLINEON) VALUES ('"
            + uuid.toString()
            + "','"
            + isonline
            + "','"
            + lastOnline
            + "','"
            + onlineOn
            + "')");
  }

  public static void setSettings(UUID uuid, String setting, int set) {
    if (!isInSettings(uuid)) {
      addPlayerToSettings(uuid, 1, 1, 1, 1, 1, 1);
    }
    mysql.update(
        "UPDATE SETTINGS SET "
            + setting
            + " = '"
            + set
            + "' WHERE UUID = '"
            + uuid.toString()
            + "'");
  }

  public static boolean isOnline(UUID uuid) {
    if (!isInData(uuid)) {
      addPlayerToData(
          uuid, 1, new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date()), "Lobby");
    }
    try {
      ResultSet rs =
          mysql.query("SELECT ISONLINE FROM DATA WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        return rs.getInt("ISONLINE") == 1;
      }
    } catch (SQLException ex) {
    }
    return false;
  }

  public static boolean getSetting(UUID uuid, String setting) {
    if (!isInSettings(uuid)) {
      addPlayerToSettings(uuid, 1, 1, 1, 1, 1, 1);
    }
    try {
      ResultSet rs =
          mysql.query(
              "SELECT " + setting + " FROM SETTINGS WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        if (rs.getInt(setting) == 1) {
          return true;
        } else {
          return false;
        }
      }
    } catch (SQLException ex) {
    }
    return false;
  }

  public static ArrayList<String> getFriends(UUID uuid) {
    ArrayList<String> ret = new ArrayList<String>();
    createTable(uuid);
    String table = "friends_" + uuid.toString().replace("-", "");
    try {
      ResultSet rs = mysql.query("SELECT * FROM " + table + " WHERE STATE = 'friend'");
      while (rs.next()) {
        ret.add(PlayerData.getName(UUID.fromString(rs.getString("UUID"))));
      }
    } catch (SQLException ex) {
    }
    return ret;
  }

  public static void createTable(UUID uuid) {
    String table = "friends_" + uuid.toString().replace("-", "");
    mysql.update(
        "CREATE TABLE IF NOT EXISTS "
            + table
            + "(ID int AUTO_INCREMENT primary key, UUID varchar(128), STATE varchar(128))");
  }

  public static void removeFriend(UUID uuid1, UUID uuid2) {
    String table1 = "friends_" + uuid1.toString().replace("-", "");
    String table2 = "friends_" + uuid2.toString().replace("-", "");
    createTable(uuid1);
    createTable(uuid2);
    mysql.update("DELETE FROM " + table1 + " WHERE UUID = '" + uuid2.toString() + "'");
    mysql.update("DELETE FROM " + table2 + " WHERE UUID = '" + uuid1.toString() + "'");
  }

  public static boolean hasRequestOf(UUID uuid1, UUID uuid2) {
    String table1 = "friends_" + uuid1.toString().replace("-", "");
    createTable(uuid1);
    createTable(uuid2);
    try {
      ResultSet rs =
          mysql.query(
              "SELECT UUID FROM "
                  + table1
                  + " WHERE UUID = '"
                  + uuid2.toString()
                  + "' AND STATE = 'request'");
      if (rs.next()) {
        return rs.getString("UUID") != null;
      }
    } catch (SQLException ex) {
    }
    return false;
  }

  public static ArrayList<String> getRequests(UUID uuid) {
    ArrayList<String> ret = new ArrayList<String>();
    String table1 = "friends_" + uuid.toString().replace("-", "");
    createTable(uuid);
    try {
      ResultSet rs = mysql.query("SELECT UUID FROM " + table1 + " WHERE STATE = 'request'");
      while (rs.next()) {
        ret.add(PlayerData.getName(UUID.fromString(rs.getString("UUID"))));
      }
    } catch (SQLException ex) {
    }
    return ret;
  }

  public static void addRequest(UUID uuid1, UUID uuid2) {
    String table2 = "friends_" + uuid2.toString().replace("-", "");
    createTable(uuid1);
    createTable(uuid2);
    mysql.update(
        "INSERT INTO " + table2 + "(UUID, STATE) VALUES ('" + uuid1.toString() + "','request')");
  }

  public static void turnToFriend(UUID uuid1, UUID uuid2) {
    String table1 = "friends_" + uuid1.toString().replace("-", "");
    String table2 = "friends_" + uuid2.toString().replace("-", "");
    createTable(uuid1);
    createTable(uuid2);
    mysql.update(
        "UPDATE " + table2 + " SET STATE = 'friend' WHERE UUID = '" + uuid1.toString() + "'");
    mysql.update(
        "INSERT INTO " + table1 + "(UUID, STATE) VALUES ('" + uuid2.toString() + "','friend')");
  }

  public static int getOnlineFriendCount(UUID uuid) {
    ArrayList<String> a = new ArrayList<String>();
    for (String all : getFriends(uuid)) {
      if (isOnline(PlayerData.getUUID(all))) {
        a.add(all);
      }
    }
    return a.size();
  }

  public static String getLastOnline(UUID uuid) {
    try {
      ResultSet rs =
          mysql.query("SELECT LASTONLINE FROM DATA WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        return rs.getString("LASTONLINE");
      }
    } catch (SQLException ex) {
    }
    return null;
  }

  public static String getOnlineOn(UUID uuid) {
    try {
      ResultSet rs =
          mysql.query("SELECT ONLINEON FROM DATA WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        return rs.getString("ONLINEON");
      }
    } catch (SQLException ex) {
    }
    return null;
  }
}
