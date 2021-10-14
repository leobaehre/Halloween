package jam.codedred.halloween.commands;

import jam.codedred.halloween.minigame.Minigame;
import jam.codedred.halloween.minigame.MinigameManager;
import jam.codedred.halloween.utils.CommandInformation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInformation("next")
public class NextCommand implements CommandExecutor {

    // Starts Test Minigame (Mostly for testing!)
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            MinigameManager.switchToMiniGame(MinigameManager.getNextMinigame());
        }

        return true;
    }
}
