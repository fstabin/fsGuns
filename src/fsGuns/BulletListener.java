package fsGuns;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SplashPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import fsGuns.entity.EntityBullet;
import fsGuns.info.InfoBullet;
import fsGuns.info.helper.ExplosionPerformance;
import fsGuns.info.helper.FlamePerformance;

public class BulletListener  implements Listener  {
	BulletManager bm;
	JavaPlugin plugin;
	public BulletListener(JavaPlugin pl, BulletManager bmng) {
		plugin = pl;
		bm = bmng;
	}
	
	@EventHandler
	public void onShotHit(ProjectileHitEvent event) {
		Projectile ent = event.getEntity();
		if(ent != null) {
			Entity en = (Entity)ent;
			EntityBullet eb = bm.getBullet(en);
			if(eb != null) {
				Location hitloc = null;
				Entity he = event.getHitEntity();
				Block hb = event.getHitBlock();
				if(he != null)hitloc = he.getLocation();
				else if(hb != null) {
					hitloc = hb.getLocation();
					hitloc.add(0.5,0.5,0.5);
				}
				else hitloc = en.getLocation();
				World wor = en.getWorld();
				if(eb.getHitted() == 1) {
					eb.setHitted(2);
				}
				else if(eb.getHitted() == 0) {
					eb.setHitted(1);
					InfoBullet ib = eb.getInfo();
					if(ib != null) {
						ExplosionPerformance ep = ib.getExplosionPerformance();
						if(ep != null) {
							Vector pos = null;
							if(he != null) {
								pos = he.getLocation().toVector();
								pos.setX(pos.getX() + he.getWidth() * Math.random());
								pos.setY(pos.getY() + he.getHeight() * Math.random());
								pos.setZ(pos.getZ() + he.getWidth() * Math.random());
							}
							else if(hb != null) {
								pos = hb.getLocation().toVector();
								pos.setX(pos.getX() + Math.random());
								pos.setY(pos.getY() + Math.random());
								pos.setZ(pos.getZ() + Math.random());
							}
							else {
								pos = en.getLocation().toVector().add(ent.getVelocity());
								pos.setX(pos.getX() + Math.random() * 2 - 1.0);
								pos.setY(pos.getY() + Math.random() * 2 - 1.0);
								pos.setZ(pos.getZ() + Math.random() * 2 - 1.0);
							}
							wor.createExplosion(pos.getX() + Math.random() * 2 - 1.0, pos.getY()+ Math.random() * 2 - 1.0, pos.getZ()+ Math.random() * 2 - 1.0, ep.getPower(), ep.getSetFire(), false);
						}
						if(ib.getSplashPotionEffectCount() > 0) {
							SplashPotion splashPt = wor.spawn(hitloc, SplashPotion.class);
							if(splashPt != null) {
								ItemStack is = new ItemStack(Material.SPLASH_POTION);
								ItemMeta meta = is.getItemMeta();
								if(meta instanceof PotionMeta) {
									PotionMeta pmeta = (PotionMeta)meta;
									for(int i = 0;i < ib.getSplashPotionEffectCount();i++) {
										pmeta.addCustomEffect(ib.getSplashPotionEffect(i), true);
									}
									pmeta.setBasePotionData(new PotionData(PotionType.UNCRAFTABLE));
									is.setItemMeta(pmeta);
									splashPt.setItem(is);
									
									splashPt.setVelocity(ent.getVelocity());
									splashPt.setShooter(ent.getShooter());
									splashPt.setGravity(true);
									splashPt.setPortalCooldown(0x7fffffff);
								}
							}
						}
						if(ib.getLingeringPotionEffectCount() > 0) {
							LingeringPotion lPt = wor.spawn(hitloc, LingeringPotion.class);
							if(lPt != null) {
								ItemStack is = new ItemStack(Material.LINGERING_POTION);
								ItemMeta meta = is.getItemMeta();
								if(meta instanceof PotionMeta) {
									PotionMeta pmeta = (PotionMeta)meta;
									for(int i = 0;i < ib.getLingeringPotionEffectCount();i++) {
										pmeta.addCustomEffect(ib.getLingeringPotionEffect(i), true);
									}
									pmeta.setBasePotionData(new PotionData(PotionType.UNCRAFTABLE));
									is.setItemMeta(pmeta);
									lPt.setItem(is);
									lPt.setVelocity(ent.getVelocity());
									lPt.setShooter(ent.getShooter());
									lPt.setGravity(true);
									lPt.setPortalCooldown(0x7fffffff);
								}
							}
						}
					}
					Entity lmob = event.getHitEntity();
					if(lmob instanceof LivingEntity) {
						LivingEntity le = (LivingEntity)lmob;
						le.setNoDamageTicks(0);
						return;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityDamaged(EntityDamageByEntityEvent event) {
		Entity damager = event.getDamager();
		if(damager != null) {
			EntityBullet eb = bm.getBullet(damager);
			if(eb != null) {
				if(eb.getHitted() <= 1) {
					eb.setHitted(2);
					Entity lmob = event.getEntity();
					if(lmob instanceof LivingEntity) {
						LivingEntity damagee = (LivingEntity)lmob;
						event.setDamage(eb.getDamage());
						InfoBullet ib = eb.getInfo();
						FlamePerformance fp = ib.getFlamePerformance();
						if(fp != null) {
							damagee.setFireTicks(damagee.getFireTicks() + fp.getFireTick());
						}
						for(int i = 0;i < ib.getPotionEffectCount();i++) {
							damagee.addPotionEffect(ib.getPotionEffect(i));
						}
						return;
					}
				}
				else {
					event.setCancelled(true);
				}
			}
		}
	}
}
