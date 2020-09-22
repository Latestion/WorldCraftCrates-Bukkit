package me.Latestion.Crates.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Latestion.Crates.Main;
import me.Latestion.Crates.Utils.Crate;
import me.Latestion.Crates.Utils.CreateCrate;
import me.Latestion.Crates.Utils.RefillInv;

public class Executor implements CommandExecutor {

	private Main plugin;
	
	public Executor(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		
		if (label.equalsIgnoreCase("crate")) {

			if (args.length == 0) {
				player.sendMessage(ChatColor.RED + "/crate create {cratename}" + ChatColor.WHITE + ": Creates the desierd crate!");
				player.sendMessage(ChatColor.RED + "/crate set {cratename}" + ChatColor.WHITE + ": Set the shulker box to crate!");
				player.sendMessage(ChatColor.RED + "/crate setprice {cratename}" + ChatColor.WHITE + ": Sets the price of crate!");
				player.sendMessage(ChatColor.RED + "/crate refill {cratename}" + ChatColor.WHITE + ": Refills the crate!");
				return false;
			}
			
			if (args[0].equalsIgnoreCase("create")) {
				// Create a new crate
				if (sender.hasPermission("crate.create")) {
					if (args.length == 1) {
						return false;
					}
					String name = stringBuilder(args, 1);
					
					if (plugin.isBeingCreated.contains(name)) {
						return false;
					}
					
					if (plugin.util.isCrate(name)) {
						return false;
					}
					
					CreateCrate crate = new CreateCrate(plugin, name, player);
					player.openInventory(crate.getInventory());
					plugin.newCrates.put(player, crate);
					plugin.isCreating.add(player);
					plugin.isBeingCreated.add(name);
				
				}
			}
			
			if (args[0].equalsIgnoreCase("set")) {
				// Set it to shulker box here
				if (player.hasPermission("crate.set")) {
					Block block = player.getTargetBlockExact(5);	
					if (block == null) {
						return false;
					}	
					if (args.length == 1) {
						return false;
					}
					String name = stringBuilder(args, 1);
					
					if (!plugin.util.isCrate(name)) {
						return false;
					}
				
					plugin.data.getConfig().set("shulker." + plugin.util.locToString(block.getLocation()) + ".crate-name", name);
					plugin.data.saveConfig();
					Crate crate = new Crate(plugin, name);
					crate.createArmorStand(block.getLocation().clone().add(0.5, -0.5, 0.5), block.getLocation());
				}
			}
			
			if (args[0].equalsIgnoreCase("setprice")) {
				// Set the price of crate with the crate name and eco plugin will be used
				if (player.hasPermission("crate.setprice")) {
					if (args.length < 2) {
						return false;
					}	
					String name = stringBuilder(args, 1, args.length - 1);
					
					if (!plugin.util.isCrate(name)) {
						return false;
					}
					
					if (!plugin.util.isNum(args[args.length - 1])) {
						return false;
					}
					
					Crate crate = new Crate(plugin, name);
					crate.setPrice(plugin.util.parseInt(args[args.length - 1]));
				}
			}
			
			if (args[0].equalsIgnoreCase("give")) {
				if (player.hasPermission("crate.give")) {
					if (args.length == 1 || args.length == 2) {
						return false;
					}
					try {
						Player target = Bukkit.getPlayer(plugin.idInstance.get(args[1]));
						String name = stringBuilder(args, 2, args.length - 1);
						int i = plugin.util.parseInt(args[args.length - 1]);
						int current = plugin.data.getConfig().getInt("data." + player.getUniqueId().toString() + "." + name);
						plugin.data.getConfig().set("data." + target.getUniqueId().toString() + "." + name, (current + i));
						plugin.data.saveConfig();
					} catch (Exception e) {
						return false;
					}
					
				}
			}
			
			if (args[0].equalsIgnoreCase("refill")) {
				
				if (player.hasPermission("crate.refill"))  {
					String name = stringBuilder(args, 1);
					
					if (!plugin.util.isCrate(name)) {
						return false;
					}
					RefillInv inv = new RefillInv(plugin, name, player);
					player.openInventory(inv.getInventory());
					plugin.refillInv.put(player, inv);
				}
			}
			
			if (args[0].equalsIgnoreCase("remove")) {
				if (args.length == 1) {
					return false;
				}
				// REMOVE LOCATION WITH THE CRATES TOO
				// REMOVE THIS CRATE FROM ALL HASHMAPS
				plugin.data.getConfig().set("crates." + stringBuilder(args, 1), null);
				plugin.data.saveConfig();
			}
		}
		return false;
	}
	
	public String stringBuilder(String[] args, int start) {
		StringBuffer sb = new StringBuffer();
      	for(int i = start; i < args.length; i++) {
      		sb.append(args[i] + " ");
      	}
      	return ChatColor.stripColor(sb.toString());
	}
	
	public String stringBuilder(String[] args, int start, int end) {
		StringBuffer sb = new StringBuffer();
      	for(int i = start; i < end; i++) { 	
      		sb.append(args[i] + " ");
      	}
      	return ChatColor.stripColor(sb.toString());
	}
}
