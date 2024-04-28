package me.tulio.yang.essentials.command;

import me.tulio.yang.Locale;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ShowPlayerCommand extends BaseCommand {

	@Command(name = "showplayer", permission = "yang.command.showplayer")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /showplayer (player) or /showplayer (player) (target)");
			return;
		}

		Player target = Bukkit.getPlayer(args[0]);
		if (args.length == 1) {
			if (target == null) {
				new MessageFormat(Locale.PLAYER_NOT_FOUND
						.format(Profile.get(player.getUniqueId()).getLocale()))
						.send(player);
				return;
			}
			player.showPlayer(target);
			player.sendMessage(ChatColor.GOLD + "Showing you " + target.getName());
		}
		else {
			Player target2 = Bukkit.getPlayer(args[1]);
			if (target2 == null) {
				new MessageFormat(Locale.PLAYER_NOT_FOUND
						.format(Profile.get(player.getUniqueId()).getLocale()))
						.send(player);
				return;
			}
			target.showPlayer(target2);
			player.sendMessage(ChatColor.GOLD + "Showing " + target2.getName() + " to " + target.getName());
		}
	}
}
