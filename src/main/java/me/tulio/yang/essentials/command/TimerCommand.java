package me.tulio.yang.essentials.command;

import me.tulio.yang.event.game.EventGame;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.Cooldown;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TimerCommand extends BaseCommand {

	@Command(name = "timer", permission = "yang.command.timer")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			for (String s : Arrays.asList(
					"&5&lTimer Command",
					"",
					"&dTimers: &f enderpearl, event",
					"",
					"&7/timer clear <time> <player(optional)>")) {
				player.sendMessage(CC.translate(s));
			}
		}
		else if (args[0].equalsIgnoreCase("clear")) {
			if (args.length == 1) {
				player.sendMessage(CC.translate("&dTimers: &f enderpearl, event"));
			}
			else if (args[1].equalsIgnoreCase("enderpearl")) {
				if (args.length == 3 && Bukkit.getPlayer(args[2]) != null) {
					Player target = Bukkit.getPlayer(args[2]);
					Profile profile = Profile.get(target.getUniqueId());
					if (profile.getEnderpearlCooldown() != null) {
						profile.getEnderpearlCooldown().setForceExpired(true);
						player.sendMessage(CC.translate("&d" + target.getName() + " Enderpearl cooldown cleared!"));
					}
					return;
				}
				Profile profile = Profile.get(player.getUniqueId());
				if (profile.getEnderpearlCooldown() != null) {
					profile.getEnderpearlCooldown().setForceExpired(true);
					player.sendMessage(CC.translate("&dEnderpearl cooldown cleared!"));
				}
			}
			else if (args[1].equalsIgnoreCase("event")) {
				EventGame.setCooldown(new Cooldown(0));
				player.sendMessage(CC.translate("&dEvent cooldown cleared!"));
			}
		}
	}
}
