package net.pgfmc.core.inventoryAPI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;

public abstract class PagedInventory<T> extends InteractableInventory {
	
	/**
	 * The List of entries, in the form of Button(s).
	 */
	protected List<Button> entries;
	
	/**
	 * The Size of the Inventory. (BIG (56 slots) or SMALL (27 slots))
	 */
	SizeData sizeD;
	
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
	 * Enum to store data for chest sizes.
	 * holds data for chest size, and the amount of entries available for each page.
	 * @author CrimsonDart
	 *
	 */
	public enum SizeData {
		BIG(56, 36.0f),
		SMALL(27, 21.0f);
		
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
	 * 
	 * @param mat
	 * @param name
	 * @param lore
	 * @param function
	 * @return
	 */
	public static Button createButton(Material mat, String name, String lore, Butto function) {
		return new Button(mat, 0, name, lore, function);
	}
	
	public interface ItemController<T> {
		
		Button makeButton(T entry);
	}
	
	/**
	 * Constructor for PagedInventory. The inventory can be in two sizes: 27 or 56 (single or double chest). 
	 * @param size The size of the inventory. can only be 27 or 56.
	 * @param name Name displayed at the top of the inventory's interface.
	 * @param entries The list of data that will fill the pages. 
	 * @param itemController The function that is ran per entry in "entries"; itemController must return a button Object, with the entry itself as the input.
	 * @param mat The material that all entries will be set to.
	 */
	public PagedInventory(SizeData size, String name, Collection<T> entries, ItemController<T> itemController) {
		super(size.getSize(), name);
		
		this.sizeD = size;
		
		this.entries = entries.stream()
		.map((t) -> {
			return itemController.makeButton(t);
		})
		.sorted((x1, x2) -> {
			
			String n1 = x1.getName();
			String n2 = x2.getName();
			
			if (n1.length() > n2.length()) {
				
				for (int integer = 0; integer < n2.length(); integer++) {
					if (n1.charAt(integer) == n2.charAt(integer)) {
						continue;
					} else {
						return Character.compare(n1.charAt(integer), n2.charAt(integer));
					}
				}
			} else {
				for (int integer = 0; integer < n1.length(); integer++) {
					if (n1.charAt(integer) == n2.charAt(integer)) {
						continue;
					} else {
						return Character.compare(n1.charAt(integer), n2.charAt(integer));
					}
				}
			}
			return 0;
		})
		.collect(Collectors.toList());
		
		this.pages = (int) Math.ceil(entries.size() / size.getPageSize());
		
		setPage(page);
		System.out.println(String.valueOf(pages));
		System.out.println(String.valueOf(entries.size()));
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
		
		buttons = new ArrayList<>(size);
		inv.clear();
		
		if (newPage > pages || newPage < 1) {
			return;
		}
		
		this.page = newPage;
		
		// Places the next and previous page buttons in their respective spots in the inventory.
		if (page > 1) {
			if (sizeD == SizeData.BIG) {
				createButton(Material.IRON_HOE, 48, "Previous Page", (x, e) -> {
					flipPage(-1);
				});
			} else {
				createButton(Material.IRON_HOE, 9, "Previous Page", (x, e) -> {
					flipPage(-1);
				});
			}
		}
		
		if (page < pages) {
			if (sizeD == SizeData.BIG) {
				createButton(Material.ARROW, 50, "Next Page", (x, e) -> {
					flipPage(+1);
				});
			} else {
				createButton(Material.ARROW, 18, "Next Page", (x, e) -> {
					flipPage(+1);
				});
			}
		}
		
		// Places the entries in the correct spots. ----- WIP
		if (sizeD == SizeData.BIG) {
			for (int i = 0; i < 36; i++) {
				if (i >= entries.size()) { // if "i" gets bigger than the entries size.
					
					Button button = new Button(i);
					this.buttons.add(button);
					inv.setItem(i, button.getItem());
					
				} else {
					
					Button button = entries.get(i + (page - 1) * 36);
					this.buttons.add(button);
					inv.setItem(i, button.getItem());
				}
			}
		} else if (sizeD == SizeData.SMALL) {
			for (int i = 0; i < 21; i++) {
				if (i + (page - 1) * 21 >= entries.size()) { // if "i" gets bigger than the entries size.
					
					Button button = new Button(entryToSlot(i));
					this.buttons.add(button);
					inv.setItem(entryToSlot(i), button.getItem());
				} else {
					Button button = entries.get(i + (page - 1) * 21);
					
					button.setSlot(entryToSlot(i));
					this.buttons.add(button);
					inv.setItem(entryToSlot(i), button.getItem());
				}
			}
		}
		
		// re-sets the button in the top left
		if (persistentButton != null) {
			buttons.add(persistentButton);
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
	protected void setPersistentButton(Material mat, String name, String lore, Butto function) {
		persistentButton = new Button(mat, 0, name, lore, function);
	}
	
	/**
	 * Sets the persistent button to 
	 * @param ae
	 */
	public void setBackButton(InteractableInventory ae) {
		persistentButton = new Button(Material.FEATHER, 0, "Back", null, (x, e) -> {
			x.openInventory(ae.getInventory());
		});
		buttons.add(persistentButton);
		inv.setItem(0, persistentButton.getItem());
	}
	
	public List<Button> getEntries() {
		return entries;
	}
}