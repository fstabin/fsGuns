package fsGuns.info.helper;

public class ExplosionPerformance {
	float p;
	boolean f;
	public ExplosionPerformance(float power, boolean setfire) {
		p = power;
		f = setfire;
	}
	
	public float getPower() {
		return p;
	}
	
	public boolean getSetFire() {
		return f;
	}
}
