package fr.roytreo.hikabrain.core.arena.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.handler.Team;

public class PlayerSwitchTeamEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Player player;
	private final Team newTeam;
	private final Team oldTeam;
	private final Arena arena;
	private boolean isCancelled;
	
	public PlayerSwitchTeamEvent(Player player, Team newTeam, Team oldTeam, Arena arena) {
		this.player = player;
		this.newTeam = newTeam;
		this.oldTeam = oldTeam;
		this.arena = arena;
		this.isCancelled = false;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Team getNewTeam() {
		return this.newTeam;
	}
	
	public Team getOldTeam() {
		return this.oldTeam;
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