package net.pgfmc.core.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.pgfmc.core.discord.Discord;
import net.pgfmc.core.playerdataAPI.PlayerData;

public class Roles {
	
	private static List<Role> getDefault() {
		List<Role> r = new ArrayList<>(1);
		r.add(Role.MEMBER);
		return r;
	}
	
	/**
	 * Roles Enum
	 * @author CrimsonDart
	 *
	 */
	public enum Role {
		FOUNDER("§b✦", 7),
		ADMIN("§c✦", 6),
		MODERATOR("§d✦", 5),
		TRAINEE("§5✦", 4),
		DEVELOPER("§2✦", 3),
		DONATOR("§e", 2),
		VETERAN("§9", 1),
		MEMBER("§6", 0);
		
		private String colorCode;
		private int dominance;
		private String id;
		
		
		Role(String cc, int d) {
			colorCode = cc;
			dominance = d;
		}
		
		public String getColorCode() {
			return colorCode;
		}
		
		public int getDominance() {
			return dominance;
		}
		
		public String getID() {
			return id;
		}
		
		public static Role getRole(String id) {
			
			if (id.equals("579062298526875648")) { return MEMBER;} else
			if (id.equals("779928656658432010")) { return VETERAN;} else
			if (id.equals("899932873921003540")) { return DONATOR;} else
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
		
		public static Role getDominantOf(List<String> ids) {
			return getDominant(getRoles(ids));
		}
		
		public static Role getDominant(List<Role> list) {
			
			System.out.println(list);
			
			if (list == null || list.size() == 0) {
				System.out.println("out 1");
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
	}
	
	public static void recalculateRoles(PlayerData pd) {
		
		String discordo = (String) pd.loadFromFile("Discord");
		
		if (discordo != null) {
			pd.setData("Discord", discordo);
			List<Role> list = Role.getRoles(Discord.JDA.getGuildById("579055447437475851").getMemberById(discordo).getRoles().stream().map(x -> x.getId()).collect(Collectors.toList()));
			pd.setData("Roles", list);
			
		} else {
			pd.setData("Roles", getDefault());
		}
		
		
	}
}
