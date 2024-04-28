package me.tulio.yang.tournament.commands.subcommands;

import me.tulio.yang.Locale;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.tournament.Tournament;
import me.tulio.yang.tournament.TournamentState;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TournamentStopCommand extends BaseCommand {

    @Command(name = "tournament.stop", permission = "yang.command.tournament.stop")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Tournament<?> tournament = Tournament.getTournament();
        if (tournament == null || tournament.getState() == TournamentState.ENDED) {
            player.sendMessage(ChatColor.RED + "No tournament found.");
            new MessageFormat(Locale.TOURNAMENT_NO_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }
        if ((tournament.getState() == TournamentState.IN_FIGHT || tournament.getState() == TournamentState.SELECTING_DUELS)
                && tournament.getTeams().size() == 1) {
            tournament.end(tournament.getTeams().get(0));
            return;
        }
        tournament.end(null);
    }
}