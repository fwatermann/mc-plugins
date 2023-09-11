package de.finn.CloudSystem.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.json.JSONObject;

public class SkullItemGenerator {

  public static ItemStack getSkullItem(String url, int amount) {
    ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
    SkullMeta meta = (SkullMeta) head.getItemMeta();
    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    byte[] encData =
        Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
    profile.getProperties().put("textures", new Property("textures", new String(encData)));
    try {
      Field f = meta.getClass().getDeclaredField("profile");
      f.setAccessible(true);
      f.set(meta, profile);
      f.setAccessible(false);
    } catch (NoSuchFieldException
        | SecurityException
        | IllegalArgumentException
        | IllegalAccessException e1) {
    }
    head.setItemMeta(meta);
    head.setAmount(amount);
    return head;
  }

  public static ItemStack getSkullItemOfTextureValue(String value, int amount) {

    String a = new String(Base64.getDecoder().decode(value.getBytes()));

    JSONObject obj = new JSONObject(a);
    JSONObject textures = obj.getJSONObject("textures");
    JSONObject skin = textures.getJSONObject("SKIN");
    String url = skin.getString("url");

    return getSkullItem(url, amount);
  }

  public enum SKINS {
    MHF_ARROWRIGHT(
        "http://textures.minecraft.net/texture/1b6f1a25b6bc199946472aedb370522584ff6f4e83221e5946bd2e41b5ca13b"),
    MHF_ARROWLEFT(
        "http://textures.minecraft.net/texture/3ebf907494a935e955bfcadab81beafb90fb9be49c7026ba97d798d5f1a23"),
    MHF_ARROWUP(
        "http://textures.minecraft.net/texture/d48b768c623432dfb259fb3c3978e98dec111f79dbd6cd88f21155374b70b3c"),
    MHF_ARROWDOWN(
        "http://textures.minecraft.net/texture/2dadd755d08537352bf7a93e3bb7dd4d733121d39f2fb67073cd471f561194dd"),
    MHF_QUESTION(
        "http://textures.minecraft.net/texture/5163dafac1d91a8c91db576caac784336791a6e18d8f7f62778fc47bf146b6"),
    MHF_EXCLAMATION(
        "http://textures.minecraft.net/texture/6a53bdd1545531c9ebb9c6f895bc576012f61820e6f489885988a7e8709a3f48"),
    MHF_ALEX(
        "http://textures.minecraft.net/texture/63b098967340daac529293c24e04910509b208e7b94563c3ef31dec7b3750"),
    MHF_BLAZE(
        "http://textures.minecraft.net/texture/b78ef2e4cf2c41a2d14bfde9caff10219f5b1bf5b35a49eb51c6467882cb5f0"),
    MHF_CAVESPIDER(
        "http://textures.minecraft.net/texture/41645dfd77d09923107b3496e94eeb5c30329f97efc96ed76e226e98224"),
    MHF_CHICKEN(
        "http://textures.minecraft.net/texture/1638469a599ceef7207537603248a9ab11ff591fd378bea4735b346a7fae893"),
    MHF_COW(
        "http://textures.minecraft.net/texture/5d6c6eda942f7f5f71c3161c7306f4aed307d82895f9d2b07ab4525718edc5"),
    MHF_CREEPER(
        "http://textures.minecraft.net/texture/295ef836389af993158aba27ff37b6567185f7a721ca90fdfeb937a7cb5747"),
    MHF_ENDERMAN(
        "http://textures.minecraft.net/texture/7a59bb0a7a32965b3d90d8eafa899d1835f424509eadd4e6b709ada50b9cf"),
    MHF_GHAST(
        "http://textures.minecraft.net/texture/8b6a72138d69fbbd2fea3fa251cabd87152e4f1c97e5f986bf685571db3cc0"),
    MHF_GOLEM(
        "http://textures.minecraft.net/texture/89091d79ea0f59ef7ef94d7bba6e5f17f2f7d4572c44f90f76c4819a714"),
    MHF_HEROBRINE(
        "http://textures.minecraft.net/texture/8bfd171f69f4fb60cce6939f3badddfdfef328ff856189fe4ce3f4443664dc16"),
    MHF_LAVASLIME(
        "http://textures.minecraft.net/texture/38957d5023c937c4c41aa2412d43410bda23cf79a9f6ab36b76fef2d7c429"),
    MHF_MUSHROOMCOW(
        "http://textures.minecraft.net/texture/d0bc61b9757a7b83e03cd2507a2157913c2cf016e7c096a4d6cf1fe1b8db"),
    MHF_OCELOT(
        "http://textures.minecraft.net/texture/5657cd5c2989ff97570fec4ddcdc6926a68a3393250c1be1f0b114a1db1"),
    MHF_PIG(
        "http://textures.minecraft.net/texture/621668ef7cb79dd9c22ce3d1f3f4cb6e2559893b6df4a469514e667c16aa4"),
    MHF_PIGZOMBIE(
        "http://textures.minecraft.net/texture/74e9c6e98582ffd8ff8feb3322cd1849c43fb16b158abb11ca7b42eda7743eb"),
    MHF_SHEEP(
        "http://textures.minecraft.net/texture/f31f9ccc6b3e32ecf13b8a11ac29cd33d18c95fc73db8a66c5d657ccb8be70"),
    MHF_SKELETON(
        "http://textures.minecraft.net/texture/2e5be6a3c0159d2c1f3b1e4e1d8384b6f7ebac993d58b10b9f8989c78a232"),
    MHF_SLIME(
        "http://textures.minecraft.net/texture/b0cc3597f25d62b7f5748cec22e2fbed236040f1c27047afea1f50f768a8"),
    MHF_SPIDER(
        "http://textures.minecraft.net/texture/cd541541daaff50896cd258bdbdd4cf80c3ba816735726078bfe393927e57f1"),
    MHF_SQUID(
        "http://textures.minecraft.net/texture/01433be242366af126da434b8735df1eb5b3cb2cede39145974e9c483607bac"),
    MHF_STEVE(""),
    MHF_VILLAGER(
        "http://textures.minecraft.net/texture/822d8e751c8f2fd4c8942c44bdb2f5ca4d8ae8e575ed3eb34c18a86e93b"),
    MHF_WITHERSKELETON(
        "http://textures.minecraft.net/texture/233b41fa79cd53a230e2db942863843183a70404533bbc01fab744769bcb"),
    MHF_ZOMBIE(
        "http://textures.minecraft.net/texture/56fc854bb84cf4b7697297973e02b79bc10698460b51a639c60e5e417734e11"),
    MHF_CACTUS(
        "http://textures.minecraft.net/texture/38c9a730269ce1de3e9fa064afb370cbcd0766d729f3e29e4f320a433b098b5"),
    MHF_CAKE(
        "http://textures.minecraft.net/texture/f9136514f342e7c5208a1422506a866158ef84d2b249220139e8bf6032e193"),
    MHF_CHEST(
        "http://textures.minecraft.net/texture/6f68d509b5d1669b971dd1d4df2e47e19bcb1b33bf1a7ff1dda29bfc6f9ebf"),
    MHF_COCONUT_B(
        "http://textures.minecraft.net/texture/32c62fd8e474d09940604f82712a44abb249d63aff87f998374ca849ab17412"),
    MHF_COCONUT_G(
        "http://textures.minecraft.net/texture/bf61259a7ed75dfc15f4328f69fa5d549ef1ba9c7aa85c53b8c76173fac3c69"),
    MHF_MELON(
        "http://textures.minecraft.net/texture/c3fed514c3e238ca7ac1c94b897ff6711b1dbe50174afc235c8f80d029"),
    MHF_OAKLOG(
        "http://textures.minecraft.net/texture/ffe5109c86fe6de8eb8afe32f0496a844dd655e4d4a39ef7b0fb61791848a5da"),
    MHF_PRESENT1(
        "http://textures.minecraft.net/texture/64abe81e6f4961e0f6bd82f2d4135b6b5fc845739e71cfe3b8943531d921e"),
    MHF_PRESENT2(
        "http://textures.minecraft.net/texture/bd7a9f6ed08dd217fdf09f4652bf6b7af621e1d5f8963605349da73998a443"),
    MHF_PUMPKIN(
        "http://textures.minecraft.net/texture/d7d7ad2dcb57dfa9f023dbb99b698fc53075c3e9d654506139a647ac907fddc5"),
    MHF_TNT(
        "http://textures.minecraft.net/texture/728a1815689d7194cf7db061b59f63106264b51387976a7fb74ab79b5641"),
    MHF_TNT2(
        "http://textures.minecraft.net/texture/eb994b41f07f87b328186acfcbdabc699d5b1847fabb2e49d5abc27865143a4e");

    private String url;

    SKINS(String url) {
      this.url = url;
    }

    public String getURL() {
      return url;
    }

    @Override
    public String toString() {
      return this.url;
    }
  }
}
