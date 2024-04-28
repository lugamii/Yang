package me.tulio.yang.kit.command;

import me.tulio.yang.kit.Kit;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitSetIconCommand extends BaseCommand {

    @Command(name = "kit.seticon", permission = "yang.kit.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please usage: /kit seticon (kit)");
            return;
        }

        Kit kit = Kit.getByName(args[0]);
        if (kit == null) {
            player.sendMessage(CC.RED + "This kit doesn't exist.");
            return;
        }

        kit.setDisplayIcon(player.getItemInHand());
        kit.save();
        player.sendMessage(ChatColor.GREEN + "Kit icon update");
    }
}