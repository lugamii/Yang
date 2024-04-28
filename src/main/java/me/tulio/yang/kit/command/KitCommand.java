package me.tulio.yang.kit.command;

import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitCommand extends BaseCommand {

    public KitCommand() {
        super();
        new KitCreateCommand();
        new KitSetRuleCommand();
        new KitGetLoadoutCommand();
        new KitSetLoadoutCommand();
        new KitSetIconCommand();
        new KitToggleComand();
        new KitDeleteCommand();
        new KitStatusCommand();
        new KitRulesCommand();
        new KitSetRankedSlotCommand();
        new KitSetUnRankedSlotCommand();
        new KitRenameCommand();
        new KitEditorCommand();
        new KitArenasCommand();
    }

    @Command(name = "kit", permission = "yang.kit.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&6&lKits Help"));
        player.sendMessage(CC.translate("&7/kit create (kit_name)"));
        player.sendMessage(CC.translate("&7/kit delete (kit_name)"));
        player.sendMessage(CC.translate("&7/kit editor"));
        player.sendMessage(CC.translate("&7/kit seticon (kit_name)"));
        player.sendMessage(CC.translate("&7/kit setloadout (kit_name)"));
        player.sendMessage(CC.translate("&7/kit getloadout (kit_name)"));
        player.sendMessage(CC.translate("&7/kit toggle (kit_name)"));
        player.sendMessage(CC.translate("&7/kit setrule (kit_name) (rule) (value)"));
        player.sendMessage(CC.translate("&7/kit rules"));
        player.sendMessage(CC.translate("&7/kit setrankedslot (kit_name) (slot)"));
        player.sendMessage(CC.translate("&7/kit setunrankedslot (kit_name) (slot)"));
        player.sendMessage(CC.translate("&7/kit rename (kit_name) (newName)"));
        player.sendMessage(CC.translate("&7/kit status (kit_name)"));
        player.sendMessage(CC.translate("&7/kits"));
        player.sendMessage(CC.CHAT_BAR);
    }
}
