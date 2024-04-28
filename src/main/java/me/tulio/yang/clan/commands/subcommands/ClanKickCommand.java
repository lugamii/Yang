package me.tulio.yang.clan.commands.subcommands;

import me.tulio.yang.Locale;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.TaskUtil;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.find.ProfileFinder;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClanKickCommand extends BaseCommand {

    @Command(name = "clan.kick")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please insert a Target.");
            return;
        }

        Profile target;
        if (Bukkit.getPlayer(args[0]) != null) {
            target = Profile.get(Bukkit.getPlayer(args[0]).getUniqueId());
        } else {
            target = ProfileFinder.findProfileByName(args[0]);
        }

        if (target == null) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        Clan clan = profile.getClan();
        if (clan == null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_FOUND
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        if(!clan.getLeader().equals(player.getUniqueId())){
            new MessageFormat(Locale.CLAN_ERROR_ONLY_OWNER
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        if(!profile.getClan().getMembers().contains(target.getUuid())){
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_IN_YOUR_CLAN.format(profile.getLocale()))
                    .add("{target_name}", target.getName())
                    .send(player);
            return;
        }

        target.setClan(null);

        clan.getMembers().remove(target.getUuid());

        if (!target.isOnline()) {
            new MessageFormat(Locale.CLAN_KICKED_PLAYER
                    .format(target.getLocale()))
                    .send(target.getPlayer());
        }

        for (Player onPlayer : clan.getOnPlayers()) {
            new MessageFormat(Locale.CLAN_KICKED_BROADCAST.format(Profile.get(onPlayer.getUniqueId()).getLocale()))
                    .add("{target_name}", target.getName())
                    .send(onPlayer);
        }

        TaskUtil.runAsync(target::save);
    }
}