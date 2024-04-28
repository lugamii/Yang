package me.tulio.yang.essentials.command;

import me.tulio.yang.Yang;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RemoveWorldCommand extends BaseCommand {

    @Command(name = "removeworld", aliases = {"rw"}, permission = "yang.command.removeworld")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();
        String label = commandArgs.getLabel();
        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /" + label + " (world)"));
            return;
        }

        String world = args[0];
        if (!CreateWorldCommand.VOID_WORLDS.contains(world)) {
            player.sendMessage(CC.translate("&cThat world doesn't exist!"));
            return;
        }

        Bukkit.unloadWorld(world, false);
        CreateWorldCommand.VOID_WORLDS.remove(world);
        player.sendMessage(CC.translate("&aSuccessfully removed world &e" + world + "&a!"));

        Yang.get().getMainConfig().getConfiguration().set("WORLDS", CreateWorldCommand.VOID_WORLDS);
        Yang.get().getMainConfig().save();
    }
}
