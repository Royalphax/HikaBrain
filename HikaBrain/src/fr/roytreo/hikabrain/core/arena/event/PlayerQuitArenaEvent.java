package fr.roytreo.hikabrain.core.arena.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.roytreo.hikabrain.core.arena.Arena;

public class PlayerQuitArenaEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Player player;
	private final Arena arena;
	private boolean isCancelled;
	
	public PlayerQuitArenaEvent(Player player, Arena arena) {
		this.player = player;
		this.arena = arena;
		this.isCancelled = false;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	public boolean isCancelled() {
		return this.isCancelled;
	}
	
	public void setCancelled(boolean b) {
		this.isCancelled = b;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}