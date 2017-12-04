package fsGuns.info;

import fsGuns.info.helper.GunPerformance;

public class InfoMagazine extends InfoAccessory{

	String CartridgeName;
	int Cap;
	
	public InfoMagazine(String name, String cartridgeName, GunPerformance mod, int cap) {
		super(name, "Magazine", mod);
		CartridgeName = cartridgeName;
		Cap = cap;
	}
	
	public String getCartridgeName() {
		return CartridgeName;
	}
	
	public int getCapacity() {
		return Cap;
	}
}
