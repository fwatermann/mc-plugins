package de.finn.CloudSystem.Spigot.config;

import de.finn.CloudSystem.Spigot.main.CloudAPI;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MySQLConfig {

  private static File file = new File("./plugins/cloud/mysql.yml");
  private static FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

  public static String[] getCredentials(String datenbank) {
    if (!cfg.isSet("MySQL." + datenbank + ".Database")) {
      createCredentials(datenbank);
      System.out.println("+++++++++++++++++++++++CREDENTIALS CREATED!");
    }
    String database = cfg.getString("MySQL." + datenbank + ".Database");
    String host = cfg.getString("MySQL." + datenbank + ".Host");
    String username = cfg.getString("MySQL." + datenbank + ".Username");
    String password = cfg.getString("MySQL." + datenbank + ".Password");
    return new String[] {
      host.equalsIgnoreCase("CLOUDMASTERIP") ? CloudAPI.getMasterIP() : host,
      database,
      username,
      password
    };
  }

  private static void createCredentials(String datenbank) {
    cfg.set("MySQL." + datenbank + ".Database", datenbank);
    cfg.set("MySQL." + datenbank + ".Host", "CLOUDMASTERIP");
    cfg.set("MySQL." + datenbank + ".Username", datenbank);
    cfg.set("MySQL." + datenbank + ".Password", "password");
    try {
      cfg.save(file);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
