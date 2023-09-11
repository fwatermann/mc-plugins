package de.finn.CloudSystem.Coins;

import de.finn.CloudSystem.Spigot.config.MySQLConfig;
import de.finn.CloudSystem.Spigot.main.Main;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CoinSQL {

  public static MySQL mysql;

  public static void connect() {
    String[] credentials = MySQLConfig.getCredentials("CoinSystem");
    mysql =
        new MySQL(
            credentials[0],
            credentials[1],
            credentials[2],
            credentials[3],
            Main.getPlugin(Main.class));
    mysql.update(
        "CREATE TABLE IF NOT EXISTS COINS(ID int primary key auto_increment, UUID varchar(128), COINS int)");
  }

  public static boolean isListed(UUID uuid) {
    try {
      ResultSet rs = mysql.query("SELECT * FROM COINS WHERE UUID = '" + uuid.toString() + "'");
      if (rs.next()) {
        return rs.getString("UUID") != null;
      }
    } catch (SQLException ex) {
    }
    return false;
  }

  public static void createEntry(UUID uuid, int coins) {
    mysql.update(
        "INSERT INTO COINS (UUID, COINS) VALUES ('" + uuid.toString() + "','" + coins + "')");
  }

  public static int getCoins(UUID uuid) {
    if (!isListed(uuid)) {
      createEntry(uuid, 0);
      return 0;
    } else {
      try {
        ResultSet rs =
            mysql.query("SELECT COINS FROM COINS WHERE UUID = '" + uuid.toString() + "'");
        if (rs.next()) {
          return rs.getInt("COINS");
        }
      } catch (SQLException ex) {
      }
      return 0;
    }
  }

  public static void setCoins(UUID uuid, int coins) {
    if (!isListed(uuid)) {
      createEntry(uuid, coins);
    } else {
      mysql.update(
          "UPDATE COINS SET COINS = '" + coins + "' WHERE UUID = '" + uuid.toString() + "'");
    }
  }

  public static void addCoins(UUID uuid, int coins) {
    int a = getCoins(uuid);
    int b = a + coins;
    setCoins(uuid, b);
  }

  public static void removeCoins(UUID uuid, int coins) {
    int a = getCoins(uuid);
    int b = a - coins;
    setCoins(uuid, b);
  }
}
