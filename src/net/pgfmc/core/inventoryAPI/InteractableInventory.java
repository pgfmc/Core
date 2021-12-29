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
 * Abstract class that supports all inventory functions, as well as some extra convenience, such as  
 * @author CrimsonDart
 *
 */
public abstract class InteractableInventory implements InventoryHolder {
	
	// fields
	/**
	 * The list of functional buttons in an inventory.
	 */
	protected ArrayList<Button> buttons;
	
	/**
	 * The Size of the Inventory. (BIG (56 slots) or SMALL (27 slots))
	 */
	SizeData sizeD;
	
	/**
	 * The inventory itself.
	 */
	protected Inventory inv;
	
	/**
	 * Enum to store data for chest sizes.
	 * holds data for chest size, and the amount of entries available for each page.
	 * @author CrimsonDart
	 *
	 */
	public enum SizeData {
		BIG(56, 36.0f),
		SMALL(27, 21.0f),
		HOPPER(5, 0.0f),
		DROPPER(9, .0f);
		
		public int size;
		public float pageSize;
		
		SizeData(int size, float pageSize) {
			this.size = size;
			this.pageSize = pageSize;
		}
		
		int getSize() {
			return size;
		}
		
		float getPageSize() {
			return pageSize;
		}
	}
	
	/**
	 * interface to add functionality to the buttons.
	 * @author CrimsonDart
	 *
	 */
	@FunctionalInterface
	public interface Butto {
		
		public void press(Player p, InventoryClickEvent e);
		
		/**
		 * The default button function used in place of null.
		 */
		public final Butto defaultButto = (x, e) -> {};
		public final Butto unProtectedButto = (x, e) -> {
			e.setCancelled(false);
		};
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
		
		// constructor
		
		/**
		 * 
		 * Creates a new Button for an InteractableInventory. includes support for custom names, lore, and Full button clicking action!
		 * 
		 * @param mat The material of the item.
		 * @param name The name of the item. (set to null to not change the name)
		 * @param lore The lore of the item.  Syntax:    "line1\nline2\nline3..... and so on"
		 * @param function The lambda function that is ran when the item is clicked. (bk, this is a lambda tutorial for you)
		 * 		where the "Butto function" parameter goes, type:      (x) -> { /* code here /* }      and thats it    also x is the player who clicked the item
		 * 		You can also set function to null to just do nothing.
		 */
		public Button(Material mat, String name, String lore, Butto function) {
			
			if (function == null) {
				this.function = Butto.defaultButto;
			} else {
				this.function = function;
			}
			
			this.item = new ItemStack(mat);
			//position = pos;
			
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
	public InteractableInventory(SizeData size, String name) {
		this.inv = Bukkit.createInventory(this, size.getSize(), name);
		// System.out.println("new Interactable Inventory Created!");
		// System.out.println(String.valueOf(size));
		buttons = new ArrayList<>(size.getSize());
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
		Button button = new Button(mat, name, lore, function);
		
		inv.setItem(pos, button.getItem());
		this.buttons.set(pos, button);
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
		Button button = new Button(mat, name, lore, null);
		
		inv.setItem(pos, button.getItem());
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
		Button button = new Button(mat, name, null, null);
		
		inv.setItem(pos, button.getItem());
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
		Button button = new Button(mat, null, null, null);

		inv.setItem(pos, button.getItem());
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
		Button button = new Button(mat, null, null, function);
		
		inv.setItem(pos, button.getItem());
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
		Button button = new Button(mat, name, null, function);
		
		inv.setItem(pos, button.getItem());
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
		
		if (slot + 1 > inv.getSize()) {return;};
		
		Button b = 	buttons.get(slot);
		if (b!= null) b.run(p, e);
		return;
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
		buttons.get(index);
		return null;
	}
	
	public void removeButton(int index) {
		buttons.set(index, null);
	}
}