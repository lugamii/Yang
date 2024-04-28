package me.tulio.yang.match.command;

import com.lunarclient.bukkitapi.LunarClientAPI;
import me.tulio.yang.Locale;
import me.tulio.yang.match.Match;
import me.tulio.yang.match.lunar.BukkitAPI;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FocusCommand extends BaseCommand {

    @Command(name = "focus")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();
        Profile profile = Profile.get(player.getUniqueId());

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Usage: /focus (player)");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND.format(profile.getLocale())).send(player);
            return;
        }

        if (!BukkitAPI.isRunning(player)) {
            new MessageFormat(Locale.LUNAR_CLIENT_NOT_RUNNING.format(profile.getLocale())).send(player);
            return;
        }

        if (profile.getState() == ProfileState.FIGHTING) {
            Match match = profile.getMatch();

            if (match.getKit().getGameRules().isHcf()) {
                profile.setFocused(target);
                BukkitAPI.sendTeammates(player, target);
                new MessageFormat(Locale.DUEL_FOCUSED_MESSAGE.format(profile.getLocale())).send(player);
            } else {
                new MessageFormat(Locale.DUEL_NOT_HCF_KIT.format(profile.getLocale())).send(player);
            }
        } else {
            new MessageFormat(Locale.DUEL_NOT_IN_MATCH.format(profile.getLocale())).send(player);
        }
    }
}
