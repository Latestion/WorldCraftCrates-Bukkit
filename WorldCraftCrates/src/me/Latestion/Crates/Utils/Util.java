package me.Latestion.Crates.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.Latestion.Crates.Main;

public class Util {

	private Main plugin;
	
	public Util(Main plugin) {
		this.plugin = plugin;
	}
	
	public String locToString(Location loc) {
		return (loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
	}
	
	public Location stringToLoc(String loc) {
		String[] split = loc.split(",");
		Location send = new Location(Bukkit.getWorld(split[0]), parseInt(split[1]), parseInt(split[2]), parseInt(split[3]));
		return send;
	}
	
	public int parseInt(String i) {
		return Integer.parseInt(i);
	}
	
	public boolean isCrate(String name) {
		try {
			for (String key : plugin.data.getConfig().getConfigurationSection("crates").getKeys(false)) {
				if (key.equalsIgnoreCase(name)) {
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}
	
	public boolean isNum(String i) {
		try {
			Integer.parseInt(i);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
	
	public int getCratePrice(String name) {
		return plugin.data.getConfig().getInt("crates." + name + ".price");
	}
	
	public boolean isInventoryFull(Player player) {
		Inventory inv = player.getInventory();
		for (ItemStack item : inv) {
			if (item == null) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isShulkerBox(Material m) {
		switch (m) {
			case SHULKER_BOX:
			case LIGHT_GRAY_SHULKER_BOX:
			case BLACK_SHULKER_BOX:
			case BLUE_SHULKER_BOX:
			case BROWN_SHULKER_BOX:
			case CYAN_SHULKER_BOX:
			case GRAY_SHULKER_BOX:
			case GREEN_SHULKER_BOX:
			case LIGHT_BLUE_SHULKER_BOX:
			case LIME_SHULKER_BOX:
			case MAGENTA_SHULKER_BOX:
			case ORANGE_SHULKER_BOX:
			case PINK_SHULKER_BOX:
			case PURPLE_SHULKER_BOX:
			case RED_SHULKER_BOX:
			case WHITE_SHULKER_BOX:
			case YELLOW_SHULKER_BOX:
				return true;
			default:
				return false;
		}
	}
    
    public boolean isLocationTaken(Location loc) {
    	List<Location> test = new ArrayList<>();
    	plugin.data.getConfig().getConfigurationSection("shulker").getKeys(false).forEach(key -> {
    		test.add(this.stringToLoc(key));
    	});
    	if (test.contains(loc)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
}
