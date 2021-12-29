package net.pgfmc.core.inventoryAPI;

import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * interface to add functionality to the buttons.
 * @author CrimsonDart
 *
 */
@FunctionalInterface
public interface Butto {
	
	public void press(InventoryClickEvent e, int slot);
	
	/**
	 * The default button function used in place of null.
	 */
	public final Butto defaultButto = (e, i) -> {};
	public final Butto unProtectedButto = (e, i) -> {
		e.setCancelled(false);
	};
}
