package me.tulio.yang.arena.command;

import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.command.CommandSender;


public class ArenaCommand extends BaseCommand {

    public ArenaCommand() {
        super();
        new ArenaAddKitCommand();
        new ArenaCreateCommand();
        new ArenaDeleteCommand();
        new ArenaGenerateCommand();
        new ArenaGenHelperCommand();
        new ArenaRemoveKitCommand();
        new ArenaSaveCommand();
        new ArenaSelectionCommand();
        new ArenaSetSpawnCommand();
        new ArenaStatusCommand();
        new ArenaSetAuthorCommand();
        new ArenaTeleportCommand();
    }

    @Command(name = "arena", permission = "yang.arena.admin", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs commandArgs) {
        CommandSender sender = commandArgs.getSender();

        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.translate("&6&lArena Help"));
        sender.sendMessage(CC.translate("&7/arena addkit (arena_name) (kit_name)"));
        sender.sendMessage(CC.translate("&7/arena removekit (arena_name) (kit_name)"));
        sender.sendMessage(CC.translate("&7/arena create (arena_name) (SHARED/STANDALONE)"));
        sender.sendMessage(CC.translate("&7/arena delete (arena_name)"));
        sender.sendMessage(CC.translate("&7/arena generate (arena_name)"));
        sender.sendMessage(CC.translate("&7/arena genhelper"));
        sender.sendMessage(CC.translate("&7/arena save"));
        sender.sendMessage(CC.translate("&7/arena wand"));
        sender.sendMessage(CC.translate("&7/arena setspawn (arena_name) (a/b/[red/blue])"));
        sender.sendMessage(CC.translate("&7/arena status (arena_name)"));
        sender.sendMessage(CC.translate("&7/arena setauthor (arena_name) (author_name)"));
        sender.sendMessage(CC.translate("&7/arena teleport (arena_name)"));
        sender.sendMessage(CC.translate("&7/arenas"));
        sender.sendMessage(CC.CHAT_BAR);
    }
}
