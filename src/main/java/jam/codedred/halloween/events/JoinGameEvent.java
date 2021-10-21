package jam.codedred.halloween.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import jam.codedred.halloween.minigame.MinigameManager;
import jam.codedred.halloween.utils.ChatUtil;

public class JoinGameEvent implements Listener {
	
	//When the player joins, send all players a message and add the player to the game
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatUtil.colorize("&a" + player.getName() + "&e Has joined the game!"));
		}
		MinigameManager.addPlayer(player);
	}
	
}