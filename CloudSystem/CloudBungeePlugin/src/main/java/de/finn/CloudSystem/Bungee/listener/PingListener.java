package de.finn.CloudSystem.Bungee.listener;

import de.finn.CloudSystem.Bungee.main.Main;
import java.util.UUID;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.PlayerInfo;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class PingListener implements Listener {

  public static boolean wartung = false;

  @SuppressWarnings("deprecation")
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPing(ProxyPingEvent e) {
    int fake = Main.ping_fakePlayer;
    if (!wartung) {
      ServerPing resp = e.getResponse();
      resp.setDescription(Main.ping_line1 + "\n" + Main.ping_line2);
      PlayerInfo[] info =
          new PlayerInfo[] {
            new PlayerInfo("§c" + Main.name, UUID.randomUUID()),
            new PlayerInfo(
                "§e" + (BungeeCord.getInstance().getOnlineCount() + fake) + " §bSpieler online",
                UUID.randomUUID())
          };
      resp.setPlayers(
          new Players(Main.ping_maxPlayer, BungeeCord.getInstance().getOnlineCount() + fake, info));
      e.setResponse(resp);
    } else {
      ServerPing resp = e.getResponse();
      resp.setDescription(Main.ping_wartung_line1 + "\n" + Main.ping_wartung_line2);
      PlayerInfo[] info =
          new PlayerInfo[] {
            new PlayerInfo("§c" + Main.name, UUID.randomUUID()),
            new PlayerInfo("§4§lWARTUNG", UUID.randomUUID())
          };
      resp.setVersion(new Protocol("§4§lWARTUNGSARBEITEN", 1 /*47*/));
      resp.setPlayers(
          new Players(Main.ping_maxPlayer, BungeeCord.getInstance().getOnlineCount() + fake, info));
      e.setResponse(resp);
    }
  }
}
