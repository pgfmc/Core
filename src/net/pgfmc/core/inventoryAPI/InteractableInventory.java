package net.pgfmc.core.inventoryAPI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * abstract class that supports all inventory functions, as well as some extra convenience, such as  
 * @author CrimsonDart
 *
 */
public abstract class InteractableInventory implements InventoryHolder {
	
	// fields
	/**
	 * The list of functional buttons in an inventory.
	 */
	protected ArrayList<Button> buttons;
	
	/*
	 * The amount of slots in the inventory.
	 */
	protected int size;
	
	/**
	 * The inventory itself.
	 */
	protected Inventory inv;
	
	/**
	 * interface to add functionality to the buttons.
	 * @author CrimsonDart
	 *
	 */
	@FunctionalInterface
	protected interface Butto {
		
		public void press(Player p, InventoryClickEvent e);
		
		/**
		 * The default button function used in place of null.
		 */
		public Butto defaultButto = (x, e) -> {};
		
		
	}
	
	/**
	 * The Class that defines a Button object.
	 * Buttons can be set in an inventory, and when the button is pressed, the action, <Butto>, is ran.
	 * @author CrimsonDart
	 *
	 */
	public static class Button {
		
		// fields
		
		private Butto function;
		private ItemStack item;
		private int position;
		
		// constructor
		
		/**
		 * 
		 * Creates a new Button for an InteractableInventory. includes support for custom names, lore, and Full button clicking action!
		 * 
		 * @param mat The material of the item.
		 * @param pos The Item's slot number in the InteractableInventory
		 * @param name The name of the item. (set to null to not change the name)
		 * @param lore The lore of the item.  Syntax:    "line1\nline2\nline3..... and so on"
		 * @param function The lambda function that is ran when the item is clicked. (bk, this is a lambda tutorial for you)
		 * 		where the "Butto function" parameter goes, type:      (x) -> { /* code here /* }      and thats it    also x is the player who clicked the item
		 * 		You can also set function to null to just do nothing.
		 */
		protected Button(Material mat, int pos, String name, String lore, Butto function) {
			
			if (function == null) {
				this.function = Butto.defaultButto;
			} else {
				this.function = function;
			}
			
			this.item = new ItemStack(mat);
			position = pos;
			
			ItemMeta imeta = this.item.getItemMeta();
			
			if (name != null) {
				imeta.setDisplayName(name);
			}
			
			// sets lore
			if (lore != null) {
				LinkedList<String> lines = new LinkedList<String>();
				
				for (String line : lore.split("\n")) {
					lines.add(line);
				}
				imeta.setLore(lines);
			}
			item.setItemMeta(imeta);
		}
		
		protected Button(int pos) {
			this.function = Butto.defaultButto;
			this.item = new ItemStack(Material.AIR);
			position = pos;
		}
		
		// methods
		
		/**
		 * runs the lambda expression Butto
		 * @param player the object of the operation.
		 */
		protected void run(Player player, InventoryClickEvent e) {
			function.press(player, e);
		}
		
		/**
		 * Returns the Button's ItemStack.
		 * @return the Button's respective ItemStack.
		 */
		public ItemStack getItem() {
			return item;
		}
		
		/**
		 * Returns the slot the Button is set to.
		 * @return the slot the Button is set to.
		 */
		public int getSlot() {
			return position;
		}
		
		/**
		 * Sets the Button's slot to the input's.
		 */
		public void setSlot(int slot) {
			position = slot;
		}
		
		/**
		 * Convenience Method. 
		 * Returns the name of the ItemStack.
		 * @return
		 */
		public String getName() {
			return item.getItemMeta().getDisplayName();
		}
	}

	
	/**
	 * Constructor for Interactable Inventory.
	 * automatically creates an inventory of the size requested.
	 * @param size The size of the inventory needed.
	 */
	public InteractableInventory(int size, String name) {
		this.inv = Bukkit.createInventory(this, size, name);
		this.size = inv.getSize();
		// System.out.println("new Interactable Inventory Created!");
		// System.out.println(String.valueOf(size));
		buttons = new ArrayList<>(size);
	}

	// methods
	
	// set button methods.
	
	/**
	 * 
	 * Creates a new Button for an InteractableInventory. includes support for custom names, lore, and Full button clicking action!
	 * This function automatically creates and sets the button to the proper position.
	 * 
	 * @param mat The material of the item.
	 * @param pos The Item's slot number in the InteractableInventory
	 * @param function The lambda function that is ran when the item is clicked. (bk, this is a lambda tutorial for you)
	 * 		where the "Butto function" parameter goes, type:      (x) -> { /* code here /* }      and thats it    also x is the player who clicked the item
	 * 		You can also set function to null to just do nothing.
	 * @param name The name of the item. (set to null to not change the name)
	 * @param lore The lore of the item.  Syntax:    "line1\nline2\nline3..... and so on"
	 * @return 
	 */
	public Button createButton(Material mat, int pos, String name, String lore, Butto function) {
		Button button = new Button(mat, pos, name, lore, function);
		
		inv.setItem(button.getSlot(), button.getItem());
		this.buttons.add(button);
		return button;
	}
	
