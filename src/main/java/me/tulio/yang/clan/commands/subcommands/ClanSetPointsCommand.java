package me.tulio.yang.clan.commands.subcommands;

import me.tulio.yang.clan.Clan;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class ClanSetPointsCommand extends BaseCommand {

    private final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Command(name = "clan.setpoints", permission = "yang.command.clan.setpoints")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.RED + "Please insert a Clan Name.");
            return;
        }
        else if (args.length == 1) {
            player.sendMessage(CC.RED + "Please insert a valid Integer.");
            return;
        }
        String clanName = args[0];
        String value = args[1];
        String deColored = ChatColor.stripColor(clanName);
        if(Clan.getByName(deColored) == null){
            player.sendMessage(CC.translate("&cThis clan doesn't exist."));
            return;
        }
        if (!pattern.matcher(value).matches()) {
            player.sendMessage(CC.translate("&cPlease use a Valid Num."));
            return;
        }
        Clan clan = Clan.getByName(deColored);
        int num = Integer.parseInt(value);
        clan.setPoints(num);
        clan.save();
        player.sendMessage(CC.translate(clan.getColoredName() + " &anew points is a " + num));
    }
}
