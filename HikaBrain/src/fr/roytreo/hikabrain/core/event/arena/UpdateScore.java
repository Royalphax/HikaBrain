package fr.roytreo.hikabrain.core.event.arena;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.event.UpdateScoreEvent;
import fr.roytreo.hikabrain.core.event.EventListener;
import fr.roytreo.hikabrain.core.handler.Messages;
import fr.roytreo.hikabrain.core.handler.Team;

public class UpdateScore extends EventListener {

	public UpdateScore(final HikaBrainPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onUpdateScore(final UpdateScoreEvent event) {
		Messages message = null;
		if (event.getWinner() == Team.RED) {
			message = Messages.TEAM_RED_SCORED;
		} else if (event.getWinner() == Team.BLUE) {
			message = Messages.TEAM_BLUE_SCORED;
		}
		if (message != null)
			for (Player player : event.getArena().getPlayers())
				message.sendMessage(player, event.getScoredPlayer());
	}
}
