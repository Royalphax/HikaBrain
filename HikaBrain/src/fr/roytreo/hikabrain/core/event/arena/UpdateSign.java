package fr.roytreo.hikabrain.core.event.arena;

import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.arena.event.UpdateSignEvent;
import fr.roytreo.hikabrain.core.event.EventListener;
import fr.roytreo.hikabrain.core.handler.GameState;
import fr.roytreo.hikabrain.core.handler.Messages;

public class UpdateSign extends EventListener {

	public UpdateSign(final HikaBrainPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onUpdateSigns(final UpdateSignEvent event) {
		final Arena arena = event.getArena();
		final Sign sign = event.getSign();
		sign.setLine(0, Messages.SIGN_HEADER.getMessage(arena));
		sign.setLine(1, Messages.SIGN_LINE_2.getMessage(arena));
		sign.setLine(2, (arena.getGameState() == GameState.WAITING ? Messages.SIGN_LINE_3_PLAYERS.getMessage(arena) : Messages.SIGN_LINE_3_SCORE.getMessage(arena)));
		sign.setLine(3, Messages.SIGN_LINE_4.getMessage(arena));
		sign.update();
	}
}
