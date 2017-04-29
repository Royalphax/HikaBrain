package fr.roytreo.hikabrain.core.version;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface INMSUtils {

	public ItemStack setIllegallyGlowing(final ItemStack item);
	
	public void setUnbreakable(final ItemMeta meta, final boolean bool);
	
	public void setMaxHealth(final LivingEntity ent, final Double maxHealth);
	
	public void displayRedScreen(final Player player, final boolean activate);
}
