package me.tulio.yang.arena.command;

import me.tulio.yang.arena.Arena;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ArenaSaveCommand extends BaseCommand {

	@Command(name = "arena.save", permission = "yang.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		for (Arena arena : Arena.getArenas()) {
			arena.save();
		}
		player.sendMessage(ChatColor.GREEN + "Saved all arenas!");
	}

}
