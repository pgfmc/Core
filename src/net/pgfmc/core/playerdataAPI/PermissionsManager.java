package net.pgfmc.core.playerdataAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

import net.pgfmc.core.configify.Configify;
import net.pgfmc.core.misc.CoreMain;
import net.pgfmc.core.misc.Mixins;

public class PermissionsManager extends Configify implements Listener {
	
	private static FileConfiguration yml;
	
	/**
	 * Recalculate the permissions for a player
	 * 
	 * @param pd PlayerData of player
	 */
	public static void recalcPerms(PlayerData pd)
	{
		Player player = pd.getPlayer();
		if (player == null)
		{
			System.out.println("Updating perms failed, player was offline");
			return;
		}
		
		
		
		Set<Permission> perms = getPermissions(pd);
		PermissionAttachment patt = player.addAttachment(CoreMain.plugin);
		
		for (Permission p : perms)
		{
			System.out.println("PERM ON: " + p.getName());
			patt.setPermission(p, true);
		}
		
	    Set<Permission> antiPerms = Bukkit.getPluginManager().getPermissions();
	    
	    for (Permission p : antiPerms)
	    {
	    	if (perms.contains(p)) { continue; }
	    	System.out.println("PERM OFF: " + p.getName());
	        patt.setPermission(p, false);
	    }
		
		player.recalculatePermissions();
		player.updateCommands();
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (e.getPlayer() != null) {
			recalcPerms(PlayerData.getPlayerData(e.getPlayer()));
		}
	}
	
	/*
	public static Role getRole(String id) {
		
		if (id.equals("579062298526875648")) { return MEMBER;} else
		if (id.equals("779928656658432010")) { return VETERAN;} else
		if (id.equals("899932873921003540") || id.equals("645442029756874753")) { return DONATOR;} else
		if (id.equals("814184657674305546")) { return DEVELOPER;} else
		if (id.equals("802671617804730379")) { return TRAINEE;} else
		if (id.equals("595560680023654401")) { return MODERATOR;} else
		if (id.equals("594015606626320417")) { return ADMIN;} else
		if (id.equals("579061127921664000")) { return FOUNDER;}
		
		return null;
	}
	
	
	public static List<Role> getRoles(List<String> ids) {
		
		return ids.stream()
				.map(x -> getRole(x))
				.filter(x -> (x != null))
				.collect(Collectors.toList());
		
	}
	
	*/
	
	/**
	 * Get all permissions a player has
	 * 
	 * @param pd PlayerData of a Player
	 * @return Set of Permission
	 */
	public static Set<Permission> getPermissions(PlayerData pd)
	{
		FileConfiguration db = pd.loadFile();
		
		List<String> roles = db.getStringList("Roles");
		Set<Permission> perms = new HashSet<Permission>();
		
		if (roles.isEmpty())
		{
			pd.setData("Roles", getDefaultRoles());
			roles.addAll(getDefaultRoles());
		}
		
		for (String r : roles)
		{
			perms.addAll(getAllRolePermissions(r));
		}
		
		return perms;
	}
	
	/**
	 * Get all permission of a role, DOES NOT include parent permissions
	 * 
	 * @param role
	 * @return Set of Permission
	 */
	public static Set<Permission> getRolePermissions(String role)
	{
		Set<Permission> perms = new HashSet<Permission>();
		
		if (!yml.contains("roles." + role + ".permisisons"))
		{
			new Exception("Role " + role + " in PlayerData file is not found in Permissions.yml").printStackTrace();
		}
		
		for (String p : yml.getStringList("roles." + role + ".permisisons"))
		{
			perms.add(Bukkit.getPluginManager().getPermission(p));
		}
		
		return perms;
	}
	
	/**
	 * Get all permission of a role, DOES include parent permissions
	 * 
	 * @param role
	 * @return Set of Permission
	 */
	public static Set<Permission> getAllRolePermissions(String role)
	{
		Set<Permission> perms = new HashSet<Permission>();
		perms.addAll(getRolePermissions(role));
		
		// No parents (orphan :())
		if (!yml.contains("roles." + role + ".options.parents"))
		{
			return perms;
		}
		
		for (String parent : yml.getStringList("roles." + role + ".options.parents"))
		{
			// Recursive, will include all inherited permissions
			perms.addAll(getAllRolePermissions(parent));
		}
		
		return perms;
	}
	
	/**
	 * Get all roles
	 * 
	 * @param ladder Can be empty for no ladder, null for all roles
	 * @return List of roles
	 */
	public static List<String> getAllRoles()
	{
		return yml.getStringList("roles");
	}
	
	public static String getRolePrefix(String role)
	{
		if (yml.contains("roles." + role + ".options.prefix"))
		{
			return yml.getString("roles." + role + ".options.prefix");
		} else
		{
			return "";
		}
	}
	
	/**
	 * Gets the default roles
	 * 
	 * @return List of default roles
	 */
	public static List<String> getDefaultRoles()
	{
		List<String> roles = getAllRoles();
		List<String> defaults = new ArrayList<String>();
		
		for (String r : roles)
		{
			if (yml.contains("roles." + r + ".optons.default") && yml.getBoolean("roles." + r + ".options.default"))
			{
				defaults.add(r);
			}
		}
		
		return defaults;
	}
	
	/*
	 * getParents
	 * getAllParents
	 * 
	 * 
	 */
	
	
	@Override
	public void reload() {
		
		yml = Mixins.getDatabase(CoreMain.plugin.getDataFolder() + File.separator + "permissions.yml");
	}

	public static void loadDefaultYml() {
		yml = Mixins.getDatabase(CoreMain.plugin.getDataFolder() + File.separator + "permissions.yml");
		yml.addDefault("roles.default.options.default", true);
		yml.addDefault("roles.default.options.parents", Arrays.asList());
		yml.addDefault("roles.default.options.ladder.name", "");
		yml.addDefault("roles.default.options.ladder.rank", "");
		yml.addDefault("roles.default.options.discordRoleId", "");
		yml.addDefault("roles.default.options.prefix", "");
		yml.addDefault("roles.default.options.suffix", "");
		yml.addDefault("roles.default.permissions", Arrays.asList("test.test", "test.test2", "test.test3"));
		
	}
	
	/*
	public static Role getDominantOf(List<String> ids) {
		return getDominant(getRoles(ids));
	}
	
	public static Role getDominant(List<Role> list) {
		
		// System.out.println(list);
		
		if (list == null || list.size() == 0) {
			// System.out.println("out 1");
			return MEMBER;
		}
		
		return list.stream()
				.reduce((h, r) -> {
			if (h == null) {
				return r;
			} 
			return (r.dominance > h.dominance) ? r : h;
		}).get();
	}
	
	*/
}
