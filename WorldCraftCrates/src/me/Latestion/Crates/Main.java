package me.Latestion.Crates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.api.Economy;

import me.Latestion.Crates.Commands.Executor;
import me.Latestion.Crates.Files.DataManager;
import me.Latestion.Crates.MyEvents.InventoryClick;
import me.Latestion.Crates.MyEvents.InventoryClose;
import me.Latestion.Crates.MyEvents.InventoryOpen;
import me.Latestion.Crates.MyEvents.PlayerJoin;
import me.Latestion.Crates.Utils.CreateCrate;
import me.Latestion.Crates.Utils.RefillInv;
import me.Latestion.Crates.Utils.Util;

public class Main extends JavaPlugin {

	public Economy eco;
	public DataManager data;
	public Util util;
	
	public Map<String, UUID> idInstance = new HashMap<String, UUID>();
	public Map<Player, CreateCrate> newCrates = new HashMap<Player, CreateCrate>(); // NEW CRATES DONE EVERYTHING BY CLASS
	public Map<UUID, Inventory> shulkerInv = new HashMap<UUID, Inventory>();
	
	public Map<Player, Location> inCrate = new HashMap<>();
	public List<Player> isCreating = new ArrayList<Player>();
	public List<String> isBeingCreated = new ArrayList<String>();
	public List<Block> inProcess = new ArrayList<Block>();
	public List<Player> cache = new ArrayList<Player>();
	
	public Map<Player, RefillInv> refillInv = new HashMap<>();
	
	@Override
	public void onEnable() {
		if (!setupEconomy()) {
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		loadFiles();
		loadEvents();
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economy = getServer().getServicesManager().getRegistration
				(com.earth2me.essentials.api.Economy.class);
		if (economy != null) {
			eco = economy.getProvider();
		}
		return (eco == null);
	}
	
	private void loadFiles() {
		this.data = new DataManager(this);
		this.util = new Util(this);
		this.getCommand("crate").setExecutor(new Executor(this));
	}
	
	private void loadEvents() {
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents(new InventoryClick(this), this);
		manager.registerEvents(new InventoryClose(this), this);
		manager.registerEvents(new InventoryOpen(this), this);
		manager.registerEvents(new PlayerJoin(this), this);
	}
	
}
