package net.pgfmc.core.roles;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;

import net.pgfmc.core.Main;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.core.roles.Roles.Role;

public class Permissions implements Listener {
	
	private static final String[] disabledPerms = {
			//"multiverse.core.version",
			//"multiverse.help",
			"bukkit.command.plugins",
			"minecraft.command.teammsg"
	};
	
	private static final String[] memberPerms = {
			"pgf.cmd.back",
			"pgf.cmd.home.*",
			"pgf.cmd.tp.tpa",
			"pgf.cmd.tp.tpaccept",
			"pgf.cmd.tp.tpdeny",
			"pgf.cmd.afk",
			//"pgf.cmd.goto",
			"pgf.cmd.block",
			"pgf.cmd.help",
			"pgf.cmd.link",
			"pgf.cmd.unlink",
			"teams.friend.*",
			"bukkit.command.seed",
			"bukkit.command.tps", 
			"bukkit.command.list",
			"pgf.dim.survival",
			"ultimatechairs.use"
	};
	
	private static final String[] veteranPerms = {
			"pgf.cmd.echest",
			"ultimatechairs.sit"
	};
	
	private static final String[] modPerms = {
			"pgf.cmd.gamemode.creative", 
			"pgf.cmd.gamemode.survival", 
			"pgf.cmd.gamemode.adventure", 
			"pgf.cmd.gamemode.spectator", 
			"bukkit.command.plugins", 
			"bukkit.command.give", 
			"bukkit.command.kick", 
			"bukkit.command.say", 
			"bukkit.command.teleport",
			"bukkit.command.ban.list"
	};
	
	private static final String[] devPerms = {
			"pgf.admin.toggledim",
			"pgf.admin.backup",
			"pgf.admin.restore"
	};
	
	private static final String[] adminPerms = {
			"pgf.admin.vanish", 
			"pgf.cmd.fly",
			"pgf.admin.god",
			"pgf.admin.sudo",
			"pgf.cmd.heal",
			"pgf.admin.day",
			"bukkit.command.restart",
			"bukkit.commands.timings",
			"bukkit.command.reload",
			"bukkit.command.ban.ip",
			"bukkit.command.ban.player",
			"bukkit.command.clear",
			"bukkit.command.defaultgamemode",
			"bukkit.command.difficulty",
			"bukkit.command.effect",
			"bukkit.command.enchant",
			"bukkit.command.gamemode",
			"bukkit.command.gamerule",
			"bukkit.command.kill",
			"bukkit.command.unban.player",
			"bukkit.command.unban.ip",
			"bukkit.command.save.perform",
			"bukkit.command.save.off",
			"bukkit.command.save.on",
			"bukkit.command.scoreboard",
			"bukkit.command.spawnpoint",
			"bukkit.command.stop",
			"bukkit.command.time.set",
			"bukkit.command.time.add",
			"bukkit.command.toggledownfall",
			"bukkit.command.weather",
			"bukkit.command.whitelist.add",
			"bukkit.command.whitelist.remove",
			"bukkit.command.whitelist.list",
			"bukkit.command.whitelist.enable",
			"bukkit.command.whitelist.disable",
			"bukkit.command.whitelist.reload",
			"bukkit.command.xp"
	};
	
	public static void recalcPerms(PlayerData pd) {
		
		List<Role> r = pd.getData("Roles");
		
		Player p = pd.getPlayer();
			
		PermissionAttachment permatch = p.addAttachment(Main.plugin);
		
		if (r != null) {
			
			for (String s : memberPerms) {
				permatch.setPermission(s, true);
			}
			
			for (String s : disabledPerms) {
				permatch.setPermission(s, false);
			}
			
			for (String s : veteranPerms) {
				permatch.setPermission(s, r.contains(Role.VETERAN) || r.contains(Role.DONATOR));
			}
			
			for (String s : modPerms) {
				permatch.setPermission(s, r.contains(Role.MODERATOR));
			}
			
			for (String s : devPerms) {
				permatch.setPermission(s, r.contains(Role.DEVELOPER));
			}
			
			for (String s : adminPerms) {
				permatch.setPermission(s, r.contains(Role.ADMIN));
			}
		} else {
			for (String s : memberPerms) {
				permatch.setPermission(s, true);
			}
			
			for (String s : disabledPerms) {
				permatch.setPermission(s, false);
			}
			
			for (String s : veteranPerms) {
				permatch.setPermission(s, false);
			}
			
			for (String s : modPerms) {
				permatch.setPermission(s, false);
			}
			
			for (String s : devPerms) {
				permatch.setPermission(s, false);
			}
			
			for (String s : adminPerms) {
				permatch.setPermission(s, false);
			}
		}
		p.recalculatePermissions();
		p.updateCommands();
	}
	
	public static void recalcPerms(Player p) {
		if (p != null) {
			recalcPerms(PlayerData.getPlayerData(p));
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		recalcPerms(e.getPlayer());
	}
}
