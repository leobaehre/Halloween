package jam.codedred.halloween.utils;

import jam.codedred.halloween.minigame.MinigameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtil {

    // Colorize strings with colorcodes
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    // Tell a specific player a message
    public static void tell(Player player, String... messages) {
        for (String message : messages) {
            player.sendMessage(colorize(message));
        }
    }

    // Broadcast a message to all the players in the game
    public static void broadcast(String... messages) {
        for (String name : MinigameManager.getPlayers()) {
            Player player = Bukkit.getPlayer(name);
            tell(player, messages);
        }
    }
}
