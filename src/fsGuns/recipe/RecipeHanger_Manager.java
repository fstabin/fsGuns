package fsGuns.recipe;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.NamespacedKey;

public class RecipeHanger_Manager {
	
	Map<NamespacedKey, RecipeHanger> mrec;

	public RecipeHanger_Manager() {
		mrec = new HashMap<NamespacedKey, RecipeHanger>();
	}
	
	//key = "namespace:" + "name"
	public void addRecipieHanger(NamespacedKey key,RecipeHanger recipie) {
		mrec.put(key, recipie);
	}
	
	public RecipeHanger getRecipeHanger(NamespacedKey key) {
		return mrec.get(key);
	}
}
