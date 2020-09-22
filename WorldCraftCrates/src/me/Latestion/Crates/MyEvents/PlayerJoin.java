package me.Latestion.Crates.MyEvents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.Latestion.Crates.Main;

public class PlayerJoin implements Listener {

	private Main plugin;
	public PlayerJoin(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void pJoin(PlayerJoinEvent event) {
		plugin.idInstance.put(event.getPlayer().getName(), event.getPlayer().getUniqueId());
	}
	
}
