package me.tulio.yang.essentials.command;

import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand extends BaseCommand {

	@Command(name = "rename", permission = "yang.command.rename")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please insert a valid name.");
			return;
		}

		if (player.getItemInHand() != null) {
			ItemStack itemStack = player.getItemInHand();
			ItemMeta itemMeta = itemStack.getItemMeta();
			StringBuilder string = new StringBuilder();
			for (String message : args) {
				string.append(message).append(" ");
			}
			itemMeta.setDisplayName(CC.translate(string.toString()));
			itemStack.setItemMeta(itemMeta);

			player.updateInventory();
			player.sendMessage(CC.GREEN + "You renamed the item in your hand.");
		} else {
			player.sendMessage(CC.RED + "There is nothing in your hand.");
		}
	}
}
