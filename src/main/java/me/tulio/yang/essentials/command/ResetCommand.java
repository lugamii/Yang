package me.tulio.yang.essentials.command;

import me.tulio.yang.Locale;
import me.tulio.yang.Yang;
import me.tulio.yang.hotbar.Hotbar;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.profile.visibility.VisibilityLogic;
import me.tulio.yang.utilities.PlayerUtil;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ResetCommand extends BaseCommand {

    @Command(name = "reset", permission = "yang.command.reset")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            PlayerUtil.reset(player);
            Profile.get(player.getUniqueId()).setState(ProfileState.LOBBY);
            Yang.get().getEssentials().teleportToSpawn(player);
            VisibilityLogic.handle(player);
            Hotbar.giveHotbarItems(player);
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
            return;
        }

        PlayerUtil.reset(target);
        Profile.get(target.getUniqueId()).setState(ProfileState.LOBBY);
        Yang.get().getEssentials().teleportToSpawn(target);
        VisibilityLogic.handle(target);
        Hotbar.giveHotbarItems(target);
    }
}
