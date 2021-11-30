package net.pgfmc.core.inventoryAPI;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryPressEvent implements Listener {
	
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof InteractableInventory && e.getWhoClicked() instanceof Player) {
			((InteractableInventory) e.getClickedInventory().getHolder()).press(e.getSlot(), (Player) e.getWhoClicked(), e);
			e.setCancelled(true);
		}
	}
	
	public void onslideEvent(InventoryDragEvent e) {
		if (e.getInventory().getHolder() instanceof InteractableInventory) {
			e.setCancelled(true);
			return;
		}
	}
}