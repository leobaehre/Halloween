package jam.codedred.halloween.commands;

import jam.codedred.halloween.minigame.MinigameManager;
import jam.codedred.halloween.minigames.MazeMinigame;
import jam.codedred.halloween.utils.ChatUtil;
import jam.codedred.halloween.utils.CommandInformation;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInformation("maze")
public class StartMazeGameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;

        for (Player op : Bukkit.getOnlinePlayers()) {
            MinigameManager.setPlayerCandies(op, 0);
        }
        MinigameManager.addPlayerCandies(player.getName(), 1);

        ChatUtil.tell(player, String.valueOf(MinigameManager.getPlayers()), String.valueOf(MinigameManager.getPlayerCandies(player)));

        player.sendMessage("Starting maze game");
        new MazeMinigame().startGame();

        return true;
    }
}
