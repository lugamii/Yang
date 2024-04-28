package me.tulio.yang.profile.meta.option.command;

import me.tulio.yang.Locale;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.entity.Player;

public class ToggleGlobalChatCommand extends BaseCommand {

    @Command(name = "toggleglobalchat", aliases = {"tgc", "togglepublicchat", "tpc"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());
        profile.getOptions().publicChatEnabled(!profile.getOptions().publicChatEnabled());

        if (profile.getOptions().publicChatEnabled()) {
            new MessageFormat(Locale.OPTIONS_GLOBAL_CHAT_ENABLED
                    .format(profile.getLocale()))
                    .send(player);
        } else {
            new MessageFormat(Locale.OPTIONS_GLOBAL_CHAT_DISABLED
                    .format(profile.getLocale()))
                    .send(player);
        }
    }
}
