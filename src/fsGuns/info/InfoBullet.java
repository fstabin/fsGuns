package fsGuns.info;

import java.util.List;

import org.bukkit.potion.PotionEffect;

import fsGuns.info.helper.ExplosionPerformance;
import fsGuns.info.helper.FlamePerformance;
import fsGuns.info.helper.GunPerformance;

public class InfoBullet {
	String Name;
	String CartridgeName;
	GunPerformance mgp;
	ExplosionPerformance mep;
	FlamePerformance mfp;
	List<PotionEffect> mlpp;
	List<PotionEffect> mlsp;
	List<PotionEffect> mllp;
	int count;
	//must, must, must, option, option
	public InfoBullet(String name,String cartridgeName,GunPerformance g, ExplosionPerformance ep,FlamePerformance fp,
			List<PotionEffect> lpp,List<PotionEffect> lsp,List<PotionEffect> llp, int cnt) {
		Name = name;
		CartridgeName = cartridgeName;
		mgp = g;
		mep = ep;
		mfp = fp;
		mlpp = lpp;
		mlsp = lsp;
		mllp = llp;
		count = cnt;
	}

	public String getName() {
		return Name;
	}
	
	public String getCartridgeName() {
		return CartridgeName;
	}
	
	//type or null
	public ExplosionPerformance getExplosionPerformance() {
		return mep;
	}
	
	//type or null
	public FlamePerformance getFlamePerformance() {
		return mfp;
	}
	
	public void culcGunPerformance(GunPerformance gp) {
		gp.mul(mgp);
	}

	public int getPotionEffectCount() {
		if(mlpp == null)return 0;
		return mlpp.size();
	}
	
	public PotionEffect getPotionEffect(int index) {
		return mlpp.get(index);
	}
	
	public int getSplashPotionEffectCount() {
		if(mlsp == null)return 0;
		return mlsp.size();
	}
	
	public PotionEffect getSplashPotionEffect(int index) {
		return mlsp.get(index);
	}
	
	public int getLingeringPotionEffectCount() {
		if(mllp == null)return 0;
		return mllp.size();
	}
	
	public PotionEffect getLingeringPotionEffect(int index) {
		return mllp.get(index);
	}
	
	public int getCount() {
		return count;
	}
}
