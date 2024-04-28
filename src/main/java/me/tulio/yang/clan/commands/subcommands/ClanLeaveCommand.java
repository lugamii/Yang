package me.tulio.yang.clan.commands.subcommands;

import me.tulio.yang.Locale;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.TaskUtil;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.entity.Player;

public class ClanLeaveCommand extends BaseCommand {

    @Command(name = "clan.leave")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        Profile profile = Profile.get(player.getUniqueId());
        Clan clan = profile.getClan();
        if (clan == null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_FOUND
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        if (player.getUniqueId().equals(clan.getLeader())) {
            new MessageFormat(Locale.CLAN_ERROR_OWNER_LEAVE
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        profile.setClan(null);
        clan.getMembers().remove(player.getUniqueId());
        new MessageFormat(Locale.CLAN_LEAVE_PLAYER
                .format(profile.getLocale()))
                .send(player);
        for (Player onPlayer : clan.getOnPlayers()) {
            new MessageFormat(Locale.CLAN_LEAVE_BROADCAST.format(Profile.get(onPlayer.getUniqueId()).getLocale()))
                    .add("{player_name}", player.getName())
                    .send(onPlayer);
        }

        TaskUtil.runAsync(profile::save);
    }
}