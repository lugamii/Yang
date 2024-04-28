package me.tulio.yang.essentials.command;

import me.tulio.yang.Yang;
import me.tulio.yang.utilities.BukkitReflection;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public class SetSlotsCommand extends BaseCommand {

	@Command(name = "setslots", permission = "yang.command.setslots")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please insert a valid slot.");
			return;
		}

		int slots;
		if (!StringUtils.isNumeric(args[0])) {
			player.sendMessage(CC.RED + "Please insert a valid integer");
			return;
		}
		slots = Integer.getInteger(args[0]);

		BukkitReflection.setMaxPlayers(Yang.get().getServer(), slots);
		player.sendMessage(CC.GOLD + "You set the max slots to " + slots + ".");
	}
}
