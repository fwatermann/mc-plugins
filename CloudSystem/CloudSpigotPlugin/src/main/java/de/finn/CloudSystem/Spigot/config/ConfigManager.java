package de.finn.CloudSystem.Spigot.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class ConfigManager {

  private File file;
  private boolean loaded;

  private HashMap<String, String> values = new HashMap<String, String>();

  public ConfigManager(File file) {
    this.file = file;
    try {
      if (!this.file.exists()) this.file.createNewFile();
    } catch (IOException e) {
    }
  }

  public void load() {
    try {
      FileReader reader = new FileReader(file);
      BufferedReader br = new BufferedReader(reader);
      String tmp = null;
      while ((tmp = br.readLine()) != null) {
        String[] a = tmp.split("=");
        String b = a[0];
        String c = a[1];
        if (a.length > 2) {
          for (int i = 2; i < a.length; i++) {
            c += "=" + a[i];
          }
        }
        values.put(b, c);
      }
      br.close();
      reader.close();
      loaded = true;
    } catch (IOException ex) {
    }
  }

  public void unload() {
    values.clear();
    loaded = false;
  }

  public void reload() {
    this.unload();
    this.load();
  }

  public boolean isLoaded() {
    return loaded;
  }

  /*
   *
   * >>>> GET <<<<
   *
   */

  public int getInt(String key) {
    if (!loaded) {
      System.out.println(
          "Could not return <Boolean>, config <" + file.getAbsolutePath() + "> is not loaded!");
      return 0;
    }
    if (values.containsKey(key)) {
      return Integer.parseInt(values.get(key));
    } else {
      System.out.println(
          "Could not found entry <" + key + "> in config <" + file.getAbsolutePath() + ">!");
      return 0;
    }
  }

  public String getString(String key) {
    if (!loaded) {
      System.out.println(
          "Could not return <Boolean>, config <" + file.getAbsolutePath() + "> is not loaded!");
      return null;
    }
    if (values.containsKey(key)) {
      return values.get(key);
    } else {
      System.out.println(
          "Could not found entry <" + key + "> in config <" + file.getAbsolutePath() + ">!");
      return null;
    }
  }

  public byte getByte(String key) {
    if (!loaded) {
      System.out.println(
          "Could not return <Boolean>, config <" + file.getAbsolutePath() + "> is not loaded!");
      return (byte) 0;
    }
    if (values.containsKey(key)) {
      return Byte.parseByte(values.get(key));
    } else {
      System.out.println(
          "Could not found entry <" + key + "> in config <" + file.getAbsolutePath() + ">!");
      return (byte) 0;
    }
  }

  public short getShort(String key) {
    if (!loaded) {
      System.out.println(
          "Could not return <Boolean>, config <" + file.getAbsolutePath() + "> is not loaded!");
      return (short) 0;
    }
    if (values.containsKey(key)) {
      return Short.parseShort(values.get(key));
    } else {
      System.out.println(
          "Could not found entry <" + key + "> in config <" + file.getAbsolutePath() + ">!");
      return (short) 0;
    }
  }

  public double getDouble(String key) {
    if (!loaded) {
      System.out.println(
          "Could not return <Boolean>, config <" + file.getAbsolutePath() + "> is not loaded!");
      return 0.0D;
    }
    if (values.containsKey(key)) {
      return Double.parseDouble(values.get(key));
    } else {
      System.out.println(
          "Could not found entry <" + key + "> in config <" + file.getAbsolutePath() + ">!");
      return (double) 0;
    }
  }

  public boolean getBoolean(String key) {
    if (!loaded) {
      System.out.println(
          "Could not return <Boolean>, config <" + file.getAbsolutePath() + "> is not loaded!");
      return false;
    }
    if (values.containsKey(key)) {
      return Boolean.parseBoolean(values.get(key));
    } else {
      System.out.println(
          "Could not found entry <" + key + "> in config <" + file.getAbsolutePath() + ">!");
      return false;
    }
  }

  /*
   *
   * >>>> SET <<<<
   *
   */

  public void set(String key, Object value) {
    if (!loaded) {
      System.out.println(
          "Cannot write into config <" + file.getAbsolutePath() + "> because it is not loaded!");
      return;
    }
    if (!values.containsKey(key)) {
      values.put(key, value.toString());
      rewriteConfig();
    } else {
      values.remove(key);
      values.put(key, value.toString());
      rewriteConfig();
    }
  }

  public void rewriteConfig() {
    try {
      this.file.delete();
      this.file.createNewFile();
      PrintWriter writer = new PrintWriter(new FileOutputStream(this.file));
      for (String all : values.keySet()) {
        writer.println(all + "=" + values.get(all));
        writer.flush();
      }
      writer.close();
    } catch (IOException ex) {
    }
  }

  /*
   *
   * >>>> isSet <<<<
   *
   *
   */

  public boolean isSet(String key) {
    if (!loaded) {
      System.out.println(
          "Cannot check if key is set because config <"
              + file.getAbsolutePath()
              + "> is not loaded!");
      return false;
    } else {
      return values.containsKey(key);
    }
  }
}
