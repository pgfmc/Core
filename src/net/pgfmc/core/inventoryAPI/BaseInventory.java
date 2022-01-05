package net.pgfmc.core.inventoryAPI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.core.inventoryAPI.extra.Button;
import net.pgfmc.core.inventoryAPI.extra.SizeData;

/**
 * The basic Inventory.
 * @author CrimsonDart
 * @version 4.0.2
 * @since 2.0.0
 *
 */
public abstract class BaseInventory implements InventoryHolder {

	// fields
		
	/**
	 * The list of functional buttons in an inventory.
	 */
	protected Button[] buttons;
		
	/**
	 * The Size of the Inventory. (BIG (56 slots) or SMALL (27 slots))
	 */
	SizeData sizeD;
		
	/**
	 * The inventory itself.
	 */
	protected Inventory inv;
	
	public BaseInventory(SizeData size, String name) {
		sizeD = size;
		this.inv = Bukkit.createInventory(this, size.getSize(), name);
		
		buttons = new Button[size.getSize()];
	}
	
	/**
	 * Sets a slot in the inventory to a button.
	 * @param slot
	 * @param b
	 */
	void setButton(int slot, Button b) {
		if (b == null) b = new Button(Material.AIR);
		
		if (slot < sizeD.getSize() && slot > -1) {
			buttons[slot] = b;
			inv.setItem(slot, b.getItem());
		}
	}
	
	/*
	 * Returns all buttons from the inventory.
	 * Changes to the returned array will NOT reflect changes in the inventory.
	 */
	public final Button[] getButtons() {
		return buttons.clone();
	}
	
	/**
	 * Get the button for the input index.
	 * @param index The slot of the inventory.
	 * @return Returns the button at the input slot, and null if there is no button at that slot.
	 */
	public Button getButton(int index) {
		return buttons[index];
	}
	
	public void remove(int index) {
		setButton(index, null);
	}
	
	/**
	 * Presses the Button in slot <slot>.
	 * @param slot The slot that was clicked.
	 * @param p The player that clicked the slot.
	 * @param e The InventoryClickEvent that caused the press.
	 */
	public final void press(int slot, InventoryClickEvent e) {
		
		if (slot + 1 > inv.getSize()) return;
		
		Button b = buttons[slot];
		if (b!= null) b.run(e, slot);
		return;
	}
	
	/**
	 * Returns the Inventory's Inventory Object.
	 */
	@Override
	public Inventory getInventory() {
		return inv;
	}
}