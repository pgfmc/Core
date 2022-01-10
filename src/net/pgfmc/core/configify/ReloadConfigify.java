package net.pgfmc.core.configify;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfigify implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 1 && args[0].equals("reload"))
		{
			Configify.reloadConfigs();
		}
		
		return true;
	}
	
	public void init()
	{
		Configify.reloadConfigs();
	}
}
