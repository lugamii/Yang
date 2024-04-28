package me.tulio.yang.essentials.command;

import me.tulio.yang.Yang;
import me.tulio.yang.utilities.chat.CC;
import me.tulio.yang.utilities.command.BaseCommand;
import me.tulio.yang.utilities.command.Command;
import me.tulio.yang.utilities.command.CommandArgs;
import me.tulio.yang.utilities.string.TPSUtil;
import me.tulio.yang.utilities.string.UUIDFetcher;
import org.bukkit.entity.Player;

public class YangCommand extends BaseCommand {

    @Command(name = "yang")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("reload") && player.hasPermission("yang.reload")) {
                long start = System.currentTimeMillis();
                Yang.get().getMainConfig().reload();
                Yang.get().getLang().reload();
                Yang.get().getLangConfig().reload();
                Yang.get().getHotbarConfig().reload();
                Yang.get().getArenasConfig().reload();
                Yang.get().getEventsConfig().reload();
                Yang.get().getKitsConfig().reload();
                Yang.get().getScoreboardConfig().reload();
                Yang.get().getTabLobbyConfig().reload();
                Yang.get().getTabSingleFFAFightConfig().reload();
                Yang.get().getTabSingleTeamFightConfig().reload();
                Yang.get().getTabPartyFFAFightConfig().reload();
                Yang.get().getTabPartyTeamFightConfig().reload();
                Yang.get().getTabEventConfig().reload();
                Yang.get().getOptionsConfig().reload();
                Yang.get().getDeathEffectsInvConfig().reload();
                Yang.get().getDiscordWebhookConfig().reload();
                Yang.get().getEssentials().setMotd(CC.translate(Yang.get().getLangConfig().getStringList("MOTD")));
                long finish = System.currentTimeMillis();
                player.sendMessage(CC.translate("&dYang reloaded &7(" + (finish - start) + "ms)"));
                return;
            }
            else if (args[0].equalsIgnoreCase("information") || args[0].equalsIgnoreCase("info")
                    || args[0].equalsIgnoreCase("dev")) {
                try {
                    if (UUIDFetcher.getUUIDOf("KingLugami").equals(player.getUniqueId())) {
                        player.sendMessage(CC.CHAT_BAR);
                        player.sendMessage(CC.translate("&dVersion: &r" + Yang.get().getDescription().getVersion()));
                        player.sendMessage("");
                        player.sendMessage(CC.translate("&dLicense: &r" + Yang.get().getLicense().getLicense()));
                        player.sendMessage(CC.translate("&dBuyer: &r" + Yang.get().getLicense().getBuyer()));
                        player.sendMessage(CC.translate("&dIP: &r" + Yang.get().getLicense().getIp()));
                        player.sendMessage(CC.translate("&dGenerate: &r" + Yang.get().getLicense().getGenerateDate()));
                        player.sendMessage("");
                        player.sendMessage(CC.translate("&dTPS: &7[" + TPSUtil.getCoolestTPS(60) + "&7] (&d" + TPSUtil.getTPS() + "&7)"));
                        player.sendMessage(CC.CHAT_BAR);
                        return;
                    }
                } catch (Exception e) {
                    player.sendMessage(CC.CHAT_BAR);
                    player.sendMessage(CC.translate("&d&lYang"));
                    player.sendMessage(CC.translate("&dAuthor &7- " + Yang.get().getDescription().getAuthors().toString().replace("[", "").replace("]", "")));
                    player.sendMessage(CC.translate("&dDiscord &7- discord.pandacommunity.org"));
                    player.sendMessage("");
                    player.sendMessage(CC.translate("&dVersion &7- " + Yang.get().getDescription().getVersion()));
                    player.sendMessage(CC.CHAT_BAR);
                }
            }
            return;
        }

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&d&lYang"));
        player.sendMessage(CC.translate("&dAuthor &7- " + Yang.get().getDescription().getAuthors().toString().replace("[", "").replace("]", "")));
        player.sendMessage(CC.translate("&dDiscord &7- discord.pandacommunity.org"));
        player.sendMessage("");
        player.sendMessage(CC.translate("&dVersion &7- " + Yang.get().getDescription().getVersion()));
        player.sendMessage(CC.CHAT_BAR);
    }
}