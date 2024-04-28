package me.tulio.yang.leaderboard.commands;

import me.tulio.yang.Locale;
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

public class ResetEloCommand extends BaseCommand {

    @Command(name = "resetelo", permission = "yang.command.resetelo")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please usage: /resetelo (player)");
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

        target.getKitData().forEach((kit, profileKitData) -> {
            profileKitData.setElo(1000);
        });
        target.updateCategory();
        if (!target.isOnline()) TaskUtil.runAsync(target::save);
        player.sendMessage(CC.translate("&a" + target.getName() + " reset elo successfully"));
    }
}
