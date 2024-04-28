package me.tulio.yang.profile.meta.option.command;

import me.tulio.yang.profile.meta.option.menu.ProfileOptionsMenu;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class OptionsCommand extends BaseCommand {

	@Command(name = "options", aliases = {"settings"})
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		new ProfileOptionsMenu().openMenu(player);
	}
}
