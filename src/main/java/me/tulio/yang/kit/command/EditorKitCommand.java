package me.tulio.yang.kit.command;

import me.tulio.yang.kit.menu.edit.KitEditorSelectKitMenu;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class EditorKitCommand extends BaseCommand {

    @Command(name = "editorkit")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        if (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING) {
            new KitEditorSelectKitMenu().openMenu(player);
        }
    }
}
