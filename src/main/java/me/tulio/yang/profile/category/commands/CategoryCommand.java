package me.tulio.yang.profile.category.commands;

import me.tulio.yang.profile.category.commands.subcommands.*;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class CategoryCommand extends BaseCommand {

    public CategoryCommand() {
        super();
        new ListCommand();
        new CreateCommand();
        new RemoveCommand();
        new SetCommand();
        new GetCommand();
    }

    @Command(name = "category", permission = "yang.command.category")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        for (String s : Arrays.asList(
                CC.CHAT_BAR,
                "&5&lCategory Commands",
                "",
                "&7/category create <name> &7- &aAdd a new category",
                "&7/category remove <name> &7- &aRemove a category",
                "&7/category list &7- &aList all categories",
                "&7/category set <name> <type> <value> &7- &aSet a category value",
                "&7/category get <player> &7- &aGet a category of player",
                CC.CHAT_BAR
        )) {
            player.sendMessage(CC.translate(s));
        }
    }
}
