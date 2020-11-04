package me.Latestion.Crates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.api.Economy;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.Latestion.Crates.Commands.Executor;
import me.Latestion.Crates.Files.CrateLootManager;
import me.Latestion.Crates.Files.DataManager;
import me.Latestion.Crates.MyEvents.AsInteract;
import me.Latestion.Crates.MyEvents.InventoryClick;
import me.Latestion.Crates.MyEvents.InventoryClose;
import me.Latestion.Crates.MyEvents.InventoryOpen;
import me.Latestion.Crates.MyEvents.PlayerJoin;
import me.Latestion.Crates.Utils.Crate;
import me.Latestion.Crates.Utils.CreateCrate;
import me.Latestion.Crates.Utils.RefillInv;
import me.Latestion.Crates.Utils.Util;

public class Main extends JavaPlugin {

	public Economy eco;
	public DataManager data;
	public CrateLootManager loot;
	public Util util;
	
	public Map<String, UUID> idInstance = new HashMap<String, UUID>();
	public Map<Player, CreateCrate> newCrates = new HashMap<Player, CreateCrate>(); // NEW CRATES DONE EVERYTHING BY CLASS
	public Map<UUID, Inventory> shulkerInv = new HashMap<UUID, Inventory>();
	
	public Map<Player, Location> inCrate = new HashMap<>();
	public List<Player> isCreating = new ArrayList<Player>();
	public List<String> isBeingCreated = new ArrayList<String>();
	public List<Block> inProcess = new ArrayList<Block>();
	public List<Player> cache = new ArrayList<Player>();
	
	public Map<Location, Hologram> holoInstance = new HashMap<Location, Hologram>();
	public Map<Location, ArmorStand> asInstance = new HashMap<>();
	public List<ArmorStand> mani = new ArrayList<>();
	public Map<Player, RefillInv> refillInv = new HashMap<>();
	
	@Override
	public void onEnable() {
		if (!setupEconomy()) {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		loadFiles();
		loadEvents();
		holo();
		loadHolos();
		spawnAs();
	}
	
	@Override
	public void onDisable() {
		removeHolos();
		removeAs();
	}
	
	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economy = getServer().getServicesManager().getRegistration
				(com.earth2me.essentials.api.Economy.class);
		if (economy != null) {
			eco = economy.getProvider();
		}
		return (eco == null);
	}
	
	public void holo() {
		if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
			getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
			getLogger().severe("*** This plugin will be disabled. ***");
			this.setEnabled(false);
			return;
		}
	}
	
	private void loadFiles() {
		this.data = new DataManager(this);
		// this.loot = new CrateLootManager(this);
		this.util = new Util(this);
		this.getCommand("crate").setExecutor(new Executor(this));
	}
	
	private void loadEvents() {
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents(new InventoryClick(this), this);
		manager.registerEvents(new InventoryClose(this), this);
		manager.registerEvents(new InventoryOpen(this), this);
		manager.registerEvents(new PlayerJoin(this), this);
		manager.registerEvents(new AsInteract(this), this);
	}
	
	private void loadHolos() {
		try {
			data.getConfig().getConfigurationSection("shulker").getKeys(false).forEach(key -> {
				Location loc = util.stringToLoc(key);
				String name = data.getConfig().getString("shulker." + key + ".crate-name");
				Crate crate = new Crate(this, name);
				crate.createArmorStand(loc);
			});
		} catch (Exception e) {
		}
	}
	
	private void removeHolos() {
		for (Hologram gram : HologramsAPI.getHolograms(this)) {
			gram.delete();
		}
	}
	
	private void spawnAs() {
		try {
			data.getConfig().getConfigurationSection("shulker").getKeys(false).forEach(key -> {
				Location loc = util.stringToLoc(key);
				ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
				prepareArmorStand(as);
				asInstance.put(loc, as);
			});
		} catch (Exception e) {
		}
	}
	
	private void removeAs() {
		for (ArmorStand as : asInstance.values()) {
			as.remove();
		}
	}
	
    public void prepareArmorStand(ArmorStand as) {
    	as.setVisible(false);
    	as.setMarker(false);
    	as.setBasePlate(false);
    	as.setSmall(true);
    	as.setInvulnerable(true);
    	as.setAI(false);
    	as.setGravity(false);
    	as.setCollidable(false);
    }
    
}
