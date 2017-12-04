package fsGuns;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.event.block.*;

public class ShooterListener implements Listener  {
	public JavaPlugin owner;
	public ShooterManager smng;
	
	public ShooterListener(JavaPlugin jp, ShooterManager m) {
		owner = jp;
		smng = m;
	}
	
	private boolean stopFire(Player pl) {
		if(smng.isOnShooting(pl)){
			smng.stopFire(pl);
			return true;
		}
		return false;
	}
	
	//ClickID 0 = left,1 = right
	public boolean onClick(int ClickID ,ItemStack it, Player pl) {
		if(it.getType() == Material.STICK) {
			if(ClickID == 0) {
				if(pl.isSneaking()) {
					if(stopFire(pl)) {
						return true;
					}
					if(smng.addShooter(pl, it)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@EventHandler
	public void onIntract(PlayerInteractEvent event) {
		if(event.useItemInHand() == Result.DENY)return;
		Action act =  event.getAction();
		ItemStack it = event.getItem();
		if(it == null)return;
		Player pl = event.getPlayer();
		if(pl == null) return;
		
		if(act == Action.LEFT_CLICK_AIR || act == Action.LEFT_CLICK_BLOCK) {
			if(onClick(0, it, pl)) {
				event.setUseItemInHand(Result.DENY);
				event.setUseInteractedBlock(Result.DENY);
			}
		}
		else if(act == Action.RIGHT_CLICK_AIR || act == Action.RIGHT_CLICK_BLOCK){
			if(onClick(1, it, pl)) {
				event.setUseItemInHand(Result.DENY);
				event.setUseInteractedBlock(Result.DENY);
			}
		}
	}
		
	@EventHandler
	public void onIntractEntity(PlayerInteractAtEntityEvent Pi) {
		if(Pi.isCancelled())return;
		Player pl = Pi.getPlayer();
		if(pl == null)return;
		PlayerInventory inv = pl.getInventory() ;
		if(inv == null)return;
		ItemStack it;
		switch(Pi.getHand()) {
		case HAND:
			it = inv.getItemInMainHand();
			break;
		case OFF_HAND:
			it = inv.getItemInOffHand();
			break;
		case HEAD:
			it = inv.getHelmet();
			break;
		case CHEST:
			it = inv.getChestplate();
			break;
		case LEGS:
			it = inv.getLeggings();
			break;
		case FEET:
			it = inv.getBoots();
			break;
		default:
			return;
		}
		if(it == null)return;
		Pi.setCancelled(onClick(1, it, pl));
	}
	
	@EventHandler
	public void onItemHeld(PlayerItemHeldEvent event) {
		Player pl = event.getPlayer();
		if(pl != null)stopFire(event.getPlayer());
	}
	
	@EventHandler
	public void onSwapHand(PlayerSwapHandItemsEvent event) {
		Player pl = event.getPlayer();
		if(pl != null)stopFire(event.getPlayer());
	}
	
	@EventHandler
	public void onToggleSneak(PlayerToggleSneakEvent event) {
		Player pl = event.getPlayer();
		if(pl != null)stopFire(event.getPlayer());
	}
	
}
