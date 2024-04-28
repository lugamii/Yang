package me.tulio.yang.essentials.command;

import com.google.common.collect.ImmutableMap;
import me.tulio.yang.Locale;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GameModeCommand extends BaseCommand {

	private static final ImmutableMap<String, GameMode> MAP = ImmutableMap.<String, GameMode>
					builder()
			.put("0", GameMode.SURVIVAL)
			.put("s", GameMode.SURVIVAL)
			.put("survival", GameMode.SURVIVAL)
			.put("1", GameMode.CREATIVE)
			.put("c", GameMode.CREATIVE)
			.put("creative", GameMode.CREATIVE)
			.build();

	@Command(name = "gamemode", aliases = {"gm"}, permission = "yang.command.gamemode")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please insert GameMode.");
			return;
		}

		GameMode gameMode = MAP.get(args[0].toLowerCase());

		if (gameMode == null) {
			player.sendMessage(CC.RED + "Please insert a valid GameMode.");
			return;
		}

		if (args.length == 1) {
			player.setGameMode(gameMode);
			player.updateInventory();
			player.sendMessage(CC.GOLD + "You updated your game mode.");
			return;
		}

		Player target = Bukkit.getPlayer(args[1]);
		if (target == null) {
			new MessageFormat(Locale.PLAYER_NOT_FOUND
					.format(Profile.get(player.getUniqueId()).getLocale()))
					.send(player);
			return;
		}

		target.setGameMode(gameMode);
		target.updateInventory();
		target.sendMessage(CC.GOLD + "Your game mode has been updated by " + player.getName());
	}
}
