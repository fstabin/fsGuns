package fsGuns.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import fsGuns.info.Info_Manager;
import fsGuns.item.ItemGun;
import fsGuns.item.ItemMagazine;

public class RHAttachMag implements RecipeHanger{
	Info_Manager ma;
	
	public RHAttachMag(Info_Manager m){
		ma = m;
	}

	@Override
	public boolean onCraft(CraftingInventory ci) {
		return true;
	}

	@Override
	public void onPreCraft(CraftingInventory ci) {
		ItemStack is[] = ci.getMatrix();
		ItemMagazine im = null;
		ItemGun ig = null;
		for(int i = 0; i < is.length; i++) {
			if(is[i] != null) {
				if(is[i].getType() == Material.IRON_INGOT) {
					im = ItemMagazine.createItemMagazine(is[i], ma);
				}
				if(is[i].getType() == Material.STICK) {
					ig = ItemGun.createItemGun(is[i], ma);
				}
			}
		}
		if(im != null && ig != null) {
			if(ig.getInfoMagazine() == null) {
				if(ig.setMagazine(im)) {	
					ci.setResult(ig.createItemStack());
					return;
				}
			}
		}
		ci.setResult(null);
		return;
	}

}
