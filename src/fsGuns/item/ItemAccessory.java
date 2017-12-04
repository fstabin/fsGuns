package fsGuns.item;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fsGuns.util;
import fsGuns.info.InfoAccessory;
import fsGuns.info.Info_Manager;

public class ItemAccessory implements ItemSource {
	InfoAccessory prop;
	
	static public ItemAccessory createItemAccessory(ItemStack is, Info_Manager am) {
		ItemMeta meta = is.getItemMeta();
		if(meta == null)return null;
		List<String>lore = meta.getLore();
		if(lore == null)return null;
		if(lore.size() < 2)return null;
		if(!lore.get(0).equalsIgnoreCase(util.ST_fsGunsAccessory))return null;
		String str[] = lore.get(1).split(":",2);
		if(!str[0].equalsIgnoreCase(util.SGT_Name))return null;
		InfoAccessory info = am.getAccessory(str[1]);
		if(info == null)return null;
		return new ItemAccessory(info);
	}
	
	static public ItemAccessory createItemAccessory(String str, Info_Manager am) {
		InfoAccessory info = am.getAccessory(str);
		if(info == null)return null;
		return new ItemAccessory(info);
	}
	
	public ItemAccessory(InfoAccessory info) {
		prop = info;
	}
	
	public InfoAccessory getInfo() {
		return prop;
	}

	public ItemStack createItemStack() {
		ItemStack is = new ItemStack(Material.CLAY_BRICK);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(prop.getName());
		List<String> lore = Arrays.asList(util.ST_fsGunsAccessory, util.SGT_Name + ":" + prop.getName(), util.SGT_Slot + ":" + prop.getSlotName());
		im.setLore(lore);
		is.setItemMeta(im);
		return is;
	}
}
