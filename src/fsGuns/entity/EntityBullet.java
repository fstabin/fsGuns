package fsGuns.entity;

import org.bukkit.entity.Entity;

import fsGuns.info.InfoBullet;

public class EntityBullet {
	Entity ent;
	InfoBullet ib;
	double damage;
	double teff,tmax;
	int hitted = 0;
	
	public EntityBullet(Entity e,InfoBullet info , double baseDamage, double effectiveTick, double maximumTick) {
		ent = e;
		ib = info;
		teff = effectiveTick;
		tmax = maximumTick;
		damage = baseDamage;
	}
	
	public double getDamage() {
		int tl = ent.getTicksLived();
		if(tl <= teff)return damage;
		else if(tl <= tmax && teff != tmax)return (damage * ((tmax - (double)tl) / (tmax - teff)));
		else return 0;
	}

	public int getMaxTick() {
		return (int)tmax;
	}

	public Entity getEntity() {
		return ent;
	}

	public InfoBullet getInfo() {
		return ib;
	}

	public void setHitted(int b) {
		hitted = b;
	}
	
	public int getHitted() {
		return hitted;
	}

}
