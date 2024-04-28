package me.tulio.yang.kit.command;

import me.tulio.yang.kit.menu.KitMainEditorMenu;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitEditorCommand extends BaseCommand {

    @Command(name = "kit.editor", aliases = {"kit.editormenu"}, permission = "yang.command.kit.editor")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        new KitMainEditorMenu().openMenu(player);
    }
}
