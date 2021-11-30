package net.pgfmc.core.backup;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import net.pgfmc.core.Main;
import net.pgfmc.core.Mixins;

public class Backups {
	
	/**
	 * Creates the backup with information from the provided Backup object
	 * Will restart the server immediately after saving the backup
	 * 
	 * Time it takes to copy depends on world size
	 * ~10 seconds for small world
	 * 
	 * @param b A Backup object
	 */
	public static void backup(Backup b)
	{
		System.out.println("Creating thread.");
		
		/*
		 * Save the server before backing up
		 * Not doing this results in faulty backups
		 */
		Bukkit.getScheduler().callSyncMethod(Main.plugin, () -> Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "save-all"));
		// Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "save-all");
		
		/*
		 * Creates a new thread to run this on, makes it so server doesn't crash lol (jk idk how it works)
		 */
		Thread thread = new Thread() {
			public void run() {				
				try {
					String sourcePath = "C:\\Users\\bk\\Desktop\\servers\\test";
					String destPath = "C:\\Users\\bk\\Desktop\\Backup\\" + b.backup.get("Date") + "\\";
					File source = new File(sourcePath);
					File dest = new File(destPath);
					dest.mkdirs();
					
					/*
					 * Copy all files and directories from source to dest
					 */
					FileUtils.copyDirectoryToDirectory(source, dest);
					
					/*
					 * Uses info from the Backup object to create a YML with
					 * helpful information
					 */
					FileConfiguration info = Mixins.getDatabase(destPath + "info.yml");
					info.set("info", b.backup);
					
					Mixins.saveDatabase(info, destPath + "info.yml");
					
					System.out.println("Successfully created backup at \"" + destPath + "\"."
							+ "\nRestarting the server now.");
					
					/*
					 * Shutdown the server
					 * Does not shutdown if the backup fails
					 */
					Bukkit.getServer().shutdown();
					
				} catch (IOException e) {
					System.out.println("Failed to create backup, some files may have copied over.");
					b.sender.sendMessage("§cFailed to create backup, some files may have copied over."
							+ "\nNot restarting the server.");
					e.printStackTrace();
				}
				
				System.out.println("Thread has ended.");
			}
			
		};
		
		
		thread.start();
	}
	
	/**
	 * Used for creating a fast backup with default values
	 */
	public static void backup()
	{
		backup(new Backup(Bukkit.getConsoleSender(), "Console", "Auto save"));
	}
	
	public static void restore()
	{
		// TODO
	}

}
