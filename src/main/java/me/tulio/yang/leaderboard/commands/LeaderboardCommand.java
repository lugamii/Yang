package me.tulio.yang.leaderboard.commands;

import me.tulio.yang.leaderboard.menu.LeaderBoardMenu;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

/*
This Proyect has been created
by TulioTriviño#6969
*/
public class LeaderboardCommand extends BaseCommand {

    @Command(name = "topelo", aliases = {"leaderboard"})
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        new LeaderBoardMenu(player).openMenu(player);
    }
}
