package fr.roytreo.hikabrain.core.arena.event;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerClickArenaSignEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final Sign sign;
	private final Player player;

	public PlayerClickArenaSignEvent(Sign sign, Player player) {
		this.sign = sign;
		this.player = player;
	}

	public Sign getSign() {
		return this.sign;
	}
	
	public Player getPlayer() {
		return this.player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
