package jam.codedred.halloween.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import jam.codedred.halloween.minigame.Minigame;
import jam.codedred.halloween.minigame.MinigameManager;

public class StartMinigameCommand implements CommandExecutor {

	//Allows you to start any minigame using /startminigame name 
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("startminigame")) {
			if (args.length < 1) {
				sender.sendMessage(ChatColor.RED + "Usage: /startminigame name");
				return true;
			}
			if (MinigameManager.getMinigame(args[0]) != null) {
				Minigame game = MinigameManager.getMinigame(args[0]);
				game.startGame();
				return true;
			}
			sender.sendMessage(ChatColor.RED + "Invalid minigame name");
			return true;
		}
		return false;
	}

}
