package me.Latestion.Crates.Utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Latestion.Crates.Main;

public class CrateInv {

	private Inventory inv;
	private String name;
	private Main plugin;
	private UUID id;
	
	public CrateInv(Main plugin, UUID id, String name) {
		this.name = name;
		this.plugin = plugin;
		this.id = id;
		createInv();
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	private void createInv() {
		inv = Bukkit.createInventory(null, 27, name);
		Crate crate = new Crate(plugin, name);
		int total = crate.getPlayerCrate(Bukkit.getPlayer(id));
		inv.setItem(11, iron(total));
		if (total > 64) {
			inv.setItem(11, iron(64));
		}
		inv.setItem(15, gold());
	}
	
	private ItemStack iron(int i) {
		if (i == 0) {
			i = 1;
		}
		ItemStack item = new ItemStack(Material.IRON_NUGGET, i);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + "Pay with purchased crates");
		item.setItemMeta(meta);
		return item;
	}
	
	private ItemStack gold() {
		ItemStack item = new ItemStack(Material.GOLD_NUGGET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Pay with dollars");
		item.setItemMeta(meta);
		return item;
	}
	
}
