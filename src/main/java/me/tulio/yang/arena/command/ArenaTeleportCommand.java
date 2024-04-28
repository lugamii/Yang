package me.tulio.yang.arena.command;

import me.tulio.yang.arena.Arena;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class ArenaTeleportCommand extends BaseCommand {

    @Command(name = "arena.teleport", permission = "yang.arena.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        Arena arena = Arena.getByName(args[0]);
        if (arena == null) {
            player.sendMessage(CC.RED + "An arena with that name does not exist.");
            return;
        }

        player.teleport(arena.getSpawnA());
        player.sendMessage(CC.translate("&aYou have been teleported to Arena " + arena.getName()));
    }
}