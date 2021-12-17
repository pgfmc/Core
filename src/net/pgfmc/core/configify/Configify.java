package net.pgfmc.core.configify;

import java.util.LinkedList;

public abstract class Configify {
	
	public static LinkedList<Configify> configs = new LinkedList<Configify>();
	
	public Configify()
	{
		configs.add(this);
	}
	
	/**
	 * Reload method, set variables and stuff when called (from file)
	 */
	public abstract void reload();
	
	public static void reloadConfigs()
	{
		configs.forEach(c -> c.reload());
	}
}
