package net.pgfmc.core.inventoryAPI;

import java.util.List;

import org.bukkit.Material;

public abstract class PagedInventory extends BaseInventory {
	
	/**
	 * The List of entries, in the form of Button(s).
	 */
	protected Button[][] pages;
	
	/*
	 * The Button that is in the top left corner in SMALL inventories, or in the center in BIG ones. Usually used as a back button.
	 */
	Button persistentButton;
	
	/**
	 * holds the current page.
	 */
	transient int page = 1;
	
	/**
	 * Constructor for PagedInventory. The inventory can be in two sizes: 27 or 56 (single or double chest). 
	 * The method {@code setEntries()} is the method used to load entries.
	 * @param size The size of the inventory. can only be 27 or 56.
	 * @param name Name displayed at the top of the inventory's interface.
	 * @param itemController The function that is ran per entry in "entries"; itemController must return a button Object, with the entry itself as the input.
	 */
	public PagedInventory(SizeData size, String name, List<Button> entries) {
		super(size, name);
		
		if (size == SizeData.DROPPER || size == SizeData.HOPPER) {
			throw new IllegalArgumentException();
		}
		
		pages = new Button[(int) Math.ceil(entries.size() / (float) size.getPageSize())][size.pageSize];
		
		for (int i = 0;
				i < entries.size();
				i++	) {
			pages[i / size.pageSize][i % size.pageSize] = entries.get(i);
		}
		
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
			return -1;
		}
		
	}
	
	protected void flipPage(int flips) {
		if (flips + page > 0 && flips + page <= pages.length) {
			page = page + flips;
			setPage(page);
		}
	}
	
	/**
	 * Manages all page turning. 
	 * @param page The page that the inventory will be set to.
	 */
	protected void setPage(int newPage) {
		
		if (newPage > pages.length || newPage < 1) return;
		
		//buttons = new Button[sizeD.getSize()];
		//inv.clear();
		
		page = newPage;
		
		if (sizeD == SizeData.BIG) {
			
			// sets the Previous page button, if apropriate.
			if (page > 1) {
				setButton(48, new Button(Material.IRON_HOE, (x, e) -> {
					flipPage(-1);
				}, "Previous Page"));
			} else {
				setButton(48, null);
			}
			
			// sets the next page button, if apropriate.
			if (page < pages.length) {
				setButton(50, new Button(Material.ARROW, (x, e) -> {
					flipPage(+1);
				}, "Next Page"));
			} else {
				setButton(50, null);
			}
			
			Button[] currentPage = pages[page -1];
			for (int i = 0;
					i < 36;
					i++) {
				
				setButton(i, currentPage[i]);
			}
			
		} else if (sizeD == SizeData.SMALL) {
			
			// sets the Previous page button, if apropriate.
			if (page > 1) {
				setButton(9, new Button(Material.IRON_HOE, (x, e) -> {
					flipPage(-1);
				}, "Previous Page"));
			} else {
				setButton(9, null);
			}
			
			// sets the next page button, if apropriate.
			if (page < pages.length) {
				setButton(18, new Button(Material.ARROW, (x, e) -> {
					flipPage(+1);
				}, "Next Page"));
			} else {
				setButton(18, null);
			}
			
			Button[] currentPage = pages[page -1];
			for (int i = 0;
					i < 21;
					i++) {
				
				setButton(entryToSlot(i), currentPage[i]);
			}
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
		setButton(0, persistentButton);
	}
	
	/**
	 * Sets the persistent button to 
	 * @param ae
	 */
	public void setBackButton(BaseInventory ae) {
		
		setPersistentButton(new Button(Material.FEATHER, (e, i) -> {
			e.getWhoClicked().openInventory(ae.getInventory());
		}, "§r§7Back"));
	}
	
	public Button[][] getPages() {
		return pages;
	}
}