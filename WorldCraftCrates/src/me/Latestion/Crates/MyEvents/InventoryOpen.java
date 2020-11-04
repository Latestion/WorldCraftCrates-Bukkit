package me.Latestion.Crates.MyEvents;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import me.Latestion.Crates.Main;
import me.Latestion.Crates.Utils.Crate;
import me.Latestion.Crates.Utils.CrateInv;

public class InventoryOpen implements Listener {
	
	private Main plugin;
	
	public InventoryOpen(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void invOpen(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock() == null) {
				return;
			}
			Block block = event.getClickedBlock();
			Location loc = event.getClickedBlock().getLocation();
			if (plugin.util.isShulkerBox(event.getClickedBlock().getType())) {
				if (plugin.util.isLocationTaken(loc)) {
					event.setCancelled(true);
					// REAL CRATE
					Player player = event.getPlayer();
					
					if (plugin.util.isInventoryFull(player)) {
						player.sendMessage(ChatColor.RED + "Your inventory is full!");
						return;
					}
			
					if (plugin.inProcess.contains(block)) {
						return;
					}
					
					String name = plugin.data.getConfig().getString("shulker." + plugin.util.locToString(loc) + ".crate-name");
					Crate crate = new Crate(plugin, name);
					if (crate.isEmpty()) {
						player.sendMessage(ChatColor.RED + "Crate is empty!");
						return;
					}
					UUID id = event.getPlayer().getUniqueId();
					CrateInv inv = new CrateInv(plugin, id, name);
					Inventory invv = inv.getInventory();
					event.getPlayer().openInventory(invv);
					plugin.shulkerInv.put(id, invv);
					plugin.inCrate.put(event.getPlayer(), event.getClickedBlock().getLocation());
				}
			}
		}
	}
}
