package me.tulio.yang.profile.command;

import me.tulio.yang.Locale;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.entity.Player;

public class FlyCommand extends BaseCommand {

	@Command(name = "fly", permission = "yang.command.fly")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());

		if (profile.getState() == ProfileState.LOBBY || profile.getState() == ProfileState.QUEUEING) {
			if (player.getAllowFlight()) {
				player.setAllowFlight(false);
				player.setFlying(false);
				player.updateInventory();
				new MessageFormat(Locale.ESSENTIALS_FLY_COMMAND_DEACTIVATE
						.format(profile.getLocale()))
						.send(player);
			} else {
				player.setAllowFlight(true);
				player.setFlying(true);
				player.updateInventory();
				new MessageFormat(Locale.ESSENTIALS_FLY_COMMAND_ACTIVATE
						.format(profile.getLocale()))
						.send(player);
			}
		} else {
			new MessageFormat(Locale.ESSENTIALS_FLY_COMMAND_CANNOT_FLY
					.format(profile.getLocale()))
					.send(player);
		}
	}
}
