package fsGuns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import fsGuns.entity.EntityBullet;
import fsGuns.info.InfoBullet;
import fsGuns.info.helper.GunPerformance;

public class BulletManager {
	
	HashMap<String, InfoBullet> mib;
	HashMap<Entity, EntityBullet> met;

	public BulletManager() {
		mib = new HashMap<String, InfoBullet>();
		met = new HashMap<Entity, EntityBullet>();
	}

	public void run() {
		List<Entity>removed = new ArrayList<Entity>();
		for (Map.Entry<Entity, EntityBullet> entry : met.entrySet()) {
			Entity et = entry.getKey();
			EntityBullet eb = entry.getValue();
			if(!et.isDead()) {
				if(et.getTicksLived() <= eb.getMaxTick()) {
					continue;
				}
				et.remove();
			}
			removed.add(et);
		}
		for (Entity et : removed) {
			met.remove(et);
		}
		return;
	}

	public void summonBullet(InfoBullet ib,GunPerformance gp, Player pl, Random rnd) {
		World wor = pl.getWorld();
		if(wor != null) {
			Location loc = pl.getEyeLocation();
			if(loc != null) {
				Vector dir = loc.getDirection();
				if(dir.getY() >= -0.5)loc.add(loc.getDirection());
				else loc.add(loc.getDirection().multiply(2.0));
				int max = ib.getCount();
				for(int i = 0;i < max; i++) {
					Entity ent = wor.spawnEntity(loc, EntityType.ARROW);
					if(ent != null) {
						Arrow sb = (Arrow)ent;
						dir = loc.getDirection();
						dir = dir.normalize();
						Vector vdtop = dir.clone();
						vdtop.crossProduct(new Vector(0,1,0));
						Vector vdside = vdtop.clone();
						vdtop.crossProduct(dir);
						double r = rnd.nextDouble() * Math.PI * 2;
						vdtop.multiply(Math.sin(r));
						vdside.multiply(Math.cos(r));
						vdtop.add(vdside);
						double modsp = rnd.nextDouble();
						vdtop.multiply(gp.Spreading * modsp * modsp);
						dir.add(vdtop);
						dir.normalize();
						
						dir.multiply(gp.MuzzleVelocity);
						sb.setVelocity(dir);
						sb.setShooter(pl);
						sb.setGravity(true);
						sb.setPortalCooldown(0x7fffffff);
						sb.setBounce(false);
						
						sb.setKnockbackStrength(0);
						this.addBullet(new EntityBullet(sb, ib, gp.Damage, gp.EffectiveTick, gp.MaximumTick));
					}
				}
				wor.playSound(loc,Sound.ENTITY_GENERIC_EXPLODE ,1, 1f);
			}
		}
	}
	
	public void addBullet(EntityBullet eb) {
		met.put(eb.getEntity(), eb);
	}
		
	public void removeBullet(EntityBullet eb) {
		eb.getEntity().remove();
		met.remove(eb.getEntity());
	}
	
	public EntityBullet getBullet(Entity e) {
		return met.get(e);
	}
	
	public void clearBullet() {
		for (Map.Entry<Entity, EntityBullet> entry : met.entrySet()) {
			Entity et = entry.getKey();
			et.remove();
		}
		met.clear();
	}
}
