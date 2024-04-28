package me.tulio.yang.chat.impl.format;

import me.tulio.yang.chat.impl.ChatFormat;
import me.tulio.yang.utilities.chat.CC;
import org.bukkit.entity.Player;

public class DefaultChatFormat implements ChatFormat {

    @Override
    public String format(Player sender, Player receiver, String message) {
        return CC.translate(sender.getDisplayName() + "&7:&f " +
            (sender.hasPermission("yang.chat.color") ? CC.translate(message) : CC.strip(message)));
    }

}
