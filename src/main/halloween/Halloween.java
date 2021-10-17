package jam.codedred.halloween;

import jam.codedred.halloween.commands.SpawnCommand;
import jam.codedred.halloween.commands.SpawnMazeDwellerCommand;
import jam.codedred.halloween.commands.StartCommand;
import jam.codedred.halloween.minigame.Minigame;
import jam.codedred.halloween.minigame.MinigameManager;
import jam.codedred.halloween.nms.NMS;
import jam.codedred.halloween.tasks.MainScheduler;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public final class Halloween extends JavaPlugin {

    private static Halloween instance;
    private NMS nms;
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        initNMS();
        loadCommands();
        loadEvents();
        registerMinigames();
        
        MainScheduler.start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initNMS() {
    	String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
    	Bukkit.getLogger().info("version: " + version);
    	try {
    		nms = (NMS) Class.forName("jam.codedred.halloween.nms.NMS_" + version).getDeclaredConstructor().newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
    
    private void loadCommands() {
    	getCommand("spawn").setExecutor(new SpawnCommand());
    	getCommand("start").setExecutor(new StartCommand());
    	getCommand("maze").setExecutor(null);
    	getCommand("spawnmazedweller").setExecutor(new SpawnMazeDwellerCommand());
    }
    
    private void loadEvents() {
    	try {
			for (final ClassInfo classInfo : ClassPath.from(getClassLoader()).getTopLevelClassesRecursive("jam.codedred.halloween.events")) {
				final Class<?> cls = Class.forName(classInfo.getName());
				if (Listener.class.isAssignableFrom(cls)) {
					try {
						Bukkit.getPluginManager().registerEvents((Listener) cls.newInstance(), this);
					} catch (InstantiationException | IllegalAccessException e) {
						Bukkit.getConsoleSender().sendMessage("§cEvents in class " + cls.getName() + " couldn't be loaded.");
						continue;
					}
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			Bukkit.getConsoleSender().sendMessage("§cHalloween plugin wasn't able to load events.");
		}
    }

    private void registerMinigames() {
		try {
			for (final ClassInfo classInfo : ClassPath.from(getClassLoader()).getTopLevelClassesRecursive("jam.codedred.halloween.minigames")) {
				try {
					MinigameManager.minigamesList.add((Minigame) Class.forName(classInfo.getName()).newInstance());
				} catch (Exception ex) {
					continue;
				}
			}
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage("§cHalloween plugin wasn't able to load minigames.");
		}
	}

	public static Halloween getInstance() {
    	return instance;
	}

	public NMS getNms() {
		return nms;
	}
}