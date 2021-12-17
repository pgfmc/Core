package net.pgfmc.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.server.ServerLoadEvent.LoadType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import net.pgfmc.bot.Discord;
import net.pgfmc.core.backup.Backup;
import net.pgfmc.core.backup.Backupconfirm;
import net.pgfmc.core.backup.Restore;
import net.pgfmc.core.cmd.Blocked;
import net.pgfmc.core.cmd.Goto;
import net.pgfmc.core.cmd.admin.Day;
import net.pgfmc.core.cmd.admin.Debug;
import net.pgfmc.core.cmd.admin.DimToggle;
import net.pgfmc.core.cmd.admin.Fly;
import net.pgfmc.core.cmd.admin.Gamemode;
import net.pgfmc.core.cmd.admin.God;
import net.pgfmc.core.cmd.admin.Heal;
import net.pgfmc.core.cmd.admin.Skull;
import net.pgfmc.core.cmd.admin.Sudo;
import net.pgfmc.core.cmd.admin.Vanish;
import net.pgfmc.core.cmd.donator.Nick;
import net.pgfmc.core.configify.ReloadConfigify;
import net.pgfmc.core.inventoryAPI.InventoryPressEvent;
import net.pgfmc.core.permissions.Permissions;
import net.pgfmc.core.permissions.Roles;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.playerdataAPI.PlayerDataManager;

/**
 * @author bk and CrimsonDart
 */
public class Main extends JavaPlugin implements Listener {
	
	public static String configPath;
	public static String PlayerDataPath;
	public static final String currentSeason = "Season 10";
	public static final String homeDir = "C:" + File.separator + "Users" + File.separator + "pgfmc"
			+ File.separator + "PGF" + File.separator;
	// "Print Working Directory" gets the working directory of the server
	public static String pwd;
	public static String backupDir;
	
	public static Main plugin;
	public static Scoreboard scoreboard;
	
	public enum Machine {
		MAIN,
		TEST,
		JIMBO,
		CRIMSON
	};
	
	public static Machine machine;
	
