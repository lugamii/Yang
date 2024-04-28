package me.tulio.yang.tournament.impl;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.Locale;
import me.tulio.yang.arena.Arena;
import me.tulio.yang.match.Match;
import me.tulio.yang.match.impl.BasicTeamMatch;
import me.tulio.yang.match.participant.MatchGamePlayer;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.match.participant.GameParticipant;
import me.tulio.yang.tournament.Tournament;
import me.tulio.yang.tournament.events.TournamentEndEvent;
import me.tulio.yang.utilities.TaskUtil;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.countdown.Countdown;
import me.tulio.yang.utilities.countdown.CountdownBuilder;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import static me.tulio.yang.tournament.TournamentState.*;

@Getter @Setter
public class TournamentSolo extends Tournament<Player> {

    @Override
    public void join(Player player){
        MatchGamePlayer playerA = new MatchGamePlayer(player.getUniqueId(), player.getName());
        getTeams().add(new GameParticipant<>(playerA));

        HashMap<String, String> variables = Maps.newHashMap();
        variables.put("{player}", player.getName());
        broadcast(Locale.TOURNAMENT_PLAYER_JOIN, variables);

        Profile.get(player.getPlayer().getUniqueId()).setInTournament(true);
        getPlayers().add(player.getUniqueId());

        if (getTeams().size() == getLimit()) {
            countdown = Countdown.of(15, TimeUnit.SECONDS)
                .players(getOnlinePlayers())
                .broadcastAt(15, TimeUnit.SECONDS)
                .broadcastAt(10, TimeUnit.SECONDS)
                .broadcastAt(5, TimeUnit.SECONDS)
                .broadcastAt(4, TimeUnit.SECONDS)
                .broadcastAt(3, TimeUnit.SECONDS)
                .broadcastAt(2, TimeUnit.SECONDS)
                .broadcastAt(1, TimeUnit.SECONDS)
                .withMessage(Locale.TOURNAMENT_START)
                .onFinish(this::start).start();
        }
    }

    public void start(){
        nextRound();
    }

    public void nextRound(){
        setState(SELECTING_DUELS);
        setRound(getRound() + 1);
        //Shuffle list to randomize
        Collections.shuffle(getTeams());
        //New team LinkedList to remove usedTeams
        LinkedList<GameParticipant<MatchGamePlayer>> teamsShuffle = new LinkedList<>(getTeams());
        //Count down
        Locale round = Locale.TOURNAMENT_NEXT_ROUND;
        if (getRound() == 1) round = Locale.TOURNAMENT_STARTING_MATCH;
        CountdownBuilder countdownBuilder  = Countdown.of(10, TimeUnit.SECONDS);
        countdownBuilder.players(getOnlinePlayers());
        countdownBuilder.broadcastAt(10, TimeUnit.SECONDS);
        countdownBuilder.broadcastAt(5, TimeUnit.SECONDS);
        countdownBuilder.broadcastAt(4, TimeUnit.SECONDS);
        countdownBuilder.broadcastAt(3, TimeUnit.SECONDS);
        countdownBuilder.broadcastAt(2, TimeUnit.SECONDS);
        countdownBuilder.broadcastAt(1, TimeUnit.SECONDS);
        countdownBuilder.withMessage(round);
        countdownBuilder.onFinish(() -> {
            setState(IN_FIGHT);
            //Logic to start match
            TaskUtil.runTimer(new BukkitRunnable() {
                @Override
                public void run() {
                    if (teamsShuffle.isEmpty()) {
                        cancel();
                        return;
                    }
                    GameParticipant<MatchGamePlayer> teamA = teamsShuffle.poll();
                    if (teamsShuffle.isEmpty()) {
                        end(teamA);
                        teamA.getPlayers().forEach(matchGamePlayer -> new MessageFormat(Locale.TOURNAMENT_NO_PLAYER_FOUND
                                .format(Profile.get(matchGamePlayer.getUuid()).getLocale()))
                                .add("{bars}", CC.CHAT_BAR)
                                .send(matchGamePlayer.getPlayer()));
                        return;
                    }
                    GameParticipant<MatchGamePlayer> teamB = teamsShuffle.poll();

                    Arena arena = Arena.getRandomArena(getKit());

                    if (arena == null) {
                        teamA.getPlayers().forEach(matchGamePlayer -> new MessageFormat(Locale.TOURNAMENT_TRY_START_WITHOUT_ARENAS
                                .format(Profile.get(matchGamePlayer.getUuid()).getLocale()))
                                .add("{bars}", CC.CHAT_BAR)
                                .send(matchGamePlayer.getPlayer()));
                        teamB.getPlayers().forEach(matchGamePlayer -> new MessageFormat(Locale.TOURNAMENT_TRY_START_WITHOUT_ARENAS
                                .format(Profile.get(matchGamePlayer.getUuid()).getLocale()))
                                .add("{bars}", CC.CHAT_BAR)
                                .send(matchGamePlayer.getPlayer()));
                        return;
                    }
                    arena.setBusy(true);
                    Match match = new BasicTeamMatch(null, getKit(), arena, false, teamA, teamB);
                    match.start();
                    getMatches().add(match);
                }
            }, 1L, 1L);
        });
        countdown = countdownBuilder.start();
    }

    public void eliminatedTeam(GameParticipant<MatchGamePlayer> teamEliminated){
        getTeams().remove(teamEliminated);
        for (GameParticipant<MatchGamePlayer> team : getTeams()) {
            for (MatchGamePlayer matchGamePlayer : team.getPlayers()) {
                Profile.get(matchGamePlayer.getPlayer().getUniqueId()).setInTournament(false);
                getPlayers().remove(matchGamePlayer.getUuid());
                Player player = matchGamePlayer.getPlayer();
                if (player != null) new MessageFormat(Locale.TOURNAMENT_ELIMINATE_PLAYER
                        .format(Profile.get(player.getUniqueId()).getLocale()))
                        .add("{bars}", CC.CHAT_BAR)
                        .add("{players}", teamEliminated.getConjoinedNames())
                        .send(player);
            }
        }
    }

    public void end(GameParticipant<MatchGamePlayer> winner){
        setState(ENDED);
        for (GameParticipant<MatchGamePlayer> team : getTeams()) {
            for (MatchGamePlayer matchGamePlayer : team.getPlayers()) {
                Profile.get(matchGamePlayer.getPlayer().getUniqueId()).setInTournament(false);
            }
        }
        setWinner(winner);
        getTeams().clear();
        getPlayers().clear();
        setStarted(false);
        if(winner != null){
            new TournamentEndEvent(winner, true, false).call();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                new MessageFormat(Locale.TOURNAMENT_WINNER_MESSAGE
                        .format(Profile.get(onlinePlayer.getUniqueId()).getLocale()))
                        .add("{bars}", CC.CHAT_BAR)
                        .add("{winner}", winner.getConjoinedNames())
                        .send(onlinePlayer);
            }
        }else {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                new MessageFormat(Locale.TOURNAMENT_STOP
                        .format(Profile.get(onlinePlayer.getUniqueId()).getLocale()))
                        .add("{bars}", CC.CHAT_BAR)
                        .send(onlinePlayer);
            }
        }
        setTournament(null);
    }
}