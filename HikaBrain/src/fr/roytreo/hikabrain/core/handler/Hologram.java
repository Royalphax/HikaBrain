package fr.roytreo.hikabrain.core.handler;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Hologram {
	private ArmorStand as = null;

	public Hologram(String name, Location loc) {
		World world = loc.getWorld();
		this.as = ((ArmorStand) world.spawnEntity(loc.subtract(0, 1.0, 0), EntityType.ARMOR_STAND));
		this.as.setVisible(false);
		this.as.setBasePlate(false);
		this.as.setCustomName(name);
		this.as.setCustomNameVisible(true);
		this.as.setGravity(false);
		this.as.setSmall(true);
	}

	public void remove() {
		this.as.remove();
	}

	public void setName(String s) {
		this.as.setCustomName(s);
	}
	
	public static void runHologramTask(String name, Location location, Integer lifeTimeInSec, Plugin plugin)
	{
		final Hologram holo = new Hologram(name, location);
		new BukkitRunnable()
		{
			public void run()
			{
				holo.remove();
			}
		}.runTaskLater(plugin, lifeTimeInSec*20);
	}
}
