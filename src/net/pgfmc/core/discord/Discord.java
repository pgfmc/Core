package net.pgfmc.core.discord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class Discord extends ListenerAdapter {
	
	public static JDA JDA;
	public static String PREFIX = "!";
	public static List<String> ADMINS = new ArrayList<>(Arrays.asList("243499063838769152", "440726027405361152")); // bk, Crimsona
	private static TextChannel serverChannel;
	
	public static final String SERVER_CHANNEL = "771247931005206579";
	public static final String START_MESSAGE = "<:START:905682398790959125> Server has started!";
	public static final String STOP_MESSAGE = "<:STOP:905683316844429312> Server is stopping...";
	
	public static void initialize() throws LoginException, InterruptedException {
		JDABuilder builder = JDABuilder.createDefault(new Secret().getKey()); // bot token, don't share.
		
		builder.addEventListeners(new DiscordListener());
		
		// creates JDA and allows the bot to load all members 
		JDA = builder
				.setChunkingFilter(ChunkingFilter.ALL)
				.setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();
		JDA.awaitReady();
	}
	
	public static void sendMessage(String m) {
		serverChannel.sendMessage(m).queue();
		System.out.println("Discord: " + m);
	}
	
	protected static void setChannel(TextChannel server) {
		if (serverChannel == null) {
			serverChannel = server;
		}
	}
	
	public static TextChannel getChannel() {
		return serverChannel;
	}
}
