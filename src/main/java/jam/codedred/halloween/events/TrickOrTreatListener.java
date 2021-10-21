package jam.codedred.halloween.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import jam.codedred.halloween.minigame.MinigameManager;
import jam.codedred.halloween.minigames.TrickOrTreatMinigame;

//Listener for events in the trick or treat game
public class TrickOrTreatListener implements Listener {
	
	
	@EventHandler
	public void onEntityClick(PlayerInteractAtEntityEvent event) {
		if (event.getHand() == EquipmentSlot.HAND) {
			TrickOrTreatMinigame game = (TrickOrTreatMinigame) MinigameManager.getMinigame("trickortreat");
			game.onFrameClick(event);
		}
	}

}
