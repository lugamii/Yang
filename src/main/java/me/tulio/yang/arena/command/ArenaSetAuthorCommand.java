package me.tulio.yang.arena.command;

import me.tulio.yang.arena.Arena;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class ArenaSetAuthorCommand extends BaseCommand {

    @Command(name = "arena.setauthor", permission = "yang.arena.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /arena setauthor (arena) (author)"));
            return;
        }

        Arena arena = null;
        for (Arena val : Arena.getArenas()) {
            if (val.getName().equalsIgnoreCase(args[0])) {
                arena = val;
                break;
            }
        }
        String author = args[1];

        if (arena == null) {
            player.sendMessage(CC.translate("&cPlease usage a valid arena name"));
            return;
        }

        arena.setAuthor(author);
        player.sendMessage(CC.translate("&aAuthor of &e" + arena.getName() + "&a has seen set to &e" + author + "&a."));
        arena.save();
    }
}