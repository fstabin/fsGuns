package fsGuns.item;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fsGuns.util;
import fsGuns.info.InfoBullet;
import fsGuns.info.Info_Manager;
import fsGuns.info.InfoMagazine;

public class ItemMagazine implements ItemSource {
	InfoMagazine prop;
	InfoBullet bullet = null;
	int Remaining = 0;
	
	public static ItemMagazine createItemMagazine(ItemStack is, Info_Manager am) {
		ItemMeta meta = is.getItemMeta();
		if(meta == null)return null;
		List<String>lore = meta.getLore();
		if(lore == null)return null;
		if(lore.size() < 4)return null;
		if(!lore.get(0).equals(util.ST_fsGunsMagazine))return null;
		
		String str[] = lore.get(1).split(":",2);
		if(str.length != 2)return null;
		if(!str[0].equals(util.SGT_Name))return null;
		InfoMagazine info = am.getMagazine(str[1]);
		if(info == null)return null;
		
		str = lore.get(3).split(":",2);
		if(!str[0].equals(util.SGT_BulletType))return null;
		InfoBullet ifb = null;
		if(str.length == 2) {
			ifb = am.getBullet(str[1]);
		}
		
		if(lore.size() == 5) {
			if(ifb == null)return null;
			str = lore.get(4).split(":",2);
			if(str.length != 2)return null;
			if(!str[0].equals(util.SGT_Remaining))return null;
			String str2[] = str[1].split("/",2);
			return new ItemMagazine(info,ifb, Integer.parseInt(str2[0]));
		}
		
		return new ItemMagazine(info);
	}
	
	public ItemMagazine createItemMagazine(String str, Info_Manager am) {
		InfoMagazine info = am.getMagazine(str);
		if(info == null)return null;
		return new ItemMagazine(info);
	}
	
	public ItemMagazine(InfoMagazine im) {
		prop = im;	
	}
	
	public ItemMagazine(InfoMagazine im, InfoBullet ib, int remaining) {
		prop = im;
		bullet = ib;
		Remaining = remaining;
	}
	
	public InfoMagazine getInfo() {
		return prop;
	}
	
	public void setInfoBullet(InfoBullet ibullet) {
		bullet = ibullet;
	}
	
	public InfoBullet getInfoBullet() {
		return bullet;
	}

	public void setRemaining(int r) {
		Remaining = r;
	}
	
	public int getRemaining() {
		return Remaining;
	}
		
	public ItemStack createItemStack() {
		ItemStack is = new ItemStack(Material.IRON_INGOT);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(prop.getName());
		List<String> lore = new ArrayList<String>();
		lore.add(util.ST_fsGunsMagazine);
		lore.add(util.SGT_Name + ":" + prop.getName());
		lore.add(util.SGT_Cartridge + ":" + prop.getCartridgeName());
		if(bullet != null)lore.add(util.SGT_BulletType + ":" + bullet.getName());
		else lore.add(util.SGT_BulletType + ":");
		if(Remaining > 0) {
			lore.add(util.SGT_Remaining + ":" + String.valueOf(Remaining)+ "/" + String.valueOf(prop.getCapacity()));
		}
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
}
