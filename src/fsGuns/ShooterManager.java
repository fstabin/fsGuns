package fsGuns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import fsGuns.info.InfoBullet;
import fsGuns.info.Info_Manager;
import fsGuns.info.helper.GunPerformance;
import fsGuns.tool.Gun;

public class ShooterManager{
	Plugin plugin;
	Info_Manager acmanager;
	BulletManager bumanager;
	Map<Player, Gun> shooters;
	public ShooterManager(Plugin pl, Info_Manager acmng, BulletManager bm) {
		plugin = pl;
		acmanager = acmng;
		bumanager = bm;
		shooters = new HashMap<Player, Gun>();
	}
	
	public void run() {
		List<Player>removed = new ArrayList<Player>();
		for (Map.Entry<Player, Gun> entry : shooters.entrySet()) {
			Player pl = entry.getKey();
			Gun wp = entry.getValue();
			if(wp.getCoolDownNow()) {
				if(wp.calcCoolDownNow())continue;	
			}
			else {
				if(pl.isOnline()) {
					if(!wp.isEmpty()) {
						int fc = wp.calcThisTickFireCount();
						InfoBullet ib = acmanager.getBullet(wp.getBulletName());
						for(int i = 0; i < fc; i++) {
							GunPerformance gp = wp.getGunPerformance();
							bumanager.summonBullet(ib, gp, pl, wp.getRandom());
							//”½“®ŒvŽZ
							Location loc = pl.getEyeLocation();
							Vector dir = loc.getDirection();
							dir.normalize();
							dir.multiply(-gp.Recoil);
							pl.setVelocity(dir.add(pl.getVelocity()));
						}
						wp.Fired(fc, !pl.hasMetadata(plugin.getName() + ":fire_unlimited"));
						continue;
					}
					if(wp.isEmpty() || wp.getCoolDownNow())stopFire(pl);
					continue;
				}
			}
			removed.add(pl);
		}
		for (Player pl : removed) {
			removeShooter(pl);
		}
		return;
	}

	public boolean addShooter(Player pl,ItemStack is) {
		if(pl.hasPermission(Permissions.fire) && !shooters.containsKey(pl)) {
			Gun g = new Gun(acmanager, is);
			if(g.getStat()) {
				shooters.put(pl, g);
				return true;
			}
		}
		return false;
	}
	
	public void stopFire(Player pl) {
		Gun gun  = shooters.get(pl);
		if(gun != null) {
			gun.setCoolDown(true);
		}
	}
	
	private void removeShooter(Player pl) {
		shooters.remove(pl);
	}

	public boolean isOnShooting(Player pl) {
		return shooters.containsKey(pl);
	}
}
