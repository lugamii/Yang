package me.tulio.yang.essentials.command;

import me.tulio.yang.utilities.LocationUtil;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class LocationCommand extends BaseCommand {

	@Command(name = "location", aliases = {"loc"}, permission = "yang.command.loc")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(LocationUtil.serialize(player.getLocation()));
	}
}
