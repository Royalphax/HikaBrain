package fr.roytreo.hikabrain.core.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.event.EventListener;
import fr.roytreo.hikabrain.core.manager.ArenaManager;

public class PlayerDeath extends EventListener {

	public PlayerDeath(final HikaBrainPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDeath(final org.bukkit.event.entity.PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (ArenaManager.isPlayerInArena(player)) {
			ArenaManager arena = ArenaManager.getPlayerArena(player);
			event.setDeathMessage(null);
			arena.respawn(player);
		}
	}
}
