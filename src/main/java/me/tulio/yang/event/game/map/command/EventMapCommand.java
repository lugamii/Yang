package me.tulio.yang.event.game.map.command;

import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.entity.Player;

public class EventMapCommand extends BaseCommand {

    private final String[][] HELP = new String[][] {
            new String[]{ "/event map create", "Create a event map" },
            new String[]{ "/event map delete", "Delete a event map" },
            new String[]{ "/event map addspawn", "Add spawn to event map" },
            new String[]{ "/event map status", "Get status of event map" }
    };

    public EventMapCommand() {
        super();
        new EventMapCreateCommand();
        new EventMapDeleteCommand();
        new EventMapSetSpawnCommand();
        new EventMapStatusCommand();
    }

    @Command(name = "event.map", permission = "yang.event.admin")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.GOLD + "Event Map Help");

        for (String[] command : HELP) {
            player.sendMessage(CC.BLUE + command[0] + CC.GRAY + " - " + CC.WHITE + command[1]);
        }

        player.sendMessage(CC.CHAT_BAR);
    }
}
