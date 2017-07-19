package fr.roytreo.hikabrain.core.arena;

import java.util.HashMap;

import org.bukkit.entity.Player;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.handler.GameState;
import fr.roytreo.hikabrain.core.handler.Messages;
import fr.roytreo.hikabrain.core.handler.PlayerRestorer;
import fr.roytreo.hikabrain.core.handler.Team;

public abstract class Game {

	protected HashMap<Player, Team> players = new HashMap<>();
	
	protected int redScore = 0;
	protected int blueScore = 0;
	protected GameState state;
	
	protected HikaBrainPlugin plugin;
	protected PlayerRestorer playerRestorer;
	
	public Game(HikaBrainPlugin plugin) {
		this.plugin = plugin;
		playerRestorer = new PlayerRestorer(plugin);
	}
	
	public int getRedScore() {
		return this.redScore;
	}
	
	public int getBlueScore() {
		return this.blueScore;
	}
	
	public GameState getGameState() {
		return this.state;
	}
	
	public void setRedScore(int i) {
		this.redScore = i;
	}
	
	public void setBlueScore(int i) {
		this.blueScore = i;
	}
	
	public abstract void join(Player player);
	
	public abstract void quit(Player player);
	
	public abstract void respawn(Player player);
	
	public abstract void broadcastMessage(Messages message, Player target);
	
}
