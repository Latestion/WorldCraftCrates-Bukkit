package me.Latestion.Crates.MyEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.Latestion.Crates.Main;

public class InventoryClose implements Listener {

	private Main plugin;
	
	public InventoryClose(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void invClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if (plugin.isCreating.contains(event.getPlayer())) {
			if (plugin.cache.contains(event.getPlayer())) {
				plugin.cache.remove(event.getPlayer());
				return;
			}
	        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	            public void run() {
	            	event.getPlayer().openInventory(event.getInventory());
	            }            
	        }, 10);
		}
		if (plugin.shulkerInv.containsValue(event.getInventory())) {
			plugin.inCrate.remove(event.getPlayer());
		}
		if (plugin.refillInv.containsKey(player))
		if (plugin.refillInv.get(player).invs.contains(event.getInventory())) {
			if (plugin.cache.contains(event.getPlayer())) {
				plugin.cache.remove(event.getPlayer());
				return;
			}
	        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	            public void run() {
	            	event.getPlayer().openInventory(event.getInventory());
	            }            
	        }, 10);
		}
	}
}
