package net.pgfmc.core.discord;

import java.util.Random;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class LinkCommand implements CommandExecutor {
	
	Random r = new Random();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
		
		if (sender instanceof Player) {
			
			PlayerData pd = PlayerData.getPlayerData((OfflinePlayer) sender);
			
			if (pd.getData("Discord") == null) {
				int code = Integer.parseInt(""  + r.nextInt(10) + r.nextInt(10) + r.nextInt(10) + r.nextInt(10));
				
				pd.setData("linkCode", code);
				sender.sendMessage("§6Private Message the code §f[ " + String.valueOf(code) + " ] §ato PGF.bot");
				sender.sendMessage("§ato link your accounts.");
				
				return true;
			} else {
				pd.sendMessage("§cYour Discord has already been linked.");
				pd.sendMessage("§cUse §r/unlink §cto unlink your account.");
				return true;
			}
		}
		sender.sendMessage("§Only players can execute this command.");
		return true;	
	}
}