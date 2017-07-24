package fr.roytreo.hikabrain.core.version;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface ATitleUtils {

	public void titlePacket(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle);

	public void tabPacket(Player player, String footer, String header);
	
	public void actionBarPacket(Player player, String message);
	
	public default void defaultTitle(Type type, Player player, Object... obj) {
		try {
			switch (type)
			{
			case ACTION:
				actionBarPacket(player, (String) obj[0]);
				break;  
			case TAB:
				tabPacket(player, (String) obj[0], (String) obj[1]);
				break;
			case TITLE:
				titlePacket(player, 10, 40, 10, (String) obj[0], (String) obj[1]);
				break;
			}
		} catch (java.lang.Exception e) {
			// Do nothing
		}
	}

	public default void broadcastDefaultTitle(Type type, Object... obj) {
		for (Player online : Bukkit.getOnlinePlayers())
			defaultTitle(type, online, obj);
	}
	
	public enum Type {
		TITLE(0),
		TAB(1),
		ACTION(2);
		
		private int id;
		private Type(int id)
		{
			this.id = id;
		}
		
		public int getId() {
			return this.id;
		}
	}
}
