package fr.roytreo.hikabrain.core.event.gui;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.event.EventListener;
import fr.roytreo.hikabrain.core.handler.GameState;

public class InventoryClick extends EventListener {

	public InventoryClick(final HikaBrainPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDeath(final org.bukkit.event.inventory.InventoryClickEvent event) {
		HumanEntity human = event.getWhoClicked();
		if (human instanceof Player) {
			Player player = (Player) human;
			if (Arena.isPlayerInArena(player)) {
				Arena arena = Arena.getPlayerArena(player);
				if (arena.getGameState() != GameState.INGAME)
					event.setCancelled(true);
			}
		}
	}
}
