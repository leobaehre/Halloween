package jam.codedred.halloween.minigame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jam.codedred.halloween.Halloween;
import jam.codedred.halloween.StartingKit;
import jam.codedred.halloween.tasks.RunMinigameTask;
import jam.codedred.halloween.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import jam.codedred.halloween.minigame.MinigameManager.ScoreboardManager;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class Minigame implements Listener {
	
	// TODO
	// armor stand with icon as helmet rotating in the beginning
	
	public final String name, displayName;
	public final ItemStack icon;
	public final StartingKit kit;
	public final Location[] locations;
	public final Location armorStandLocation;

	private BukkitTask task;

	public Minigame(String name, String displayName, ItemStack icon, StartingKit kit, Location armorStandLocation, Location... locations) {
		this.name = name;
		this.displayName = displayName;
		this.icon = icon;
		this.kit = kit;
		this.locations = locations;
		this.armorStandLocation = armorStandLocation;
		Bukkit.getPluginManager().registerEvents(this, Halloween.INSTANCE);
	}

	protected void unRegisterEvents() {
		HandlerList.unregisterAll(this);
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
			i++;
		}
	}

	public void endGame() {
		task.cancel();
		onEnd();
		this.unRegisterEvents();
	}

	public void startGame() {
		task = new RunMinigameTask(this).runTaskTimer(Halloween.INSTANCE, 0, 1);
		onStart();
		updateScoreboard();
	}
	
	// sets the current game's name to its name
	public void updateScoreboard() {
		ScoreboardManager.setGameName(name);
	}

	public abstract void onStart();
	public abstract void onTick();
	public abstract void onEnd();

	protected void broadcast(String... messages) {
		for (String name : MinigameManager.getPlayers()) {
			Player player = Bukkit.getPlayer(name);
			ChatUtil.tell(player, messages);
		}
	}
}