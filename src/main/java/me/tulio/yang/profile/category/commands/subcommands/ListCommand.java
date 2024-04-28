package me.tulio.yang.profile.category.commands.subcommands;

import me.tulio.yang.profile.category.Category;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class ListCommand extends BaseCommand {

    @Command(name = "category.list", permission = "yang.command.category")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate(CC.CHAT_BAR));
        player.sendMessage(CC.translate("&5&lCategory List"));
        player.sendMessage("");
        for (Category category : Category.getCategories()) {
            player.sendMessage(CC.translate("&7- " + category.getDisplayName() + " &7(" + category.getName() + ")" + " &7(ELO: &f" + category.getElo() + ")"));
        }
        player.sendMessage(CC.translate(CC.CHAT_BAR));
    }
}
