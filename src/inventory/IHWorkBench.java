package inventory;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import fsGuns.InventoryUpdater;
import fsGuns.util;
import fsGuns.info.InfoAccessory;
import fsGuns.info.InfoFrame;
import fsGuns.info.Info_Manager;
import fsGuns.item.ItemAccessory;
import fsGuns.item.ItemGun;
import fsGuns.item.ItemMagazine;

public class IHWorkBench implements InventoryHanger{
	static public String Name = "faGunsWorkBench";
	
	static int iFrameloc = 1;
	static int iRow = 9;
	static int iMagloc = iRow * 2;
	static int iInvSize = iRow * 3;
	
	Plugin plugin;
	Info_Manager info;
	InventoryUtil iutil;
	
	public IHWorkBench(Plugin pl,Info_Manager im,InventoryUtil iu) {
		info = im;
		plugin = pl;
		iutil = iu;
	}

	private ItemStack createGunStatsItem(ItemGun ig) {
		ItemStack is = new ItemStack(Material.ENDER_PEARL);
		ItemMeta meta = is.getItemMeta();
		InfoFrame info = ig.getInfo();
		meta.setDisplayName(info.getName());
		meta.setLore(Arrays.asList(
				util.SGT_Cartridge + ":" + info.getCartridgeName()
				));
		is.setItemMeta(meta);
		return is;
	}
	
	private ItemStack createAccessoryStatsItem(InfoAccessory info) {
		ItemStack is = new ItemStack(Material.ENDER_PEARL);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(info.getName());
		meta.setLore(Arrays.asList(
				util.SGT_Slot + ":" + info.getSlotName()
				));
		is.setItemMeta(meta);
		return is;
	}
	
	private ItemStack createAccessoryStatsItem(String sslot) {
		ItemStack is = new ItemStack(Material.ENDER_PEARL);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(" ");
		meta.setLore(Arrays.asList(
				util.SGT_Slot + ":" + sslot
				));
		is.setItemMeta(meta);
		return is;
	}
	
	//cf 0 = pickup, 1 = place, 2 = swap_with
	private boolean onChangeGunFrame(int cf, Inventory inv, ItemStack cur, Player pl) {
		//銃アイテム取り除き時
		if(cf == 0 || cf == 2) {
			inv.setItem(0, null);
			for(int i = iMagloc;i <iInvSize;i++) {
				ItemStack is = inv.getItem(i);
				if(is != null) {
					inv.setItem(i, null);
				}
				inv.setItem(i - 9, null);
			}
		}
		//銃アイテムセット時
		if(cf == 1 || cf == 2) {
			if(cur != null) {
				ItemGun gi = ItemGun.createItemGun(cur, info);
				if(gi == null) return true;
				int i = 0;
				ItemStack mag = gi.createMagazine(info);
				if(mag != null) {
					inv.setItem(i + iMagloc - 9, createAccessoryStatsItem(gi.getInfoMagazine()));
					inv.setItem(i + iMagloc, mag);
				}else {
					inv.setItem(i + iMagloc - 9, createAccessoryStatsItem("Magazine"));
				}
				InfoAccessory info;
				for(i = 0;i < gi.getAccessorySlotCount();i++) {
					info = gi.getInfoAccessory(i);
					if(info != null) {
						ItemAccessory ac = new ItemAccessory(info);
						inv.setItem(i + 1 + iMagloc - 9, createAccessoryStatsItem(info));
						inv.setItem(i + 1 + iMagloc, ac.createItemStack());
					}else {
						inv.setItem(i + 1 + iMagloc - 9, createAccessoryStatsItem(gi.getInfo().getAttchmentSlot().get(i)));
					}
				}
				inv.setItem(0, createGunStatsItem(gi));
			}
		}
		return false;
	}
	
