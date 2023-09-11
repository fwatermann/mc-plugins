package de.finn.CloudSystem.utils;

import java.util.HashMap;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class CloudScheduler {

  private static HashMap<Integer, Thread> id_thread = new HashMap<Integer, Thread>();
  private static HashMap<Integer, Boolean> id_running = new HashMap<Integer, Boolean>();

  public static int getSyncRepeatingTask(JavaPlugin plugin, Runnable run, long delay) {
    Random rnd = new Random();
    int id = rnd.nextInt(Integer.MAX_VALUE);
    while (id_thread.containsKey(id)) {
      id = rnd.nextInt(Integer.MAX_VALUE);
    }
    int lid = id;

    id_running.put(id, true);

    Thread th =
        new Thread(
            new Runnable() {

              @Override
              public void run() {
                while (id_running.containsKey(lid) && id_running.get(lid)) {
                  Bukkit.getScheduler()
                      .runTask(
                          plugin,
                          new Runnable() {

                            @Override
                            public void run() {
                              run.run();
                            }
                          });
                  try {
                    Thread.sleep(delay);
                  } catch (InterruptedException ex) {
                    ex.printStackTrace();
                  }
                }
              }
            });

    th.setName("CloudScheduler-#" + id);
    th.start();
    return id;
  }

  public static int getAsyncRepeatingTask(Runnable run, long delay) {
    Random rnd = new Random();
    int id = rnd.nextInt(Integer.MAX_VALUE);
    while (id_thread.containsKey(id)) {
      id = rnd.nextInt(Integer.MAX_VALUE);
    }
    int lid = id;
    id_running.put(id, true);
    Thread th =
        new Thread(
            new Runnable() {

              @Override
              public void run() {
                while (id_running.containsKey(lid) && id_running.get(lid)) {
                  run.run();
                  try {
                    Thread.sleep(delay);
                  } catch (InterruptedException ex) {
                    ex.printStackTrace();
                  }
                }
              }
            });

    th.setName("CloudScheduler-#" + id);
    th.start();
    return id;
  }

  public static void cancleTask(int id) {
    if (id_thread.containsKey(id)) {
      id_thread.get(id).interrupt();
      id_thread.remove(id);
    }
    if (id_running.containsKey(id)) {
      id_running.remove(id);
      id_running.put(id, false);
      id_running.remove(id);
    }
  }
}
