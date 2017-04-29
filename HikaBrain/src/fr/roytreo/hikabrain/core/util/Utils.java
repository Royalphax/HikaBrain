package fr.roytreo.hikabrain.core.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.roytreo.hikabrain.core.HikaBrainPlugin;
import fr.roytreo.hikabrain.core.handler.Sounds;

public class Utils {
	
	public static void playSoundAll(Location location, Sounds sound, float volume, float pitch)
	{
		for (Player online : Bukkit.getOnlinePlayers())
			online.playSound((location == null ? online.getLocation() : location), sound.bukkitSound(), volume, pitch);
	}
	
	public static void playSound(Player player, Location location, Sounds sound, float volume, float pitch)
	{
		player.playSound((location == null ? player.getLocation() : location), sound.bukkitSound(), volume, pitch);
	}

	public static void registerException(Exception ex, Boolean cast)
	{
		HikaBrainPlugin instance = HikaBrainPlugin.getInstance();
		if (cast)
			instance.getLogger().warning("An error occured: " + ex.getMessage() + " || Contact the developer if you don't understand what happened");
		BufferedWriter writer = null;
        try {
            String timeLog = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
            File folder = new File(instance.getDataFolder(), "reports/");
            if (!folder.exists()) folder.mkdirs();
            File logFile = new File(instance.getDataFolder(), "reports/" + timeLog + ".txt");

            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));
            
            try {
            	writer = new BufferedWriter(new FileWriter(logFile, true));
				writer.write(errors.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
        } finally {
            try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	public static void registerException(Throwable th, Boolean cast)
	{
		HikaBrainPlugin instance = HikaBrainPlugin.getInstance();
		if (cast)
			instance.getLogger().warning("An error occured: " + th.getMessage() + " || Contact the developer if you don't understand what happened");
		BufferedWriter writer = null;
        try {
            String timeLog = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
            File folder = new File(instance.getDataFolder(), "reports/");
            if (!folder.exists()) folder.mkdirs();
            File logFile = new File(instance.getDataFolder(), "reports/" + timeLog + ".txt");

            StringWriter errors = new StringWriter();
            th.printStackTrace(new PrintWriter(errors));
            
            try {
            	writer = new BufferedWriter(new FileWriter(logFile, true));
				writer.write(errors.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
        } finally {
            try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	public static Location toLocation(final String string) {
        final String[] splitted = string.split("_");
        World world = Bukkit.getWorld(splitted[0]);
        if (world == null || splitted.length < 6) {
            world = Bukkit.getWorlds().get(0);
        }
        return new Location(world, Double.parseDouble(splitted[1]), Double.parseDouble(splitted[2]), Double.parseDouble(splitted[3]), Float.parseFloat(splitted[4]), Float.parseFloat(splitted[5]));
    }
    
    public static String toString(final Location location) {
        final World world = location.getWorld();
        return String.valueOf(world.getName()) + "_" + location.getX() + "_" + location.getY() + "_" + location.getZ() + "_" + location.getYaw() + "_" + location.getPitch();
    }
    
    
    public static boolean inventoryContains(Player player, Material mat)
    {
    	for (ItemStack item : player.getInventory().getContents())
    	{
    		if (item == null) continue;
    		if (item.getType() == mat)
    			return true;
    	}
    	return false;
    }
}
