package me.tulio.yang.duel.command;

import me.tulio.yang.queue.menu.QueueSelectKitMenu;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class RankedCommand extends BaseCommand {

    @Command(name = "ranked")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new QueueSelectKitMenu(true).openMenu(player);
    }
}
