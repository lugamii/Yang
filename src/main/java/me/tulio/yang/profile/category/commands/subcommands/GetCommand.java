package me.tulio.yang.profile.category.commands.subcommands;

import me.tulio.yang.Locale;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.find.ProfileFinder;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.entity.Player;

public class GetCommand extends BaseCommand {

    @Command(name = "category.get", permission = "yang.command.category")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUse /category get <player>"));
            return;
        }

        String playerName = args[0];

        if (ProfileFinder.findProfileByName(playerName) == null) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                        .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        Profile profile = ProfileFinder.findProfileByName(playerName);

        profile.updateCategory();

        player.sendMessage(CC.translate("&7" + profile.getName() + " has &r" + profile.getCategory().getDisplayName() + " &7category."));
    }
}
