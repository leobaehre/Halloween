package jam.codedred.halloween.minigames;

import jam.codedred.halloween.Halloween;
import jam.codedred.halloween.StartingKit;
import jam.codedred.halloween.minigame.Minigame;
import jam.codedred.halloween.minigame.MinigameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static jam.codedred.halloween.utils.ChatUtil.colorize;


public class MazeMinigame extends Minigame {

    public MazeMinigame() {
        super ("Maze",
                "Maze Game",
                new ItemStack(Material.WITHER_SKELETON_SKULL),
                new StartingKit(),
                null,
                new Location(Bukkit.getWorld("world"), 0, 70, 0));
    }

    /*
     * This teleports all the players to the beginning and gives a very quick rundown of how to play before the game starts
     */
    private void runStartCutscene() {
        // force players to look forward (towards the maze) and make them unable to move
        Location freezeLocation = new Location(Bukkit.getWorld("world"), 0, 70, 0);
        BukkitTask forcePlayerLocationTask = Bukkit.getScheduler().runTaskTimer(Halloween.getInstance(), () -> {
            for (String playerName : MinigameManager.getPlayers()) {
                Player player = Bukkit.getPlayer(playerName);
                Objects.requireNonNull(player);
                player.teleport(freezeLocation.setDirection(new Vector(1, 0, 0)));
            }
        }, 0L, 1L);


        AtomicInteger stage = new AtomicInteger(0);
        Bukkit.getScheduler().runTaskTimer(Halloween.getInstance(), () -> {
            List<Player> players = new ArrayList<>();
            for (String playerName : MinigameManager.getPlayers()) {
                Player player = Bukkit.getPlayer(playerName);
                players.add(player);
            }

                switch (stage.get()) {
                    case 0 -> players.forEach(player -> {
                        player.sendTitle(
                                colorize("&4&l&oWelcome to THE MAZE."),
                                colorize("&6Game is starting. . ."),
                                20, 70, 15);
                        player.playSound(player.getLocation(), "entity.wither.spawn", 1f, 1f);
                    });
                    case 1 -> players.forEach(player -> {
                        player.sendTitle(
                                colorize("&4&l&oThe goal is simple."),
                                colorize("&6You must find the end."),
                                20, 70, 15);
                        player.playSound(player.getLocation(), "entity.wither.shoot", 1f, 1f);
                        Bukkit.getScheduler().runTaskLater(Halloween.getInstance(), () -> {
                            for (String playerNameSound : MinigameManager.getPlayers()) {
                                Player playerSound = Bukkit.getPlayer(playerNameSound);
                                Objects.requireNonNull(playerSound);
                                playerSound.playSound(playerSound.getLocation(), "entity.wither.ambient", 1f, 1f);
                            }
                        }, 25L);

                    });

                    case 2 -> {
                        players.forEach(player -> {
                            player.sendTitle(
                                    colorize("&4&l&oT&0&l&o&nhe with&4&l&oered&0&k&l&oFFF &4&l&oroam the maze..."),
                                    colorize("&6If they touch you, you return to the start."),
                                    20, 70, 15);
                            player.playSound(player.getLocation(), "entity.ghast.hurt", 1f, 1f);
                        });
                    }

                    case 3 -> {
                        forcePlayerLocationTask.cancel();
                        endGame();
                    }

                }

            stage.getAndIncrement();

        }, 0L, 70L);
    }

    @Override
    public void onStart() {
        giveKit();
        runStartCutscene();
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onEnd() {

    }
}
