package fsGuns.info.helper;

public class GunPerformance implements Cloneable {

	public GunPerformance() {
		// TODO Auto-generated constructor stub
	}
	
	public GunPerformance(GunPerformance other) {
		Damage = other.Damage;
		RPM = other.RPM;
		EffectiveTick = other.EffectiveTick;
		MaximumTick = other.MaximumTick;
		MuzzleVelocity = other.MuzzleVelocity;
		Recoil = other.Recoil;
		Spreading = other.Spreading;
	}
	
	public Object clone(){
		try{
			return super.clone();
			}catch(CloneNotSupportedException e){
				throw new InternalError(e.toString());
			}
	}
	
	public void mul(GunPerformance other) {
		Damage *= other.Damage;
		RPM *= other.RPM;
		EffectiveTick *= other.EffectiveTick;
		MaximumTick *= other.MaximumTick;
		MuzzleVelocity *= other.MuzzleVelocity;
		Recoil *= other.Recoil;
		Spreading *= other.Spreading;
	}
	
	public double Damage = 1;
	public double RPM = 1;
	public double EffectiveTick = 1;
	public double MaximumTick = 1;
	public double MuzzleVelocity = 1;
	public double Recoil = 1;
	public double Spreading = 1;
}
