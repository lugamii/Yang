package me.tulio.yang.arena.command;

import me.tulio.yang.arena.Arena;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArenaAddKitCommand extends BaseCommand {

	@Command(name = "arena.addkit", permission = "yang.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		Arena arena = Arena.getByName(args[0]);
		if (arena == null) {
			player.sendMessage(ChatColor.RED + "An arena with that name does not exist.");
			return;
		}

		Kit kit = Kit.getByName(args[1]);
		if (kit == null) {
			player.sendMessage(ChatColor.RED + "A kit with that name does not exist.");
			return;
		}

		arena.getKits().add(kit.getName());
		arena.save();

		player.sendMessage(ChatColor.GOLD + "Added kit \"" + kit.getName() +
				"\" to arena \"" + arena.getName() + "\"");
	}

}
