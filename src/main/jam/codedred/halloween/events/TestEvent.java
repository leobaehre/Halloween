package jam.codedred.halloween.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TestEvent implements Listener {
	
	// test event
	@EventHandler
	void join(PlayerJoinEvent e) {
		System.out.println(e.getPlayer().getName() + " joined.");
	}
	
}