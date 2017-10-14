package fr.roytreo.hikabrain.core.arena.event;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.roytreo.hikabrain.core.arena.Arena;

public class BuildBridgeEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Location from;
	private final Location to;
	private final Arena arena;
	private Material material;
	private boolean isCancelled;
	
	public BuildBridgeEvent(Location from, Location to, Arena arena) {
		this.from = from;
		this.to = to;
		this.arena = arena;
		this.material = Material.SANDSTONE;
		this.isCancelled = false;
	}
	
	public Location getFrom() {
		return this.from;
	}
	
	public Location getTo() {
		return this.to;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	public boolean isCancelled() {
		return this.isCancelled;
	}
	
	public void setMaterial(Material m) {
		this.material = m;
	}
	
	public void setCancelled(boolean b) {
		this.isCancelled = b;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}