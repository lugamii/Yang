package me.tulio.yang.arena.command;

import me.tulio.yang.arena.selection.Selection;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class ArenaSelectionCommand extends BaseCommand {

	@Command(name = "arena.wand", aliases = {"arena.selection"}, permission = "yang.arena.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		if (player.getInventory().first(Selection.SELECTION_WAND) != -1) {
			player.getInventory().remove(Selection.SELECTION_WAND);
		} else {
			player.getInventory().addItem(Selection.SELECTION_WAND);
		}

		player.updateInventory();
	}
}
