package jeengbe.crater;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
  public static Main me;

  @Override
  public void onEnable() {
    Main.me = this;
    Blocklist.createBlocklist();
    Blocklist.loadBlocklist();

    getServer().getPluginManager().registerEvents(this, this);
  }

  @EventHandler
  public void onExplode(EntityExplodeEvent e) {
    e.setYield(1.0f);
    List<Block> l = new ArrayList<>(e.blockList());
    Random r = new Random();
    for (Block b : e.blockList()) {
      for (Block t : trace(e.getLocation(), b.getLocation().add(0.5, 0.5, 0.5), r)) {
        if (!l.contains(t)) {
          Blocklist.transform(e.getEntity().getType(), t, r);
          l.add(t);
        }
      }
    }
  }

  public static List<Block> trace(Location a, Location b, Random r) {
    List<Block> ret = new ArrayList<>();
    for (int i = 0; i < r.nextInt(3) + 2; i++) {
      Location diff = b.clone().add(r.nextDouble() / 2, r.nextDouble() / 2, r.nextDouble() / 2).subtract(a);
      Double max = Math.max(Math.abs(diff.getX()), Math.max(Math.abs(diff.getY()), Math.abs(diff.getZ())));
      diff.setX(diff.getX() / max);
      diff.setY(diff.getY() / max);
      diff.setZ(diff.getZ() / max);

      ret.add(b.clone().add(diff).getBlock());
    }

    for (int i = 0; i < r.nextInt(9) + 6; i++) {
      Location diff = b.clone().add(r.nextDouble() / 2, r.nextDouble() / 2, r.nextDouble() / 2).subtract(a);
      Double max = Math.max(Math.abs(diff.getX()), Math.max(Math.abs(diff.getY()), Math.abs(diff.getZ())));
      diff.setX(diff.getX() / max * (r.nextDouble() * 1.3 + 1.3));
      diff.setY(diff.getY() / max * (r.nextDouble() * 1.3 + 1.3));
      diff.setZ(diff.getZ() / max * (r.nextDouble() * 1.3 + 1.3));

      ret.add(b.clone().add(diff).getBlock());
    }

    return ret;
  }
}
