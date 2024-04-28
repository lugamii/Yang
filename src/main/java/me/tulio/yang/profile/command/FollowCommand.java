package me.tulio.yang.profile.command;

import me.tulio.yang.Locale;
import me.tulio.yang.Yang;
import me.tulio.yang.hotbar.Hotbar;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.profile.follow.Follow;
import me.tulio.yang.profile.visibility.VisibilityLogic;
import me.tulio.yang.utilities.PlayerUtil;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FollowCommand extends BaseCommand {

    @Command(name = "follow", aliases = {"f"}, permission = "yang.command.follow")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String s = commandArgs.getLabel();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Usage: /" + s + " <player> or /" + s + " exit");
            return;
        }

        Profile profile = Profile.get(player.getUniqueId());
        if (args[0].equalsIgnoreCase("exit")) {
            if (!profile.isFollowing()) {
                player.sendMessage(CC.RED + "You are not following anyone!");
                return;
            }
            profile.setFollow(null);
            Follow.getFollows().remove(player.getUniqueId());
            if (profile.getState() == ProfileState.FIGHTING) profile.getMatch().removeSpectator(player);
            profile.setState(ProfileState.LOBBY);
            PlayerUtil.reset(player);
            Yang.get().getEssentials().teleportToSpawn(player);
            Hotbar.giveHotbarItems(player);
            VisibilityLogic.handle(player);
            player.sendMessage(CC.GREEN + "You are no longer following anyone.");
        }
        else if (Bukkit.getPlayer(args[0]) != null) {
            Player target = Bukkit.getPlayer(args[0]);

            if (target.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(CC.RED + "You cannot follow yourself!");
                return;
            }

            if (profile.isBusy()) {
                player.sendMessage(CC.RED + "You are busy!");
                return;
            }

            if (profile.getState() == ProfileState.STAFF_MODE) {
                player.sendMessage(CC.RED + "You cannot follow while staff mode is enabled!");
                return;
            }

            if (profile.isFollowing()) {
                player.sendMessage(CC.RED + "You are already following " + profile.getFollow().getFollowedPlayer().getName() + "!");
                player.sendMessage(CC.RED + "Use /" + s + " exit to stop following.");
                return;
            }

            profile.follow(target);
            VisibilityLogic.handle(player, target);
            player.sendMessage(CC.GREEN + "You are now following " + target.getName() + ".");
            target.sendMessage(CC.GREEN + "You are now being followed by " + player.getName() + ".");
        }
        else {
            new MessageFormat(Locale.PLAYER_NOT_FOUND
                    .format(Profile.get(player.getUniqueId()).getLocale()))
                    .send(player);
        }
    }
}
