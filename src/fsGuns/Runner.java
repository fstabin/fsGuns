package fsGuns;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Runner extends BukkitRunnable {

	ShooterManager sm;
	BulletManager bm;
	
	public Runner(Plugin pl,ShooterManager s,BulletManager b) {
		sm = s;
		bm = b;
		this.runTaskTimer(pl, 1, 1);
	}
	
	@Override
	public void run() {
		sm.run();
		bm.run();
		return;
	}

}
