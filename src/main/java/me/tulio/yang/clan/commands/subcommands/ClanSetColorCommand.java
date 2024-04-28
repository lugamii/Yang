package me.tulio.yang.clan.commands.subcommands;

import me.tulio.yang.Locale;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.nametags.NameTag;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.TaskUtil;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.chat.StyleUtil;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClanSetColorCommand extends BaseCommand {

    @Command(name = "clan.setcolor")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please insert color.");
            return;
        }

        String color = args[0];
        Profile profile = Profile.get(player.getUniqueId());
        Clan clan = profile.getClan();
        if (clan == null) {
            new MessageFormat(Locale.CLAN_ERROR_PLAYER_NOT_FOUND
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        if (!player.getUniqueId().equals(profile.getClan().getLeader())) {
            new MessageFormat(Locale.CLAN_ERROR_ONLY_OWNER
                    .format(profile.getLocale()))
                    .send(player);
            return;
        }

        ChatColor chatColor = CC.getByName(color);
        if (chatColor == null || !chatColor.isColor()) {
            player.sendMessage(CC.RED + "Invalid color.");
            return;
        }

        clan.setColor(chatColor);
        clan.save();
        clan.broadcast(Locale.CLAN_SET_COLOR_BROADCAST, new MessageFormat()
                .add("{new_color}", StyleUtil.colorName(ChatColor.valueOf(color.toUpperCase())))
                .add("{color}", color.toUpperCase()));
        TaskUtil.runAsync(() -> {
            NameTag.reloadOthersFor(player);
            NameTag.reloadPlayer(player);
        });
    }
}