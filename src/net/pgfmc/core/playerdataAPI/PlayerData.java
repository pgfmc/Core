package net.pgfmc.core.playerdataAPI;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import net.pgfmc.core.cmd.donator.Nick;
import net.pgfmc.core.misc.CoreMain;
import net.pgfmc.core.misc.Mixins;
import net.pgfmc.core.permissions.PermissionsManager;

/**
 * stores dynamic, temporary and non-temporary data for each player.
 * @author CrimsonDart
 */
public final class PlayerData extends AbstractPlayerData {
	
	// fields
	
	/**
	 * Hashmap to contain all instances of PlayerData, so they can be accesed.
	 */
	private final static Set<PlayerData> instances = new HashSet<PlayerData>();
	private final static Set<PlayerData> debug = new HashSet<PlayerData>();
	
	private final HashMap<String, Object> data = new HashMap<String, Object>();
	protected final List<String> queue = new LinkedList<String>();
	
	/**
	 * Creates a new PlayerData for anyone who joins the server for the first time.
	 * @param p Player who joined.
	 */
	protected PlayerData(OfflinePlayer p) {
		super(p);
		
		PlayerData pd = getPlayerData(p);
		if (pd == null) {
			
			for (Consumer<PlayerData> consoomer : PlayerDataManager.pdInit) {
				consoomer.accept(this);
			}
			
			instances.add(this);
			System.out.println(p.getName() + " has been loaded!");
		}
	}
	
	// find PlayerData functions ------------------------------------------
	
	/**
	 * Gets a player's associated PlayerData.
	 * @param p The player.
	 * @return The Player's PlayerData class.
	 */
	public static PlayerData getPlayerData(OfflinePlayer p) { // gets a player's playerdata.
		return getPlayerData(p.getUniqueId());
	}
	
	/**
	 * Gets a player's PlayerData class.
	 * @param id The player's UUID.
	 * @return The player's PlayerData.
	 */
	public static PlayerData getPlayerData(UUID id) {
		Objects.requireNonNull(id);
		for (PlayerData uid : instances) {
			if (id.toString().equals(uid.getUniqueId().toString())) {
				return uid;
			}
		}
		return null;
	}
	
	/**
	 * Gets a player's PlayerData class.
	 * @param name The player's mc username.
	 * @return The player's PlayerData.
	 */
	public static PlayerData getPlayerData(String name) {
		for (PlayerData uid : instances) {
			if (uid.getName().equals(name)) {
				return uid;
			}
		}
		return null;
	}
	
	public static PlayerData getPlayerDataById(String discordUserId)
	{
		for (PlayerData uid : instances)
		{
			if (discordUserId.equals(uid.getData("Discord"))) return uid;
		}
		return null;
		
	}
	
	// getters and setters
	
<<<<<<< HEAD
<<<<<<< HEAD
	public String getRankedName()
	{
		Nick.removeImpostors(this);
		String name = getName();
		return (String) Optional.ofNullable(getData("nick")).orElse(name);
	}
	
	public String getNicknameRaw()
	{
		return Nick.removeCodes(getRankedName());
=======
=======
>>>>>>> parent of 90a268f (h)
	public String getNickname(boolean raw)
	{
		Nick.removeImpostors(this);
		
		Role role = Role.getDominant(getData("Roles"));
		String name = getOfflinePlayer().getName();
		
		// If role is Donator or higher
		if (role.getDominance() >= Role.DONATOR.getDominance())
		{
			String nick = (String) Optional.ofNullable(getData("nick")).orElse(name);
			if (raw)
			{
				return Nick.removeCodes(nick);
			} else
			{
				return nick;
			}
			
		}
		return name;
	}
	
	/**
	 * Gets the Player's Name, Formatted according to their Dominant Role.
	 * @return The player's name (or nickname if applicable) colored according to their role.
	 */
	public String getRankedName() {
		return getRankColor() + getNickname(false);
<<<<<<< HEAD
>>>>>>> parent of 90a268f (h)
=======
>>>>>>> parent of 90a268f (h)
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof OfflinePlayer) {
			return (((OfflinePlayer) o).getUniqueId().toString().equals(getUniqueId().toString()));
		} else if (o instanceof PlayerData) {
			return (((PlayerData) o).getUniqueId().toString().equals(getUniqueId().toString()));
		}
		return false;
	}
	
