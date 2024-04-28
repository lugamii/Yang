package me.tulio.yang.party.command.subcommands;

import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class PartyLeaveCommand extends BaseCommand {

	@Command(name = "party.leave", aliases = {"p.leave"})
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());

		if (profile.getParty() == null) {
			player.sendMessage(CC.RED + "You do not have a party.");
			return;
		}

		if (profile.getParty().getLeader().equals(player)) {
			profile.getParty().disband();
		} else {
			profile.getParty().leave(player, false);
		}
	}
}
