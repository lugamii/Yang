package me.tulio.yang.kit.command;

import me.tulio.yang.kit.Kit;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitRenameCommand extends BaseCommand {

    @Command(name = "kit.rename", description = "Rename a kit", permission = "yang.kit.rename")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.RED + "Please usage: /kit rename (kit) (newName)");
            return;
        }

        Kit kit = Kit.getByName(args[0]);
        if (kit == null) {
            player.sendMessage(CC.RED + "Kit not found!");
            return;
        }

        kit.rename(args[1]);
        player.sendMessage(CC.GREEN + "Kit " + args[0] + " renamed to " + args[1]);
    }
}
