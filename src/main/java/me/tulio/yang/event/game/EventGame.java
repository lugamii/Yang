package me.tulio.yang.event.game;

import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.Locale;
import me.tulio.yang.event.Event;
import me.tulio.yang.event.game.map.EventGameMap;
import me.tulio.yang.event.game.map.vote.EventGameMapVoteData;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.match.participant.GameParticipant;
import me.tulio.yang.match.participant.GamePlayer;
import me.tulio.yang.utilities.Cooldown;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.chat.ChatComponentBuilder;
import me.tulio.yang.utilities.chat.ChatHelper;
import me.tulio.yang.utilities.string.MessageFormat;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventGame {

	@Getter @Setter private static EventGame activeGame;
	@Getter @Setter private static Cooldown cooldown = new Cooldown(0);

	@Getter private final Event event;
	@Getter @Setter private EventGameState gameState;
	@Getter private EventGameLogic gameLogic;
	@Getter @Setter private EventGameMap gameMap;
	@Getter private final GamePlayer gameHost;
	@Getter private final List<GameParticipant<GamePlayer>> participants;
	@Getter private final Map<EventGameMap, EventGameMapVoteData> votesData;
	@Getter private final int maximumPlayers;

	public EventGame(Event event, Player player, int maximumPlayers) {
		this.event = event;
		this.gameHost = new GamePlayer(player.getUniqueId(), player.getName());
		this.participants = new ArrayList<>();
		this.votesData = new HashMap<>();
		this.maximumPlayers = maximumPlayers;

		activeGame = this;
	}

	public int getRemainingParticipants() {
		if (gameState == EventGameState.WAITING_FOR_PLAYERS) {
			return participants.size();
		}

		int i = 0;

		for (GameParticipant participant : participants) {
			if (!participant.isEliminated()) {
				i++;
			}
		}

		return i;
	}

	public int getRemainingPlayers() {
		if (gameState == EventGameState.WAITING_FOR_PLAYERS) {
			return participants.size();
		}

		int i = 0;

		for (GameParticipant<GamePlayer> participant : participants) {
			if (!participant.isEliminated()) {
				i += participant.getPlayers().size();
			}
		}

		return i;
	}

	public GamePlayer getGamePlayer(Player player) {
		for (GameParticipant<GamePlayer> participant : participants) {
			if (participant.containsPlayer(player.getUniqueId())) {
				for (GamePlayer gamePlayer : participant.getPlayers()) {
					if (gamePlayer.getUuid().equals(player.getUniqueId())) {
						return gamePlayer;
					}
				}
			}
		}

		return null;
	}

	public void sendMessage(String message) {
		message = ChatColor.translateAlternateColorCodes('&', message);

		for (GameParticipant<GamePlayer> participant : participants) {
			for (GamePlayer gamePlayer : participant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) player.sendMessage(message);
				}
			}
		}
	}

	public void sendMessage(Locale lang, MessageFormat messageFormat) {
		for (GameParticipant<GamePlayer> participant : participants) {
			for (GamePlayer gamePlayer : participant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						Profile profile = Profile.get(player.getUniqueId());
						messageFormat.setMessage(lang.format(profile.getLocale()));
						messageFormat.send(player);
					}
				}
			}
		}
	}

	public void sendSound(Sound sound, float volume, float pitch) {
		for (GameParticipant<GamePlayer> participant : participants) {
			for (GamePlayer gamePlayer : participant.getPlayers()) {
				if (!gamePlayer.isDisconnected()) {
					Player player = gamePlayer.getPlayer();

					if (player != null) {
						player.playSound(player.getLocation(), sound, volume, pitch);
					}
				}
			}
		}
	}

	public void broadcastJoinMessage() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			Profile profile = Profile.get(player.getUniqueId());
			List<BaseComponent[]> compiledComponents = new ArrayList<>();
			List<String> lines = new MessageFormat(Locale.EVENT_JOIN_BROADCAST.format(profile.getLocale()))
								.add("{event_name}", event.getName())
								.add("{event_displayname}", event.getDisplayName())
								.add("{host_name}", CC.RED + gameHost.getUsername())
								.toList();

			for (String line : lines) {
				compiledComponents.add(new ChatComponentBuilder("")
					.parse(line)
					.attachToEachPart(ChatHelper.hover(Locale.EVENT_JOIN_HOVER.getString(profile.getLocale())))
					.attachToEachPart(ChatHelper.click("/event join"))
					.create());
			}

			for (BaseComponent[] components : compiledComponents) {
				player.spigot().sendMessage(components);
			}
		}
	}

	public void start() {
		gameState = EventGameState.WAITING_FOR_PLAYERS;
		gameLogic = event.start(this);
	}

}
