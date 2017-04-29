package fr.roytreo.hikabrain.core.event;

import org.bukkit.event.Listener;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;

public class EventListener implements Listener
{
    protected HikaBrainPlugin plugin;
    
    protected EventListener(final HikaBrainPlugin plugin) {
        this.plugin = plugin;
    }
}
