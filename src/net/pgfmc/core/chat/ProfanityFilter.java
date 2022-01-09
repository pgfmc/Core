package net.pgfmc.core.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.Mixins;
import net.pgfmc.core.configify.Configify;

public class ProfanityFilter extends Configify {
	
	private static List<String> nword = new ArrayList<String>();

	@Override
	public void reload() {
		nword = Configify.setDefaultValue(
				Mixins.getDatabase(CoreMain.plugin.getDataFolder() + "profanity")
				, "profantities", new ArrayList<String>());
	}
	
	public static List<String> getFilter()
	{
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
