package net.pgfmc.core.configify;

import java.util.LinkedList;

import org.bukkit.configuration.file.FileConfiguration;

public abstract class Configify {
	
	public static LinkedList<Configify> configs = new LinkedList<>();
	
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
		configs.stream().forEach(c -> c.reload());
	}
	
	/**
	 * Sets a default value in the yml if the given key returns null
	 * 
	 * @param <T> Generic type
	 * @param file The file to check
	 * @param key The key to check
	 * @param value The default value to set if file.get(key) is null
	 * @return The value from file.get(key) or, if null, value
	 */
	public static <T> T setDefaultValue(FileConfiguration file, String key, T value)
	{
		@SuppressWarnings("unchecked")
		T obj = (T) file.getObject(key, value.getClass());
		if (obj == null)
		{
			file.set(key, value);
			return value;
		}
		
		return obj;
	}
}
