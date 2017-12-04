package fsGuns.info;

import java.util.List;

import fsGuns.info.helper.GunPerformance;

public class InfoFrame{
	public static final class FireMode implements Cloneable  {
		public static enum ModeType{
			FULLAUTO,
			SEMIAUTO,
			BURST,
		}
		public ModeType type;
		public int MaxFireCount;
		
		public Object clone(){
			try{
				return super.clone();
				}catch(CloneNotSupportedException e){
					throw new InternalError(e.toString());
				}
		}
		
	}
	
	String Name;
	String CartridgeName;
	List<String> atsl;

	GunPerformance basePerformance;
	FireMode mode;
	
	
	public InfoFrame(String name, String cn, List<String>Slot,GunPerformance perf,FireMode fm) {
		Name = name;
		atsl = Slot;
		CartridgeName = cn;
		basePerformance = perf;
		mode = fm;
	}
	
	public String getName() {
		return Name;
	}
	
	public GunPerformance getPerforMance() {
		return (GunPerformance)basePerformance.clone();
	}

	public String getCartridgeName() {
		return CartridgeName;
	}
	
	public List<String> getAttchmentSlot() {
		return atsl; 
	}

	
	public FireMode getFireMode() {
		return (FireMode)mode.clone();
	}
}
