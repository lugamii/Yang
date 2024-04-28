package me.tulio.yang.tournament;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.Locale;
import me.tulio.yang.Yang;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.match.Match;
import me.tulio.yang.match.participant.MatchGamePlayer;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.match.participant.GameParticipant;
import me.tulio.yang.tournament.impl.TournamentSolo;
import me.tulio.yang.tournament.impl.TournamentTeams;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.countdown.Countdown;
import me.tulio.yang.utilities.file.type.BasicConfigurationFile;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static me.tulio.yang.tournament.TournamentState.ENDED;
import static me.tulio.yang.tournament.TournamentState.IN_FIGHT;

@Getter @Setter
public abstract class Tournament<T>{

    @Getter @Setter private static Tournament<?> tournament;

    private boolean started = false;
    private TournamentState state = TournamentState.STARTING;
    private final List<UUID> players = Lists.newArrayList();
    private int size, limit;
    private List<GameParticipant<MatchGamePlayer>> teams = Lists.newArrayList();
    private Kit kit;
    private final List<Match> matches = Lists.newArrayList();
    private boolean clans;
    private int round = 0;
    private GameParticipant<MatchGamePlayer> winner;
    public Countdown countdown;

    public abstract void join(T type);

    public abstract void start();

    public abstract void nextRound();

    public abstract void eliminatedTeam(GameParticipant<MatchGamePlayer> teamEliminated);

    public abstract void end(GameParticipant<MatchGamePlayer> winner);

    public void broadcast(String msg){
        for (GameParticipant<MatchGamePlayer> team : teams) {
            team.getPlayers().forEach(matchGamePlayer -> matchGamePlayer.getPlayer().sendMessage(CC.translate(msg)));
        }
    }

    public void broadcast(Locale locale, HashMap<String, String> variables){
        for (GameParticipant<MatchGamePlayer> team : teams) {
            for (MatchGamePlayer player : team.getPlayers()) {
                MessageFormat messageFormat = new MessageFormat(locale.format(Profile.get(player.getUuid()).getLocale()));
                messageFormat.setVariables(variables);
                messageFormat.send(player.getPlayer());
            }
        }
    }

    public List<Player> getOnlinePlayers(){
        List<Player> list = new ArrayList<>();
        for (UUID player : players) {
            Player player1 = Bukkit.getPlayer(player);
            if (player1 != null) {
                list.add(player1);
            }
        }
        return list;
    }

    public GameParticipant<MatchGamePlayer> getParticipant(Player player) {
        for (GameParticipant<MatchGamePlayer> team : teams) {
            for (MatchGamePlayer teamPlayer : team.getPlayers()) {
                if (teamPlayer.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                    return team;
                }
            }
        }
        return null;
    }

    public List<String> getTournamentScoreboard() {
        List<String> lines = Lists.newArrayList();
        BasicConfigurationFile config = Yang.get().getScoreboardConfig();
        String mode;
        if (tournament instanceof TournamentSolo) mode = "Solo";
        else if (tournament instanceof TournamentTeams) mode = "Team";
        else mode = "Clan";

        for (String s : config.getStringList("BOARD.TOURNAMENT.LINES")) {
            if (s.contains("{in-fight}")) {
                if (this.getState() == IN_FIGHT) {
                    for (String line : config.getStringList("BOARD.TOURNAMENT.IN-FIGHT")) {
                        lines.add(line.replace("{round}", String.valueOf(getRound())));
                    }
                }
                continue;
            }
            if (s.contains("{end}")) {
                if (this.getState() == ENDED && getWinner() != null) {
                    MatchGamePlayer leader = getWinner().getLeader();
                    for (String line : config.getStringList("BOARD.TOURNAMENT.END")) {
                        lines.add(line.replace("{color}", Profile.get(leader.getPlayer().getUniqueId()).getColor())
                                .replace("{player}", leader.getPlayer().getName()));
                    }
                }
                continue;
            }
            lines.add(s.replace("{kit}", getKit().getName())
                    .replace("{size}", String.valueOf(getTeams().size()))
                    .replace("{limit}", String.valueOf(getLimit()))
                    .replace("{state}", getState().getName())
                    .replace("{mode}", mode));
        }
        return lines;
    }
}