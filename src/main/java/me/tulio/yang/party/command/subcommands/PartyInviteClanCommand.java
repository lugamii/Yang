package me.tulio.yang.party.command.subcommands;

import me.tulio.yang.Locale;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.party.PartyPrivacy;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.entity.Player;

public class PartyInviteClanCommand extends BaseCommand {

    @Command(name = "party.inviteclan", aliases = {"p.inviteclan"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        if(profile.getClan() == null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_FOUND
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        if (profile.getParty() == null) {
            player.sendMessage(CC.RED + "You do not have a party.");
            return;
        }

        if (!profile.getParty().getLeader().equals(player)) {
            player.sendMessage(CC.RED + "You are not the leader of your party.");
            return;
        }

        if (profile.getParty().getPrivacy() == PartyPrivacy.OPEN) {
            player.sendMessage(CC.RED + "The party state is Open. You do not need to invite players.");
            return;
        }

        Clan clan = profile.getClan();

        for (Player target : clan.getOnPlayers()) {
            if (profile.getParty().getInvite(target.getUniqueId()) != null) {
                return;
            }

            if (profile.getParty().containsPlayer(target.getUniqueId())) {
                return;
            }

            Profile targetData = Profile.get(target.getUniqueId());

            if (targetData.isBusy()) {
                return;
            }

            profile.getParty().invite(target);
        }
        clan.getOnPlayers().forEach(target -> {

        });
    }
}