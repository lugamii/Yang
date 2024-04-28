package me.tulio.yang.event.command;

import me.tulio.yang.event.Event;
import me.tulio.yang.event.game.map.EventGameMap;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class EventAddMapCommand extends BaseCommand {

	@Command(name = "event.addmap", permission = "yang.event.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0 || args.length == 1) {
			player.sendMessage(CC.RED + "Please usage: /event addmap (event) (map)");
			return;
		}

		Event event = Event.getByName(args[0]);
		if (event == null) {
			player.sendMessage(CC.RED + "An event type by that name does not exist.");
			player.sendMessage(CC.RED + "Types: sumo, spleef, tntrun, gulag, tnttag");
			return;
		}

		EventGameMap gameMap = EventGameMap.getByName(args[1]);
		if (gameMap == null) {
			player.sendMessage(CC.RED + "A map with that name does not exist.");
			return;
		}

		if (!event.getAllowedMaps().contains(gameMap.getMapName())) {
			event.getAllowedMaps().add(gameMap.getMapName());
			event.save();

			player.sendMessage(CC.GOLD + "You successfully added the \"" + CC.GREEN + gameMap.getMapName() +
					CC.GOLD + "\" map from the \"" + CC.GREEN + event.getName() + CC.GOLD +
					"\" event.");
		}
	}
}
