package fr.roytreo.hikabrain.core.arena.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.roytreo.hikabrain.core.arena.Arena;

/**
 * This event has to be used to equip the player only.
 * @author Roytreo28
 */
public class ArenaPlayerRespawnEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Player player;
	private final Arena arena;
	private boolean isEquippingCancelled;
	
	public ArenaPlayerRespawnEvent(Player player, Arena arena) {
		this.player = player;
		this.arena = arena;
		this.isEquippingCancelled = false;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	public boolean isEquippingCancelled() {
		return this.isEquippingCancelled;
	}
	 
	public void setEquippingCancelled(boolean bool) {
		this.isEquippingCancelled = bool;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
