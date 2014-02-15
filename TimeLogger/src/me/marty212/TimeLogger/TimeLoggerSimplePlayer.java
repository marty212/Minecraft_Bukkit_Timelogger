package me.marty212.TimeLogger;

import java.util.Date;

public class TimeLoggerSimplePlayer {
	private final String name;
	private Date date;
	private long playerTime;
	private boolean login;
	public TimeLoggerSimplePlayer(String newName, Date newDate, long newPlayerTime, boolean isLogined) {
		name = newName;
		date = newDate;
		playerTime = newPlayerTime;
		login = isLogined;
	}
	public String getName()
	{
		return name;
	}
	public Date getDate()
	{
		return date;
	}
	public void setDate(Date newDate)
	{
		date = newDate;
	}
	public long getPT()
	{
		return playerTime;
	}
	public boolean getLoginStatus()
	{
		return login;
	}
	public void setLoginStatus(boolean newStatus)
	{
		login = newStatus;
	}
	public void addTime(long time)
	{
		playerTime += time;
	}
}
