package me.tulio.yang.leaderboard.commands;

import me.tulio.yang.Locale;
import me.tulio.yang.leaderboard.menu.StatisticsMenu;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.find.ProfileFinder;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StatsCommand extends BaseCommand {

    @Command(name = "stats")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length > 0) {
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
            new StatisticsMenu(target).openMenu(player);
            return;
        }

        new StatisticsMenu(Profile.get(player.getUniqueId())).openMenu(player);
    }
}