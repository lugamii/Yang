package me.tulio.yang.essentials.command;

import me.tulio.yang.Locale;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClearCommand extends BaseCommand {

	@Command(name = "clearinv", aliases = {"clear", "ci"}, permission = "yang.command.clearinv")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.getInventory().setContents(new ItemStack[36]);
			player.getInventory().setArmorContents(new ItemStack[4]);
			player.updateInventory();
			player.sendMessage(CC.GOLD + "You cleared your inventory.");
		}
		else {
		    Player target = Bukkit.getPlayer(args[0]);
		    if (target == null) {
				new MessageFormat(Locale.PLAYER_NOT_FOUND
						.format(Profile.get(player.getUniqueId()).getLocale()))
						.send(player);
		        return;
            }
            target.getInventory().setContents(new ItemStack[36]);
            target.getInventory().setArmorContents(new ItemStack[4]);
            target.updateInventory();
            target.sendMessage(CC.GOLD + "Your inventory has been cleared by " + player.getName());
        }
	}
}
