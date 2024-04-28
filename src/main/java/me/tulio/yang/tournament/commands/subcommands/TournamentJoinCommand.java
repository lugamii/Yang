package me.tulio.yang.tournament.commands.subcommands;

import me.tulio.yang.Locale;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.party.Party;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.tournament.Tournament;
import me.tulio.yang.tournament.impl.TournamentClans;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.entity.Player;

public class TournamentJoinCommand extends BaseCommand {

    @Command(name = "tournament.join")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Tournament tournament = Tournament.getTournament();

        Profile profile = Profile.get(player.getUniqueId());
        if (tournament == null) {
            new MessageFormat(Locale.TOURNAMENT_NO_FOUND
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }
        if (profile.isInTournament()) {
            new MessageFormat(Locale.TOURNAMENT_ALREADY_IN_TOURNAMENT
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }
        if (tournament.isClans()) {
            Clan clan = profile.getClan();
            if (clan == null) {
                new MessageFormat(Locale.TOURNAMENT_ONLY_CLAN_JOIN
                        .format(profile.getLocale()))
                        .send(player);
                return;
            }

            if (!clan.getLeader().equals(player.getUniqueId())) {
                new MessageFormat(Locale.TOURNAMENT_ONLY_CLAN_OWNER_CAN_JOIN
                        .format(profile.getLocale()))
                        .send(player);
                return;
            }

            if (clan.getOnPlayers().size() != tournament.getLimit()) {
                new MessageFormat(Locale.TOURNAMENT_MINIMUM_PLAYERS_TO_JOIN
                        .format(profile.getLocale()))
                        .add("{size}", String.valueOf(tournament.getSize()))
                        .send(player);
                return;
            }

            if (((TournamentClans)tournament).getClans().size() == tournament.getLimit()) {
                new MessageFormat(Locale.TOURNAMENT_IS_FULL
                        .format(profile.getLocale()))
                        .send(player);
                return;
            }

            tournament.join(clan);
            return;
        }

        if (tournament.getSize() > 1) {
            Party party = profile.getParty();
            if (party == null) {
                new MessageFormat(Locale.TOURNAMENT_NEED_A_PARTY_TO_JOIN
                        .format(profile.getLocale()))
                        .send(player);
                return;
            }

            if (party.getLeader() != player) {
                new MessageFormat(Locale.TOURNAMENT_ONLY_PARTY_OWNER_CAN_JOIN
                        .format(profile.getLocale()))
                        .send(player);
                return;
            }

            if (party.getPlayers().size() != tournament.getLimit()) {
                new MessageFormat(Locale.TOURNAMENT_MINIMUM_PLAYERS_TO_JOIN
                        .format(profile.getLocale()))
                        .add("{size}", String.valueOf(tournament.getSize()))
                        .send(player);
                return;
            }

            if (tournament.getTeams().size() == tournament.getLimit()) {
                new MessageFormat(Locale.TOURNAMENT_IS_FULL
                        .format(profile.getLocale()))
                        .send(player);
                return;
            }
            tournament.join(party);
            return;
        }

        if (tournament.getPlayers().size() == tournament.getLimit()) {
            System.out.println(tournament.getPlayers().size() + " " + tournament.getSize());
            new MessageFormat(Locale.TOURNAMENT_IS_FULL
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }
        tournament.join(player);
    }
}