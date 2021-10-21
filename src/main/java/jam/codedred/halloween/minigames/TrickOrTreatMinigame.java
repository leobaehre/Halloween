package jam.codedred.halloween.minigames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import jam.codedred.halloween.StartingKit;
import jam.codedred.halloween.minigame.Minigame;
import jam.codedred.halloween.minigame.MinigameManager;
import jam.codedred.halloween.minigame.MinigameManager.ScoreboardManager;
import jam.codedred.halloween.utils.ChatUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

//trick or treat minigame class
/*TODO:
 * - fix onTick
 * - make resource pack
 * - modify woodland mansion for minigame map
 * - add tricks/jumpscares*/
public class TrickOrTreatMinigame extends Minigame {
	
	private Map<Player, Integer> treats = new HashMap<Player, Integer>();
	private List<GlowItemFrame> selectedFrames = new ArrayList<GlowItemFrame>();
	private List<GlowItemFrame> treatFrames = new ArrayList<GlowItemFrame>();
	private List<ItemStack> treatItems = new ArrayList<ItemStack>();
	private int seconds = 0;
	private int minutes = 5;

	public TrickOrTreatMinigame() {
		super("TrickOrTreat",
				"Trick or Treat",
				//placeholder item, might be changed
				new ItemStack(Material.GOLDEN_APPLE),
				new StartingKit(),
				null,
				//random location for testing, will be changed
				new Location(Bukkit.getWorld("jamtest"), 2899793.5, 64.5, 1091042.5, -180, 0));
	}

	@Override
	public void onStart() {
		teleportPlayers();
        for (String name : MinigameManager.getPlayers()) {
            Player player = Bukkit.getPlayer(name);
            addTreats(player, 0);
            ScoreboardManager.setScoreboard(player);
            //Intro title and a quick dialogue on how the game works
            player.sendTitle(ChatUtil.colorize("&e&lTrick or Treat"), "", 5, 60, 5);
            String[] broadcast = new String[] {
            		"",
            		ChatUtil.colorize("&e&l              Game " + MinigameManager.game + " - Trick or Treat"),
                    ChatUtil.colorize("&f------------------------------------------------"),
                    "",
                    ChatUtil.colorize("&a In this game you must find treats hidden"),
                    ChatUtil.colorize("&a in the mansion"),
                    "",
                    ChatUtil.colorize("&a You will have 5 minutes to play"),
                    ChatUtil.colorize("&a The player that collects the most treats wins!"),
                    "",
                    ChatUtil.colorize("&a But don't get tricked!"),
                    ChatUtil.colorize("&a If you get tricked you will lose 3 treats!"),
                    "",
                    ChatUtil.colorize("&f------------------------------------------------"),
                    ""
            };
            ChatUtil.broadcast(broadcast);
        }
        ScoreboardManager.sortScoreboard();
        setTreatList();
        setTreats();
	}

	int ticks = 0;
	//this is not running
	@Override
	public void onTick() {
		System.out.println("tick");
		ticks++;
		if (ticks >= 20) {
			if (seconds == 0 && minutes == 0) {
				endGame();
				return;
			}
			if (seconds == 0) {
				minutes--;
				seconds = 59;
			} else seconds--;
			ticks = 0;
		}
		//action bar display for treats and game timer, untested because onTick is not working
		for (String name : MinigameManager.getPlayers()) {
			Player player = Bukkit.getPlayer(name);
			String message = ChatUtil.colorize("&e&lTreats: &f&6" + treats.get(player) + " &f&e&l - Time: &f&6" + minutes + ":" + seconds);
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
		}
	}

	@Override
	public void onEnd() {
		restoreFrames();
	}
	
	//selects random spots for a random treat to spawn on the map
	private void setTreats() {
		for (Entity entity : Bukkit.getWorld("jamtest").getEntities()) {
			if (entity.getType() == EntityType.GLOW_ITEM_FRAME) {
				GlowItemFrame frame = (GlowItemFrame) entity;
				if (frame.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatUtil.colorize("&fTreat"))) {
					treatFrames.add(frame);
					frame.setFixed(true);
				}
			}
		}
		
		Collections.shuffle(treatFrames);
		for (int x = 0;x < 10;x++) {
			selectedFrames.add(treatFrames.get(x));
		}
		
		for (GlowItemFrame frame : selectedFrames) {
			frame.setVisible(false);
			frame.setItem(treatItems.get(ThreadLocalRandom.current().nextInt(0, treatItems.size() - 1)));
		}
		
		List<GlowItemFrame> treatsToRemove = treatFrames;
		treatsToRemove.removeAll(selectedFrames);
		for (GlowItemFrame frame : treatsToRemove) {
			frame.setVisible(false);
			frame.setItem(new ItemStack(Material.AIR));
		}
	}
	
	//adds the possible treats to a list they are dyes for now
	//but i will make a resource pack to make them look like different candies/treats
	private void setTreatList() {
		addTreatItem(Material.RED_DYE);
		addTreatItem(Material.PINK_DYE);
		addTreatItem(Material.ORANGE_DYE);
		addTreatItem(Material.GREEN_DYE);
		addTreatItem(Material.LIME_DYE);
		addTreatItem(Material.LIGHT_BLUE_DYE);
		addTreatItem(Material.GRAY_DYE);
		addTreatItem(Material.BROWN_DYE);
	}
	//method for setTreatList
	private void addTreatItem(Material mat) {
		ItemStack item = new ItemStack(mat);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatUtil.colorize("&fTreat"));
		item.setItemMeta(meta);
		treatItems.add(item);
	}
	
	//adds treats to a player
	private void addTreats(Player player, int amount) {
		int t = treats.containsKey(player) ? treats.get(player) : 0;
		amount = (t + amount) < 0 ? 0 : t + amount;
		treats.put(player, amount);
	}
	
	//this event is for detecting when a treat is clicked/found
	public void onFrameClick(PlayerInteractAtEntityEvent event) {
		Player player = (Player) event.getPlayer();
		if (event.getRightClicked().getType() == EntityType.GLOW_ITEM_FRAME) {
			GlowItemFrame frame = (GlowItemFrame) event.getRightClicked();
			if (frame.getItem().getItemMeta() == null) return;
			if (frame.getItem().getItemMeta().getDisplayName() == null) return;
			if (frame.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatUtil.colorize("&fTreat"))) {
				event.setCancelled(true);
				frame.setVisible(false);
				frame.setItem(new ItemStack(Material.AIR));
				addTreats(player, 1);
				ChatUtil.broadcast(ChatUtil.colorize("&e" + player.getName() + " Has collected a treat!\nThey have &f&6" + treats.get(player) + "&f&e treats!"));
			}
		}
	}
	
	//restores the item frames to the placeholder item
	//frames with the barrier named treat are placed in the map to choose all possible treat locations
	private void restoreFrames() {
		for (GlowItemFrame frame : treatFrames) {
			frame.setVisible(true);
			ItemStack item = new ItemStack(Material.BARRIER);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(ChatUtil.colorize("&fTreat"));
			item.setItemMeta(meta);
			frame.setItem(item);
		}
	}
	
}
