package net.pgfmc.core.inventoryAPI;

/**
 * Enum to store data for chest sizes.
 * holds data for chest size, and the amount of entries available for each page.
 * @author CrimsonDart
 *
 */
public enum SizeData {
	BIG(56, 36),
	SMALL(27, 21),
	HOPPER(5, 0),
	DROPPER(9, 0);
	
	protected int size;
	protected int pageSize;
	
	SizeData(int size, int pageSize) {
		this.size = size;
		this.pageSize = pageSize;
	}
	
	int getSize() {
		return size;
	}
	
	int getPageSize() {
		return pageSize;
	}
}
