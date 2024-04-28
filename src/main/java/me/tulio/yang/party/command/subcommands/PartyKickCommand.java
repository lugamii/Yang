package me.tulio.yang.party.command.subcommands;

import me.tulio.yang.Locale;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PartyKickCommand extends BaseCommand {

	@Command(name = "party.kick", aliases = {"p.kick"})
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /party kick (player)");
			return;
		}

		Profile profile = Profile.get(player.getUniqueId());
		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			new MessageFormat(Locale.PLAYER_NOT_FOUND
					.format(profile.getLocale()))
					.send(player);
			return;
		}

		if (profile.getParty() == null) {
			player.sendMessage(CC.RED + "You do not have a party.");
			return;
		}

		if (!profile.getParty().getLeader().equals(player)) {
			player.sendMessage(CC.RED + "You are not the leader of your party.");
			return;
		}

		if (!profile.getParty().containsPlayer(target.getUniqueId())) {
			player.sendMessage(CC.RED + "That player is not a member of your party.");
			return;
		}

		if (player.equals(target)) {
			player.sendMessage(CC.RED + "You cannot kick yourself from your party.");
			return;
		}

		profile.getParty().leave(target, true);
	}
}
