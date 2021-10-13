package jam.codedred.halloween;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class StartingKit {
	
	public final HashMap<Integer, ItemStack> kit = new HashMap<>();
	
	// adds an item to the kit, receives a slot and an item
	public StartingKit item(int slot, ItemStack item) {
		kit.put(slot, item);
		return this;
	}
	
}