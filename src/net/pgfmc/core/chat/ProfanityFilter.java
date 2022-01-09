package net.pgfmc.core.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProfanityFilter {// extends Configify {
	
	private static List<String> nword = new ArrayList<String>();
	/*
	@Override
	public void reload() {
		nword = Mixins.getDatabase(CoreMain.plugin.getDataFolder() + File.separator + "profanity.yml").getStringList("profantity");
		System.out.println("New profanity list: " + nword);
	}
	*/
	
	public static List<String> getFilter()
	{
		// XXX remove the line under this once Configify works??
		// nword = Mixins.getDatabase(CoreMain.plugin.getDataFolder() + File.separator + "profanity.yml").getStringList("profantity");
		return nword;
	}
	
	/**
	 * Checks to see if a message has blacklisted words
	 * Doesn't stop everything though
	 * 
	 * @param message Message to be checked
	 * @return true if has profanity
	 */
	public static boolean hasProfanity(String message)
	{
		return !Collections.disjoint(Arrays.asList(message.split(" ")), nword);
	}

}
