package me.tulio.yang.clan.commands.subcommands;

import me.tulio.yang.Locale;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.entity.Player;

public class ClanDisbandCommand extends BaseCommand {

    @Command(name = "clan.disband", aliases = {"clan.remove", "clan.delete"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length != 0) {
            if (player.hasPermission("yang.clan.disband")) {
                Clan clan = Clan.getByName(args[0]);
                if (clan == null) {
                    player.sendMessage(CC.RED + "Please insert a valid Clan.");
                    return;
                }

                clan.disband(player);
            } else {
                player.sendMessage(CC.RED + "You do not have permissions to disband other clans.");
            }
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        if (profile.getClan() == null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_FOUND
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }
        else if (!profile.getClan().getLeader().equals(player.getUniqueId())) {
            player.sendMessage(CC.RED + "You do not leader to disband this clan");
            return;
        }
        profile.getClan().disband(player);
    }
}
