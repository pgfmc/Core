package net.pgfmc.core.inventoryAPI;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class BaseInventory implements InventoryHolder {

	// fields
		
	/**
	 * The list of functional buttons in an inventory.
	 */
	protected Button[] buttons;
	
	
	//protected ArrayList<Button> items;
		
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
		
		//items = new ArrayList<>(size.getSize());
	}
	
	public void setButton(int slot, Button b) {
		if (slot < sizeD.size && slot > -1) {
			
			if (!b.setState(this, slot)) {
				b = b.clone();
				return;
			}
			buttons[slot] = b;
			b.setState(this, slot);
			inv.setItem(slot, b.getItem());
		}
	}
	
	/*
	 * Returns all buttons from the inventory.
	 */
	public Button[] getButtons() {
		return buttons;
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
	protected void press(int slot, InventoryClickEvent e) {
		
		// System.out.println("Slot " + String.valueOf(slot) + " Pressed!");
		
		if (slot + 1 > inv.getSize()) {return;};
		
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