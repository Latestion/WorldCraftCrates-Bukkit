package me.Latestion.Crates.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Latestion.Crates.Main;
import net.minecraft.server.v1_15_R1.BlockPosition;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import net.minecraft.server.v1_15_R1.TileEntityShulkerBox;

public class Crate {

	String name; // Crate name
	Main plugin;
	
	public Crate(Main plugin, String name) {
		this.plugin = plugin;
		this.name = name;
	}
	
	public List<ItemStack> getCrateItems() {
		List<ItemStack> send = new ArrayList<ItemStack>();
		try {
			plugin.data.getConfig().getConfigurationSection("crates." + name + ".items").getKeys(false).forEach(num -> {
				// num is number
				ItemStack add = plugin.data.getConfig().getItemStack("crates." + name + ".items." + num);
				send.add(add);
			});
		} catch (Exception e) {
		}
		return send;
	}
	
	public void setCrateItems(List<ItemStack> items) {
		int i = 0;
		plugin.data.getConfig().set("crates." + name + ".items", null);
		plugin.data.saveConfig();
		for (ItemStack item : items) {
			plugin.data.getConfig().set("crates." + name + ".items." + i, item);
			plugin.data.saveConfig();
			i++;
		}
	}
	
	public List<Location> getLocations() {
		List<Location> send = new ArrayList<Location>();
		try {
			plugin.data.getConfig().getConfigurationSection("shulker").getKeys(false).forEach(loc -> {
				if (plugin.data.getConfig().getString("shulker." + loc + ".crate-name").equalsIgnoreCase(name)) {
					send.add(plugin.util.stringToLoc(loc));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return send;
	}
	
	public boolean isEmpty() {
		return getCrateItems().isEmpty();
	}
	
	public int getPrice() {
		return plugin.data.getConfig().getInt("crates." + name + ".price");
	}
	
	public void setPrice(int price) {
		plugin.data.getConfig().set("crates." + name + ".price", price);
		plugin.data.saveConfig();
	}
	
	public int getPlayerCrate(Player player) {
		return plugin.data.getConfig().getInt("data." + player.getUniqueId().toString() + "." + name);
	}
	
	public void setPlayerCrate(Player player, int i) {
		plugin.data.getConfig().set("data." + player.getUniqueId().toString() + "." + name, i);
		plugin.data.saveConfig();
	}
	
	public void purchase(Block block, Player player) {
		Location loc = block.getLocation();
		tele(getUpperArmorStand(loc), getUpperArmorStand(loc).getLocation());
		tele(getLowerArmorStand(loc), getLowerArmorStand(loc).getLocation());
		plugin.inProcess.add(block);
        new SpawnParticles(plugin, loc, 5, Particle.TOTEM); // Spawns the particles
        ItemStack item = getRandomItem();
        openShulker(loc, 10, block);
        armorStandRotateAnimate(loc.clone().subtract(0, 1, 0), 10, item);
        updateArmorStandName(ChatColor.BOLD + "" + ChatColor.GOLD + player.getName(), ChatColor.WHITE + "Has won " 
        + item.getAmount() + "x " + item.getItemMeta().getDisplayName() + "!", 10, loc);
        List<ItemStack> items = this.getCrateItems();
        items.remove(item);
        this.setCrateItems(items);
        player.getInventory().addItem(item);
        player.updateInventory();
	}

	public ArmorStand getUpperArmorStand(Location loc) {
		for (Entity en : loc.getWorld().getNearbyEntities(loc.clone().add(0, 1, 0), 3, 3, 3)) {
			if (en instanceof ArmorStand) {
				if (en.getUniqueId().toString().equals(getUpperArmorStandID(loc))) {
					return ((ArmorStand) en);
				}
			}
		}
		return null;
	}
	
	public ArmorStand getLowerArmorStand(Location loc) {
		for (Entity en : loc.getWorld().getNearbyEntities(loc.clone().add(0, 1, 0), 3, 3, 3)) {
			if (en instanceof ArmorStand) {
				if (en.getUniqueId().toString().equals(getLowerArmorStandID(loc))) {
					return ((ArmorStand) en);
				}
			}
		}
		return null;
	}
	
	public String getUpperArmorStandID(Location loc) {
		String send = plugin.data.getConfig().getString("shulker." + plugin.util.locToString(loc) + ".as." + "upper");
		return send;
	}
	
	public String getLowerArmorStandID(Location loc) {
		String send = plugin.data.getConfig().getString("shulker." + plugin.util.locToString(loc) + ".as" + ".lower");
		return send;
	}
	
	public void createArmorStand(Location loc, Location save) {
		ArmorStand upperAs = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0, 1, 0), EntityType.ARMOR_STAND);
		prepareArmorStand(upperAs);
		upperAs.setCustomName(format(ChatColor.BOLD + "" + ChatColor.GOLD + name));
		plugin.data.getConfig().set("shulker." + plugin.util.locToString(save) + ".as.upper", upperAs.getUniqueId().toString());
		plugin.data.saveConfig();
		freezeEntity(upperAs);
		
		ArmorStand lowerAs = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0, 0.77, 0), EntityType.ARMOR_STAND);
		prepareArmorStand(lowerAs);
		lowerAs.setCustomName(getPrice() + " Dollars");
		plugin.data.getConfig().set("shulker." + plugin.util.locToString(save) + ".as.lower", lowerAs.getUniqueId().toString());
		plugin.data.saveConfig();
		freezeEntity(lowerAs);
	}
	
