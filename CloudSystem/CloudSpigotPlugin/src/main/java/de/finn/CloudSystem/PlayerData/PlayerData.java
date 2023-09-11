package de.finn.CloudSystem.PlayerData;

import de.finn.CloudSystem.Spigot.config.MySQLConfig;
import de.finn.CloudSystem.Spigot.main.Main;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class PlayerData {

  public static MySQL mysql;

  private static HashMap<UUID, String> texture_value_cache = new HashMap<UUID, String>();
  private static HashMap<UUID, String> texture_signature_cache = new HashMap<UUID, String>();
  private static HashMap<UUID, String> uuid_name_cache = new HashMap<UUID, String>();
  private static HashMap<String, UUID> name_uuid_cache = new HashMap<String, UUID>();

  public static void connect() {
    String[] credentials = MySQLConfig.getCredentials("PlayerData");
    mysql =
        new MySQL(
            credentials[0],
            credentials[1],
            credentials[2],
            credentials[3],
            Main.getPlugin(Main.class));
    mysql.update("CREATE TABLE IF NOT EXISTS PlayerData(UUID varchar(128), NAME varchar(128))");
    mysql.update(
        "CREATE TABLE IF NOT EXISTS TEXTURE(UUID varchar(128), VALUE varchar(1024), SIGNATUR varchar(1024))");
  }

  public static boolean isListedUUID(UUID uuid) {
    try {
      ResultSet rs =
          mysql.query("SELECT UUID FROM PlayerData WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        return rs.getString("UUID") != null;
      }
    } catch (SQLException ex) {
    }
    return false;
  }

  public static void updateEntry(UUID uuid, String name) {
    if (!isListedUUID(uuid)) {
      mysql.update(
          "INSERT INTO PlayerData(UUID, NAME) VALUES ('" + uuid.toString() + "','" + name + "')");
    } else {
      mysql.update(
          "UPDATE PlayerData SET NAME = '" + name + "' WHERE UUID = '" + uuid.toString() + "'");
    }
  }

  public static String getName(UUID uuid) {
    if (!uuid_name_cache.containsKey(uuid)) {
      try {
        ResultSet rs =
            mysql.query("SELECT NAME FROM PlayerData WHERE UUID = '" + uuid.toString() + "'");
        if (rs.next()) {
          String name = rs.getString("NAME");
          uuid_name_cache.put(uuid, name);
          return name;
        }
      } catch (SQLException ex) {
      }
    } else {
      return uuid_name_cache.get(uuid);
    }
    return uuid.toString();
  }

  public static UUID getUUID(String name) {
    if (!name_uuid_cache.containsKey(name)) {
      try {
        ResultSet rs = mysql.query("SELECT UUID FROM PlayerData WHERE NAME = '" + name + "'");
        if (rs.next()) {
          UUID uuid = UUID.fromString(rs.getString("UUID"));
          name_uuid_cache.put(name, uuid);
          return uuid;
        }
      } catch (SQLException ex) {
      }
    } else {
      return name_uuid_cache.get(name);
    }
    return null;
  }

  public static boolean isInTextureDatabase(UUID uuid) {
    try {
      ResultSet rs = mysql.query("SELECT UUID FROM TEXTURE WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        return rs.getString("UUID") != null;
      }
    } catch (SQLException ex) {
    }
    return false;
  }

  public static void setSkinTexture(UUID uuid, String value, String signatur) {
    if (!isInTextureDatabase(uuid)) {
      mysql.update(
          "INSERT INTO TEXTURE(UUID, VALUE, SIGNATUR) VALUES ('"
              + uuid.toString()
              + "','"
              + value
              + "','"
              + signatur
              + "')");
    } else {
      mysql.update(
          "UPDATE TEXTURE SET VALUE = '" + value + "' WHERE UUID = '" + uuid.toString() + "'");
      mysql.update(
          "UPDATE TEXTURE SET SIGNATUR = '"
              + signatur
              + "' WHERE UUID = '"
              + uuid.toString()
              + "'");
    }
  }

  public static String getValue(UUID uuid) {

    if (!texture_value_cache.containsKey(uuid)) {
      try {
        ResultSet rs =
            mysql.query("SELECT VALUE FROM TEXTURE WHERE UUID = '" + uuid.toString() + "'");
        if (rs.next()) {
          String value = rs.getString("VALUE");
          texture_value_cache.put(uuid, value);
          return value;
        }
      } catch (SQLException ex) {
      }
    } else {
      return texture_value_cache.get(uuid);
    }
    return null;
  }

  public static String getSignatur(UUID uuid) {
    if (!texture_signature_cache.containsKey(uuid)) {
      try {
        ResultSet rs =
            mysql.query("SELECT SIGNATUR FROM TEXTURE WHERE UUID = '" + uuid.toString() + "'");
        if (rs.next()) {
          String signature = rs.getString("SIGNATURE");
          texture_signature_cache.put(uuid, signature);
          return signature;
        }
      } catch (SQLException ex) {
      }
    }
    return null;
  }

  public static String[] getRandomTexture() {
    ArrayList<String> uuids = new ArrayList<String>();
    try {
      ResultSet rs = mysql.query("SELECT UUID FROM TEXTURE");
      while (rs.next()) {
        uuids.add(rs.getString("UUID"));
      }
    } catch (SQLException ex) {
    }
    Random rnd = new Random();
    UUID uuid = UUID.fromString(uuids.get(rnd.nextInt(uuids.size())));
    String signatur = getSignatur(uuid);
    String value = getValue(uuid);
    return new String[] {value, signatur};
  }

  public static String[] getTexture(UUID uuid) {
    String signatur = getSignatur(uuid);
    String value = getValue(uuid);
    return new String[] {value, signatur};
  }

  public static ArrayList<String> getAllNames() {
    ArrayList<String> ret = new ArrayList<String>();
    try {
      ResultSet rs = mysql.query("SELECT NAME FROM PlayerData");
      while (rs.next()) {
        ret.add(rs.getString("NAME"));
      }
    } catch (SQLException ex) {
    }
    return ret;
  }
}
