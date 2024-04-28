package me.tulio.yang.chat;

import me.clip.placeholderapi.PlaceholderAPI;
import me.tulio.yang.Yang;
import me.tulio.yang.chat.impl.ChatFormat;
import me.tulio.yang.clan.Clan;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.utilities.chat.CC;
import org.bukkit.entity.Player;

public class YangChatFormat implements ChatFormat {
    @Override
    public String format(Player sender, Player receiver, String message) {
        Profile senderProfile = Profile.get(sender.getUniqueId());
        if (Clan.getByPlayer(sender) != null) {
            if (Yang.get().isPlaceholderAPI())
                return PlaceholderAPI.setPlaceholders(sender, CC.translate(Yang.get().getMainConfig().getString("CHAT.CLAN_FORMAT")
                        .replace("{prefix}", Yang.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                        .replace("{suffix}", Yang.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                        .replace("{color}", senderProfile.getColor())
                        .replace("{player}", sender.getName())
                        .replace("{message}", (sender.hasPermission("yang.chat.color") ? CC.translate(message) : CC.strip(message)))
                        .replace("{clan}", Clan.getByPlayer(sender).getColoredName())));
            return CC.translate(Yang.get().getMainConfig().getString("CHAT.CLAN_FORMAT")
                    .replace("{prefix}", Yang.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                    .replace("{suffix}", Yang.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                    .replace("{color}", senderProfile.getColor())
                    .replace("{player}", sender.getName())
                    .replace("{message}", (sender.hasPermission("yang.chat.color") ? CC.translate(message) : CC.strip(message)))
                    .replace("{clan}", Clan.getByPlayer(sender).getColoredName()));
        }
        if (Yang.get().isPlaceholderAPI())
            return PlaceholderAPI.setPlaceholders(sender, CC.translate(Yang.get().getMainConfig().getString("CHAT.DEFAULT_FORMAT")
                    .replace("{prefix}", Yang.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                    .replace("{suffix}", Yang.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                    .replace("{color}", senderProfile.getColor())
                    .replace("{player}", sender.getName())
                    .replace("{message}", (sender.hasPermission("yang.chat.color") ? CC.translate(message) : CC.strip(message)))));
        return CC.translate(Yang.get().getMainConfig().getString("CHAT.DEFAULT_FORMAT")
                .replace("{prefix}", Yang.get().getRankManager().getRank().getPrefix(sender.getUniqueId()))
                .replace("{suffix}", Yang.get().getRankManager().getRank().getSuffix(sender.getUniqueId()))
                .replace("{color}", senderProfile.getColor())
                .replace("{player}", sender.getName())
                .replace("{message}", (sender.hasPermission("yang.chat.color") ? CC.translate(message) : CC.strip(message))));
    }
}