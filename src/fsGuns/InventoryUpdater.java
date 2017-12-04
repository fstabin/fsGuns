package fsGuns;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryUpdater extends BukkitRunnable{
	Player pl;
	public InventoryUpdater(Player player) {
		pl = player;
	}
	
	@Override
	public void run() {
		if(pl != null)pl.updateInventory();
		this.cancel();
		return;
	}

}
