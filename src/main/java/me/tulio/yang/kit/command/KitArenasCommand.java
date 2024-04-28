package me.tulio.yang.kit.command;

import me.tulio.yang.arena.Arena;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class KitArenasCommand extends BaseCommand {

	@Command(name = "kit.arenas", permission = "yang.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /kit arenas (kit)");
			return;
		}

		Kit kit = Kit.getByName(args[0]);
		if (kit == null) {
			player.sendMessage(CC.RED + "A kit with that name does not exist.");
			return;
		}
		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate("&6&lArenas with this Kit &7(&a" + kit.getName() + "&7)"));
		player.sendMessage("");
		for (Arena arena : Arena.getArenas()) {
			if (arena.getKits().contains(kit.getName())) {
				player.sendMessage(CC.translate("&7- &a" + arena.getName()));
			}
		}
		player.sendMessage(CC.CHAT_BAR);
	}
}
