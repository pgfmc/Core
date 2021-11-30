package net.pgfmc.core.playerdataAPI;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.pgfmc.core.Main;
import net.pgfmc.core.Mixins;
import net.pgfmc.core.roles.Roles.Role;

/**
 * stores dynamic, temporary and non-temporary data for each player.
 * @author CrimsonDart
 */
public class PlayerData {
	
	// fields
	
	/**
	 * Hashmap to contain all instances of PlayerData, so they can be accesed.
	 */
	private static HashMap<UUID, PlayerData> instances = new HashMap<UUID, PlayerData>();
	
	UUID uuid;
	private HashMap<String, Object> data = new HashMap<String, Object>();
	
	private List<String> queue = new LinkedList<String>();
	
	public static void set(Consumer<PlayerData> consoomer) {
		
		for (PlayerData pd: getPlayerDataList()) {
			consoomer.accept(pd);
		}
	}
	
	public static void save(Consumer<PlayerData> consoomer) {
		
	}
	
	/**
	 * Creates a new PlayerData for anyone who joins the server for the first time.
	 * @param p Player who joined.
	 */
	public PlayerData(OfflinePlayer p) {
		uuid = p.getUniqueId();
		
		PlayerData pd = getPlayerData(p);
		if (pd == null) {
			
			uuid = p.getUniqueId();
			instances.put(uuid, this);
		}
		System.out.println(p.getName() + " has been loaded!");
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
	
	public static PlayerData getPlayerData(UUID id) {
		Objects.requireNonNull(id);
		for (UUID uid : instances.keySet()) {
			if (id.toString().equals(uid.toString())) {
				return instances.get(uid);
			}
		}
		return null;
	}
	
	public static PlayerData getPlayerData(String name) {
		for (Entry<UUID, PlayerData> uid : instances.entrySet()) {
			if (uid.getValue().getName().equals(name)) {
				return uid.getValue();
			}
		}
		return null;
	}
	
	// getters and setters
	
	/**
	 * Returns the player's name.
	 * @return
	 */
	public String getName() {
		return getOfflinePlayer().getName();
	}
	
	public String getRankedName() {
		return getRankColor() + getName() + "§r";
	}
	
	/**
	 * Returns the player, cast to OfflinePlayer.
	 * @return the player, cast to OfflinePlayer.
	 * 
	 */
	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(uuid);
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}
	
	/**
	 * Returns the Player's UUID.
	 * @return the Player's UUID.
	 */
	public UUID getUniqueId() {
		return uuid;
	}
	
	public void sendMessage(String message) {
		if (getOfflinePlayer().getPlayer() != null) {
			getOfflinePlayer().getPlayer().sendMessage(message);
		}
	}
	
	public boolean isOnline() {
		return getOfflinePlayer().isOnline();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof OfflinePlayer) {
			return (((OfflinePlayer) o).getUniqueId().toString().equals(uuid.toString()));
		} else if (o instanceof PlayerData) {
			return (((PlayerData) o).getUniqueId().toString().equals(uuid.toString()));
		}
		return false;
	}
	
	public boolean equals(PlayerData o) {
		return o.getUniqueId().toString().equals(uuid.toString());
	}
	
	
	public void teleport(Entity o) {
		getPlayer().teleport(o);
	}
	
	public void teleport(Location o) {
		getPlayer().teleport(o);
	}
	
	public void teleport(PlayerData o) {
		getPlayer().teleport(o.getPlayer());
	}
	
	/*
	 * This should be called from the Survival addon
	 */
	@Deprecated
	public void tempProtect(int ticks)
	{
		// SpawnProtection.TEMP_PROTECT(getPlayer(), ticks);
	}
	
	public void playSound(Location location, Sound sound, float volume, float pitch) {
		if (isOnline()) {
			getPlayer().playSound(location, sound, volume, pitch);
		}
	}
	
	public void playSound(Sound sound) {
		if (isOnline()) {
			getPlayer().playSound(getPlayer().getLocation(), sound, 1f, 1f);
		}
	}
	
	public String getRankColor() {
		return Role.getDominant(getData("Roles")).getColorCode();
	}
	
	/**
	 * Sets a player's data, and saves it in RAM.
	 * @param p The player
	 * @param n Name of the data; also the name used to call the data
	 * @param d The data itself
	 * @param queue Should the data be saved to a file later
	 */
	public static Queueable setData(OfflinePlayer p, String n, Object d) { // static function that passes to non-static setData
		if (p == null) {
			return null;
		}
		return getPlayerData(p).setData(n, d);
	}
	
	/**
	 * Sets a player's data, and saves it in RAM.
	 * @param n Name of the data; also the name used to call the data
	 * @param d The data itself
	 * @param queue Should the data be saved to a file later
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
		
		String data;
		
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
		return Mixins.getDatabase(Main.PlayerDataPath + "\\" + getUniqueId().toString() + ".yml").get(path);
	}
	
	public FileConfiguration loadFile() {
		return Mixins.getDatabase(Main.PlayerDataPath + "\\" + getUniqueId().toString() + ".yml");
	}
	
	/**
	 * Saves A Player's data.
	 * @author bk
	 * @param path Name of the data saved.
	 * @param payload The data saved.
	 */
	public void saveToFile(String path, Object payload) {

		FileConfiguration database = Mixins.getDatabase(Main.PlayerDataPath + "\\" + getUniqueId().toString() + ".yml");
		database.set(path, payload);
		//queue.forEach((s, o) -> database.set(s, o));
		//queue.clear();
		System.out.println("Queue saved to system!");
		
		Mixins.saveDatabase(database, Main.PlayerDataPath + "\\" + getUniqueId().toString() + ".yml");
	}
	
	public static void initializeQ() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			
			@Override
			public void run() {
				saveQ();
			}
			
		}, 20 * 60);
	}
	
	public static void saveQ() {
		for (PlayerData pd : getPlayerDataList()) {
			for (String key : pd.queue) {
				pd.saveToFile(key, pd.getData(key));
			}
			pd.queue.clear();
		}
		System.out.println("Queue has been saved.");
	}
	
	public static List<PlayerData> getPlayerDataList() {
		return instances.entrySet().stream().map((x) -> x.getValue()).collect(Collectors.toList());
	}

	public static int size() {
		return instances.size();
	}
	
	public static Stream<PlayerData> stream() {
		return StreamSupport.stream(getPlayerDataList().spliterator(), false);
	}
	
	
}