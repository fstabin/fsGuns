package fsGuns.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fsGuns.util;
import fsGuns.info.Info_Manager;
import fsGuns.info.InfoAccessory;
import fsGuns.info.InfoBullet;
import fsGuns.info.InfoFrame;
import fsGuns.info.InfoMagazine;

public class ItemGun implements ItemSource {
	String ItemName;
	
	InfoFrame prop;
	
	InfoMagazine Mag;
	List<InfoAccessory> Acce;
	InfoBullet Bullet;
	
	int Remaining = 0;
	
	static public ItemGun createItemGun(ItemStack is, Info_Manager am) {
		ItemMeta meta = is.getItemMeta();
		if(meta == null)return null;
		List<String>lore = meta.getLore();
		String fullName = meta.getDisplayName();
		if(lore == null || fullName == null) return null;
		
		//ñºëOâêÕ
		int lra = fullName.lastIndexOf(ChatColor.GRAY + "<");
		int lrb = fullName.lastIndexOf('>');
		String itemName = null;
		int remaining = 0;
		if(0 <= lra && lra + 1 < lrb) {
			itemName = fullName.substring(0, lra);
			String ds = fullName.substring(lra + (ChatColor.GRAY + "<").length(), lrb);
			String str[] = ds.split("/", 2);
			if(str != null) {
				if(str.length >= 2) {
					remaining =  Integer.parseInt(str[0]);
				}
			}
		}else {
			itemName = fullName;
		}
		
		//loreâêÕ
		if(lore.size() < 5)return null;
		if(!lore.get(0).equals(util.ST_fsGunsGun))return null;
		String str[] = null; 
		String ss = null;
		
		InfoBullet infob = null;
		str = lore.get(1).split(":",2);
		if(str.length >= 2) {
			if(!str[0].equals("BulletType"))return null;
			infob = am.getBullet(str[1]);
		}
		
		str = lore.get(2).split(":",2);
		if(str.length < 2)return null;
		if(!str[0].equals("Frame"))return null;
		InfoFrame infof = am.getFrame(str[1]);
		if(infof == null)return null;
		
		str = lore.get(3).split(":",2);
		if(str.length < 2)return null;
		if(!str[0].equals("Accessory"))return null;
		InfoMagazine infom = am.getMagazine(lore.get(4).substring(1));
		List<InfoAccessory> lia = new ArrayList<InfoAccessory>();
		for(int i = 5;i < lore.size();i++) {
			ss = lore.get(i).substring(1);
			if(ss.length() > 1)lia.add(am.getAccessory(ss));
			else lia.add(null);
		}
		return new ItemGun(itemName ,infof, infom, lia, infob, remaining);
	}
	
	static public ItemGun createItemGun(String str, Info_Manager am) {
		InfoFrame f =  am.getFrame(str);
		if(f == null)return null;
		else return new ItemGun(f);
	}
	
	public ItemGun(String name, InfoFrame a,InfoMagazine m, List<InfoAccessory> l, InfoBullet b, int remaining) {
		ItemName = name;
		prop = a;
		Mag = m;
		Acce = l;
		Bullet = b;
		Remaining = remaining;
	}
	
	public ItemGun(InfoFrame a,InfoMagazine m, List<InfoAccessory> l) {
		ItemName = a.getName();
		prop = a;
		Mag = m;
		Acce = l;
		Bullet = null;
		Remaining = 0;
	}
	
	public ItemGun(InfoFrame a) {
		ItemName = a.getName();
		prop = a;
		Mag = null;
		InfoAccessory iaa[] = new InfoAccessory[prop.getAttchmentSlot().size()];
		Acce = Arrays.asList(iaa);
		Bullet = null;
		Remaining = 0;
	}
		
	public void setItemName(String name) {
		ItemName = name;
	}
	
	public String getItemName() {
		return ItemName;
	}
	
	public InfoBullet getInfoBullet() {
		return Bullet;
	}
	
	public InfoFrame getInfo() {
		return prop;
	}
	
	public int getRemaining() {
		return Remaining;
	}
	
	public InfoMagazine getInfoMagazine() {
		return Mag;
	}
	
	public boolean setMagazine(ItemMagazine mi) {
		if(mi != null) {
			if(mi.getInfo().getCartridgeName().equals(prop.getCartridgeName())) {
				Mag = mi.getInfo();
				Bullet = mi.getInfoBullet();
				Remaining = mi.getRemaining();
				return true;
			}
			return false;
		}
		Mag = null;
		Bullet = null;
		Remaining = 0;
		return true;
	}
	
	public int getAccessorySlotCount() {
		return Acce.size();
	}
	
	public InfoAccessory getInfoAccessory(int index) {
		return 	Acce.get(index);
	}
	
	public boolean setAccessory(int index,ItemAccessory ai) {
		if(index >= prop.getAttchmentSlot().size())return false;
		if(ai == null) {
			Acce.set(index, null);
			return true;
		}else {
			if(ai.getInfo().getSlotName().equals(prop.getAttchmentSlot().get(index))) {
				Acce.set(index, ai.getInfo());
				return true;
			}else {
				return false;
			}
		}
	}
	
	public ItemStack createMagazine(Info_Manager am) {
		if(Mag != null) {
			ItemMagazine im = new ItemMagazine(Mag);
			if(Remaining > 0) {
				im.setInfoBullet(Bullet);
				im.setRemaining(Remaining);
			}
			return im.createItemStack();
		}
		return null;	
	}
	
	public ItemStack createItemStack() {
		ItemStack is = new ItemStack(Material.STICK);
		ItemMeta meta = is.getItemMeta();
		if(meta != null) {
			List<String>lore = new ArrayList<String>();
			lore.add(util.ST_fsGunsGun);
			if(Bullet != null)lore.add(util.SGT_BulletType + ":" + Bullet.getName());
			else lore.add(util.SGT_BulletType + ":");
			lore.add(util.SGT_Frame + ":" + prop.getName());
			lore.add(util.SGT_Accessory + ":");
			if(Mag != null)lore.add("-" + Mag.getName());
			else lore.add("-");
			for(InfoAccessory ac: Acce) {
				if(ac != null)lore.add("-" + ac.getName());
				else lore.add("-");
			}
			String iname = ItemName;
			if(Remaining > 0) {
				iname += ChatColor.GRAY + "<" + String.valueOf(Remaining);
				if(Mag != null)iname += "/" + String.valueOf(Mag.getCapacity()) + ">";
			}
			meta.setDisplayName(iname);
			meta.setLore(lore);
			is.setItemMeta(meta);
			return is;
		}
		return null;
	}
}
