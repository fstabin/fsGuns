package fsGuns;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import fsGuns.ShooterListener;
import fsGuns.info.Info_Manager;
import fsGuns.recipe.RecipeHanger_Manager;
import inventory.IHBrowser;
import inventory.IHWorkBench;
import fsGuns.ExRecipe;

public class fsGunsPlugin extends JavaPlugin implements fsGunsAPI{
	private ShooterManager shooterManager;
	private Info_Manager accessoryManager;
	private GuiManager guiManager;
	private BulletManager bulletManager;
	private RecipeHanger_Manager rhMng;
	
	@Override 
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Predicate<String> hasPermission = (permission)->{
			if(!sender.hasPermission((String) permission)) {
				sender.sendMessage(ChatColor.RED + "権限が不足しています。[" + permission + "]");
				return false;
			}
			return true;
		};
		if(command.getName().contentEquals("fsGuns")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.YELLOW + "[fsGuns] version [" + this.getDescription().getVersion() + "]");
				return true;
			}
			else if(args[0].contentEquals(Commands.workbench)) {
				if(args.length == 1) {
					if (sender instanceof Player) {
						Player pl = (Player) sender;
						if(hasPermission.test(Permissions.command_workbench)){
							guiManager.openInventory(pl, IHWorkBench.Name);
						}
						return true;
				    }  
					sender.sendMessage(ChatColor.RED + "プレイヤーがゲーム内で実行してください！");
					return true;
				}
			}
			else if(args[0].contentEquals(Commands.browse)) {
				if(args.length == 1) {
					if (sender instanceof Player) {
						Player pl = (Player) sender;
						if(hasPermission.test(Permissions.command_browse)){
							guiManager.openInventory(pl, IHBrowser.Name);
						}
						return true;
				    }  
					sender.sendMessage(ChatColor.RED + "プレイヤーがゲーム内から実行してください！");
			        return true;
				}
			}
			else if(args[0].contentEquals(Commands.set_firemode)) {
				if(!hasPermission.test(Permissions.command_set_firemode))return true;
				Player pl = null;
				if (sender instanceof Player)pl = (Player) sender;
				if(args.length == 2 || args.length == 3) {
					Runnable usage_outer = () -> {
						sender.sendMessage(ChatColor.YELLOW + "USAGE:/fsGuns " + Commands.set_firemode + " <mode> <player>");
						sender.sendMessage(ChatColor.YELLOW + "mode = {limited | unlimited}");
					};
					if(args.length == 3) {
						pl = this.getServer().getPlayer(args[2]);
					}
					if(pl == null) {
						usage_outer.run();
						return true;
					}
					
					if(args[1].contentEquals("limited")) {
						if(pl.hasMetadata(this.getName() + ":fire_unlimited")) {
							pl.removeMetadata(this.getName() + ":fire_unlimited", this);
							sender.sendMessage("プレイヤー[" + pl.getName() + "]に射撃モード[limited]を適用しました");
						}
					}
					else if(args[1].contentEquals("unlimited")) {
						pl.setMetadata(this.getName() + ":fire_unlimited", new FixedMetadataValue(this, null));
						sender.sendMessage("プレイヤー[" + pl.getName() + "]に射撃モード[unlimited]を適用しました");						
					}else {
						usage_outer.run();
						return true;
					}
				}
		        return true;
			}
			else if(args[0].contentEquals(Commands.reload)) {
				if(args.length == 1) {
					if(hasPermission.test(Permissions.command_reload)){
						accessoryManager.reload(this);
						sender.sendMessage("リロード完了!!");			
					}
			        return true;
				}
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> out = new ArrayList<String>();
		if(command.getName().contentEquals("fsGuns")) {
			if(args.length == 1) {
				if (sender instanceof Player) {
					if(sender.hasPermission(Permissions.command_workbench))out.add(Commands.workbench);
					if(sender.hasPermission(Permissions.command_browse))out.add(Commands.browse);
				}
				if(sender.hasPermission(Permissions.command_set_firemode))out.add(Commands.set_firemode);
				if(sender.hasPermission(Permissions.command_reload))out.add(Commands.reload);
			}
			else {
				if(args.length == 2) {
					if(args[0].contentEquals(Commands.set_firemode)) {
						out.add("limited");
						out.add("unlimited");
					}
				}else {
					for (Player p2: this.getServer().getOnlinePlayers()) {
						out.add(p2.getName());
					}
				}
			}
		}
		return out;
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
		util.Init();
		accessoryManager = new Info_Manager(this);
		bulletManager = new BulletManager();
		shooterManager = new ShooterManager(this, accessoryManager, bulletManager);
		rhMng = new RecipeHanger_Manager();
		guiManager = new GuiManager(this, accessoryManager);
		ExRecipe.CreateRecipe(this, accessoryManager, rhMng);
		getServer().getPluginManager().registerEvents(new ShooterListener(this, shooterManager), this);
		getServer().getPluginManager().registerEvents(new GuiListener(this, guiManager, accessoryManager, rhMng), this);
		getServer().getPluginManager().registerEvents(new BulletListener(this, bulletManager), this);
		
		new Runner(this,shooterManager,bulletManager);	
	}
		
	@Override
	public ItemStack createfsGunsItem() {
		return null;
	}
}
