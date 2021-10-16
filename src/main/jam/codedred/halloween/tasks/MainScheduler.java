package jam.codedred.halloween.tasks;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;

import jam.codedred.halloween.Halloween;

public class MainScheduler {
	
	protected static final Set<PluginScheduler> asyncSchedulers = new HashSet<>();
	protected static final Set<PluginScheduler> syncSchedulers = new HashSet<>();
	
	private static boolean running = false;
	@SuppressWarnings("deprecation")
	public static void start() {
		if (running) return;
		running = true;
		
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(Halloween.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (final PluginScheduler scheduler : asyncSchedulers) {
					scheduler.tick();
				}
			}
		}, 0, 1);
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Halloween.getInstance(), new Runnable() {
			@Override
			public void run() {
				for (final PluginScheduler scheduler : syncSchedulers) {
					scheduler.tick();
				}
			}
		}, 0, 1);
	}
	
	public static boolean isRunning() {
		return running;
	}
	
}