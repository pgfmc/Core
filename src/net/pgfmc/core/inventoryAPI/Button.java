package net.pgfmc.core.inventoryAPI;

import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The Class that defines a Button object.
 * Buttons can be set in an inventory, and when the button is pressed, the action, <Butto>, is ran.
 * @author CrimsonDart
 *
 */
public class Button implements Cloneable {
	
	// fields
	
	protected ItemStack item;
	private Butto function;
	transient private BaseInventory parent;
	transient private int slot = -1;
	
	
	// constructors
	
	public Button(ItemStack item) {
		this.item = (item != null) ? item : new ItemStack(Material.AIR);
		function = Butto.defaultButto;
	}
	
	public Button(ItemStack item, Butto function) {
		this.item = (item != null) ? item : new ItemStack(Material.AIR);
		this.function = (function != null) ? function : Butto.defaultButto;
	}
	
	public Button(Material mat) {
		item = (mat != null) ? new ItemStack(mat) : new ItemStack(Material.AIR);
		function = Butto.defaultButto;
	}
	
	public Button (Material mat, Butto function) {
		item = (mat != null) ? new ItemStack(mat) : new ItemStack(Material.AIR);
		this.function = (function != null) ? function : Butto.defaultButto;
	}
	
	public Button (Material mat, String name) {
		item = (mat != null) ? new ItemStack(mat) : new ItemStack(Material.AIR);
		function = Butto.defaultButto;
		
		if (name != null) {
			
			ItemMeta imeta = item.getItemMeta();
			imeta.setDisplayName(name);
			item.setItemMeta(imeta);
		}
	}
	
	public Button(Material mat, Butto function, String name) {
		item = (mat != null) ? new ItemStack(mat) : new ItemStack(Material.AIR);
		this.function = (function != null) ? function : Butto.defaultButto;
		
		if (name != null) {
			
			ItemMeta imeta = item.getItemMeta();
			imeta.setDisplayName(name);
			item.setItemMeta(imeta);
		}
	}
	
	public Button(Material mat, String name, String lore) {
		item = (mat != null) ? new ItemStack(mat) : new ItemStack(Material.AIR);
		function = Butto.defaultButto;
		
		if (name != null) {
			
			ItemMeta imeta = item.getItemMeta();
			imeta.setDisplayName(name);
			
			if (lore != null) {
				LinkedList<String> lines = new LinkedList<String>();
				
				for (String line : lore.split("\n")) {
					lines.add(line);
				}
				imeta.setLore(lines);
			}
			item.setItemMeta(imeta);
			
		} else if (lore != null) {
			ItemMeta imeta = item.getItemMeta();
			LinkedList<String> lines = new LinkedList<String>();
			
			for (String line : lore.split("\n")) {
				lines.add(line);
			}
			imeta.setLore(lines);
			item.setItemMeta(imeta);
		}
	}
	
	public Button(Material mat, Butto function, String name, String lore) {
		item = (mat != null) ? new ItemStack(mat) : new ItemStack(Material.AIR);
		this.function = (function != null) ? function : Butto.defaultButto;
		
		if (name != null) {
			
			ItemMeta imeta = item.getItemMeta();
			imeta.setDisplayName(name);
			
			if (lore != null) {
				LinkedList<String> lines = new LinkedList<String>();
				
				for (String line : lore.split("\n")) {
					lines.add(line);
				}
				imeta.setLore(lines);
			}
			item.setItemMeta(imeta);
			
		} else if (lore != null) {
			ItemMeta imeta = item.getItemMeta();
			LinkedList<String> lines = new LinkedList<String>();
			
			for (String line : lore.split("\n")) {
				lines.add(line);
			}
			imeta.setLore(lines);
			item.setItemMeta(imeta);
		}
	}
	
	// methods
	
	/**
	 * runs the lambda expression Butto
	 * @param player the object of the operation.
	 */
	protected void run(InventoryClickEvent e, int slot) {
		function.press(e, slot);
	}
	
	/**
	 * Returns the Button's ItemStack.
	 * @return the Button's respective ItemStack.
	 */
	public ItemStack getItem() {
		return item;
	}
	
	/**
	 * Attempts to set the parent data to this button. Only one button can be bound to a BaseInventory at once.
	 * @param p The Parent BaseInventory.
	 * @param s The slot this button belongs in.
	 * @return {@code true} if not set, {@code false} if already set.
	 */
	public boolean setState(BaseInventory p, int s) {
		
		if (parent == null && slot == -1) {
			parent = p;
			slot = s;
			return true;
		}
		return false;
	}
	
	/**
	 * Convenience Method. 
	 * Returns the name of the ItemStack.
	 * @return
	 */
	public String getName() {
		return item.getItemMeta().getDisplayName();
	}
	
	@Override
	public Button clone() {
		return new Button(item, function);
	}
}
