package me.tulio.yang.profile.category.commands.subcommands;

import me.tulio.yang.profile.category.Category;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class RemoveCommand extends BaseCommand {

    @Command(name = "category.remove", permission = "yang.command.category")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&cUsage: /category remove <category>"));
            return;
        }

        String category = args[0];
        if (Category.getByName(category) == null) {
            player.sendMessage(CC.translate("&cCategory &6" + category + " &cnot found."));
            return;
        }

        Category.getCategories().remove(Category.getByName(category));
        player.sendMessage(CC.translate("&aCategory &6" + category + " &aremoved."));
    }
}