	/**
	 * Gets the Player's Role prefix.
	 * @return The player's role prefix.
	 */
	public String getRankColor() {
		// Role positions in list are guarenteed to be ordered by JDA, index 0 is top Role
		List<String> roles = getData("Roles");
		
		if (roles.isEmpty())
		{
			return PermissionsManager.getRolePrefix("Member");
		}
		
		return PermissionsManager.getRolePrefix(roles.get(0));
	}
	
	public void setDebug(boolean d) {
		if (d) {
			debug.add(this);
		} else {
			debug.remove(this);
		}
	}
	
	public boolean isDebug() {
		return (debug.contains(this));
	}
	
	public void toggleDebug() {
		setDebug(!isDebug());
	}
	
	public static void sendDebug(String message) {
		for (PlayerData pd : debug) {
			pd.sendMessage(message);
		}
	}
	
	/**
	 * Sets a player's data, and saves it in RAM.
	 * Returns a Queueable, which can be <.queue>ed to save to file.
	 * @param p The player
	 * @param n Name of the data; also the name used to call the data
	 * @param d The data itself
	 */
	public static Queueable setData(OfflinePlayer p, String n, Object d) { // static function that passes to non-static setData
		if (p == null) return null;
		return getPlayerData(p).setData(n, d);
	}
	
	/**
	 * Sets a player's data, and saves it in RAM.
	 * Returns a Queueable, which can be <.queue>ed to save to file.
	 * @param n Name of the data; also the name used to call the data
	 * @param d The data itself
	 */
	public Queueable setData(String n, Object d) { // sets a playerData point
		data.put(n, d);
		return new Queueable(n);
	}
	
	/**
	 * Queueable class
	 * 
	 * @author CrimsonDart
	 *
	 */
	public class Queueable {
		
		private String data;
		
		Queueable(String n) {
			data = n;
		}
		
		/**
		 * adds this data to the queue
		 */
		public void queue() {
			queue.add(data);
		}
		
		public void save() {
			saveToFile(data, getData(data));
		}
	}
	
	/**
	 * adds a data point to the queue.
	 * @param data The data point to be added to the queue.
	 * @return wether or not the addition was successful.
	 */
	public boolean addQ(String data) {
		if (data.contains(data)) {
			queue.add(data);
			return true;
		}
		return false;
	}
	
	/**
	 * removes a data point from the queue.
	 * @param data The data point to remove.
	 * @return Wether or not the removal was successful.
	 */
	public boolean removeQ(String data) {
		return queue.remove(data);
	}
	
	/**
	 * Gets a player's data. must be Cast to the Correct Class to be used, however.
	 * @param p The player
	 * @param n Name of the data.
	 * @return The data called by "n". Must be Cast to be used.
	 */
	public static <T> T getData(OfflinePlayer p, String n) { // static function that passes to non-static getData
		return getPlayerData(p).getData(n);
	}
	
	/**
	 * Gets a player's data. must be Cast to the Correct Class to be used, however.
	 * @param n Name of the data.
	 * @return The data called by "n". Must be Cast to be used.
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData(String n) { // gets a playerData point
		return (T) data.get(n);
	}
	
	/**
	 * @author bk
	 * @return a player's associated FileConfiguration from database.yml.
	 */
	public Object loadFromFile(String path) {
		return Mixins.getDatabase(CoreMain.PlayerDataPath + File.separator + getUniqueId().toString() + ".yml").get(path);
	}
	
	public FileConfiguration loadFile() {
		return Mixins.getDatabase(CoreMain.PlayerDataPath + File.separator + getUniqueId().toString() + ".yml");
	}
	
	/**
	 * Saves A Player's data.
	 * @author bk
	 * @param path Name of the data saved.
	 * @param payload The data saved.
	 */
	public void saveToFile(String path, Object payload) {

		FileConfiguration database = Mixins.getDatabase(CoreMain.PlayerDataPath + File.separator + getUniqueId().toString() + ".yml");
		database.set(path, payload);
		System.out.println("Queue saved to system!");
		
		Mixins.saveDatabase(database, CoreMain.PlayerDataPath + File.separator + getUniqueId().toString() + ".yml");
	}
	
	/**
	 * Returns a set containing all PlayerData.
	 * @return A set containing all PlayerData.
	 */
	public static Set<PlayerData> getPlayerDataSet() {
		return instances;
	}
	
	/**
	 * Returns a stream of each PlayerData.
	 */
	public static Stream<PlayerData> stream() {
		return StreamSupport.stream(getPlayerDataSet().spliterator(), false);
	}
	
	
}