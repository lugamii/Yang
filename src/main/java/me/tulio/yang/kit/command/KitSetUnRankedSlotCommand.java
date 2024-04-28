package me.tulio.yang.kit.command;

import me.tulio.yang.kit.Kit;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

public class KitSetUnRankedSlotCommand extends BaseCommand {

	@Command(name = "kit.setunrankedslot", permission = "yang.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit setunrankedslot (kit) (slot)");
			return;
		}

		Kit kit = Kit.getByName(args[0]);
		if (kit == null) {
			player.sendMessage(CC.RED + "A kit with that name does not exist.");
			return;
		}

		int slot;
		if (!StringUtils.isNumeric(args[1])) {
			player.sendMessage(CC.RED + "Please usage a valid slot.");
			return;
		}
		slot = Integer.parseInt(args[1]);

		if (slot > 44) {
			player.sendMessage(CC.RED + "You have exceeded the maximum");
			return;
		}

		kit.setUnrankedSlot(slot);
		player.sendMessage(CC.RED + "Slot changed to " + kit.getName() + " successfully to " + slot);
	}
}
