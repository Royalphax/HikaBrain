package fr.roytreo.hikabrain.core.handler;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerRestorer {

	private final Plugin plugin;

	public PlayerRestorer(Plugin plugin) {
		this.plugin = plugin;
	}

	public void saveProperties(Player player) {
		clearProperties(player);
		for (Metadata meta : Metadata.values()) {
			switch (meta) {
				case INVENTORY_METADATA :
					meta.setMetadata(player, new FixedMetadataValue(this.plugin, player.getInventory().getContents()));
					break;
				case LOCATION_METADATA :
					meta.setMetadata(player, new FixedMetadataValue(this.plugin, player.getLocation()));
					break;
				case HEALTH_HUNGER_METADATA :
					meta.setMetadata(player, new FixedMetadataValue(this.plugin, player.getHealth() + "_" + player.getMaxHealth() + "_" + player.getFoodLevel()));
					break;
				case EXPERIENCE_GAMEMODE_METADATA :
					meta.setMetadata(player, new FixedMetadataValue(this.plugin, player.getExp() + "_" + player.getLevel() + "_" + player.getGameMode().toString()));
					break;
				case SCOREBOARD_METADATA :
					meta.setMetadata(player, new FixedMetadataValue(this.plugin, player.getScoreboard()));
					break;
				case POTION_EFFECT_METADATA :
					StringBuilder potionEffects = new StringBuilder("_");
					for (PotionEffect effect : player.getActivePotionEffects()) {
						potionEffects.append("_" + effect.getType().getName() + ":" + effect.getDuration() + ":" + effect.getAmplifier() + ":" + effect.isAmbient() + ":" + effect.hasParticles());
					}
					meta.setMetadata(player, new FixedMetadataValue(this.plugin, potionEffects.toString().replaceAll("__", "")));
					break;
			}
		}
		player.getInventory().clear();
		player.setMaxHealth(20.0);
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.setExp(0.0f);
		player.setLevel(0);
		player.setGameMode(GameMode.ADVENTURE);
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
	}

	public void restoreProperties(Player player) {
		player.getInventory().clear();
		for (Metadata meta : Metadata.values()) {
			switch (meta) {
				case INVENTORY_METADATA :
					for (MetadataValue value : player.getMetadata(meta.toString()))
						if (value.getOwningPlugin().getName().equals(this.plugin.getName()))
							player.getInventory().setContents((ItemStack[]) value.value());
					break;
				case LOCATION_METADATA :
					for (MetadataValue value : player.getMetadata(meta.toString()))
						if (value.getOwningPlugin().getName().equals(this.plugin.getName()))
							player.teleport((Location) value.value());
					break;
				case HEALTH_HUNGER_METADATA :
					for (MetadataValue value : player.getMetadata(meta.toString()))
						if (value.getOwningPlugin().getName().equals(this.plugin.getName())) {
							String[] split = value.value().toString().split("_");
							player.setMaxHealth(Double.parseDouble(split[1]));
							player.setHealth(Double.parseDouble(split[0]));
							player.setFoodLevel(Integer.parseInt(split[2]));
						}
					break;
				case EXPERIENCE_GAMEMODE_METADATA :
					for (MetadataValue value : player.getMetadata(meta.toString()))
						if (value.getOwningPlugin().getName().equals(this.plugin.getName())) {
							String[] split = value.value().toString().split("_");
							player.setExp(Float.parseFloat(split[0]));
							player.setLevel(Integer.parseInt(split[1]));
							player.setGameMode(GameMode.valueOf(split[2]));
						}
					break;
				case SCOREBOARD_METADATA :
					for (MetadataValue value : player.getMetadata(meta.toString()))
						if (value.getOwningPlugin().getName().equals(this.plugin.getName()))
							player.setScoreboard((Scoreboard) value.value());
					break;
				case POTION_EFFECT_METADATA :
					for (MetadataValue value : player.getMetadata(meta.toString()))
						if (value.getOwningPlugin().getName().equals(this.plugin.getName())) {
							ArrayList<String> potionEffect = new ArrayList<>(Arrays.asList(((String) value.value()).split("_")));
							for (String effect : potionEffect) {
								String[] properties = effect.split(":");
								PotionEffectType type = PotionEffectType.getByName(properties[0]);
								int duration = Integer.parseInt(properties[1]);
								int amplifier = Integer.parseInt(properties[2]);
								boolean ambient = Boolean.parseBoolean(properties[3]);
								boolean particles = Boolean.parseBoolean(properties[4]);
								player.addPotionEffect(new PotionEffect(type, duration, amplifier, ambient, particles));
							}
						}
					break;
			}
		}
		clearProperties(player);
	}

	public void clearProperties(Player player) {
		for (Metadata meta : Metadata.values())
			if (player.hasMetadata(meta.toString()))
				player.removeMetadata(meta.toString(), this.plugin);
	}
	
	private enum Metadata {
		INVENTORY_METADATA("last_saved_inventory"),
		LOCATION_METADATA("last_saved_location"),
		HEALTH_HUNGER_METADATA("last_saved_health_hunger"),
		EXPERIENCE_GAMEMODE_METADATA("last_saved_experience_gamemode"),
		SCOREBOARD_METADATA("last_saved_scoreboard"),
		POTION_EFFECT_METADATA("last_saved_potion_effects");
		
		private String string;
		private Metadata(String key) {
			this.string = key;
		}
		
		public void setMetadata(Player player, MetadataValue value) {
			player.setMetadata(this.string, value);
		}
		
		@Override
		public String toString() {
			return this.string;
		}
	}
}
