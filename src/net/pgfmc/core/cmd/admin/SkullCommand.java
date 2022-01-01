package net.pgfmc.core.cmd.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.misc.Skull;

public class SkullCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (args.length > 0)
		{
			Player player = Bukkit.getPlayer(args[0]);
			
			if (Bukkit.getPlayer(args[0]) == null)
			{
				p.sendMessage("§cCould not find player §6§n" + args[0]);
				return true;
			}
			
			String lore = null;
			if (args.length >= 2)
			{
				lore = String.join(" ", args).replace(args[0], "").replace("&", "§").strip();
			}
			
			p.getInventory().addItem(Skull.getHead(player.getUniqueId(), lore));
			
			return true;
		}
		
		p.getInventory().addItem(Skull.getHead(p.getUniqueId(), null));
		
		return true;
	}
}
