package me.tulio.yang.essentials.command;

import me.tulio.yang.arena.Arena;
import me.tulio.yang.arena.ArenaType;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.TPSUtil;
import org.bukkit.entity.Player;

public class ServerInfoCommand extends BaseCommand {

    @Command(name = "serverinfo", permission = "yang.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&5&lServer Information"));
        player.sendMessage("");
        player.sendMessage(CC.translate("&5Arenas"));
        player.sendMessage("");
        player.sendMessage(CC.translate("&dStandalone&7: &f" + getStandaloneSize() + " &7(&c" + getStandaloneAvailable() + "&7)"));
        player.sendMessage(CC.translate("&dShared&7: &f" + getSharedSize()));
        player.sendMessage("");
        player.sendMessage(CC.translate("&5&lPerformance"));
        player.sendMessage(CC.translate("&7[" + TPSUtil.getCoolestTPS(60) + "&7]"));
        player.sendMessage(CC.translate("&dFree Memory&7: &f" + getFreeMemory()));
        player.sendMessage(CC.CHAT_BAR);
    }

    private int getStandaloneAvailable() {
        int count = 0;
        for (Arena arena : Arena.getArenas()) {
            if (!arena.isBusy() && arena.isSetup() && arena.getType() == ArenaType.STANDALONE) {
                count++;
            }
        }
        return count;
    }

    private int getStandaloneSize() {
        int count = 0;
        for (Arena arena : Arena.getArenas()) {
            if (arena.isSetup() && arena.getType() == ArenaType.STANDALONE) {
                count++;
            }
        }
        return count;
    }

    private int getSharedSize() {
        int count = 0;
        for (Arena arena : Arena.getArenas()) {
            if (arena.isSetup() && arena.getType() == ArenaType.SHARED) {
                count++;
            }
        }
        return count;
    }

    private String getFreeMemory() {
        return Runtime.getRuntime().freeMemory() / 1048576 + " MB";
    }
}
