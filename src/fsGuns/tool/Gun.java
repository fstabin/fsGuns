package fsGuns.tool;

import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fsGuns.util;
import fsGuns.info.InfoAccessory;
import fsGuns.info.InfoBullet;
import fsGuns.info.Info_Manager;
import fsGuns.info.helper.GunPerformance;
import fsGuns.info.InfoFrame;

public class Gun {
	ItemStack is;
	ItemMeta meta;
	List<String> lore;
	
	static double tick_per_min = (20 * 60);
	
	boolean stat = false;
	
	double afv = 1.0;
	
	String ItemName;
	String BulletFullName;
	GunPerformance perf;
	int MagazineSize = 100;
	int Remaining = 100;
	
	Random rnd;
	
	int fireable = 0;

	private boolean parseItemName(String itemfullname) {
		int lra = itemfullname.lastIndexOf(ChatColor.GRAY + "<");
		int lrb = itemfullname.lastIndexOf('>');
		if(0 <= lra && lra + 1 < lrb) {
			ItemName = itemfullname.substring(0, lra);
			String ds = itemfullname.substring(lra + + (ChatColor.GRAY + "<").length(), lrb);
			String str[] = ds.split("/", 2);
			if(str != null) {
				if(str.length >= 2) {
					Remaining =  Integer.parseInt(str[0]);
					MagazineSize =  Integer.parseInt(str[1]);
					return true;
				}
			}
		}
		return false;
	}
		
	private boolean parseItemLore(Info_Manager mng) {
		if(lore.size() >= 4) {
			if(lore.get(0).equalsIgnoreCase(util.ST_fsGunsGun)) {
				String str[] = lore.get(1).split(":",2);
				String ss = null;
				if(str[0].equalsIgnoreCase("BulletType")) {
					BulletFullName = str[1];
					InfoBullet ib = mng.getBullet(BulletFullName);
					str = lore.get(2).split(":",2);
					if(str[0].equalsIgnoreCase("Frame")) {
						InfoFrame f = mng.getFrame(str[1]);
						if(f != null) {
							switch(f.getFireMode().type){
								case FULLAUTO: fireable = -1;break;
								case SEMIAUTO: fireable = 1;break;
								case BURST:fireable = f.getFireMode().MaxFireCount;break;
								default: fireable = -1;break;
							}
							perf = f.getPerforMance();
							if(ib != null) {
								ib.culcGunPerformance(perf);
							}
							str = lore.get(3).split(":",2);
							if(str[0].equalsIgnoreCase("Accessory")) {
								for(int i = 4;i < lore.size();i++) {
									ss = lore.get(i).substring(1);
									if(ss.length() > 1) {
										InfoAccessory ac = mng.getAccessory(ss);
										if(ac != null)ac.culcGunPerformance(perf);
									}
								}
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public Gun(Info_Manager mng,ItemStack items) {
		rnd = new Random();
		is = items;
		if(is.getAmount() == 1) {
			meta = is.getItemMeta();
			if(meta != null) {
				String ifullname = meta.getDisplayName();
				lore = meta.getLore();
				if((ifullname != null) && (lore != null)) {
					if(parseItemName(ifullname))if(parseItemLore(mng))stat = true;
				}
			}
		}
	}

	public boolean getStat() {
		return stat;
	}
	
	public Random getRandom() {
		return rnd;
	}
	
	public boolean isEmpty() {
		return (Remaining == 0 || fireable == 0);
	}

	public String getItemName() {
		return ItemName;
	}
	
	public String getBulletName() {
		return BulletFullName;
	}
	
	public GunPerformance getGunPerformance() {
		return perf;
	}
	
	public void Fired(int launched, boolean consumed) {		
		if(fireable != -1)fireable -= launched;
		if(consumed) {
			Remaining -= launched;
			meta.setDisplayName(ItemName + ChatColor.GRAY  + "<" + String.valueOf(Remaining) + "/" + String.valueOf(MagazineSize) + ">");
			is.setItemMeta(meta);
		}
		return;
	}
	
	public int calcThisTickFireCount() {
		afv += (perf.RPM / tick_per_min);
		double result = Math.floor(afv);
		afv -= result;
		return Math.min(Math.min((int)result, Remaining), ((fireable == -1) ? Integer.MAX_VALUE : fireable));
	}
	
	boolean coolDownnow = false;

	public void setCoolDown(boolean b) {
		coolDownnow = b;
	}
	
	public boolean getCoolDownNow() {
		return coolDownnow;
	}
	
	public boolean calcCoolDownNow() {
		afv += (perf.RPM / tick_per_min);
		double result = Math.floor(afv);
		afv -= result;
		return (int)result < 1;
	}
}
