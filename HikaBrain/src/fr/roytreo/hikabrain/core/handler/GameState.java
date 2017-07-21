package fr.roytreo.hikabrain.core.handler;

public enum GameState {

	WAITING(Messages.WAITING, (byte) 5),
	STARTING(Messages.STARTING, (byte) 1),
	INGAME(Messages.INGAME, (byte) 14),
	ENDING(Messages.ENDING, (byte) 4);
	
	private Messages message;
	private byte color;
	
	private GameState(Messages message, byte color) {
		this.message = message;
		this.color = color;
	}
	
	public Messages getMessage() {
		return this.message;
	}
	
	public byte getColor() {
		return this.color;
	}
}
