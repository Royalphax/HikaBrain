package fr.roytreo.hikabrain.core.arena.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.handler.Team;

public class UpdateScoreEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Arena arena;
	private final Team winner;
	private final Player scoredPlayer;
	private final int newRedScore;
	private final int newBlueScore;

	public UpdateScoreEvent(Arena arena, Team winner, Player scoredPlayer) {
		this.arena = arena;
		this.winner = winner;
		this.scoredPlayer = scoredPlayer;
		this.newRedScore = arena.getRedScore() + (winner == Team.RED ? 1 : 0);
		this.newBlueScore = arena.getBlueScore() + (winner == Team.BLUE ? 1 : 0);
	}

	public int getNewRedScore() {
		return this.newRedScore;
	}
	
	public int getNewBlueScore() {
		return this.newBlueScore;
	}
	
	public Team getWinner() {
		return this.winner;
	}

	public Arena getArena() {
		return this.arena;
	}
	
	public Player getScoredPlayer() {
		return this.scoredPlayer;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
