package me.tulio.yang.arena.command;

import me.tulio.yang.arena.Arena;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

public class ArenaStatusCommand extends BaseCommand {

	@Command(name = "arena.status", permission = "yang.arena.admin", inGameOnly = false)
	@Override
	public void onCommand(CommandArgs commandArgs) {
		CommandSender sender = commandArgs.getSender();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			sender.sendMessage(CC.RED + "Usage: /arena status <arena>");
			return;
		}

		Arena arena = Arena.getByName(args[0]);
		if (arena != null) {
			sender.sendMessage(CC.GOLD + CC.BOLD + "Arena Status " + CC.GRAY + "(" +
					(arena.isSetup() ? CC.GREEN : CC.RED) + arena.getName() + CC.GRAY + ")");

			sender.sendMessage(CC.GREEN + "Cuboid Lower Location: " + CC.YELLOW +
					(arena.getLowerCorner() == null ?
							StringEscapeUtils.unescapeJava("\u2717") :
							StringEscapeUtils.unescapeJava("\u2713")));

			sender.sendMessage(CC.GREEN + "Cuboid Upper Location: " + CC.YELLOW +
					(arena.getUpperCorner() == null ?
							StringEscapeUtils.unescapeJava("\u2717") :
							StringEscapeUtils.unescapeJava("\u2713")));

			sender.sendMessage(CC.GREEN + "Spawn A Location: " + CC.YELLOW +
					(arena.getSpawnA() == null ?
							StringEscapeUtils.unescapeJava("\u2717") :
							StringEscapeUtils.unescapeJava("\u2713")));

			sender.sendMessage(CC.GREEN + "Spawn B Location: " + CC.YELLOW +
					(arena.getSpawnB() == null ?
							StringEscapeUtils.unescapeJava("\u2717") :
							StringEscapeUtils.unescapeJava("\u2713")));

			sender.sendMessage(CC.GREEN + "Kits: " + CC.YELLOW + StringUtils.join(arena.getKits(), ", "));
		} else {
			sender.sendMessage(CC.RED + "An arena with that name does not exist.");
		}
	}
}
