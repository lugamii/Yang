package me.tulio.yang.event.game.command;

import me.tulio.yang.event.game.EventGame;
import me.tulio.yang.utilities.Cooldown;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class EventClearCooldownCommand extends BaseCommand {

	@Command(name = "event.clearcooldown", aliases = {"event.clearcd"}, permission = "yang.event.admin", inGameOnly = false)
	@Override
	public void onCommand(CommandArgs commandArgs) {
		CommandSender sender = commandArgs.getSender();

		EventGame.setCooldown(new Cooldown(0));
		sender.sendMessage(ChatColor.GREEN + "You cleared the event cooldown.");
	}
}
