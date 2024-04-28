package me.tulio.yang.essentials.command;

import me.tulio.yang.Locale;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.entity.Player;

public class HealCommand extends BaseCommand {

	@Command(name = "heal", permission = "yang.command.heal")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.setHealth(20.0);
			player.setFoodLevel(20);
			player.setSaturation(5.0F);
			player.updateInventory();
			player.sendMessage(CC.GOLD + "You healed yourself.");
		} else {
			Player target = commandArgs.getPlayer();
			if (target == null) {
				new MessageFormat(Locale.PLAYER_NOT_FOUND
						.format(Profile.get(player.getUniqueId()).getLocale()))
						.send(player);
				return;
			}
			target.setHealth(20.0);
			target.setFoodLevel(20);
			target.setSaturation(5.0F);
			target.updateInventory();
			target.sendMessage(CC.GOLD + "You have been healed by " + player.getName());
		}
	}
}
