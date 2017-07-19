package fr.roytreo.hikabrain.core.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.event.EventListener;

public class PlayerQuit extends EventListener {

	public PlayerQuit(final HikaBrainPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerQuit(final org.bukkit.event.player.PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (Arena.isPlayerInArena(player)) {
			Arena arena = Arena.getPlayerArena(player);
			arena.quit(player);
		}
	}
}
