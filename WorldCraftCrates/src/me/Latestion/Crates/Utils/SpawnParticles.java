package me.Latestion.Crates.Utils;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;

import me.Latestion.Crates.Main;

public class SpawnParticles {

	private Main plugin;
	
	public SpawnParticles(Main plugin, Location loc, int amount, Particle particle) {
		this.plugin = plugin;
		playSpiralParticle(loc, amount, particle);
	}
	
	private void playSpiralParticle(Location loc, int amount, Particle particle) {
        Location l = loc.add(.5, .7, .5);
        new BukkitRunnable() {
            int time = 16;
            @Override
            public void run() {
                if (time == 15) {
                    l.getWorld().spawnParticle(particle, l.clone().add(.8, 0, 0), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(-.8, 0, 0), amount, 0, 0, 0, 0);
                }
                if (time == 14) {
                    l.getWorld().spawnParticle(particle, l.clone().add(.75, 0, .43), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(-.75, 0, -.43), amount, 0, 0, 0, 0);
                }
                if (time == 13) {
                    l.getWorld().spawnParticle(particle, l.clone().add(.65, 0, .65), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(-.65, 0, -.65), amount, 0, 0, 0, 0);
                }
                if (time == 12) {
                    l.getWorld().spawnParticle(particle, l.clone().add(.43, 0, .75), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(-.43, 0, -.75), amount, 0, 0, 0, 0);
                }
                if (time == 11) {
                    l.getWorld().spawnParticle(particle, l.clone().add(0, 0, .8), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(0, 0, -.8), amount, 0, 0, 0, 0);
                }
                if (time == 10) {
                    l.getWorld().spawnParticle(particle, l.clone().add(-.43, 0, .75), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(.43, 0, -.75), amount, 0, 0, 0, 0);
                }
                if (time == 9) {
                    l.getWorld().spawnParticle(particle, l.clone().add(-.65, 0, .65), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(.65, 0, -.65), amount, 0, 0, 0, 0);
                }
                if (time == 8) {
                    l.getWorld().spawnParticle(particle, l.clone().add(-.75, 0, .43), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(.75, 0, -.43), amount, 0, 0, 0, 0);
                }
                if (time == 7) {
                    l.getWorld().spawnParticle(particle, l.clone().add(-.8, 0, 0), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(.8, 0, 0), amount, 0, 0, 0, 0);
                }
                if (time == 6) {
                    l.getWorld().spawnParticle(particle, l.clone().add(-.75, 0, -.43), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(.75, 0, .43), amount, 0, 0, 0, 0);
                }
                if (time == 5) {
                    l.getWorld().spawnParticle(particle, l.clone().add(-.65, 0, -.65), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(.65, 0, .65), amount, 0, 0, 0, 0);
                }
                if (time == 4) {
                    l.getWorld().spawnParticle(particle, l.clone().add(-.43, 0, -.75), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(.43, 0, .75), amount, 0, 0, 0, 0);
                }
                if (time == 3) {
                    l.getWorld().spawnParticle(particle, l.clone().add(0, 0, -.8), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(0, 0, .8), amount, 0, 0, 0, 0);
                }
                if (time == 2) {
                    l.getWorld().spawnParticle(particle, l.clone().add(.43, 0, -.75), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(-.43, 0, .75), amount, 0, 0, 0, 0);
                }
                if (time == 1) {
                    l.getWorld().spawnParticle(particle, l.clone().add(.65, 0, -.65), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(-.65, 0, .65), amount, 0, 0, 0, 0);
                }
                if (time == 0) {
                    l.getWorld().spawnParticle(particle, l.clone().add(.75, 0, -.43), amount, 0, 0, 0, 0);
                    l.getWorld().spawnParticle(particle, l.clone().add(-.75, 0, .43), amount, 0, 0, 0, 0);
                    cancel();
                }
                time--;
            }

        }.runTaskTimer(plugin, 0, 2);
    }
}
