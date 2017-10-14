package fr.roytreo.hikabrain.core.arena.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.handler.GameState;

public class UpdateGameStateEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Arena arena;
	private final GameState newGameState;

	public UpdateGameStateEvent(Arena arena, GameState newGameState) {
		this.arena = arena;
		this.newGameState = newGameState;
	}

	public GameState getNew() {
		return this.newGameState;
	}
	
	public GameState getLast() {
		return this.arena.getGameState();
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
