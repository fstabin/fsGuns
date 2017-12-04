package fsGuns.item;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fsGuns.util;
import fsGuns.info.InfoBullet;
import fsGuns.info.Info_Manager;

public class ItemBullet implements ItemSource {
	InfoBullet prop;
	
	static public ItemBullet createItemBullet(ItemStack is, Info_Manager am) {
		ItemMeta meta = is.getItemMeta();
		if(meta == null) return null;
		List<String>lore = meta.getLore();
		if(lore == null) return null;
		if(lore.size() < 2) return null;
		
		if(!lore.get(0).equals(util.ST_fsGunsBullet))return null;
		String str[] = lore.get(1).split(":",2);
		if(str.length != 2)return null;
		if(!str[0].equals(util.SGT_Name))return null;
		InfoBullet info = am.getBullet(str[1]);
		if(info == null)return null;
		return new ItemBullet(info);
	}
	
	static public ItemBullet createItemBullet(String str, Info_Manager am) {
		InfoBullet info = am.getBullet(str);
		if(info == null)return null;
		return new ItemBullet(info);
	}

	public ItemBullet(InfoBullet i) {
		prop = i;
	}
	
	public InfoBullet getInfo() {
		return prop;
	}
	
	public ItemStack createItemStack() {
		ItemStack is = new ItemStack(Material.GOLD_NUGGET);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(prop.getName());
		List<String> lore =  Arrays.asList(util.ST_fsGunsBullet, util.SGT_Name + ":" + prop.getName(), util.SGT_Cartridge + ":" + prop.getCartridgeName());
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
}
