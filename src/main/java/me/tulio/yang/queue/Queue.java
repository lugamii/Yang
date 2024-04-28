package me.tulio.yang.queue;

import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.Locale;
import me.tulio.yang.Yang;
import me.tulio.yang.hotbar.Hotbar;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.match.Match;
import me.tulio.yang.match.participant.MatchGamePlayer;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.match.participant.GameParticipant;
import me.tulio.yang.queue.thread.QueueThread;
import me.tulio.yang.utilities.TaskUtil;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Getter
public class Queue {

	@Getter private static final List<Queue> queues = new ArrayList<>();

	@Getter public static final boolean pingRangeBoolean = Yang.get().getMainConfig().getBoolean("QUEUE.RANKED_PING_RANGE_BOOLEAN");
	@Getter public static int pingRange = Yang.get().getMainConfig().getInteger("QUEUE.RANKED_PING_RANGE");

	private final UUID uuid = UUID.randomUUID();
	private final Kit kit;
	@Setter private boolean ranked;
	private final LinkedList<QueueProfile> players = new LinkedList<>();

	public Queue(Kit kit, boolean ranked) {
		this.kit = kit;
		this.ranked = ranked;

		queues.add(this);
	}

	public String getQueueName() {
		return (ranked ? "Ranked" : "Unranked") + " " + kit.getName();
	}

	public void addPlayer(Player player, int elo) {
		QueueProfile queueProfile = new QueueProfile(this, player.getUniqueId());
		queueProfile.setElo(elo);

		Profile profile = Profile.get(player.getUniqueId());
		profile.setQueueProfile(queueProfile);
		profile.setState(ProfileState.QUEUEING);

		players.add(queueProfile);

		Hotbar.giveHotbarItems(player);

		if (ranked) {
			new MessageFormat(Locale.QUEUE_JOIN_RANKED.format(profile.getLocale()))
				.add("{kit_name}", kit.getName())
				.add("{elo}", String.valueOf(elo))
				.send(player);
		} else {
			new MessageFormat(Locale.QUEUE_JOIN_UNRANKED.format(profile.getLocale()))
				.add("{kit_name}", kit.getName())
				.send(player);
		}
	}

	public void removePlayer(QueueProfile queueProfile) {
		players.remove(queueProfile);

		Profile profile = Profile.get(queueProfile.getPlayerUuid());
		profile.setQueueProfile(null);
		profile.setState(ProfileState.LOBBY);

		Player player = Bukkit.getPlayer(queueProfile.getPlayerUuid());

		if (player != null) {
			Hotbar.giveHotbarItems(player);

			if (ranked) {
				new MessageFormat(Locale.QUEUE_LEAVE_RANKED.format(profile.getLocale()))
					.add("{kit_name}", kit.getName())
					.send(player);
			} else {
				new MessageFormat(Locale.QUEUE_LEAVE_UNRANKED.format(profile.getLocale()))
					.add("{kit_name}", kit.getName())
					.send(player);
			}
		}

	}

	public static Queue getByUuid(UUID uuid) {
		for (Queue queue : queues) {
			if (queue.getUuid().equals(uuid)) {
				return queue;
			}
		}

		return null;
	}

	public static QueueProfile getQueueProfileByUuid(UUID uuid) {
		for (Queue queue : queues) {
            for (QueueProfile queueProfile : queue.getPlayers()) {
                if (queueProfile.getPlayerUuid().equals(uuid)) {
                    return queueProfile;
                }
            }
        }

		return null;
	}

	public static void init() {
		new QueueThread().start();
		TaskUtil.runTimerAsync(() -> {
			int queue = 0, match = 0, ranked = 0, unranked = 0;

			for (Queue queue1 : queues) {
				for (QueueProfile ignored : queue1.getPlayers()) {
					queue++;
				}
			}
			Yang.get().inQueues = queue;

			for (Match match1 : Match.getMatches()) {
				for (GameParticipant<MatchGamePlayer> participant : match1.getParticipants()) {
					for (MatchGamePlayer player : participant.getPlayers()) {
						if (match1.getKit().getGameRules().isRanked()) ranked++;
						else unranked++;
						match++;
					}
				}
			}
			Yang.get().inFightsTotal = match;
			Yang.get().inFightsUnRanked = unranked;
			Yang.get().inFightsRanked = ranked;
		}, 2L, 2L);
	}
}
