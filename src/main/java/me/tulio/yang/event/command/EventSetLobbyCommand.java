package me.tulio.yang.event.command;

import me.tulio.yang.event.Event;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EventSetLobbyCommand extends BaseCommand {

	@Command(name = "event.setlobby", permission = "yang.event.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /event setlobby (event)");
			return;
		}

		Event event = Event.getByName(args[0]);
		if (event == null) {
			player.sendMessage(CC.RED + "An event with that name does not exist.");
			return;
		}

		event.setLobbyLocation(player.getLocation());
		event.save();

		player.sendMessage(ChatColor.GOLD + "You updated the " + ChatColor.GREEN + event.getName() +
				ChatColor.GOLD + " Event's lobby location.");
	}
}
