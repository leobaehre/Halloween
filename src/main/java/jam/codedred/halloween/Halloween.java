package jam.codedred.halloween;

import jam.codedred.halloween.minigame.Minigame;
import jam.codedred.halloween.minigame.MinigameManager;
import jam.codedred.halloween.utils.CommandInformation;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public final class Halloween extends JavaPlugin {

    public static Halloween INSTANCE;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        
        loadCommands();
        loadEvents();
        loadMinigames();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    private void loadCommands() {
    	try {
			for (final ClassInfo classInfo : ClassPath.from(getClassLoader()).getTopLevelClassesRecursive("jam.codedred.halloween.commands")) {
				final Class<?> cls = Class.forName(classInfo.getName());
				if (CommandExecutor.class.isAssignableFrom(cls) && cls.isAnnotationPresent(CommandInformation.class)) {
					try {
						getCommand(cls.getDeclaredAnnotation(CommandInformation.class).value()).setExecutor((CommandExecutor) cls.newInstance());
					} catch (InstantiationException | IllegalAccessException e) {
						Bukkit.getConsoleSender().sendMessage("§cCommand in class " + cls.getName() + " couldn't be loaded.");
						continue;
					}
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			Bukkit.getConsoleSender().sendMessage("§cHalloween plugin wasn't able to load commands.");
		}
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

    private void loadMinigames() {
		try {
			for (final ClassInfo classInfo : ClassPath.from(getClassLoader()).getTopLevelClassesRecursive("jam.codedred.halloween.minigames")) {
				try {
					MinigameManager.minigameList.add((Minigame) Class.forName(classInfo.getName()).newInstance());
				} catch (Exception ex) {
					continue;
				}
			}
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage("§cHalloween plugin wasn't able to load minigames.");
		}
	}
    
}