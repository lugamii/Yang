package me.tulio.yang.kit.command;

import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class KitRulesCommand extends BaseCommand {

	@Command(name = "kit.rules", permission = "yang.kit.admin")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();

		player.sendMessage(CC.CHAT_BAR);
		player.sendMessage(CC.translate("&2&lExample: &a(rule) (value)"));
		player.sendMessage(CC.translate(""));
		player.sendMessage(CC.translate(" &7- &abuild (boolean)"));
		player.sendMessage(CC.translate(" &7- &aspleef (boolean)"));
		player.sendMessage(CC.translate(" &7- &asumo (boolean)"));
		player.sendMessage(CC.translate(" &7- &aparkour (boolean)"));
		player.sendMessage(CC.translate(" &7- &ahealthregeneration (boolean)"));
		player.sendMessage(CC.translate(" &7- &aranked (boolean)"));
		player.sendMessage(CC.translate(" &7- &ashowhealth (boolean)"));
		player.sendMessage(CC.translate(" &7- &ahcf (boolean)"));
		player.sendMessage(CC.translate(" &7- &abridge (boolean)"));
		player.sendMessage(CC.translate(" &7- &aboxing (boolean)"));
		player.sendMessage(CC.translate(" &7- &ahitdelay (int)"));
		player.sendMessage(CC.translate(" &7- &akbprofile (string)"));
		player.sendMessage(CC.translate(" &7- &ahcftrap (boolean)"));
		player.sendMessage(CC.translate(" &7- &askywars (boolean)"));
		player.sendMessage(CC.translate(" &7- &anofood (boolean)"));
		player.sendMessage(CC.translate(" &7- &anofall (boolean)"));
		player.sendMessage(CC.translate(" &7- &asoup (boolean)"));
		player.sendMessage(CC.translate(" &7- &aeditoritems"));
		player.sendMessage(CC.translate(" &7- &aeffects (value's) [usage '/kit setrule (kit) effect help'"));
		player.sendMessage(CC.CHAT_BAR);
	}
}
