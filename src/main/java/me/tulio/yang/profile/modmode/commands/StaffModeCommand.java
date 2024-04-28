package me.tulio.yang.profile.modmode.commands;

import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.profile.modmode.ModMode;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class StaffModeCommand extends BaseCommand {

    @Command(name = "staffmode", aliases = {"staff", "mod", "h"}, permission = "yang.command.staffmode")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        Profile profile = Profile.get(player.getUniqueId());

        if (profile.isBusy() && !ModMode.getStaffmode().contains(player.getUniqueId())) {
            player.sendMessage(CC.RED + "You cannot use this command while busy!");
            return;
        }

        if (profile.getState() == ProfileState.STAFF_MODE) ModMode.remove(player);
        else ModMode.add(player);
    }
}
