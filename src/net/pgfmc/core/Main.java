package net.pgfmc.core;

import java.io.File;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

import javax.security.auth.login.LoginException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import net.pgfmc.core.admin.Day;
import net.pgfmc.core.admin.DimToggle;
import net.pgfmc.core.admin.Fly;
import net.pgfmc.core.admin.Gamemode;
import net.pgfmc.core.admin.God;
import net.pgfmc.core.admin.Heal;
import net.pgfmc.core.admin.Sudo;
import net.pgfmc.core.admin.Vanish;
import net.pgfmc.core.backup.Backup;
import net.pgfmc.core.backup.Backupconfirm;
import net.pgfmc.core.backup.Restore;
import net.pgfmc.core.chat.ChatEvents;
import net.pgfmc.core.dim.DimManager;
import net.pgfmc.core.dim.Goto;
import net.pgfmc.core.dim.Worlds;
import net.pgfmc.core.discord.Discord;
import net.pgfmc.core.discord.LinkCommand;
import net.pgfmc.core.discord.UnlinkCommand;
import net.pgfmc.core.inventoryAPI.InventoryPressEvent;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.requestAPI.Blocked;
import net.pgfmc.core.restart.Restart;
import net.pgfmc.core.roles.Permissions;
import net.pgfmc.core.roles.Roles;

/**
 * @author bk and CrimsonDart
 */
@SuppressWarnings("deprecation")
public class Main extends JavaPlugin {
	
	public static final String configPath = "plugins\\PGF-Essentials\\config.yml";
	public static final String PlayerDataPath = "plugins\\PGF-Essentials\\playerData";
	
	public static final String currentSeason = "Season 10";
	public static final String homeDir = "C:" + File.separator + "Users" + File.separator + "pgfmc"
			+ File.separator + "PGF" + File.separator;
	public static final String serverPath = homeDir + "Servers" + File.separator + "Main" + File.separator + "Survival" + File.separator;
	public static final String backupPath =  homeDir + ".uploads" + File.separator
	+ "bk" + File.separator + "Survival" + File.separator + currentSeason + File.separator;
	
	public static Main plugin;
	public static Scoreboard scoreboard;
	
	/**
	 * creates all files, loads all worlds, PlayerData, commands and events.
	 * @author bk
	 */
	@Override
	public void onEnable()
	{ 
		// defines all constants for the plugin
		plugin = this;
		
		// Tries to initialize discord integration
		try {
			Discord.initialize();
		} catch (LoginException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		// initializes all players' playerdatas
		Arrays.asList(Bukkit.getOfflinePlayers()).forEach(p -> new PlayerData(p));
		
		// makes sure all files exist
		Mixins.getFile(configPath);
		new File(PlayerDataPath).mkdirs();
		
		// scoreboard stuff
		Scoreboard scorebored = Bukkit.getScoreboardManager().getNewScoreboard();
		scorebored.registerNewTeam("survival");
		scoreboard = scorebored;
		
		// loads PlayerData
		
		PlayerData.initializeQ();
		
		PlayerData.set(x -> x.setData("AFK", false));
		PlayerData.set(x -> Roles.recalculateRoles(x));
		
		DimManager.updateConfigForWorldPermissionAccess();
		
		Worlds.SURVIVAL.set(Bukkit.getWorld("survival"));
		Worlds.SURVIVAL_NETHER.set(Bukkit.getWorld("survival_nether"));
		Worlds.SURVIVAL_END.set(Bukkit.getWorld("survival_the_end"));
		
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
		
		getCommand("link").setExecutor(new LinkCommand());
		getCommand("unlink").setExecutor(new UnlinkCommand());
		
		getCommand("backup").setExecutor(new Backup());
		getCommand("backupconfirm").setExecutor(new Backupconfirm());
		getCommand("restore").setExecutor(new Restore());
		
		getCommand("day").setExecutor(new Day());
		
		getCommand("dimtoggle").setExecutor(new DimToggle());
		
		getServer().getPluginManager().registerEvents(new ChatEvents(), this);
		
		getServer().getPluginManager().registerEvents(new InventoryPressEvent(), this);
		getServer().getPluginManager().registerEvents(new Fly(), this);
		getServer().getPluginManager().registerEvents(new God(), this);
		getServer().getPluginManager().registerEvents(new Vanish(), this);
		getServer().getPluginManager().registerEvents(new Permissions(), this);
		// getServer().getPluginManager().registerEvents(new Requester(), this); // Does not work brv
		
		
		
		System.out.println(Bukkit.getServer().getCommandAliases());
		
		new Restart().init(); // Starts auto restart
		
	}
	
	@Override
	public void onDisable() {
		
		PlayerData.saveQ();
		
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			
			Discord.sendMessage("<:LEAVE:905682349239463957> " + p.getName());
		}
		
		Discord.sendMessage(Discord.STOP_MESSAGE);
		
		if (action != null) {
			action.accept(null);
		}

	}
	
	public static Consumer<Random> action;
}

