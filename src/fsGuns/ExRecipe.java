package fsGuns;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fsGuns.info.Info_Manager;
import fsGuns.recipe.RHAttachMag;
import fsGuns.recipe.RHDetachMag;
import fsGuns.recipe.RHLoadBullet;
import fsGuns.recipe.RecipeHanger_Manager;

import org.bukkit.inventory.*;

public class ExRecipe {
	
	static public String RID_LoadBullet = "LoadBullet";
	static public String RID_AttachMag = "AttachMag";
	static public String RID_DetachMag = "DetachMag";
	
	static public void CreateRecipe(JavaPlugin plugin,Info_Manager im, RecipeHanger_Manager rm) {
	{
			//���V�s�̃L�[�쐬
			NamespacedKey key = new NamespacedKey(plugin, RID_LoadBullet);
			//�N���t�g��A�C�e���쐬
			ItemStack item = new ItemStack(Material.IRON_INGOT);
			
			ShapelessRecipe rc = new ShapelessRecipe(key, item);
			
			rc.addIngredient(1, Material.IRON_INGOT);
			rc.addIngredient(1, Material.GOLD_NUGGET);
			
			plugin.getServer().addRecipe(rc);	
			rm.addRecipieHanger(key, new RHLoadBullet(im));
		}
		
		{
			//���V�s�̃L�[�쐬
			NamespacedKey key = new NamespacedKey(plugin, RID_AttachMag);
			//�N���t�g��A�C�e���쐬
			ItemStack item = new ItemStack(Material.STICK);
			
			ShapelessRecipe rc = new ShapelessRecipe(key, item);
			
			rc.addIngredient(1, Material.IRON_INGOT);
			rc.addIngredient(1, Material.STICK);
			
			plugin.getServer().addRecipe(rc);	
			rm.addRecipieHanger(key, new RHAttachMag(im));
		}
		
		{
			//���V�s�̃L�[�쐬
			NamespacedKey key = new NamespacedKey(plugin, RID_DetachMag);
			//�N���t�g��A�C�e���쐬
			ItemStack item = new ItemStack(Material.IRON_INGOT);
			
			ShapelessRecipe rc = new ShapelessRecipe(key, item);
			
			rc.addIngredient(1, Material.STICK);
			
			plugin.getServer().addRecipe(rc);		
			rm.addRecipieHanger(key, new RHDetachMag(im));
		}
	}
}
