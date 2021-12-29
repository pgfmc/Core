package net.pgfmc.core.inventoryAPI;

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
