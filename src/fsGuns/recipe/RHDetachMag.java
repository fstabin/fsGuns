package fsGuns.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import fsGuns.info.Info_Manager;
import fsGuns.item.ItemGun;

public class RHDetachMag implements RecipeHanger{
	Info_Manager ma;
	
	public RHDetachMag(Info_Manager m) {
		ma = m;
	}

	@Override
	public boolean onCraft(CraftingInventory ci) {
		ItemStack is[] = ci.getMatrix();
		ItemGun ig = null;
		ItemStack ia = null;
		for(int i = 0; i < is.length; i++) {
			if(is[i] != null) {
				if(is[i].getType() == Material.STICK&& is[i].getAmount() == 1) {
					ia = is[i];
					ig = ItemGun.createItemGun(is[i], ma);
				}
			}
		}
		if(ig != null) {
			ig.setMagazine(null);	
			ia.setAmount(ia.getAmount() + 1);
			ia.setItemMeta(ig.createItemStack().getItemMeta());
			return true;
		}
		ci.setResult(null);
		return false;
	}

	@Override
	public void onPreCraft(CraftingInventory ci) {
		ItemStack is[] = ci.getMatrix();
		ItemGun ig = null;
		for(int i = 0; i < is.length; i++) {
			if(is[i] != null) {
				if(is[i].getType() == Material.STICK && is[i].getAmount() == 1) {
					ig = ItemGun.createItemGun(is[i], ma);
				}
			}
		}
		if(ig != null) {
			ItemStack mag = ig.createMagazine(ma);
			if(mag != null) {
				ig.setMagazine(null);	
				ci.setResult(mag);
				return;
			}
		}
		ci.setResult(null);
	}

}
