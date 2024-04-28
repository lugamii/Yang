package me.tulio.yang.event.game.map.command;

import me.tulio.yang.event.game.map.EventGameMap;
import me.tulio.yang.event.game.map.impl.SpreadEventGameMap;
import me.tulio.yang.event.game.map.impl.TeamEventGameMap;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.entity.Player;

public class EventMapStatusCommand extends BaseCommand {

	@Command(name = "event.map.status", permission = "yang.event.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /event map status (mapName)");
			return;
		}

		EventGameMap gameMap = EventGameMap.getByName(args[0]);
		if (gameMap == null) {
			player.sendMessage(CC.RED + "An event map with that name does not exist.");
		} else {
			player.sendMessage(CC.GOLD + CC.BOLD + "Event Map Status " + CC.GRAY + "(" +
					(gameMap.isSetup() ? CC.GREEN : CC.RED) + gameMap.getMapName() + CC.GRAY + ")");
			player.sendMessage(CC.GREEN + "Spectator Location: " + CC.YELLOW +
					(gameMap.getSpectatorPoint() == null ?
							StringEscapeUtils.unescapeJava("\u2717") :
							StringEscapeUtils.unescapeJava("\u2713")));

			if (gameMap instanceof TeamEventGameMap) {
				TeamEventGameMap teamGameMap = (TeamEventGameMap) gameMap;

				player.sendMessage(CC.GREEN + "Spawn A Location: " + CC.YELLOW +
						(teamGameMap.getSpawnPointA() == null ?
								StringEscapeUtils.unescapeJava("\u2717") :
								StringEscapeUtils.unescapeJava("\u2713")));

				player.sendMessage(CC.GREEN + "Spawn B Location: " + CC.YELLOW +
						(teamGameMap.getSpawnPointB() == null ?
								StringEscapeUtils.unescapeJava("\u2717") :
								StringEscapeUtils.unescapeJava("\u2713")));
			} else if (gameMap instanceof SpreadEventGameMap) {
				SpreadEventGameMap spreadGameMap = (SpreadEventGameMap) gameMap;

				player.sendMessage(CC.GREEN + "Spread Locations: " + CC.YELLOW +
						(spreadGameMap.getSpawnLocations().isEmpty() ?
								StringEscapeUtils.unescapeJava("\u2717") :
								StringEscapeUtils.unescapeJava("\u2713")));
			}
		}
	}
}
