package me.tulio.yang.clan.commands.subcommands;

import me.tulio.yang.clan.Clan;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public class ClanListCommand extends BaseCommand {

    @Command(name = "clan.list")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        player.sendMessage(CC.translate("&6&lList of all Clans"));
        for (Clan value : Clan.getClans().values()) {
            player.sendMessage(CC.translate("&7- &e" + StringUtils.capitalize(value.getName().toLowerCase())));
        }
    }
}