	public void updateArmorStandName(String up, String down, int duration, Location loc) {
		getUpperArmorStand(loc).setCustomName(format(up));
		getLowerArmorStand(loc).setCustomName(format(down));
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
            	getUpperArmorStand(loc).setCustomName(format(name));
            	getLowerArmorStand(loc).setCustomName(getPrice() + " Dollars");
            	if (isEmpty()) {
            		getUpperArmorStand(loc).setCustomName(format(ChatColor.BOLD + "" + ChatColor.GOLD + "(Empty) " + name));
            	}
            }            
        }, duration * 20);
	}
	
	private void openShulker(Location loc, int i, Block block) {
		net.minecraft.server.v1_15_R1.World world = ((CraftWorld) loc.getWorld()).getHandle();
        BlockPosition position = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
        TileEntityShulkerBox tileShulker = (TileEntityShulkerBox) world.getTileEntity(position);
        world.playBlockAction(position, tileShulker.getBlock().getBlock(), 1, 1);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
            	world.playBlockAction(position, tileShulker.getBlock().getBlock(), 1, 0);
            	plugin.inProcess.remove(block);
            }            
        }, i * 20);
	}
	
    private void armorStandRotateAnimate(Location loc, int i, ItemStack item){
    	ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
    	prepareArmorStand(as, item);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
            	as.remove();
            }            
        }, i * 20);
    	plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
        	if (as.isDead()) {
        		return;
        	}
            loc.setYaw(loc.getYaw() +  4);
            as.teleport(loc);
        }, 0, 1);
    }
    
    private ItemStack getRandomItem() {
        Random rand = new Random();
        int i = rand.nextInt(getCrateItems().size());
        return getCrateItems().get(i);
    }
    
    private void prepareArmorStand(ArmorStand as, ItemStack item) {
    	as.setVisible(false);
    	as.setMarker(false);
    	as.setBasePlate(false);
    	as.setSmall(true);
    	as.setHelmet(item);
    	as.setInvulnerable(true);
    	as.setAI(false);
    	as.setGravity(false);
    	as.setCollidable(false);
    }
    
    private void prepareArmorStand(ArmorStand as) {
    	as.setVisible(false);
    	as.setMarker(false);
    	as.setBasePlate(false);
    	as.setSmall(true);
    	as.setInvulnerable(true);
    	as.setAI(false);
    	as.setGravity(false);
    	as.setCollidable(false);
    	as.setCustomNameVisible(true);
    }
    
    private String format(String s) {
    	return ChatColor.translateAlternateColorCodes('&', s);
    }
    
    private void tele(ArmorStand as, Location loc) {
    	plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
        	as.teleport(loc);
        }, 0, 1);
    }
    
    public void freezeEntity(Entity en){
        net.minecraft.server.v1_15_R1.Entity nmsEn = ((CraftEntity) en).getHandle();
        NBTTagCompound compound = new NBTTagCompound();
        nmsEn.c(compound);
        compound.setByte("NoAI", (byte) 1);
        nmsEn.f(compound);
    }   
}

/* crates:
 *   cratename:
 *     items:
 *       1:
 *         item
 *       2:
 *         item
 *     price: int
 */

/* 
 * shulker:
 *   location: 
 *     crate-name: name
 *     as:
 *       upper: UUID
 *       lower: UUID
 */   