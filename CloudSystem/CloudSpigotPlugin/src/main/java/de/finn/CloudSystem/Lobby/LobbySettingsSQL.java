package de.finn.CloudSystem.Lobby;

import de.finn.CloudSystem.Spigot.config.MySQLConfig;
import de.finn.CloudSystem.Spigot.main.Main;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LobbySettingsSQL {

  public static MySQL mysql;

  public static void connect() {
    String[] credentials = MySQLConfig.getCredentials("LobbySystem");
    mysql =
        new MySQL(
            credentials[0],
            credentials[1],
            credentials[2],
            credentials[3],
            Main.getPlugin(Main.class));
  }

  public static boolean isListed(UUID uuid) {
    try {
      ResultSet rs = mysql.query("SELECT * FROM SETTINGS WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        return rs.getString("UUID") != null;
      }
    } catch (SQLException ex) {
    }
    return false;
  }

  public static void addToList(
      UUID uuid, int jump, int scboard, int visible, int pets, int particles, int chat) {
    mysql.update("DELETE FROM SETTINGS WHERE UUID = '" + uuid.toString() + "'");
    mysql.update(
        "INSERT INTO SETTINGS (UUID, DOUBLEJUMP, SCOREBOARD, VISIBLE, PETS, PARTICLES, CHAT, NICK) VALUES ('"
            + uuid.toString()
            + "','"
            + jump
            + "','"
            + scboard
            + "','"
            + visible
            + "','"
            + pets
            + "','"
            + particles
            + "','"
            + chat
            + "', '0')");
  }

  public static boolean getSettingsBoolean(UUID uuid, String setting) {
    if (!isListed(uuid)) {
      addToList(uuid, 1, 1, 2, 1, 1, 1);
    }
    int ret = 0;
    try {
      ResultSet rs = mysql.query("SELECT * FROM SETTINGS WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        ret = rs.getInt(setting);
      }
    } catch (SQLException ex) {
    }
    return ret == 1;
  }

  public static int getSettingsInt(UUID uuid, String setting) {
    if (!isListed(uuid)) {
      addToList(uuid, 1, 1, 2, 1, 1, 1);
    }
    int ret = 0;
    try {
      ResultSet rs = mysql.query("SELECT * FROM SETTINGS WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        ret = rs.getInt(setting);
      }
    } catch (SQLException ex) {
    }
    return ret;
  }

  public static void setSettings(UUID uuid, String setting, int value) {
    if (!isListed(uuid)) {
      addToList(uuid, 1, 1, 2, 1, 1, 1);
    }
    mysql.update(
        "UPDATE SETTINGS SET "
            + setting
            + " = '"
            + value
            + "' WHERE UUID = '"
            + uuid.toString()
            + "'");
  }

  public static class DSGVO {

    private static boolean isListed(UUID uuid) {
      try {
        ResultSet rs = mysql.query("SELECT UUID FROM DSGVO WHERE UUID = '" + uuid.toString() + "'");
        if (rs.next()) {
          return rs.getString("UUID").equalsIgnoreCase(uuid.toString());
        }
      } catch (SQLException ex) {
      }
      return false;
    }

    public static boolean hasAccepted(UUID uuid) {
      if (!isListed(uuid)) return false;
      try {
        ResultSet rs =
            mysql.query("SELECT ACCEPTED FROM DSGVO WHERE UUID = '" + uuid.toString() + "'");
        if (rs.next()) {
          return rs.getInt("ACCEPTED") == 1;
        }
      } catch (SQLException ex) {
      }
      return false;
    }

    public static void setAccepted(UUID uuid, boolean accepted) {
      if (!isListed(uuid)) {
        mysql.update(
            "INSERT INTO DSGVO (UUID, ACCEPTED) VALUES ('"
                + uuid.toString()
                + "','"
                + (accepted ? 1 : 0)
                + "')");
      } else {
        mysql.update(
            "UPDATE DSGVO SET ACCEPTED = '"
                + (accepted ? 1 : 0)
                + "' WHERE UUID = '"
                + uuid.toString()
                + "'");
      }
    }
  }
}
