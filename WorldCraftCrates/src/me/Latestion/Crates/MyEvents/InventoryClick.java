package me.Latestion.Crates.MyEvents;

import java.math.BigDecimal;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.PlayerInventory;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import me.Latestion.Crates.Main;
import me.Latestion.Crates.Utils.Crate;
import me.Latestion.Crates.Utils.CreateCrate;
import me.Latestion.Crates.Utils.RefillInv;

public class InventoryClick implements Listener {

	private Main plugin;
	
	public InventoryClick(Main plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void invClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (plugin.isCreating.contains(event.getWhoClicked())) {
			CreateCrate crate = plugin.newCrates.get(player);
			if (event.getClickedInventory() instanceof PlayerInventory) {
				return;
			}
			if (event.getSlot() < 9 || event.getSlot() > 44) {
				event.setCancelled(true);
			}
			if (event.getSlot() == 49) {
				plugin.isCreating.remove(player);
				plugin.isBeingCreated.remove(event.getView().getTitle());
				plugin.newCrates.remove(player);
				player.closeInventory();
			}
			if (event.getSlot() == 48) {
				if (event.getInventory().equals(crate.getInventory())) {
					event.setCancelled(true);
					return;
				}
				/// Open the inventory index - 1
				plugin.cache.add(player);
				player.openInventory(crate.invs.get(crate.invs.indexOf(event.getInventory()) - 1));
			}
			if (event.getSlot() == 50) {
				if (crate.isInventoryFull(event.getInventory())) {
					plugin.cache.add(player);
					player.openInventory(crate.createNewInv());
				}
			}
			if (event.getSlot() == 4) {
				crate.createCrate();
				plugin.isCreating.remove(player);
				plugin.isBeingCreated.remove(event.getView().getTitle());
				plugin.newCrates.remove(player);
				player.closeInventory();
			}
			return;
		}
		if (plugin.shulkerInv.containsValue(event.getInventory())) {
			String name = event.getView().getTitle();
			if (event.getClickedInventory() instanceof PlayerInventory) {
				event.setCancelled(true);
				return;
			}
			if (event.getCurrentItem() == null) {
				return;
			}
			event.setCancelled(true);
			Block block = player.getLocation().getWorld().getBlockAt(plugin.inCrate.get(player));
			if (event.getSlot() == 15) {
				try {
					BigDecimal i = Economy.getMoneyExact(event.getWhoClicked().getUniqueId());
					int price = plugin.util.getCratePrice(name);
					if (i.doubleValue() > price) {
						Economy.subtract(event.getWhoClicked().getUniqueId(), new BigDecimal(price));
						player.closeInventory();
						Crate crate = new Crate(plugin, name);
						crate.purchase(block, player);
						// RUN THE MECHANICS FOR PARTICLES AND THE ROTATING ITEM AND THE CHANGE THE NAME OF THE ARMOR STAND ABOVE IT
					}
					else {
						event.getWhoClicked().closeInventory();
						event.getWhoClicked().sendMessage(ChatColor.RED + "Not enough funds!");
					}
				} catch (UserDoesNotExistException e) {
				} catch (NoLoanPermittedException e) {
				} catch (ArithmeticException e) {
				}
				event.getWhoClicked().closeInventory();
			}
			if (event.getSlot() == 11) {
				Crate crate = new Crate(plugin, name);
				int currentCrates = crate.getPlayerCrate(player);
				if (currentCrates == 0) {
					event.getWhoClicked().sendMessage(ChatColor.RED + "Not enought crates!");
					event.getWhoClicked().closeInventory();
					return;
				}
				currentCrates--;
				crate.setPlayerCrate(player, currentCrates);
				event.getWhoClicked().closeInventory();
				crate.purchase(block, player);
			}
		}
		if (plugin.refillInv.containsKey(player))
		if (plugin.refillInv.get(player).invs.contains(event.getInventory())) {
			RefillInv crate = plugin.refillInv.get(player);
			if (event.getClickedInventory() instanceof PlayerInventory) {
				return;
			}
			if (event.getSlot() < 9 || event.getSlot() > 44) {
				event.setCancelled(true);
			}
			if (event.getSlot() == 49) {
				plugin.refillInv.remove(player);
				player.closeInventory();
			}
			if (event.getSlot() == 48) {
				if (event.getInventory().equals(crate.getInventory())) {
					event.setCancelled(true);
					return;
				}
				/// Open the inventory index - 1
				plugin.cache.add(player);
				player.openInventory(crate.invs.get(crate.invs.indexOf(event.getInventory()) - 1));
			}
			if (event.getSlot() == 50) {
				if (crate.isInventoryFull(event.getInventory())) {
					plugin.cache.add(player);
					player.openInventory(crate.createNewInv());
				}
			}
			if (event.getSlot() == 4) {
				crate.createCrate();
				plugin.refillInv.remove(player);
				player.closeInventory();
			}
			return;
		}
	}
}
