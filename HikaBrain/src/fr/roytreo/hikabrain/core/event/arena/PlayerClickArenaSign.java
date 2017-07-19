package fr.roytreo.hikabrain.core.event.arena;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.arena.Arena;
import fr.roytreo.hikabrain.core.arena.event.PlayerClickArenaSignEvent;
import fr.roytreo.hikabrain.core.event.EventListener;
import fr.roytreo.hikabrain.core.handler.Messages;

public class PlayerClickArenaSign extends EventListener {

	public PlayerClickArenaSign(final HikaBrainPlugin plugin) {
		super(plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerClickArenaSign(final PlayerClickArenaSignEvent event) {
		Player player = event.getPlayer();
		Sign sign = event.getSign();
		if (sign.getLine(0).equals(Messages.SIGN_HEADER.getMessage()))
		{
			Arena arena = Arena.getArena(ChatColor.stripColor(sign.getLine(1)));
			if (arena != null)
			{
				if (!Arena.isPlayerInArena(player)) {
					player.chat("/hb join " + ChatColor.stripColor(sign.getLine(1)));
				} else {
					Messages.ALREADY_IN_GAME.sendMessage(player, player);
				}
			} else {
				Messages.SIGN_NOT_LINK_TO_ARENA.sendMessage(player, player);
			}
		}
	}
}
