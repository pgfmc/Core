package net.pgfmc.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Configify implements CommandExecutor, Mixins {
	
	public void init()
	{
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		sender.sendMessage("§aConfigs have been reloaded!");
		init();
		
		return true;
	}
	
}
