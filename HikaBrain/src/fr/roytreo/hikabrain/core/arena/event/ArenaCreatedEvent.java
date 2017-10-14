package fr.roytreo.hikabrain.core.arena.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.roytreo.hikabrain.core.arena.Arena;

public class ArenaCreatedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Player creator;
	private final Arena arena;
	
	public ArenaCreatedEvent(Player creator, Arena arena) {
		this.creator = creator;
		this.arena = arena;
	}
	
	public Player getCreator() {
		return this.creator;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
