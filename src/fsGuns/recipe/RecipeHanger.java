package fsGuns.recipe;

import org.bukkit.inventory.CraftingInventory;

public interface RecipeHanger {
	boolean onCraft(CraftingInventory ci);
	
	void onPreCraft(CraftingInventory ci);
}
