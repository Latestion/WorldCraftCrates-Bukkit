package me.Latestion.Crates.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Latestion.Crates.Main;

public class RefillInv {
	
	private Inventory invv;
	private String name;
	public List<Inventory> invs = new ArrayList<Inventory>();
	private Main plugin;
	private Player player;
	
	public RefillInv(Main plugin, String name, Player player) {
		this.name = name;
		this.plugin = plugin;
		this.player = player;
		createInv();
	}

	public void createInv() {
		invv = Bukkit.createInventory(null, 54, name);
		setGlass(invv);
		setLaunch(invv);
		setClose(invv);
		setArrow(invv);
		int i = 9;
		invs.add(invv);
		Inventory set = invv;
		Crate crate = new Crate(plugin, name);
		List<ItemStack> items = crate.getCrateItems;
		for (ItemStack item : items) {
			set.setItem(i, item);
			if (i == 44) {
				Inventory nw = createNewInv();
				set = nw;
				i = 8;
			}
			i++;
		}
	}
	
	
	public Inventory getInventory() {
		return invs.get(0);
	}
	
	public Inventory createNewInv() {
		Inventory newInv = Bukkit.createInventory(null, 54, name);
		setGlass(newInv);
		setLaunch(newInv);
		setClose(newInv);
		setArrow(newInv);
		invs.add(newInv); // Added
		return newInv;
	}
	
	public boolean isInventoryFull(Inventory inv) {
		for (ItemStack item : inv.getContents()) {
			if (item == null) {
				return false;
			}
		}
		return true;
	}
	
	public void createCrate() {
		// Means they pressed launch
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (Inventory inv : invs) {
			for (int i = 9; i < 45; i++) {
				ItemStack item = inv.getItem(i);
				if (item != null) {
					items.add(item);
				}
		
			}
		}
		if (items.isEmpty()) {
			player.sendMessage(ChatColor.RED + "Cannot create an empty crate!");
			return;
		}
		player.sendMessage(ChatColor.GOLD + "Crate has been updates!");
		Crate crate = new Crate(plugin, name);
		crate.setCrateItems(items);
	}
	
	private ItemStack glass() {
		ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		return item;
	}
	
	private void setGlass(Inventory inv) {
		inv.setItem(0, glass());
		inv.setItem(1, glass());
		inv.setItem(2, glass());
		inv.setItem(3, glass());
		inv.setItem(5, glass());
		inv.setItem(6, glass());
		inv.setItem(7, glass());
		inv.setItem(8, glass());
		inv.setItem(45, glass());
		inv.setItem(46, glass());
		inv.setItem(47, glass());
		inv.setItem(51, glass());
		inv.setItem(52, glass());
		inv.setItem(53, glass());
	}
	
	private void setLaunch(Inventory inv) {
		ItemStack item = new ItemStack(Material.GREEN_TERRACOTTA);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Launch");
		item.setItemMeta(meta);
		inv.setItem(4, item);
	}
	
	private void setClose(Inventory inv) {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Close");
		item.setItemMeta(meta);
		inv.setItem(49, item);
	}
	
	private void setArrow(Inventory inv) {
		ItemStack item = new ItemStack(Material.SPECTRAL_ARROW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Previous Page");
		item.setItemMeta(meta);
		inv.setItem(48, item);
		
		ItemStack item2 = new ItemStack(Material.SPECTRAL_ARROW);
		ItemMeta meta2 = item2.getItemMeta();
		meta2.setDisplayName(ChatColor.GREEN + "Next Page");
		item2.setItemMeta(meta2);
		inv.setItem(50, item2);
	}
	
}