	public boolean onChangeGunAccessory(int cf, int rslot,Inventory inv, ItemStack cur, Player pl) {
		ItemStack gun = inv.getItem(iFrameloc);
		boolean changed = false;
		if(gun == null)return true;
		ItemGun gi = ItemGun.createItemGun(gun, info);
		if(gi != null) {
			if(cf == 0 || cf == 2) {
				if(rslot == iMagloc) {
					if(!gi.setMagazine(null))return true;
					changed = true;
				}
				else if(iMagloc < rslot && rslot < iInvSize){
					if(!gi.setAccessory(rslot - iMagloc - 1, null))return true;
					changed = true;
				}
			}
			if(cf == 1 || cf == 2) {
				if(cur != null) {
					if(rslot == iMagloc) {
						ItemMagazine mag = ItemMagazine.createItemMagazine(cur, info);
						if(mag == null)return true;
						if(!gi.setMagazine(mag)) return true;
						changed = true;
					}
					else if(iMagloc < rslot && rslot < iInvSize){
						ItemAccessory acc = ItemAccessory.createItemAccessory(cur, info);
						if(acc == null) return true;
						if(!gi.setAccessory(rslot - iMagloc - 1, acc)) return true;
						changed = true;
					}
				}
			}
		}
		if(changed)inv.setItem(iFrameloc, gi.createItemStack());
		return false;
	}

	@Override
	public String getInventoryName() {
		return Name;
	}

	@Override
	public Inventory onPreInventoryOpen(HumanEntity who) {
		Inventory inv = Bukkit.createInventory(null, 9 * 3, Name);
		inv.setItem(8, iutil.isClose);
		inv.setMaxStackSize(1);
		return inv;
	}

	@Override
	public void onClicked(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		HumanEntity who = event.getWhoClicked();
		if(who instanceof Player) {
			Player pl = (Player)who;
			(new InventoryUpdater(pl)).runTask(plugin);
			ItemStack is = event.getCurrentItem();
			if(is != null) {
			//左クリ+クローズボタン
				if(event.getClick() == ClickType.LEFT && is.isSimilar(iutil.isClose)) {
					event.setCancelled(true);
					who.closeInventory();
					return;
				}
			}
			int rslot = event.getRawSlot();
			if(0 <= rslot && rslot < 9 * 3) {
				int cf = -1;
				switch(event.getAction()){
				case PICKUP_ALL:
				case PICKUP_HALF:
				case PICKUP_ONE:
				case PICKUP_SOME:
					cf = 0;
					break;
				case PLACE_ALL:
				case PLACE_ONE:
				case PLACE_SOME:
					cf = 1;
					break;
				case SWAP_WITH_CURSOR:
					cf = 2;
					break;
				default:
					event.setCancelled(true);
					return;
				}
				if(cf >= 0) {
					if(rslot == 1) {
						if(onChangeGunFrame(cf, inv, event.getCursor(), pl)){
							event.setCancelled(true);
						}
						return;
					}
					else if(9 * 2 <= rslot && rslot < 9 * 3) {
						if(onChangeGunAccessory(cf, rslot ,inv, event.getCursor(), pl)){
							event.setCancelled(true);
						}
						return;
					}
				}
				event.setCancelled(true);
				return;
			}else {
				if(event.isShiftClick()) {
					event.setCancelled(true);
					return;
				}
			}
		}
	}

	@Override
	public void onDraged(InventoryDragEvent event) {
		Set<Integer>e =  event.getRawSlots();
		for(int i = 0;i < iInvSize;i++) {
			if(e.contains(i)) {
				event.setCancelled(true);
				return;
			}
		}
	}

	@Override
	public void onClose(InventoryCloseEvent event) {
		HumanEntity pl  = event.getPlayer();
		Inventory invPl = pl.getInventory();
		ItemStack item = event.getView().getTopInventory().getItem(iFrameloc);
		if(item != null) {
			Map<Integer, ItemStack>exitem = invPl.addItem(item); 
			for(Map.Entry<Integer, ItemStack> e:exitem.entrySet()) {
				World wor = pl.getLocation().getWorld();
				if(wor != null) {
					wor.dropItemNaturally(pl.getEyeLocation(), e.getValue());
				}
			}
		}
	}
	
}
