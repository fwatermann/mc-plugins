package de.finn.CloudSystem.Statistics;

import de.finn.CloudSystem.Spigot.config.MySQLConfig;
import de.finn.CloudSystem.Spigot.main.Main;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class StatisticSQL {

  public static MySQL mysql;

  public static void connect() {
    String[] credentials = MySQLConfig.getCredentials("PlayerStatistics");
    mysql =
        new MySQL(
            credentials[0],
            credentials[1],
            credentials[2],
            credentials[3],
            Main.getPlugin(Main.class));
    mysql.update(
        "CREATE TABLE IF NOT EXISTS BEDWARS(ID int primary key auto_increment, UUID varchar(128), KILLS int Default '0', DEATH int Default '0', WINS int Default '0', GAMES int Default '0', BEDS int Default '0')");
    mysql.update(
        "CREATE TABLE IF NOT EXISTS SKYWARS(ID int primary key auto_increment, UUID varchar(128), KILLS int Default '0', DEATH int Default '0', WINS int Default '0', GAMES int Default '0')");
    mysql.update(
        "CREATE TABLE IF NOT EXISTS KNOCKIT(ID int primary key auto_increment, UUID varchar(128), KILLS int Default '0', DEATH int Default '0', MAXSTREAK int Default '0')");
    mysql.update(
        "CREATE TABLE IF NOT EXISTS KNOCKITKITS(ID int primary key auto_increment, UUID varchar(128), KIT int, INVENTORY varchar(2048))");
    mysql.update(
        "CREATE TABLE IF NOT EXISTS ONELINE(ID int primary key auto_increment, UUID varchar(128), KILLS int Default '0', DEATH int Default '0', WINS int Default '0', GAMES int Default '0')");
    mysql.update(
        "CREATE TABLE IF NOT EXISTS DARKHUNT(ID int primary key auto_increment, UUID varchar(128), KILLS int Default '0', DEATH int Default '0', WINS int Default '0', GAMES int Default '0', HUNTERPASS int Default '0', MUSICSHOP varchar(512) Default '1;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0;0', MUSIC int Default '1')");
    mysql.update(
        "CREATE TABLE IF NOT EXISTS TRAINING_KITS(ID int primary key auto_increment, UUID varchar(128), INVENTORY varchar(2048), ARMOR varchar(2048))");
  }

  public static int getInt(String gamemode, String part, UUID uuid) {
    if (!isListed(gamemode, uuid)) {
      createEntry(gamemode, uuid);
    }
    try {
      ResultSet rs =
          mysql.query("SELECT * FROM " + gamemode + " WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        return rs.getInt(part);
      }
    } catch (SQLException ex) {
    }
    return -1;
  }

  public static String getString(String gamemode, String part, UUID uuid) {
    if (!isListed(gamemode, uuid)) {
      createEntry(gamemode, uuid);
    }
    try {
      ResultSet rs =
          mysql.query("SELECT * FROM " + gamemode + " WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        return rs.getString(part);
      }
    } catch (SQLException ex) {
    }
    return null;
  }

  public static void createEntry(String gamemode, UUID uuid) {
    mysql.update("INSERT INTO " + gamemode + "(UUID) VALUES ('" + uuid.toString() + "')");
  }

  public static boolean isListed(String gamemode, UUID uuid) {
    try {
      ResultSet rs =
          mysql.query("SELECT * FROM " + gamemode + " WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        return rs.getString("UUID") != null;
      }
    } catch (SQLException ex) {
    }
    return false;
  }

  public static void setInt(String gamemode, String part, int amount, UUID uuid) {
    if (!isListed(gamemode, uuid)) {
      createEntry(gamemode, uuid);
    }
    mysql.update(
        "UPDATE "
            + gamemode
            + " SET "
            + part
            + " = '"
            + amount
            + "' WHERE UUID = '"
            + uuid.toString()
            + "'");
  }

  public static void setString(String gamemode, String part, String value, UUID uuid) {
    if (!isListed(gamemode, uuid)) {
      createEntry(gamemode, uuid);
    }
    mysql.update(
        "UPDATE "
            + gamemode
            + " SET "
            + part
            + " = '"
            + value
            + "' WHERE UUID = '"
            + uuid.toString()
            + "'");
  }

  public static void removeInt(String gamemode, String part, int amount, UUID uuid) {
    int a = getInt(gamemode, part, uuid);
    int b = amount;
    int c = a - b;
    setInt(gamemode, part, c, uuid);
  }

  public static void addInt(String gamemode, String part, int amount, UUID uuid) {
    int a = getInt(gamemode, part, uuid);
    int b = amount;
    int c = a + b;
    setInt(gamemode, part, c, uuid);
  }

  public static Integer getPlace(String gamemode, String part, UUID uuid) {
    int i = -1;

    try {
      ResultSet rs = mysql.query("SELECT * FROM " + gamemode + " ORDER BY " + part + " DESC");
      while (rs.next()) {
        if (i == -1) {
          i = 1;
        } else {
          i++;
        }
        UUID u = UUID.fromString(rs.getString("UUID"));
        if (u.toString().equals(uuid.toString())) {
          return i;
        }
      }
    } catch (SQLException ex) {
    }

    return i;
  }

  public static HashMap<Integer, UUID> getPlaces(int highest, String part, String gamemode) {

    HashMap<Integer, UUID> ret = new HashMap<Integer, UUID>();

    try {
      ResultSet rs =
          mysql.query("SELECT * FROM " + gamemode + " ORDER BY " + part + " DESC LIMIT " + highest);
      int p = 0;
      while (rs.next()) {
        p++;
        ret.put(p, UUID.fromString(rs.getString("UUID")));
      }
    } catch (SQLException ex) {
    }

    return ret;
  }

  public static UUID getUUIDOfPlace(String gamemode, String part, int place) {
    try {
      ResultSet rs =
          mysql.query(
              "SELECT * FROM " + gamemode + " ORDER BY " + part + " DESC LIMIT " + (place) + ", 1");
      if (rs.next()) {
        return UUID.fromString(rs.getString("UUID"));
      }
    } catch (SQLException ex) {
    }

    return null;
  }

  public static Integer getExistingPlayers(String gamemode) {
    try {
      ResultSet rs = mysql.query("SELECT COUNT(*) AS Players FROM " + gamemode);
      if (rs.next()) {
        return rs.getInt("Players");
      }
    } catch (SQLException ex) {
    }
    return -1;
  }

  /*
   *
   *  KNOCKIT
   *
   */

  public static class KnockIt {

    public static boolean hasKitsaved(int kit, UUID uuid) {
      try {
        ResultSet rs =
            mysql.query(
                "SELECT * FROM KNOCKITKITS WHERE UUID = '"
                    + uuid.toString()
                    + "' AND KIT = "
                    + kit);
        return rs.next();
      } catch (SQLException ex) {
      }
      return false;
    }

    public static boolean saveKit(int kit, UUID uuid, Inventory inv) {

      String invs = null;
      try {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.writeInt(inv.getSize());

        for (int i = 0; i < inv.getSize(); i++) {
          dataOutput.writeObject(inv.getItem(i));
        }

        dataOutput.close();
        invs = Base64Coder.encodeLines(outputStream.toByteArray());
      } catch (Exception e) {
        return false;
      }

      if (hasKitsaved(kit, uuid)) {
        mysql.update(
            "UPDATE KNOCKITKITS SET INVENTORY = '"
                + invs
                + "' WHERE UUID = '"
                + uuid.toString()
                + "' AND KIT = "
                + kit);
      } else {
        mysql.update(
            "INSERT INTO KNOCKITKITS(UUID,KIT,INVENTORY) VALUES ('"
                + uuid.toString()
                + "','"
                + kit
                + "','"
                + invs
                + "')");
      }
      return true;
    }

    public static Inventory getKit(int kit, UUID uuid) {

      try {
        String data = null;
        ResultSet rs =
            mysql.query(
                "SELECT * FROM KNOCKITKITS WHERE UUID = '"
                    + uuid.toString()
                    + "' AND KIT = "
                    + kit);
        if (rs.next()) {
          data = rs.getString("INVENTORY");
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
        Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

        for (int i = 0; i < inventory.getSize(); i++) {
          inventory.setItem(i, (ItemStack) dataInput.readObject());
        }

        dataInput.close();
        return inventory;
      } catch (Exception e) {
      }
      return null;
    }
  }

  public static class Training {

    public static boolean hasKitSaved(UUID uuid) {
      try {
        ResultSet rs =
            mysql.query("SELECT UUID FROM TRAINING_KITS WHERE UUID = '" + uuid.toString() + "'");
        if (rs.next()) {
          return rs.getString("UUID").equalsIgnoreCase(uuid.toString());
        }
      } catch (SQLException ex) {
      }
      return false;
    }

    public static boolean saveKit(UUID uuid, Inventory inv, ItemStack[] arms) {

      String inventory = null;
      try {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outStream);

        dataOutput.writeInt(inv.getSize());

        for (int i = 0; i < inv.getSize(); i++) {
          dataOutput.writeObject(inv.getItem(i));
        }
        dataOutput.close();
        inventory = Base64.getEncoder().encodeToString(outStream.toByteArray());
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
      String armor = null;
      try {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outStream);

        dataOutput.writeInt(arms.length);

        for (int i = 0; i < arms.length; i++) {
          dataOutput.writeObject(arms[i]);
        }
        dataOutput.close();
        armor = Base64.getEncoder().encodeToString(outStream.toByteArray());
      } catch (Exception ex) {
        ex.printStackTrace();
        return false;
      }

      if (hasKitSaved(uuid)) {
        mysql.update(
            "UPDATE TRAINING_KITS SET INVENTORY = '"
                + inventory
                + "' WHERE UUID = '"
                + uuid.toString()
                + "'");
        mysql.update(
            "UPDATE TRAINING_KITS SET ARMOR = '"
                + armor
                + "' WHERE UUID = '"
                + uuid.toString()
                + "'");
      } else {
        mysql.update(
            "INSERT INTO TRAINING_KITS (UUID, INVENTORY, ARMOR) VALUES ('"
                + uuid.toString()
                + "','"
                + inventory
                + "','"
                + armor
                + "')");
      }
      return true;
    }

    public static Object[] getKit(UUID uuid) {

      String inventory;
      String armor;

      try {
        ResultSet rs =
            mysql.query(
                "SELECT INVENTORY,ARMOR FROM TRAINING_KITS WHERE UUID = '" + uuid.toString() + "'");
        if (rs.next()) {
          inventory = rs.getString("INVENTORY");
          armor = rs.getString("ARMOR");
        } else {
          return null;
        }
      } catch (SQLException ex) {
        ex.printStackTrace();
        return null;
      }

      Inventory ret_inv;
      ItemStack[] ret_armor;

      try {
        ByteArrayInputStream inputStream =
            new ByteArrayInputStream(Base64.getDecoder().decode(inventory));
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

        int size = dataInput.readInt();
        ret_inv = Bukkit.getServer().createInventory(null, size);

        for (int i = 0; i < size; i++) {
          ret_inv.setItem(i, (ItemStack) dataInput.readObject());
        }
        dataInput.close();
      } catch (IOException | IllegalArgumentException | ClassNotFoundException e) {
        e.printStackTrace();
        return null;
      }

      try {
        ByteArrayInputStream inputStream =
            new ByteArrayInputStream(Base64.getDecoder().decode(armor));
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
        int size = dataInput.readInt();
        ret_armor = new ItemStack[size];
        for (int i = 0; i < size; i++) {
          ret_armor[i] = (ItemStack) dataInput.readObject();
        }
        dataInput.close();
      } catch (IOException | IllegalArgumentException | ClassNotFoundException e) {
        e.printStackTrace();
        return null;
      }

      return new Object[] {ret_inv, ret_armor};
    }
  }
}
