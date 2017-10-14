package fr.roytreo.hikabrain.core.arena.event;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerClickArenaSignEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final PlayerInteractEvent event;
	private final Sign sign;
	private final Player player;

	public PlayerClickArenaSignEvent(PlayerInteractEvent event, Sign sign, Player player) {
		this.event = event;
		this.sign = sign;
		this.player = player;
	}
	
	public PlayerInteractEvent getInteractEvent() {
		return this.event;
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
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
