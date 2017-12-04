package fsGuns.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import fsGuns.info.InfoBullet;
import fsGuns.info.Info_Manager;
import fsGuns.item.ItemBullet;
import fsGuns.item.ItemMagazine;

public class RHLoadBullet implements RecipeHanger{
	Info_Manager ma;
	
	public RHLoadBullet(Info_Manager m) {
		ma = m;
	}

	@Override
	public boolean onCraft(CraftingInventory ci) {
		ItemStack is[] = ci.getMatrix();
		ItemMagazine im = null;
		ItemBullet ib = null;
		ItemStack ibullet = null;
		int amountb = 0;
		for(int i = 0; i < is.length; i++) {
			if(is[i] != null) {
				if(is[i].getType() == Material.IRON_INGOT && is[i].getAmount() == 1) {
					im = ItemMagazine.createItemMagazine(is[i], ma);
				}
				else if(is[i].getType() == Material.GOLD_NUGGET) {
					ibullet = is[i];
					ib = ItemBullet.createItemBullet(is[i], ma);
					amountb = is[i].getAmount();
				}
			}
		}
		//’e‚ðŒ¸‚ç‚·
		if(im != null && ib != null) {
			int used = Math.min(amountb, im.getInfo().getCapacity() - im.getRemaining());
			ibullet.setAmount(amountb - used + 1);
			im.setRemaining(im.getRemaining() + used);
			im.setInfoBullet(ib.getInfo());
			ci.setResult(im.createItemStack());
			return true;
		}
		return false;
	}

	@Override
	public void onPreCraft(CraftingInventory ci) {
		ItemStack is[] = ci.getMatrix();
		ItemMagazine im = null;
		ItemBullet ib = null;
		int amountb = 0;
		for(int i = 0; i < is.length; i++) {
			if(is[i] != null) {
				if(is[i].getType() == Material.IRON_INGOT && is[i].getAmount() == 1) {
					im = ItemMagazine.createItemMagazine(is[i], ma);
				}
				if(is[i].getType() == Material.GOLD_NUGGET) {
					ib = ItemBullet.createItemBullet(is[i], ma);
					amountb = is[i].getAmount();
				}
			}
		}
		if(im != null && ib != null) {
			InfoBullet old = im.getInfoBullet();
			if(old == ib.getInfo() || (im.getRemaining() <= 0 && ib.getInfo().getCartridgeName().equals(im.getInfo().getCartridgeName()))) {
				int used = Math.min(amountb, im.getInfo().getCapacity() - im.getRemaining());
				if(used > 0) {
					im.setRemaining(im.getRemaining() + used);
					im.setInfoBullet(ib.getInfo());
					ci.setResult(im.createItemStack());
					return;
				}
			}
		}
		ci.setResult(null);
	}

}
