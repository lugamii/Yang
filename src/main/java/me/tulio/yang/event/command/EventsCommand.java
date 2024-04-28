package me.tulio.yang.event.command;

import me.tulio.yang.event.Event;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.chat.ChatComponentBuilder;
import me.tulio.yang.utilities.chat.ChatHelper;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class EventsCommand extends BaseCommand {

	@Command(name = "events", permission = "yang.event.host")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(CC.GOLD + "Events:");
		for (Event events : Event.events) {
			ChatComponentBuilder builder = new ChatComponentBuilder("")
					.parse("&7- " + "&a" + events.getName());

			ChatComponentBuilder status = new ChatComponentBuilder("").parse("&7[&6STATUS&7]");
			status.attachToEachPart(ChatHelper.hover("&6Click to view this event's status."));
			status.attachToEachPart(ChatHelper.click("/event info"));

			builder.append(" ");

			for (BaseComponent component : status.create()) {
				builder.append((TextComponent) component);
			}

			player.spigot().sendMessage(builder.create());
		}
	}
}
