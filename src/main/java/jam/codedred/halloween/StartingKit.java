package jam.codedred.halloween;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class StartingKit {
	
	public final HashMap<Integer, ItemStack> kit = new HashMap<>();
	
	public StartingKit item(int slot, ItemStack item) {
		kit.put(slot, item);
		return this;
	}
	
}