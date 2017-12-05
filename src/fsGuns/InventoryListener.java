package fsGuns;

import org.bukkit.Material;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

import fsGuns.info.Info_Manager;
import fsGuns.recipe.RecipeHanger;
import fsGuns.recipe.RecipeHanger_Manager;
import inventory.InventoryHanger;

public class InventoryListener implements Listener {
	static int iFrameloc = 1;
	static int iRow = 9;
	static int iMagloc = iRow * 2;
	static int iInvSize = iRow * 3;
	
	Plugin plugin;
	InventoryManager manager;
	Info_Manager acmng;
	RecipeHanger_Manager rhmng;
	public InventoryListener(Plugin pl, InventoryManager gmng,Info_Manager ac,RecipeHanger_Manager rhm) {
		plugin = pl;
		manager = gmng;
		acmng = ac;
		rhmng = rhm;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		InventoryHanger ih = manager.getInventoryHanger(inv.getName());
		if(ih != null) {
			ih.onClicked(event);
		}
	}

	@EventHandler
	public void onInventoryDrag(InventoryDragEvent event) {
		Inventory inv = event.getInventory();
		InventoryHanger ih = manager.getInventoryHanger(inv.getName());
		if(ih != null) {
			ih.onDraged(event);
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		InventoryHanger ih = manager.getInventoryHanger(inv.getName());
		if(ih != null) {
			ih.onClose(event);
		}
	}
		
	@EventHandler
	public void onCraft(CraftItemEvent event){
		Recipe re = event.getRecipe();
		if(re != null && event.getResult() != Result.DENY && !event.isCancelled()) {
			if(re instanceof ShapelessRecipe) {
				ShapelessRecipe sr = (ShapelessRecipe)re;
				RecipeHanger rh = rhmng.getRecipeHanger(sr.getKey());
				if(rh != null) {
					//when probably cancel
					if((!event.isShiftClick()) && ((event.getCursor() != null) ? (event.getCursor().getType() != Material.AIR) : (false))) {
						event.setCancelled(true);
					}else {
						boolean b = rh.onCraft(event.getInventory());
						if(!b)event.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPreCraft(PrepareItemCraftEvent event){
		Recipe re = event.getRecipe();
		if(re instanceof ShapelessRecipe) {
			ShapelessRecipe sr = (ShapelessRecipe)re;
			RecipeHanger rh = rhmng.getRecipeHanger(sr.getKey());
			if(rh != null) {
				rh.onPreCraft(event.getInventory());
			}
		}
	}

}
