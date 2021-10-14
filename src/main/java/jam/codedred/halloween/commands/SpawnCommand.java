package jam.codedred.halloween.commands;

import jam.codedred.halloween.utils.ChatUtil;
import jam.codedred.halloween.utils.CommandInformation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

@CommandInformation("spawn")
public class SpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            Villager entity = (Villager) player.getWorld().spawnEntity(player.getLocation(), EntityType.VILLAGER);
            entity.setBaby();
            entity.setInvulnerable(true);
            entity.setCanPickupItems(false);
            entity.setAgeLock(true);
            entity.setCustomName(ChatUtil.colorize("&a&lLittle Johnny"));
            entity.setCustomNameVisible(true);
        }

        return true;
    }
}
