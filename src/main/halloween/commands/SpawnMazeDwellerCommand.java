package jam.codedred.halloween.commands;

import jam.codedred.halloween.Halloween;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnMazeDwellerCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        Location dwellerLoc = new Location(player.getWorld(), 0, 70, 0);
        Location targetLoc = player.getLocation();

        Bukkit.getLogger().info("command /spawnmazedweller");
        Halloween.getInstance().getNms().spawnDweller(player.getWorld(), dwellerLoc, targetLoc);

        return true;
    }
}
