package fr.roytreo.hikabrain.core.arena.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.roytreo.hikabrain.core.arena.Arena;

public class ArenaPlayerRespawnEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Player player;
	private final Arena arena;
	
	public ArenaPlayerRespawnEvent(Player player, Arena arena) {
		this.player = player;
		this.arena = arena;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
