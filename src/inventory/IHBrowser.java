package inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fsGuns.info.InfoAccessory;
import fsGuns.info.InfoBullet;
import fsGuns.info.InfoFrame;
import fsGuns.info.InfoMagazine;
import fsGuns.info.Info_Manager;
import fsGuns.item.ItemAccessory;
import fsGuns.item.ItemBullet;
import fsGuns.item.ItemGun;
import fsGuns.item.ItemMagazine;
import fsGuns.item.ItemSource;

public class IHBrowser implements InventoryHanger {
	InventoryUtil util;
	
	static public String Name = "fsGunsBrowser";
	
	public ItemStack isFrame = null;
	public ItemStack isAccessory = null;
	public ItemStack isBullet = null;
	
	Inventory browseTop;
	List<Inventory> browseFrame;
	List<Inventory> browseBullet;
	List<Inventory> browseAccessory;
	
	static int BrowseClose = 8;
	static int BrowseReturn = 6;
	static int BrowsePrev = 9 * 5;
	static int BrowseNext = 9 * 6 - 1;
	
	static int BrowseSize = 9 * 6;
	
	static int BrowseClientLT = 9;//left top
	static int BrowseClientRB = 9 * 5 - 1;//right bottom
	
	public IHBrowser(InventoryUtil iu,Info_Manager info) {
		util = iu;
		
		//create buttons (item)
		ItemMeta meta = null;
		
		isFrame = new ItemStack(Material.STICK);
		meta = isFrame.getItemMeta();
		meta.setDisplayName("Frame");
		isFrame.setItemMeta(meta);
		
		isAccessory = new ItemStack(Material.CLAY_BRICK);
		meta = isAccessory.getItemMeta();
		meta.setDisplayName("Accessory");
		isAccessory.setItemMeta(meta);
		
		isBullet = new ItemStack(Material.GOLD_NUGGET);
		meta = isBullet.getItemMeta();
		meta.setDisplayName("Bullet");
		isBullet.setItemMeta(meta);
		
		//create top page
		browseTop = Bukkit.createInventory(null, BrowseSize, Name);
		browseTop.setItem(9 * 2 + 3, isFrame);
		browseTop.setItem(9 * 2 + 4, isAccessory);
		browseTop.setItem(9 * 2 + 5, isBullet);
		SetMenu(browseTop, "Top", 0);
		FillInventory(browseTop);
		
		Reset(info);
	}

	public void Reset(Info_Manager info) {
		//create other pages
		browseFrame = new ArrayList<Inventory>();
		browseAccessory = new ArrayList<Inventory>();
		browseBullet = new ArrayList<Inventory>();
		
		Inventory inv = null;
		int i = BrowseClientRB + 1;
		for(Map.Entry<String, InfoFrame> a:info.getFrameEntrySet()) {
			if(i > BrowseClientRB) {
				i = BrowseClientLT;
				if(inv != null)FillInventory(inv);
				inv = Bukkit.createInventory(null, BrowseSize, Name);
				browseFrame.add(inv);
				SetMenu(inv, "Frame", browseFrame.size());
			}
			ItemSource s = new ItemGun(a.getValue());
			inv.setItem(i, s.createItemStack());
			i++;
		}
		if(inv != null)FillInventory(inv);
		
		i = BrowseClientRB + 1;
		for(Map.Entry<String, InfoAccessory> a:info.getAccessoryEntrySet()) {
			if(i > BrowseClientRB) {
				i = BrowseClientLT;
				if(inv != null)FillInventory(inv);
				inv = Bukkit.createInventory(null, BrowseSize, Name);
				browseAccessory.add(inv);
				SetMenu(inv, "Accessory", browseAccessory.size());
			}
			ItemSource s = null;
			if(a.getValue() instanceof InfoMagazine) {
				s = new ItemMagazine((InfoMagazine)a.getValue());
			}else {
				s = new ItemAccessory(a.getValue());
			}
			inv.setItem(i, s.createItemStack());
			i++;
		}
		if(inv != null)FillInventory(inv);
		
		i = BrowseClientRB + 1;
		for(Map.Entry<String, InfoBullet> a:info.getBulletEntrySet()) {
			if(i > BrowseClientRB) {
				i = BrowseClientLT;
				if(inv != null)FillInventory(inv);
				inv = Bukkit.createInventory(null, BrowseSize, Name);
				browseBullet.add(inv);
				SetMenu(inv, "Bullet", browseBullet.size());
			}
			ItemSource s = new ItemBullet(a.getValue());
			inv.setItem(i, s.createItemStack());
			i++;
		}
		if(inv != null)FillInventory(inv);
	}

