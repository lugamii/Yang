package me.tulio.yang.leaderboard.commands;

import me.tulio.yang.Locale;
import me.tulio.yang.Yang;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.find.ProfileFinder;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EloCommand extends BaseCommand {

    @Command(name = "elo", permission = "yang.command.elo")
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
            for (String s : Yang.get().getLangConfig().getStringList("ELO.VIEW_OTHER")) {
                if (s.contains("{format}")) {
                    for (Kit kit : Kit.getKits()) {
                        if (kit.isEnabled()) {
                            if (kit.getGameRules().isRanked()) {
                                player.sendMessage(CC.translate(Yang.get().getLangConfig().getString("ELO.VIEW_FORMAT")
                                        .replace("{kit}", kit.getName())
                                        .replace("{elo}", String.valueOf(target.getKitData().get(kit).getElo()))));
                            }
                        }
                    }
                    continue;
                }
                player.sendMessage(CC.translate(s
                        .replace("{bars}", CC.CHAT_BAR)
                        .replace("{color}", target.getColor())
                        .replace("{player}", target.getName())));
            }
            return;
        }

        for (String s : Yang.get().getLangConfig().getStringList("ELO.VIEW_YOUR")) {
            if (s.contains("{format}")) {
                for (Kit kit : Kit.getKits()) {
                    if (kit.isEnabled()) {
                        if (kit.getGameRules().isRanked()) {
                            player.sendMessage(CC.translate(Yang.get().getLangConfig().getString("ELO.VIEW_FORMAT")
                                    .replace("{kit}", kit.getName())
                                    .replace("{elo}", String.valueOf(Profile.get(player.getUniqueId()).getKitData().get(kit).getElo()))));
                        }
                    }
                }
                continue;
            }
            player.sendMessage(CC.translate(s.replace("{bars}", CC.CHAT_BAR)));
        }
    }
}
