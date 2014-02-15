package me.marty212.TimeLogger;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class TimeLoggerListener extends PlayerListener {
	private final TimeLogger timeLogger;
	/**
	 * Pretty obvious.
	 * @param log So it can print to the file.
	 */
	public TimeLoggerListener(TimeLogger log) {
		timeLogger = log;
	}
	/**
	 * Obvious.
	 */
	public void onPlayerJoin(PlayerJoinEvent event) {
		Date date = new Date();
		timeLogger.printStatus(event.getPlayer().getDisplayName(), ": joined at " + new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z").format(date), date, false);
	}
	/**
	 * Obvious.
	 */
	public void onPlayerQuit(PlayerQuitEvent event) {
		Date date = new Date();
		timeLogger.printStatus(event.getPlayer().getDisplayName(), ": quit at " + new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z").format(date), date, true);
	}
}
