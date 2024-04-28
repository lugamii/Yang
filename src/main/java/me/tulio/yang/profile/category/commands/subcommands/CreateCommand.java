package me.tulio.yang.profile.category.commands.subcommands;

import me.tulio.yang.profile.category.Category;
import me.tulio.yang.profile.category.data.CategoryEditorData;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class CreateCommand extends BaseCommand {

    @Command(name = "category.create", permission = "yang.command.category")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&cUsage: /category create <name>"));
            return;
        }

        String name = args[0];
        if (Category.getByName(name) != null) {
            player.sendMessage(CC.translate("&cCategory with name &6" + name + "&c already exists!"));
            return;
        }
        Category category = new Category(name);
        if (name.length() < 3) {
            player.sendMessage(CC.translate("&cName must be at least 3 characters long."));
        }
        else if (name.length() > 16) {
            player.sendMessage(CC.translate("&cName must be at most 16 characters long."));
        }
        else {
            Category.getCategoryEditor().add(new CategoryEditorData(player.getUniqueId(), category, "displayname"));
            player.sendMessage(CC.translate("&cNow, insert the DisplayName in the chat (Remember you can use the characters for colors)"));
        }
    }
}
