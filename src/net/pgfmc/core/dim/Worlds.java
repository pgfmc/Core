package net.pgfmc.core.dim;

import org.bukkit.World;
/**
 * Enum for the Survival Worlds, containing their respective world data.
 * @author CrimsonDart
 * 
 *
 */
public enum Worlds {
	SURVIVAL,
	SURVIVAL_NETHER,
	SURVIVAL_END;
	
	public World world;
	
	/**
	 * sets the Enum's world.
	 */
	public void set(World world) {
		this.world = world;
	}			
	 /**
	  * Tests if the input world matches with the Enum's world
	  * @param world
	  * @return true if the Enum's world matches, false if not
	  */
	public boolean equals(World world) {
		return (this.world == world);
	}
}
