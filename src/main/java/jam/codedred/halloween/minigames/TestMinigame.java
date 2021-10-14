package jam.codedred.halloween.minigames;

import jam.codedred.halloween.StartingKit;
import jam.codedred.halloween.minigame.Minigame;
import jam.codedred.halloween.minigame.MinigameManager;
import jam.codedred.halloween.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import jam.codedred.halloween.minigame.MinigameManager.ScoreboardManager;


/*
 *
 *  TEMPLATE FOR A MINIGAME
 *
 *
 */

public class TestMinigame extends Minigame {
    public TestMinigame() {
        super("Test",
                "Test Game",
                new ItemStack(Material.DIAMOND_SWORD),
                new StartingKit(),
                null,
                new Location(Bukkit.getWorld("world"), 0, 75, 0),
                new Location(Bukkit.getWorld("world"), 0, 75, 0),
                new Location(Bukkit.getWorld("world"), 0, 75, 0)
        );
    }

    @Override
    public void onStart() {
        giveKit();
        teleportPlayers();
        for (String name : MinigameManager.getPlayers()) {
            Player player = Bukkit.getPlayer(name);
            ScoreboardManager.setScoreboard(player);
            ChatUtil.broadcast("&aGame has started!");
        }
        ScoreboardManager.sortScoreboard();
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onEnd() {
        for (String name : MinigameManager.getPlayers()) {
            MinigameManager.addPlayerCandies(name, 1);
        }
    }

    @EventHandler
    public static void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }
}
