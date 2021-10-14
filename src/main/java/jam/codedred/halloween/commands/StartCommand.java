package jam.codedred.halloween.commands;

import jam.codedred.halloween.minigame.Minigame;
import jam.codedred.halloween.minigame.MinigameManager;
import jam.codedred.halloween.minigames.TestMinigame;
import jam.codedred.halloween.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    // Starts Test Minigame (Mostly for testing!)
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            for (Player op : Bukkit.getOnlinePlayers()) {
                MinigameManager.setPlayerCandies(op, 0);
            }
            MinigameManager.addPlayerCandies(player.getName(), 1);

            ChatUtil.tell(player, String.valueOf(MinigameManager.getPlayers()), String.valueOf(MinigameManager.getPlayerCandies(player)));

            new TestMinigame().startGame();
        }

        return true;
    }
}
