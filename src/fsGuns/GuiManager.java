package fsGuns;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import fsGuns.info.Info_Manager;
import inventory.IHBrowser;
import inventory.IHWorkBench;
import inventory.InventoryHanger;
import inventory.InventoryUtil;

public class GuiManager {
	
	InventoryUtil iutil;
	IHBrowser browser;
	
	Map<String, InventoryHanger> mrec;
	
	public GuiManager(Plugin pl, Info_Manager im) {
		mrec = new HashMap<String, InventoryHanger>();
		iutil = new InventoryUtil();
		this.addInventoryHanger(browser = new IHBrowser(iutil, im));
		this.addInventoryHanger(new IHWorkBench(pl, im, iutil));
	}
	
	public void addInventoryHanger(InventoryHanger ih) {
		mrec.put(ih.getInventoryName(), ih);
	}
	
	public InventoryHanger getInventoryHanger(String InventoryName) {
		return mrec.get(InventoryName);
	}

	public boolean openInventory(HumanEntity who, String inventoryName) {
		InventoryHanger ih = getInventoryHanger(inventoryName);
		if(ih != null) {
			Inventory inv = ih.onPreInventoryOpen(who);
			if(inv != null) {
				who.openInventory(inv);
				return true;
			}
		}
		return false;
	}
	
}
