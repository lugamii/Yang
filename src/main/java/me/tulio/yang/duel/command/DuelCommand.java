package me.tulio.yang.duel.command;

import me.tulio.yang.Locale;
import me.tulio.yang.duel.DuelProcedure;
import me.tulio.yang.duel.DuelRequest;
import me.tulio.yang.duel.menu.DuelSelectKitMenu;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DuelCommand extends BaseCommand {

	@Command(name = "duel")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if (args.length == 0) {
			player.sendMessage(CC.RED + "Please usage: /duel (player)");
			return;
		}

		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			new MessageFormat(Locale.DUEL_PLAYER_NOT_FOUND.format(Profile.get(player.getUniqueId()).getLocale()))
					.send(player);
			return;
		}

		if (player.getUniqueId().equals(target.getUniqueId())) {
			new MessageFormat(Locale.DUEL_CANNOT_YOURSELF.format(Profile.get(player.getUniqueId()).getLocale()))
					.send(player);
			return;
		}

		Profile playerProfile = Profile.get(player.getUniqueId());
		Profile targetProfile = Profile.get(target.getUniqueId());

		if (playerProfile.isBusy()) {
			new MessageFormat(Locale.DUEL_CANNOT_DUEL_RIGHT_NOW.format(playerProfile.getLocale()))
					.send(player);
			return;
		}

		if (targetProfile.isBusy()) {
			new MessageFormat(Locale.DUEL_IS_BUSY.format(playerProfile.getLocale()))
					.add("{player}", target.getName())
					.send(player);
			return;
		}

		if (!targetProfile.getOptions().receiveDuelRequests()) {
			new MessageFormat(Locale.DUEL_DONT_RECEIVE_DUELS.format(playerProfile.getLocale()))
					.send(player);
			return;
		}

		DuelRequest duelRequest = targetProfile.getDuelRequest(player);

		if (duelRequest != null) {
			if (!playerProfile.isDuelRequestExpired(duelRequest)) {
				new MessageFormat(Locale.DUEL_ALREADY_SENT.format(playerProfile.getLocale()))
						.send(player);
				return;
			}
		}

		if (playerProfile.getParty() != null && targetProfile.getParty() == null) {
			new MessageFormat(Locale.DUEL_CANNOT_SEND_PARTY_DUEL.format(playerProfile.getLocale()))
					.send(player);
			return;
		}

		if (playerProfile.getParty() == null && targetProfile.getParty() != null) {
			new MessageFormat(Locale.DUEL_REQUEST_NO_PARTY.format(playerProfile.getLocale()))
					.send(player);
			return;
		}

		if (playerProfile.getParty() != null && targetProfile.getParty() != null) {
			if (playerProfile.getParty().equals(targetProfile.getParty())) {
				new MessageFormat(Locale.DUEL_REQUEST_EQUALS_PARTY.format(playerProfile.getLocale()))
						.send(player);
				return;
			}
			for (Player member : playerProfile.getParty().getListOfPlayers()) {
				Profile profileMember = Profile.get(member.getUniqueId());
				if (profileMember.getState() != ProfileState.LOBBY) {
					new MessageFormat(Locale.DUEL_NO_PLAYERS_ON_LOBBY_PARTY.format(playerProfile.getLocale()))
							.send(player);
					return;
				}
			}
		}

		DuelProcedure procedure = new DuelProcedure(player, target, playerProfile.getParty() != null);
		playerProfile.setDuelProcedure(procedure);

		new DuelSelectKitMenu().openMenu(player);
	}
}
