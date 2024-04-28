package me.tulio.yang.kit.command;

import me.tulio.yang.kit.Kit;
import me.tulio.yang.queue.Queue;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitCreateCommand extends BaseCommand {

	@Command(name = "kit.create", permission = "yang.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit create (name)");
			return;
		}

		String kitName = args[0];
		if (Kit.getByName(kitName) != null) {
			player.sendMessage(CC.RED + "A kit with that name already exists.");
			return;
		}

		Kit kit = new Kit(kitName);
		kit.save();
		Kit.getKits().add(kit);
		Queue.getQueues().add(new Queue(kit, false));

		player.sendMessage(CC.GREEN + "You created a new kit.");
	}
}