	/**
	 * 
	 * Creates a new Button for an InteractableInventory. includes support for custom names, lore, and Full button clicking action!
	 * This function automatically creates and sets the button to the proper position.
	 * 
	 * @param mat The material of the item.
	 * @param pos The Item's slot number in the InteractableInventory
	 * @param name The name of the item. (set to null to not change the name)
	 * @param lore The lore of the item.  Syntax:    "line1\nline2\nline3..... and so on"
	 */
	public Button createButton(Material mat, int pos, String name, String lore) {
		Button button = new Button(mat, pos, name, lore, null);
		
		inv.setItem(button.getSlot(), button.getItem());
		this.buttons.add(button);
		return button;
	}
	
	/**
	 * 
	 * Creates a new Button for an InteractableInventory. includes support for custom names, lore, and Full button clicking action!
	 * This function automatically creates and sets the button to the proper position.
	 * 
	 * @param mat The material of the item.
	 * @param pos The Item's slot number in the InteractableInventory
	 * @param name The name of the item. (set to null to not change the name)
	 * @return 
	 */
	public Button createButton(Material mat, int pos, String name) {
		Button button = new Button(mat, pos, name, null, null);
		
		inv.setItem(button.getSlot(), button.getItem());
		this.buttons.add(button);
		return button;
	}
	
	/**
	 * 
	 * Creates a new Button for an InteractableInventory. includes support for custom names, lore, and Full button clicking action!
	 * This function automatically creates and sets the button to the proper position.
	 * 
	 * @param mat The material of the item.
	 * @param pos The Item's slot number in the InteractableInventory
	 * @return 
	 */
	public Button createButton(Material mat, int pos) {
		Button button = new Button(mat, pos, null, null, null);

		inv.setItem(button.getSlot(), button.getItem());
		this.buttons.add(button);
		return button;
	}
	
	/**
	 * 
	 * Creates a new Button for an InteractableInventory. includes support for custom names, lore, and Full button clicking action!
	 * This function automatically creates and sets the button to the proper position.
	 * 
	 * @param mat The material of the item.
	 * @param pos The Item's slot number in the InteractableInventory
	 * @param function The lambda function that is ran when the item is clicked. (bk, this is a lambda tutorial for you)
	 * 		where the "Butto function" parameter goes, type:      (x) -> { /* code here /* }      and thats it    also x is the player who clicked the item
	 * 		You can also set function to null to just do nothing.
	 * @return 
	 */
	public Button createButton(Material mat, int pos, Butto function) {
		Button button = new Button(mat, pos, null, null, function);
		
		inv.setItem(button.getSlot(), button.getItem());
		this.buttons.add(button);
		return button;
	}
	
	/**
	 * 
	 * Creates a new Button for an InteractableInventory. includes support for custom names, lore, and Full button clicking action!
	 * This function automatically creates and sets the button to the proper position.
	 * 
	 * @param mat The material of the item.
	 * @param pos The Item's slot number in the InteractableInventory
	 * @param lore The lore of the item.  Syntax:    "line1\nline2\nline3..... and so on"
	 * @param function The lambda function that is ran when the item is clicked. (bk, this is a lambda tutorial for you)
	 * 		where the "Butto function" parameter goes, type:      (x) -> { /* code here /* }      and thats it    also x is the player who clicked the item
	 * 		You can also set function to null to just do nothing.
	 * @return 
	 */
	public Button createButton(Material mat, int pos, String name, Butto function) {
		Button button = new Button(mat, pos, name, null, function);
		
		inv.setItem(button.getSlot(), button.getItem());
		this.buttons.add(button);
		return button;
	}
	
	/**
	 * Presses the Button in slot <slot>.
	 * @param slot The slot that was clicked.
	 * @param p The player that clicked the slot.
	 * @param e The InventoryClickEvent that caused the press.
	 */
	protected void press(int slot, Player p, InventoryClickEvent e) {
		
		// System.out.println("Slot " + String.valueOf(slot) + " Pressed!");
		
		if (slot + 1 > size) {return;};
		
		for (Button button : buttons) {
			if (button.getSlot() == slot) {
				button.run(p, e);
				return;
			}
		}
	}
	
	/**
	 * Returns the Inventory's Inventory Object.
	 */
	@Override
	public Inventory getInventory() {
		return inv;
	}
	
	/*
	 * Returns all buttons from the inventory.
	 */
	public List<Button> getButtons() {
		return buttons;
	}
	
	/**
	 * Get the button for the input index.
	 * @param index The slot of the inventory.
	 * @return Returns the button at the input slot, and null if there is no button at that slot.
	 */
	public Button getButton(int index) {
		for (Button button : buttons) {
			if (button.getSlot() == index) {
				return button;
			}
		}
		return null;
	}
}