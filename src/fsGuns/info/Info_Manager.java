package fsGuns.info;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fsGuns.info.InfoFrame.FireMode;
import fsGuns.info.InfoFrame.FireMode.ModeType;
import fsGuns.info.helper.ExplosionPerformance;
import fsGuns.info.helper.FlamePerformance;
import fsGuns.info.helper.GunPerformance;

public class Info_Manager {
	Map<String, InfoAccessory> mac;
	Map<String, InfoFrame> mfr;
	Map<String, InfoBullet> mbl;
	
	private GunPerformance loadPerformanceSection(ConfigurationSection p) {
		GunPerformance gp = new GunPerformance();
		gp.Damage = p.getDouble("damage",1);
		gp.RPM = p.getDouble("RPM",1);
		gp.EffectiveTick = p.getDouble("effectiveTick",1);
		gp.MaximumTick = p.getDouble("maximumTick",1);
		gp.MuzzleVelocity = p.getDouble("muzzleVelocity",1);
		gp.Recoil = p.getDouble("recoil",1);
		gp.Spreading = p.getDouble("spreading",1);
		return gp;
	}
	
	private List<PotionEffect> loadPotionSection(Plugin pl,ConfigurationSection p) {
		if(p != null) {
			Set<String> keys = p.getKeys(false);//key is potion name
			if(keys.size() > 0) {
				List<PotionEffect> l = new ArrayList<PotionEffect>(keys.size());
				for (String ptname : keys) {
					ConfigurationSection ptisec = p.getConfigurationSection(ptname);
					PotionEffectType pt = PotionEffectType.getByName(ptname);
					if(pt != null) {
						l.add(new PotionEffect(pt, ptisec.getInt("tick", 1), ptisec.getInt("amplifier", 1)));
					}else {
						pl.getLogger().log(Level.WARNING, "found invailed potioneffectName");
					}
				}
				return l;
			}
		}
		return null;
	}
	
