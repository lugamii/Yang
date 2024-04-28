package me.tulio.yang.tournament.commands.subcommands;

import com.google.common.collect.Maps;
import me.tulio.yang.Locale;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.tournament.Tournament;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.entity.Player;

public class TournamentForcestartCommand extends BaseCommand {

    @Command(name = "tournament.forcestart", permission = "yang.command.tournament.forcestart")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        if (Tournament.getTournament() == null) {
            new MessageFormat(Locale.TOURNAMENT_NO_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        if (Tournament.getTournament().getCountdown() != null) Tournament.getTournament().getCountdown().stop();
        Tournament.getTournament().broadcast(Locale.TOURNAMENT_FORCE_START, Maps.newHashMap());
        Tournament.getTournament().start();
    }
}
