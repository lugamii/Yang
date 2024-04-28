package me.tulio.yang.match.task;

import me.tulio.yang.Locale;
import me.tulio.yang.Yang;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MatchPearlCooldownTask extends BukkitRunnable {

	@Override
	public void run() {
		for (Player player : Yang.get().getServer().getOnlinePlayers()) {
			Profile profile = Profile.get(player.getUniqueId());

			if (profile.getState() == ProfileState.FIGHTING || profile.getState() == ProfileState.EVENT) {
				if (profile.getEnderpearlCooldown().hasExpired()) {
					if (!profile.getEnderpearlCooldown().isNotified()) {
						profile.getEnderpearlCooldown().setNotified(true);
						new MessageFormat(Locale.MATCH_ENDERPEARL_COOLDOWN_EXPIRED
								.format(profile.getLocale()))
								.send(player);
					}
				} else if (profile.getEnderpearlCooldown().isForceExpired()) {
					player.setExp(0.0F);
				}
				else {
					int seconds = Math.round(profile.getEnderpearlCooldown().getRemaining()) / 1_000;

					player.setLevel(seconds);
					player.setExp(profile.getEnderpearlCooldown().getRemaining() / 16_000.0F);
				}
			} else {
				if (player.getLevel() > 0) {
					player.setLevel(0);
				}

				if (player.getExp() > 0.0F) {
					player.setExp(0.0F);
				}
			}
		}
	}

}
