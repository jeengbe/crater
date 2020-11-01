package jeengbe.crater;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

public class Blocklist {
  private static File                                                  blocklistFile;
  private static FileConfiguration                                     blocklistConfig;

  private static Map<EntityType, Map<Material, Map<Material, Double>>> blocklist = new HashMap<>();

  public static void createBlocklist() {
    blocklistFile = new File(Main.me.getDataFolder(), "blocklist.yml");
    if (!blocklistFile.exists()) {
      blocklistFile.getParentFile().mkdirs();
      Main.me.saveResource("blocklist.yml", false);
    }

    blocklistConfig = new YamlConfiguration();
    try {
      blocklistConfig.load(blocklistFile);
    } catch (IOException | InvalidConfigurationException e) {
      e.printStackTrace();
    }
  }

  public static void loadBlocklist() {
    for (String e : blocklistConfig.getKeys(false)) {
      EntityType entity = EntityType.valueOf(e);
      ConfigurationSection esec = blocklistConfig.getConfigurationSection(e);
      Map<Material, Map<Material, Double>> emap = new HashMap<>();
      for (String m : esec.getKeys(false)) {
        Material mat = Material.valueOf(m);
        ConfigurationSection msec = esec.getConfigurationSection(m);
        Map<Material, Double> mmap = new HashMap<>();
        for (String r : msec.getKeys(false)) {
          Material repl = Material.valueOf(r);
          Double prob = msec.getDouble(r);
          mmap.put(repl, prob);
        }
        emap.put(mat, mmap);
      }
      blocklist.put(entity, emap);
    }
  }

  public static void transform(EntityType e, Block t, Random r) {
    if (blocklist == null)
      return;
    if (!blocklist.containsKey(e))
      return;
    if (t.getType() == null)
      return;
    Map<Material, Double> rep = blocklist.get(e).get(t.getType());
    if (rep == null)
      return;
    for (Material re : rep.keySet()) {
      if (r.nextDouble() < rep.get(re) * 0.4) {
        t.setType(re);
        return;
      }
    }
  }
}
