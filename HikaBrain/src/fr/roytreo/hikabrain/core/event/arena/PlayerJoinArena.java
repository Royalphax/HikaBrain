package fr.roytreo.hikabrain.core.event.arena;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.arena.event.PlayerJoinArenaEvent;
import fr.roytreo.hikabrain.core.event.EventListener;

public class PlayerJoinArena extends EventListener {

	public PlayerJoinArena(final HikaBrainPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoinArena(final PlayerJoinArenaEvent event) {
		Player player = event.getPlayer();
		event.setCancelled(Arena.isPlayerInArena(player));
	}
}
