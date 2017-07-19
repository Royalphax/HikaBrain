package fr.roytreo.hikabrain.core.arena.event;

import org.bukkit.block.Sign;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.roytreo.hikabrain.core.arena.Arena;

public class UpdateSignEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Sign sign;
	private final Arena arena;

	public UpdateSignEvent(Sign sign, Arena arena) {
		this.sign = sign;
		this.arena = arena;
	}

	public Sign getSign() {
		return this.sign;
	}

	public Arena getArena() {
		return this.arena;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
