package me.marty212.TimeLogger;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

/**
 * Simple plugin to log join and quit events to a file for each user on display name (I think/hope).
 * 
 * 
 * @author Marty212
 *
 */
public class TimeLogger extends JavaPlugin {
	private final TimeLoggerListener listener = new TimeLoggerListener(this);
	private final HashMap<String, TimeLoggerSimplePlayer> playerList = new HashMap<String, TimeLoggerSimplePlayer>();
	@Override
	public void onDisable()
	{
		System.out.println("[TimeLogger] disabled.");
		Iterator<String> it = playerList.keySet().iterator();
		while(it.hasNext()){
			String play = it.next();
			if (playerList.get(play).getLoginStatus()){
				TimeLoggerSimplePlayer cPlay = playerList.get(play);
				cPlay.setLoginStatus(false);
				playerList.put(play, cPlay);
				Date date = new Date();
				printStatus(play, ": quit at " + new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z").format(date), date, true);
			}
		}
		File file = new File("plugins\\TimeLogger\\", "MasterLog.txt");
		try {
			PrintWriter out = new PrintWriter(file);
			Iterator<String> it2 = playerList.keySet().iterator();
			while (it2.hasNext()) {
				String playerName = it2.next();
				out.println(playerName + ":" + playerList.get(playerName).getPT());
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onEnable() {
		System.out.println("[TimeLogger] enabled.");
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, listener, Event.Priority.Monitor, this);
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_QUIT, listener, Event.Priority.Monitor, this);
		try {
			File dir = new File("plugins\\TimeLogger\\");
			if (!dir.exists()) {
				dir.mkdir();
			}
			File file = new File(dir, "MasterLog.txt");
			if (file.exists()){
				Scanner scan = new Scanner(file);
				while (scan.hasNextLine()){
					scan.useDelimiter(":");
					String userName = scan.next();
					long time = Long.parseLong(scan.next().trim()); //in seconds
					TimeLoggerSimplePlayer player = new TimeLoggerSimplePlayer(userName, null, time, false);
					playerList.put(userName, player);
				}
			}
			else {
				file.createNewFile();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	public void printStatus(String name, String status, Date date, Boolean logout) {
		try {
			File dir = new File("plugins\\TimeLogger\\");
			if (!dir.exists()) {
				dir.mkdir();
			}
			File file = new File(dir, name + ".txt");
			if (!file.exists()){
				file.createNewFile();
			}
			PrintWriter out = new PrintWriter(new FileWriter(file, true));
			out.println(name + status);
			out.close();
			if (logout) {
				TimeLoggerSimplePlayer player = playerList.get(name);
				player.addTime(diffDate(player.getDate(), date));
				player.setLoginStatus(false);
				player.setDate(null);
				playerList.put(name, player);
			} else {
				if (playerList.containsKey(name)) {
					TimeLoggerSimplePlayer player = playerList.get(name);
					player.setDate(date);
					player.setLoginStatus(true);
					playerList.put(name, player);
				}
				else {
					playerList.put(name, new TimeLoggerSimplePlayer(name,date, 0, true));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	private long diffDate(Date d1, Date d2)
	{
		return Math.abs(d1.getTime() - d2.getTime())/1000;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = null;
		if (sender instanceof Player)
			player = (Player) sender;
		try{
			if (cmd.getName().equalsIgnoreCase("getplayertime") && (player == null || player.hasPermission("TimeLogger.admin"))) {
				if (args.length > 0) {
					if(getServer().getPlayer(args[0]) == null) {
						if (playerList.containsKey(args[0]) && playerList.get(args[0]) != null){
							sender.sendMessage(args[0] + ":" + String.valueOf(playerList.get(args[0]).getPT()));
							return true;
						}
					}
					else {
						if (playerList.containsKey(args[0]) && playerList.get(args[0]) != null){
							sender.sendMessage(args[0] + ":" + String.valueOf(playerList.get(args[0]).getPT() + diffDate(playerList.get(args[0]).getDate(), new Date())));
							return true;
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
