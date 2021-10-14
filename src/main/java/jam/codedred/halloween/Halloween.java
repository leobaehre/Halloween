package jam.codedred.halloween;

import jam.codedred.halloween.commands.SpawnCommand;
import jam.codedred.halloween.commands.StartCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Halloween extends JavaPlugin {

    static Halloween INSTANCE;
    public static Halloween getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        INSTANCE = this;

        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("start").setExecutor(new StartCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
