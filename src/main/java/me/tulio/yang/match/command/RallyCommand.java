package me.tulio.yang.match.command;

import me.tulio.yang.Locale;
import me.tulio.yang.match.Match;
import me.tulio.yang.match.participant.GameParticipant;
import me.tulio.yang.match.participant.MatchGamePlayer;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.entity.Player;

public class RallyCommand extends BaseCommand {

    @Command(name = "rally")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        if (profile.getState() == ProfileState.FIGHTING) {
            Match match = profile.getMatch();

            if (match.getKit().getGameRules().isHcf()) {
                GameParticipant<MatchGamePlayer> participant = match.getParticipant(player);
                participant.setRally(player.getLocation());
                participant.sendRallyWaypoints();
                new MessageFormat(Locale.DUEL_RALLY_ACTIVATE_MESSAGE.format(profile.getLocale())).send(player);
            } else {
                new MessageFormat(Locale.DUEL_NOT_HCF_KIT.format(profile.getLocale())).send(player);
            }
        } else {
            new MessageFormat(Locale.DUEL_NOT_IN_MATCH.format(profile.getLocale())).send(player);
        }
    }
}
