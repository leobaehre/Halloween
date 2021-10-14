package jam.codedred.halloween.minigame;

import java.util.*;

import jam.codedred.halloween.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class MinigameManager {

	static List<Minigame> minigameList = new ArrayList<>();
	static List<Minigame> roundMinigameList = new ArrayList<>();

	// Candies Manager ->

	private static HashMap<String, Integer> playerCandies = new HashMap<>();

	// returns all players in the hashmap
	public static Set<String> getPlayers() {
		return playerCandies.keySet();
	}

	// returns the amount of candies the player has
	// this method receives the name of the player
	public static int getPlayerCandies(String player) {
		return playerCandies.getOrDefault(player.toLowerCase(), 0);
	}

	// returns the amount of candies the player has
	// this method receives the instance of the player
	public static int getPlayerCandies(Player player) {
		return getPlayerCandies(player.getName());
	}

	// sets the amount of candies the player has
	// this method receives the name and the amount of candies
	public static void setPlayerCandies(String player, int candies) {
		playerCandies.put(player.toLowerCase(), candies);
	}

	// sets the amount of candies the player has
	// this method receives the instance and the amount of candies
	public static void setPlayerCandies(Player player, int candies) {
		setPlayerCandies(player.getName(), candies);
	}

	// adds an amount of candies to the player
	// this method receives the name of the player and the amount of candies
	public static void addPlayerCandies(String player, int candies) {
		setPlayerCandies(player, getPlayerCandies(player) + candies);
	}

	// resets thoroughly the hashmap
	// it might be used to new rounds
	// notice that you might add the player back in the hashmap if you want the scoreboard to show them
	public static void resetCandies() {
		playerCandies = new HashMap<>();
	}

	// <- Candies Manager

	public static void selectGames(int amount) {
		for (int i =0; i < amount; i++) {
			if (amount <= minigameList.size()) continue;
			Minigame selected = minigameList.get(new Random().nextInt(minigameList.size()));
			if (roundMinigameList.contains(selected)) selected = minigameList.get(new Random().nextInt(minigameList.size()));
			roundMinigameList.add(selected);
		}
	}

	// Scoreboard Manager ->

	@SuppressWarnings("deprecation")
	public static class ScoreboardManager {
		private static final Scoreboard scoreboard;
		private static final Objective objective;
		private static final Team first, second, third, game;
		private static OfflinePlayer firstPlayer, secondPlayer, thirdPlayer, gamePlayer;

		static {
			scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

			first = scoreboard.registerNewTeam("first");
			second = scoreboard.registerNewTeam("second");
			third = scoreboard.registerNewTeam("third");
			game = scoreboard.registerNewTeam("game");

			game.setColor(ChatColor.YELLOW);
			first.setColor(ChatColor.YELLOW);
			second.setColor(ChatColor.YELLOW);
			third.setColor(ChatColor.YELLOW);

			first.setPrefix(ChatUtil.colorize("&e#1 "));
			second.setPrefix(ChatUtil.colorize("&e#2 "));
			third.setPrefix(ChatUtil.colorize("&e#3 "));

			first.setSuffix(ChatUtil.colorize("&e - 0"));
			second.setSuffix(ChatUtil.colorize("&e - 0"));
			third.setSuffix(ChatUtil.colorize("&e - 0"));
			game.setSuffix(ChatUtil.colorize("&eNone"));

			firstPlayer = Bukkit.getOfflinePlayer(ChatUtil.colorize("&eNone"));
			secondPlayer = Bukkit.getOfflinePlayer(ChatUtil.colorize("&a&eNone"));
			thirdPlayer = Bukkit.getOfflinePlayer(ChatUtil.colorize("&b&eNone"));
			gamePlayer = Bukkit.getOfflinePlayer(ChatUtil.colorize("&fGame: "));

			first.addPlayer(firstPlayer);
			second.addPlayer(secondPlayer);
			third.addPlayer(thirdPlayer);
			game.addPlayer(gamePlayer);

			objective = scoreboard.registerNewObjective("candies", "dummy", ChatUtil.colorize("&6Halloween"));
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.getScore(ChatUtil.colorize("&e")).setScore(6);
			objective.getScore(ChatUtil.colorize("&fRanking")).setScore(5);
			objective.getScore(firstPlayer).setScore(4);
			objective.getScore(secondPlayer).setScore(3);
			objective.getScore(thirdPlayer).setScore(2);
			objective.getScore(ChatUtil.colorize("&f")).setScore(1);
			objective.getScore(gamePlayer).setScore(0);
		}

		// sets the name of the current game
		public static void setGameName(String name) {
			game.setSuffix(ChatUtil.colorize("&e" + name));
		}

		// sets up the amount of candies in the scoreboard
		private static void setPlayerPlace(Team place, String player) {
			place.setSuffix(ChatUtil.colorize("&e - " + getPlayerCandies(player.toLowerCase())));
		}

		private static void setFirstPlayer(OfflinePlayer player) {
			scoreboard.resetScores(firstPlayer);
			first.removePlayer(firstPlayer);
			first.addPlayer(player);
			firstPlayer = player;
			objective.getScore(firstPlayer).setScore(4);
			setPlayerPlace(first, player.getName());
		}

		private static void setSecondPlayer(OfflinePlayer player) {
			scoreboard.resetScores(secondPlayer);
			second.removePlayer(secondPlayer);
			second.addPlayer(player);
			secondPlayer = player;
			objective.getScore(secondPlayer).setScore(3);
			setPlayerPlace(second, player.getName());
		}

		private static void setThirdPlayer(OfflinePlayer player) {
			scoreboard.resetScores(thirdPlayer);
			third.removePlayer(thirdPlayer);
			third.addPlayer(player);
			thirdPlayer = player;
			objective.getScore(thirdPlayer).setScore(2);
			setPlayerPlace(third, player.getName());
		}

		// sorts the scoreboard and organize it from the person who has more candies to the person who has less candies
		public static void sortScoreboard() {
			final String[] ranking = getRanking();
			setFirstPlayer(Bukkit.getOfflinePlayer(ChatUtil.colorize("&e" + ranking[0])));
			setSecondPlayer(Bukkit.getOfflinePlayer(ChatUtil.colorize("&e" + ranking[1])));
			setThirdPlayer(Bukkit.getOfflinePlayer(ChatUtil.colorize("&e" + ranking[2])));
		}

		// get the ranking, from the person who has more candies to the person who has less candies
		public static String[] getRanking() {
			final Set<String> remaining = playerCandies.keySet();
			final String[] ranking = new String[3];
			for (int i = 0; i < 3; i++) {
				int max = -1;
				String p = ChatUtil.colorize(i == 1 ? "&a&eNone" : "&b&eNone");
				for (String player : remaining) {
					final int candies = getPlayerCandies(player);
					if (candies > max) {
						p = player;
						max = candies;
					}
				}
				remaining.remove(p);
				ranking[i] = p;
			}
			return ranking;
		}

		// show the scoreboard to a player
		public static void setScoreboard(Player player) {
			player.setScoreboard(scoreboard);
		}
	}

	// <- Scoreboard Manager

}