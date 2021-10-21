package jam.codedred.halloween;

import org.bukkit.plugin.java.JavaPlugin;

import jam.codedred.halloween.commands.SpawnCommand;
import jam.codedred.halloween.commands.StartCommand;
import jam.codedred.halloween.commands.StartMinigameCommand;
import jam.codedred.halloween.events.JoinGameEvent;
import jam.codedred.halloween.events.TrickOrTreatListener;
import jam.codedred.halloween.minigame.MinigameManager;
import jam.codedred.halloween.tasks.MainScheduler;

public final class Halloween extends JavaPlugin {

    public static Halloween INSTANCE;
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        
        loadCommands();
        loadEvents();
        
        //method to replace the minigame load method
        MinigameManager.createMinigamesList();
        
        MainScheduler.start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    private void loadCommands() {
    	this.getCommand("start").setExecutor(new StartCommand());
    	this.getCommand("startminigame").setExecutor(new StartMinigameCommand());
    	this.getCommand("spawn").setExecutor(new SpawnCommand());
    	//command load method removed, it was broken
    }
    
    private void loadEvents() {
    	this.getServer().getPluginManager().registerEvents(new JoinGameEvent(), this);
    	this.getServer().getPluginManager().registerEvents(new TrickOrTreatListener(), this);
    	//event load method removed, it was broken
    }
    
  //minigame load method removed, it was also broken
}