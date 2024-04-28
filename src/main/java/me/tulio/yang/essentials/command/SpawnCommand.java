package me.tulio.yang.essentials.command;

import me.tulio.yang.Yang;
import me.tulio.yang.hotbar.Hotbar;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import me.tulio.yang.profile.visibility.VisibilityLogic;
import me.tulio.yang.utilities.PlayerUtil;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class SpawnCommand extends BaseCommand {

	@Command(name = "spawn", permission = "yang.command.spawn")
	@Override
	public void onCommand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Profile profile = Profile.get(player.getUniqueId());

		PlayerUtil.reset(player);
		profile.setState(ProfileState.LOBBY);
		Yang.get().getEssentials().teleportToSpawn(player);
		VisibilityLogic.handle(player);
		Hotbar.giveHotbarItems(player);

		player.sendMessage(CC.GREEN + "You teleported to this world's spawn.");
	}
}
