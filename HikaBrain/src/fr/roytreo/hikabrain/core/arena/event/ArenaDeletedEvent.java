package fr.roytreo.hikabrain.core.arena.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.roytreo.hikabrain.core.arena.Arena;

public class ArenaDeletedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Player creator;
	private final Arena arena;
	private boolean isCancelled;
	
	public ArenaDeletedEvent(Player creator, Arena arena) {
		this.creator = creator;
		this.arena = arena;
		this.isCancelled = false;
	}
	
	public Player getCreator() {
		return this.creator;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	public boolean isCancelled() {
		return this.isCancelled;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
