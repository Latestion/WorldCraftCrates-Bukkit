package me.Latestion.Crates.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.Latestion.Crates.Main;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("all")
public class CrateLoot {

	private Main plugin;
	public List<Inventory> invs = new ArrayList<Inventory>();
	
	public CrateLoot(Main plugin) {
		this.plugin = plugin;
		createInv();
	}
	
	public Inventory getInventory() {
		return invs.get(0);
	}
	
	private void createInv() {
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Crate Loot");
	}
	
	private ItemStack glass() {
		ItemStack item = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		return item;
	}
	
}
