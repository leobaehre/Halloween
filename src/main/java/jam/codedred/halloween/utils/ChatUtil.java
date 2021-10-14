package jam.codedred.halloween.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatUtil {

    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static void tell(Player player, String... messages) {
        for (String message : messages) {
            player.sendMessage(colorize(message));
        }
    }
}
