package net.pgfmc.core.backup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.Main;

/**
 * Backup command
 * Will save then backup the server and include a YML file with relevant information
 * YML contains Sender, Sender's UUID, Date, and additional notes
 * 
 * Restart.java will also create a backup and restart the server
 * @author bk
 *
 */
public class Backup implements CommandExecutor {
	
	/*
	 * List of all Backup objects
	 */
	public static List<Backup> BACKUPS = new ArrayList<Backup>();
	
	/*
	 * Information for the YML file
	 */
	protected HashMap<String, String> backup = new HashMap<String, String>();
	
	/*
	 * CommandSender, defaults to console
	 */
	protected CommandSender sender = Bukkit.getConsoleSender();
	
	/*
	 * Formatted date as a String
	 */
	protected final String date = new SimpleDateFormat("MMM dd, YYYY @ kkmm").format(new Date()); // Jan, 01, 2022 @ 0330
	
	/**
	 * Creates a Backup object, but does not start the backup
	 * The backupconfirm command will double check against the UUID value
	 * 
	 * @param sender Sender of the command
	 * @param uuid UUID of the Sender
	 * @param notes Additional notes, optional
	 */
	public Backup(CommandSender sender, String uuid, String notes)
	{
		this.sender = sender;
		backup.put("Sender", sender.getName());
		backup.put("UUID", uuid);
		backup.put("Date", date);
		backup.put("Notes", notes);
	}
	
	public Backup() {}
	
	/**
	 * Backup command, extra arguments count as part of the notes, arguments optional
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		String uuid = "Console";
		if (sender instanceof Player) { uuid = ((Player) sender).getUniqueId().toString(); }
		
		String notes = "(none)";
		if (args.length != 0) { notes = StringUtils.join(args, " "); }
		
		sender.sendMessage("§6Creating a backup will restart the server."
				+ "\nType §f§o/backupconfirm §r§6to backup."
				+ "\n\nPath: §f§o" + Main.backupPath + date
				+ "\n§r§6Notes: §f§o" + notes);
		
		BACKUPS.add(new Backup(sender, uuid, notes));
		
		return true;
	}
	
	public void backup()
	{
		Backups.backup(this);
	}
	
}
