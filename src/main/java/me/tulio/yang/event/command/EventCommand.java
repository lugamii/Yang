package me.tulio.yang.event.command;

import me.tulio.yang.Locale;
import me.tulio.yang.event.game.command.*;
import me.tulio.yang.event.game.map.command.EventMapCommand;
import me.tulio.yang.event.game.map.command.EventMapsCommand;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.entity.Player;

public class EventCommand extends BaseCommand {

	public EventCommand() {
		super();
		new EventAddMapCommand();
		new EventAdminCommand();
		new EventRemoveMapCommand();
		new EventSetLobbyCommand();
		new EventCancelCommand();
		new EventClearCooldownCommand();
		new EventForceStartCommand();
		new EventInfoCommand();
		new EventJoinCommand();
		new EventLeaveCommand();
		new EventMapCommand();
		new EventMapsCommand();
	}

	@Command(name = "event")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		new MessageFormat(Locale.EVENT_HELP.format(Profile.get(player.getUniqueId()).getLocale())).send(player);
	}
}
