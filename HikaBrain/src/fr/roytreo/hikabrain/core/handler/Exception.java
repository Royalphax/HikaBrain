package fr.roytreo.hikabrain.core.handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.bukkit.plugin.Plugin;

public class Exception {
	
	private Throwable throwable;

	public Exception(java.lang.Exception ex) {
		this.throwable = ex;
	}
	
	public Exception(Throwable th) {
		this.throwable = th;
	}
	
	public boolean register(Plugin plugin, boolean cast) {
		if (cast) {
			plugin.getLogger().warning("An error occured: " + throwable.getMessage());
			plugin.getLogger().info("Contact the developer if you don't know what happend");
		}
		String timeLog = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
        File folder = new File(plugin.getDataFolder(), "reports/");
        if (!folder.exists()) 
        	folder.mkdirs();
        File logFile = new File(plugin.getDataFolder(), "reports/" + timeLog + ".txt");

        StringWriter errors = new StringWriter();
        throwable.printStackTrace(new PrintWriter(errors));
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
			writer.write(errors.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        return true;
	}
}
