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
		setDisplay(mat);
		this.function = (function != null) ? function : Butto.defaultButto;
	}
	
	public Button (Material mat, String name) {
		function = Butto.defaultButto;
		
		setDisplay(mat, name);
	}
	
	public Button(Material mat, Butto function, String name) {
		this.function = (function != null) ? function : Butto.defaultButto;
		
		setDisplay(mat, name);
	}
	
	public Button(Material mat, String name, String lore) {
		function = Butto.defaultButto;
		
		setDisplay(mat, name, lore);
	}
	
	public Button(Material mat, Butto function, String name, String lore) {
		this.function = (function != null) ? function : Butto.defaultButto;
		
		setDisplay(mat, name, lore);
	}
	
	public void setDisplay(Material mat) {
		item = (mat != null) ? new ItemStack(mat) : new ItemStack(Material.AIR);
	}
	
	public void setDisplay(Material mat, String name) {
		setDisplay(mat);
		
		if (name != null) {
			
			ItemMeta imeta = item.getItemMeta();
			imeta.setDisplayName(name);
			item.setItemMeta(imeta);
		}
	}
	
	public void setDisplay(Material mat, String name, String lore) {
		setDisplay(mat);
		
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
