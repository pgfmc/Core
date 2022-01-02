package net.pgfmc.core.permissions;

public class Roles {
	/*
	 * Enums for roles
	 * Enums have fields for parents, permissions, name, rank/dominance, 
	 */
	
	public enum Role {
		FOUNDER,
		ADMIN,
		DEVELOPER,
		MODERATOR,
		TRAINEE,
		STAFF,
		DONATOR,
		VETERAN,
		DOOKIE,
		MEMBER;
		
		private Role()
		{
			
		}
	}
	
	/*
	 * EnumMap for parents
	 * 
	 * Also
	public static EnumMap<PizzaStatus, List<Pizza>> 
  groupPizzaByStatus(List<Pizza> pzList) {
    EnumMap<PizzaStatus, List<Pizza>> map = pzList.stream().collect(
      Collectors.groupingBy(Pizza::getStatus,
      () -> new EnumMap<>(PizzaStatus.class), Collectors.toList()));
    return map;
}
	 */
	
}
// Add player
// remove player