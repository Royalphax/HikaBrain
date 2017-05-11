package fr.roytreo.hikabrain.core.handler;

public enum GameState {

	WAITING(Messages.WAITING),
	STARTING(Messages.STARTING),
	INGAME(Messages.INGAME),
	ENDING(Messages.ENDING);
	
	private Messages message;
	
	private GameState(Messages message) {
		this.message = message;
	}
	
	public Messages getMessage() {
		return this.message;
	}
}
