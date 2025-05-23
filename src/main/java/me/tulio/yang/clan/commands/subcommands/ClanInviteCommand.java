package me.tulio.yang.clan.commands.subcommands;

import me.tulio.yang.Locale;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.clan.ClanInvite;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.chat.Clickable;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClanInviteCommand extends BaseCommand {

    @Command(name = "clan.invite")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "A player with that name could not be found.");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        if (player.getUniqueId().equals(target.getUniqueId())) {
            player.sendMessage(CC.RED + "You cannot invite yourself.");
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        Profile profileTarget = Profile.get(target.getUniqueId());
        if (profile.getClan() == null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_FOUND
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        Clan clan = profile.getClan();
        if (clan.getMembers().size() == 15) {
            new MessageFormat(Locale.CLAN_ERROR_MEMBERS_LIMIT
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        if (profileTarget.getClan() != null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_ALREADY_IN_CLAN_OTHER.format(profile.getLocale()))
                    .add("{target_name}", target.getName())
                    .send(player);
            return;
        }

        if (!clan.getLeader().equals(player.getUniqueId())) {
            new MessageFormat(Locale.CLAN_ERROR_ONLY_OWNER
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        profileTarget.getInvites().put(profile.getClan().getName(), new ClanInvite(player.getUniqueId(), target.getUniqueId()));
        new MessageFormat(Locale.CLAN_INVITE_SENDER.format(profile.getLocale()))
                .add("{target_name}", target.getName())
                .send(player);
        Clickable clickable = new Clickable();
        clickable.add(new MessageFormat(Locale.CLAN_INVITE_RECEIVER.format(profile.getLocale()))
                .add("{clan_name}", profile.getClan().getColoredName())
                .toString());
        clickable.add(" &6C&6l&6i&6c&6k &6h&6e&6r&6e to enter", CC.translate("&bClick to enter"), "/clan join " +  profile.getClan().getName());
        clickable.sendToPlayer(target);
    }
}