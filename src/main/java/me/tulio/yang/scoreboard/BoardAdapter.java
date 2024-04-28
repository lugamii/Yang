package me.tulio.yang.scoreboard;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.tulio.yang.Yang;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.event.game.EventGame;
import me.tulio.yang.party.Party;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.profile.modmode.ModMode;
import me.tulio.yang.queue.QueueProfile;
import me.tulio.yang.scoreboard.impl.Assemble;
import me.tulio.yang.scoreboard.impl.AssembleAdapter;
import me.tulio.yang.tournament.Tournament;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.elo.EloUtil;
import me.tulio.yang.utilities.file.type.BasicConfigurationFile;
import me.tulio.yang.utilities.string.Animation;
import me.tulio.yang.utilities.string.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public class BoardAdapter implements AssembleAdapter {

	private final BasicConfigurationFile config = Yang.get().getScoreboardConfig();
	@Getter private final static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
	@Getter public static String date;

	@Override
	public String getTitle(Player player) {
		return CC.translate(Animation.getScoreboardTitle());
	}

	@Override
	public List<String> getLines(Player player) {
		Profile profile = Profile.get(player.getUniqueId());
		if (!profile.getOptions().showScoreboard()) return Lists.newArrayList();
		List<String> lines = Lists.newArrayList();

		if (profile.getState() == ProfileState.LOBBY) {
			for (String s : config.getStringList("BOARD.LOBBY")) {
				if (s.contains("{tournament}")) {
					if (Tournament.getTournament() != null) {
						lines.addAll(Tournament.getTournament().getTournamentScoreboard());
					}
					continue;
				}
				if (s.contains("{party}")) {
					if (profile.getParty() != null && Tournament.getTournament() == null) {
						Party party = profile.getParty();
						for (String s1 : config.getStringList("BOARD.PARTY")) {
							lines.add(s1
									.replace("{hoster}", party.getLeader().getName())
									.replace("{players}", String.valueOf(party.getPlayers().size())));
						}
					}
					continue;
				}
				if (s.contains("{clan}")) {
					if (profile.getClan() != null && profile.getParty() == null && Tournament.getTournament() == null) {
						Clan clan = profile.getClan();
						for (String s1 : config.getStringList("BOARD.CLAN")) {
							lines.add(s1
									.replace("{clan}", clan.getName())
									.replace("{color}", clan.getColor().toString())
									.replace("{size}", String.valueOf(clan.getMembers().size())));
						}
					}
					continue;
				}
				if (s.contains("{following}")) {
					if (profile.getFollow() != null) {
						lines.addAll(profile.getFollow().getScoreboardLines());
					}
					continue;
				}
				lines.add(s
						.replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
						.replace("{in-fights}", String.valueOf(Yang.get().getInFightsTotal()))
						.replace("{in-fights-ranked}", String.valueOf(Yang.get().getInFightsRanked()))
						.replace("{in-fights-unranked}", String.valueOf(Yang.get().getInFightsUnRanked()))
						.replace("{in-queue}", String.valueOf(Yang.get().getInQueues()))
						.replace("{elo}", String.valueOf(EloUtil.getGlobalElo(profile)))
						.replace("%localtime_time%", date));
			}
		}
		else if (profile.getState() == ProfileState.QUEUEING) {
			QueueProfile queueProfile = profile.getQueueProfile();

			for (String s : config.getStringList("BOARD.QUEUE.LINES")) {
				if (s.contains("{ranked}")) {
					if (queueProfile.getQueue().isRanked()) {
						for (String s1 : config.getStringList("BOARD.QUEUE.RANKED")) {
							lines.add(s1
									.replace("{min-range}", String.valueOf(queueProfile.getMinRange()))
									.replace("{max-range}", String.valueOf(queueProfile.getMaxRange()))
									.replace("{elapsed}", TimeUtil.millisToTimer(queueProfile.getPassed()))
									.replace("{queue}", queueProfile.getQueue().getQueueName())
									.replace("%localtime_time%", date));
						}
					}
					continue;
				}
				lines.add(s
						.replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
						.replace("{in-fights}", String.valueOf(Yang.get().getInFightsTotal()))
						.replace("{in-fights-ranked}", String.valueOf(Yang.get().getInFightsRanked()))
						.replace("{in-fights-unranked}", String.valueOf(Yang.get().getInFightsUnRanked()))
						.replace("{in-queue}", String.valueOf(Yang.get().getInQueues()))
						.replace("{elo}", String.valueOf(EloUtil.getGlobalElo(profile)))
						.replace("{elapsed}", TimeUtil.millisToTimer(queueProfile.getPassed()))
						.replace("{queue}", queueProfile.getQueue().getQueueName())
						.replace("%localtime_time%", date));
			}
		}
		else if (profile.getState() == ProfileState.STAFF_MODE) {
			lines.addAll(ModMode.getScoreboardLines(player));
		}
		else if (profile.getState() == ProfileState.EVENT) {
			if (EventGame.getActiveGame() != null) {
				lines.addAll(EventGame.getActiveGame().getGameLogic().getScoreboardEntries());
			}
		}
		else if (profile.getState() == ProfileState.FIGHTING) {
			lines.addAll(profile.getMatch().getScoreboardLines(player));
		}
		else if (profile.getState() == ProfileState.SPECTATING) {
			lines.addAll(profile.getMatch().getSpectatorScoreboardLines());
		}

		if (lines.contains("{footer}")) {
			lines.remove("{footer}");
			if (config.getBoolean("FOOTER_ENABLED")) {
				lines.add("");
				lines.add(Animation.getScoreboardFooter());
			}
		}
		if (config.getBoolean("BARS_ENABLED")) {
			String bars = config.getString("BARS_FORMAT");
			lines.add(0, bars);
			lines.add(bars);
		}

		return Yang.get().isPlaceholderAPI() ? PlaceholderAPI.setPlaceholders(player, lines) : lines;
	}

	public static void hook() {
		Yang.get().assemble = new Assemble(Yang.get(), new BoardAdapter());
		Yang.get().getAssemble().setTicks(2);
	}
}
