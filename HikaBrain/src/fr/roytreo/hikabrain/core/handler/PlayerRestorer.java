package fr.roytreo.hikabrain.core.handler;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class PlayerRestorer {

	private final static String inventoryMetadata = "last_saved_inventory";
	private final static String locationMetadata = "last_saved_location";
	private final static String healthhungerMetadata = "last_saved_health_hunger";
	private final static String experienceMetadata = "last_saved_experience";

	private final Plugin plugin;

	public PlayerRestorer(Plugin plugin) {
		this.plugin = plugin;
	}

	public void saveProperties(Player player) {
		clearProperties(player);
		player.setMetadata(inventoryMetadata, new FixedMetadataValue(this.plugin, player.getInventory().getContents()));
		player.setMetadata(locationMetadata, new FixedMetadataValue(this.plugin, player.getLocation()));
		player.setMetadata(healthhungerMetadata, new FixedMetadataValue(this.plugin, player.getHealth() + "_" + player.getMaxHealth() + "_" + player.getFoodLevel()));
		player.setMetadata(experienceMetadata, new FixedMetadataValue(this.plugin, player.getExp() + "_" + player.getLevel()));
		player.getInventory().clear();
		player.setMaxHealth(20.0);
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setExp(0.0f);
		player.setLevel(0);
	}

	public void restoreProperties(Player player) {
		player.getInventory().clear();
		for (MetadataValue value : player.getMetadata(inventoryMetadata))
			if (value.getOwningPlugin().getName().equals(this.plugin.getName()))
				player.getInventory().setContents((ItemStack[]) value.value());
		for (MetadataValue value : player.getMetadata(locationMetadata))
			if (value.getOwningPlugin().getName().equals(this.plugin.getName()))
				player.teleport((Location) value.value());
		for (MetadataValue value : player.getMetadata(healthhungerMetadata))
			if (value.getOwningPlugin().getName().equals(this.plugin.getName())) {
				String[] split = value.value().toString().split("_");
				player.setMaxHealth(Double.parseDouble(split[1]));
				player.setHealth(Double.parseDouble(split[0]));
				player.setFoodLevel(Integer.parseInt(split[2]));
			}
		for (MetadataValue value : player.getMetadata(experienceMetadata))
			if (value.getOwningPlugin().getName().equals(this.plugin.getName())) {
				String[] split = value.value().toString().split("_");
				player.setExp(Float.parseFloat(split[0]));
				player.setLevel(Integer.parseInt(split[1]));
			}
		clearProperties(player);
	}

	public void clearProperties(Player player) {
		if (player.hasMetadata(inventoryMetadata))
			player.removeMetadata(inventoryMetadata, this.plugin);
		if (player.hasMetadata(locationMetadata))
			player.removeMetadata(locationMetadata, this.plugin);
		if (player.hasMetadata(healthhungerMetadata))
			player.removeMetadata(healthhungerMetadata, this.plugin);
		if (player.hasMetadata(experienceMetadata))
			player.removeMetadata(experienceMetadata, this.plugin);
	}

}
