package me.tulio.yang.kit.command;

import me.tulio.yang.kit.Kit;
import me.tulio.yang.utilities.chat.ChatComponentBuilder;
import me.tulio.yang.utilities.chat.ChatHelper;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KitsCommand extends BaseCommand {

	@Command(name = "kits", permission = "yang.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(ChatColor.GOLD + "Kits");

		for (Kit kit : Kit.getKits()) {
			ChatComponentBuilder builder = new ChatComponentBuilder("")
					.parse("&7- " + (kit.isEnabled() ? "&a" : "&c") + kit.getName() +
							" &7(" + (kit.getGameRules().isRanked() ? "Ranked" : "Un-Ranked") + ")");

			ChatComponentBuilder status = new ChatComponentBuilder("").parse("&7[&6STATUS&7]");
			status.attachToEachPart(ChatHelper.hover("&6Click to view this kit's status."));
			status.attachToEachPart(ChatHelper.click("/kit status " + kit.getName()));

			builder.append(" ");

			for (BaseComponent component : status.create()) {
				builder.append((TextComponent) component);
			}

			player.spigot().sendMessage(builder.create());
		}
	}
}
