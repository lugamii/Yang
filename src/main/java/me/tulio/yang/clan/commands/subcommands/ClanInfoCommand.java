package me.tulio.yang.clan.commands.subcommands;

import me.tulio.yang.clan.Clan;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ClanInfoCommand extends BaseCommand {

    @Command(name = "clan.info", aliases = {"clan.show", "clan.i"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            Profile profile = Profile.get(player.getUniqueId());

            if (profile.getClan() != null) profile.getClan().show(player);
            else player.sendMessage(ChatColor.GRAY + "/clan info (name)");
            return;
        }

        Clan clan = Clan.getByName(args[0]);
        clan.show(player);
    }
}
