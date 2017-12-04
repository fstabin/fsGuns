package inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public interface InventoryHanger {
	String getInventoryName();
	
	Inventory onPreInventoryOpen(HumanEntity who);
	
	void onClicked(InventoryClickEvent event);
	
	void onDraged(InventoryDragEvent event);

	void onClose(InventoryCloseEvent event);
}
