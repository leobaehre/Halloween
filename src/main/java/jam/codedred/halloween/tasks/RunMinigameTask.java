package jam.codedred.halloween.tasks;

import jam.codedred.halloween.minigame.Minigame;
import org.bukkit.scheduler.BukkitRunnable;

public class RunMinigameTask extends BukkitRunnable {

    Minigame minigame;

    public RunMinigameTask(Minigame minigame) {
        this.minigame = minigame;
    }

    @Override
    public void run() {
        minigame.onTick();
    }
}
