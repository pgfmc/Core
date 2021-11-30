package net.pgfmc.core.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.BroadcastMessageEvent;

import net.pgfmc.core.discord.Discord;
import net.pgfmc.core.playerdataAPI.PlayerData;

/**
 * Makes all the chat colorful :)
 * @author bk (basically CrimsonDart now)
 *
 */
public class ChatEvents implements Listener {
	
	private static boolean altColor = false;
	private static String lastSender = "null lol"; // lol (not null so no possible errors lol
	
	@EventHandler
	public void onMessage(AsyncPlayerChatEvent e)
	{
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		e.setFormat(pd.getRankedName() + "§8 -> " + getMessageColor(e.getPlayer().getUniqueId().toString()) + e.getMessage());
		
		Discord.sendMessage(pd.getName() + " -> " + e.getMessage());
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		if (pd == null) {
			pd = new PlayerData(e.getPlayer());
		}
		
		e.setJoinMessage("§7[§a+§7]§r " + pd.getRankColor() + pd.getName());
		Discord.sendMessage("<:JOIN:905023714213625886> " + pd.getName());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		
		e.setQuitMessage("§7[§c-§7]§r " + PlayerData.getPlayerData(p).getRankColor() + p.getName());
		Discord.sendMessage("<:LEAVE:905682349239463957> " + p.getName());
	}
	
	@EventHandler
	public void onDie(PlayerDeathEvent e) {
		Discord.sendMessage("<:DEATH:907865162558636072> " + e.getDeathMessage());
	}
	
	@EventHandler
	public void onAdvancement(BroadcastMessageEvent e) {
		
		System.out.println(e.getMessage());
		
		String s = e.getMessage();
		if (s.contains("has made the advancement")) {
			Discord.sendMessage(e.getMessage());
		}
	}
	
	public static String getMessageColor(String sender) {
		
		if (sender.equals(lastSender)) {
			return (altColor) ? "§7" : "§f";
			
		}
		lastSender = sender;
		altColor = !altColor;
		return (altColor) ? "§7" : "§f";
	}
	
}
