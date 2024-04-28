package me.tulio.yang.profile.deatheffects.commands;

import me.tulio.yang.profile.deatheffects.menu.DeathEffectMenu;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class DeathEffectCommand extends BaseCommand {

    @Command(name = "deatheffect", permission = "yang.command.deatheffects")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        new DeathEffectMenu().openMenu(player);
    }
}
