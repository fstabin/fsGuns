package inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryUtil {

	public ItemStack isPrev = null;
	public ItemStack isNext = null;
	public ItemStack isClose = null;
	public ItemStack isReturn = null;
	
	public ItemStack isHeader = null;
	public ItemStack isBack = null;
	
	public InventoryUtil() {
		//ItemStack is = null;
				ItemMeta meta = null;
				
				isPrev = new ItemStack(Material.SLIME_BALL);
				meta = isPrev.getItemMeta();
				meta.setDisplayName("Prev");
				isPrev.setItemMeta(meta);
				
				isNext = new ItemStack(Material.SLIME_BALL);
				meta = isNext.getItemMeta();
				meta.setDisplayName("Next");
				isNext.setItemMeta(meta);
				
				isClose = new ItemStack(Material.SLIME_BALL);
				meta = isClose.getItemMeta();
				meta.setDisplayName("Close");
				isClose.setItemMeta(meta);
				
				isReturn = new ItemStack(Material.SLIME_BALL);
				meta = isReturn.getItemMeta();
				meta.setDisplayName("Return");
				isReturn.setItemMeta(meta);
				
				isHeader = new ItemStack(Material.STAINED_GLASS_PANE);
				meta = isHeader.getItemMeta();
				meta.setDisplayName("-");
				isHeader.setItemMeta(meta);
				isHeader.setDurability((short)3);
				
				isBack = new ItemStack(Material.STAINED_GLASS_PANE);
				meta = isBack.getItemMeta();
				meta.setDisplayName("-");
				isBack.setItemMeta(meta);
				isBack.setDurability((short)15);
	}

}
