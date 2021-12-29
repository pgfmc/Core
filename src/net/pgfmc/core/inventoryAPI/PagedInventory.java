package net.pgfmc.core.inventoryAPI;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class PagedInventory extends BaseInventory {
	
	/**
	 * The List of entries, in the form of Button(s).
	 */
	protected List<Button> entries;
	
	/*
	 * The Button that is in the top left corner in SMALL inventories, or in the center in BIG ones. Usually used as a back button.
	 */
	Button persistentButton;
	
	/**
	 * holds the current page.
	 */
	transient int page = 1;
	
	/**
	 * Holds the amount of total pages the inventory has.
	 */
	int pages;
	
	/**
	 * Constructor for PagedInventory. The inventory can be in two sizes: 27 or 56 (single or double chest). 
	 * The method {@code setEntries()} is the method used to load entries.
	 * @param size The size of the inventory. can only be 27 or 56.
	 * @param name Name displayed at the top of the inventory's interface.
	 * @param itemController The function that is ran per entry in "entries"; itemController must return a button Object, with the entry itself as the input.
	 */
	public PagedInventory(SizeData size, String name, List<Button> entries) {
		super(size, name);
		
		this.entries = entries;
		this.pages = (int) Math.ceil(entries.size() / size.getPageSize());
		
		setPage(page);
	}
	
	/**
	 * Input the entry slot number, and the function will return the appropriate inventory slot number (only for SMALL inventories)
	 * @param index The index of the item in the given page.
	 * @return Returns the Respective slot for the given "index" in a 27 slot inventory.
	 */
	protected int entryToSlot(int index) {
		
		if (index >= 0 && index < 7) {
			return index + 2;
		} else if (index >= 7 && index < 14) {
			return index + 4;
		} else if (index >= 14 && index < 21) {
			return index + 6;
		} else {
			new Exception("input \"index\" is out of bounds!");
		}
		return -1;
	}
	
	protected void flipPage(int flips) {
		if (flips + page > 0 && flips + page <= pages) {
			page = page + flips;
			setPage(page);
		}
	}
	
	/**
	 * Manages all page turning. 
	 * @param page The page that the inventory will be set to.
	 */
	protected void setPage(int newPage) {
		
		buttons = new Button[sizeD.getSize()];
		inv.clear();
		
		if (newPage > pages || newPage < 1) {
			return;
		}
		
		this.page = newPage;
		
		// Places the next and previous page buttons in their respective spots in the inventory.
		if (page > 1) {
			if (sizeD == SizeData.BIG) {
				setButton(48, new Button(Material.IRON_HOE, (x, e) -> {
					flipPage(-1);
				}, "Previous Page"));
				
			} else {
				setButton(9, new Button(Material.IRON_HOE, (x, e) -> {
					flipPage(-1);
				}, "Previous Page"));
			}
		}
		
		if (page < pages) {
			if (sizeD == SizeData.BIG) {
				setButton(50, new Button(Material.ARROW, (x, e) -> {
					flipPage(+1);
				}, "Next Page"));
			} else {
				setButton(18 , new Button(Material.ARROW, (x, e) -> {
					flipPage(+1);
				}, "Next Page"));
			}
		}
		
		// Places the entries in the correct spots. ----- WIP
		if (sizeD == SizeData.BIG) {
			for (int i = 0; i < 36; i++) {
				if (i >= entries.size()) { // if "i" gets bigger than the entries size.
					
					buttons[i] = null;
					inv.setItem(i, new ItemStack(Material.AIR));
					
				} else {
					
					Button button = entries.get(i + (page - 1) * 36);
					
					buttons[i] = button;
					inv.setItem(i, button.getItem());
				}
			}
		} else if (sizeD == SizeData.SMALL) {
			for (int i = 0; i < 21; i++) {
				if (i + (page - 1) * 21 >= entries.size()) { // if "i" gets bigger than the entries size.
					
					
					inv.setItem(entryToSlot(i), new ItemStack(Material.AIR));
				} else {
					Button button = entries.get(i + (page - 1) * 21);
					
					buttons[i] = button;
					inv.setItem(entryToSlot(i), button.getItem());
				}
			}
		}
		
		// re-sets the button in the top left
		if (persistentButton != null) {
			
			buttons[0] = persistentButton;
			inv.setItem(0, persistentButton.getItem());
		}
	}
	
	/**
	 * Set the button in the top left corner. This is the only button with a special function, so use it wisely.
	 * @param mat The material of the item used.
	 * @param name The name given to the item.
	 * @param lore The lore to be added to the item.
	 * @param function The code that is to be ran when the button is pressed, in the form of a lambda function.
	 */
	protected void setPersistentButton(Button b) {
		persistentButton = b;
	}
	
	/**
	 * Sets the persistent button to 
	 * @param ae
	 */
	public void setBackButton(BaseInventory ae) {
		persistentButton = new Button(Material.FEATHER, (e, i) -> {
			e.getWhoClicked().openInventory(ae.getInventory());
		}, "back");
		buttons[0] = persistentButton;
		inv.setItem(0, persistentButton.getItem());
	}
	
	public List<Button> getEntries() {
		return entries;
	}
}