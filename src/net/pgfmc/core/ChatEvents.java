package net.pgfmc.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.pgfmc.bot.Discord;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.survival.cmd.home.SetHome;

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
		
		// For homes
		if (pd.getData("tempHomeLocation") != null) {
			
			e.setCancelled(true);
			SetHome.setHome(pd.getPlayer(), e.getMessage(), pd.getData("tempHomeLocation"));
			pd.setData("tempHomeLocation", null);
			
			return;
		}
		
		e.setFormat(pd.getRankedName() + "§8 -> " + getMessageColor(e.getPlayer().getUniqueId().toString()) + e.getMessage());
		
		Discord.sendMessage(pd.getNickname(true) + " -> " + e.getMessage());
		System.out.println(pd.getNickname(true));
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		if (pd == null) {
			pd = new PlayerData(e.getPlayer());
		}
		
		e.setJoinMessage("§7[§a+§7]§r " + pd.getRankedName());
		Discord.sendMessage("<:JOIN:905023714213625886> " + pd.getNickname(true));
		//Discord.sendEmbed(Discord.simplePlayerEmbed(pd.getPlayer(), "joined the server", Discord.green));
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		
		e.setQuitMessage("§7[§c-§7]§r " + PlayerData.getPlayerData(p).getRankedName());
		Discord.sendMessage("<:LEAVE:905682349239463957> " + PlayerData.getPlayerData(p).getNickname(true));
		//Discord.sendEmbed(Discord.simplePlayerEmbed(p, "left the server", Discord.red));
	}
	
	@EventHandler
	public void onDie(PlayerDeathEvent e) {
		PlayerData pd = PlayerData.getPlayerData(e.getEntity());
		
		String inDiscord = e.getDeathMessage().toString(); // ??? lololo
		inDiscord.replace(pd.getName(), pd.getNickname(true));
		
		e.setDeathMessage(e.getDeathMessage().replace(pd.getName(), pd.getRankedName()));
		
		Discord.sendMessage("<:DEATH:907865162558636072> " + inDiscord);
		//Discord.sendEmbed(Discord.simpleServerEmbed(e.getDeathMessage(), "https://cdn.discordapp.com/emojis/907865162558636072.png?size=44", Discord.black));
	}
	
	@EventHandler
	public void onAdvancementDone(PlayerAdvancementDoneEvent e) throws IOException {
		String logPath = Main.pwd + File.separator + "logs" + File.separator + "latest.log";
		
		List<String> all = Files.readAllLines(Paths.get(logPath), Charset.forName("Cp1252"));
		String adv = all.get(all.size() - 1);
		
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		if (adv.contains(" has made the advancement "))
		{
			adv = adv.substring(adv.lastIndexOf(pd.getName()), adv.length());
			adv.replace(pd.getName(), pd.getNickname(true));
			
			Discord.sendMessage(adv);
			System.out.println("Advancement Get!"); // DO NOT REMOVE THIS!!!!!!!!!!!!!!!! (IT BREAKS) XXX lol
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
