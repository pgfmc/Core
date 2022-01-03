package net.pgfmc.core.cmd.donator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.pgfmc.core.playerdataAPI.PlayerData;

public class Nick implements CommandExecutor {
	
	/*
	 * Color codes and format codes
	 */
	public static final List<String> colors = new ArrayList<>(Arrays.asList("&0"
			, "&2", "&4", "&6", "&8"
			, "&a", "&c", "&e", "&1"
			, "&3", "&5", "&7", "&9"
			, "&b", "&d", "&d", "&f"));
	public static final List<String> formats = new ArrayList<>(Arrays.asList("&k"
			, "&m", "&o", "&l"
			, "&n", "&r", "&k"));

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
		{
			sender.sendMessage("§cOnly players can execute this command.");
			return true;
		}
		
		if (args.length == 0)
		{
			return false;
		}
		
		
		
		Player p = (Player) sender;
		PlayerData pd = PlayerData.getPlayerData(p);
		
		String nick = String.join("", args).replaceAll("[^A-Za-z0-9&]", "").replace("&k", "");
		String raw = removeCodes(nick);
		
		/*
		 * A raw length of 0 means the nickname had no content, just color codes (lmao)
		 */
		if (raw.length() <= 0)
		{
			sender.sendMessage("§cThe nickname must be more than just color codes!");
			return true;
		}
		
		/*
		 * The nickname without color codes must be less than 20 characters
		 */
		if (raw.length() > 20)
		{
			sender.sendMessage("§cThe max nickname length is 20!");
			return true;
		}
		
		/*
		 * If the raw nickname is "off" or "reset" or the player's name
		 * then it will reset the nickname to Player.getName()
		 */
		if (raw.equals("off") || raw.equals("reset") || nick.equals(p.getName()))
		{
			pd.setData("nick", null).queue();
			sender.sendMessage("§6Nickname changed to " + pd.getRankedName() + "§6!");
			
			return true;
		}
		
		/*
		 * No impostors
		 */
		for (OfflinePlayer op : Bukkit.getOfflinePlayers())
		{
			if (op.getUniqueId().equals(p.getUniqueId())) { continue; }
			
			if (op.getName().equals(raw) || removeCodes(
					(String) Optional.ofNullable(PlayerData.getData(op, "nick"))
					.orElse(""))
					.equals(raw))
			{
				sender.sendMessage("§cYou cannot have the same name as another player!");
				return true;
			}
		}
		
		
		
		
		
		
		pd.setData("nick", nick.replace("&", "§") + "§r").queue();
		sender.sendMessage("§6Nickname changed to " + pd.getRankedName() + "§6!");
		
		return true;
	}
	
	
	/**
	 * Removes the color codes and formatting codes
	 * from the String
	 * 
	 * @param nick String to remove codes from
	 * @return a "raw" form of "nick", no codes
	 */
	public static String removeCodes(String nick)
	{
		String nickname = nick.replace("§", "&");
		
		for (String code : colors)
		{
			nickname = nickname.replace(code, "");
		}
		
		for (String code : formats)
		{
			nickname = nickname.replace(code, "");
		}
		
		return nickname;
	}
	/**
	 * This prevents a player from
	 * having the same name/nickname as another player
	 * 
	 * @param p The sus player
	 * @return The name to use with color codes
	 */
	public static void removeImpostors(PlayerData pd)
	{
		String raw = removeCodes((String) Optional.ofNullable(PlayerData.getData(pd.getOfflinePlayer(), "nick")).orElse(pd.getName()));
		if (raw.equals(pd.getName())) { return; }
		
		for (OfflinePlayer op : Bukkit.getOfflinePlayers())
		{
			if (op.getUniqueId().equals(pd.getUniqueId())) { continue; }
			
			if (op.getName().equals(raw) || removeCodes(
					(String) Optional.ofNullable(PlayerData.getData(op, "nick"))
					.orElse(""))
					.equals(raw))
			{
				pd.setData("nick", null).queue();
				return;
			}
		}
	}
	
	// Annoying way to get a String from a file using PlayerData
	@Deprecated
	public void init()
	{
		
	}

}