	private void SetMenu(Inventory inv, String title, int page) {
		ItemStack is = null;
		ItemMeta meta = null;
				
		//set title item
		is = new ItemStack(Material.ENDER_PEARL);
		meta = is.getItemMeta();
		meta.setDisplayName(title);
		if(page > 0)meta.setLore(Arrays.asList(String.valueOf(page)));
		is.setItemMeta(meta);
		
		inv.setItem(0, is);
		
		//set button items
		inv.setItem(BrowsePrev, util.isPrev);
		inv.setItem(BrowseClose, util.isClose);
		inv.setItem(BrowseNext, util.isNext);
		inv.setItem(BrowseReturn, util.isReturn);
	}
	
	private void FillInventory(Inventory inv){
		for (int i = 9; i < BrowseSize - 9; i++) {
			if(inv.getItem(i) == null) {
				inv.setItem(i, util.isBack);
			}
		}
		for (int i = 0; i < 9; i++) {
			if(inv.getItem(i) == null) {
				inv.setItem(i, util.isHeader);
			}
		}
		for (int i = BrowseSize - 9; i < BrowseSize; i++) {
			if(inv.getItem(i) == null) {
				inv.setItem(i, util.isHeader);
			}
		}
	}
	
	public Inventory getBrowseTop() {
		return browseTop;
	}
	
	public Inventory getBrowsePage(String title, int page) {
		if(title.equals("Top")) {
			return browseTop;
		}else if(title.equals("Frame")) {
			if(page < 1 || browseFrame.size() < page)return null;
			return browseFrame.get(page - 1);
		}else if(title.equals("Accessory")) {
			if(page < 1 || browseAccessory.size() < page)return null;
			return browseAccessory.get(page - 1);
		}else if(title.equals("Bullet")) {
			if(page < 1 || browseBullet.size() < page)return null;
			return browseBullet.get(page - 1);
		}
		return null;
	}

	public boolean isClient(int pos) {
		return (BrowseClientLT <= pos && pos <= BrowseClientRB);
	}
	
	public boolean isMenu(int pos) {
		return (pos < BrowseClientLT || (BrowseClientRB < pos && pos < BrowseSize));
	}

	@Override
	public String getInventoryName() {
		return Name;
	}

	@Override
	public Inventory onPreInventoryOpen(HumanEntity who) {
		return browseTop;
	}

	@Override
	public void onClicked(InventoryClickEvent event) {
		int rawSlot = event.getRawSlot();
		//clicked out of browse
		if(rawSlot < 0 || BrowseSize <= rawSlot)return;
		
		//clicked browse
		event.setCancelled(true);
		String title = null;
		int page = 0;
		ItemStack it = event.getInventory().getItem(0);
		if(it != null) {
			//get page title
			title = it.getItemMeta().getDisplayName();
			List<String>l = it.getItemMeta().getLore();
			if(l != null)page = Integer.parseInt(l.get(0));
		}
		ItemStack is = event.getCurrentItem();
		if(is != null && title != null && event.getClick() == ClickType.LEFT) {
			if(isClient(rawSlot)) {
				if(is.isSimilar(util.isBack)) {
					
				}
				else if(is.isSimilar(this.isFrame)) {
					Inventory newInv = this.getBrowsePage("Frame", 1);
					if(newInv != null)event.getWhoClicked().openInventory(newInv);
				}
				else if(is.isSimilar(this.isAccessory)) {
					Inventory newInv = this.getBrowsePage("Accessory", 1);
					if(newInv != null)event.getWhoClicked().openInventory(newInv);
				}
				else if(is.isSimilar(this.isBullet)) {
					Inventory newInv = this.getBrowsePage("Bullet", 1);
					if(newInv != null)event.getWhoClicked().openInventory(newInv);
				}else {
					//give item
					event.getWhoClicked().getInventory().addItem(is);
				}
			}else if(this.isMenu(rawSlot)) {
				if(is.isSimilar(util.isPrev)) {
					Inventory newInv = this.getBrowsePage(title, page + 1);
					if(newInv != null)event.getWhoClicked().openInventory(newInv);
				}
				else if(is.isSimilar(util.isNext)) {
					Inventory newInv = this.getBrowsePage(title, page - 1);
					if(newInv != null)event.getWhoClicked().openInventory(newInv);
				}
				else if(is.isSimilar(util.isClose)) {
					event.getWhoClicked().closeInventory();
				}
				else if(is.isSimilar(util.isReturn)) {
					Inventory newInv = this.getBrowsePage("Top", 0);
					if(newInv != null)event.getWhoClicked().openInventory(newInv);
				}
			}
		}
		
	}

	@Override
	public void onDraged(InventoryDragEvent event) {
		//cancel when drag on browse area
		Set<Integer>e =  event.getRawSlots();
		for(int i = 0;i < BrowseSize;i++) {
			if(e.contains(i)) {
				event.setCancelled(true);
				return;
			}
		}
	}

	@Override
	public void onClose(InventoryCloseEvent event) {
		
	}
}
