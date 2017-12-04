package fsGuns.info;

import fsGuns.info.helper.GunPerformance;

public class InfoAccessory {
	GunPerformance modPerformance;
	
	String Name;
	String UseSlotName;

	public InfoAccessory(String name, String useSlotName, GunPerformance mod) {
		Name = name;
		UseSlotName = useSlotName;
		modPerformance = mod;
	}

	public String getName() {
		return Name;
	}
	
	public String getSlotName() {
		return UseSlotName;
	}
	
	//public GunPerformance getPerformance() {
	//	return  this.modPerformance; 
	//}
	
	public void culcGunPerformance(GunPerformance gp) {
		gp.mul(modPerformance);
	}
}
