package me.tulio.yang.clan.commands;

import me.tulio.yang.Locale;
import me.tulio.yang.clan.commands.subcommands.*;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.entity.Player;

public class ClanCommand extends BaseCommand {

    public ClanCommand() {
        new ClanCreateCommand();
        new ClanDisbandCommand();
        new ClanInfoCommand();
        new ClanListCommand();
        new ClanJoinCommand();
        new ClanInviteCommand();
        new ClanRenameCommand();
        new ClanKickCommand();
        new ClanSetColorCommand();
        new ClanLeaveCommand();
        new ClanSetPointsCommand();
        new ClanChatCommand();
    }

    @Command(name = "clan")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new MessageFormat(Locale.CLAN_HELP
                .format(Profile.get(player.getUniqueId()).getLocale()))
                .send(player);
    }
}