	protected void LoadAccessoryYML(Plugin pl){
		//open YAML file
		File f = new File(pl.getDataFolder(), "accessory.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		//lead all accessory item sections
		for (String key : config.getConfigurationSection("accessory").getKeys(false)) {
			ConfigurationSection isec = config.getConfigurationSection("accessory." + key);
			
			String Name = key; //unique item name
			boolean isMag = false;
			String slotName = null;
			GunPerformance gp;
			String CartridgeName = null;
			int Cap = 0;
			
			//load magazine flag
			isMag = isec.contains("isMag", false);	
			
			if(isMag) {
				//on magazine
				if(isec.contains("cartridgeName")) {
					CartridgeName = isec.getString("cartridgeName");
				}else {
					pl.getLogger().log(Level.WARNING, "not found section(Accessory.yml item." + key + ".cartridgeName)");
					continue;
				}
				Cap = isec.getInt("capacity",100);
			}else {
				//on others
				if(!isec.contains("slotName")) {
					pl.getLogger().log(Level.WARNING, "not found section(Accessory.yml item." + key + ".slotName)");
					continue;
				}
				slotName = isec.getString("slotName");
			}
			
			//load performance
			ConfigurationSection performanceSection = isec.getConfigurationSection("performance");
			if(performanceSection != null) {
				gp = loadPerformanceSection(performanceSection);
			}else {
				gp = new GunPerformance();
			}
			
			//create/set InfoAccessory or InfoMagazine
			InfoAccessory ac = null;
			if(isMag) {
				ac = new InfoMagazine(Name,CartridgeName, gp, Cap);
			}else {
				ac = new InfoAccessory(Name,slotName, gp);
			}
			addAccessory(ac);
		}
	}
	
	protected void LoadFrameYML(Plugin pl){
		//open YAML file
		File f = new File(pl.getDataFolder(), "frame.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
				
		//lead all frame item sections
		for (String key : config.getConfigurationSection("frame").getKeys(false)) {
			ConfigurationSection isec = config.getConfigurationSection("frame." + key);
			
			String Name = key;  //unique item name
			String CartridgeName = null;
			List<String> slots = new ArrayList<String>();
			GunPerformance gp;
			FireMode mode = new FireMode();
			
			//load cartridge Name
			CartridgeName = isec.getString("cartridgeName");
			
			//load slots name list
			slots = isec.getStringList("slots");
			
			//load performance
			if(!isec.contains("performance")) {
				pl.getLogger().log(Level.WARNING, "not found section(frame.yml item." + key + ".performance)");
				continue;
			}else {
				gp = loadPerformanceSection(isec.getConfigurationSection("performance"));
			}
			
			//load mode
			try {
				mode.type = ModeType.valueOf(isec.getString("mode.type","FULLAUTO"));
			} catch(IllegalArgumentException e){
				mode.type =  ModeType.FULLAUTO;
			}
			mode.MaxFireCount = isec.getInt("mode.amount",1);
			
			//create/set InfoFrame
			InfoFrame fm =  new InfoFrame(Name, CartridgeName, slots, gp, mode);
			addFrame(fm);
		}
	}
	
	protected void LoadBulletYML(Plugin pl){
		//open YAML file
		File f = new File(pl.getDataFolder(), "bullet.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(f);
		
		//load all bullet item sections
		for (String key : config.getConfigurationSection("bullet").getKeys(false)) {
			ConfigurationSection isec = config.getConfigurationSection("bullet." + key);
			
			String Name = key; //unique item name
			String CartridgeName = null;
			GunPerformance gp;
			ExplosionPerformance ep = null;
			FlamePerformance fp = null;
			List<PotionEffect> lpp;//hit effect
			List<PotionEffect> lsp;//splash
			List<PotionEffect> llp;//crowd
			int cnt;
			
			//load cartridge Name
			CartridgeName = isec.getString("cartridgeName");
			
			//load performance
			ConfigurationSection performanceSection = isec.getConfigurationSection("performance");
			if(performanceSection != null) {
				gp = loadPerformanceSection(performanceSection);
			}else {
				gp = new GunPerformance();
			}
			
			//load explosion
			if(isec.contains("explosion")) {
				ep = new ExplosionPerformance((float)isec.getDouble("explosion.power", 1), isec.getBoolean("explosion.setfire", false));
			}
			//load flame
			if(isec.contains("flame")) {
				fp = new FlamePerformance(isec.getInt("flame.tick", 1));
			}
			//load potion effects (hit, splash, lingering)
			lpp = loadPotionSection(pl,isec.getConfigurationSection("potionEffect"));
			lsp = loadPotionSection(pl,isec.getConfigurationSection("splashPotionEffect"));
			llp = loadPotionSection(pl,isec.getConfigurationSection("lingeringPotionEffect"));
					
			//the number of arrow
			cnt = isec.getInt("amount",1);
			
			//create/set InfoBullet
			InfoBullet ni =  new InfoBullet(Name, CartridgeName, gp, ep, fp, lpp,lsp,llp,cnt);
			addBullet(ni);
		}
	}
	
	public Info_Manager(Plugin pl) {
		reload(pl);
	}

	public void reload(Plugin pl) {
		mac = new HashMap<String, InfoAccessory>();
		mfr = new HashMap<String, InfoFrame>();
		mbl = new HashMap<String, InfoBullet>();
		
		LoadAccessoryYML(pl);
		LoadFrameYML(pl);
		LoadBulletYML(pl);
	}
	
 	public void addAccessory(InfoAccessory ac) {
		mac.put(ac.getName(), ac);
	}
		
	public InfoAccessory getAccessory(String name) {
		return mac.get(name);
	}
	
	public InfoMagazine getMagazine(String name) {
		InfoAccessory ac = mac.get(name);
		if(ac instanceof InfoMagazine)return (InfoMagazine)ac;
		return null;
	}
	
	public Set<Map.Entry<String, InfoAccessory>> getAccessoryEntrySet(){
		return mac.entrySet();
	}
	
	public void addFrame(InfoFrame ac) {
		mfr.put(ac.getName(), ac);
	}
		
	public InfoFrame getFrame(String name) {
		return mfr.get(name);
	}
	
	public Set<Map.Entry<String, InfoFrame>> getFrameEntrySet(){
		return mfr.entrySet();
	}
	
	public void addBullet(InfoBullet a){
		mbl.put(a.getName(), a);
	}
	
	public InfoBullet getBullet(String name) {
		return mbl.get(name);
	}
		
	public Set<Map.Entry<String, InfoBullet>> getBulletEntrySet(){
		return mbl.entrySet();
	}
}
