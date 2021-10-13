package jam.codedred.halloween;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import jam.codedred.halloween.MinigameManager.ScoreboardManager;

public class Minigame {
	
	// TODO
	// armor stand with icon as helmet rotating in the beginning
	
	public final String name, displayName;
	public final ItemStack icon;
	public final StartingKit kit;
	public final Location[] locations;
	public final Location armorStandLocation;
	public Minigame(String name, String displayName, ItemStack icon, StartingKit kit, Location armorStandLocation, Location... locations) {
		this.name = name;
		this.displayName = displayName;
		this.icon = icon;
		this.kit = kit;
		this.locations = locations;
		this.armorStandLocation = armorStandLocation;
	}
	
	// teleports all players to their respective locations
	public void teleportPlayers() {
		final Iterator<String> iterator = MinigameManager.getPlayers().iterator();
		int i = 0;
		while (i < 3 && iterator.hasNext()) {
			final Player player = Bukkit.getPlayer(iterator.next());
			if (player != null) player.teleport(locations[i++]);
		}
	}
	
	// gives the starting kit (if it isn't null) to all players in the candies hashmap
	public void giveKit() {
		if (kit == null) return;
		final Iterator<String> iterator = MinigameManager.getPlayers().iterator();
		int i = 0;
		while (i < 3 && iterator.hasNext()) {
			final Player player = Bukkit.getPlayer(iterator.next());
			if (player != null) {
				for (int slot : kit.kit.keySet()) {
					player.getInventory().setItem(slot, kit.kit.get(slot));
				}
			}
		}
	}
	
	// sets the current game's name to its name
	public void updateScoreboard() {
		ScoreboardManager.setGameName(name);
	}
	
}