package net.pgfmc.core.discord;

import java.io.File;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.pgfmc.core.Main;
import net.pgfmc.core.Mixins;
import net.pgfmc.core.chat.ChatEvents;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.roles.Permissions;
import net.pgfmc.core.roles.Roles;
import net.pgfmc.core.roles.Roles.Role;

public class DiscordListener implements EventListener {

	@Override
	public void onEvent(GenericEvent e) {
		
		
		// Message Event
		if (e instanceof MessageReceivedEvent) {
			MessageReceivedEvent m = (MessageReceivedEvent) e;
			
			if (m.getChannel().getId().equals(Discord.SERVER_CHANNEL) && !m.getAuthor().getId().equals("721949520728031232")) {
				
				// when a message is sent by a user in #server.
				
				String s = m.getMessage().getContentDisplay();
				if (s.length() == 0) {return;}
				
				Role r = Role.getDominantOf(m.getMember().getRoles().stream().map(x -> x.getId()).collect(Collectors.toList()));
				
				s = format(s, "\\*\\*\\*", "§l§o");
				s = format(s, "\\*\\*", "§l");
				s = format(s, "\\*", "§o");
				s = format(s, "__", "§n");
				
				Bukkit.getServer().broadcastMessage(r.getColorCode() + m.getMember().getEffectiveName() + " §r§8-|| " + ChatEvents.getMessageColor(m.getMember().getId()) + s);
				return;
				
			} else if (m.getChannelType() == ChannelType.PRIVATE && !m.getAuthor().getId().equals("721949520728031232")) {
				
				// when a Direct Message is sent to the bot.
				
				String c = m.getMessage().getContentRaw();
				c.strip().substring(0, 3);
				
				int code = Integer.parseInt(c);

				for (PlayerData Pd : PlayerData.getPlayerDataList()) {
					
					if (Pd.getData("linkCode") != null && (int) Pd.getData("linkCode") == code) {
						Pd.setData("linkCode", null);
						Pd.setData("Discord", m.getAuthor().getId()).save();
						m.getChannel().sendMessage("Your Account has been linked to " + Pd.getName() + ".").queue();
						
						Roles.recalculateRoles(Pd);
						Permissions.recalcPerms(Pd);
						Pd.sendMessage("§aYour roles have been updated!");
						return;
					}
				}
				
			} else if (m.getChannel().getId().equals(Discord.SERVER_CHANNEL) && m.getAuthor().getId().equals("721949520728031232")) {
				if (m.getMessage().getContentRaw().equals(Discord.STOP_MESSAGE)) {
					
					
					FileConfiguration database = Mixins.getDatabase(Main.configPath);
					
					database.set("delete", m.getMessageId());
					
					Mixins.saveDatabase(database, Main.configPath);
					
					//Discord.JDA.shutdown();
					
					
				} else if (m.getMessage().getContentRaw().equals(Discord.START_MESSAGE)) {
					
					Main.action = x -> {
						m.getChannel().deleteMessageById(m.getMessageId()).queue();
						
					};
					
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
						
						@Override
						public void run()
						{
							Main.action.accept(null);
							Main.action = null;
						}
						
					}, 20 * 60);
				}
			}
		}
		
		// Ready Event
		
		else if (e instanceof ReadyEvent) {
			Discord.setChannel(Discord.JDA.getTextChannelById(Discord.SERVER_CHANNEL));
			
			// auto delete stuff down below :\
			FileConfiguration database = YamlConfiguration.loadConfiguration(new File(Main.plugin.getDataFolder() + "\\config.yml"));
			
			if (database.getString("delete") != null) {
				Discord.getChannel().deleteMessageById(database.getString("delete")).queue();
			}
			
			System.out.println("Discord Bot Initialized!");
			Discord.sendMessage(Discord.START_MESSAGE);
		}
	}
	
	private String format(String s, String ds, String mc) {
		
		String[] sa = s.split(ds);
		
		boolean mark = false;
		s = "";
		
		for (String S : sa) {
			
			if (mark) {
				s = s + mc + S + "§r";
				mark = false;
			} else {
				s = s + S;
				mark = true;
			}
		}
		return s;
	}
}
