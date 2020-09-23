package me.Latestion.Crates.MyEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import me.Latestion.Crates.Main;

public class AsInteract implements Listener {

	private Main plugin;
	
	public AsInteract(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void asInteract(PlayerArmorStandManipulateEvent event) {
		if (plugin.mani.contains(event.getRightClicked())) {
			event.setCancelled(true);
		}
	}
	
}
