package fr.roytreo.hikabrain.core.arena.event;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.roytreo.hikabrain.core.arena.Arena;

public class ArenaRegenEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Arena arena;
	private List<Material> materialsToRemove;
	private boolean isCancelled;
	
	public ArenaRegenEvent(Arena arena) {
		this.arena = arena;
		this.materialsToRemove = Arrays.asList(Material.SANDSTONE);
		this.isCancelled = false;
	}
	
	public Arena getArena() {
		return this.arena;
	}
	
	public void setMaterialsToRemove(Material... materials) {
		this.materialsToRemove = Arrays.asList(materials);
	}
	
	public void setMaterialsToRemove(List<Material> materials) {
		this.materialsToRemove = materials;
	}
	
	public List<Material> getMaterialsToRemove() {
		return this.materialsToRemove;
	}
	
	public boolean isCancelled() {
		return this.isCancelled;
	}
	
	public void setCancelled(boolean bool) {
		this.isCancelled = bool;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