	/**
	 * creates all files, loads all worlds, PlayerData, commands and events.
	 * @author bk
	 */
	@Override
	public void onEnable()
	{ 
		// defines all constants for the plugin
		plugin = this;
		
		pwd = Main.plugin.getServer().getWorldContainer().getAbsolutePath();
		configPath = Main.plugin.getDataFolder() + File.separator + "config.yml";
		PlayerDataPath = Main.plugin.getDataFolder() + File.separator + "playerData";
		backupDir =  homeDir + ".uploads" + File.separator
				+ "bk" + File.separator + "Survival" 
				+ File.separator + currentSeason + File.separator;
		
		switch (this.getServer().getPort()) {
		case 25566: machine = Machine.TEST; break;
		case 25567: machine = Machine.JIMBO; break;
		default: machine = Machine.MAIN; break;
		}
		
		// Tries to initialize discord integration
		try {
			Discord.initialize();
		} catch (LoginException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		// makes sure all files exist
		Mixins.getFile(configPath);
		new File(PlayerDataPath).mkdirs();
		
		// scoreboard stuff
		Scoreboard scorebored = Bukkit.getScoreboardManager().getNewScoreboard();
		scorebored.registerNewTeam("survival");
		scoreboard = scorebored;
		
		// loads PlayerData
		
		PlayerDataManager.setInit(x -> x.setData("AFK", false));
		PlayerDataManager.setInit(x -> Roles.recalculateRoles(x));
		PlayerDataManager.setInit(pd -> {
			
			Map<String, Location> homes = new HashMap<>();
			FileConfiguration db = pd.loadFile();
			
			if (db == null)
			{
				pd.setData("homes", homes);
				return;
			}
			
			ConfigurationSection config = db.getConfigurationSection("homes");
			
			if (config != null)
			{
				config.getKeys(false).forEach(home -> {
					homes.put(home, config.getLocation(home));
				});
			}
			
			pd.setData("homes", homes);
		});
		PlayerDataManager.setInit(pd -> {
			
			FileConfiguration db = pd.loadFile();
			
			if (db == null)
			{
				pd.setData("nick", null);
				return;
			}
			
			ConfigurationSection config = db.getConfigurationSection("nick");
			
			if (config != null)
			{
				config.getKeys(false).forEach(nick -> {
					pd.setData("nick", nick);
				});
			}
		});
		
		DimManager.updateConfigForWorldPermissionAccess();
		
		
		
		//checks for the team "survival"
		//if it doesnt exist, it creates a new team with the same name.
		
		getCommand("goto").setExecutor(new Goto());
		
		// Makes it so you can /<world> if you want instead of /goto ((	PROBABLY DOESN'T WORK ))
		// getCommand("goto").setAliases(DimManager.getAllWorldNames()); // /hub, /creative, /<world>
		
		
		
		
		getCommand("gmc").setExecutor(new Gamemode());
		getCommand("gms").setExecutor(new Gamemode());
		getCommand("gma").setExecutor(new Gamemode());
		getCommand("gmsp").setExecutor(new Gamemode());
		
		getCommand("vanish").setExecutor(new Vanish());

		getCommand("fly").setExecutor(new Fly());
		getCommand("god").setExecutor(new God());
		getCommand("sudo").setExecutor(new Sudo());
		getCommand("heal").setExecutor(new Heal());
		
		getCommand("block").setExecutor(new Blocked());
		
		
		getCommand("debug").setExecutor(new Debug());
		
		
		
		getCommand("backup").setExecutor(new Backup());
		getCommand("backupconfirm").setExecutor(new Backupconfirm());
		getCommand("restore").setExecutor(new Restore());
		
		getCommand("day").setExecutor(new Day());
		
		getCommand("dimtoggle").setExecutor(new DimToggle());
		
		getCommand("nick").setExecutor(new Nick());
		
		getCommand("skull").setExecutor(new Skull());
		
		//getCommand("pgf").setExecutor(new ReloadConfigify());
		
		getServer().getPluginManager().registerEvents(new ChatEvents(), this);
		
		
		getServer().getPluginManager().registerEvents(new InventoryPressEvent(), this);
		getServer().getPluginManager().registerEvents(new Fly(), this);
		getServer().getPluginManager().registerEvents(new God(), this);
		getServer().getPluginManager().registerEvents(new Vanish(), this);
		getServer().getPluginManager().registerEvents(new Permissions(), this);
		
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new PlayerDataManager(), this);
		
		
		System.out.println(Bukkit.getServer().getCommandAliases());
		
		new Restart().init(); // Starts auto restart
		// new Homes().init(); // Loads all homes into memory
		// new Nick().init(); // Loads all nicknames into memory
		new ReloadConfigify().init();
	}
	
	@Override
	public void onDisable() {
		
		PlayerDataManager.saveQ();
		
		String msg = "";
		
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			
			msg += "<:LEAVE:905682349239463957> " + p.getName() + "\n";
		}
		
		Discord.sendMessage(msg);
		Discord.sendMessage(Discord.STOP_MESSAGE);
		
		
		//FileConfiguration database = Mixins.getDatabase(EssentialsMain.configPath);
		
		//database.set("delete", Discord.sendMessage(Discord.STOP_MESSAGE).getId());
		
		//Mixins.saveDatabase(database, EssentialsMain.configPath);
		
		if (action != null) {
			action.accept(null);
		}
	}
	
	public static Consumer<Random> action;
	
	@EventHandler
	public void onLoad(ServerLoadEvent e) {
		
		if (e.getType() == LoadType.STARTUP) {
			PlayerDataManager.InitializePD();
		}

	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (PlayerData.getPlayerData(e.getPlayer()) == null) {
			new PlayerData(e.getPlayer());
		}
	}
}

