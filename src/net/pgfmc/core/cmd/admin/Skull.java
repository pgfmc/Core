package net.pgfmc.core.cmd.admin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Skull implements CommandExecutor {

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
			if (Bukkit.getPlayer(args[0]) == null)
			{
				p.sendMessage("§cCould not find player §6§n" + args[0]);
				return true;
			}
			
			p.getInventory().addItem(getHead(Bukkit.getPlayer(args[0]).getUniqueId()));
			
			return true;
		}
		
		p.getInventory().addItem(getHead(p.getUniqueId()));
		
		return true;
	}
	
	public static ItemStack getHead(UUID player)
	{
		// Credit: https://www.spigotmc.org/threads/skullmeta-with-custom-skin-in-gui-1-14-4.403014/#post-3598618
		
		ItemStack item = new ItemStack(Material.PLAYER_HEAD); // Create a new ItemStack of the Player Head type.
		SkullMeta meta = (SkullMeta) item.getItemMeta(); // Get the created item's ItemMeta and cast it to SkullMeta so we can access the skull properties
		meta.setOwningPlayer(Bukkit.getOfflinePlayer(player)); // Set the skull's owner so it will adapt the skin of the provided username (case sensitive).
		item.setItemMeta(meta); // Apply the modified meta to the initial created item
		
		return item;
	}

}
